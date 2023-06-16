package com.gdu.pupo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.pupo.service.CartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping(value = "/", method = { RequestMethod.POST, RequestMethod.GET })
@Controller
public class CartController {

  private final CartService cartService;
  
  @PostMapping("item/addCart")
  public String addCart(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int addResult = cartService.addCart(request);
    redirectAttributes.addFlashAttribute("addResult", addResult);
    return "redirect:/item/cart";
  }
  
}
