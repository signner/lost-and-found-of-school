package com.shiro.web;

import com.shiro.myEnum.UPFILEPATH;
import com.shiro.pojo.LookingFor;
import com.shiro.pojo.UserInfo;
import com.shiro.pojo.re.ExceptionType;
import com.shiro.pojo.re.MyException;
import com.shiro.pojo.re.ReturnMessage;
import com.shiro.service.LookingForService;
import com.shiro.service.SchoolService;
import com.shiro.service.UserInfoService;
import com.shiro.utils.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

//LookingFor管理控制层
@SuppressWarnings("ALL")
@RestController
@Slf4j
public class LookingForController {

    /*业务层对象*/
    @Autowired
    LookingForService lookingForService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    SchoolService schoolService;
    @Value("${rows}")
    private int rows1;
    //    @Value("${noneImg}")
//    private String noneImg;
    @Value("${urlPath.online}")
    private String online;
    @Value("${urlPath.local}")
    private String local;

    /**
     * 按照查询条件分页查询寻物启事记录
     *
     * @param school      查询本school的所有寻物启事
     *                    附带条件
     * @param title
     * @param goodsName
     * @param lostTime
     * @param lostPlace
     * @param telephone
     * @param username    【userObj.username】  lookingFor.userObj=userInfo.username
     * @param currentPage 查询第几页数据
     * @param rows        设置每页数据条目数
     * @return
     * @throws Exception
     */
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.GET},value = "none/LookingFor/list")
    public ReturnMessage list(@RequestParam(value = "school",required = false)String school,
                              @RequestParam(value = "title",required = false)String title,
                              @RequestParam(value = "goodsName",required = false)String goodsName,
                              @RequestParam(value = "lostTime",required = false)Date lostTime,
                              @RequestParam(value = "lostPlace",required = false)String lostPlace,
                              @RequestParam(value = "telephone",required = false)String telephone,
                              @RequestParam(value = "username",required = false)String username,
                              @RequestParam(value = "currentPage")Integer currentPage,
                              @RequestParam(value = "rows")Integer rows) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try {
            if (currentPage == null || currentPage == 0) currentPage = 1;
            if (title == null) title = "";
            Integer sid = 0;
            if (school != null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
            if (goodsName == null) goodsName = "";
            if (lostTime == null) lostTime = null;
            if (lostPlace == null) lostPlace = "";
            if (telephone == null) telephone = "";

            if (rows == null || rows == 0) lookingForService.setRows(rows1);//为空，为0即默认
            else lookingForService.setRows(rows);

            UserInfo userObj = new UserInfo();
            if (username != null) userObj = userInfoService.findByUsername(username);
            else userObj.setUid(0);
            Integer uid = Math.toIntExact(userObj.getUid());

            /*计算总的页数和总的记录数*/
            lookingForService.queryTotalPageAndRecordNumber(sid, title, goodsName, lostTime, lostPlace, telephone, uid, 0);
            /*获取到总的页码数目*/
            int totalPage = lookingForService.getTotalPage();
            /*当前查询条件下总记录数*/
            int recordNumber = lookingForService.getRecordNumber();
            Map<String, Object> map = new HashMap<>();
            map.put("pages", totalPage);
            map.put("total", recordNumber);
            if (currentPage > totalPage && currentPage != 1) currentPage = totalPage;
            List<LookingFor> lookingForList = lookingForService.queryLookingFor(sid, title, goodsName, lostTime, lostPlace, telephone, uid, 0, currentPage);
            map.put("list", lookingForList);
            r.setData(map);
            r.setMessage("查询成功!");
        } catch (Exception e) {
            throw new MyException(ExceptionType.select);
        }
        return r;
    }


    /*可以查询失物招领消息的状态*/
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.GET},value = "user/LookingFor/list")
    public ReturnMessage list1(@RequestParam(value = "school",required = false)String school,
                               @RequestParam(value = "title",required = false)String title,
                               @RequestParam(value = "goodsName",required = false)String goodsName,
                               @RequestParam(value = "lostTime",required = false)Date lostTime,
                               @RequestParam(value = "lostPlace",required = false)String lostPlace,
                               @RequestParam(value = "telephone",required = false)String telephone,
                               @RequestParam(value = "username",required = false)String username,
                               @RequestParam(value = "currentPage")Integer currentPage,
                               @RequestParam(value = "rows")Integer rows,
                               @RequestParam(value = "state")Integer state) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try {
            if (currentPage == null || currentPage == 0) currentPage = 1;
            if (title == null) title = "";
            Integer sid = 0;
            if (school != null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
            if (goodsName == null) goodsName = "";
            if (lostTime == null) lostTime = null;
            if (lostPlace == null) lostPlace = "";
            if (telephone == null) telephone = "";

            if (rows == null || rows == 0) lookingForService.setRows(rows1);//为空，为0即默认
            else lookingForService.setRows(rows);

            UserInfo userObj = new UserInfo();
            if (username != null) userObj = userInfoService.findByUsername(username);
            else userObj.setUid(0);
            Integer uid = Math.toIntExact(userObj.getUid());

            if (state==null) state=0;

            /*计算总的页数和总的记录数*/
            lookingForService.queryTotalPageAndRecordNumber(sid, title, goodsName, lostTime, lostPlace, telephone, uid, state);
            /*获取到总的页码数目*/
            int totalPage = lookingForService.getTotalPage();
            /*当前查询条件下总记录数*/
            int recordNumber = lookingForService.getRecordNumber();
            Map<String, Object> map = new HashMap<>();
            map.put("pages", totalPage);
            map.put("total", recordNumber);
            if (currentPage > totalPage && currentPage != 1) currentPage = totalPage;
            List<LookingFor> lookingForList = lookingForService.queryLookingFor(sid, title, goodsName, lostTime, lostPlace, telephone, uid, state, currentPage);
            map.put("list", lookingForList);
            r.setData(map);
            r.setMessage("查询成功!");
        } catch (Exception e) {
            throw new MyException(ExceptionType.select);
        }
        return r;
    }



    /*返回此条件下一个用户的信息总数*/
    @RequestMapping(method = {RequestMethod.POST},value = "user/LookingFor/all")
    public ReturnMessage all(@RequestParam(value = "school",required = false)String school,
                             @RequestParam(value = "username",required = false)String username) throws Exception {
        ReturnMessage r = new ReturnMessage();

        try {
            Integer sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
            Integer uid = Math.toIntExact(userInfoService.findByUsername(username).getUid());
            /*计算总的页数和总的记录数*/
            lookingForService.queryTotalPageAndRecordNumber(sid, "", "", null, "", "", uid, 0);
            /*当前查询条件下总记录数*/
            int recordNumber = lookingForService.getRecordNumber();

            lookingForService.queryTotalPageAndRecordNumber(sid, "", "", null, "", "", uid, 1);
            /*当前查询条件下总记录数*/
            int recordNumber1 = lookingForService.getRecordNumber();
//            System.out.println(recordNumber);
//            System.out.println(recordNumber1);
            Map<String, Object> map = new HashMap<>();
            map.put("total", recordNumber+recordNumber1);
            r.setData(map);
        } catch (Exception e) {
            throw new Exception("username和school字段不能为空！");
        }

        return r;
    }

