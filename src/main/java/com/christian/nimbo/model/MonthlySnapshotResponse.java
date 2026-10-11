
package com.christian.nimbo.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MonthlySnapshotResponse {

    private UUID id;
    private LocalDate month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal monthlySavings;
    private BigDecimal monthlyInvestment;

    //region Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getMonth() {
        return month;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getMonthlySavings() {
        return monthlySavings;
    }

    public void setMonthlySavings(BigDecimal monthlySavings) {
        this.monthlySavings = monthlySavings;
    }

    public BigDecimal getMonthlyInvestment() {
        return monthlyInvestment;
    }

    public void setMonthlyInvestment(BigDecimal monthlyInvestment) {
        this.monthlyInvestment = monthlyInvestment;
    }
    //endregion
}