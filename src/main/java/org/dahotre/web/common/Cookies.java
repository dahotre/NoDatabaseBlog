package org.dahotre.web.common;

import javax.servlet.http.Cookie;

/**
 * Values of static cookies
 */
public class Cookies {

  public static final Cookie DEBUG_TRUE;
  public static final Cookie DEBUG_FALSE ;

  static  {
    Cookie trueDebugCookie = new Cookie("debug", "true");
    trueDebugCookie.setMaxAge(300);
    DEBUG_TRUE = trueDebugCookie;

    Cookie falseDebugCookie = new Cookie("debug", "false");
    falseDebugCookie.setMaxAge(0);
    DEBUG_FALSE = falseDebugCookie;
  }
}
