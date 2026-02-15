package com.formacaospring.dscommerce.services.exceptions;

public class IllegalArgumentException extends RuntimeException{
    public IllegalArgumentException(String msg){
        super(msg);
    }
}
