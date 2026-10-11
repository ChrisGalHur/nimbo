
package com.christian.nimbo.repository;

import com.christian.nimbo.model.dto.FinancialCategoryResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinancialCategoryRepository {

    //region Find active categories by user and type
    List<FinancialCategoryResponse> findActiveByUserAndType(
            String userId,
            String categoryType
    );
    //endregion

    //region Find category accessible by user
    Optional<FinancialCategoryResponse> findCategoryByIdAndUserId(
            UUID id,
            String userId
    );
    //endregion
}