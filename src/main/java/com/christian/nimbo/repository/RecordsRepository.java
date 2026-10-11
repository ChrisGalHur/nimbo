
package com.christian.nimbo.repository;

import com.christian.nimbo.model.dto.MonthlySnapshotRequest;
import com.christian.nimbo.model.dto.MonthlySnapshotResponse;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface RecordsRepository {

    //region Find monthly snapshot
    Optional<MonthlySnapshotResponse> findByUserIdAndMonth(
            String userId,
            LocalDate month
    );
    //endregion

    //region Find owned monthly snapshot
    Optional<MonthlySnapshotResponse> findByIdAndUserId(
            UUID id,
            String userId
    );
    //endregion

    //region Create monthly snapshot
    MonthlySnapshotResponse create(
            String userId,
            MonthlySnapshotRequest request
    );
    //endregion

    //region Update monthly snapshot
    MonthlySnapshotResponse update(
            UUID id,
            String userId,
            MonthlySnapshotRequest request
    );
    //endregion
}