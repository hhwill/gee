package com.geekcattle.util;

import com.geekcattle.model.msg.MsgShortInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MsgUtil {

    public static String TYPE = "TYPE";
    public static String ACCNO = "ACCNO";
    public static String AMOUNT = "AMOUNT";
    public static String BALANCE = "BALANCE";
    public static String OPP = "OPP";
    public static String BANK = "BANK";
    public static String TIME = "TIME";
    public static String UUID = "UUID";
    public static String PHONE = "PHONE";
    public static String T = "1";
    public static String F = "0";

    public Map<String,String> prase(String src) {
        if (src.indexOf("收入")>= 0 && src.indexOf("支取") < 0 && src.indexOf("支出") < 0) {
            return praseIncome(src);
        } else {
            return prasePayout(src);
        }
    }
    public Map<String, String> praseIncome(String src) {
        Map<String, String> result = new HashMap<String, String>();
        result.put(TYPE, "INCOME");
        result.put(ACCNO, getNumber(src, "尾号|账户"));
        result.put(AMOUNT, getNumber(src, "人民币"));
        result.put(BALANCE, getNumber(src, "余额"));
        result.put(OPP, getOpp(src, "对方户名:|对方为", "。|【"));
        result.put(BANK, getOpp(src, "[|【", "]|】"));
        return result;
    }

    public Map<String, String> prasePayout(String src) {
        Map<String, String> result = new HashMap<String, String>();
        result.put(TYPE, "PAYOUT");
        result.put(ACCNO, getNumber(src, "尾号|账户"));
        result.put(AMOUNT, getNumber(src, "人民币"));
        result.put(BALANCE, getNumber(src, "余额"));
        result.put(OPP, getOpp(src, "对方户名:|对方为", "。|【"));
        result.put(BANK, getOpp(src, "[|【", "]|】"));
        return result;
    }

    public String getOpp(String src, String keyword, String tail) {
        String result = "";
        String[] keywords = keyword.split("\\|");
        String[] tails = tail.split("\\|");
        for (int i = 0; i < keywords.length; i++) {
            if (src.indexOf(keywords[i]) > 0) {
                result = src.substring(src.indexOf(keywords[i])+keywords[i].length());
                for (int j = 0; j < tails.length; j++) {
                    if (result.indexOf(tails[j])> 0) {
                        result = result.substring(0, result.indexOf(tails[j]));
                    }
                }
            }
        }
        return result;
    }

    public String getNumber(String src, String keyword) {
        String result = "";
        String[] ss = keyword.split("\\|");
        for (int i = 0; i < ss.length; i++) {
            if (src.indexOf(ss[i]) >= 0) {
                if (result.equals("")) {
                    String sbegin = src.substring(src.indexOf(ss[i])+ss[i].length());
                    boolean begin = false;
                    for (int j = 0; j < sbegin.length(); j++) {
                        char c = sbegin.charAt(j);
                        if (c == '0' || c == '1'|| c == '2'|| c == '3'|| c == '4'|| c == '5'||
                                c == '6'|| c == '7'|| c == '8'|| c == '9'|| c == '.') {
                            if (!begin) {
                                begin = true;
                            }
                            result += c;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    public MsgShortInfo getMsgShortInfo(Map<String, String> value) {
        boolean flag = true;//判断数据是否有效
        MsgShortInfo info = new MsgShortInfo();
        info.setInfoId(RandomStringUtils.randomAlphanumeric( 16 ));

        if(StringUtils.isBlank(value.get(TIME))){
            flag = false;
        }else {
            info.setInfoTime(value.get(TIME));
        }

        if(StringUtils.isBlank(value.get(TYPE))){
            flag = false;
        }else {
            info.setInfoType(value.get(TYPE));
        }
        if(StringUtils.isBlank(value.get(ACCNO))){
            flag = false;
        }else {
            info.setInfoAccountNum(value.get(ACCNO));
        }
        if(StringUtils.isBlank(value.get(AMOUNT))){
            flag = false;
        }else {
            info.setInfoAmt(new BigDecimal(value.get(AMOUNT)));
        }
        if(StringUtils.isBlank(value.get(OPP))){
            flag = false;
        }else {
            info.setInfoOpp(value.get(OPP));
        }
        if(StringUtils.isBlank(value.get(BANK))){
            flag = false;
        }else {
            info.setInfoBankName(value.get(BANK));
        }
        if(StringUtils.isBlank(value.get(BALANCE))){
            flag = false;
        }else {
            info.setInfoBalance(new BigDecimal(value.get(BALANCE)));
        }

        if(StringUtils.isBlank(value.get(BANK))){
            flag = false;
        }else {
            info.setInfoBankName(value.get(BANK));
        }
        if(StringUtils.isBlank(value.get(PHONE))){
            flag = false;
        }else {
            info.setInfoSender(value.get(PHONE));
        }
        if(StringUtils.isBlank(value.get(UUID))){
            flag = false;
        }else {
            info.setInfoReceiveKey(value.get(UUID));
        }

        if(flag){
            info.setDataStatus(T);
        }else {
            info.setDataStatus(F);
        }
        info.setInsertTime(new Date());
        return info;
    }
}
