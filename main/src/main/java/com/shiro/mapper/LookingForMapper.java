package com.shiro.mapper;

import com.shiro.pojo.LookingFor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface LookingForMapper {
	/*添加寻物启事信息*/
	public void addLookingFor(LookingFor lookingFor) throws Exception;

	/*按照查询条件分页查询寻物启事记录*/
	public ArrayList<LookingFor> queryLookingFor(@Param("where") String where, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有寻物启事记录*/
	public ArrayList<LookingFor> queryLookingForList(@Param("where") String where) throws Exception;

	/*按照查询条件的寻物启事记录数*/
	public int queryLookingForCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条寻物启事记录*/
	public LookingFor getLookingFor(int lookingForId) throws Exception;

	/*更新寻物启事记录*/
	public void updateLookingFor(LookingFor lookingFor) throws Exception;

	/*删除寻物启事记录*/
	public void deleteLookingFor(@Param("lookingForIds") String lookingForIds) throws Exception;

	/*查询超过2个月的数据*/
	public List<Integer> overTwoMonth()throws  Exception;

	public int test();
}
