package com.omnet.data.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import com.omnet.common.Numbers;

/**
 * This class implements a vehicle object.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Entity
@Getter
@Setter
@Table(
    name = "omnet_vehicle",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "registration"})
)
public class OmnetVehicle {
  
  /**
   * Default constructor.
   */
  public OmnetVehicle() {    
  }

  /**
   * Constructor.
   *
   * @param registration vehicle registration.
   * @param user vehicle OmnetUser user object.
   */
  public OmnetVehicle(String registration, OmnetUser user) {    
    
    this.id = UUID.randomUUID();

    this.registration = registration;
    
    this.user = user;
  }

  @Id
  @Column(nullable = false, unique = true, name = "id")
  private UUID id;

  @Column(nullable = false, name = "registration", length = 255)
  private String registration;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private OmnetUser user; 
  
  public boolean equals(Object other) {

    if (this == other) {
      
      return true;
    }

    if (!(other instanceof OmnetVehicle)) {
      
      return false;
    }
    
    return id != null && ((OmnetVehicle) other).id.equals(id);
  }

  public int hashCode() {
    
    if (id == null) {
    
      return Numbers._CONSTANT_NUMBER_DEFAULT_HASHCODE;
    }
    
    return id.hashCode();
  }
}
