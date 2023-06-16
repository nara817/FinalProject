package com.gdu.pupo.service;

import java.util.List;

import org.springframework.ui.Model;

import com.gdu.pupo.domain.ItemDTO;

public interface ItemMainService {

  public List<ItemDTO> getList();
  public ItemDTO get(String itemCode, Model model);
  public boolean modify(ItemDTO itemMainDTO);
  public boolean remove(String itemCode);
  public void register(ItemDTO itemMainDTO);
}
