
package com.christian.nimbo.service;

import com.christian.nimbo.model.dto.FinancialCategoryResponse;
import com.christian.nimbo.model.dto.MonthlySnapshotRequest;
import com.christian.nimbo.model.dto.MonthlySnapshotResponse;
import com.christian.nimbo.model.dto.MonthlyTransactionRequest;
import com.christian.nimbo.model.dto.MonthlyTransactionResponse;
import com.christian.nimbo.repository.FinancialCategoryRepository;
import com.christian.nimbo.repository.MonthlyTransactionRepository;
import com.christian.nimbo.repository.RecordsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RecordsService {

    private static final Set<String> CATEGORY_TYPES =
            Set.of("income", "expense", "saving", "investment");

    private final RecordsRepository recordsRepository;
    private final MonthlyTransactionRepository transactionRepository;
    private final FinancialCategoryRepository categoryRepository;

    public RecordsService(
            RecordsRepository recordsRepository,
            MonthlyTransactionRepository transactionRepository,
            FinancialCategoryRepository categoryRepository
    ) {
        this.recordsRepository = recordsRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    //region Find monthly snapshot
    public MonthlySnapshotResponse getMonth(
            String userId,
            LocalDate month
    ) {
        validateMonth(month);

        return recordsRepository.findByUserIdAndMonth(userId, month)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No monthly snapshot exists for this month"
                ));
    }
    //endregion

    //region Create monthly snapshot
    public MonthlySnapshotResponse createMonth(
            String userId,
            MonthlySnapshotRequest request
    ) {
        validateRequest(request);

        if (recordsRepository.findByUserIdAndMonth(
                userId,
                request.getMonth()
        ).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A monthly snapshot already exists for this month"
            );
        }

        return recordsRepository.create(userId, request);
    }
    //endregion

    //region Update monthly snapshot
    public MonthlySnapshotResponse updateMonth(
            String userId,
            UUID id,
            MonthlySnapshotRequest request
    ) {
        validateRequest(request);

        MonthlySnapshotResponse existing =
                recordsRepository.findByIdAndUserId(id, userId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Monthly snapshot not found"
                        ));

        if (!existing.getMonth().equals(request.getMonth())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The month of an existing snapshot cannot be changed"
            );
        }

        return recordsRepository.update(id, userId, request);
    }
    //endregion

    //region Get financial categories
    public List<FinancialCategoryResponse> getCategories(
            String userId,
            String type
    ) {
        if (type == null || type.isBlank() || "all".equalsIgnoreCase(type)) {
            List<FinancialCategoryResponse> categories = new ArrayList<>();

            for (String categoryType : CATEGORY_TYPES) {
                categories.addAll(
                        categoryRepository.findActiveByUserAndType(
                                userId,
                                categoryType
                        )
                );
            }

            return categories;
        }

        String normalizedType = type.toLowerCase();

        if (!CATEGORY_TYPES.contains(normalizedType)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid category type"
            );
        }

        return categoryRepository.findActiveByUserAndType(
                userId,
                normalizedType
        );
    }
    //endregion

    //region Get monthly transactions
    public List<MonthlyTransactionResponse> getTransactions(
            String userId,
            UUID snapshotId
    ) {
        if (snapshotId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "snapshotId is required"
            );
        }

        requireOwnedSnapshot(userId, snapshotId);

        return transactionRepository.findBySnapshotIdAndUserId(
                snapshotId,
                userId
        );
    }
    //endregion

    //region Create transaction
    public MonthlyTransactionResponse createTransaction(
            String userId,
            MonthlyTransactionRequest request
    ) {
        validateTransactionRequest(request);
        requireOwnedSnapshot(userId, request.getSnapshotId());

        try {
            return transactionRepository.create(userId, request);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage()
            );
        }
    }
    //endregion

    //region Update transaction
    public MonthlyTransactionResponse updateTransaction(
            String userId,
            UUID id,
            MonthlyTransactionRequest request
    ) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transaction id is required"
            );
        }

        validateTransactionRequest(request);
        requireOwnedSnapshot(userId, request.getSnapshotId());

        boolean exists = transactionRepository
                .findByIdAndSnapshotIdAndUserId(
                        id,
                        request.getSnapshotId(),
                        userId
                )
                .isPresent();

        if (!exists) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Transaction not found in the specified monthly snapshot"
            );
        }

        try {
            return transactionRepository.update(id, userId, request);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage()
            );
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Transaction not found"
            );
        }
    }
    //endregion

    //region Delete transaction
    public void deleteTransaction(String userId, UUID id) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transaction id is required"
            );
        }

        try {
            transactionRepository.delete(id, userId);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage()
            );
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Transaction not found"
            );
        }
    }
    //endregion

    //region Validate owned snapshot
    private void requireOwnedSnapshot(String userId, UUID snapshotId) {
        if (snapshotId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "snapshotId is required"
            );
        }

        recordsRepository.findByIdAndUserId(snapshotId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Monthly snapshot not found"
                ));
    }
    //endregion

    //region Validate snapshot request
    private void validateRequest(MonthlySnapshotRequest request) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Request body is required"
            );
        }

        validateMonth(request.getMonth());
        validateAmount(request.getIncome(), "income");
        validateAmount(request.getExpenses(), "expenses");
        validateAmount(request.getMonthlySavings(), "monthlySavings");
        validateAmount(request.getMonthlyInvestment(), "monthlyInvestment");
    }
    //endregion

    //region Validate transaction request
    private void validateTransactionRequest(
            MonthlyTransactionRequest request
    ) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Request body is required"
            );
        }

        if (request.getSnapshotId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "snapshotId is required"
            );
        }

        if (request.getCategoryId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "categoryId is required"
            );
        }

        if (request.getTransactionDate() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "transactionDate is required"
            );
        }

        if (request.getAmount() == null
                || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "amount must be greater than zero"
            );
        }

        if (request.getDescription() == null) {
            request.setDescription("");
        } else {
            request.setDescription(request.getDescription().trim());
        }
    }
    //endregion

    //region Validate month
    private void validateMonth(LocalDate month) {
        if (month == null || month.getDayOfMonth() != 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "month must be the first day of the month"
            );
        }
    }
    //endregion

    //region Validate amount
    private void validateAmount(BigDecimal amount, String field) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    field + " must be zero or greater"
            );
        }
    }
    //endregion
}