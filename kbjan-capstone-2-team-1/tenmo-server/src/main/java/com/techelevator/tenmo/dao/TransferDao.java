package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    public Transfer getTransfer(int transferId);
    public Transfer getTransferByAccountId(int account_id);
    public int getTransferStatus(int transfer_id);
    public void createTransfer(int toAccountId, int fromAccountId, BigDecimal amount, BigDecimal currentBalance);
    public void createRequestTransfer(int toAccountId, int fromAccountId, BigDecimal amount, BigDecimal currentBalance);
    public void executeRequest(Transfer transfer);
    public void rejectRequest(Transfer transfer);
    public List<Transfer> pendingTransfer(int accountId);
}
