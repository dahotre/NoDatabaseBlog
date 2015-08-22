package org.dahotre.web.config;

import org.dahotre.core.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Config for spring security
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private static final Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);

  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth, UserRepository userRepository) throws Exception {

    auth.authenticationProvider(new AbstractUserDetailsAuthenticationProvider() {
      @Override
      protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //not implemented
      }

      @Override
      protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return userRepository.find(username, (String) authentication.getCredentials());
      }
    });
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    LOG.debug("Configuring HttpSecurity..");
    http
        .authorizeRequests()
        .antMatchers("/resources/**", "/user/**", "/", "/api/**")
        .permitAll() //Permit these patterns without authorization
        .anyRequest().authenticated() //Everything else has to be authorized
        .and()
        .formLogin()
        .loginPage("/user/login").permitAll()
        //Use this custom login page, and allow to hit this page without authentication
        .and()
        .logout()
        .permitAll()  //Allow logout without auth
        .and()
        .rememberMe()
        .and()
        .httpBasic();
  }
}
