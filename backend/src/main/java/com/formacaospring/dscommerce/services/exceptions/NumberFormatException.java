package com.formacaospring.dscommerce.services.exceptions;

@SuppressWarnings("serial")
public class NumberFormatException extends RuntimeException{
    public NumberFormatException(String msg){
        super(msg);
    }
}
