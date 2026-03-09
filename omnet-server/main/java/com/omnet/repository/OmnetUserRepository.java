package com.omnet.repository;

import com.omnet.data.entity.OmnetUser;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This class implements an {@link OmnetUser} repository object.
 *
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Repository
public interface OmnetUserRepository extends JpaRepository<OmnetUser, String> {

  /**
   * Method checks whether the a User exists in the database with an Id equal to the parameter Id.

   * @param id User id value to be used in search.
   * @return boolean true if User with id exists in database, false otherwise,
   */
  boolean existsById(UUID id);
 
  /**
   * Method searches for and returns a matching {@link OmnetUser}  using the supplied Id as a key.

   * @param id User id value to be used in search.
   * @return Optional {@link OmnetUser} if found, Optional.empty() otherwise,
   */
  Optional<OmnetUser> findById(UUID id);
  
  /**
   * Method searches for and returns a matching OmnetUser using the supplied username as a key.

   * @param username User user name value to be used in search.
   * @return Optional {@link OmnetUser} if found, Optional.empty() otherwise,
   */
  @Query(value = "SELECT * FROM omnet_user WHERE LOWER(username) = LOWER(:username)", nativeQuery = true)
  Optional<OmnetUser> findByUsername(@Param("username") String username);
  
  /**
   * Method searches for and returns a matching OmnetUser using the supplied user name and surname as a key.
   * 
   * @param username User user name value to be used in search.
   * @param surname User surname value to be used in search.
   * @return Optional {@link OmnetUser} if found, Optional.empty() otherwise,
   */
  @Query(value = "SELECT * FROM omnet_user WHERE LOWER(username) = LOWER(:username) AND LOWER(surname) = LOWER(:surname) ", nativeQuery = true)
  Optional<OmnetUser> findByUsernameAndSurname(@Param("username") String username, @Param("surname") String surname);  
  
  /**
   * Method searches for and returns a matching OmnetUser using the supplied user email as a key.
   * 
   * @param email User user email value to be used in search.
   * @return Optional {@link OmnetUser} if found, Optional.empty() otherwise,
   */
  @Query(value = "SELECT * FROM omnet_user WHERE LOWER(email) = LOWER(:email)", nativeQuery = true)
  Optional<OmnetUser> findByEmail(@Param("email") String email);
}
