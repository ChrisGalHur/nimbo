
package com.christian.nimbo.controller;

import com.christian.nimbo.model.dto.FinancialCategoryResponse;
import com.christian.nimbo.model.dto.MonthlySnapshotRequest;
import com.christian.nimbo.model.dto.MonthlySnapshotResponse;
import com.christian.nimbo.model.dto.MonthlyTransactionRequest;
import com.christian.nimbo.model.dto.MonthlyTransactionResponse;
import com.christian.nimbo.service.RecordsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/records")
public class RecordsController {

    private final RecordsService recordsService;

    public RecordsController(RecordsService recordsService) {
        this.recordsService = recordsService;
    }

    //region Get monthly snapshot
    @GetMapping("/monthly")
    public ResponseEntity<MonthlySnapshotResponse> getMonth(
            @RequestParam String month,
            HttpServletRequest httpRequest
    ) {
        String userId = getAuthenticatedUserId(httpRequest);

        LocalDate monthDate;

        try {
            monthDate = YearMonth.parse(month).atDay(1);
        } catch (DateTimeParseException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid month format. Expected YYYY-MM."
            );
        }

        return ResponseEntity.ok(
                recordsService.getMonth(userId, monthDate)
        );
    }
    //endregion

    //region Create monthly snapshot
    @PostMapping("/monthly")
    public ResponseEntity<MonthlySnapshotResponse> createMonth(
            @RequestBody MonthlySnapshotRequest request,
            HttpServletRequest httpRequest
    ) {
        String userId = getAuthenticatedUserId(httpRequest);

        MonthlySnapshotResponse response =
                recordsService.createMonth(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    //endregion

    //region Update monthly snapshot
    @PutMapping("/monthly/{id}")
    public ResponseEntity<MonthlySnapshotResponse> updateMonth(
            @PathVariable UUID id,
            @RequestBody MonthlySnapshotRequest request,
            HttpServletRequest httpRequest
    ) {
        String userId = getAuthenticatedUserId(httpRequest);

        return ResponseEntity.ok(
                recordsService.updateMonth(userId, id, request)
        );
    }
    //endregion

    //region Get financial categories
    @GetMapping("/categories")
    public ResponseEntity<List<FinancialCategoryResponse>> getCategories(
            @RequestParam(defaultValue = "all") String type,
            HttpServletRequest httpRequest
    ) {
        String userId = getAuthenticatedUserId(httpRequest);

        return ResponseEntity.ok(
                recordsService.getCategories(userId, type)
        );
    }
    //endregion

    //region Get monthly transactions
    @GetMapping("/transactions")
    public ResponseEntity<List<MonthlyTransactionResponse>> getTransactions(
            @RequestParam UUID snapshotId,
            HttpServletRequest httpRequest
    ) {
        String userId = getAuthenticatedUserId(httpRequest);

        return ResponseEntity.ok(
                recordsService.getTransactions(userId, snapshotId)
        );
    }
    //endregion

    //region Create transaction
    @PostMapping("/transactions")
    public ResponseEntity<MonthlyTransactionResponse> createTransaction(
            @RequestBody MonthlyTransactionRequest request,
            HttpServletRequest httpRequest
    ) {
        String userId = getAuthenticatedUserId(httpRequest);

        MonthlyTransactionResponse response =
                recordsService.createTransaction(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    //endregion

    //region Update transaction
    @PutMapping("/transactions/{id}")
    public ResponseEntity<MonthlyTransactionResponse> updateTransaction(
            @PathVariable UUID id,
            @RequestBody MonthlyTransactionRequest request,
            HttpServletRequest httpRequest
    ) {
        String userId = getAuthenticatedUserId(httpRequest);

        return ResponseEntity.ok(
                recordsService.updateTransaction(userId, id, request)
        );
    }
    //endregion

    //region Delete transaction
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable UUID id,
            HttpServletRequest httpRequest
    ) {
        String userId = getAuthenticatedUserId(httpRequest);

        recordsService.deleteTransaction(userId, id);

        return ResponseEntity.noContent().build();
    }
    //endregion

    //region Authenticated user
    private String getAuthenticatedUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");

        if (!(userId instanceof String) || ((String) userId).isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authenticated user not found"
            );
        }

        return (String) userId;
    }
    //endregion
}