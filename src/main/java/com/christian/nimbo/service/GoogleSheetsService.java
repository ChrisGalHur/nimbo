package com.christian.nimbo.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoogleSheetsService {

    private static final String SPREADSHEET_ID =
            "1Hpur_sOfJNMPasO2xDMw-g4sfALvN__zaFh76Le-8XY";

    private static final String SHEET_NAME =
            "Respuestas";

    private final Sheets sheets;

    public GoogleSheetsService()
            throws IOException, GeneralSecurityException {

        GoogleCredentials credentials =
                GoogleCredentials
                        .getApplicationDefault()
                        .createScoped(
                                List.of(
                                        SheetsScopes.SPREADSHEETS
                                )
                        );

        sheets =
                new Sheets.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        GsonFactory.getDefaultInstance(),
                        new HttpCredentialsAdapter(credentials)
                )
                        .setApplicationName("Nimbo")
                        .build();
    }

    //region Read

    public String readSheet() throws IOException {

        String range =
                SHEET_NAME + "!A1:N5";

        ValueRange response =
                sheets
                        .spreadsheets()
                        .values()
                        .get(
                                SPREADSHEET_ID,
                                range
                        )
                        .execute();

        if (response.getValues() == null) {
            return "[]";
        }

        return response
                .getValues()
                .toString();
    }

    //endregion

    //region Write

    public void appendDiagnostic(
            String id,
            double income,
            double expenses,
            double monthlySaving,
            double monthlyInvestment,
            double debt,
            double emergencyFund,
            double expensesScore,
            double savingScore,
            double investmentScore,
            double emergencyScore,
            double debtScore,
            int totalScore
    ) throws IOException {

        String range =
                SHEET_NAME + "!A:N";

        List<Object> row =
                List.of(
                        OffsetDateTime.now().toString(),
                        id,
                        income,
                        expenses,
                        monthlySaving,
                        monthlyInvestment,
                        debt,
                        emergencyFund,
                        expensesScore,
                        savingScore,
                        investmentScore,
                        emergencyScore,
                        debtScore,
                        totalScore
                );

        ValueRange body =
                new ValueRange()
                        .setValues(
                                List.of(row)
                        );

        sheets
                .spreadsheets()
                .values()
                .append(
                        SPREADSHEET_ID,
                        range,
                        body
                )
                .setValueInputOption("RAW")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
    }

    //endregion

    //region Get diagnostic

    public Map<String, Object> getDiagnostic(
            String id) throws IOException {

        String range =
                SHEET_NAME + "!A2:N";

        ValueRange response =
                sheets
                        .spreadsheets()
                        .values()
                        .get(
                                SPREADSHEET_ID,
                                range
                        )
                        .execute();

        List<List<Object>> rows =
                response.getValues();

        if (rows == null) {
            return null;
        }

        for (List<Object> row : rows) {

            if (row.size() < 14) {
                continue;
            }

            String rowId =
                    String.valueOf(row.get(1));

            if (!rowId.equals(id)) {
                continue;
            }

            Map<String, Object> scores =
                    new HashMap<>();

            scores.put(
                    "expenses",
                    toNumber(row.get(8))
            );

            scores.put(
                    "saving",
                    toNumber(row.get(9))
            );

            scores.put(
                    "investment",
                    toNumber(row.get(10))
            );

            scores.put(
                    "emergency",
                    toNumber(row.get(11))
            );

            scores.put(
                    "debt",
                    toNumber(row.get(12))
            );

            scores.put(
                    "total",
                    toNumber(row.get(13))
            );

            Map<String, Object> diagnostic =
                    new HashMap<>();

            diagnostic.put(
                    "id",
                    rowId
            );

            diagnostic.put(
                    "date",
                    row.get(0)
            );

            diagnostic.put(
                    "income",
                    toNumber(row.get(2))
            );

            diagnostic.put(
                    "expenses",
                    toNumber(row.get(3))
            );

            diagnostic.put(
                    "monthlySaving",
                    toNumber(row.get(4))
            );

            diagnostic.put(
                    "monthlyInvestment",
                    toNumber(row.get(5))
            );

            diagnostic.put(
                    "debt",
                    toNumber(row.get(6))
            );

            diagnostic.put(
                    "emergencyFund",
                    toNumber(row.get(7))
            );

            diagnostic.put(
                    "scores",
                    scores
            );

            return diagnostic;
        }

        return null;
    }

    //endregion

    //region Helpers

    private double toNumber(Object value) {

        if (value == null) {
            return 0;
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        try {
            return Double.parseDouble(
                    value.toString()
            );

        } catch (NumberFormatException e) {
            return 0;
        }
    }

    //endregion
}
