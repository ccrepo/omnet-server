package com.omnet.data.entity;

import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.omnet.common.Numbers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.mail.internet.InternetAddress;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * This class implements a user object.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Slf4j
@Entity
@Getter
@Setter
@Table(
  name = "omnet_user",
  uniqueConstraints = { @UniqueConstraint(columnNames = {"username", "surname"}), @UniqueConstraint(columnNames = {"email"})}
)
public class OmnetUser implements UserDetails {
 
  private static final long serialVersionUID = 1L;

  /**
   * Default  constructor.
   */
  public OmnetUser() {
  }
  
  /**
   * Constructor.
   *
   * @param username account name for user.
   * @param surname surname for user.
   * @param email email for the user.
   * @param password password for the user.
   * @param vehicles LIst containing Ids for vehicles which user wishes to use.
   */
  public OmnetUser(String username, String surname, String email, String password, List<OmnetVehicle> vehicles) {    
    
    this.id = UUID.randomUUID();

    this.username = username;

    this.surname = surname;

    this.email = email;
    
    this.password = password;
 
    this.isLoggedIn = false;
    
    this.vehicles = new ArrayList<>(vehicles);
  }
  
  /**
   * User id.
   */
  @Id
  @Column(nullable = false, unique = true, name = "id")
  private UUID id;

  /**
   * User user name.
   */
  @Column(nullable = false, name = "username", length = 25)
  private String username;

  /**
   * User surname.
   */
  @Column(nullable = false, name = "surname", length = 25)
  private String surname;

  /**
   * User password.
   */
  @Column(nullable = false, name = "password", length = 40)
  private String password;

  /**
   * User password.
   */
  @Column(nullable = false, name = "email", length = 255)
  private String email;

  /**
   * User status determining whether the user is logged in.
   */
  @Column(nullable = false, name = "is_logged_in")
  private boolean isLoggedIn;
  
  /**
   * List of OmnetVehicle vehicles which the user wants to use.
   */
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<OmnetVehicle> vehicles;
  
  /**
   * Method to override equals.
   */
  public boolean equals(Object other) {

    if (this == other) {
      
      return true;
    }

    if (!(other instanceof OmnetUser)) {
      
      return false;
    }
    
    return id != null && ((OmnetUser) other).id.equals(id);
  }
  
  /**
   * Method to generate hash code.
   */
  public int hashCode() {
  
    if (id == null) {
      
      return Numbers._CONSTANT_NUMBER_DEFAULT_HASHCODE;
    }

    return id.hashCode();
  }

  /**
   * Method to return permissions. 
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    return Collections.emptyList();
  }
  
  /**
   * Method to get full name composed of user name and surname.
   * 
   * @return String containing user full name.
   */
  public String getFullname() {
    
    return getUsername() + " " + getSurname();
  }
  
  /**
   * Method to get email address
   * 
   * @param username to be used in generated email address.
   * @param surname to be used in generated email address.
   * @param domain to be used in generated email address.
   * @return String containing created email address.
   */
  public static String generateEmail(String username, String surname, String domain) {

    String email =
        username.toLowerCase() + 
        "." + 
        surname.toLowerCase() + 
        "@" + 
        domain;

    email = email.toLowerCase().replaceAll("[^a-z@.]", "");
    
    if (email.length() >= Numbers._MAX_OKTA_EMAIL_LENGTH) { 
      
      throw new IllegalArgumentException("Invalid email is too long at " + email.length() + " length: " + email);
    }
    
    if (email.chars().filter(ch -> ch == '.').count() > 2) {
      
      throw new IllegalArgumentException("Invalid email contains more than one '.': " + email);
    }
    
    try {
    
      InternetAddress addr = new InternetAddress(email);
      
      addr.validate();
  
    } catch (Exception e) {
    
      throw new IllegalArgumentException("Invalid email generated: " + email);
    }
    
    return email;
  }
}

