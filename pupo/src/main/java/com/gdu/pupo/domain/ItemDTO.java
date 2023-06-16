package com.gdu.pupo.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
  
  private int itemNo;
  private String itemName;
  private String itemCategory;
  private String itemComment;
  private int price;
  private int originPrice;
  private int stock;
  private String dispStat;
  private String saleStat;
  
  
  private Date regDate;
  private Date updateDate;
  
  
  
  /*
   * private int itemCode; private String itemName; private String itemContent;
   * private int price; private int normPrice; private int stock; private String
   * dispStat; private String saleStat; private String itemChr; private String
   * itemImg1; private String itemImg2; private String itemImg3; private String
   * itemImg4; private String imgDetail; private Timestamp regDate; private
   * Timestamp updateDate;
   * 
   * 
   * private String classCode; private String id;
   */
}
