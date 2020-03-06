package com.shiro.mapper;

import com.shiro.pojo.Cities;
import com.shiro.pojo.Provinces;
import com.shiro.pojo.School;
import com.shiro.pojo.re.QueryOfElementUi;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 省市学校三级信息操作类
 */
@Repository
public interface PCSMapper {

//-------------------------------------------------------------------------------------------------------------------------

    /**
     * 三级联动消息
     *
     * @return
     */
    @Select("select * from provinces")
    @Results({
            @Result(id = true, property = "id", column = "pid", javaType = Long.class),
            @Result(property = "name", column = "name", javaType = String.class),
            @Result(property = "children", column = "pid", many = @Many(select = "com.shiro.mapper.PCSMapper.getCities"))
    })
    List<Provinces> getPCS();


    @Select("select * from cities where pid = #{pid}")
    @Results({
            @Result(property = "name", column = "name", javaType = String.class),
            @Result(id = true, property = "id", column = "cid", javaType = Long.class),
            @Result(property = "children", column = "cid", many = @Many(select = "com.shiro.mapper.PCSMapper.getSchool"))
    })
    List<Cities> getCities(int pid);

    @Select("select * from school where cid = #{cid}")
    @Results({
            @Result(id = true, property = "id", column = "sid", javaType = Long.class),
    })
    School getSchool(int cid);

    //----------------------------------------------------------------------------------------------------------------------

    /**
     * 模糊查询
     *
     * @param school
     * @return
     */
    @Select("select `name` value from school where `name` like #{school} limit 0,10")
    List<QueryOfElementUi> searchSchoolsByLike(String school);

}
