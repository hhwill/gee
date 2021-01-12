package com.geekcattle.controller.message;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.geekcattle.model.message.Account;
import com.geekcattle.model.message.Total;
import com.geekcattle.service.msg.MessageService;
import com.geekcattle.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 数据接收
     * @param request
     * @return
     */
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
        System.out.println(params);
        messageService.processMsg(params);
        return "success";
    }

    @RequestMapping(value = "/msg/total.json")
    @ResponseBody
    public ModelMap getTotal(){
        Total total = messageService.getTotal();
        return ReturnUtil.success(null,total);
    }

    @RequestMapping(value = "/msg/account.json")
    @ResponseBody
    public ModelMap getAccount(String accountId) {
        Account account = messageService.getAccDetail(accountId);
        return ReturnUtil.success(null,account);
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

//    @RequestMapping(value = "/msg/total",method = {RequestMethod.GET})
//    public String getTotal(Model model) {
//        Total total = messageService.getTotal();
//        model.addAttribute("total", total);
//        return "message/total";
//    }

//    @RequestMapping(value = "/msg/account/{accno}",method = {RequestMethod.GET})
//    public String getAccount(@PathVariable String accno, Model model) {
//        Account account = messageService.getAccDetail(accno);
//        model.addAttribute("account", account);
//        return "message/account";
//    }


    @RequestMapping(value = "/msg/doMain",method = {RequestMethod.GET})
    public String doMain() {
//        messageService.doMain();
        return "success";
    }
}
