package com.christian.nimbo.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Repository
public class SupabaseNimboRepository implements NimboRepository {

    private final RestClient restClient;

    public SupabaseNimboRepository(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.key}") String supabaseKey) {

        this.restClient =
                RestClient.builder()
                        .baseUrl(
                                supabaseUrl +
                                        "/rest/v1"
                        )
                        .defaultHeader(
                                HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_JSON_VALUE
                        )
                        .defaultHeader(
                                "apikey",
                                supabaseKey
                        )
                        .build();
    }

    //region Save public test snapshot

    @Override
    public void savePublicTestSnapshot(
            Map<String, Object> snapshot) {

        restClient
                .post()
                .uri("/public_test_snapshots")
                .body(snapshot)
                .retrieve()
                .toBodilessEntity();
    }

    //endregion

    //region Find public test snapshot

    @Override
    public Map<String, Object> findPublicTestSnapshotById(
            String id) {

        List<Map<String, Object>> results =
                restClient
                        .get()
                        .uri(
                                "/public_test_snapshots" +
                                        "?id=eq." + id +
                                        "&select=*"
                        )
                        .retrieve()
                        .body(List.class);

        if (results == null || results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }

    //endregion
}