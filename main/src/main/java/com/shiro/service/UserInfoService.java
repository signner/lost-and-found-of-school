package com.shiro.service;

import com.shiro.mapper.CheckUsersMapper;
import com.shiro.mapper.UserInfoMapper;
import com.shiro.pojo.UserInfo;
import com.shiro.pojo.re.QueryOfElementUi;
import com.shiro.pojo.re.ReturnMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class UserInfoService {
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    CheckUsersMapper checkUsersMapper;

    /*每页显示记录数目*/
    @Value("${rows}")
    private int rows ;
    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }


    /*通过username查找一个对象*/
    public UserInfo findByUsername(String username) {
        return userInfoMapper.getUserInfoByUsername(username);
    }
    /*校验是否被注册*/
    public void checkUser(String field, String param) throws Exception {
        switch (field) {
            case "username":
                if (!(checkUsersMapper.checkUsername(param) == 0))
                    throw new Exception("此用户名已被注册");
                break;
            case "phone":
                if (!(checkUsersMapper.checkPhone(param) == 0))
                    throw new Exception("此号码已被注册");
                break;
            case "email":
                if (!(checkUsersMapper.checkEmail(param) == 0))
                    throw new Exception("此邮箱已被注册");
                break;
            default:
                break;
        }
    }
    /*注册用户*/
    public void logonUser(UserInfo users) {
        userInfoMapper.addUserInfo(users);
    }

    /*通过主键uid查询一个userinfo对象*/
    public UserInfo findByUid(Integer uid){return userInfoMapper.getUserInfo(uid);}
    /*添加一个用户*/
    public void addUserInfo(UserInfo userInfo){
        userInfoMapper.addUserInfo(userInfo);}
    /*更新信息*/
    public void updateUserInfo(UserInfo user){
        userInfoMapper.updateUserInfo(user);}
   /*删除用户*/
    public void deleteUserInfos(String uids)throws  Exception{
        String[] uid = uids.split(",");
        Arrays.asList(uid).forEach(u->{
            String username = userInfoMapper.getUserInfo(Integer.parseInt(u)).getUsername();
            userInfoMapper.deleteUserInfo(username);
        });
    }

    /*按照查询条件分页查询*/
    public ArrayList<UserInfo> queryUserInfoList(Integer uid,String username,String realName,
                                           String phone,String email, String sex,Integer sid,
                                           int currentPage) throws Exception {
        String where = "where 1=1";
        if (uid!=0) where = where + " and userInfo.uid like '%" + uid +"%'";
        if (!username.equals("")) where = where + " and userInfo.username like '%" + username +"%'";
        if (!realName.equals("")) where = where + " and userInfo.real_name like '%" + realName +"%'";
        if (!phone.equals("")) where = where + " and userInfo.phone like '%" + phone +"%'";
        if (!email.equals("")) where = where + " and userInfo.email like '%" + email +"%'";
        if (!sex.equals("")) where = where + " and userInfo.sex ='" + sex + "'";
        if (sid!=0) where = where + " and userInfo.sid ='" + sid + "'" ;
        int startIndex = (currentPage-1) * rows;
        return userInfoMapper.queryUserInfoList(where, startIndex, rows);
    }

    /*按照查询条件分页查询*/
    public ArrayList<UserInfo> queryUserInfo(Integer uid,String username,String realName,
                                                 String phone,String email, String sex,Integer sid) throws Exception {
        String where = "where 1=1";
        if (uid!=0) where = where + " and userInfo.uid like '%" + uid +"%'";
        if (!username.equals("")) where = where + " and userInfo.username like '%" + username +"%'";
        if (!realName.equals("")) where = where + " and userInfo.real_name like '%" + realName +"%'";
        if (!phone.equals("")) where = where + " and userInfo.phone like '%" + phone +"%'";
        if (!email.equals("")) where = where + " and userInfo.email like '%" + email +"%'";
        if (!sex.equals("")) where = where + " and userInfo.sex ='" + sex + "'";
        if (sid!=0) where = where + " and userInfo.sid ='" + sid + "'" ;
        return userInfoMapper.queryUserInfo(where);
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Integer uid,String username,String realName,
                                              String phone,String email, String sex,Integer sid) throws Exception {
        String where = "where 1=1";
        if (uid!=0) where = where + " and userInfo.uid like '%" + uid +"%'";
        if (!username.equals("")) where = where + " and userInfo.username like '%" + username +"%'";
        if (!realName.equals("")) where = where + " and userInfo.real_name like '%" + realName +"%'";
        if (!phone.equals("")) where = where + " and userInfo.phone like '%" + phone +"%'";
        if (!email.equals("")) where = where + " and userInfo.email like '%" + email +"%'";
        if (!sex.equals("")) where = where + " and userInfo.sex ='" + sex + "'";
        if (sid!=0) where = where + " and userInfo.sid ='" + sid + "'" ;
        recordNumber = userInfoMapper.queryUserInfoForCount(where);
        int mod = recordNumber % rows;
        totalPage = recordNumber / rows;
        if(mod != 0) totalPage++;
    }

    public String getToken (Integer uid){
      return userInfoMapper.getToken(uid);
    }
    public void setToken(String token, Integer uid){
        userInfoMapper.setToken(token,uid);
    }

    public List<QueryOfElementUi> searchOfLike(String school, String username) {
        return userInfoMapper.searchOfLike(school,"%"+username+"%");
    }
}
