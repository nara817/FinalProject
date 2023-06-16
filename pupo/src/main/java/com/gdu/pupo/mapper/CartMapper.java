package com.gdu.pupo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.pupo.domain.CartDTO;

@Mapper
public interface CartMapper {

  public int addCart(CartDTO cartDTO);
}
