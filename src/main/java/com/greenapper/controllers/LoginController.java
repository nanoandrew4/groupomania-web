package com.greenapper.controllers;

import com.greenapper.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller that provides the login page that is to be filled out in the front end, and then handled by Spring Security.
 */
@Controller
public class LoginController {

	@Autowired
	private SessionService sessionService;

	@GetMapping(value = {"/login"})
	public String login() {
		return "login";
	}

	// Login form with error
	@RequestMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}
}
