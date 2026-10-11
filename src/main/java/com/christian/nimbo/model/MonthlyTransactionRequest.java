
package com.christian.nimbo.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MonthlyTransactionRequest {

    private UUID snapshotId;
    private UUID categoryId;
    private LocalDate transactionDate;
    private String description;
    private BigDecimal amount;

    //region Getters and setters

    public UUID getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(UUID snapshotId) {
        this.snapshotId = snapshotId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    //endregion
}