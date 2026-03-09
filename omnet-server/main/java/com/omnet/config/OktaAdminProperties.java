package com.omnet.config;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientProperties;

/**
 * This class implements an OKTA admin properties values service bean.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Component
public class OktaAdminProperties extends OktaProperties {

  private final static String prefix = "okta-admin";
      
  private final static String apiTokenPropertyName = prefix + ".api.token";

  private final static String apiDomainNamePropertyName = "omnet." + prefix + ".domain.name";

  private final static String apiIsActivePropertyName = "omnet." + prefix + ".domain.is-active";

  @Value("${" + apiTokenPropertyName + "}")
  private String apiToken;

  @Value("${" + apiDomainNamePropertyName + "}")
  private String oktaDomainName;

  @Value("${" + apiIsActivePropertyName + "}")
  private String oktaIsActive;
  
  private final OAuth2ClientProperties properties;

  private String oktaIsActiveValue;


  /**
   * Constructor.
   * 
   * @param properties properties object containing property values.
   */
  public OktaAdminProperties(OAuth2ClientProperties properties) {

    this.properties = properties;
  }

  @PostConstruct void setOktaIsActiveValue() {
    
    this.oktaIsActiveValue = oktaIsActive.toLowerCase();
  }
  
  /**
   * Method returns OKTA API ID.
   * 
   * @return String containing API ID.
   */
  public String getClientId() {

    return properties.getRegistration().get(prefix).getClientId();
  }

  /**
   * Method returns OKTA API secret.
   * 
   * @return String containing API secret.
   */
  public String getClientSecret() {

    return properties.getRegistration().get(prefix).getClientSecret();
  }

  /**
   * Method returns OKTA API URL.
   * 
   * @return String containing API URL.
   */
  public String getIssuerUri() {

    return properties.getProvider().get(prefix).getIssuerUri();
  }

  /**
   * Method returns OKTA permission scopes.
   * 
   * @return {@link Set<String>} containing permission scopes.
   */
  public Set<String> getScope() {

    return properties.getRegistration().get(prefix).getScope();
  }

  /**
   * Method returns OKTA API token.
   * 
   * @return String containing API token.
   */
  public String getApiToken() {

    return apiToken;
  }

  /**
   * Method returns OKTA domain name.
   * 
   * @return String containing domain name.
   */
  public String getOktaDomainName() {

    return oktaDomainName;
  }

  /**
   * Method returns OKTA is active flag.
   * 
   * @return boolean indicating whether OKTA layer should be active or not.
   */
  public boolean getOktaIsActive() {
    
    return super.getOktaIsActive(apiIsActivePropertyName, oktaIsActiveValue);
  }

  /**
   * Method which checks whether all String parameters have been set.
   *
   * @throws IllegalStateException if invalid parameter / value found.
   */
  @PostConstruct
  public void validate() {
    
    List<Map.Entry<String, String>> settings = List.of(
        Map.entry(apiToken, apiTokenPropertyName),
        Map.entry(oktaDomainName, apiDomainNamePropertyName),
        Map.entry("getClientId", getClientId()),
        Map.entry("getClientSecret", getClientSecret()),
        Map.entry("getIssuerUri", getIssuerUri()),
        Map.entry("getApiToken", getApiToken()),
        Map.entry("getOktaDomainName", getOktaDomainName())
        );
    
    super.validate(settings);
  }
}
