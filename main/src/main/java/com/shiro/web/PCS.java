package com.shiro.web;

import com.shiro.pojo.Provinces;
import com.shiro.pojo.re.QueryOfElementUi;
import com.shiro.pojo.re.ReturnMessage;
import com.shiro.service.PCSService;
import com.shiro.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * DATE: 2019/9/9 14:08
 * USER: create by 申水根
 */
@RestController
@RequestMapping("none/")
public class PCS {
    @Autowired
    PCSService pcsService;
    @Autowired
    SchoolService schoolService;
    @RequestMapping(method = {RequestMethod.GET,RequestMethod.POST},value = "pro")
    public List<Provinces> ProvincialAndMunicipalLevel() {
        return pcsService.getProvincialAndMunicipalLevel();
    }

    @RequestMapping(method = {RequestMethod.GET,RequestMethod.POST},value = "searchSchool")
    public List<QueryOfElementUi> searchSchoolsByLike(@RequestParam("school") String school) {
        if (school==null) school="";
        return pcsService.searchSchoolsByLike(school);
    }

    @RequestMapping(method = {RequestMethod.POST},value = "school/images")
    public ReturnMessage getSchoolImages(@RequestParam("school") String school){
        ReturnMessage r = new ReturnMessage();
        r.setData(schoolService.getSchoolImage(school));
        return r;
    }


}

