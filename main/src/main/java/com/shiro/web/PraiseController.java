package com.shiro.web;

import com.shiro.pojo.LookingFor;
import com.shiro.pojo.Praise;
import com.shiro.pojo.re.ReturnMessage;
import com.shiro.service.PraiseService;
import com.shiro.service.SchoolService;
import com.shiro.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
@RestController
public class PraiseController {
    @Autowired
    PraiseService praiseService;
    @Autowired
    SchoolService schoolService;
    @Autowired
    UserInfoService userInfoService;

    /*添加表扬记录*/
    @RequestMapping("user/praise/add")
    public ReturnMessage addPraise(Praise praise) throws Exception {
        ReturnMessage r = new ReturnMessage();
        r.setMessage("发布成功！");
        praiseService.addPraise(praise);
        return r;
    }

    /*按照查询条件分页查询表扬记录*/
    @RequestMapping("none/praise/list")
    public ReturnMessage queryPraise(Integer LookingForId, String praiseUserObj, String title, String school, Date addTime, Integer rows, Integer currentPage) throws Exception {
        ReturnMessage r = new ReturnMessage();
        LookingFor lookingFor = null;
        try {
            if (LookingForId != null) {
                lookingFor = new LookingFor();
                lookingFor.setLookingForId(LookingForId);
            }
            if (praiseUserObj == null) praiseUserObj = "";
            int sid = 0;
            if (school != null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
            if (title == null) title = "";
            if (addTime == null) addTime = null;
            if (rows == null || rows == 0) System.out.println("采用默认");
            else praiseService.setRows(rows);
            if (currentPage == null || currentPage == 0) currentPage = 1;

            praiseService.queryTotalPageAndRecordNumber(lookingFor, praiseUserObj, title, sid, addTime);
            /*获取到总的页码数目*/
            int totalPage = praiseService.getTotalPage();
            /*当前查询条件下总记录数*/
            int recordNumber = praiseService.getRecordNumber();
            Map<String, Object> map = new HashMap<>();
            map.put("pages", totalPage);
            map.put("total", recordNumber);

            if (currentPage > totalPage && currentPage != 1) currentPage = totalPage;
            ArrayList<Praise> praiseArrayList = praiseService.queryPraise(lookingFor, praiseUserObj, title, sid, addTime, currentPage);
            map.put("list", praiseArrayList);
            r.setData(map);
            r.setMessage("查询成功!");
        } catch (Exception e) {
            throw new Exception(e.getMessage() + ";查询失败！");
        }
        return r;
    }


    /*按照查询条件分页查询表扬记录的总数*/
    @RequestMapping("user/praise/all")
    public ReturnMessage queryAll(String username, String school) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try {
            String praiseUserObj = username;
            int sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());

            praiseService.queryTotalPageAndRecordNumber(null, praiseUserObj, "", sid, null);
            /*当前查询条件下总记录数*/
            int recordNumber = praiseService.getRecordNumber();
            Map<String, Object> map = new HashMap<>();
            map.put("total", recordNumber);
            r.setData(map);
            r.setMessage("查询成功!");
        } catch (Exception e) {
            throw new Exception("username和school字段不能为空！");
        }
        return r;
    }

//    /*查询所有表扬记录*/
//    @RequestMapping("none/praise/listAll")
//    public ReturnMessage queryAllPraise() throws Exception {
//        ReturnMessage r = new ReturnMessage();
//        r.setMessage("查询成功!");
//        r.setData(praiseService.queryAllPraise());
//        return r;
//    }


    /*根据主键获取表扬记录*/
    @RequestMapping("admin/praise/getById")
    public ReturnMessage getPraise(int praiseId) throws Exception {
        ReturnMessage r = new ReturnMessage();
        r.setMessage("查询成功!");
        r.setData(praiseService.getPraise(praiseId));
        return r;
    }

    /*更新表扬记录*/
    @RequestMapping("admin/praise/update")
    public ReturnMessage updatePraise(Praise praise) throws Exception {
        ReturnMessage r = new ReturnMessage();
        praiseService.updatePraise(praise);
        r.setMessage("更新成功!");
        return r;
    }

    /*更新表扬记录*/
    /*验证用户这是否是自己发布的信息*/
    @RequestMapping("user/praise/update")
    public ReturnMessage updatePraise1(Praise praise, int uid, String token) throws Exception {
        ReturnMessage r = new ReturnMessage();
        String sql_token = userInfoService.findByUid(uid).getToken();
        if (token.equals(sql_token)) {
            praiseService.updatePraise(praise);
            r.setMessage("更新成功!");
        }else{
            throw new Exception("更新失败！是否是token过期");
        }

        return r;
    }


    /*删除表扬信息*/
    @RequestMapping("admin/praise/delete")
    public ReturnMessage deletePraises(String praiseIds) throws Exception {
        ReturnMessage r = new ReturnMessage();
        praiseService.deletePraises(praiseIds);
        r.setMessage("删除成功!");
        return r;
    }

    /*删除表扬信息*/
    @RequestMapping("user/praise/delete")
    public ReturnMessage deletePraises1(String praiseIds, int uid, String token) throws Exception {
        ReturnMessage r = new ReturnMessage();
        String sql_token = userInfoService.findByUid(uid).getToken();
        if (token.equals(sql_token)) {
            praiseService.deletePraises(praiseIds);
            r.setMessage("删除成功!");
        }else{
            throw new Exception("删除失败！是否token过期");
        }
        return r;
    }
}
