package com.shiro.service;

import com.shiro.mapper.PCSMapper;
import com.shiro.pojo.Provinces;
import com.shiro.pojo.re.QueryOfElementUi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DATE: 2019/9/16 13:45
 * USER: create by 申水根
 */
@Service
@Transactional
public class PCSService{
    @Autowired
    PCSMapper pcsMapper;

    public List<Provinces> getProvincialAndMunicipalLevel() {
        return pcsMapper.getPCS();
    }

    public List<QueryOfElementUi> searchSchoolsByLike(String school) {
        return pcsMapper.searchSchoolsByLike("%" + school + "%");
    }
}
