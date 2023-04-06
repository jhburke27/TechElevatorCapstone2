package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    public void createAccount(Account account, int accountId);
    public Account getAccount(int accountId);
    public Account getAccountByUserId(int userId);
    public BigDecimal getBalanceByAccountId(int accountId);
    public BigDecimal getBalanceByUserId(int userId);
    public BigDecimal addToBalance (BigDecimal amountToAdd, int id);
    public BigDecimal subtractFromBalance (BigDecimal amountToSubtract, int id);
}
