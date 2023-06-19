package com.gdu.pupo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.pupo.domain.CouponDTO;
import com.gdu.pupo.service.AdminService;
import com.gdu.pupo.service.CouponService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

	// field
	private final AdminService adminService;
	private final CouponService couponService;

	// 관리자-회원관리 페이지 이동
	@GetMapping("/adminUserList.html")
	public String adminUserList(HttpServletRequest request, Model model) {
		// List<UserDTO> user = adminService.userList();
		// model.addAttribute("user", user);
		adminService.getlistUsingPagination(request, model);

		return "admin/adminUserList";
	}

	// 관리자-메인 이동
	@GetMapping("/adminMain.html")
	public String adminMain(Model model) {
		model.addAttribute("userCount", adminService.userCount());
		model.addAttribute("sellerCount", adminService.sellerCount());
		return "admin/adminMain";
	}
	// 관리자-회원관리 페이지 정렬

//	// 관리자-쿠폰리스트
//	@GetMapping("/couponList.html")
//	public String couponList(Model model) {
//		return "admin/couponList";
//	}

	// 관리자-쿠폰등록
	@GetMapping("/addCoupon.html")
	public String addCoupon(@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "event", required = false) String event, Model model) {
		model.addAttribute("location", location);
		model.addAttribute("event", event);
		return "admin/addCoupon";

	}

	// 관리자-쿠폰등록
	@PostMapping("/addCoupon.do")
	public void addCoupon(HttpServletRequest request, HttpServletResponse response) {
		couponService.addCoupon(request, response);
	}

	// 관리자-쿠폰조회
	@GetMapping("/couponList.html")
	public String couponList(HttpServletRequest request, Model model) {
		List<CouponDTO> coupon = couponService.couponList();
		model.addAttribute("coupon", coupon);
		return "admin/couponList";
	}
	
	// 관리자-이벤트 페이지 이동
	@GetMapping("/eventCoupon.html")
	public String eventCoupon(HttpServletRequest request, HttpServletResponse response) {

		return "admin/eventCoupon";

	}
	
	// 관리자-이벤트 페이지 / 쿠폰발급
	@PostMapping("/eventCoupon.do")
	public void getEventCoupon(HttpServletRequest request, HttpServletResponse response) {
		couponService.getEventCoupon(request, response);
		
	}
}
