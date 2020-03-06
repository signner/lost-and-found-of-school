package com.shiro.web;

import com.shiro.myEnum.UPFILEPATH;
import com.shiro.pojo.LostFound;
import com.shiro.pojo.UserInfo;
import com.shiro.pojo.re.ExceptionType;
import com.shiro.pojo.re.MyException;
import com.shiro.pojo.re.ReturnMessage;
import com.shiro.service.LostFoundService;
import com.shiro.service.SchoolService;
import com.shiro.service.UserInfoService;
import com.shiro.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@RestController
public class LostFoundController {
    @Autowired
    LostFoundService lostFoundService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    SchoolService schoolService;
    @Value("${rows}")
    private int rows1;
    @Value("${urlPath.online}")
    private String online;
    @Value("${urlPath.local}")
    private String local;

    /*按照查询条件分页查询失物招领记录*/
    @SuppressWarnings("Duplicates")
    @RequestMapping("none/LostFound/list")
    public ReturnMessage queryLostFound(String school, String title, String goodsName, Date pickUpTime,
                                        String pickUpPlace, String connectPersonName,
                                        String telephone, Integer currentPage, Integer rows) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try {
            if (currentPage == null || currentPage == 0) currentPage = 1;
            if (title == null) title = "";
            Integer sid = 0;
            if (school != null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
            if (goodsName == null) goodsName = "";
            if (pickUpTime == null) pickUpTime = null;
            if (pickUpPlace == null) pickUpPlace = "";
            if (telephone == null) telephone = "";

            if (rows == null || rows == 0) lostFoundService.setRows(rows1);//为空，为0即默认
            else lostFoundService.setRows(rows);

            UserInfo userObj = new UserInfo();
            if (connectPersonName != null) userObj = userInfoService.findByUsername(connectPersonName);
            else userObj.setUid(0);
            Integer uid = Math.toIntExact(userObj.getUid());

            /*计算总的页数和总的记录数*/
            lostFoundService.queryTotalPageAndRecordNumber(sid, title, goodsName, pickUpTime, pickUpPlace, uid, telephone, 0);
            /*获取到总的页码数目*/
            int totalPage = lostFoundService.getTotalPage();
            /*当前查询条件下总记录数*/
            int recordNumber = lostFoundService.getRecordNumber();
            Map<String, Object> map = new HashMap<>();
            map.put("pages", totalPage);
            map.put("total", recordNumber);
            if (currentPage > totalPage && currentPage != 1) currentPage = totalPage;
            List<LostFound> lookingForList = lostFoundService.queryLostFound(sid, title, goodsName, pickUpTime, pickUpPlace, uid, telephone, 0, currentPage);
            map.put("list", lookingForList);
            r.setData(map);
            r.setMessage("查询成功!");
        } catch (Exception e) {
            throw new MyException(ExceptionType.select);
        }
        return r;
    }

    /*按照查询条件分页查询失物招领记录*/
    @SuppressWarnings("Duplicates")
    @RequestMapping("user/LostFound/list")
    public ReturnMessage queryLostFound1(String school, String title, String goodsName, Date pickUpTime,
                                         String pickUpPlace, String connectPersonName,
                                         String telephone, Integer state, Integer currentPage, Integer rows) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try {
            if (currentPage == null || currentPage == 0) currentPage = 1;
            if (title == null) title = "";
            Integer sid = 0;
            if (school != null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
            if (goodsName == null) goodsName = "";
            if (pickUpTime == null) pickUpTime = null;
            if (pickUpPlace == null) pickUpPlace = "";
            if (telephone == null) telephone = "";
            if (state == null) state = 0;

            if (rows == null || rows == 0) lostFoundService.setRows(rows1);//为空，为0即默认
            else lostFoundService.setRows(rows);

            UserInfo userObj = new UserInfo();
            if (connectPersonName != null) userObj = userInfoService.findByUsername(connectPersonName);
            else userObj.setUid(0);
            Integer uid = Math.toIntExact(userObj.getUid());

            /*计算总的页数和总的记录数*/
            lostFoundService.queryTotalPageAndRecordNumber(sid, title, goodsName, pickUpTime, pickUpPlace, uid, telephone, state);
            /*获取到总的页码数目*/
            int totalPage = lostFoundService.getTotalPage();
            /*当前查询条件下总记录数*/
            int recordNumber = lostFoundService.getRecordNumber();
            Map<String, Object> map = new HashMap<>();
            map.put("pages", totalPage);
            map.put("total", recordNumber);
            if (currentPage > totalPage && currentPage != 1) currentPage = totalPage;
            List<LostFound> lookingForList = lostFoundService.queryLostFound(sid, title, goodsName, pickUpTime, pickUpPlace, uid, telephone, state, currentPage);
            map.put("list", lookingForList);
            r.setData(map);
            r.setMessage("查询成功!");
        } catch (Exception e) {
            throw new MyException(ExceptionType.select);
        }
        return r;
    }

