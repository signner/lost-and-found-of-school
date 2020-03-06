package com.shiro.mapper;

import com.shiro.pojo.Images;
import com.shiro.pojo.School;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SchoolMapper {
    /*通过主键sid查询一个school*/
    School getSchool(Integer sid);
    /*通过学校名称查询一个school*/
    School getSchoolByName(String name);
    /*通过学校名称查询学校的图片*/
    List<String> getSchoolImages(String name);
    /*为学校添加首页图片*/
    void addImages(Images images);
}
