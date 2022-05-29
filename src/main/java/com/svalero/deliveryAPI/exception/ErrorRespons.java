package com.svalero.deliveryAPI.exception;


import lombok.AllArgsConstructor;
import lombok.Data;

import static com.svalero.deliveryAPI.exception.Constants.MANDATORY_FIELD_ERROR_CODE;

@Data
@AllArgsConstructor
public class ErrorRespons {
    private int internalError;
    private String message;

    public static ErrorRespons mandatoryField  (String message){
        return new ErrorRespons(MANDATORY_FIELD_ERROR_CODE, message);
    }

}
