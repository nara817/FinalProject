package com.gdu.pupo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.gdu.pupo.domain.FaqDTO;

public interface FaqService {
  
  //reviewsBoard 서비스
  public void getFaqList(HttpServletRequest request,Model model);
  public void addFaq(HttpServletRequest request, HttpServletResponse response);
  public FaqDTO getFaqByNo(HttpServletRequest request);
  public void modifyFaq(HttpServletRequest request, HttpServletResponse response);
  public void removeFaq(HttpServletRequest request, HttpServletResponse response);
  


 

}
