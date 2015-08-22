package org.dahotre.web.controller;

import org.dahotre.web.common.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controls home page as well as other one-off endpoints that won't need CRUD actions
 */
@Controller
@RequestMapping("/")
public class HomeController {

  @RequestMapping("")
  public ModelAndView getHome() {
    return new ModelAndView(ViewNames.HOME)
        .addObject("hello", "Hello world!");
  }
}
