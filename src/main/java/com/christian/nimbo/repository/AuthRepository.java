package com.christian.nimbo.repository;

import java.util.Map;

public interface AuthRepository {

    //region Register
    Map<String, Object> register(
            String email,
            String password,
            String name
    );
    //endregion

    //region Login
    Map<String, Object> login(
            String email,
            String password
    );
    //endregion

    //region Validate token
    Map<String, Object> validateToken(
            String accessToken
    );
    //endregion
}