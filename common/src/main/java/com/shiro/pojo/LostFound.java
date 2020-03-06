package com.shiro.pojo;

import com.shiro.utils.StringUtil;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
public class LostFound {
  /*招领id*/
  private Integer lostFoundId;
  /*标题*/
  @NonNull
  private String title;
  /*物品名称*/
  @NonNull
  private String goodsName;
  /*物品照片*/
  private String goodsPhoto="";
  /*捡得时间*/
  @NonNull
  private Date pickUpTime;
  /*拾得地点*/
  @NonNull
  private String pickUpPlace;
  /*描述说明*/
  @NonNull
  private String contents;
  /*联系人*/
  @NonNull
  private UserInfo connectPerson;
  /*联系电话*/
  @NonNull
  private String phone;
  /*发布时间*/
  private Date addTime = StringUtil.getNow();
  /*认领状态*/
  private String state;

  public LostFound(){}
}
