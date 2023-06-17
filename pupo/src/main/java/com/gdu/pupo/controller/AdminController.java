package com.gdu.pupo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.pupo.service.AdminService;

import lombok.RequiredArgsConstructor;	

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

  // field
  private final AdminService adminService;
  
  //  관리자-회원관리 페이지 이동
  @GetMapping("/adminUserList.html")
  public String adminUserList(HttpServletRequest request, Model model) {
	  //List<UserDTO> user = adminService.userList();
      //model.addAttribute("user", user);		
      adminService.getlistUsingPagination(request, model);

	  return "admin/adminUserList";
  }

  // 관리자-메인 이동
  @GetMapping("/adminMain.html")
  public String adminMain(Model model) {
	  model.addAttribute("userCount", adminService.userCount());
	  model.addAttribute("sellerCount", adminService.sellerCount());
	  return "admin/adminMain";
  }
  // 관리자-회원관리 페이지 정렬
  
  // 관리자-쿠폰
  @GetMapping("/couponList.html")
  public String couponList(Model model) {
	  return "admin/couponList";
  }
  
  
}