package com.gdu.pupo.service;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.pupo.domain.QnaAttachDTO;
import com.gdu.pupo.domain.QnaDTO;
import com.gdu.pupo.mapper.QnaMapper;
import com.gdu.pupo.util.MyFileUtil;
import com.gdu.pupo.util.PageUtil;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@RequiredArgsConstructor
@Service
public class QnaServiceImpl implements QnaService {
  
  private final QnaMapper qnaMapper;
  private final PageUtil pageUtil;
  private final MyFileUtil myFileUtil;

  
  
  @Transactional
  @Override
  public int qnaAdd(MultipartHttpServletRequest multipartRequest) {
    /* Notice 테이블에 NoticeDTO 넣기 */
    
    // 카테고리, 제목, 내용  파라미터
    String qnaCategory = multipartRequest.getParameter("qnaCategory");
    String qnaTitle = multipartRequest.getParameter("qnaTitle");
    String qnaEmail = multipartRequest.getParameter("qnaEmail");
    String qnaContent = multipartRequest.getParameter("qnaContent");
   
    
    // DB로 보낼 NoticeDTO 만들기
    QnaDTO qnaDTO = new QnaDTO();
    qnaDTO.setQnaCategory(qnaCategory);
    qnaDTO.setQnaContent(qnaContent);
    qnaDTO.setQnaTitle(qnaTitle);
    qnaDTO.setQnaEmail(qnaEmail);

    
    // DB로 NoticeDTO 보내기
 
    int addResult = qnaMapper.qnaAdd(qnaDTO);  // <selectKey>에 의해서 uploadDTO 객체의 uploadNo 필드에 UPLOAD_SEQ.NEXTVAL값이 저장된다.

    /* noticeAttach 테이블에 noticeAttachDTO 넣기 */
    
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
          String path = myFileUtil.getPath();
          
          // 첨부 파일의 저장 경로가 없으면 만들기
          File dir = new File(path);
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
          QnaAttachDTO qnaAttachDTO = new QnaAttachDTO();
          qnaAttachDTO.setFilesystemName(filesystemName);
          qnaAttachDTO.setHasThumbnail(hasThumbnail ? 1 : 0);
          qnaAttachDTO.setOriginName(originName);
          qnaAttachDTO.setPath(path);
          qnaAttachDTO.setQnaNo(qnaDTO.getQnaNo());
          

          // DB로 AttachDTO 보내기
          qnaMapper.qnaAddAttach(qnaAttachDTO);
          
        } catch(Exception e) {
          e.printStackTrace();
        }
        
      }
      
    }
    
    return addResult;
  }
  
  
  @Override
  public void getQnaList(HttpServletRequest request, Model model) {
  
    
    // 'page' 매개변수가 제공되지 않으면 1로 설정합니다.
    Optional<String> opt1 = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt1.orElse("1"));

    // 세션에서 'recordPerPage' 값을 가져옵니다. 세션에 없을 경우 10으로 기본값을 설정합니다.
    HttpSession session = request.getSession();
    Optional<Object> opt2 = Optional.ofNullable(session.getAttribute("recordPerPage"));
    int recordPerPage = (int)(opt2.orElse(10));

    // 'order' 매개변수가 제공되지 않으면 'DESC'로 설정합니다.
    Optional<String> opt3 = Optional.ofNullable(request.getParameter("order"));
    String order = opt3.orElse("DESC");

    // 'column' 매개변수가 제공되지 않으면 'FAQ_NO'로 설정합니다.
    Optional<String> opt4 = Optional.ofNullable(request.getParameter("orderColumn"));
    String orderColumn = opt4.orElse("QNA_NO");
    
    // 파라미터 query가 전달되지 않는 경우 query=""로 처리한다. (검색어)
    Optional<String> opt6 = Optional.ofNullable(request.getParameter("query"));
    String query = opt6.orElse("");
    
    // 파라미터 faqCategory가 전달되지 않는 경우 faqCategory=""로 처리한다. (검색어)
    Optional<String> opt7 = Optional.ofNullable(request.getParameter("kind"));
    String kind = opt7.orElse("");


    // 데이터베이스로 전달할 맵(Map)을 생성합니다.
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("query", query);
    map.put("kind", kind);
    int totalRecord = qnaMapper.getQnaCount(map);
    
    // column과 query를 이용해 검색된 레코드 개수를 구한다.
  
    int kindCount = qnaMapper.getQnaCategoryCount(kind);

    // 'recordPerPage' 값이 변경되었을 때, 현재 페이지의 데이터가 없는 경우를 확인한다.
    int totalPage = (int) Math.ceil((double) totalRecord / recordPerPage);
    if ((page - 1) * recordPerPage >= totalRecord) {
        page = Math.max(totalPage, 1);
    }
    

    // 페이지 유틸리티(PageUtil)를 계산합니다. (페이지네이션에 필요한 모든 정보 포함)
    pageUtil.setPageUtil(page, totalRecord, recordPerPage);
   
    map.put("begin", pageUtil.getBegin());
    map.put("order", order);
    map.put("recordPerPage", recordPerPage);
    map.put("orderColumn", orderColumn);
    
    // 지정된 범위(begin ~ end)의 목록을 가져옵니다.
  
    List<QnaDTO> qnaList = qnaMapper.getQnaList(map);
    
    // pagination.jsp로 전달할 정보를 저장합니다.
    model.addAttribute("qnaList",  qnaList);
    model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/customerCenter/qna.html?orderColumn=" + orderColumn + "&order=" + order + "&query=" + query + "&kind=" + kind));
    model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
    model.addAttribute("kindCount", kindCount);
    model.addAttribute("totalRecord", totalRecord);
    model.addAttribute("page", page);
    
   
    
  }
  
  @Override
  public int qnaModify(MultipartHttpServletRequest multipartRequest) {
    // TODO Auto-generated method stub
    return 0;
  }
  
  @Override
  public int qnaRemove(int qnaNo) {
    // TODO Auto-generated method stub
    return 0;
  }
  
  @Override
  public void getQnaByNo(int qnaNo, Model model) {
    // TODO Auto-generated method stub
    
  }

}