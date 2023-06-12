package com.gdu.pupo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.pupo.domain.RegularDetailImgDTO;
import com.gdu.pupo.domain.RegularProductDTO;

@Mapper
public interface RegularMapper {

  public int addRegular(RegularProductDTO regularProductDTO); // 상품등록
  public int addRegImg(RegularDetailImgDTO regularDetailImgDTO); // 이미지 등록
  public List<RegularProductDTO> getRegularList(Map<String, Object> map); // 상품 리스트
  public List<RegularDetailImgDTO> getRegularImgList(); // 이미지 리스트 가져오기
  public int getRegularCount(); // 총 갯수
  public RegularDetailImgDTO getRegularImgByNo(int regularNo); // 이미지 불러오기
  public RegularProductDTO getRegularByNo(int regularNo); // 상세페이지
  
}
