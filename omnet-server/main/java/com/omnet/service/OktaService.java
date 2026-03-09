package com.omnet.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.omnet.common.Text;
import com.omnet.config.OktaAdminProperties;
import com.omnet.config.OktaUserProperties;
import com.omnet.data.entity.OmnetUser;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.api.UserApi;
import com.okta.sdk.resource.client.ApiClient;
import com.okta.sdk.resource.client.ApiException;
import com.okta.sdk.resource.model.CreateUserRequest;
import com.okta.sdk.resource.model.PasswordCredential;
import com.okta.sdk.resource.model.User;
import com.okta.sdk.resource.model.UserCredentials;
import com.okta.sdk.resource.model.UserProfile;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

/**
 * This class implements a user service bean.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Slf4j
@Service
public class OktaService {

  @Autowired
  private OktaAdminProperties oktaAdminProperties;

  @Autowired
  private OktaUserProperties oktaUserProperties;

  /**
   * Default constructor.
   */
  public OktaService() {

  }

  /**
   * Method that creates an OKTA user on the remote OKTA service configured in the OKTA parameters
   * within the parameter configuration.
   * 
   * @param user {@link OmnetUser} containing user values.
   * @return Map.Entry containing boolean true indicating success, false otherwise with additional diagnostic information in String.
   */
  public Map.Entry<Boolean, String>  createUser(OmnetUser user) {

    ApiClient client = Clients.builder()
        .setOrgUrl(oktaAdminProperties.getIssuerUri())
        .setClientCredentials(new TokenClientCredentials(oktaAdminProperties.getApiToken()))
        .build();

    UserProfile profile = new UserProfile();
    profile.setEmail(user.getEmail());
    profile.setLogin(user.getEmail());
    profile.setFirstName(user.getUsername());
    profile.setLastName(user.getSurname());

    PasswordCredential pwd = new PasswordCredential();
    pwd.setValue(user.getPassword());

    UserCredentials credentials = new UserCredentials();
    credentials.setPassword(pwd);

    CreateUserRequest request = new CreateUserRequest();
    request.setProfile(profile);
    request.setCredentials(credentials);

    UserApi userApi = new UserApi(client);

    try {

      User oktaUser = userApi.createUser(request, true, false, 
          null, new HashMap<String, String>());

      if (log.isDebugEnabled()) {

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oktaUser);

        log.debug(json);
      }

      return Map.entry(true, oktaUser.getId());

    } catch (ApiException | JsonProcessingException e) {

      String diagnostic = (e instanceof ApiException) ? getOktaDiagnostic((ApiException) e) : "";

      log.error("Create user failed " + ((diagnostic == null || 
          diagnostic.isBlank()) ? e.getMessage() : (e.getMessage() + ": " + diagnostic)));

      log.error(e.getMessage(), e);

      return Map.entry(false, diagnostic);
    }
  }

  /**
   * Method that logs an OKTA user in on the remote OKTA service configured in the OKTA parameters
   * within the parameter configuration.
   * 
   * @param user {@link OmnetUser} containing user values.
   * @return Map.Entry containing boolean true indicating success, false otherwise with additional diagnostic information in String.
   */
  public Map.Entry<Boolean, String>  loginUserSession(OmnetUser user) {

    try {

      RestTemplate restTemplate = new RestTemplate();

      Map<String, Object> requestBody = new HashMap<>();

      requestBody.put("password", user.getPassword());
      requestBody.put("username", user.getEmail());

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

      if (log.isInfoEnabled()) {

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

        log.info(json); 
      }

      String url = oktaAdminProperties.getIssuerUri() + Text._URL_OKTA_AUTH_PATH;

      ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

      ObjectMapper mapper = new ObjectMapper();

      JsonNode root = mapper.readTree(response.getBody());

      if (root != null && "SUCCESS".equals(root.path("status").asText())) {

        String sessionToken = root.path("sessionToken").asText();

        log.info("Logged in with session token: " + sessionToken);
        
        return Map.entry(true, sessionToken);
      } 

      return Map.entry(false, "Authentication session failed: " + (root != null ?
          root.path("status").asText() : "null response"));

    } catch (ApiException | JsonProcessingException e) {

      log.error("Login session failed", e);

      return Map.entry(false, e.getMessage());
    }
  }

  /**
   * Method that logs an OKTA user and obtains an OKTA access token from the OKTR service.
   * 
   * @param user {@link OmnetUser} containing user values.
   * @return Map.Entry containing boolean true indicating success, false otherwise with additional diagnostic information or access token in String.
   */
  public Map.Entry<Boolean, String> loginUserAccess(OmnetUser user) {

    try {

      RestTemplate restTemplate = new RestTemplate();

      MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
      requestBody.add("grant_type", "password");
      requestBody.add("scope", "openid");
      requestBody.add("username", user.getEmail());
      requestBody.add("client_id", oktaUserProperties.getClientId());
      requestBody.add("password", user.getPassword());
      
      String codeVerifier = generateCodeVerifier();   // random 43-128 char base64url string
      String codeChallenge = generateCodeChallenge(codeVerifier); // SHA-256 hash, base64url encoded

      requestBody.add("code_verifier", codeVerifier);
      requestBody.add("code_challenge", codeChallenge);
      requestBody.add("code_challenge_method", "S256");
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      
      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

      if (log.isInfoEnabled()) {

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

        log.info(json);
      }

      String url = oktaUserProperties.getIssuerUri() + Text._URL_OKTA_OAUTH2_TOKEN_PATH;

      ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

      log.error("Okta token response: status={}, body={}", response.getStatusCode(), response.getBody());
      
      ObjectMapper mapper = new ObjectMapper();

      JsonNode root = mapper.readTree(response.getBody());

      if (root != null && root.has("access_token")) {

        String accessToken = root.path("access_token").asText();

        log.info("Logged in with access token: " + accessToken);
        
        return Map.entry(true, accessToken);
      } 

      return Map.entry(false, "Access token login failed: " + response.getBody());

    } catch (Exception e) {

      log.error("Access token login failed", e);

      return Map.entry(false, e.getMessage());
    }
  }
  
  private String getOktaDiagnostic(ApiException oe) {

    try {

      ObjectMapper mapper = new ObjectMapper();

      JsonNode root = mapper.readTree(oe.getResponseBody());

      String summary = root.path("errorSummary").asText();

      StringBuilder causes = new StringBuilder();

      for (JsonNode cause : root.path("errorCauses")) {

        causes.append(cause.path("errorSummary").asText()).append("; ");
      }

      return summary + " - " + causes.toString();

    } catch (Exception e) {

      log.error(e.getMessage(), e);

      return "";
    }
  }
  
  private String generateCodeVerifier() {

    SecureRandom sr = new SecureRandom();
    
    byte[] bytes = new byte[32];
    
    sr.nextBytes(bytes);
    
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  private String generateCodeChallenge(String verifier) throws Exception {
    
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    
    byte[] digest = md.digest(verifier.getBytes(StandardCharsets.US_ASCII));
    
    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
  }
}

