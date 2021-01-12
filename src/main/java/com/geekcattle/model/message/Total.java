package com.geekcattle.model.message;

import java.math.BigDecimal;
import java.util.List;

public class Total {

    BigDecimal balance;//CNY 余额
    BigDecimal ubalance;//USD 余额

    List<Bank> banks;
    List<Account> accounts;//账户余额明细
    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
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
}
