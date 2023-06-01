package com.gdu.pupo.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdu.pupo.domain.UserDTO;

public class UserServiceImpl implements UserService {

	@Override
	public Map<String, Object> verifyId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> verifyEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> sendAuthCode(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void join(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void login(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void autologin(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void leave(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sleepUserHandle() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restore(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkPw(String id, String pw) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserDTO getUserById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateMypage(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modifyUserInfo(UserDTO userDTO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, Object> findUser(String name, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void findPw(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyPw(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

}
