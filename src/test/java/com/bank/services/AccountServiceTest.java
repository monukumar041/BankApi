package com.bank.services;

import com.bank.controller.LoggerController;
import com.bank.entity.Account;
import com.bank.entity.Logger;
import com.bank.exception.NoDataFoundException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private LoggerController loggerController;
    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBalanceForValidId() {
        int accountId =12;
        Account account2=new Account(accountId,1200,"Active");
        when(accountRepository.findById(accountId)).thenReturn(account2);
        when(accountRepository.findBalanceByAcctId(accountId)).thenReturn(account2.getBalance());
        int result=accountService.getBalance(accountId);
        assertEquals(1200,result);
        verify(accountRepository).findById(accountId);
        verify(accountRepository).findBalanceByAcctId(accountId);
    }
    @Test
    void getBalanceForInvalidId()
    {
        int invalidId=999;
        when(accountRepository.findById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->accountService.getBalance(invalidId));
        verify(accountRepository).findById(invalidId);

    }

    @Test
    void getAccountInfoForvalidId() {
        int accountId = 1;
        Account account2 = new Account(accountId, 19000, "Active");
        when(accountRepository.findById(accountId)).thenReturn(account2);
        Account result = accountService.getAccountInfo(accountId);

        assertEquals(accountId, result.getAcctId());
        assertEquals(19000, result.getBalance());
        assertEquals("Active", result.getActStatus());
        verify(accountRepository).findById(accountId);
    }
    @Test
    void getAccountInfoForInvalidId()
    {
        int invalidId=999;
        when(accountRepository.findById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->accountService.getAccountInfo(invalidId));
        verify(accountRepository).findById(invalidId);

    }


    @Test
    void deleteAccountForValidId()
    {
        int accountId=13;
        Account account2=new Account(accountId,1000,"Active");
        when(accountRepository.findById(accountId)).thenReturn(account2);
        when(accountRepository.deleteById(accountId)).thenReturn(account2);
        Account result=accountService.deleteAccount(accountId);
        assertEquals(13,result.getAcctId());
        assertEquals(1000,result.getBalance());
        assertEquals("Active",result.getActStatus());
        verify(accountRepository).findById(accountId);
        verify(accountRepository).deleteById(accountId);
    }
    @Test
    void deleteAccountForInvalidId()
    {
        int invalidId=999;
        when(accountRepository.findById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->accountService.deleteAccount(invalidId));
        verify(accountRepository).findById(invalidId);
    }

    @Test
    void depositeAmountForValidId()
    {
        int accountId=13;
        Account account2=new Account(accountId,2000,"Active");
        when(accountRepository.findById(accountId)).thenReturn(account2);
        when(accountService.getBalance(accountId)).thenReturn(account2.getBalance());
        doNothing().when(accountRepository).saveBalanceByAcctId(accountId,500);
        accountService.depositeAmount(accountId,500);
        verify(accountRepository,times(3)).findById(accountId);
        verify(loggerController).addLog(any(Logger.class));
        verify(accountRepository).saveBalanceByAcctId(accountId,500);
    }
    @Test
    void depositeAmountForInvalidId()
    {
        int InvalidId=999;
        when(accountRepository.findById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->accountService.depositeAmount(InvalidId,500));
        verify(accountRepository).findById(InvalidId);


    }

    @Test
    void withdrawAmountForValidId() {
        int accountId = 14;
        Account account2 = new Account(accountId, 3000, "Active");
        when(accountRepository.findById(accountId)).thenReturn(account2);
        when(accountService.getBalance(accountId)).thenReturn(account2.getBalance());
        doNothing().when(accountRepository).withdrawAmountByAcctId(accountId, 500);
        accountService.withdrawAmount(accountId, 500);
        verify(accountRepository,times(3)).findById(accountId);
        verify(accountRepository, times(1)).withdrawAmountByAcctId(accountId, 500);
        verify(loggerController, times(1)).addLog(any(Logger.class));
    }
    @Test
    void withdrawAmountForInvalidId()
    {
        int accoutId=1999;
        when(accountRepository.findById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->accountService.withdrawAmount(accoutId,6900));
        verify(accountRepository,times(2)).findById(accoutId);
    }
    @Test
    void withdrawAmountForInsufficientBalance()
    {
        int accountId=70;
        int intitalBal=0;
        int amount=100;
        Account account2=new Account(accountId,intitalBal,"Active");
        when(accountRepository.findById(accountId)).thenReturn(account2);
        when(accountService.getBalance(accountId)).thenReturn(account2.getBalance());
        assertThrows(NoDataFoundException.class,()->accountService.withdrawAmount(accountId,amount));

        verify(accountRepository,times(3)).findById(accountId);
    }


    @Test
    void transferAmount_InsufficientBalance()
    {
        int senderId=12;
        int descentId=14;
        int balance=2000;
        int amount=10000;
        Account senderAccount=new Account(senderId,balance,"Active");
        Account receiverAccount=new Account(descentId,balance,"Active");
        when(accountRepository.findById(senderId)).thenReturn(senderAccount);
        when(accountRepository.findById(descentId)).thenReturn(receiverAccount);
        when(accountService.getBalance(senderId)).thenReturn(senderAccount.getBalance());

        assertThrows(NoDataFoundException.class,()->accountService.tranferAmount(senderId,descentId,amount));
        verify(accountRepository,times(3)).findById(senderId);

    }

    @Test
    void transferAmount_InvalidId()
    {
        int invalidId=1234;
        int invalidId1=1256;
        when(accountRepository.findById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->accountService.tranferAmount(invalidId,invalidId1,1000));
        verify(accountRepository,times(2)).findById(invalidId);
    }

    @Test
    void transferAmount_Successful()
    {
        int accountId=12;
        int descentAccountId=14;
        int balance=2000;
        int amount=100;
        Account senderAccount=new Account(accountId,balance,"Active");
        Account receiverAccount=new Account(descentAccountId,balance,"Active");
        when(accountRepository.findById(accountId)).thenReturn(senderAccount);
        when(accountRepository.findById(descentAccountId)).thenReturn(receiverAccount);
        when(accountService.getBalance(accountId)).thenReturn(senderAccount.getBalance());
        doNothing().when(accountRepository).withdrawAmountByAcctId(accountId,amount);
        doNothing().when(accountRepository).saveBalanceByAcctId(descentAccountId,amount);
        accountService.tranferAmount(accountId,descentAccountId,amount);

        verify(accountRepository,times(3)).findById(accountId);
        verify(accountRepository).saveBalanceByAcctId(descentAccountId,amount);
        verify(accountRepository).withdrawAmountByAcctId(accountId,amount);
        verify(loggerController,times(2)).addLog(any(Logger.class));
    }


    @Test
    void getValidAccounts()
    {
        Account account2=new Account(123,1230,"Actice");
        Account account3=new Account(1234,1231,"Active");
        List<Account>list=new ArrayList<>(Arrays.asList(account2,account3));
        when(accountRepository.findAll()).thenReturn(list);
        List<Account>result=accountService.getAccounts();
        assertEquals(list.size(),result.size());
        assertEquals(list.get(0).getBalance(),result.get(0).getBalance());
        verify(accountRepository).findAll();
    }
    @Test
    void getInvalidAccount()
    {
        List<Account>list=new ArrayList<>();
        when(accountRepository.findAll()).thenReturn(list);
        assertThrows(NoDataFoundException.class,()->accountService.getAccounts());
        verify(accountRepository).findAll();
    }

}