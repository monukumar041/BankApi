package com.bank.exception;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorObject {
    private int statusCode;
    private String message;
    private long timeStamped;
}
