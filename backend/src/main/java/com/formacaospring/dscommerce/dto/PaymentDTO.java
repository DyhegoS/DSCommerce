package com.formacaospring.dscommerce.dto;

import java.time.Instant;

import com.formacaospring.dscommerce.entities.Payment;
import com.formacaospring.dscommerce.entities.PaymentStatus;

public class PaymentDTO {
    private Long id;
    private Instant moment;
    private PaymentStatus status;

    public PaymentDTO(){
    }

    public PaymentDTO(Long id, Instant moment, PaymentStatus status) {
        this.id = id;
        this.moment = moment;
        this.status = status;
    }

    public PaymentDTO(Payment entity){
        id = entity.getId();
        moment = entity.getMoment();
        status = entity.getStatus();
    }

    public Long getId() {
        return id;
    }

    public Instant getMoment() {
        return moment;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}
