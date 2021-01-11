package com.geekcattle.controller;

import com.geekcattle.model.message.Total;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Lyh
 * @version 1.0
 * @date 2021-01-11 20:25
 */

@Controller
@RequestMapping
public class UrlController {


//    @RequestMapping(value = "/msg/total",method = {RequestMethod.GET})
//    public String getTotal(Model model) {
//        return "message/total";
//    }


    @RequestMapping("{module}/{url}.html")
    public String module(@PathVariable("module") String module, @PathVariable("url") String url){
        return  module + "/" + url ;
    }
}
