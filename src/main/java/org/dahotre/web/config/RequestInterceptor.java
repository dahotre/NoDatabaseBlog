package org.dahotre.web.config;

import com.google.common.base.Strings;
import org.dahotre.web.common.Cookies;
import org.dahotre.web.common.EvernoteData;
import org.dahotre.web.helper.EvernoteSyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Intercepts all methods. Also adds a debug cookie, to print debug
 * variables on screen
 */
public class RequestInterceptor implements HandlerInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestInterceptor.class);

  @Autowired
  EvernoteSyncClient evernoteSyncClient;

  @Value("#{systemEnvironment['EVERNOTE_NAV_TAGS']}")
  private String evernoteNavTags;

  @Value("#{systemEnvironment['OWNER_NAME']}")
  private String ownerName;

  @Value("#{systemEnvironment['OWNER_ABOUT']}")
  private String ownerAbout;

  @Value("#{systemEnvironment['OWNER_IMAGE']}")
  private String ownerImage;

  @Value("#{systemEnvironment['APP_FACEBOOK_URL']}")
  private String appFacebookUrl;

  @Value("#{systemEnvironment['APP_NAME']}")
  private String appName;

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
      ModelAndView modelAndView) throws Exception {
    if (modelAndView != null) {
      addAppVariables(modelAndView);
      final int liveUpdateCount = evernoteSyncClient.getSyncState().getUpdateCount();
      if (liveUpdateCount > EvernoteData.updateCount || EvernoteData.globalTags == null) {
        EvernoteData.globalTags = evernoteSyncClient.listTags();
        EvernoteData.updateCount = liveUpdateCount;
      }

      modelAndView.addObject("tags", EvernoteData.globalTags);
      modelAndView.addObject("navTags", EvernoteData.globalTags.stream()
          .filter(tag -> navTags().contains(tag.getName()))
          .collect(Collectors.toList()));
    }
  }

  private void addAppVariables(ModelAndView modelAndView) {
    modelAndView.addObject("ownerName", ownerName)
        .addObject("ownerAbout", ownerAbout)
        .addObject("ownerImage", ownerImage)
        .addObject("appName", appName)
        .addObject("appFacebookUrl", appFacebookUrl);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {}

  private List<String> navTags() {
    return Arrays.asList(evernoteNavTags.split(","));
  }
}
