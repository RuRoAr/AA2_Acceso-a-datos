package com.svalero.deliveryAPI.exception;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorRespons {
    private String internalError;
    private String message;

}
