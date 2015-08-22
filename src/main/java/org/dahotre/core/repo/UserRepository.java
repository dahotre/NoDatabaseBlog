package org.dahotre.core.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User CRUD
 */
public class UserRepository {
  private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

  public UserDetails find(String username, String credentials) {
    //TODO implement this
    return null;
  }
}
