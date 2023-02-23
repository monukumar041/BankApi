package com.bank.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Customer")
public class Customer {
    @Id
    private int actId;
    private String custName;
    private String city;
    private String state;
    private String country;
    private  int phoneNo;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private Account account;
}
