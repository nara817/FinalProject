package com.gdu.pupo.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.pupo.domain.RegularProductDTO;

public interface RegularService {

  public int addRegular(MultipartHttpServletRequest multipartRequest); // 상품등록
  public void regularList(HttpServletRequest request, Model model); // 상품리스트
  public ResponseEntity<byte[]> regularDisplay(int regularNo); // 상품썸네일, 그냥이미지
  public RegularProductDTO regularDetail(int regularNo, Model model);
  public ResponseEntity<byte[]> regularMainDisplay(int regularNo);
}
