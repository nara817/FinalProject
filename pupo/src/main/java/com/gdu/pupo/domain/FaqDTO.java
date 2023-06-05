package com.gdu.pupo.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqDTO {
	
	  private int faqNo;
	  private int category;
	  private String title;
	  private String content;
	  private Date createdAt;
	  private Date modifiedAt;
	  private int hit;
	  private String id;

}
