package com.christian.nimbo.service;

import com.christian.nimbo.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final GoogleSheetsService googleSheetsService;
    private final PasswordService passwordService;

    public AuthService(
            GoogleSheetsService googleSheetsService,
            PasswordService passwordService) {

        this.googleSheetsService =
                googleSheetsService;

        this.passwordService =
                passwordService;
    }

    //region Register
    public Map<String, Object> register(
            String name,
            String email,
            String password) throws IOException {

        name = normalizeName(name);
        email = normalizeEmail(email);

        validateRegistration(
                name,
                email,
                password
        );

        User existingUser =
                googleSheetsService
                        .findUserByEmail(email);

        if (existingUser != null) {

            throw new IllegalArgumentException(
                    "Ya existe una cuenta con ese email."
            );
        }

        User user =
                new User(
                        UUID.randomUUID().toString(),
                        name,
                        email,
                        passwordService.hash(password),
                        "BASIC"
                );

        googleSheetsService.appendUser(user);

        return Map.of(
                "ok",
                true,
                "user",
                user.toPublicData()
        );
    }
    //endregion

    //region Login
    public Map<String, Object> login(
            String email,
            String password) throws IOException {

        email = normalizeEmail(email);

        if (email.isBlank() ||
                password == null ||
                password.isBlank()) {

            throw new IllegalArgumentException(
                    "Email y contraseña son obligatorios."
            );
        }

        User user =
                googleSheetsService
                        .findUserByEmail(email);

        if (user == null ||
                !passwordService.matches(
                        password,
                        user.passwordHash()
                )) {

            throw new IllegalArgumentException(
                    "Email o contraseña incorrectos."
            );
        }

        return Map.of(
                "ok",
                true,
                "user",
                user.toPublicData()
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