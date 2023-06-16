package com.gdu.pupo.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.pupo.domain.RegularCategoryDTO;
import com.gdu.pupo.domain.RegularProductDTO;
import com.gdu.pupo.domain.RegularPurchaseDTO;

public interface RegularService {

  public void addRegCategory(HttpServletRequest request); // 카테고리 등록
  public void getRegCategory(Model model);   // 카테고리 리스트
  public int addRegular(MultipartHttpServletRequest multipartRequest); // 상품등록
  public void regularList(HttpServletRequest request, Model model); // 상품리스트
  public ResponseEntity<byte[]> regularMainDisplay(int regularNo); // 상품썸네일, 그냥이미지
  public RegularProductDTO regularDetail(int regularNo, Model model);
  public ResponseEntity<byte[]> regularDetailDisplay(int regularNo);
  public RegularPurchaseDTO regularPurchase(HttpServletRequest request, Model model); // 구매완료 후 구매 저장
  public String getToken(); // 아임포트 토큰가져오기
  public String regAgainPay(); // 자동결제
}
