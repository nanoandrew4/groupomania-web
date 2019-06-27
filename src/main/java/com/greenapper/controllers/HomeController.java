package com.greenapper.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that exists to redirect from the page root to the default page, which is '/campaigns'.
 */
@Controller
public class HomeController {

	@GetMapping("/")
	public String redirectToHome() {
		return "redirect:/campaigns";
	}
}
