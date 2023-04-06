package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/accounts")
public class AccountController {

    @Autowired
    private JdbcAccountDao accountDao;
    @Autowired
    private UserDao userDao;

    public AccountController(UserDao dao, JdbcAccountDao accountDao) {
        this.userDao = dao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/{accountId}/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int accountId){
        return accountDao.getBalanceByAccountId(accountId);
    }

    @RequestMapping(path = "/balance/{userId}", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId(@PathVariable int userId){return accountDao.getBalanceByUserId(userId);}
}