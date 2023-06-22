package com.gdu.pupo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcController {

	@GetMapping(value = {"/", "/index.do"})
	public String welcome() {
		return "index";  // src/main/resources/templates/index.html
	}
	
	@GetMapping("theme/admin/modules-sweet-alert.html")
  public String admin() {
    return "theme/admin/modules-sweet-alert";  // src/main/resources/templates/index.html
  }
	
}