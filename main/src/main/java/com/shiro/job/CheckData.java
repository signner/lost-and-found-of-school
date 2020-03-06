package com.shiro.job;

import com.shiro.pojo.LookingFor;
import com.shiro.pojo.LostFound;
import com.shiro.service.LookingForService;
import com.shiro.service.LostFoundService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class CheckData {
    @Autowired
     LookingForService lookingForService;
    @Autowired
     LostFoundService lostFoundService;


//    @Scheduled(cron = "0/5 * * * * ?")    //每5秒打印当前时间
    public static void printCurrentTime() {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        CheckData.log.info("The quartz's test,Current Time is:"+date);
    }

    //每天早上6点对数据库进行检查，删除超过2个月的数据
    @Scheduled(cron="0 0 6 * * ?")
//    @Scheduled(cron = "0/30 * * * * ?")    //每30秒
    public void check() throws Exception {
        CheckData.log.info("--------------------开始查询数据库超过俩个月的数据");

        List<Integer> lookingForIds = lookingForService.overTwoMonth();
        List<Integer> lostFoundIds = lostFoundService.overTwoMonth();


        if (!CheckData.isNull(lookingForIds)) {
            List<LookingFor> overLookingFor = new ArrayList<>();
            lookingForIds.forEach(id -> {
                try {
                    overLookingFor.add(lookingForService.getLookingFor(id));
                } catch (Exception e) {
                }
            });
            CheckData.log.info("lookingFor:" + overLookingFor);
            CheckData.log.info("-------------------开始删除以上t_lookingFor表超过2个月的数据");
            lookingForService.deleteLookingFor(StringUtils.join(lookingForIds, ","));
            CheckData.log.info("------------------------------------------------删除成功！");
        } else {
            CheckData.log.info("--------------------------t_lookingFor表没有超过2个月的数据");
        }

        if (!CheckData.isNull(lostFoundIds)) {
            List<LostFound> overLostFound = new ArrayList<>();
            lostFoundIds.forEach(id -> {
                try {
                    overLostFound.add(lostFoundService.getLostFound(id));
                } catch (Exception e) {
                }
            });
            CheckData.log.info("LostFound:" + overLostFound);
            CheckData.log.info("----------------------------------------开始删除以上数据");
            lostFoundService.deleteLostFounds(StringUtils.join(lostFoundIds, ","));
            CheckData.log.info("-----------------------------------------------删除成功");
        } else {
            CheckData.log.info("--------------------------t_lostFound表没有超过2个月的数据");
        }
    }


    private static boolean isNull(Collection<?> collection) {
        if (collection == null || collection.size() == 0)
            return true;
        else
            return false;
    }

//    public static void main(String[] args) {
//        System.out.println(isNull(new ArrayList<>()));
//        System.out.println(isNull(Arrays.asList(new String[]{"1", "2"})));
//    }
}
