package com.gdu.pupo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.pupo.domain.UserDTO;
import com.gdu.pupo.service.RegularService;
import com.gdu.pupo.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/regular")
@Controller
public class RegularController {

  private final RegularService regularService;
  private final UserService userService;
  
  @GetMapping("/regularMain.html")
  public String regularMain() {
    return "regular/regularMain";
  }
  
  @GetMapping("/regularAddPage.html") // 상품등록 페이지
  public String regularAddPage(Model model) {
    regularService.getRegCategory(model);
    return "regular/regularAddPage";
  }
    
  @PostMapping("/regularAdd.do") // 상품 등록
  public String addRegular(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) {
    int addResult = regularService.addRegular(multipartRequest);
    redirectAttributes.addFlashAttribute("addResult", addResult);
    return "redirect:/regular/regularMain.html";
  }
  
  @GetMapping("/regularList.do") // 리스트 불러오기
  public String regularList (HttpServletRequest request, Model model) {
    regularService.regularList(request, model);
    return "/regular/regularList";
  }
  
  // 메인 이미지  보여주기
  @GetMapping("/regularMainDisplay.do")
  public ResponseEntity<byte[]> regularDsiplay(@RequestParam("regularNo") int regularNo){
    return regularService.regularMainDisplay(regularNo);
  }
  
  @GetMapping("/regularDetail.do")
  public String regularDetail(@RequestParam("regularNo") int regularNo, Model model) {
    model.addAttribute("regularDetail", regularService.regularDetail(regularNo, model));
    return "/regular/regularDetail";
  }
  @GetMapping("/regularDetailDisplay.do")
  public ResponseEntity<byte[]> regularMainDsiplay(@RequestParam("regularNo") int regularNo){
    return regularService.regularMainDisplay(regularNo);
  }
  
  @PostMapping("/regularPayPage.do")
  public String regularPayPage(HttpServletRequest request, Model model) {
    int regularNo = Integer.parseInt(request.getParameter("regularNo"));
    String loginId = request.getParameter("loginId");
    model.addAttribute("regularDetail", regularService.regularDetail(regularNo, model));
    UserDTO userDTO = userService.getUserById(loginId);
    model.addAttribute("loginId", userDTO);
    return "regular/regularPayPage";
  }
  
  @PostMapping("/regularPurchase.do")
  public String regularPurchase(HttpServletRequest request, Model model) {
    model.addAttribute("regPurchase", regularService.regularPurchase(request, model));
    return "regular/regularPayDone";
  }
  
  @PostMapping("/addCategory.do")
  public String addCategory(HttpServletRequest request) {
  regularService.addRegCategory(request);
  return "redirect:/regular/regularAddPage.html";
  }
}

