package com.shiro.pojo;


import com.shiro.utils.StringUtil;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
public class LookingFor {

  private Integer lookingForId ;
  /*标题*/
  @NonNull
  private String title;
  /*丢失物品*/
  @NonNull
  private String goodsName;
  /*物品照片*/
  private String goodsPhoto="";
  /*丢失时间*/
  @NonNull
  private Date lostTime;
  /*丢失地点*/
  @NonNull
  private String lostPlace;
  /*物品描述*/
  @NonNull
  private String goodDesc;
  /*报酬*/
  @NonNull
  private String reward;
  @NonNull
  private String telephone;
  /*学生*/
  private UserInfo userObj;
  /*发布时间*/
  private Date addTime = StringUtil.getNow();
  /*认领状态*/
  private String state;
  public LookingFor(){}
  public LookingFor(String title,String goodsName,Date lostTime,String lostPlace,String goodDesc,String reward,String telephone){
    this.title = title;
    this.goodsName = goodsName;
    this.lostTime = lostTime;
    this.lostPlace = lostPlace;
    this.goodDesc = goodDesc;
    this.reward = reward;
    this.telephone = telephone;
  }
}
