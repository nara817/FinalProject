package com.gdu.pupo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.gdu.pupo.domain.UserDTO;
import com.gdu.pupo.mapper.AdminMapper;
import com.gdu.pupo.util.PageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
	
	// field
	private final AdminMapper adminMapper;
	private final PageUtil pageUtil;
	
	
	// 회원 조회
//	@Override
//	public List<UserDTO> userList() {
//	
//	    List<UserDTO> selectUserByUserListDTO = adminMapper.selectUserByUserListDTO();
//			return selectUserByUserListDTO;
//		}

	// 회원 count
//	@Override
//	public Map<String, Object> selectUserCount() {
//		return adminMapper.selectUserCount();
//	}
//	
	@Override
		public int sellerCount() {
			return adminMapper.selectSellerCount();
		}
	@Override
	public int userCount() {
		return adminMapper.selectUserCount();
	}
	
	// 페이지네이션
	@Override
	public void getlistUsingPagination(HttpServletRequest request, Model model) {
		// 파라미터 page가 전달되지 않는 경우 page=1로 처리한다.
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt1.orElse("1"));
		
		// 전체 레코드 개수를 구한다.
		int totalRecord = adminMapper.getListCount();
		
		// 세션에 있는 recordPerPage를 가져온다. 세션에 없는 경우 recordPerPage=10으로 처리한다.
		HttpSession session = request.getSession();
		Optional<Object> opt2 = Optional.ofNullable(session.getAttribute("recordPerPage"));
		int recordPerPage = (int)(opt2.orElse(10));

		// 파라미터 order가 전달되지 않는 경우 order=ASC로 처리한다.
		Optional<String> opt3 = Optional.ofNullable(request.getParameter("order"));
		String order = opt3.orElse("ASC");

		// 파라미터 column이 전달되지 않는 경우 column=EMPLOYEE_ID로 처리한다.
		Optional<String> opt4 = Optional.ofNullable(request.getParameter("column"));
		String column = opt4.orElse("USER_NO");
		
		// PageUtil(Pagination에 필요한 모든 정보) 계산하기
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		
		// DB로 보낼 Map 만들기
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("begin", pageUtil.getBegin());
		map.put("end", pageUtil.getEnd());
//		map.put("order", order);
//		map.put("column", column);
		// begin ~ end 사이의 목록 가져오기
		List<UserDTO> user = adminMapper.getlistUsingPagination(map);
		
		// pagination.jsp로 전달할(forward)할 정보 저장하기
		model.addAttribute("user", user);
		model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/admin/adminUserList.html?column=" + column + "&order=" + order));
		model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
		switch(order) {
		case "ASC" : model.addAttribute("order", "DESC"); break;  // 현재 ASC 정렬이므로 다음 정렬은 DESC이라고 Jsp에 알려준다.
		case "DESC": model.addAttribute("order", "ASC"); break;
		}
		model.addAttribute("page", page);
	}

}
	
	