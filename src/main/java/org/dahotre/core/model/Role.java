package org.dahotre.core.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Name of different authorities
 */
public enum Role implements GrantedAuthority {
  ;

  @Override
  public String getAuthority() {
    return name();
  }
}
