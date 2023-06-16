package com.gdu.pupo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.pupo.domain.RegularCategoryDTO;
import com.gdu.pupo.domain.RegularDetailImgDTO;
import com.gdu.pupo.domain.RegularMainImgDTO;
import com.gdu.pupo.domain.RegularProductDTO;
import com.gdu.pupo.domain.RegularPurchaseDTO;

@Mapper
public interface RegularMapper {

  public int addRegular(RegularProductDTO regularProductDTO); // 상품등록
  public int addRegCategory(RegularCategoryDTO regularCategoryDTO); // 카테고리 등록
  public List<RegularCategoryDTO> getRegCategoryList();        // 카테고리 목록 가져오기
  public int addRegImg(RegularDetailImgDTO regularDetailImgDTO); // 이미지 등록
  public int addRegMainImg(RegularMainImgDTO regularMainImgDTO); // 이미지 등록
  public List<RegularProductDTO> getRegularList(Map<String, Object> map); // 상품 리스트
  public List<RegularDetailImgDTO> getRegularImgList(); // 이미지 리스트 가져오기
  public RegularDetailImgDTO getRegularImgByNo(int regularNo); // 이미지 불러오기
  public List<RegularMainImgDTO> getRegularMainImgList(); // 메인이미지리스트가져오기
  public RegularMainImgDTO getRegularMainImgByNo(int regularNo); // 메인이미지 가져오기
  public int getRegularCount(); // 총 갯수
  public RegularProductDTO getRegularByNo(int regularNo); // 상세페이지
  public int addRegPurchase(RegularPurchaseDTO regularPurchaseDTO); // 주문완료 주문정보 저장
  public RegularPurchaseDTO getRegularPurchaseByNo(int regPurchaseNo); // 주문완료 시 정보 보여주기
  public List<RegularPurchaseDTO> regularPayList(); // 정기구독 상태가 1인 주문정보
  public int regularPayUpdate(int regPurchaseNo); // 정기구독 자동결제 후 결제된 횟수 증가 및 마지막 결제 업데이트
}
