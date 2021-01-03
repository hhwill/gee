package com.geekcattle.job;

import com.geekcattle.util.HttpUtil;
import com.geekcattle.util.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;


@Component
public class GetDataJob {
    private final Log logger = LogFactory.getLog(GetDataJob.class);

    public static Map<String, Object> Data;
    public static Map<String, Object> VipData;

    private static int interval = 5;
    /**
     * 每隔一个小时检查
     */
    @Scheduled(fixedDelay = 5 * 1000)
    public void checkCouponExpired() {
        //logger.info("系统开启任务检查优惠券是否已经过期");

        /*
        String value = HttpUtil.doGet("http://45.78.29.9:24570/bitmex_btcusd");

        Object o = JsonUtil.parse(value);
        VipData = ((Map<String,Object>)o);
        interval++;
        if (interval > 5) {
            interval = 0;
            Data = VipData;
        }
        */
    }

}