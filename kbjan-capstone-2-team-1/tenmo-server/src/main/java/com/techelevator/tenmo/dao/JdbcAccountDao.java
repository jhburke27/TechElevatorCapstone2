package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createAccount(Account account, int accountId) {
    }

    @Override
    public Account getAccount(int accountId) {

        return null;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;

        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        if(results.next()) {
            account = mapRowToAccount(results);
        }

        return null;
    }

    @Override
    public BigDecimal getBalanceByAccountId(int accountId) {

        String sql = "SELECT balance FROM account WHERE account_id = ?;";
        SqlRowSet results = null;
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);

        return balance;

    }


    @Override
    public BigDecimal getBalanceByUserId(int userId) {

        String sql = "SELECT balance from account WHERE user_id = ?;";
        SqlRowSet results = null;
        BigDecimal balance = null;
        results = jdbcTemplate.queryForRowSet(sql, userId);
        if(results.next()){
            balance = results.getBigDecimal("balance");
        }
        return balance;
    }

    public BigDecimal addToBalance(BigDecimal amount, int accountId) {

        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ? RETURNING balance;";

        BigDecimal newBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, amount, accountId);

        return newBalance;
    }

    public BigDecimal subtractFromBalance(BigDecimal amount, int accountId) {

        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ? RETURNING balance;";

        BigDecimal newBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, amount, accountId);

        return newBalance;
    }



    public Account mapRowToAccount (SqlRowSet results) {
        Account account = new Account();

        int userId = results.getInt("user_id");
        account.setUserId(userId);

        int accountId = results.getInt("account_id");
        account.setAccountId(accountId);

        BigDecimal balance = results.getBigDecimal("balance");
        account.setBalance(balance);

        return account;
    }
}