package com.shiro.service;

import com.shiro.mapper.LookingForMapper;
import com.shiro.mapper.PraiseMapper;
import com.shiro.pojo.LookingFor;
import com.shiro.pojo.Praise;
import com.shiro.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LookingForService {

	@Autowired
    LookingForMapper lookingForMapper;
	@Autowired
    PraiseMapper praiseMapper;

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
    private int totalPage=0;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber=0;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加寻物启事记录*/
    public void addLookingFor(LookingFor lookingFor) throws Exception {
        lookingForMapper.addLookingFor(lookingFor);
    }

    /*按照查询条件分页查询寻物启事记录*/
    public ArrayList<LookingFor> queryLookingFor(Integer sid, String title, String goodsName, Date lostTime,
                                                 String lostPlace, String telephone,Integer uid,Integer state,
                                                 int currentPage) throws Exception {
     	String where = "where 1=1";
        if(sid!=0) where = where + " and userinfo.sid = '"+sid+"'";
    	if(!title.equals("")) where = where + " and t_lookingFor.title like '%" + title + "%'";
    	if(!goodsName.equals("")) where = where + " and t_lookingFor.goodsName like '%" + goodsName + "%'";
        if(!(lostTime==null)) where = where + " and t_lookingFor.lostTime like '%" +   StringUtil.dateTimeToString(lostTime) + "%'";
    	if(!lostPlace.equals("")) where = where + " and t_lookingFor.lostPlace like '%" + lostPlace + "%'";
    	if(!telephone.equals("")) where = where + " and t_lookingFor.telephone like '%" + telephone + "%'";
    	if(uid!=0)  where += " and t_lookingFor.userObj='" + uid + "'";
    	where += " and t_lookingFor.state='" + state + "'";
    	int startIndex = (currentPage-1) * rows;
    	return lookingForMapper.queryLookingFor(where, startIndex, rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<LookingFor> queryLookingFor(Integer sid,String title,String goodsName,Date lostTime,
                                                 String lostPlace,String telephone,Integer uid,Integer state) throws Exception  {
     	String where = "where 1=1";
        if(sid!=0) where = where + " and userinfo.sid = '"+sid+"'";
    	if(!title.equals("")) where = where + " and t_lookingFor.title like '%" + title + "%'";
    	if(!goodsName.equals("")) where = where + " and t_lookingFor.goodsName like '%" + goodsName + "%'";
    	if(!(lostTime==null)) where = where + " and t_lookingFor.lostTime like '%" +   StringUtil.dateTimeToString(lostTime) + "%'";
    	if(!lostPlace.equals("")) where = where + " and t_lookingFor.lostPlace like '%" + lostPlace + "%'";
    	if(!telephone.equals("")) where = where + " and t_lookingFor.telephone like '%" + telephone + "%'";
        if(uid!=0)  where += " and t_lookingFor.userObj='" + uid + "'";
        where += " and t_lookingFor.state='" + state + "'";
    	return lookingForMapper.queryLookingForList(where);
    }

    /*查询所有寻物启事记录*/
    public ArrayList<LookingFor> queryAllLookingFor()  throws Exception {
        return lookingForMapper.queryLookingForList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Integer sid,String title,String goodsName,Date lostTime,
                                              String lostPlace,String telephone,Integer uid,Integer state) throws Exception {
     	String where = "where 1=1";
        if(sid!=0) where = where + " and userinfo.sid = '"+sid+"'";
    	if(!title.equals("")) where = where + " and t_lookingFor.title like '%" + title + "%'";
    	if(!goodsName.equals("")) where = where + " and t_lookingFor.goodsName like '%" + goodsName + "%'";
        if(!(lostTime==null)) where = where + " and t_lookingFor.lostTime like '%" +   StringUtil.dateTimeToString(lostTime) + "%'";
    	if(!lostPlace.equals("")) where = where + " and t_lookingFor.lostPlace like '%" + lostPlace + "%'";
    	if(!telephone.equals("")) where = where + " and t_lookingFor.telephone like '%" + telephone + "%'";
        if(uid!=0)  where += " and t_lookingFor.userObj='" + uid + "'";
        where += " and t_lookingFor.state='" + state + "'";
        recordNumber = lookingForMapper.queryLookingForCount(where);
        int mod = recordNumber % rows;
        totalPage = recordNumber / rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取寻物启事记录*/
    public LookingFor getLookingFor(int lookingForId) throws Exception  {
        LookingFor lookingFor = lookingForMapper.getLookingFor(lookingForId);
        return lookingFor;
    }

    /*更新寻物启事记录*/
    public void updateLookingFor(LookingFor lookingFor) throws Exception {
        lookingForMapper.updateLookingFor(lookingFor);
    }

    /*删除寻物启事记录*/
    @Transactional(rollbackFor = Exception.class)
    public void deleteLookingFor (String lookingForIds) throws Exception {
        String[] lookingForId = lookingForIds.split(",");
        List<Praise> list =  praiseMapper.queryPraiseList(" where 1=1").stream().filter(praise -> {
           return Arrays.asList(lookingForId).contains(String.valueOf(praise.getLookingFor().getLookingForId()));
        }).collect(Collectors.toList());

        if (list==null||list.size()==0)
            System.out.println("此消息还未有过找到记录");
        else
            list.forEach(p->{try{
            praiseMapper.deletePraise(String.valueOf(p.getPraiseId()));}catch (Exception e){}});

        lookingForMapper.deleteLookingFor(lookingForIds);
    }

    /*查询超过俩个月的数据*/
    public List<Integer> overTwoMonth() throws  Exception{
        return lookingForMapper.overTwoMonth();
//        return null;
    }
}
