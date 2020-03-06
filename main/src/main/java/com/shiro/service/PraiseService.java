package com.shiro.service;


import com.shiro.mapper.PraiseMapper;
import com.shiro.pojo.LookingFor;
import com.shiro.pojo.Praise;
import com.shiro.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

@Service
@Transactional
public class PraiseService {

	@Autowired
    PraiseMapper praiseMapper;
    /*每页显示记录数目*/
    @Value("${rows}")
    private int rows;
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

    /*添加表扬记录*/
    public void addPraise(Praise praise) throws Exception {
        praiseMapper.addPraise(praise);
    }

    /*按照查询条件分页查询表扬记录*/
    public ArrayList<Praise> queryPraise(LookingFor lookingFor,String praiseUserObj, String title,int sid, Date addTime, int currentPage) throws Exception {
     	String where = "where 1=1";
     	if (!praiseUserObj.equals("")) where = where + " AND t_praise.praiseUserObj like '%"+praiseUserObj+"%'";
    	if(null != lookingFor && lookingFor.getLookingForId()!= null && lookingFor.getLookingForId()!= 0)  where += " and t_praise.lookingFor=" + lookingFor.getLookingForId();
    	if(!title.equals("")) where = where + " and t_praise.title like '%" + title + "%'";
    	if(addTime!=null) where = where + " and t_praise.addTime like '%" + StringUtil.dateTimeToString(addTime) + "%'";
        if(sid!=0) where = where + " and userinfo.sid = " + sid;
    	int startIndex = (currentPage-1) * rows;
    	return praiseMapper.queryPraise(where, startIndex, rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Praise> queryPraise(LookingFor lookingFor,String praiseUserObj,String title,int sid,Date addTime) throws Exception  {
     	String where = "where 1=1";
        if (!praiseUserObj.equals("")) where = where + " AND t_praise.praiseUserObj like '%"+praiseUserObj+"%'";
    	if(null != lookingFor && lookingFor.getLookingForId()!= null && lookingFor.getLookingForId()!= 0)  where += " and t_praise.lookingFor=" + lookingFor.getLookingForId();
    	if(!title.equals("")) where = where + " and t_praise.title like '%" + title + "%'";
    	if(addTime!=null) where = where + " and t_praise.addTime like '%" + StringUtil.dateTimeToString(addTime) + "%'";
    	if(sid!=0) where = where + " and userinfo.sid = " + sid;
    	return praiseMapper.queryPraiseList(where);
    }

    /*查询所有表扬记录*/
    public ArrayList<Praise> queryAllPraise()  throws Exception {
        return praiseMapper.queryPraiseList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(LookingFor lookingFor,String praiseUserObj,String title,int sid,Date addTime) throws Exception {
     	String where = "where 1=1";
        if (!praiseUserObj.equals("")) where = where + " AND t_praise.praiseUserObj like '%"+praiseUserObj+"%'";
    	if(null != lookingFor && lookingFor.getLookingForId()!= null && lookingFor.getLookingForId()!= 0)  where += " and t_praise.lookingFor=" + lookingFor.getLookingForId();
    	if(!title.equals("")) where = where + " and t_praise.title like '%" + title + "%'";
    	if(addTime!=null) where = where + " and t_praise.addTime like '%" + StringUtil.dateTimeToString(addTime) + "%'";
        if(sid!=0) where = where + " and userinfo.sid = " + sid;
        recordNumber = praiseMapper.queryPraiseCount(where);
        int mod = recordNumber % rows;
        totalPage = recordNumber / rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取表扬记录*/
    public Praise getPraise(int praiseId) throws Exception  {
        Praise praise = praiseMapper.getPraise(praiseId);
        return praise;
    }

    /*更新表扬记录*/
    public void updatePraise(Praise praise) throws Exception {
        praiseMapper.updatePraise(praise);
    }


    /*删除表扬信息*/
    public void deletePraises (String praiseIds) throws Exception {
        praiseMapper.deletePraise(praiseIds);
    }
}
