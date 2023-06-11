package com.gdu.pupo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller

@RequiredArgsConstructor
@RequestMapping("/customerCenter")
public class CustomerCenterController {
  
  @GetMapping("/centerHome.html")
  public String centerHome() {
    return "customerCenter/centerHome";  
  }
  
  @GetMapping("/faq.html")
  public String faq() {
    return "customerCenter/faq";  
  }
  

}
