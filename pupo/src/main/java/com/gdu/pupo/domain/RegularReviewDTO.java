package com.gdu.pupo.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegularReviewDTO {
  private int regReviewNo;
  private int regPurchaseNo;
  private String regReviewDetail;
  private String regReviewImgPath;
  private int regReviewRating;
  private Date regReviewCreatedAt;
  private Date regReviewModifiedAt;
}
