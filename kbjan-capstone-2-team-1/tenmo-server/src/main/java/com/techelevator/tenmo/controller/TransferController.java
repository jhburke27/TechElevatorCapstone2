package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {

    private JdbcTransferDao jdbcTransferDao;
    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao, JdbcTransferDao jdbcTransferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.jdbcTransferDao = jdbcTransferDao;
    }

    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId) {
        return transferDao.getTransfer(transferId);
    }

    @RequestMapping(path = "/accounts/{accountId}", method = RequestMethod.GET)
    public Transfer getTransferByAccountId(@PathVariable int accountId) {
        return transferDao.getTransferByAccountId(accountId);
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public String createTransfer(@RequestBody Transfer transfer) {
        if (transfer.getAmount().compareTo(accountDao.getBalanceByAccountId(transfer.getFromAccountId())) >= 0) {
            return "Transfer amount cannot exceed current balance.";
        } else if (transfer.getFromAccountId() == transfer.getToAccountId()) {
            return "Error! You cannot send money to yourself!";
        } else if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return "You have to send an amount greater than zero.";
        }
        transferDao.createTransfer(transfer.getToAccountId(), transfer.getFromAccountId(), transfer.getAmount(), accountDao.getBalanceByAccountId(transfer.getFromAccountId()));
        accountDao.addToBalance(transfer.getAmount(), transfer.getToAccountId());
        accountDao.subtractFromBalance(transfer.getAmount(), transfer.getFromAccountId());
        return "Transfer Successfully Created and Sent!";
    }

    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public String createRequestTransfer(@RequestBody Transfer transfer) {
        if (transfer.getFromAccountId() == transfer.getToAccountId()) {
            return "You cannot request money from yourself";
        } else if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return "You have to send an amount greater than zero.";
        }
        transferDao.createRequestTransfer(transfer.getToAccountId(), transfer.getFromAccountId(), transfer.getAmount(), accountDao.getBalanceByAccountId(transfer.getFromAccountId()));
        return "Request Sent - Pending Approval.";
    }

    @RequestMapping(path = "/request/approved", method = RequestMethod.POST)
    public String executeRequest(@RequestBody Transfer transfer) {
        if (transfer.getAmount().compareTo(accountDao.getBalanceByAccountId(transfer.getFromAccountId())) <= 0) {
            transferDao.executeRequest(transfer);
            accountDao.addToBalance(transfer.getAmount(), transfer.getToAccountId());
            accountDao.subtractFromBalance(transfer.getAmount(), transfer.getFromAccountId());
            return "Request Successfully Approved";
        }
        return "Account balance must be greater than transfer amount";
    }

    @RequestMapping(path = "/request/rejected", method = RequestMethod.POST)
    public String rejectRequest(@RequestBody Transfer transfer) {
        transferDao.rejectRequest(transfer);
        return "Transfer Successfully Rejected";
    }

    @RequestMapping(path = "/mytransfers/{accountId}", method = RequestMethod.GET)
    public List<Transfer> getTransferListByAccountId(@PathVariable int accountId) {
        List<Transfer> nullList = new ArrayList<>();
        nullList = jdbcTransferDao.getTransferListByAccountId(accountId);
        return nullList;
    }
}