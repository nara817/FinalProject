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

import com.gdu.pupo.service.RegularService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/regular")
@Controller
public class RegularController {

  private final RegularService regularService;
  
  @GetMapping("/regularMain.html")
  public String regularMain() {
    return "regular/regularMain";
  }
  
  @GetMapping("/regularAddPage.html") // 상품등록 페이지
  public String regularAddPage() {
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
  
  @GetMapping("/regularDisplay.do")
  public ResponseEntity<byte[]> regularDsiplay(@RequestParam("regularNo") int regularNo){
    return regularService.regularDisplay(regularNo);
  }
  
  @GetMapping("/regularDetail.do")
  public String regularDetail(@RequestParam("regularNo") int regularNo, Model model) {
    model.addAttribute("regularDetail", regularService.regularDetail(regularNo, model));
    return "/regular/regularDetail";
  }
  @GetMapping("/regularMainDisplay.do")
  public ResponseEntity<byte[]> regularMainDsiplay(@RequestParam("regularNo") int regularNo){
    return regularService.regularMainDisplay(regularNo);
  }
  
}

