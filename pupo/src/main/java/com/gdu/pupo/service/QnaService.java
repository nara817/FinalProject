package com.gdu.pupo.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface QnaService {
  
  public void getQnaList(HttpServletRequest request, Model model);
  public int qnaAdd(MultipartHttpServletRequest multipartRequest);
  public void getQnaByNo(int qnaNo, Model model);
  public int qnaModify(MultipartHttpServletRequest multipartRequest);
  public int qnaRemove(int qnaNo);
  /*
  public void loadBlogList(HttpServletRequest request, Model model);
  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest);
  public int increaseHit(int blogNo);
  public void loadBlog(int blogNo, Model model);
 */
}
