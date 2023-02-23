package com.bank.controller;

import com.bank.entity.Account;
import com.bank.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountControllerTest {

    Account account = new Account(1234, 1000, "Active");
    Account account1 = new Account(1235, 2000, "Active");
    Account account2 = new Account(1236, 10000, "Active");
    Account account3 = new Account(1237, 20300, "Active");
    @InjectMocks
    private AccountController accountController;
    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void getBalanceSuccess() throws Exception {
        given(accountService.getBalance(account.getAcctId())).willReturn(account.getBalance());
        int bal=accountController.getBalance(account.getAcctId());
        assertEquals(account.getBalance(),bal);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/1234/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(account.getBalance())));


    }

    @Test
    void getAccountInfoSuccess() throws Exception {
        given(accountService.getAccountInfo(account1.getAcctId())).willReturn(account1);
        Account act=accountController.getAccountInfo(account1.getAcctId());
        assertEquals(account1.getBalance(),act.getBalance());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/1235")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.balance", is(account1.getBalance())));
    }

    @Test
    void deleteAccountSuccess() throws Exception {
        given(accountService.deleteAccount(account3.getAcctId())).willReturn(account3);
        Account act=accountController.deleteAccount(account3.getAcctId());
        assertEquals(account3.getBalance(),act.getBalance());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/account/1237")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.balance", is(account3.getBalance())));
    }

    @Test
    void depositeAmountSuccess() throws Exception {
        doNothing().when(accountService).depositeAmount(account.getAcctId(), 500);
        accountController.depositeAmount(account.getAcctId(), 500);
        verify(accountService).depositeAmount(1234, 500);


    }

    @Test
    void withdrawAmountSuccess() throws Exception {
        doNothing().when(accountService).withdrawAmount(account.getAcctId(), 300);
        accountController.withdrawAmount(account.getAcctId(), 300);
        verify(accountService).withdrawAmount(account.getAcctId(), 300);
    }

    @Test
    void transferAmountSuccess() throws Exception {
        doNothing().when(accountService).tranferAmount(account.getAcctId(), account1.getAcctId(), 10000);
        accountController.transferAmount(account.getAcctId(), account1.getAcctId(), 10000);
        verify(accountService).tranferAmount(account.getAcctId(), account1.getAcctId(), 10000);
    }

    @Test
    void getAccounts() throws Exception {
        List<Account> list = new ArrayList<>(Arrays.asList(account, account1, account2, account3));
        given(accountService.getAccounts()).willReturn(list);
        List<Account>list1=accountController.getAccounts();
        assertEquals(list.size(),list1.size());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[2].balance", is(10000)));
    }
}
