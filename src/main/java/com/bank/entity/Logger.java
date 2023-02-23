package com.bank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="Logger")
public class Logger {
    @Id
    private int actId;
    private String transType;
    private String transStatus;
    private int initialBal;
    private int finalBal;
}
