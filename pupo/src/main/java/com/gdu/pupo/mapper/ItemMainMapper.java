package com.gdu.pupo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.pupo.domain.ItemDTO;

@Mapper
public interface ItemMainMapper {
  public List<ItemDTO> getList();
  public void insert(ItemDTO itemMainDTO);
  public void insertSelectKey(ItemDTO itemMainDTO);
  public ItemDTO get(String itemCode);
  public int update(ItemDTO itemMainDTO);
  public int delete(String itemCode);
}
