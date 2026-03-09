package com.omnet.data.entity;

import java.util.UUID;

import com.omnet.common.Numbers;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

/**
 * This class implements session object.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Entity
@Getter
@Setter
@Table(
    name = "omnet_session", 
    uniqueConstraints = @UniqueConstraint(columnNames = {"vehicle_id"})
)
public class OmnetSession {
  
  /**
   * Default constructor.
   */
  public OmnetSession() {
  }
  
  /**
   * Constructor.
   *
   * @param startTime start-time of session.
   * @param endTime end-time of session.
   * @param vehicle vehicle booked in session.
   */
  public OmnetSession(ZonedDateTime startTime, ZonedDateTime endTime, OmnetVehicle vehicle) {    
    
    this.id = UUID.randomUUID();

    this.startTime = startTime;
    
    this.endTime = endTime;
    
    this.vehicle = vehicle;
  }

  @Id
  @Column(nullable = false, unique = true, name = "id")
  private UUID id;

  @Column(nullable = false, name = "start_time")
  private ZonedDateTime startTime;

  @Column(nullable = false, name = "end_time")
  private ZonedDateTime endTime;

  @OneToOne
  @JoinColumn(name = "vehicle_id", nullable = false)
  private OmnetVehicle vehicle;
  
  public boolean equals(Object other) {

    if (this == other) {
    
      return true;
    }
    
    if (!(other instanceof OmnetSession)) {
      
      return false;
    }
    
    return id != null && ((OmnetSession) other).id.equals(id);
  }
  
  public int hashCode() {
    
    if (id == null) {
      
      return Numbers._CONSTANT_NUMBER_DEFAULT_HASHCODE;
    }

    return id.hashCode();
  }
}
