package com.gdu.pupo.service;

import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.pupo.domain.GetTokenVO;
import com.gdu.pupo.domain.RegularCategoryDTO;
import com.gdu.pupo.domain.RegularDetailImgDTO;
import com.gdu.pupo.domain.RegularMainImgDTO;
import com.gdu.pupo.domain.RegularProductDTO;
import com.gdu.pupo.domain.RegularPurchaseDTO;
import com.gdu.pupo.domain.RegularShipDTO;
import com.gdu.pupo.domain.UserDTO;
import com.gdu.pupo.mapper.RegularMapper;
import com.gdu.pupo.util.MyFileUtil;
import com.gdu.pupo.util.PageUtil;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@RequiredArgsConstructor
@Service
public class RegularServicelmpl implements RegularService {
 
  private final RegularMapper regularMapper;
  private final MyFileUtil myFileUtil;
  private final PageUtil pageUtil;
  

  // 카테고리 추가
  @Override
  public void addRegCategory(HttpServletRequest request) { 
    String regularCategoryName = request.getParameter("regularCategoryName");
    RegularCategoryDTO regularCategoryDTO = new RegularCategoryDTO();
    regularCategoryDTO.setRegularCategoryName(regularCategoryName);
    regularMapper.addRegCategory(regularCategoryDTO);
  }
  
