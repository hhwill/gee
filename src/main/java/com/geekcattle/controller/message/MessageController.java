package com.geekcattle.controller.message;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.geekcattle.model.message.Account;
import com.geekcattle.service.msg.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ResponseBody
    @RequestMapping(value = "/manageMsg",method = {RequestMethod.POST})
    public String testpost(HttpServletRequest request){
        Map<String, String> params = new HashMap<>();
        // 将request中的参数合并到param中去 ......
        Enumeration<String> paramNms = request.getParameterNames();
        while (paramNms.hasMoreElements()) {
            String paramId = paramNms.nextElement();
            String paramVal = request.getParameter(paramId);
            params.put(paramId, paramVal);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/uploadMsg",method = {RequestMethod.POST})
    public String uploadMsg(HttpServletRequest request){
        Map<String, String> params = new HashMap<>();
        // 将request中的参数合并到param中去 ......
        Enumeration<String> paramNms = request.getParameterNames();
        while (paramNms.hasMoreElements()) {
            String paramId = paramNms.nextElement();
            String paramVal = request.getParameter(paramId);
            params.put(paramId, paramVal);
        }
        messageService.processMsg(params);
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/api/getInfo",method = {RequestMethod.GET})
    public String getTrade() {
        return messageService.getInfo();
    }

    @ResponseBody
    @RequestMapping(value = "/api/getDetail/{accno}",method = {RequestMethod.GET})
    public String getDetail(@PathVariable String accno, Model model) {
        return messageService.getDetail(accno);
    }

    @RequestMapping(value = "/msg/balance",method = {RequestMethod.GET})
    public String getBalance(Model model) {
        return "message/balance";
    }

    @RequestMapping(value = "/msg/account/{accno}",method = {RequestMethod.GET})
    public String getAccount(@PathVariable String accno, Model model) {
        Account account = messageService.getAccDetail(accno);
        model.addAttribute("account", account);
        return "message/account";
    }
}
