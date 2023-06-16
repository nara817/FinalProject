package com.gdu.pupo.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface AdminService {
	
	// 회원 조회
//	List<UserDTO> userList();
	
	// 회원 count
	public int userCount();
	public int sellerCount();
	
	// 페이지네이션
	public void getlistUsingPagination(HttpServletRequest request, Model model);

}