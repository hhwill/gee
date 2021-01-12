package com.geekcattle.model.message;

import java.math.BigDecimal;

public class AccountDetail {

    private String accountNo;//账号后4位
    private String bankName;//银行名称
    private BigDecimal amount;//交易金额
    private BigDecimal balance;//余额
    private String type;//收入、支出
    private String opp;//付款方等信息
    private String time;//交易时间

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpp() {
        return opp;
    }

    public void setOpp(String opp) {
        this.opp = opp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
