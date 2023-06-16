package com.gdu.pupo.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
  private int cartCode;
  private String id;
  private int itemNo;
  private int itemCount;
  private Date madeAt;
}
