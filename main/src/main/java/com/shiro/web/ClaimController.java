package com.shiro.web;

import com.shiro.pojo.Claim;
import com.shiro.pojo.LostFound;
import com.shiro.pojo.re.ReturnMessage;
import com.shiro.service.ClaimService;
import com.shiro.service.LostFoundService;
import com.shiro.service.SchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
@RestController
public class ClaimController {

    @Autowired
    ClaimService claimService;
    @Autowired
    LostFoundService lostFoundService;
    @Autowired
    SchoolService schoolService;


    /*添加认领记录*/
    @RequestMapping(method = {RequestMethod.POST},value = "user/claim/add")
    public ReturnMessage addClaim(Claim claim) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try{
            claimService.addClaim(claim);
            r.setMessage("添加成功！");
        }catch (Exception e){
            r.setCode(500);
            r.setMessage(e.getMessage()+";添加失败！");
        }
        return r;
    }

    /**
     * 按照查询条件分页查询认领记录
     * @param lostFoundObj
     * @param personName
     * @param school
     * @param claimTime
     * @param rows
     * @param currentPage
     * @return
     * @throws Exception
     */
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.GET},value = "none/claim/list")
    public ReturnMessage queryClaim(@RequestParam(value = "lostFoundObj",required = false) Integer lostFoundObj,
                                    @RequestParam(value = "personName",required = false) String personName,
                                    @RequestParam(value = "school",required = false) String school,
                                    @RequestParam(value = "claimTime",required = false) Date claimTime,
                                    @RequestParam(value = "rows",required = true) Integer rows,
                                    @RequestParam(value = "currentPage",required = true) Integer currentPage) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try{
            LostFound lostFound = null;
            if (lostFoundObj==null) lostFoundObj=null;
            else lostFound.setLostFoundId(lostFoundObj);
            if (personName==null) personName="";
            if (claimTime==null) claimTime=null;
            if (rows==null || rows==0) System.out.println("采用默认");
            else claimService.setRows(rows);
            int sid = 0;
            if (school!=null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
            if (currentPage==null || currentPage==0) currentPage=1;

            claimService.queryTotalPageAndRecordNumber(lostFound,personName,claimTime,sid);
            /*获取到总的页码数目*/
            int totalPage = claimService.getTotalPage();
            /*当前查询条件下总记录数*/
            int recordNumber = claimService.getRecordNumber();
            Map<String, Object> map = new HashMap<>();
            map.put("pages", totalPage);
            map.put("total", recordNumber);

            if (currentPage>totalPage && currentPage!=1) currentPage=totalPage;
            ArrayList<Claim> claimArrayList = claimService.queryClaim(lostFound,personName,claimTime,sid,currentPage);

//            List<Claim> _claimArrayList = claimArrayList.stream().filter(claim -> { return  claim.getPersonName().getSchool().getName().equals(school);}).collect(Collectors.toList());

            map.put("list", claimArrayList);
            r.setData(map);
            r.setMessage("查询成功!");
        }catch (Exception e){
            r.error(e);
        }
        return  r;
    }


    //查询当前角色所有的claim消息
    @ApiOperation(value = "查询claim信息数目",notes = "通过username和school查询当前角色所有的claim消息数目")
    @RequestMapping(method = {RequestMethod.POST},value = "user/claim/all")
    public ReturnMessage queryAll(@RequestParam("username") String username,
                                  @RequestParam("school") String school ) throws Exception {
        ReturnMessage r = new ReturnMessage();
        Map<String, Object> map = new HashMap<>();
        try{
            String personName = username;
            int sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
            claimService.queryTotalPageAndRecordNumber(null,personName,null,sid);
            /*当前查询条件下总记录数*/
            int recordNumber = claimService.getRecordNumber();
            map.put("total", recordNumber);
            r.setMessage("查询成功!");
        }catch (Exception e){
           throw new Exception("username和school字段不能为空！");
        }
        r.setData(map);
        return  r;
    }

//    /*查询所有认领记录*/
//    @RequestMapping(method = {RequestMethod.POST,RequestMethod.GET},value = "user/claim/listAll")
//    public ReturnMessage queryAllClaim()  throws Exception {
//        ReturnMessage r = new ReturnMessage();
//        try{
//            r.setData(claimService.queryAllClaim());
//            r.setMessage("查询成功！");
//        }catch (Exception e){
//            r.error(e);
//        }
//        return r;
//    }
    

    /*根据主键获取认领记录*/
    @RequestMapping(method = {RequestMethod.POST},value = "admin/claim/getById")
    public ReturnMessage getClaim(int claimId) throws Exception  {
        ReturnMessage r = new ReturnMessage();
        try{
            Claim claim = claimService.getClaim(claimId);
            r.setData(claim);
            r.setMessage("查询成功！");
        }catch (Exception e){
            r.error(e);
        }
        return r;
    }

    /*更新认领记录*/
    @RequestMapping(method = {RequestMethod.POST},value = "admin/claim/update")
    public ReturnMessage updateClaim(Claim claim) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try{
            claimService.updateClaim(claim);
            r.setMessage("更新成功！");
        }catch (Exception e){
            r.error(e);
        }
        return r;
    }

    /*删除一条认领记录*/
    @RequestMapping(method = {RequestMethod.POST},value = "admin/claim/delete")
    public ReturnMessage deleteClaim (String claimIds) throws Exception {
        ReturnMessage r = new ReturnMessage();
        try{
            claimService.deleteClaim(claimIds);
            r.setMessage("删除成功！");
        }catch (Exception e){
            r.error(e);
        }
        return r;
    }
}
