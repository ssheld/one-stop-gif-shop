package com.ssheld.onestopgifshop.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: Stephen Sheldon
 **/

@Controller
public class AuthenticationController {

    @GetMapping(value = "/login")
    protected String login(HttpServletRequest request, HttpServletResponse response) {

        return "redirect:/callback";
    }
}