//    /**
//     * 查询所有寻物启事信息
//     *
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("none/LookingFor/listAll")
//    public ReturnMessage listAll() throws Exception {
//        ReturnMessage r = new ReturnMessage();
//        try {
//            List<LookingFor> lookingForList = lookingForService.queryAllLookingFor();
//            r.setData(lookingForList);
//            r.setMessage("查询成功！");
//        } catch (Exception e) {
//            throw new MyException(ExceptionType.select);
//        }
//        return r;
//    }


    /**
     * 查询某一条id信息
     *
     * @param lookingForId
     * @return
     * @throws Exception
     */
    @RequestMapping("none/LookingFor/getById")
    public ReturnMessage frontshow(@RequestParam(value = "lookingForId")Integer lookingForId) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try {
            /*根据主键lookingForId获取LookingFor对象*/
            LookingFor lookingFor = lookingForService.getLookingFor(lookingForId);
            r.setData(lookingFor);
            r.setMessage("查询成功！");
        } catch (Exception e) {
            throw new MyException(ExceptionType.select);
        }
        return r;
    }

    //-------------------------------------update-------------------------------------------------------------
    /*更新寻物启事记录
     * 判断是不是同校信息*/
    @RequestMapping("admin/LookingFor/update")
    public ReturnMessage updateLookingFor(LookingFor lookingFor, Integer uid, String token, @RequestParam(name = "imgFile", required = false) MultipartFile file) throws Exception {
        ReturnMessage r = new ReturnMessage();
       try{
           UserInfo admin = userInfoService.findByUid(uid);
           String sql_token = admin.getToken();
           String school = admin.getSchool().getName();
           if (sql_token.equals(token))
               //有图片就替换文件
               if (file != null) {
                   String goodsPhoto = FileUploadUtil.up(file, UPFILEPATH.lost, online, local);
                   String _goodPhoto = lookingFor.getGoodsPhoto();
                   FileUploadUtil.del(_goodPhoto, local, online);
                   lookingFor.setGoodsPhoto(goodsPhoto);
               }

           lookingFor.setUserObj(userInfoService.findByUsername(lookingFor.getUserObj().getUsername()));
           lookingForService.updateLookingFor(lookingFor);
           r.setMessage("更新成功！");
       }catch (Exception e){
           log.error(e.getMessage());
           throw new MyException(ExceptionType.update);
       }
        return r;
    }

    /*更新寻物启事记录
     * 判断是不是本人信息*/


    @RequestMapping("user/LookingFor/update")
    public ReturnMessage updateLookingFor1(LookingFor lookingFor, Integer uid, String token, @RequestParam(name = "imgFile", required = false) MultipartFile file) throws Exception {
        ReturnMessage r = new ReturnMessage();

       try{
           String xiugai = userInfoService.findByUid(uid).getToken();
           //shiro会限制角色访问，能访问此的角色都是user
           //是否是正确的用户：每个用户都只能修改所属自己信息，uid和token确定唯一用户
           if (xiugai.equals(token) || xiugai == token) {

               //有图片就替换文件
               if (file != null) {
                   String goodsPhoto = FileUploadUtil.up(file, UPFILEPATH.lost, online, local);
                   String _goodPhoto = lookingFor.getGoodsPhoto();
                   FileUploadUtil.del(_goodPhoto, local, online);
                   lookingFor.setGoodsPhoto(goodsPhoto);
               }


               lookingFor.setUserObj(userInfoService.findByUsername(lookingFor.getUserObj().getUsername()));
               lookingForService.updateLookingFor(lookingFor);
               r.setMessage("更新成功！");

           }
       }catch (Exception e){
           log.error(e.getMessage());
           throw new MyException(ExceptionType.update);
       }
        return r;
    }


    //-------------------------------------delete-------------------------------------------------------------
    /*删除寻物启事记录,超级管理员免去其他验证*/
    @RequestMapping("guest/LookingFor/delete")
    public ReturnMessage deleteLookingFor1(String lookingForIds) throws Exception {
        ReturnMessage r = new ReturnMessage();

       lookingForService.deleteLookingFor(lookingForIds);

        r.setMessage("删除成功！");

        return r;
    }

    /*删除寻物启事记录，验证每一条信息是否是此管理员所属学校*/
    @RequestMapping("admin/LookingFor/delete")
    public ReturnMessage deleteLookingFor1(Integer uid, String token, String lookingForIds) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try{
            //先判断当前管理员是否合法
            UserInfo admin = userInfoService.findByUid(uid);
            String xiugai = admin.getToken();
            if (xiugai.equals(token) || xiugai == token)
            {
                String school = admin.getSchool().getName();
                //判断需要删除的信息是否都属于该管理员所在院校
                String[] ids = lookingForIds.split(",");
                for (String id:ids){
                    if (!lookingForService.getLookingFor(Integer.parseInt(id)).getUserObj().getSchool().getName().equals(school))
                        throw new Exception("id为"+id+"的信息不属于你所在院校，无法删除");
                }
                lookingForService.deleteLookingFor(lookingForIds);
                r.setMessage("删除成功！");
            }
        }catch (Exception e){
            throw new MyException(ExceptionType.delete);
        }
        return r;
    }

    /*删除寻物启事记录,验证消息是否是自己发布*/
    @RequestMapping("user/LookingFor/delete")
    public ReturnMessage deleteLookingFor(String lookingForId, String token) throws Exception {
        ReturnMessage r = new ReturnMessage();

        //用户token
        String jiaoyan = token;
        //发布此条消息的用户的token
        String _true = lookingForService.getLookingFor(Integer.parseInt(lookingForId)).getUserObj().getToken();
        //检验此条消息是否是本人所发布，是才能删除
        if (jiaoyan == _true || jiaoyan.equals(_true))
            lookingForService.deleteLookingFor(lookingForId);
        else
            throw new Exception("你不是本用户");
        r.setMessage("删除成功！");

        return r;
    }
    //-------------------------------------delete-------------------------------------------------------------



    @RequestMapping("admin/LookingFor/add")
    public ReturnMessage addLookingFor1(LookingFor lookingFor,@RequestParam(name = "imgFile", required = false) MultipartFile file) throws Exception {
        ReturnMessage r = new ReturnMessage();
        UserInfo userInfo = userInfoService.findByUsername(lookingFor.getUserObj().getUsername());
        lookingFor.setUserObj(userInfo);

        if (file != null) {
            String goodsPhoto = FileUploadUtil.up(file, UPFILEPATH.find, online, local);
            lookingFor.setGoodsPhoto(goodsPhoto);
        }

        lookingForService.addLookingFor(lookingFor);
        r.setMessage("添加成功!");
        return r;
    }

    @RequestMapping("user/LookingFor/add")
    public ReturnMessage addLookingFor(LookingFor lookingFor,String token, @RequestParam(name = "imgFile", required = false) MultipartFile file) throws Exception {
        ReturnMessage r = new ReturnMessage();
        UserInfo userInfo = userInfoService.findByUsername(lookingFor.getUserObj().getUsername());
        String sql_token = userInfo.getToken();
        System.out.println(sql_token);
        System.out.println(token);
            if (sql_token.equals(token)){
                lookingFor.setUserObj(userInfo);

                if (file != null) {
                    String goodsPhoto = FileUploadUtil.up(file, UPFILEPATH.find, online, local);
                    lookingFor.setGoodsPhoto(goodsPhoto);
                }

                lookingForService.addLookingFor(lookingFor);
                r.setMessage("添加成功!");
            }

        return r;
    }
}
