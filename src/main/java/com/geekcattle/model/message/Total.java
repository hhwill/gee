package com.geekcattle.model.message;

import java.util.List;

public class Total {
    String balance;
    String ubalance;

    public String getUbalance() {
        return ubalance;
    }

    public void setUbalance(String ubalance) {
        this.ubalance = ubalance;
    }

    List<Bank> banks;
    List<Account> accounts;
    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
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
}
