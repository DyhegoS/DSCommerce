package com.formacaospring.dscommerce.services.exceptions;

@SuppressWarnings("serial")
public class IllegalArgumentException extends RuntimeException{
    public IllegalArgumentException(String msg){
        super(msg);
    }
}