    /*返回此条件下一个用户的信息总数*/
    @RequestMapping("user/LostFound/all")
    public ReturnMessage queryAll(String school,  String username) throws Exception {
        ReturnMessage r = new ReturnMessage();
        Map<String, Object> map = new HashMap<>();
        try {
            String connectPersonName = username;
            Integer sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
            Integer uid = Math.toIntExact(userInfoService.findByUsername(connectPersonName).getUid());

            /*计算总的页数和总的记录数*/
            lostFoundService.queryTotalPageAndRecordNumber(sid, "", "", null, "", uid, "", 0);
            /*当前查询条件下总记录数*/
            int recordNumber = lostFoundService.getRecordNumber();

            /*计算总的页数和总的记录数*/
            lostFoundService.queryTotalPageAndRecordNumber(sid, "", "", null, "", uid, "", 1);
            /*当前查询条件下总记录数*/
            int recordNumber1 = lostFoundService.getRecordNumber();
            map.put("total", recordNumber+recordNumber1);

            r.setMessage("查询成功!");
        } catch (Exception e) {
            throw new Exception("school和username字段不能为空");
        }
        r.setData(map);
        return r;
    }



    /*根据主键获取失物招领记录*/
    @RequestMapping("none/LostFound/getById")
    public ReturnMessage getLostFound(Integer lostFoundId) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try {
            /*根据主键lookingForId获取LookingFor对象*/
            LostFound lookingFor = lostFoundService.getLostFound(lostFoundId);
            r.setData(lookingFor);
            r.setMessage("查询成功！");
        } catch (Exception e) {
           throw new MyException(ExceptionType.select);
        }
        return r;
    }

    /*更新失物招领记录*/
    @RequestMapping("user/LostFound/update")
    public ReturnMessage updateLostFound(int uid,String token,LostFound lostFound, @RequestParam(name = "imgFile", required = false) MultipartFile file) throws Exception {
        ReturnMessage r = new ReturnMessage();
        UserInfo userInfo = userInfoService.findByUsername(lostFound.getConnectPerson().getUsername());
        lostFound.setConnectPerson(userInfo);

        String sql_token = userInfoService.findByUid(uid).getToken();
        if (token.equals(sql_token)) {
            //有图片就替换文件
            if (file != null) {
                String goodsPhoto = FileUploadUtil.up(file, UPFILEPATH.lost, online, local);
                String _goodPhoto = lostFound.getGoodsPhoto();
                if (_goodPhoto.equals("") || _goodPhoto=="")
                    System.out.println("木有图片");
                else FileUploadUtil.del(_goodPhoto, local, online);
                lostFound.setGoodsPhoto(goodsPhoto);
            }

            lostFoundService.updateLostFound(lostFound);
            r.setMessage("更新成功！");
        }else{
            throw new MyException(ExceptionType.update);
        }
        return r;
    }

    /*删除失物招领记录,只能进行一条一条的删除,本用户只能删除本用户发布的信息*/
    @RequestMapping("user/LostFound/delete")
    public ReturnMessage deleteLostFound(int uid,String token,String lostFoundId) throws Exception {
        ReturnMessage r = new ReturnMessage();

        String sql_token = userInfoService.findByUid(uid).getToken();
        if (token.equals(sql_token)) {
            lostFoundService.deleteLostFounds(lostFoundId);
            r.setMessage("删除成功！");
        }else {
            throw new MyException(ExceptionType.delete);
        }
        return r;
    }

    /*删除失物招领记录*/
    @RequestMapping("admin/LostFound/delete")
    public ReturnMessage deleteLostFound1(String lostFoundIds) throws Exception { 
        ReturnMessage r = new ReturnMessage();
       try{
           lostFoundService.deleteLostFounds(lostFoundIds);
           r.setMessage("删除成功！");
       }catch (Exception e){
           throw new MyException(ExceptionType.delete);
       }

        return r;
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping("user/LostFound/add")
    public ReturnMessage addLostFound(LostFound lostFound, @RequestParam(name = "imgFile", required = false) MultipartFile file) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try{
            UserInfo userInfo = userInfoService.findByUsername(lostFound.getConnectPerson().getUsername());
            System.out.println(userInfo);
            lostFound.setConnectPerson(userInfo);
            if (file != null) {
                String goodsPhoto = FileUploadUtil.up(file, UPFILEPATH.lost, online, local);
                lostFound.setGoodsPhoto(goodsPhoto);
            }
            lostFoundService.addLostFound(lostFound);
            r.setMessage("添加成功！");
        }catch (Exception e){
            throw new MyException(ExceptionType.add);
        }
        return r;
    }
}
