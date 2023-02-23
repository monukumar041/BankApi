package com.bank.services;

import com.bank.controller.LoggerController;
import com.bank.entity.Account;
import com.bank.entity.Logger;
import com.bank.exception.NoDataFoundException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired()
    private AccountRepository accountRepository;
    @Autowired
    private LoggerController loggerController;

    public int getBalance(int actId) {
        Account account = accountRepository.findById(actId);
        if (account != null) {
            return accountRepository.findBalanceByAcctId(actId);
        } else {
            throw new ResourceNotFoundException("Account Not Found With Account Id:" + actId);
        }
    }

    public Account getAccountInfo(int actId) {
        Account account = accountRepository.findById(actId);
        if (account != null) {
            return account;
        }
        else {
            throw new ResourceNotFoundException("Account Not Found With Given Account ID:" + actId);
        }
    }

    public Account deleteAccount(int actId) {
        Account account = accountRepository.findById(actId);
        if (account != null) {
            return accountRepository.deleteById(actId);
        }
        throw new ResourceNotFoundException("Account Not Found With Given Account Id:" + actId);
    }

    @Transactional
    public void depositeAmount(int actId, int amount) {
        Account account = accountRepository.findById(actId);
        if (account != null) {
            int initialBalance = getBalance(actId);
            accountRepository.saveBalanceByAcctId(actId, amount);
            Logger logger = new Logger(actId, "Deposited", "Success", initialBalance, initialBalance + amount);
            loggerController.addLog(logger);
        } else {
            throw new ResourceNotFoundException("No Account Found With Account Id:" + actId);
        }

    }

    @Transactional
    public void withdrawAmount(int actId, int amount) {
        Account account = accountRepository.findById(actId);
        int initialBal = getBalance(actId);
        if (initialBal <= 0 || initialBal < amount) {
            throw new NoDataFoundException("SORRY!!!!Insufficient Balance");
        } else if (account != null) {
            accountRepository.withdrawAmountByAcctId(actId, amount);
            Logger logger = new Logger(actId, "WithDraw", "Success", initialBal, initialBal - amount);
            loggerController.addLog(logger);

        }
        else
        {
            throw new ResourceNotFoundException("Account Not Found With Account Id:" + actId);
        }


    }

    @Transactional
    public void tranferAmount(int actId, int destAcctId, int amount) {
        Account senderAct = accountRepository.findById(actId);
        Account receiverAct = accountRepository.findById(destAcctId);
        int initialSenderBAl = getBalance(actId);
        int initialReceiverBAl = getBalance(destAcctId);
        if (initialSenderBAl == 0 || initialSenderBAl < amount) {
            throw new NoDataFoundException("SORRY!!!!!Insufficient Balance!!");
        } else if (senderAct != null && receiverAct != null) {
            accountRepository.withdrawAmountByAcctId(actId, amount);
            accountRepository.saveBalanceByAcctId(destAcctId, amount);
            Logger logger1 = new Logger(destAcctId, "Credited", "Success", initialReceiverBAl, initialReceiverBAl + amount);
            loggerController.addLog(logger1);
            Logger logger = new Logger(actId, "Transferred", "Success", initialSenderBAl, initialSenderBAl - amount);
            loggerController.addLog(logger);
        } else {
            throw new ResourceNotFoundException("Account Not Fount With Accout Id:" + actId);
        }
    }

    public List<Account> getAccounts() {
        List<Account> list = accountRepository.findAll();
        if (list.size() != 0) {
            return list;
        }
        throw new NoDataFoundException("Accounts Not Found in Our DataBase");

    }
}
