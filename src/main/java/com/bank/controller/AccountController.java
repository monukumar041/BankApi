package com.bank.controller;
import com.bank.entity.Account;
import com.bank.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;
    @GetMapping("/account/{actId}/balance")
    public int getBalance(@PathVariable("actId")int actId)
    {
        int balance=accountService.getBalance(actId);
        return balance;
    }
    @GetMapping("/account/{actId}")
    public Account getAccountInfo(@PathVariable("actId")int actId)
    {
        Account account=accountService.getAccountInfo(actId);
        return account;
    }

    @DeleteMapping("/account/{actId}")
    public Account deleteAccount(@PathVariable("actId")int actId)
    {
        return accountService.deleteAccount(actId);

    }

    @PutMapping("/account/{actId}/deposite/{amount}")
    public void depositeAmount(@PathVariable("actId")int actId, @PathVariable("amount")int amount)
    {
        accountService.depositeAmount(actId,amount);
    }

    @PutMapping("/account/{actId}/withdraw/{amount}")
    public void withdrawAmount(@PathVariable("actId")int actId,@PathVariable("amount")int amount)
    {
        accountService.withdrawAmount(actId,amount);
    }
    @PutMapping("/account/{actId}/transfer/{destAcctId}/{amount}")
    public void transferAmount(@PathVariable("actId")int actId,@PathVariable("destAcctId")int destAcctId,@PathVariable("amount")int amount)
    {
        accountService.tranferAmount(actId,destAcctId,amount);
    }
    @GetMapping("/account")
    public List<Account>getAccounts()
    {
        List<Account>list=accountService.getAccounts();
        return list;
    }

}
