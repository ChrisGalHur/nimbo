package com.christian.nimbo.controller;

import com.christian.nimbo.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService) {

        this.authService =
                authService;
    }

    //region Register

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody Map<String, String> data) {

        try {

            return ResponseEntity.ok(
                    authService.register(
                            data.get("name"),
                            data.get("email"),
                            data.get("password")
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "ok",
                                    false,
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }
    //endregion

    //region Forgot password
    @PostMapping("/forgot-password")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> forgotPassword(
            @RequestBody Map<String, String> data) {

        try {

            return ResponseEntity.ok(
                    authService.forgotPassword(
                            data.get("email")
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "ok",
                                    false,
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }
    //endregion

    //region Login
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> data) {

        try {

            return ResponseEntity.ok(
                    authService.login(
                            data.get("email"),
                            data.get("password")
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "ok",
                                    false,
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }
    //endregion
}