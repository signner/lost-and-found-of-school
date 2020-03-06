package com.shiro.pojo;

import com.shiro.utils.StringUtil;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
public class Praise {
  /*表扬id*/
  private Integer praiseId;
  /*招领信息*/
  private LookingFor lookingFor;
  /*表扬人*/
  private UserInfo praiseUserObj;
  /*标题*/
  @NonNull
  private String title;
  /*表扬内容*/
  @NonNull
  private String contents;
  /*表扬时间*/
  @NonNull
  private Date addTime = StringUtil.getNow();
  public Praise(){}
}
