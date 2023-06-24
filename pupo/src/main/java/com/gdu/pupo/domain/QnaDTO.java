package com.gdu.pupo.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaDTO {
	
	  private int qnaNo;
	  private int sellerCheck;
    private String id;
    private String qnaCategory;
    private String qnaTitle;
    private String qnaContent;
    private String qnaEmail;
    private Date qnaCreatedAt;
    private Date qnaModifiedAt;
    private int qnaEmailAlarmCheck;
    private int qnaHit;
    private int qnaState;
    private int qnaDepth;
    private int qnaGroupNo;
    private int qnaGroupOrder;
    


}
