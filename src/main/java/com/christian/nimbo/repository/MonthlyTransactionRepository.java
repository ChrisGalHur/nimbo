
package com.christian.nimbo.repository;

import com.christian.nimbo.model.dto.MonthlyTransactionRequest;
import com.christian.nimbo.model.dto.MonthlyTransactionResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MonthlyTransactionRepository {

    //region Find transactions by snapshot
    List<MonthlyTransactionResponse> findBySnapshotIdAndUserId(
            UUID snapshotId,
            String userId
    );
    //endregion

    //region Find transaction by id and snapshot
    Optional<MonthlyTransactionResponse> findByIdAndSnapshotIdAndUserId(
            UUID id,
            UUID snapshotId,
            String userId
    );
    //endregion

    //region Create transaction
    MonthlyTransactionResponse create(
            String userId,
            MonthlyTransactionRequest request
    );
    //endregion

    //region Update transaction
    MonthlyTransactionResponse update(
            UUID id,
            String userId,
            MonthlyTransactionRequest request
    );
    //endregion

    //region Delete transaction
    void delete(
            UUID id,
            String userId
    );
    //endregion
}