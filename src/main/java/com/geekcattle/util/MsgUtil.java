package com.geekcattle.util;

import java.util.HashMap;
import java.util.Map;

public class MsgUtil {

    public Map<String,String> prase(String src) {
        if (src.indexOf("收入")>= 0 && src.indexOf("支取") < 0 && src.indexOf("支出") < 0) {
            return praseIncome(src);
        } else {
            return prasePayout(src);
        }
    }
    public Map<String, String> praseIncome(String src) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("TYPE", "INCOME");
        result.put("ACCNO", getNumber(src, "尾号|账户"));
        result.put("AMOUNT", getNumber(src, "人民币"));
        result.put("BALANCE", getNumber(src, "余额"));
        result.put("OPP", getOpp(src, "对方户名:|对方为", "。|【"));
        result.put("BANK", getOpp(src, "[|【", "]|】"));
        return result;
    }

    public Map<String, String> prasePayout(String src) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("TYPE", "PAYOUT");
        result.put("ACCNO", getNumber(src, "尾号|账户"));
        result.put("AMOUNT", getNumber(src, "人民币"));
        result.put("BALANCE", getNumber(src, "余额"));
        result.put("OPP", getOpp(src, "对方户名:|对方为", "。|【"));
        result.put("BANK", getOpp(src, "[|【", "]|】"));
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

}
