package com.gdu.pupo.service;

import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
import com.gdu.pupo.domain.RegularDetailImgDTO;
import com.gdu.pupo.domain.RegularMainImgDTO;
import com.gdu.pupo.domain.RegularProductDTO;
import com.gdu.pupo.domain.RegularPurchaseDTO;
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
    List<RegularDetailImgDTO> regularImgList = regularMapper.getRegularImgList();
    model.addAttribute("regularList", regularList);
    model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/regular/regularList.do?column=" + column + "&query=" + query));
    model.addAttribute("regularImgList", regularImgList);
  }
  
  @Override
  public ResponseEntity<byte[]> regularDisplay(int regularNo) {
    RegularDetailImgDTO regularDetailImgDTO = regularMapper.getRegularImgByNo(regularNo);
    ResponseEntity<byte[]> image = null;  
    
    try {
      File thumbnail = new File(regularDetailImgDTO.getRegDetailImgName(), "s_" + regularDetailImgDTO.getRegFilesystemName());
      image = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(thumbnail), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return image;
  }
  
  @Override
  public RegularProductDTO regularDetail(int regularNo, Model model) {
    RegularProductDTO regularProductDTO = regularMapper.getRegularByNo(regularNo);
    return regularProductDTO;
  }
  
  @Override
  public ResponseEntity<byte[]> regularMainDisplay(int regularNo) {
    RegularDetailImgDTO regularDetailImgDTO = regularMapper.getRegularImgByNo(regularNo);
    ResponseEntity<byte[]> image = null;  
    
    try {
      File thumbnail = new File(regularDetailImgDTO.getRegDetailImgName(), regularDetailImgDTO.getRegFilesystemName());
      image = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(thumbnail), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return image;
  }
  
  @Override
  public RegularPurchaseDTO regularPurchase(HttpServletRequest request, Model model) {
    String regCustomerUid = request.getParameter("regCustomerUid");
    String id = request.getParameter("loginId");
    int regularNo = Integer.parseInt(request.getParameter("regularNo"));
    int regPurchasePrice = Integer.parseInt(request.getParameter("regPurchasePrice"));
    int regShipNo = 1; // 이부분은 주문 완료 될 때 배송 정보들 입력 되면서 배송번호 불러와야함
    int regPurchaseLastPrice = regPurchasePrice; // 최종가격, 이때 정가일수 있고 할인가일수있고.
    int regProductCount = 1; // 실제 주문 페이지에서 보내주기
    String regPg = request.getParameter("regPg");
    int regDeliverDay = 1; // 실제 주문 페이지에서 1,2 선택 해서 보내주기
    
    
    RegularPurchaseDTO regularPurchaseDTO = new RegularPurchaseDTO();
    regularPurchaseDTO.setId(id);
    regularPurchaseDTO.setRegCustomerUid(regCustomerUid);
    regularPurchaseDTO.setRegDeliveryDay(regDeliverDay);
    regularPurchaseDTO.setRegDeliveryStatus(regDeliverDay);
    regularPurchaseDTO.setRegProductCount(regProductCount);
    regularPurchaseDTO.setRegPurchasePrice(regPurchasePrice);
    regularPurchaseDTO.setRegPg(regPg);
    regularPurchaseDTO.setRegShipNo(regShipNo);
    regularPurchaseDTO.setRegularNo(regularNo);
    regularPurchaseDTO.setRegPurchaseLastPrice(regPurchaseLastPrice);
    int addResult = regularMapper.addRegPurchase(regularPurchaseDTO);
    
    // 
    int regPurchaseNo = regularPurchaseDTO.getRegPurchaseNo();
    return regularMapper.getRegularPurchaseByNo(regPurchaseNo);
  }
  
  @Override
  public String getToken() { // 아임포트 토큰 받아오기
    RestTemplate restTemplate = new RestTemplate();
    
    //서버로 요청할 Header
     HttpHeaders headers = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_JSON);
    
     Map<String, Object> map = new HashMap<>();
     map.put("imp_key", "");
     map.put("imp_secret", "");
      
     Gson var = new Gson();
     String json=var.toJson(map);
    //서버로 요청할 Body
     
    HttpEntity<String> entity = new HttpEntity<>(json,headers);
    System.out.println(restTemplate.postForObject("https://api.iamport.kr/users/getToken", entity, String.class));
    return restTemplate.postForObject("https://api.iamport.kr/users/getToken", entity, String.class);
    }
  
  @Override
    public String regAgainPay() {
      String token = getToken();
      Gson str = new Gson();
      token = token.substring(token.indexOf("response") + 10);
      token = token.substring(0, token.length() - 1);
  
      GetTokenVO vo = str.fromJson(token, GetTokenVO.class);
  
      String access_token = vo.getAccess_token();
      System.out.println(access_token);
  
      RestTemplate restTemplate = new RestTemplate();
  
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(access_token);
      
      // 정기구독 인 주문들만 출력
      List<RegularPurchaseDTO> purchaseList = regularMapper.regularPayList();
      
      // 현재 날짜 얻어오기
      LocalDate currentDate = LocalDate.now();
      LocalDateTime merchant = LocalDateTime.now();
      
      
      
      // 결제 주기
      for(RegularPurchaseDTO purchase : purchaseList) {
        // Date.util -> LocalDate로 변경
        Date regLastPayAt = purchase.getRegLastPayAt();
        Instant instant = regLastPayAt.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate regLastPayDate = localDateTime.toLocalDate();
        // 다음 결제일 계산 1달 뒤임
        LocalDate regNextPayDate =  regLastPayDate.plusMonths(1);
        if(currentDate.equals(regNextPayDate) || currentDate.isAfter(regNextPayDate)){
          Map<String, Object> map = new HashMap<>();
          map.put("customer_uid", purchase.getRegCustomerUid());
          map.put("merchant_uid", "pupo_" + merchant);
          map.put("amount", purchase.getRegPurchaseLastPrice());
          map.put("name", "풀파워 샐러드 정기결제");
          
          Gson var = new Gson();
          String json = var.toJson(map);
         
          HttpEntity<String> entity = new HttpEntity<>(json, headers);
          
          restTemplate.postForObject("https://api.iamport.kr/subscribe/payments/again", entity, String.class);
          regularMapper.regularPayUpdate(purchase.getRegPurchaseNo());
        }
      }
      return null;
  }
}
