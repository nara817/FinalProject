package com.gdu.pupo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.gdu.pupo.domain.ItemDTO;
import com.gdu.pupo.mapper.ItemMainMapper;
import com.gdu.pupo.util.MyFileUtil;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class ItemMainServiceImpl implements ItemMainService {

  
  private final ItemMainMapper itemMainMapper;
  private final MyFileUtil myFileUtil;
  
   @Override
  public List<ItemDTO> getList() {
    return itemMainMapper.getList();
  }
   
   @Override
  public ItemDTO get(String itemCode, Model model) {
    model.addAttribute("itemList", itemMainMapper.get(itemCode));
    return itemMainMapper.get(itemCode);
  }
   
   @Override
  public boolean modify(ItemDTO itemMainDTO) {
    return itemMainMapper.update(itemMainDTO) == 1;
  }
   
   @Override
  public boolean remove(String itemCode) {
    return itemMainMapper.delete(itemCode) == 1;
  }
   
   @Override
  public void register(ItemDTO itemMainDTO) {
    // TODO Auto-generated method stub
    
  }
}