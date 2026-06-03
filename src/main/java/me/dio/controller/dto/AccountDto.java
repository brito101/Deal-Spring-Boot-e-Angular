package me.dio.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import me.dio.domain.model.Account;

import java.math.BigDecimal;

public record AccountDto(
        Long id,
        @NotBlank String number,
        @NotBlank String agency,
        @NotNull @PositiveOrZero BigDecimal balance,
        @NotNull @PositiveOrZero BigDecimal limit) {

    public AccountDto(Account model) {
        this(model.getId(), model.getNumber(), model.getAgency(), model.getBalance(), model.getLimit());
    }

    public Account toModel() {
        Account model = new Account();
        model.setId(this.id);
        model.setNumber(this.number);
        model.setAgency(this.agency);
        model.setBalance(this.balance);
        model.setLimit(this.limit);
        return model;
    }
}
