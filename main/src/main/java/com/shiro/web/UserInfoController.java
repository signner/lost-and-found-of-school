package com.shiro.web;

import com.shiro.myEnum.UPFILEPATH;
import com.shiro.pojo.UserInfo;
import com.shiro.pojo.re.ExceptionType;
import com.shiro.pojo.re.MyException;
import com.shiro.pojo.re.QueryOfElementUi;
import com.shiro.pojo.re.ReturnMessage;
import com.shiro.service.SchoolService;
import com.shiro.service.UserInfoService;
import com.shiro.utils.FileUploadUtil;
import com.shiro.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserInfoController {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    SchoolService schoolService;
    @Value("${rows}")
    private int rows1;
//    @Value("${noneImg}")
//    private String img;
    @Value("${salt}")
    private String salt;
    @Value("${urlPath.online}")
    private String online;
    @Value("${urlPath.local}")
    private String local;

    /*通过主键uid查询一个userinfo对象*/
    @RequestMapping("admin/userInfo/getById")
    public ReturnMessage findByUid(Integer uid) {
        ReturnMessage r = new ReturnMessage();
        String msg = "";
        try {
            r.setData(userInfoService.findByUid(uid));
            msg = "查询成功！";
        } catch (Exception e) {
            r.setCode(500);
            msg = e.getMessage() + "查询失败！";
        }
        r.setMessage(msg);
        return r;
    }

    /*校验是否被注册*/
    @RequestMapping("none/userInfo/checkUser")
    public ReturnMessage checkUser(String field, String param) {
        ReturnMessage r = new ReturnMessage();
        String msg = "";
        try {
            userInfoService.checkUser(field, param);
        } catch (Exception e) {
            r.setCode(500);
            msg = "此" + param + "已被注册！";
        }
        r.setMessage(msg);
        return r;
    }

    /*注册用户*/
    @RequestMapping("none/userInfo/logon")
    public ReturnMessage logonUser(UserInfo users) {
        ReturnMessage r = new ReturnMessage();
        String msg = "";
        try {
            userInfoService.addUserInfo(users);
            msg = "注册成功！";
        } catch (Exception e) {
            r.setCode(500);
            msg = e.getMessage() + "注册失败！";
        }
        r.setMessage(msg);
        return r;
    }


    /*添加一个用户*/
    @RequestMapping("admin/userInfo/add")
    public ReturnMessage addUserInfo(UserInfo userInfo) {
        ReturnMessage r = new ReturnMessage();
        userInfoService.addUserInfo(userInfo);
        r.setMessage("添加成功！");
        return r;
    }

    /*更新信息*/
    @RequestMapping("user/userInfo/update")
    public ReturnMessage updateUserInfo(UserInfo user, @RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile) throws Exception {
        ReturnMessage r = new ReturnMessage();
        String token = userInfoService.getToken(Math.toIntExact(user.getUid()));
        String msg = "";

        if(token.equals(user.getToken())||token==user.getToken()){
           // 更新基础信息,用户名不能修改和其它同名，邮箱(无用)也是，还有电话
            //-----用户名，刚上传过来还没有进行更新。以新名字获取一个新对象，如果为空则可以更新。旧名字的uid会与之相同
            if (user.getUsername() != null){
                UserInfo userInfo1 = null;
                try {
                    userInfo1 = userInfoService.findByUsername(user.getUsername());//为空会直接抛出
                    System.out.println(userInfo1.getUid() != userInfo1.getUid());
                    if (userInfo1.getUid() != user.getUid())//userinfo1不为空且若uid等于用户的uid，说明此用户名未修改
                        throw new MyException(ExceptionType.update);
                } catch (NullPointerException e) {
                    System.out.println("此用户名可以使用");
                }catch (MyException e){
                    throw new Exception(e.getMessage());
                }
            }

            System.out.println(user.getPassword() != null);
            if (user.getPassword() != null)
            {
                String old = userInfoService.findByUid(Math.toIntExact(user.getUid())).getPassword();
                if (!user.getPassword().equals(old)) {
                    String _new = PasswordUtil.convertMD5(user.getPassword(), salt);
                    if (_new.equals(old))
                        throw new Exception("新密码不能与旧密码相同！");
                    else
                        user.setPassword(_new);
                }
            }
//        更新头像
            if (avatarFile != null) {
                String avatarUrl = FileUploadUtil.up(avatarFile, UPFILEPATH.avatar, online, local);
                FileUploadUtil.del(user.getAvatar(), local, online);
                user.setAvatar(avatarUrl);
            }

            userInfoService.updateUserInfo(user);
            r.setData(userInfoService.findByUid(Math.toIntExact(user.getUid())));
            msg = "更新成功！";
        }else{
            msg = "你不是当前用户！请不要擅自更改他人信息";
        }
        r.setMessage(msg);
        return r;
    }

    /*删除用户*/
    @RequestMapping("admin/userInfo/delete")
    public ReturnMessage deleteUserInfos(String uids) throws  Exception{
        ReturnMessage r = new ReturnMessage();
        userInfoService.deleteUserInfos(uids);
        r.setMessage("删除成功！");
        return r;
    }

    @RequestMapping("admin/userInfo/list")
    public ReturnMessage queryUserInfoList(Integer uid, String username, String realName,
                                           String phone, String email, String sex, String school,
                                           Integer currentPage, Integer rows) throws Exception {
        ReturnMessage r = new ReturnMessage();
        Map<String, Object> map = new HashMap<>();
        Integer sid = 0;
        String msg = "";
        try {
            if (uid == null) uid = 0;
            if (username == null) username = "";
            if (realName == null) realName = "";
            if (phone == null) phone = "";
            if (email == null) email = "";
            if (sex == null) sex = "";
            if (school == null) sid = 0;
            else sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
            if (currentPage == null || currentPage == 0) currentPage = 1;
            if (rows != null && rows != 0) userInfoService.setRows(rows);
            else userInfoService.setRows(rows1);

            userInfoService.queryTotalPageAndRecordNumber(uid, username, realName, phone, email, sex, sid);
            Integer totalPage = userInfoService.getTotalPage();
            Integer total = userInfoService.getRecordNumber();
            rows = userInfoService.getRows();
            System.out.println("总页数：" + totalPage);
            System.out.println("总条数：" + total);
            System.out.println("每页总数：" + rows);
            map.put("total", total);
            map.put("pages", totalPage);
            if (currentPage > totalPage && currentPage != 1) currentPage = totalPage;
            ArrayList<UserInfo> list = userInfoService.
                    queryUserInfoList(uid, username, realName, phone, email, sex, sid, currentPage);
            map.put("rows", rows);
            map.put("list", list);
            r.setData(map);
            msg = "查询成功!";
        } catch (Exception e) {
            r.setCode(500);
            msg = e.getMessage() + ";查询失败!";
        }
        r.setMessage(msg);
        return r;
    }

    @RequestMapping("user/userInfo/search")
    public List<QueryOfElementUi> searchOfLike(@RequestParam(name = "school") String school , String username){
        if (username==null) username="";
        return userInfoService.searchOfLike(school,username);
    }
}
