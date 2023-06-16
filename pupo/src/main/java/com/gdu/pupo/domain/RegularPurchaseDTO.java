package com.gdu.pupo.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegularPurchaseDTO {
  private int regPurchaseNo;
  private String id;
  private String regCustomerUid;
  private int regShipNo;
  private int regularNo;
  private int regPurchasePrice;
  private int regPurchaseLastPrice;
  private Date regPurchaseAt;
  private String regPg;
  private int regDeliveryStatus;
  private int regProductCount;
  private Date regPaymentAt;
  private int regDeliveryDay;
  private int regPayStatus;
  private Date regLastPayAt;
}
