package com.gdu.pupo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.pupo.domain.AttachDTO;
import com.gdu.pupo.domain.ItemDTO;

@Mapper
public interface ItemMapper {

  public List<ItemDTO> itemList();
  
  public int itemRegister(ItemDTO itemDTO);
  public int imgRegister(AttachDTO attachDTO);

  public ItemDTO itemListByNo(int itemNo);
  public List<AttachDTO> attachList(int itemNo);

  public AttachDTO attachByNo(int attachNo);
  
  
  public int itemUpdate(ItemDTO itemDTO);
  public int itemDelete(int itemNo);
  
  
  
  public int attachDelete(int attachNo);
}