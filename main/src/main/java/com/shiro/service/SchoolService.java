package com.shiro.service;

import com.shiro.mapper.SchoolMapper;
import com.shiro.pojo.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SchoolService {
    @Autowired
    SchoolMapper schoolMapper;

    /*通过主键sid查询一个school*/
    public School getSchool(Integer sid){
        return schoolMapper.getSchool(sid);
    }

    public School getSchoolByName(String name){
        return schoolMapper.getSchoolByName(name);
    }

    public List<String> getSchoolImage(String name){ return schoolMapper.getSchoolImages(name); }
}
