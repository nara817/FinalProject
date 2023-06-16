package com.gdu.pupo.domain;

import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterItemDTO {
  /*
  private int itemCode;
  private String itemName;
  private String itemContent;
  private int price;
  private int normPrice;
  private int stock;
  private String dispStat;
  private String saleStat;
  private String itemChr;
  private String itemImg1;
  private String itemImg2;
  private String itemImg3;
  private String itemImg4;
  private String imgDetail;
  private Timestamp regDate;
  private Timestamp updateDate;
  
  
  private String classCode;
  private String id;
  */
  
  private int itemNo;
  private String itemName;
  private int itemCategory;
  private String itemComment;
  private int price;
  private int originPrice;
  private int stock;
  private int dispStat;
  private int saleStat;
  private String itemImg1;
  private String itemImg2;
  
  private Date regDate;
  private Date updateDate;
}
