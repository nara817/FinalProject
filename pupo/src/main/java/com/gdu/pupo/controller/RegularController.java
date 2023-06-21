package com.gdu.pupo.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
  public String regularList(HttpServletRequest request, Model model) {
    regularService.regularList(request, model);
    return "/regular/regularList";
  }

  // 메인 이미지 보여주기
  @GetMapping("/regularDetailDisplay.do")
  public ResponseEntity<byte[]> regularDsiplay(@RequestParam("regularNo") int regularNo) {
    return regularService.regularDetailDisplay(regularNo);
  }

  @GetMapping("/regularDetail.do")
  public String regularDetail(@RequestParam("regularNo") int regularNo, Model model) {
    model.addAttribute("regularDetail", regularService.regularDetail(regularNo, model));
    return "/regular/regularDetail";
  }

  @GetMapping("/regularMainDisplay.do")
  public ResponseEntity<byte[]> regularMainDsiplay(@RequestParam("regularNo") int regularNo) {
    return regularService.regularMainDisplay(regularNo);
  }

  // 결제 페이지
  @PostMapping("/regularPayPage.do")
  public String regularPayPage(HttpServletRequest request, Model model) {
    int regularNo = Integer.parseInt(request.getParameter("regularNo"));
    int regCount = Integer.parseInt(request.getParameter("regCount"));
    int totalPrice = Integer.parseInt(request.getParameter("totalPrice").replaceAll(",", ""));
    String regDeliveryDay = request.getParameter("regDeliveryDay");
    String loginId = request.getParameter("loginId");
    model.addAttribute("regularDetail", regularService.regularDetail(regularNo, model));
    UserDTO userDTO = userService.getUserById(loginId);
    model.addAttribute("loginId", userDTO);
    model.addAttribute("regCount", regCount);
    model.addAttribute("totalPrice", totalPrice);
    model.addAttribute("regDeliveryDay", regDeliveryDay);
    return "regular/regularPayPage";
  }

  // 결제 완료 시 주문정보 저장
  @PostMapping("/regularPurchase.do")
  public String regularPurchase(HttpServletRequest request, Model model) {
    int regPurchaseNo = regularService.regularPurchase(request, model);
    return "redirect:/regular/regularPayDone.do?regPurchaseNo=" + regPurchaseNo;
  }

  // 주문완료시 주문완료 페이지 보여주기
  @GetMapping("/regularPayDone.do")
  public String regularPayDone(@RequestParam("regPurchaseNo") int regPurchaseNo, Model model) {
    model.addAttribute("regPurchase", regularService.regularPurchasInfo(regPurchaseNo, model));
    return "/regular/regularPayDone";
  }

  // 카테고리 추가
  @PostMapping("/addCategory.do")
  public String addCategory(HttpServletRequest request) {
    regularService.addRegCategory(request);
    return "redirect:/regular/regularAddPage.html";
  }
  
  // 유저 구매 내역
  @GetMapping("/regularMyOrder.html")
  public String regularMyOrder(HttpServletRequest request, Model model, HttpSession session) {
    model.addAttribute("orderList", regularService.regularMyOrder(request, session, model));
   return "/regular/regularMyOrder"; 
  }
  
  // 구독취소예약
  @PostMapping("/regCancel.do")
  public String regCancel(@RequestParam("regPurchaseNo") int regPurchaseNo) {
    regularService.regCancel(regPurchaseNo);
    return "redirect:/regular/regularMyOrder.html";
  }
  
  // 구독 취소 예약 상태 일 시 재구독
  @PostMapping("/regAgain.do")
  public String regAgain(@RequestParam("regPurchaseNo") int regPurchaseNo) {
    regularService.regAgain(regPurchaseNo);
    return "redirect:/regular/regularMyOrder.html";
  }
  
  // 나의 구독 디테일
  @GetMapping("/regularPayDetail.html")
  public String regularPayDetail(@RequestParam("regPurchaseNo") int regPurchaseNo, Model model) {
    model.addAttribute("regPurchase", regularService.regularPurchasInfo(regPurchaseNo, model));
    return "/regular/regularPayDetail";
  }
  // 배송정보 변경 페이지 이동
  @PostMapping("/regChangeDelivery.html")
  public String regChangeDelivery(HttpServletRequest request, Model model) {
    int regPurchaseNo = Integer.parseInt(request.getParameter("regPurchaseNo"));
    int regShipNo = Integer.parseInt(request.getParameter("regShipNo"));
    model.addAttribute("regPurchaseNo", regPurchaseNo);
    model.addAttribute("regShipNo", regShipNo);
    return "/regular/regularChangeDelivery";
  }
  
  // 배송정보 변경 저장
  @ResponseBody
  @PostMapping(value="/regDeliverySave.do", produces="application/json")
  public Map<String, Object> regDeliverySave(HttpServletRequest request) {
    return regularService.regDeliverySave(request);
  }
  
  // 리뷰 작성 여부 체크
  @ResponseBody
  @PostMapping(value="/checkReview.do", produces="application/json")
  public Map<String, Object> regCheckReview(HttpServletRequest request) {
    return regularService.regCheckReview(request);
  }
  
}
