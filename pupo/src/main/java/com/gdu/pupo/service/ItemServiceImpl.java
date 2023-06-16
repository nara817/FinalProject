package com.gdu.pupo.service;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.pupo.domain.AttachDTO;
import com.gdu.pupo.domain.ItemDTO;
import com.gdu.pupo.mapper.ItemMapper;
import com.gdu.pupo.util.MyFileUtil;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;


@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

  
  private final ItemMapper itemMapper;
  private final MyFileUtil myFileUtil;
  
  @Override
  public List<ItemDTO> itemList() {
      return itemMapper.itemList();
  }

  @Override
  public void itemListByNo(int itemNo, Model model) {
    model.addAttribute("item", itemMapper.itemListByNo(itemNo));
    model.addAttribute("attachList", itemMapper.attachByNo(itemNo));
  }

  @Transactional
  @Override
  public int itemRegister(MultipartHttpServletRequest multipartRequest) {
    String itemName = multipartRequest.getParameter("itemName");
    String itemCategory = multipartRequest.getParameter("itemCategory");
    String itemComment = multipartRequest.getParameter("itemComment");
    int price = Integer.parseInt(multipartRequest.getParameter("price"));
    int originPrice = Integer.parseInt(multipartRequest.getParameter("originPrice"));
    int stock = Integer.parseInt(multipartRequest.getParameter("stock"));
    String dispStat = multipartRequest.getParameter("dispStat");
    String saleStat = multipartRequest.getParameter("saleStat");
        
    ItemDTO itemDTO = new ItemDTO();
    itemDTO.setItemName(itemName);
    itemDTO.setItemCategory(itemCategory);
    itemDTO.setItemComment(itemComment);
    itemDTO.setPrice(price);
    itemDTO.setOriginPrice(originPrice);
    itemDTO.setStock(stock);
    itemDTO.setDispStat(dispStat);
    itemDTO.setSaleStat(saleStat);
    
    int registerResult = itemMapper.itemRegister(itemDTO);
    
    List<MultipartFile> files = multipartRequest.getFiles("files");
    for(MultipartFile multipartFile : files) {
      if(multipartFile != null && multipartFile.isEmpty() == false) {
        try {
          String path = myFileUtil.getPath();
          File dir = new File(path);
          if(dir.exists() == false) {
            dir.mkdirs();
          }
          String originName = multipartFile.getOriginalFilename();
          originName = originName.substring(originName.lastIndexOf("\\") + 1);
          String filesystemName = myFileUtil.getFilesystemName(originName);
          File file = new File(dir, filesystemName);
          multipartFile.transferTo(file);
          String contentType = Files.probeContentType(file.toPath());
          boolean hasThumbnail = contentType != null && contentType.startsWith("image");
          if(hasThumbnail) {
            File thumbnail = new File(dir, "s_" + filesystemName);
            Thumbnails.of(file).size(50, 50).toFile(thumbnail);
            
          }
          
          AttachDTO attachDTO = new AttachDTO();
          attachDTO.setFilesystemName(filesystemName);
          attachDTO.setHasThumbnail(hasThumbnail ? 1 : 0);
          attachDTO.setOriginName(originName);
          attachDTO.setPath(path);
          attachDTO.setItemNo(itemDTO.getItemNo());
          
          itemMapper.imgRegister(attachDTO);
          
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return registerResult;
  }
   @Transactional
   @Override
   public int itemUpdate(MultipartHttpServletRequest multipartRequest) {
    int itemNo = Integer.parseInt(multipartRequest.getParameter("itemNo"));
    String itemName = multipartRequest.getParameter("itemName");
    String itemCategory = multipartRequest.getParameter("itemCategory");
    String itemComment = multipartRequest.getParameter("itemComment");
    int price = Integer.parseInt(multipartRequest.getParameter("price"));
    int originPrice = Integer.parseInt(multipartRequest.getParameter("originPrice"));
    int stock = Integer.parseInt(multipartRequest.getParameter("stock"));
    String dispStat = multipartRequest.getParameter("dispStat");
    String saleStat = multipartRequest.getParameter("saleStat");
        
    ItemDTO itemDTO = new ItemDTO();
    itemDTO.setItemNo(itemNo);
    itemDTO.setItemName(itemName);
    itemDTO.setItemCategory(itemCategory);
    itemDTO.setItemComment(itemComment);
    itemDTO.setPrice(price);
    itemDTO.setOriginPrice(originPrice);
    itemDTO.setStock(stock);
    itemDTO.setDispStat(dispStat);
    itemDTO.setSaleStat(saleStat);
    
    int updateResult = itemMapper.itemUpdate(itemDTO);
    
    List<MultipartFile> files = multipartRequest.getFiles("files");
    for(MultipartFile multipartFile : files) {
      if(multipartFile != null && multipartFile.isEmpty() == false) {
        try {
          String path = myFileUtil.getPath();
          File dir = new File(path);
          if(dir.exists() == false) {
            dir.mkdir();
          }
          String originName = multipartFile.getOriginalFilename();
          originName = originName.substring(originName.lastIndexOf("\\") + 1);
          String filesystemName = myFileUtil.getFilesystemName(originName);
          File file = new File(dir, filesystemName);
          multipartFile.transferTo(file);
          String contentType = Files.probeContentType(file.toPath());
          boolean hasthumbnail = contentType != null && contentType.startsWith("image");
          if(hasthumbnail) {
            File thumbnail = new File(dir, "s_" + filesystemName);
            Thumbnails.of(file).size(50, 50).toFile(thumbnail);
          }
          AttachDTO attachDTO = new AttachDTO();
          attachDTO.setFilesystemName(filesystemName);
          attachDTO.setHasThumbnail(updateResult);
          attachDTO.setOriginName(originName);
          attachDTO.setPath(path);
          attachDTO.setItemNo(itemDTO.getItemNo());
          
          itemMapper.imgRegister(attachDTO);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return updateResult;
  }
  
   @Override
   public int itemDelete(int itemNo) {
     int deleteResult = itemMapper.itemDelete(itemNo);
     return deleteResult;
   }
   
  
  @Override
  public int attachDelete(int attachNo) {
    AttachDTO attachDTO = itemMapper.attachByNo(attachNo);
    if(attachDTO != null) {
      File file = new File(attachDTO.getPath(), attachDTO.getFilesystemName());
      if(file.exists()) {
        file.delete();
      }
      if(attachDTO.getHasThumbnail() == 1) {
        File thumbnail = new File(attachDTO.getPath(), "s_" + attachDTO.getFilesystemName());
        if(thumbnail.exists()) {
          thumbnail.delete();
        }
      }
    }
    int removeResult = itemMapper.attachDelete(attachNo);
    return removeResult;
  }
  
  @Override
  public ResponseEntity<byte[]> display(int attachNo) {
    AttachDTO attachDTO = itemMapper.attachByNo(attachNo);
    ResponseEntity<byte[]> image = null;
    try {
      File thumbnail = new File(attachDTO.getPath(), "s_" + attachDTO.getFilesystemName());
      image = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(thumbnail), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return image;
  }
   
}
