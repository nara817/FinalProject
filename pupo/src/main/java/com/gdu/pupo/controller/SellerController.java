package com.gdu.pupo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.pupo.service.AdminService;

import lombok.RequiredArgsConstructor;	

@RequiredArgsConstructor
@RequestMapping("/seller")
@Controller
public class SellerController {

  // field
  private final AdminService adminService;
    
  // 판매자-메인 이동
  @GetMapping("/seller.do")
  public String sellerMain(Model model) {
	  model.addAttribute("userCount", adminService.userCount());
	  model.addAttribute("sellerCount", adminService.sellerCount());
	  return "seller/sellerMain";
  }
  
  
  
}