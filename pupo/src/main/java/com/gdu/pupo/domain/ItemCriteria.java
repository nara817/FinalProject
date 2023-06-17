package com.gdu.pupo.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemCriteria {

    private int pageNum;
    private int amount;
    
    private String type;
    private String keyword;
      
    private String date1 = ""; 
    private String date2 = ""; 
    private String orderStat;
    private String orderCode;
      
    private String id;
    
    public ItemCriteria() {
      this(1,10);
    }
    
    public ItemCriteria(int pageNum, int amount) {
      this.pageNum = pageNum;
      this.amount = amount;
    }
    
    public String[] getTypeArr() {
          
        return type == null? new String[] {} : type.split("");
    }
       
    public String getListLink() {
      UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
              .queryParam("pageNum", this.pageNum)
              .queryParam("amount", this.getAmount())
              .queryParam("type", this.getType())
              .queryParam("keyword", this.getKeyword());
          
       return builder.toUriString();
    }
}
