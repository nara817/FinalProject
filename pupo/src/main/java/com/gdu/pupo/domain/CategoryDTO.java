package com.gdu.pupo.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
  private String classCode;
  private String mainCategory;
  private String midCategory;
  private String fullPath;
  private int lv;
}
