package com.gdu.pupo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.pupo.domain.ItemDTO;

public interface ItemService {
  public List<ItemDTO> itemList();
  public void itemListByNo(int itemNo, Model model);
  
  public int itemRegister(MultipartHttpServletRequest multipartRequest); 
  
  public int itemDelete(int itemNo);
  public int itemUpdate(MultipartHttpServletRequest multipartRequest); 
  public int attachDelete(int attachNo);
   
  public ResponseEntity<byte[]> display(int attachNo);

}
