package com.bank.repository;
import com.bank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;


public interface AccountRepository extends JpaRepository<Account,Integer> {
    Account findById(int actId);
    Account deleteById(int actId);
    @Query("select balance from Account where acctId=?1")
    int findBalanceByAcctId(int actId);

    @Modifying(clearAutomatically = true)
    @Query("update Account set balance=balance+?2 where acctId=?1")
   void saveBalanceByAcctId(int actId, int amount);


    @Modifying(clearAutomatically = true)
    @Query("update Account set balance=balance-?2 where acctId=?1")
    void withdrawAmountByAcctId(int actId, int amount);
}
