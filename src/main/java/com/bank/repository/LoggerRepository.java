package com.bank.repository;

import com.bank.entity.Logger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggerRepository extends JpaRepository<Logger,Integer> {
    Logger findById(int actId);

}
