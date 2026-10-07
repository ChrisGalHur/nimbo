package com.christian.nimbo.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * @author chrisgalhur
 */
@Repository
public class SupabaseAuthRepository implements AuthRepository {

    private final RestClient restClient;

    public SupabaseAuthRepository(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.key}") String supabaseKey) {

        this.restClient =
                RestClient.builder()
                        .baseUrl(supabaseUrl)
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

    //region Register
    @Override
    public Map<String, Object> register(
            String email,
            String password,
            String name) {

        return restClient
                .post()
                .uri("/auth/v1/admin/users")
                .body(
                        Map.of(
                                "email",
                                email,
                                "password",
                                password,
                                "email_confirm",
                                true,
                                "user_metadata",
                                Map.of(
                                        "name",
                                        name
                                )
                        )
                )
                .retrieve()
                .body(Map.class);
    }
    //endregion

    //region Login
    @Override
    public Map<String, Object> login(
            String email,
            String password) {

        return restClient
                .post()
                .uri("/auth/v1/token?grant_type=password")
                .body(
                        Map.of(
                                "email",
                                email,
                                "password",
                                password
                        )
                )
                .retrieve()
                .body(Map.class);
    }
    //endregion

    //region Validate token
    @Override
    public Map<String, Object> validateToken(
            String accessToken) {

        return restClient
                .get()
                .uri("/auth/v1/user")
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                )
                .retrieve()
                .body(Map.class);
    }
    //endregion
}