package com.gdu.pupo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.gdu.pupo.domain.CouponDTO;

@Service
public interface CouponService {

	// 쿠폰 등록
	public int addCoupon(HttpServletRequest request, HttpServletResponse response);
	
	// 쿠폰 조회
	public List<CouponDTO> couponList();
	
	// 이벤트 페이지 - 쿠폰 발급
	public void getEventCoupon(HttpServletRequest request, HttpServletResponse response);
	
	
	

}