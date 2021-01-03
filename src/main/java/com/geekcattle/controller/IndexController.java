package com.geekcattle.controller;

import com.geekcattle.job.GetDataJob;
import com.geekcattle.util.HttpUtil;
import com.geekcattle.util.DateUtil;
import com.geekcattle.util.JsonUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.ModelMap;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;

/**
 * @author geekcattle
 */
@Controller
@RequestMapping
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String currentValue;

    @RequestMapping
    public String index(Model model) {
        return "home/index";
    }

    @RequestMapping(value = "/mojing",method = {RequestMethod.GET})
    public String mojing(Model model) {
        return "member/mojing";
    }

    @ResponseBody
    @RequestMapping(value = "/getmojing/{indexid}",method = {RequestMethod.GET})
    public String getmojing(@PathVariable String indexid,Model model) {
        Map<String,Object> data = GetDataJob.Data;
        String[] aa = indexid.split(",");
        String result = "";
        for (int i = 0 ; i <aa.length ; i++ ) {
            String value = (String)data.get(aa[i]);
            if (value == null)
                value = "0";
            result += value + ",";
        }
        result = result.substring(0,result.length()-1);
        return result;
    }

    @RequestMapping(value = "/test",method = {RequestMethod.POST})
    public String test(Model model){
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");




        String rq = DateUtil.getCurrentTime();
        System.out.println(rq);
        model.addAttribute("rq", rq);
        return "test/test";
    }

    @ResponseBody
    @RequestMapping(value = "/api/getsnapshot/{exchangeid}/{pairid}",method = {RequestMethod.GET})
    public String getSnapshot(@PathVariable String exchangeid,@PathVariable String pairid, Model model) {
        JedisCluster jc = new JedisCluster(new HostAndPort("127.0.0.1", 43791),0,0,10000,"echocoin",new GenericObjectPoolConfig());
        String value = "";
        value = jc.get("EXMASHUP:SN:"+exchangeid+":"+pairid);
        return value;
    }

    @ResponseBody
    @RequestMapping(value = "/api/gettrade/{exchangeid}/{pairid}",method = {RequestMethod.GET})
    public String getInfo(@PathVariable String exchangeid,@PathVariable String pairid, Model model) {
        JedisCluster jc = new JedisCluster(new HostAndPort("127.0.0.1", 43791),0,0,10000,"echocoin",new GenericObjectPoolConfig());
        String value = "";
        long len = jc.llen("EXMASHUP:TD:"+exchangeid+":"+pairid);
        if (len > 200)
            len = 200;
        value = "{\"result\":[";
        for (long j = len-1; j >=0; j--) {
            value += jc.lindex("EXMASHUP:TD:"+exchangeid+":"+pairid,j) + ",";
        }
        if (value.endsWith(",")) {
            value = value.substring(0,value.length() - 1);
        }
        value += "]}";
        return value;
    }



}
