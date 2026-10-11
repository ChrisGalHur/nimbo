
package com.christian.nimbo.repository;

import com.christian.nimbo.model.dto.FinancialCategoryResponse;
import com.christian.nimbo.model.dto.MonthlySnapshotRequest;
import com.christian.nimbo.model.dto.MonthlySnapshotResponse;
import com.christian.nimbo.model.dto.MonthlyTransactionRequest;
import com.christian.nimbo.model.dto.MonthlyTransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SupabaseRecordsRepository implements
        RecordsRepository,
        MonthlyTransactionRepository,
        FinancialCategoryRepository {

    //region Supabase configuration
    private static final String SNAPSHOTS_TABLE = "/monthly_snapshots";
    private static final String TRANSACTIONS_TABLE = "/monthly_transactions";
    private static final String CATEGORIES_TABLE = "/financial_categories";

    private static final String SNAPSHOT_FIELDS =
            "id,month,income,expenses,monthly_savings,monthly_investment";

    private static final String CATEGORY_FIELDS =
            "id,name,category_type,is_system,is_active";

    private static final String TRANSACTION_FIELDS =
            "id,snapshot_id,category_id,transaction_date,description,amount,"
                    + "financial_categories(name,category_type)";

    private final RestClient restClient;

    public SupabaseRecordsRepository(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.key}") String supabaseKey
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(supabaseUrl + "/rest/v1")
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .defaultHeader("apikey", supabaseKey)
                .defaultHeader("Authorization", "Bearer " + supabaseKey)
                .build();
    }
    //endregion

    //region Find monthly snapshot by user and month
    @Override
    public Optional<MonthlySnapshotResponse> findByUserIdAndMonth(
            String userId,
            LocalDate month
    ) {
        List<Map<String, Object>> results = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(SNAPSHOTS_TABLE)
                        .queryParam("select", SNAPSHOT_FIELDS)
                        .queryParam("user_id", "eq." + userId)
                        .queryParam("month", "eq." + month)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return firstSnapshotResult(results);
    }
    //endregion

    //region Find monthly snapshot by id and user
    @Override
    public Optional<MonthlySnapshotResponse> findByIdAndUserId(
            UUID id,
            String userId
    ) {
        List<Map<String, Object>> results = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(SNAPSHOTS_TABLE)
                        .queryParam("select", SNAPSHOT_FIELDS)
                        .queryParam("id", "eq." + id)
                        .queryParam("user_id", "eq." + userId)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return firstSnapshotResult(results);
    }
    //endregion

    //region Create monthly snapshot
    @Override
    public MonthlySnapshotResponse create(
            String userId,
            MonthlySnapshotRequest request
    ) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("user_id", userId);
        payload.put("month", request.getMonth().toString());
        payload.put("income", request.getIncome());
        payload.put("expenses", request.getExpenses());
        payload.put("monthly_savings", request.getMonthlySavings());
        payload.put("monthly_investment", request.getMonthlyInvestment());

        List<Map<String, Object>> results = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(SNAPSHOTS_TABLE)
                        .queryParam("select", SNAPSHOT_FIELDS)
                        .build())
                .header("Prefer", "return=representation")
                .body(payload)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return requireSnapshotResult(results);
    }
    //endregion

    //region Update monthly snapshot
    @Override
    public MonthlySnapshotResponse update(
            UUID id,
            String userId,
            MonthlySnapshotRequest request
    ) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("income", request.getIncome());
        payload.put("expenses", request.getExpenses());
        payload.put("monthly_savings", request.getMonthlySavings());
        payload.put("monthly_investment", request.getMonthlyInvestment());
        payload.put("updated_at", OffsetDateTime.now().toString());

        List<Map<String, Object>> results = restClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path(SNAPSHOTS_TABLE)
                        .queryParam("select", SNAPSHOT_FIELDS)
                        .queryParam("id", "eq." + id)
                        .queryParam("user_id", "eq." + userId)
                        .build())
                .header("Prefer", "return=representation")
                .body(payload)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return requireSnapshotResult(results);
    }
    //endregion

    //region Find active financial categories by user and type
    @Override
    public List<FinancialCategoryResponse> findActiveByUserAndType(
            String userId,
            String categoryType
    ) {
        List<Map<String, Object>> results = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CATEGORIES_TABLE)
                        .queryParam("select", CATEGORY_FIELDS)
                        .queryParam("category_type", "eq." + categoryType)
                        .queryParam("is_active", "eq.true")
                        .queryParam(
                                "or",
                                "(user_id.is.null,user_id.eq." + userId + ")"
                        )
                        .queryParam("order", "name.asc")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        List<FinancialCategoryResponse> categories = new ArrayList<>();

        if (results != null) {
            for (Map<String, Object> row : results) {
                categories.add(mapCategoryResponse(row));
            }
        }

        return categories;
    }
    //endregion


    //region Find financial category accessible by user
    @Override
    public Optional<FinancialCategoryResponse> findCategoryByIdAndUserId(
            UUID id,
            String userId
    ) {
        List<Map<String, Object>> results = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CATEGORIES_TABLE)
                        .queryParam("select", CATEGORY_FIELDS)
                        .queryParam("id", "eq." + id)
                        .queryParam(
                                "or",
                                "(user_id.is.null,user_id.eq." + userId + ")"
                        )
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (results == null || results.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapCategoryResponse(results.get(0)));
    }
    //endregion

    //region Find transactions by snapshot and user
    @Override
    public List<MonthlyTransactionResponse> findBySnapshotIdAndUserId(
            UUID snapshotId,
            String userId
    ) {
        requireOwnedSnapshot(snapshotId, userId);

        List<Map<String, Object>> results = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(TRANSACTIONS_TABLE)
                        .queryParam("select", TRANSACTION_FIELDS)
                        .queryParam("snapshot_id", "eq." + snapshotId)
                        .queryParam("order", "transaction_date.desc,created_at.desc")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        List<MonthlyTransactionResponse> transactions = new ArrayList<>();

        if (results != null) {
            for (Map<String, Object> row : results) {
                transactions.add(mapTransactionResponse(row));
            }
        }

        return transactions;
    }
    //endregion

    //region Find transaction by id, snapshot and user
    @Override
    public Optional<MonthlyTransactionResponse>
    findByIdAndSnapshotIdAndUserId(
            UUID id,
            UUID snapshotId,
            String userId
    ) {
        requireOwnedSnapshot(snapshotId, userId);

        List<Map<String, Object>> results = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(TRANSACTIONS_TABLE)
                        .queryParam("select", TRANSACTION_FIELDS)
                        .queryParam("id", "eq." + id)
                        .queryParam("snapshot_id", "eq." + snapshotId)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (results == null || results.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapTransactionResponse(results.get(0)));
    }
    //endregion

    //region Create transaction
    @Override
    public MonthlyTransactionResponse create(
            String userId,
            MonthlyTransactionRequest request
    ) {
        requireOwnedSnapshot(request.getSnapshotId(), userId);
        requireAccessibleCategory(request.getCategoryId(), userId);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("snapshot_id", request.getSnapshotId().toString());
        payload.put("category_id", request.getCategoryId().toString());
        payload.put("transaction_date", request.getTransactionDate().toString());
        payload.put("description", request.getDescription());
        payload.put("amount", request.getAmount());

        List<Map<String, Object>> results = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(TRANSACTIONS_TABLE)
                        .queryParam("select", TRANSACTION_FIELDS)
                        .build())
                .header("Prefer", "return=representation")
                .body(payload)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return requireTransactionResult(results);
    }
    //endregion

    //region Update transaction
    @Override
    public MonthlyTransactionResponse update(
            UUID id,
            String userId,
            MonthlyTransactionRequest request
    ) {
        Map<String, Object> existing = findTransactionRowById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Transaction not found"
                ));

        UUID existingSnapshotId = UUID.fromString(
                String.valueOf(existing.get("snapshot_id"))
        );

        requireOwnedSnapshot(existingSnapshotId, userId);

        if (!existingSnapshotId.equals(request.getSnapshotId())) {
            throw new IllegalArgumentException(
                    "A transaction cannot be moved to another snapshot"
            );
        }

        requireAccessibleCategory(request.getCategoryId(), userId);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("category_id", request.getCategoryId().toString());
        payload.put("transaction_date", request.getTransactionDate().toString());
        payload.put("description", request.getDescription());
        payload.put("amount", request.getAmount());
        payload.put("updated_at", OffsetDateTime.now().toString());

        List<Map<String, Object>> results = restClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path(TRANSACTIONS_TABLE)
                        .queryParam("select", TRANSACTION_FIELDS)
                        .queryParam("id", "eq." + id)
                        .queryParam("snapshot_id", "eq." + existingSnapshotId)
                        .build())
                .header("Prefer", "return=representation")
                .body(payload)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return requireTransactionResult(results);
    }
    //endregion

    //region Delete transaction
    @Override
    public void delete(UUID id, String userId) {
        Map<String, Object> existing = findTransactionRowById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Transaction not found"
                ));

        UUID snapshotId = UUID.fromString(
                String.valueOf(existing.get("snapshot_id"))
        );

        requireOwnedSnapshot(snapshotId, userId);

        restClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(TRANSACTIONS_TABLE)
                        .queryParam("id", "eq." + id)
                        .queryParam("snapshot_id", "eq." + snapshotId)
                        .build())
                .retrieve()
                .toBodilessEntity();
    }
    //endregion

    //region Verify snapshot ownership
    private void requireOwnedSnapshot(UUID snapshotId, String userId) {
        List<Map<String, Object>> results = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(SNAPSHOTS_TABLE)
                        .queryParam("select", "id")
                        .queryParam("id", "eq." + snapshotId)
                        .queryParam("user_id", "eq." + userId)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (results == null || results.isEmpty()) {
            throw new IllegalArgumentException(
                    "Monthly snapshot not found for this user"
            );
        }
    }
    //endregion


    //region Verify category access
    private void requireAccessibleCategory(UUID categoryId, String userId) {
        FinancialCategoryResponse category = findCategoryByIdAndUserId(
                categoryId,
                userId
        ).orElseThrow(() -> new IllegalArgumentException(
                "Financial category not found for this user"
        ));

        if (!category.isActive()) {
            throw new IllegalArgumentException(
                    "Financial category is inactive"
            );
        }
    }
    //endregion

    //region Find transaction row by id
    private Optional<Map<String, Object>> findTransactionRowById(UUID id) {
        List<Map<String, Object>> results = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(TRANSACTIONS_TABLE)
                        .queryParam("select", "id,snapshot_id")
                        .queryParam("id", "eq." + id)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (results == null || results.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(results.get(0));
    }
    //endregion

    //region Map snapshot responses
    private Optional<MonthlySnapshotResponse> firstSnapshotResult(
            List<Map<String, Object>> results
    ) {
        if (results == null || results.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapSnapshotResponse(results.get(0)));
    }

    private MonthlySnapshotResponse requireSnapshotResult(
            List<Map<String, Object>> results
    ) {
        if (results == null || results.isEmpty()) {
            throw new IllegalStateException(
                    "Supabase did not return the saved monthly snapshot"
            );
        }

        return mapSnapshotResponse(results.get(0));
    }

    private MonthlySnapshotResponse mapSnapshotResponse(
            Map<String, Object> row
    ) {
        MonthlySnapshotResponse response = new MonthlySnapshotResponse();

        response.setId(UUID.fromString(String.valueOf(row.get("id"))));
        response.setMonth(LocalDate.parse(String.valueOf(row.get("month"))));
        response.setIncome(toBigDecimal(row.get("income")));
        response.setExpenses(toBigDecimal(row.get("expenses")));
        response.setMonthlySavings(toBigDecimal(row.get("monthly_savings")));
        response.setMonthlyInvestment(
                toBigDecimal(row.get("monthly_investment"))
        );

        return response;
    }
    //endregion

    //region Map category response
    private FinancialCategoryResponse mapCategoryResponse(
            Map<String, Object> row
    ) {
        FinancialCategoryResponse response = new FinancialCategoryResponse();

        response.setId(UUID.fromString(String.valueOf(row.get("id"))));
        response.setName(String.valueOf(row.get("name")));
        response.setCategoryType(String.valueOf(row.get("category_type")));
        response.setSystem(Boolean.TRUE.equals(row.get("is_system")));
        response.setActive(Boolean.TRUE.equals(row.get("is_active")));

        return response;
    }
    //endregion

    //region Map transaction response
    private MonthlyTransactionResponse requireTransactionResult(
            List<Map<String, Object>> results
    ) {
        if (results == null || results.isEmpty()) {
            throw new IllegalStateException(
                    "Supabase did not return the saved transaction"
            );
        }

        return mapTransactionResponse(results.get(0));
    }

    private MonthlyTransactionResponse mapTransactionResponse(
            Map<String, Object> row
    ) {
        MonthlyTransactionResponse response =
                new MonthlyTransactionResponse();

        response.setId(UUID.fromString(String.valueOf(row.get("id"))));
        response.setSnapshotId(
                UUID.fromString(String.valueOf(row.get("snapshot_id")))
        );
        response.setCategoryId(
                UUID.fromString(String.valueOf(row.get("category_id")))
        );
        response.setTransactionDate(
                LocalDate.parse(String.valueOf(row.get("transaction_date")))
        );
        response.setDescription(String.valueOf(row.get("description")));
        response.setAmount(toBigDecimal(row.get("amount")));

        Object categoryData = row.get("financial_categories");

        if (categoryData instanceof Map<?, ?> category) {
            Object name = category.get("name");
            Object categoryType = category.get("category_type");

            response.setCategoryName(
                    name == null ? null : String.valueOf(name)
            );
            response.setCategoryType(
                    categoryType == null ? null : String.valueOf(categoryType)
            );
        }

        return response;
    }
    //endregion

    //region Convert numeric values
    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }

        if (value == null) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(String.valueOf(value));
    }
    //endregion
}