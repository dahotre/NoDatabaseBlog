package org.dahotre.web.config;

import com.google.common.base.Strings;
import org.dahotre.web.common.Cookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Intercepts all methods. Also adds a debug cookie, to print debug
 * variables on screen
 */
public class RequestInterceptor implements HandlerInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    addDebugCookie(request, response);
    return true;
  }

  private void addDebugCookie(HttpServletRequest request, HttpServletResponse response) {
    String debugParam = request.getParameter("debug");
    if (Strings.isNullOrEmpty(debugParam)) {
      // Do nothing
    } else if (debugParam.equalsIgnoreCase("true")) {
      response.addCookie(Cookies.DEBUG_TRUE);
    } else {
      response.addCookie(Cookies.DEBUG_FALSE);
    }
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {}

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {}
}
