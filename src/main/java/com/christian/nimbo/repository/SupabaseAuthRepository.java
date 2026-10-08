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

    private final String redirectUrl;

    public SupabaseAuthRepository(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.key}") String supabaseKey,
            @Value("${supabase.redirect-url}") String redirectUrl) {

        this.redirectUrl =
                redirectUrl;

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

        try {

            return restClient
                    .post()
                    .uri("/auth/v1/signup")
                    .body(
                            Map.of(
                                    "email",
                                    email,

                                    "password",
                                    password,

                                    "data",
                                    Map.of(
                                            "name",
                                            name
                                    ),

                                    "options",
                                    Map.of(
                                            "email_redirect_to",
                                            redirectUrl
                                    )
                            )
                    )
                    .retrieve()
                    .body(Map.class);

        } catch (org.springframework.web.client.RestClientResponseException e) {

            String responseBody =
                    e.getResponseBodyAsString();

            if (responseBody != null &&
                    responseBody.toLowerCase()
                            .contains("already registered")) {

                throw new IllegalArgumentException(
                        "Este email ya está registrado."
                );
            }

            if (responseBody != null &&
                    responseBody.toLowerCase()
                            .contains("invalid email")) {

                throw new IllegalArgumentException(
                        "El email introducido no es válido."
                );
            }

            if (responseBody != null &&
                    responseBody.toLowerCase()
                            .contains("password")) {

                throw new IllegalArgumentException(
                        "La contraseña no cumple los requisitos."
                );
            }

            throw new IllegalArgumentException(
                    "No se ha podido crear la cuenta. Inténtalo de nuevo."
            );
        }
    }
    //endregion

    //region Login
    @Override
    public Map<String, Object> login(
            String email,
            String password) {

        try {

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

        } catch (org.springframework.web.client.RestClientResponseException e) {

            String responseBody =
                    e.getResponseBodyAsString();

            if (responseBody != null &&
                    responseBody.toLowerCase()
                            .contains("email not confirmed")) {

                throw new IllegalArgumentException(
                        "Debes confirmar tu email antes de iniciar sesión."
                );
            }

            if (responseBody != null &&
                    responseBody.toLowerCase()
                            .contains("invalid login credentials")) {

                throw new IllegalArgumentException(
                        "El email o la contraseña no son correctos."
                );
            }

            throw new IllegalArgumentException(
                    "No se ha podido iniciar sesión. Inténtalo de nuevo."
            );
        }
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