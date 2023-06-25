package com.gdu.pupo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.pupo.domain.QnaAttachDTO;
import com.gdu.pupo.domain.QnaDTO;

@Mapper
public interface QnaMapper {

  public List<QnaDTO> getQnaList(Map<String, Object> map);
  public int qnaAdd(QnaDTO qnaDTO);
  public int qnaAddAttach(QnaAttachDTO qnaAttachDTO);
  public int getQnaCount(Map<String, Object> map);
  public int getQnaCategoryCount(String kind);
 
}
