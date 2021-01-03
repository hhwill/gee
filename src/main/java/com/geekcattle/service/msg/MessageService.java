package com.geekcattle.service.msg;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geekcattle.model.message.Account;
import com.geekcattle.model.message.AccountDetail;
import com.geekcattle.util.MsgUtil;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public static Map<String, List<Map<String,String>>> accounts = new HashMap<String, List<Map<String,String>>>();

    public void processMsg(Map<String, String> value) {
        MsgUtil util = new MsgUtil();
        try {
            String acctno = value.get("ACCNO");
            if (accounts.containsKey(acctno)) {
                accounts.get(acctno).add(value);
            } else {
                List<Map<String,String>> lst = new ArrayList<Map<String,String>>();
                lst.add(value);
                accounts.put(acctno, lst);
            }
        } catch (Exception ex) {

        }
    }
//{ACCNO=1325, BANK=中国银行, AMOUNT=50000.00, TYPE=PAYOUT, OPP=, BALANCE=43671.49}
    public String getInfo() {
        String total = "";
        String totalBalance = "0";
        Map<String,String> bank = new HashMap<String,String>();
        Map<String,String> acc = new HashMap<String,String>();
        for (String key : accounts.keySet()) {
            List<Map<String,String>> value = accounts.get(key);
            Map<String,String> lastvalue = value.get(value.size()-1);
            totalBalance = new DecimalFormat("0.00").format(Float.valueOf(totalBalance) + Float.valueOf(lastvalue.get("BALANCE")));
            String bankname = lastvalue.get("BANK");
            if (bank.containsKey(bankname)) {
                String balance = bank.get(bankname);
                balance =
                        new DecimalFormat("0.00").format(Float.valueOf(balance) + Float.valueOf(lastvalue.get("BALANCE")));
                bank.put(bankname,balance);
            } else {
                bank.put(bankname, lastvalue.get("BALANCE"));
            }
            acc.put(key, lastvalue.get("BALANCE"));
        }
        total += "{\"total\":"+totalBalance+",\"bankbalance\":[";
        boolean exist = false;
        for (String key : bank.keySet()) {
            exist = true;
            total += "{\"bankname\":\"" + key + "\",\"balance\":" + bank.get(key) +"},";
        }
        if (exist) {
            total = total.substring(0, total.length()-1);
        }
        total += "],\"accountbalance\":[";
        exist = false;
        for (String key : accounts.keySet()) {
            exist = true;
            List<Map<String,String>> value = accounts.get(key);
            Map<String,String> lastvalue = value.get(value.size()-1);
            total += "{\"accountno\":\""+key+"\",\"bankname\":\"" + lastvalue.get("BANK") + "\",\"balance\":" + lastvalue.get("BALANCE") +"},";
        }
        if (exist) {
            total = total.substring(0, total.length()-1);
        }
        total += "]}";
        return total;
    }

    public Account getAccDetail(String account) {
        Account acc = new Account();
        acc.setAccountno(account);
        acc.setBankname("");
        acc.setBalance("0");
        List<AccountDetail> details = new ArrayList<AccountDetail>();
        acc.setDetails(details);
        if (accounts.containsKey(account)) {
            List<Map<String,String>> value = accounts.get(account);
            Map<String,String> lastvalue = value.get(value.size()-1);
            acc.setBalance(lastvalue.get("BALANCE"));
            for (Map<String,String> detail : value) {
               AccountDetail ad = new AccountDetail();
               ad.setAccountNo(account);
               ad.setAmount(detail.get("AMOUNT").toString());
               ad.setBankName(detail.get("BANK").toString());
               ad.setOpp(detail.get("OPP").toString());
               ad.setType(detail.get("TYPE").toString());
               details.add(ad);
            }
        }
        acc.setAccountno(account);

        return acc;
    }

    public String getDetail(String account) {
        String result = "{\"balance\":";
        if (accounts.containsKey(account)) {
            List<Map<String,String>> value = accounts.get(account);
            Map<String,String> lastvalue = value.get(value.size()-1);
            result += lastvalue.get("BALANCE");
            result += ", \"detail\":[ ";
            for (Map<String,String> detail : value) {
                result += "{";
                for (String key : detail.keySet()) {
                    result += "\""+key+"\":\""+detail.get(key)+"\",";
                }
                result = result.substring(0, result.length()-1) + "},";
            }
            result = result.substring(0, result.length()-1) + "]}";
        } else
        {
            result += "0, \"detail\":[]}";
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        MessageService ser = new MessageService();
        System.out.println(ser.getInfo());
        String s1 = "贵公司尾号1302的账户9月15日16时59分ARAP对账系统2003-2008服务费收入人民币11100.00元，余额27597.33元。对方户名:莎莎化妆品(中国）有限公司。[建设银行]";
        MsgUtil util = new MsgUtil();
        Map<String,String> map = util.prase(s1);
        List<Map<String,String>> lst = new ArrayList<Map<String,String>>();
        lst.add(map);
        accounts.put("1302", lst);

        System.out.println(ser.getInfo());
        System.out.println(ser.getDetail("1302"));
        System.out.println(ser.getDetail("1303"));
    }

}
