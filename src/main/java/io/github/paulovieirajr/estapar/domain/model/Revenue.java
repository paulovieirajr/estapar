package io.github.paulovieirajr.estapar.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class Revenue {

    private final UUID id;
    private final LocalDate date;
    private BigDecimal amount;
    private final String currencyCode;

    public Revenue(UUID id, LocalDate date, BigDecimal amount, String currencyCode) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public void addRevenueAmount(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
