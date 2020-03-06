package com.shiro.service;

import com.shiro.mapper.ClaimMapper;
import com.shiro.pojo.Claim;
import com.shiro.pojo.LostFound;
import com.shiro.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

@Service
@Transactional
public class ClaimService {

	@Autowired
    ClaimMapper claimMapper;
    /*每页显示记录数目*/
    @Value("${rows}")
    private int rows;;
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

    /*添加认领记录*/
    public void addClaim(Claim claim) throws Exception {
        claimMapper.addClaim(claim);
    }

    /*按照查询条件分页查询认领记录*/
    public ArrayList<Claim> queryClaim(LostFound lostFoundObj, String personName, Date claimTime, int sid, int currentPage) throws Exception {
     	String where = "where 1=1";
    	if(null != lostFoundObj && lostFoundObj.getLostFoundId()!= null && lostFoundObj.getLostFoundId()!= 0)  where += " and t_claim.lostFoundObj=" + lostFoundObj.getLostFoundId();
    	if(!personName.equals("")) where = where + " and t_claim.personName like '%" + personName + "%'";
    	if(claimTime!=null) where = where + " and t_claim.claimTime like '%" + StringUtil.dateTimeToString(claimTime)+"%'";
    	if(sid != 0) where = where + " and t_claim.personName = userinfo.username AND userinfo.sid = " +sid;
    	int startIndex = (currentPage-1) * rows;
    	return claimMapper.queryClaim(where, startIndex, rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Claim> queryClaim(LostFound lostFoundObj, String personName, Date claimTime,int sid) throws Exception  {
     	String where = "where 1=1";
    	if(null != lostFoundObj && lostFoundObj.getLostFoundId()!= null && lostFoundObj.getLostFoundId()!= 0)
    	    where += " and t_claim.lostFoundObj=" + lostFoundObj.getLostFoundId();
    	if(!personName.equals("")) where = where + " and t_claim.personName like '%" + personName + "%'";
    	if(claimTime!=null) where = where + " and t_claim.claimTime like '%" + StringUtil.dateTimeToString(claimTime)+"%'";
        if(sid != 0) where = where + " and t_claim.personName = userinfo.username AND userinfo.sid = " +sid;
    	return claimMapper.queryClaimList(where);
    }

    /*查询所有认领记录*/
    public ArrayList<Claim> queryAllClaim()  throws Exception {
        return claimMapper.queryClaimList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(LostFound lostFoundObj,String personName,Date claimTime,int sid) throws Exception {
     	String where = "where 1=1";
    	if(null != lostFoundObj && lostFoundObj.getLostFoundId()!= null && lostFoundObj.getLostFoundId()!= 0)  where += " and t_claim.lostFoundObj=" + lostFoundObj.getLostFoundId();
    	if(!personName.equals("")) where = where + " and t_claim.personName like '%" + personName + "%'";
    	if(claimTime!=null) where = where + " and t_claim.claimTime like '%" + StringUtil.dateTimeToString(claimTime)+"%'";
        if(sid != 0) where = where + " and t_claim.personName = userinfo.username AND userinfo.sid = " +sid;
        recordNumber = claimMapper.queryClaimCount(where);
        int mod = recordNumber % rows;
        totalPage = recordNumber / rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取认领记录*/
    public Claim getClaim(int claimId) throws Exception  {
        Claim claim = claimMapper.getClaim(claimId);
        return claim;
    }

    /*更新认领记录*/
    public void updateClaim(Claim claim) throws Exception {
        claimMapper.updateClaim(claim);
    }

    /*删除一条认领记录*/
    public void deleteClaim (String claimIds) throws Exception {
        claimMapper.deleteClaim(claimIds);
    }
}
