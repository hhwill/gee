package com.geekcattle.model.message;

import java.util.List;

public class Account {
    String balance;
    String accountno;
    String bankname;

    public Account() {

    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
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

    public List<AccountDetail> getDetails() {
        return details;
    }

    public void setDetails(List<AccountDetail> details) {
        this.details = details;
    }

    List<AccountDetail> details;
}
