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
    private String id;
    private int qnaCategory;
    private String qnaTitle;
    private String qnaIp;
    private String qnaContent;
    private Date qnaCreatedAt;
    private Date qnaModifiedAt;
    private int qnaHit;
    private int qnaState;
    private int qnaDepth;
    private int qnaGroupNo;
    private int qnaGroupOrder;
    private int qnaMailAlarmCheck
    ;


}
