package com.gdu.pupo.mapper;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.ui.Model;

import com.gdu.pupo.domain.UserDTO;

@Mapper
public interface AdminMapper {
	
	// 회원 조회
//	public List<UserDTO> selectUserByUserListDTO();
	
	// 회원 count
	//public List<Map<String, Object>> countUsersBySellerCheck();
	public int selectUserCount();
	public int selectSellerCount();
	public List<UserDTO> getlistUsingPagination(Map<String, Object> map);
	public int getListCount();
}
