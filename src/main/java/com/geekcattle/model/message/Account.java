package com.geekcattle.model.message;

import java.math.BigDecimal;
import java.util.List;

public class Account {
    BigDecimal balance;//余额
    BigDecimal ubalance;//美元余额
    String accountno;//账号后4位
    String bankname;//银行名称
    String accountId;//系统内部账号ID
    List<AccountDetail> details;

    public Account() {

    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getUbalance() {
        return ubalance;
    }

    public void setUbalance(BigDecimal ubalance) {
        this.ubalance = ubalance;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountNo) {
        this.accountno = accountNo;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<AccountDetail> getDetails() {
        return details;
    }

    public void setDetails(List<AccountDetail> details) {
        this.details = details;
    }


}
