package com.lhm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

	@RequestMapping(value = "/forware")
	public String homead() {

		return "/forware";
	}

	@RequestMapping(value = "/login")
	public String login() {

		return "/login";
	}
}
