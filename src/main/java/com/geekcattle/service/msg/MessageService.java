package com.geekcattle.service.msg;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.geekcattle.mapper.msg.MsgAccountBalanceMapper;
import com.geekcattle.mapper.msg.MsgShortInfoMapper;
import com.geekcattle.model.message.Account;
import com.geekcattle.model.message.AccountDetail;
import com.geekcattle.model.message.Bank;
import com.geekcattle.model.message.Total;
import com.geekcattle.model.msg.MsgAccountBalance;
import com.geekcattle.model.msg.MsgShortInfo;
import com.geekcattle.util.BizccDateUtil;
import com.geekcattle.util.DateStyle;
import com.geekcattle.util.DateUtil;
import com.geekcattle.util.MsgUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.xml.crypto.Data;

@Service
public class MessageService {

    public static String _time = "";

    public static Map<String, List<Map<String,String>>> accounts = new HashMap<String, List<Map<String,String>>>();

    @Autowired
    private MsgShortInfoMapper msgShortInfoMapper;

    @Autowired
    private MsgAccountBalanceMapper msgAccountBalanceMapper;

    public void processMsg(Map<String, String> src) {
        MsgUtil util = new MsgUtil();
        Map<String,String> value = util.prase(src.get("msg").toString());
        String lastime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(src.get("time"))));
        value.put(MsgUtil.TIME, lastime);
        value.put(MsgUtil.UUID, src.get("uuid").toString());
        value.put(MsgUtil.PHONE, src.get("phone").toString());


        //昨天
        Date thisTime = BizccDateUtil.StringToDate(lastime, DateStyle.YYYY_MM_DD_HH_MM_SS);
//        Date yesterdayTmp = BizccDateUtil.addDay(thisTime,-1);
        String yesterdayStr = BizccDateUtil.DateToString(thisTime,DateStyle.YYYY_MM_DD);
        Date yesterday = BizccDateUtil.StringToDate(yesterdayStr + "00:00:00",DateStyle.YYYY_MM_DD_HH_MM_SS);

        //入库信息表
        MsgShortInfo info = util.getMsgShortInfo(value);
        msgShortInfoMapper.insert(info);

        //如果数据正确，跟新账户表
        if(StringUtils.equals(util.T,info.getDataStatus())){
            Example example = new Example(MsgAccountBalance.class);
            example.createCriteria().andCondition("info_sender = ", info.getInfoSender())
                    .andCondition("info_receive_key = ", info.getInfoReceiveKey())
                    .andCondition("info_account_num = ", info.getInfoAccountNum())
                    .andCondition("data_status = ",info.getDataStatus());
            List<MsgAccountBalance> msgAccountBalances = msgAccountBalanceMapper.selectByExample(example);//仅一条数据
            if(CollectionUtils.isNotEmpty(msgAccountBalances)){
                MsgAccountBalance upd = msgAccountBalances.get(0);
                upd.setInfoBalance(info.getInfoBalance());
                msgAccountBalanceMapper.updateByPrimaryKey(upd);
            }else {
                MsgAccountBalance ins = new MsgAccountBalance();
                ins.setAccountId(RandomStringUtils.randomAlphanumeric( 16 ));
                ins.setInfoId(info.getInfoId());
                ins.setInfoSender(info.getInfoSender());
                ins.setInfoReceiveKey(info.getInfoReceiveKey());
                ins.setInfoAccountNum(info.getInfoAccountNum());
                ins.setInfoOpp(info.getInfoOpp());
                ins.setInfoBankName(info.getInfoBankName());
                ins.setInfoBalance(info.getInfoBalance());
                ins.setDataStatus(info.getDataStatus());
                ins.setInsertTime(new Date());

                //计算昨天余额
                Example msgShortInfoExample = new Example(MsgShortInfo.class);
                msgShortInfoExample.createCriteria().andCondition("info_sender = ", info.getInfoSender())
                        .andCondition("info_receive_key = ", info.getInfoReceiveKey())
                        .andCondition("info_account_num = ", info.getInfoAccountNum())
                        .andCondition("data_status = ",info.getDataStatus())
                        .andLessThan("insert_time",yesterday)
                ;
                msgShortInfoExample.orderBy("insert_time desc");
                List<MsgShortInfo> msgShortInfos = msgShortInfoMapper.selectByExample(msgShortInfoExample);
                if(CollectionUtils.isNotEmpty(msgShortInfos)){
                    ins.setLastBalance(msgShortInfos.get(0).getInfoBalance());
                }else {
                    ins.setLastBalance(BigDecimal.ZERO);
                }
                msgAccountBalanceMapper.insert(ins);
            }
        }




        //update by yinghui.li 2021年1月11日21点12分  以下为历史逻辑
