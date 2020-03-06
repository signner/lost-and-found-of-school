package com.shiro.web;

import com.shiro.pojo.*;
import com.shiro.service.*;
import com.shiro.utils.ExportExcelUtil;
import com.shiro.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Controller
@Api("导出excel")
@Slf4j
public class ExportExcelController {
    @Autowired
    PraiseService praiseService;
    @Autowired
    ClaimService claimService;
    @Autowired
    LookingForService lookingForService;
    @Autowired
    LostFoundService lostFoundService;
    //在此当工具，不做导出
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    SchoolService schoolService;

    /*按照查询条件导出表扬信息到Excel*/
    @ApiOperation(value = "导出praise")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "lookingForId", value = "寻物启事id", paramType = "query", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "title", value = "表扬信息标题", paramType = "query", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "praiseUserObj", value = "表扬信息发布者", paramType = "query", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "school", value = "表扬信息发布者所在院校", paramType = "query", required = true, dataType = "Date"),
//            @ApiImplicitParam(name = "addTime", value = "表扬信息发布时间", paramType = "query", required = true, dataType = "String")
//    })
    @RequestMapping(value = { "user/export/praise" }, method = {RequestMethod.GET,RequestMethod.POST})
    public void OutToExcelPraise(Integer lookingForId,String praiseUserObj, String title,String school, Date addTime, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LookingFor lookingFor = null;
        if (lookingForId!=null) {lookingFor = new LookingFor();lookingFor.setLookingForId(lookingForId);}
        if (praiseUserObj==null) praiseUserObj="";
        int sid = 0;
        if (school!=null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
        if(title == null) title = "";
        if(addTime == null) addTime = null;
        List<Praise> praiseList = praiseService.queryPraise(lookingFor,praiseUserObj,title,sid,addTime);
        if (praiseList==null || praiseList.size()==0) throw new NullPointerException("没有数据");
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Praise信息记录";
        String[] headers = { "表扬id","招领信息","标题","表扬内容","表扬时间"};
        List<String[]> dataset = new ArrayList<String[]>();
        for(int i=0;i<praiseList.size();i++) {
            Praise praise = praiseList.get(i);
            dataset.add(new String[]{praise.getPraiseId() + "",praise.getLookingFor().getTitle(),
                    praise.getTitle(),praise.getContents(),StringUtil.dateTimeToString(praise.getAddTime())});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
        OutputStream out = null;//创建一个输出流对象
        try {
            out = response.getOutputStream();//
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-disposition","attachment; filename="+"Praise.xls");//filename是下载的xls的名，建议最好用英文
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//设置类型
            response.setHeader("Pragma","No-cache");//设置头
            response.setHeader("Cache-Control","no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            ExportExcelUtil.exportExcel(rootPath,_title,headers, dataset, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(out!=null){
                    out.close();
                }
//                System.out.println("praise导出完成");
                log.info("praise导出完成");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /*按照查询条件导出寻物启事信息到Excel*/
    /*user权限不允许查看已处理的信息*/
    @ApiOperation(value = "导出Lookingfor")
    @RequestMapping(value = { "user/export/lookingfor" }, method = {RequestMethod.GET,RequestMethod.POST})
    public void OutToExcelLookingfor(String school, String title, String goodsName, Date lostTime, String lostPlace,
                           String telephone, String username,HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(title == null) title = "";
        if(goodsName == null) goodsName = "";
        if(lostTime == null) lostTime = null;
        if(lostPlace == null) lostPlace = "";
        if(telephone == null) telephone = "";
        Integer sid = 0;
        if (school != null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
        UserInfo userObj = new UserInfo();
        if (username != null) userObj = userInfoService.findByUsername(username);
        else userObj.setUid(0);
        Integer uid = Math.toIntExact(userObj.getUid());
        List<LookingFor> lookingForList = lookingForService.queryLookingFor(sid, title, goodsName, lostTime, lostPlace, telephone, uid,0);
        if (lookingForList==null || lookingForList.size()==0) throw new NullPointerException("没有数据");
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "LookingFor信息记录";
        String[] headers = { "寻物id","标题","丢失物品","物品照片","丢失时间","丢失地点","报酬","联系电话","学生","发布时间","是否已解决"};
        List<String[]> dataset = new ArrayList<String[]>();
        for(int i=0;i<lookingForList.size();i++) {
            LookingFor lookingFor = lookingForList.get(i);
            String photo = lookingFor.getGoodsPhoto().equals("http://localhost/temp/%E4%B8%8B%E8%BD%BD.jpg")?"无照片":lookingFor.getGoodsPhoto();
            String state = lookingFor.getState().equals("1")?"已解决":"待解决";
            dataset.add(new String[]{lookingFor.getLookingForId() + "",lookingFor.getTitle(),lookingFor.getGoodsName()
                    ,StringUtil.dateTimeToString(lookingFor.getLostTime()),photo,
                    lookingFor.getLostPlace(),lookingFor.getReward(),lookingFor.getTelephone(),
                    lookingFor.getUserObj().getUsername(),StringUtil.dateTimeToString(lookingFor.getAddTime()),state});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
        OutputStream out = null;//创建一个输出流对象
        try {
            out = response.getOutputStream();//
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-disposition","attachment; filename="+"LookingFor.xls");//filename是下载的xls的名，建议最好用英文
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//设置类型
            response.setHeader("Pragma","No-cache");//设置头
            response.setHeader("Cache-Control","no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            ExportExcelUtil.exportExcel(rootPath,_title,headers, dataset, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(out!=null){
                    out.close();
                }
//                System.out.println("lookingfor导出完成");
                log.info("lookingfor导出完成");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /*按照查询条件导出失物招领信息到Excel*/
    /*user权限不允许查看已处理的信息*/
    @RequestMapping(value = { "user/export/lostfound" }, method = {RequestMethod.GET,RequestMethod.POST})
    public void OutToExcelLostfound(String school, String title, String goodsName, Date pickUpTime, String pickUpPlace,
                                    String connectPersonName, String telephone,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(title == null) title = "";
        if(goodsName == null) goodsName = "";
        if(pickUpTime == null) pickUpTime = null;
        if(pickUpPlace == null) pickUpPlace = "";
        Integer sid = 0;
        if (school != null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
        UserInfo userObj = new UserInfo();
        if (connectPersonName != null) userObj = userInfoService.findByUsername(connectPersonName);
        else userObj.setUid(0);
        Integer uid = Math.toIntExact(userObj.getUid());
        if(telephone == null) telephone = "";
        List<LostFound> lostFoundList = lostFoundService.queryLostFound(sid, title, goodsName, pickUpTime, pickUpPlace,  uid,telephone,0);
        if (lostFoundList==null || lostFoundList.size()==0) throw new NullPointerException("没有数据");
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "LostFound信息记录";
        String[] headers = { "招领id","标题","图片","物品名称","捡得时间","拾得地点","联系人","联系电话","发布时间","是否已解决"};
        List<String[]> dataset = new ArrayList<String[]>();
        for(int i=0;i<lostFoundList.size();i++) {
            LostFound lostFound = lostFoundList.get(i);
            String photo = lostFound.getGoodsPhoto().equals("http://localhost/temp/%E4%B8%8B%E8%BD%BD.jpg")?"无照片":lostFound.getGoodsPhoto();
            String state = lostFound.getState().equals("1")?"已解决":"待解决";
            dataset.add(new String[]{lostFound.getLostFoundId() + "",lostFound.getTitle(),photo,lostFound.getGoodsName(),
                    StringUtil.dateTimeToString(lostFound.getPickUpTime()),lostFound.getPickUpPlace(),
                    lostFound.getConnectPerson().getUsername(),lostFound.getPhone(),StringUtil.dateTimeToString(lostFound.getAddTime()),state});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
        OutputStream out = null;//创建一个输出流对象
        try {
            out = response.getOutputStream();//
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-disposition","attachment; filename="+"LostFound.xls");//filename是下载的xls的名，建议最好用英文
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//设置类型
            response.setHeader("Pragma","No-cache");//设置头
            response.setHeader("Cache-Control","no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            ExportExcelUtil.exportExcel(rootPath,_title,headers, dataset, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(out!=null){
                    out.close();
                }
//                System.out.println("lostfound导出完成");
                log.info("lostfound导出完成");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /*按照查询条件导出认领信息到Excel*/
    @RequestMapping(value = { "user/export/claim" }, method = {RequestMethod.GET,RequestMethod.POST})
    public void OutToExcelClaim(Integer lostFoundObj, String personName,String school,
                                Date claimTime, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LostFound lostFound = null;
        if (lostFoundObj==null) lostFoundObj=null;
        else lostFound.setLostFoundId(lostFoundObj);
        if (personName==null) personName="";
        if (claimTime==null) claimTime=null;
        int sid = 0;
        if (school==null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
        List<Claim> claimList = claimService.queryClaim(lostFound,personName,claimTime,sid);
        if (claimList==null || claimList.size()==0) throw new NullPointerException("没有数据");
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Claim信息记录";
        String[] headers = { "认领id","招领信息","认领人","认领时间","发布时间"};
        List<String[]> dataset = new ArrayList<String[]>();
        List<Claim> _claimArrayList = claimList.stream().filter(claim -> { return  claim.getPersonName().getSchool().getName().equals(school);}).collect(Collectors.toList());
//        for(int i=0;i<_claimArrayList.size();i++) {
//            Claim claim = claimList.get(i);
//            dataset.add(new String[]{claim.getClaimId() + "",claim.getLostFoundObj().getTitle(),
//                    claim.getPersonName().getUsername(),StringUtil.dateTimeToString(claim.getClaimTime()),
//                    StringUtil.dateTimeToString(claim.getAddTime())});
//        }
        _claimArrayList.forEach(claim ->  {
            String id = claim.getClaimId()+"";
            String title = claim.getLostFoundObj().getTitle();
            String _personName = claim.getPersonName().getUsername();
            String _claimTime = StringUtil.dateTimeToString(claim.getClaimTime());
            String _addTime =StringUtil.dateTimeToString(claim.getAddTime());
            dataset.add(new String[]{ id,title,_personName,_claimTime,_addTime});});
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
        OutputStream out = null;//创建一个输出流对象
        try {
            out = response.getOutputStream();//
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-disposition","attachment; filename="+"Claim.xls");//filename是下载的xls的名，建议最好用英文
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//设置类型
            response.setHeader("Pragma","No-cache");//设置头
            response.setHeader("Cache-Control","no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            ExportExcelUtil.exportExcel(rootPath,_title,headers, dataset, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(out!=null){
                    out.close();
                }
//                System.out.println("claim导出完成");
                log.info("claim导出完成");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }


    /*按照查询条件导出寻物启事信息到Excel*/
    /*admin可以根据state查看已处理和未处理的信息*/
    @RequestMapping(value = { "admin/export/lookingfor" }, method = {RequestMethod.GET,RequestMethod.POST})
    public void OutToExcelLookingfor1(String school, String title, String goodsName, Date lostTime, String lostPlace,
                                     String telephone, String username,Integer state,HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(title == null) title = "";
        if(goodsName == null) goodsName = "";
        if(lostTime == null) lostTime = null;
        if(lostPlace == null) lostPlace = "";
        if(telephone == null) telephone = "";
        Integer sid = 0;
        if (school != null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
        UserInfo userObj = new UserInfo();
        if (username != null) userObj = userInfoService.findByUsername(username);
        else userObj.setUid(0);
        Integer uid = Math.toIntExact(userObj.getUid());
        List<LookingFor> lookingForList = lookingForService.queryLookingFor(sid, title, goodsName, lostTime, lostPlace, telephone, uid,state);
        if (lookingForList==null || lookingForList.size()==0) throw new NullPointerException("没有数据");
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "LookingFor信息记录";
        String[] headers = { "寻物id","标题","丢失物品","物品照片","丢失时间","丢失地点","报酬","联系电话","学生","发布时间","是否已解决"};
        List<String[]> dataset = new ArrayList<String[]>();
        for(int i=0;i<lookingForList.size();i++) {
            LookingFor lookingFor = lookingForList.get(i);
            String photo = lookingFor.getGoodsPhoto().equals("http://localhost/temp/%E4%B8%8B%E8%BD%BD.jpg")?"无照片":lookingFor.getGoodsPhoto();
            String state1 = lookingFor.getState().equals("1")?"已解决":"待解决";
            dataset.add(new String[]{lookingFor.getLookingForId() + "",lookingFor.getTitle(),lookingFor.getGoodsName()
                    ,StringUtil.dateTimeToString(lookingFor.getLostTime()),photo,
                    lookingFor.getLostPlace(),lookingFor.getReward(),lookingFor.getTelephone(),
                    lookingFor.getUserObj().getUsername(),StringUtil.dateTimeToString(lookingFor.getAddTime()),state1});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
        OutputStream out = null;//创建一个输出流对象
        try {
            out = response.getOutputStream();//
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-disposition","attachment; filename="+"LookingFor.xls");//filename是下载的xls的名，建议最好用英文
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//设置类型
            response.setHeader("Pragma","No-cache");//设置头
            response.setHeader("Cache-Control","no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            ExportExcelUtil.exportExcel(rootPath,_title,headers, dataset, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(out!=null){
                    out.close();
                }
//                System.out.println("lookingfor导出完成");
                log.info("lookingfor导出完成");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /*按照查询条件导出失物招领信息到Excel*/
    /*admin可以根据state查看已处理和未处理的信息*/
    @RequestMapping(value = { "admin/export/lostfound" }, method = {RequestMethod.GET,RequestMethod.POST})
    public void OutToExcelLostfound1(String school, String title, String goodsName, Date pickUpTime, String pickUpPlace,
                                     String connectPersonName, String telephone, Integer state,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(title == null) title = "";
        if(goodsName == null) goodsName = "";
        if(pickUpTime == null) pickUpTime = null;
        if(pickUpPlace == null) pickUpPlace = "";
        Integer sid = 0;
        if (school != null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
        UserInfo userObj = new UserInfo();
        if (connectPersonName != null) userObj = userInfoService.findByUsername(connectPersonName);
        else userObj.setUid(0);
        Integer uid = Math.toIntExact(userObj.getUid());
        if(telephone == null) telephone = "";
        List<LostFound> lostFoundList = lostFoundService.queryLostFound(sid, title, goodsName, pickUpTime, pickUpPlace,  uid,telephone,state);
        if (lostFoundList==null || lostFoundList.size()==0) throw new NullPointerException("没有数据");
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "LostFound信息记录";
        String[] headers = { "招领id","标题","图片","物品名称","捡得时间","拾得地点","联系人","联系电话","发布时间","是否已解决"};
        List<String[]> dataset = new ArrayList<String[]>();
        for(int i=0;i<lostFoundList.size();i++) {
            LostFound lostFound = lostFoundList.get(i);
            String photo = lostFound.getGoodsPhoto().equals("http://localhost/temp/%E4%B8%8B%E8%BD%BD.jpg")?"无照片":lostFound.getGoodsPhoto();
            String state1 = lostFound.getState().equals("1")?"已解决":"待解决";
            dataset.add(new String[]{lostFound.getLostFoundId() + "",lostFound.getTitle(),photo,lostFound.getGoodsName(),
                    StringUtil.dateTimeToString(lostFound.getPickUpTime()),lostFound.getPickUpPlace(),
                    lostFound.getConnectPerson().getUsername(),lostFound.getPhone(),StringUtil.dateTimeToString(lostFound.getAddTime()),state1});
        }
        OutputStream out = null;//创建一个输出流对象
        try {
            out = response.getOutputStream();//
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-disposition","attachment; filename="+"LostFound.xls");//filename是下载的xls的名，建议最好用英文
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//设置类型
            response.setHeader("Pragma","No-cache");//设置头
            response.setHeader("Cache-Control","no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            ExportExcelUtil.exportExcel(rootPath,_title,headers, dataset, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(out!=null){
                    out.close();
                }
                log.info("lostfound导出完成");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
