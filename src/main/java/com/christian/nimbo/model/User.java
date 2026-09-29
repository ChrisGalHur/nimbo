package com.christian.nimbo.model;

/**
 * @author chrisgalhur
 */
import java.util.Map;

public record User(
        String id,
        String name,
        String email,
        String passwordHash,
        String plan
) {

    //region Public data
    public Map<String, Object> toPublicData() {

        return Map.of(
                "id", id,
                "name", name,
                "email", email,
                "plan", plan
        );
    }
    //endregion
}