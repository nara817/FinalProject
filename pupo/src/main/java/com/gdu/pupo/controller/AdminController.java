package com.gdu.pupo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.pupo.domain.UserDTO;
import com.gdu.pupo.service.UserService;

import lombok.RequiredArgsConstructor;	

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

  // field
  private final UserService userService;

  
  // 관리자/회원조회
  /*
  @GetMapping("/admin.form")
  public String adminUserList(HttpSession session, Model model) {
      // 로그인이 되어 있는지 확인
      if (session.getAttribute("loginId") == null) {
          // 로그인되어 있지 않으면 로그인 페이지로 리다이렉트
          return "redirect:/user/login.form";
      }
      // 세션에서 사용자의 ID를 가져옴
      String id = (String) session.getAttribute("loginId");
      // 서비스를 통해 사용자 정보를 가져옴
      UserDTO user = userService.getUserById(id);
      // 사용자 정보를 모델에 추가
      model.addAttribute("user", user);
      return "admin/user";
  }
  */
 
    // 관리자/회원조회
  @GetMapping("/admin.form")
  public String adminUserList(HttpSession session, Model model) {
      if (session.getAttribute("loginId") == null) {
          return "redirect:/user/login.form";
      }

      String id = (String) session.getAttribute("loginId");
      UserDTO user = userService.getUserById(id);

      if (user.getSellerCheck() == 2 ) {
          model.addAttribute("user", user);
          return "admin/user";
      } else {
          return "redirect:/user/mypage";
      }
  }
  
  
  
}