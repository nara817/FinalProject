package com.gdu.pupo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO {
  private int couponNo;
  private String couponName;
  private double couponSale;
  private int couponUse; // 0 사용불가 1 사용
//  private Date couponBeginAt;
  private String couponBeginAt;
//  private Date couponEndAt;
  private String couponEndAt;
}