//        MsgUtil util = new MsgUtil();
//        try {
//            Map<String,String> value = util.prase(src.get("msg").toString());
//            String lastime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(src.get("time"))));
//            value.put("TIME", lastime);
//            _time = lastime;
//            String acctno = value.get("ACCNO");
//            if (accounts.containsKey(acctno)) {
//                accounts.get(acctno).add(value);
//            } else {
//                List<Map<String,String>> lst = new ArrayList<Map<String,String>>();
//                lst.add(value);
//                accounts.put(acctno, lst);
//            }
//        } catch (Exception ex) {
//
//        }
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
            float curr = 0.0f;
            try {
                curr = Float.valueOf(lastvalue.get("BALANCE"));
            } catch (Exception ex) {}
            totalBalance = new DecimalFormat("0.00").format(Float.valueOf(totalBalance) + curr);
            String bankname = lastvalue.get("BANK");
            if (bank.containsKey(bankname)) {
                String balance = bank.get(bankname);
                float curr1 = 0.0f;
                try {
                    curr1 = Float.valueOf(lastvalue.get("BALANCE"));
                } catch (Exception ex) {}
                balance =
                        new DecimalFormat("0.00").format(Float.valueOf(balance) + curr1);
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

    public Total getTotal() {
        Total result = new Total();
        String total = "";
        String totalBalance = "0";
        String time = "";
        List<Bank> lbank = new ArrayList<>();
        List<Account> lacc = new ArrayList<>();
        Map<String,String> bank = new HashMap<String,String>();
        for (String key : accounts.keySet()) {
            List<Map<String,String>> value = accounts.get(key);
            Map<String,String> lastvalue = value.get(value.size()-1);
            float curr = 0.0f;
            try {
                curr = Float.valueOf(lastvalue.get("BALANCE"));
            } catch (Exception ex) {}
            totalBalance = new DecimalFormat("0.00").format(Float.valueOf(totalBalance) + curr);
            String bankname = lastvalue.get("BANK");
            if (bank.containsKey(bankname)) {
                String balance = bank.get(bankname);
                float curr1 = 0.0f;
                try {
                    curr1 = Float.valueOf(lastvalue.get("BALANCE"));
                } catch (Exception ex) {}
                balance =
                        new DecimalFormat("0.00").format(Float.valueOf(balance) + curr1);
                bank.put(bankname,balance);
            } else {
                bank.put(bankname, lastvalue.get("BALANCE"));
            }
            Account _acc = new Account();
            _acc.setAccountno(key);
            _acc.setBankname(bankname);
            _acc.setBalance(lastvalue.get("BALANCE"));
            lacc.add(_acc);
        }
        for (String sbank : bank.keySet()) {
            Bank _bank = new Bank();
            _bank.setBankname(sbank);
            _bank.setBalance(bank.get(sbank));
            lbank.add(_bank);
        }
        result.setBalance(totalBalance);
        result.setUbalance(new DecimalFormat("0.00").format(Float.valueOf(totalBalance)/6.5));
        result.setBanks(lbank);
        result.setAccounts(lacc);
        result.setTime(_time);
        return result;
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
               ad.setTime(detail.get("TIME").toString());

                acc.setBankname(ad.getBankName());

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
        //莎莎化妆品(中国）有限公司

        String s1 = "贵公司尾号1302的账户9月15日16时59分ARAP对账系统2003-2008服务费收入人民币11100.00元，余额27597.33元。对方户名:莎莎化妆品(中国）有限公司。[建设银行]";
        s1 = "贵公司尾号1302的账户9月15日16时59分ARAP对账系统2003-2008服务费收入人民币11100.00元，余额27597.33元。对方户名:莎莎化妆品(中国）有限公司。[建设银行]";
        String s2 = "贵公司尾号1303的账户9月15日16时59分ARAP对账系统2003-2008服务费收入人民币11100.00元，余额27597.33元。对方户名:莎莎化妆品(中国）有限公司。[建设银行]";
        MsgUtil util = new MsgUtil();
        Map<String,String> map = new HashMap<String,String>();
        map.put("msg", s1);
        map.put("time", "1610105768");
        ser.processMsg(map);
        map.put("msg", s2);
        map.put("time", "1610105769");
        ser.processMsg(map);

        System.out.println(ser.getInfo());
        System.out.println(ser.getDetail("1302"));
        System.out.println(ser.getDetail("1303"));
        Total total = ser.getTotal();
        System.out.println(total);
    }

    public void doMain() {
        MessageService ser = new MessageService();
        System.out.println(ser.getInfo());
        String s1 = "贵公司尾号1302的账户9月15日16时59分ARAP对账系统2003-2008服务费收入人民币11100.00元，余额27597.33元。对方户名:莎莎化妆品(中国）有限公司。[建设银行]";
        s1 = "贵公司尾号1302的账户9月15日16时59分ARAP对账系统2003-2008服务费收入人民币11100.00元，余额27597.33元。对方户名:莎莎化妆品(中国）有限公司。[建设银行]";
        String s2 = "贵公司尾号1303的账户9月15日16时59分ARAP对账系统2003-2008服务费收入人民币11100.00元，余额27597.33元。对方户名:莎莎化妆品(中国）有限公司。[建设银行]";
        MsgUtil util = new MsgUtil();
        Map<String,String> map = new HashMap<String,String>();
        map.put("msg", s1);
        map.put("time", "1610105768");
        map.put("uuid", "111111");
        map.put("phone", "111111");
        ser.processMsg(map);
        map.put("msg", s2);
        map.put("time", "1610105769");
        map.put("uuid", "111111");
        map.put("phone", "111111");
        ser.processMsg(map);

        System.out.println(ser.getInfo());
        System.out.println(ser.getDetail("1302"));
        System.out.println(ser.getDetail("1303"));
        Total total = ser.getTotal();
        System.out.println(total);
    }
}
