package com.geekcattle.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class TextPraser {

    public static boolean sendPostRequest(String path,
                                          Map<String, String> params, String encoding) throws Exception {
        StringBuilder data = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                data.append(entry.getKey()).append("=");
                data.append(URLEncoder.encode(entry.getValue(), encoding));// 编码
                data.append('&');
            }
            data.deleteCharAt(data.length() - 1);
        }
        byte[] entity = data.toString().getBytes(); // 得到实体数据
        HttpURLConnection connection = (HttpURLConnection) new URL(path)
                .openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length",
                String.valueOf(entity.length));

        connection.setDoOutput(true);// 允许对外输出数据
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(entity);

        if (connection.getResponseCode() == 200) {
            return true;
        }
        return false;
    }

    public String s1 = "贵公司尾号1302的账户9月15日16时59分ARAP对账系统2003-2008服务费收入人民币11100.00元，余额27597.33元。对方户名:莎莎化妆品(中国）有限公司。[建设银行]";
    public String s2 = "贵公司尾号1302的账户10月9日12时56分报销支出人民币15000.00元，余额26390.99元。对方户名:陈建。[建设银行]";
    public String s3 = "您公司账户1325,于1203 1315收入(其他小额)人民币25000.00当前余额68671.49,对方为上海鑫卡源供应链管理有限公司(账号*3364)【中国银行】";
    public String s4 = "您公司账户1325,于1130 1139支取(柜台现金)人民币50000.00当前余额43671.49【中国银行】";

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
    public static void main(String[] args) throws Exception {
        TextPraser p = new TextPraser();
        String s = p.s1;
        Map<String,String> ss = p.prase(s);
        System.out.println(ss);
        s = p.s2;
        ss = p.prase(s);
        System.out.println(ss);
        s = p.s3;
        ss = p.prase(s);
        System.out.println(ss);
        s = p.s4;
        ss = p.prase(s);
        System.out.println(ss);
        String s2 = URLEncoder.encode(s, "UTF-8");
        System.out.println(s2);
        String s3 = URLDecoder.decode(s2, "UTF-8");
        System.out.println(s3);
        long xx = 1324666345;
        String s4 = String.valueOf(xx);
        System.out.println(s4);
        ss.put("msg", p.s1);
        ss.put("time", "1610105768");
        ss.put("uuid", "111111");
        ss.put("phone", "111111");
//        boolean a = p.sendPostRequest("http://119.27.173.165:8000/uploadMsg",ss, "UTF-8");
        boolean a = p.sendPostRequest("http://127.0.0.1:8000/gee/uploadMsg",ss, "UTF-8");
        System.out.println(a);
    }
}
