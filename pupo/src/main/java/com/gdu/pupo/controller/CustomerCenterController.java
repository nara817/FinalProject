package com.gdu.pupo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.pupo.service.FaqService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customerCenter")
public class CustomerCenterController {
  
  private final FaqService faqService;
  
  /* 고객센터 홈 */
  @GetMapping("/centerHome.html")
  public String centerHome() {
    return "customerCenter/centerHome";  
  }
  
  /* 고객센터 공지 */
  @GetMapping("/notice.html")
  public String notice() {
    return "customerCenter/notice";  
  }
  
  /* 고객센터 자주묻는질문 */

  @GetMapping("/faq.html") 
  public String faq(HttpServletRequest request, Model model) { 
    faqService.getFaqList(request, model);
    return "customerCenter/faq"; 
  }
  

  
  
  /* 고객센터 1:1문의 */
  @GetMapping("/qna.html")
  public String qna() {
    return "customerCenter/qna";  
  }
  
  
  
  /* 고객센터 고객의 소리 */
  @GetMapping("/voc.html")
  public String voc() {
    return "customerCenter/voc";  
  }
  


  @GetMapping("/write.html")
  public String write() {
    return "customerCenter/write";
  }
  

  @GetMapping("/edit.html")
  public String detail(HttpServletRequest request, Model model) {
    model.addAttribute("f", faqService.getFaqByNo(request));
    return "customerCenter/edit";
  }
  
  @PostMapping("/add.do")
  public void add(HttpServletRequest request, HttpServletResponse response) {
    faqService.addFaq(request, response);
  }

  @PostMapping("/modify.do")
  public void modify(HttpServletRequest request, HttpServletResponse response) {
    faqService.modifyFaq(request, response);
  }

  // removeBoard() 서비스 내부에 location.href를 이용한 /board/list.do 이동이 있기 때문에 응답할 View를
  // 반환하지 않는다.
  @PostMapping("/remove.do")
  public void remove(HttpServletRequest request, HttpServletResponse response) {
    faqService.removeFaq(request, response);
  }

// pagination 컨트롤러

  @GetMapping("/change/record.do")
  public String changeRecord(HttpSession session, HttpServletRequest request,
      @RequestParam(value = "recordPerPage", required = false, defaultValue = "10") int recordPerPage) {
    session.setAttribute("recordPerPage", recordPerPage);
    return "redirect:" + request.getHeader("referer"); // 현재 주소(/employees/change/record.do)의 이전 주소(Referer)로 이동하시오.
  }

  


}
