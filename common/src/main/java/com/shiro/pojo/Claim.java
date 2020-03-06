package com.shiro.pojo;

import com.shiro.utils.StringUtil;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
public class Claim {
  /*认领id*/
  private Integer claimId;
  /*招领信息*/
  @NonNull
  private LostFound lostFoundObj;
  /*认领人*/
  @NonNull
  private UserInfo personName;
  /*认领时间*/
  @NonNull
  private Date claimTime;
  /*描述说明*/
  private String contents;
  /*发布时间*/
  private Date addTime = StringUtil.getNow();

  public Claim(){}
  public Claim(LostFound lostFound,UserInfo userInfo, Date claimTime){
    lostFoundObj = lostFound;
    personName = userInfo;
    this.claimTime = claimTime;
  }
}
