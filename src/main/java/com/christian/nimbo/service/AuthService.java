package com.christian.nimbo.service;

import com.christian.nimbo.repository.AuthRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(
            AuthRepository authRepository) {

        this.authRepository =
                authRepository;
    }

    //region Register
    public Map<String, Object> register(
            String name,
            String email,
            String password) {

        name = normalizeName(name);
        email = normalizeEmail(email);

        validateRegistration(
                name,
                email,
                password
        );

        Map<String, Object> response =
                authRepository.register(
                        email,
                        password,
                        name
                );

        return Map.of(
                "ok",
                true,
                "user",
                response
        );
    }
    //endregion

    //region Login
    public Map<String, Object> login(
            String email,
            String password) {

        email = normalizeEmail(email);

        if (email.isBlank() ||
                password == null ||
                password.isBlank()) {

            throw new IllegalArgumentException(
                    "Email y contraseña son obligatorios."
            );
        }

        Map<String, Object> response =
                authRepository.login(
                        email,
                        password
                );

        return Map.of(
                "ok",
                true,
                "user",
                response.get("user"),
                "access_token",
                response.get("access_token"),
                "refresh_token",
                response.get("refresh_token"),
                "expires_in",
                response.get("expires_in")
        );
    }
    //endregion

    //region Forgot password
    public Map<String, Object> forgotPassword(
            String email) {

        email = normalizeEmail(email);

        if (email.isBlank() ||
                !email.contains("@")) {

            throw new IllegalArgumentException(
                    "Introduce un email válido."
            );
        }

        authRepository.forgotPassword(email);

        return Map.of(
                "ok",
                true
        );
    }
    //endregion

    //region Validate token
    public Map<String, Object> validateToken(
            String accessToken) {

        if (accessToken == null ||
                accessToken.isBlank()) {

            throw new IllegalArgumentException(
                    "Access token is required."
            );
        }

        return authRepository.validateToken(
                accessToken
        );
    }
    //endregion

    //region Validation
    private void validateRegistration(
            String name,
            String email,
            String password) {

        if (name.isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre es obligatorio."
            );
        }

        if (email.isBlank() ||
                !email.contains("@")) {

            throw new IllegalArgumentException(
                    "Introduce un email válido."
            );
        }

        if (password == null ||
                password.length() < 8) {

            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres."
            );
        }
    }
    //endregion

    //region Normalization
    private String normalizeName(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private String normalizeEmail(
            String value) {

        return value == null
                ? ""
                : value.trim().toLowerCase();
    }
    //endregion
}