  // 카테고리 전체 리스트
  @Override
  public void getRegCategory(Model model) {
    List<RegularCategoryDTO> list = regularMapper.getRegCategoryList();
    System.out.println(list + "입니다");
    model.addAttribute("category", list);
  }
  
  
  // 상품 등록
  @Transactional
  @Override
  public int addRegular(MultipartHttpServletRequest multipartRequest) { // 상품등록 
    // regularProductDTO에 저장 할 파라미터 
    String regularName = multipartRequest.getParameter("regularName");
    int regularSellPrice = Integer.parseInt(multipartRequest.getParameter("regularSellPrice"));
    int regularOriginPrice = Integer.parseInt(multipartRequest.getParameter("regularOriginPrice"));
    int regularDisplay = Integer.parseInt(multipartRequest.getParameter("regularDisplay"));
    int regularCategory = Integer.parseInt(multipartRequest.getParameter("regularCategory"));
    int regularState = Integer.parseInt(multipartRequest.getParameter("regularState"));
    String regularSimpleDetail = multipartRequest.getParameter("regularSimpleDetail");
    
    RegularProductDTO regularProductDTO = new RegularProductDTO();
    regularProductDTO.setRegularCategory(regularCategory);
    regularProductDTO.setRegularDisplay(regularDisplay);
    regularProductDTO.setRegularName(regularName);
    regularProductDTO.setRegularOriginPrice(regularOriginPrice);
    regularProductDTO.setRegularSellPrice(regularSellPrice);
    regularProductDTO.setRegularSimpleDetail(regularSimpleDetail);
    regularProductDTO.setRegularState(regularState);
    
    
    int addResult = regularMapper.addRegular(regularProductDTO);
    
    /* regularDetailImg 테이블에 regularDetailImg 넣기 */
    
    // 첨부된 파일 목록
    List<MultipartFile> files = multipartRequest.getFiles("files");  // <input type="file" name="files">
    
    // 첨부가 없는 경우에도 files 리스트는 비어 있지 않고,
    // [MultipartFile[field="files", filename=, contentType=application/octet-stream, size=0]] 형식으로 MultipartFile을 하나 가진 것으로 처리된다.
    
    // 첨부된 파일 목록 순회
    for(MultipartFile multipartFile : files) {
      
      // 첨부된 파일이 있는지 체크
      if(multipartFile != null && multipartFile.isEmpty() == false) {
        
        // 예외 처리
        try {
          
          /* HDD에 첨부 파일 저장하기 */
          
          // 첨부 파일의 저장 경로
          String regDetailimgName = myFileUtil.getPath();
          
          // 첨부 파일의 저장 경로가 없으면 만들기
          File dir = new File(regDetailimgName);
          if(dir.exists() == false) {
            dir.mkdirs();
          }
          
          // 첨부 파일의 원래 이름
          String originName = multipartFile.getOriginalFilename();
          originName = originName.substring(originName.lastIndexOf("\\") + 1);  // IE는 전체 경로가 오기 때문에 마지막 역슬래시 뒤에 있는 파일명만 사용한다.
          
          // 첨부 파일의 저장 이름
          String filesystemName = myFileUtil.getFilesystemName(originName);
          
          // 첨부 파일의 File 객체 (HDD에 저장할 첨부 파일)
          File file = new File(dir, filesystemName);
          
          // 첨부 파일을 HDD에 저장
          multipartFile.transferTo(file);  // 실제로 서버에 저장된다.
          
          /* 썸네일(첨부 파일이 이미지인 경우에만 썸네일이 가능) */
          
          // 첨부 파일의 Content-Type 확인
          String contentType = Files.probeContentType(file.toPath());  // 이미지 파일의 Content-Type : image/jpeg, image/png, image/gif, ...
          
          // DB에 저장할 썸네일 유무 정보 처리
          boolean hasThumbnail = contentType != null && contentType.startsWith("image");
          
          // 첨부 파일의 Content-Type이 이미지로 확인되면 썸네일을 만듬
          if(hasThumbnail) {
            
            // HDD에 썸네일 저장하기 (thumbnailator 디펜던시 사용)
            File thumbnail = new File(dir, "s_" + filesystemName);
            Thumbnails.of(file)
              .size(50, 50)
              .toFile(thumbnail);
            
          }
          
          /* DB에 첨부 파일 정보 저장하기 */
          
          // DB로 보낼 AttachDTO 만들기
          RegularDetailImgDTO regularDetailImgDTO = new RegularDetailImgDTO();
          regularDetailImgDTO.setRegDetailImgName(regDetailimgName);
          regularDetailImgDTO.setRegularNo(regularProductDTO.getRegularNo());
          regularDetailImgDTO.setRegFilesystemName(filesystemName);
          // DB로 AttachDTO 보내기
          regularMapper.addRegImg(regularDetailImgDTO);
          
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
  }
    /* regularDetailImg 테이블에 regularDetailImg 넣기 */
    
    // 첨부된 파일 목록
    List<MultipartFile> mainImg = multipartRequest.getFiles("mainImg");  // <input type="file" name="files">
    
    // 첨부된 파일 목록 순회
    for(MultipartFile multipartFile : mainImg) {
      
      // 첨부된 파일이 있는지 체크
      if(multipartFile != null && multipartFile.isEmpty() == false) {
        
        // 예외 처리
        try {
          
          /* HDD에 첨부 파일 저장하기 */
          
          // 첨부 파일의 저장 경로
          String regMainImgName = myFileUtil.getPath();
          
          // 첨부 파일의 저장 경로가 없으면 만들기
          File dir = new File(regMainImgName);
          if(dir.exists() == false) {
            dir.mkdirs();
          }
          
          // 첨부 파일의 원래 이름
          String originName = multipartFile.getOriginalFilename();
          originName = originName.substring(originName.lastIndexOf("\\") + 1);  // IE는 전체 경로가 오기 때문에 마지막 역슬래시 뒤에 있는 파일명만 사용한다.
          
          // 첨부 파일의 저장 이름
          String filesystemName = myFileUtil.getFilesystemName(originName);
          
          // 첨부 파일의 File 객체 (HDD에 저장할 첨부 파일)
          File file = new File(dir, filesystemName);
          
          // 첨부 파일을 HDD에 저장
          multipartFile.transferTo(file);  // 실제로 서버에 저장된다.
          
          /* 썸네일(첨부 파일이 이미지인 경우에만 썸네일이 가능) */
          
          // 첨부 파일의 Content-Type 확인
          String contentType = Files.probeContentType(file.toPath());  // 이미지 파일의 Content-Type : image/jpeg, image/png, image/gif, ...
          
          // DB에 저장할 썸네일 유무 정보 처리
          boolean hasThumbnail = contentType != null && contentType.startsWith("image");
          
          // 첨부 파일의 Content-Type이 이미지로 확인되면 썸네일을 만듬
          if(hasThumbnail) {
            
            // HDD에 썸네일 저장하기 (thumbnailator 디펜던시 사용)
            File thumbnail = new File(dir, "s_" + filesystemName);
            Thumbnails.of(file)
              .size(50, 50)
              .toFile(thumbnail);
            
          }
          
          /* DB에 첨부 파일 정보 저장하기 */
          
          // DB로 보낼 AttachDTO 만들기
          RegularMainImgDTO regularMainImgDTO = new RegularMainImgDTO();
          regularMainImgDTO.setRegMainImgName(regMainImgName);
          regularMainImgDTO.setRegFilesystemName(filesystemName);
          regularMainImgDTO.setRegularNo(regularProductDTO.getRegularNo());
          
          // DB로 AttachDTO 보내기
          regularMapper.addRegMainImg(regularMainImgDTO);
          
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
  }    
    return addResult;
 }
  
  
  // 전체 상품 리스트
  @Override
  public void regularList(HttpServletRequest request, Model model) {
   
    // 파라미터 column이 전달되지 않는 경우 column=""로 처리한다. (검색할 칼럼)
    Optional<String> opt1 = Optional.ofNullable(request.getParameter("column"));
    String column = opt1.orElse("");
    
    // 파라미터 query가 전달되지 않는 경우 query=""로 처리한다. (검색어)
    Optional<String> opt2 = Optional.ofNullable(request.getParameter("query"));
    String query = opt2.orElse("");
    
    Optional<String> opt3 = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt3.orElse("1"));
    // DB로 보낼 Map 만들기(column + query)    
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("column", column);
    map.put("query", query);
    
    int getRegularCount = regularMapper.getRegularCount();
    
    int recordPerPage = 5;
    
    pageUtil.setPageUtil(page, getRegularCount, recordPerPage);
    
    map.put("begin", pageUtil.getBegin());
    map.put("recordPerPage", recordPerPage);
    
    List<RegularProductDTO> regularList = regularMapper.getRegularList(map);
    List<RegularMainImgDTO> regularMainImgList = regularMapper.getRegularMainImgList();
    model.addAttribute("regularList", regularList);
    model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/regular/regularList.do?column=" + column + "&query=" + query));
    model.addAttribute("regularMainImgList", regularMainImgList);
  }
  
  
}