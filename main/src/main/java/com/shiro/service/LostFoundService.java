package com.shiro.service;

import com.shiro.mapper.ClaimMapper;
import com.shiro.mapper.LostFoundMapper;
import com.shiro.pojo.Claim;
import com.shiro.pojo.LostFound;
import com.shiro.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Service
@Transactional
public class LostFoundService{

    @Autowired
    LostFoundMapper lostFoundMapper;
	@Autowired
    ClaimMapper claimMapper;
    /*每页显示记录数目*/
    @Value("${rows}")
    private int rows ;;
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

    /*添加失物招领记录*/
    public void addLostFound(LostFound lostFound) throws Exception {
        lostFoundMapper.addLostFound(lostFound);
    }

    /*按照查询条件分页查询失物招领记录*/
    @SuppressWarnings("Duplicates")
    public ArrayList<LostFound> queryLostFound(Integer sid,String title, String goodsName,
                                               Date pickUpTime, String pickUpPlace, Integer connectPerson,
                                               String phone,Integer state, int currentPage) throws Exception {
     	String where = "where 1=1";
        if(sid!=0) where = where + " and userinfo.sid = '"+sid+"'";
    	if(!title.equals("")) where = where + " and t_lostfound.title like '%" + title + "%'";
    	if(!goodsName.equals("")) where = where + " and t_lostfound.goodsName like '%" + goodsName + "%'";
        if(!(pickUpTime==null)) where = where + " and t_lostfound.pickUpTime like '%" +  StringUtil.dateTimeToString(pickUpTime) + "%'";
    	if(!pickUpPlace.equals("")) where = where + " and t_lostfound.pickUpPlace like '%" + pickUpPlace + "%'";
    	if(connectPerson!=0) where = where + " and t_lostfound.connectPerson  = '"+connectPerson+"'";
    	if(!phone.equals("")) where = where + " and t_lostfound.phone like '%" + phone + "%'";
        where = where + " and t_lostfound.state = '" + state + "'";
    	int startIndex = (currentPage-1) * rows;
    	return lostFoundMapper.queryLostFound(where, startIndex, rows);
    }

    /*按照查询条件查询所有记录*/
    @SuppressWarnings("Duplicates")
    public ArrayList<LostFound> queryLostFound(Integer sid, String title, String goodsName, Date pickUpTime,
                                               String pickUpPlace, Integer connectPerson,
                                               String phone,Integer state) throws Exception  {
     	String where = "where 1=1";
        if(sid!=0) where = where + " and userinfo.sid = '"+sid+"'";
    	if(!title.equals("")) where = where + " and t_lostfound.title like '%" + title + "%'";
    	if(!goodsName.equals("")) where = where + " and t_lostfound.goodsName like '%" + goodsName + "%'";
        if(!(pickUpTime==null))where = where + " and t_lostfound.pickUpTime like '%" +  StringUtil.dateTimeToString(pickUpTime) + "%'";
    	if(!pickUpPlace.equals("")) where = where + " and t_lostfound.pickUpPlace like '%" + pickUpPlace + "%'";
    	if(connectPerson!=0) where = where + " and t_lostfound.connectPerson  = '"+connectPerson+"'";
        if(!phone.equals("")) where = where + " and t_lostfound.phone like '%" + phone + "%'";
        where = where + " and t_lostfound.state = '" + state + "'";
    	return lostFoundMapper.queryLostFoundList(where);
    }

    /*查询所有失物招领记录*/
    public ArrayList<LostFound> queryAllLostFound()  throws Exception {
        return lostFoundMapper.queryLostFoundList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    @SuppressWarnings("Duplicates")
    public void queryTotalPageAndRecordNumber(Integer sid,String title, String goodsName,
                                              Date pickUpTime, String pickUpPlace,
                                              Integer connectPerson, String phone,Integer state) throws Exception {
     	String where = "where 1=1";
        if(sid!=0) where = where + " and userinfo.sid = '"+sid+"'";
    	if(!title.equals("")) where = where + " and t_lostfound.title like '%" + title + "%'";
    	if(!goodsName.equals("")) where = where + " and t_lostfound.goodsName like '%" + goodsName + "%'";
        if(!(pickUpTime==null)) where = where + " and t_lostfound.pickUpTime like '%" +  StringUtil.dateTimeToString(pickUpTime) + "%'";
    	if(!pickUpPlace.equals("")) where = where + " and t_lostfound.pickUpPlace like '%" + pickUpPlace + "%'";
    	if(connectPerson!=0) where = where + " and t_lostfound.connectPerson  = '"+connectPerson+"'";
    	if(!phone.equals("")) where = where + " and t_lostfound.phone like '%" + phone + "%'";
    	if(!state.equals("")) where = where + " and t_lostfound.phone like '%" + phone + "%'";
        where = where + " and t_lostfound.state = '" + state + "'";
        recordNumber = lostFoundMapper.queryLostFoundCount(where);
        int mod = recordNumber % rows;
        totalPage = recordNumber / rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取失物招领记录*/
    public LostFound getLostFound(int lostFoundId) throws Exception  {
        LostFound lostFound = lostFoundMapper.getLostFound(lostFoundId);
        return lostFound;
    }

    /*更新失物招领记录*/
    public void updateLostFound(LostFound lostFound) throws Exception {
        lostFoundMapper.updateLostFound(lostFound);
    }

    /*删除多条失物招领信息*/
    @Transactional(rollbackFor=Exception.class)
    public void deleteLostFounds (String lostFoundIds) throws Exception {
        List<String> lostFoundId = Arrays.asList(lostFoundIds.split(","));
        List<String> claimIds = new ArrayList<>();
//      过滤要删除的id所对应的lostFoundIds
         List<Claim> list = claimMapper.queryClaimList(" where 1=1").stream().filter(claim -> {
//            System.err.println(claim.getLostFoundObj().getLostFoundId());
//            System.err.println(lostFoundId.contains(claim.getLostFoundObj().getLostFoundId()));
            return lostFoundId.contains(String.valueOf(claim.getLostFoundObj().getLostFoundId()));
        }).collect(Collectors.toList());

         if (list==null || list.size()==0)
             System.out.println("要删除的消息还没有认领消息");
         else {
             list.forEach(claim1 -> claimIds.add(String.valueOf(claim1.getClaimId())));
             claimMapper.deleteClaim(StringUtils.join(claimIds.toArray(),","));
             System.err.println(claimIds);
         }
        lostFoundMapper.deleteLostFound(lostFoundIds);
    }

    /*查询超过俩个月的数据*/
    public List<Integer> overTwoMonth() throws  Exception{
        return lostFoundMapper.overTwoMonth();
//        return null;
    }


//    public static void main(String[] args){
//        System.out.println(Arrays.asList("1".split(",")).contains(1));
//        System.out.println(Arrays.asList("1".split(",")).contains("1"));
//    }
}
