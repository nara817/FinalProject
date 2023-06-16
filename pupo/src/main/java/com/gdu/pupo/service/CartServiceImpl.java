package com.gdu.pupo.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.gdu.pupo.domain.CartDTO;
import com.gdu.pupo.mapper.CartMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

  private final CartMapper cartMapper;
  
  @Override
  public int addCart(HttpServletRequest request) {
    String id = request.getParameter("id");
    int itemNo = Integer.parseInt(request.getParameter("itemNo"));
    int itemCount = Integer.parseInt(request.getParameter("itemCount"));
    
    CartDTO cartDTO = new CartDTO();
    cartDTO.setId(id);
    cartDTO.setItemNo(itemNo);
    cartDTO.setItemCount(itemCount);
    
    int addResult = cartMapper.addCart(cartDTO);
    
    return addResult;
  }
}
