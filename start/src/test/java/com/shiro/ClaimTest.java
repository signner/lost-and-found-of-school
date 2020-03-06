package com.shiro;

import com.shiro.pojo.Claim;
import com.shiro.pojo.LostFound;
import com.shiro.service.ClaimService;
import com.shiro.service.LostFoundService;
import com.shiro.service.SchoolService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ClaimTest {
    @Autowired
    ClaimService claimService;
    @Autowired
    LostFoundService lostFoundService;
    @Autowired
    SchoolService schoolService;

    @Test
    public void addClaimtest() throws  Exception{
//        for (int i=1;i<=10;i++) {
//            LostFound lostFound = new LostFound();
//            lostFound.setLostFoundId(i);
//            UserInfo userInfo = new UserInfo();
//            userInfo.setUsername("java" + i);
//            Claim claim = new Claim(lostFound, userInfo, StringUtil.getNow());
//            claimService.addClaim(claim);
//        }
    }

    @Test
    public void queryAllClaimtest() throws  Exception{
        claimService.queryAllClaim().forEach(System.out::println);
    }

    @Test
    public void queryClaimtest() throws  Exception{
//        String personName = "";
//        Date date = new Date(2019,11,19);
//        Integer lostFoundId = 0;
//        LostFound lostFound = null;
//        if (lostFoundId!=0)
//            lostFound = lostFoundService.getLostFound(lostFoundId);
//        claimService.queryClaim(lostFound,personName,date);

        String personName = "";
        Date date = null;
        Integer lostFoundId = 0;
        LostFound lostFound = null;
        String school = "北京大学";
        int sid = 0;
        if (school==null) sid = Math.toIntExact(schoolService.getSchoolByName(school).getId());
        if (lostFoundId!=0)
            lostFound = lostFoundService.getLostFound(lostFoundId);
        List<Claim> list = claimService.queryClaim(lostFound,personName,date,sid);
        System.out.println(list.size());
//        System.out.println(list.get(1).getPersonName().getSchool());
//        list.forEach(System.out::println);
//        list.forEach(l->System.out.println(l));
        List<Claim> _claimArrayList = list.stream().filter(claim -> { return  claim.getPersonName().getSchool().getName().equals(school);}).collect(Collectors.toList());
        _claimArrayList.forEach(l->System.out.println(l.getPersonName().getSchool().getName()));

    }

    @Test
    public void test4() throws  Exception{
        int i = 0;
        i = 5/11;
        if (5%11!=0)
            i++;
        System.out.println(i);
    }
}
