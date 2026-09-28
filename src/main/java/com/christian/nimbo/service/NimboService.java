package com.christian.nimbo.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class NimboService {

    private final GoogleSheetsService googleSheetsService;

    public NimboService(
            GoogleSheetsService googleSheetsService) {

        this.googleSheetsService =
                googleSheetsService;
    }

    //region Create diagnostic

    public Map<String, Object> createDiagnostic(
            Map<String, Object> data) {

        String id =
                UUID.randomUUID().toString();

        double income =
                toNumber(data.get("income"));

        double expenses =
                toNumber(data.get("expenses"));

        double monthlySaving =
                toNumber(data.get("monthlySaving"));

        double monthlyInvestment =
                toNumber(data.get("monthlyInvestment"));

        double debt =
                toNumber(data.get("debt"));

        double emergencyFund =
                toNumber(data.get("emergencyFund"));

        //region Expenses score

        double expensesScore = 0;

        if (income > 0) {

            expensesScore =
                    ((income - expenses) / income) * 100;
        }

        expensesScore =
                clamp(
                        expensesScore,
                        0,
                        100
                );

        //endregion

        //region Saving score

        double savingScore = 0;

        if (income > 0) {

            savingScore =
                    (monthlySaving / income) * 100 * 5;
        }

        savingScore =
                clamp(
                        savingScore,
                        0,
                        100
                );

        //endregion

        //region Investment score

        double investmentScore = 0;

        if (income > 0) {

            investmentScore =
                    (monthlyInvestment / income) * 100 * 5;
        }

        investmentScore =
                clamp(
                        investmentScore,
                        0,
                        100
                );

        //endregion

        //region Emergency fund score

        double emergencyMonths = 0;

        if (expenses > 0) {

            emergencyMonths =
                    emergencyFund / expenses;
        }

        double emergencyScore =
                (emergencyMonths / 6) * 100;

        emergencyScore =
                clamp(
                        emergencyScore,
                        0,
                        100
                );

        //endregion

        //region Debt score

        double debtScore = 100;

        if (income > 0) {

            debtScore =
                    100 - ((debt / income) * 10);
        }

        debtScore =
                clamp(
                        debtScore,
                        0,
                        100
                );

        //endregion

        //region Total score

        int totalScore =
                (int) Math.round(
                        (
                                expensesScore +
                                        savingScore +
                                        investmentScore +
                                        emergencyScore +
                                        debtScore
                        ) / 5
                );

        //endregion

        //region Scores

        Map<String, Object> scores =
                new HashMap<>();

        scores.put(
                "expenses",
                Math.round(expensesScore)
        );

        scores.put(
                "saving",
                Math.round(savingScore)
        );

        scores.put(
                "investment",
                Math.round(investmentScore)
        );

        scores.put(
                "emergency",
                Math.round(emergencyScore)
        );

        scores.put(
                "debt",
                Math.round(debtScore)
        );

        scores.put(
                "total",
                totalScore
        );

        scores.put(
                "emergencyMonths",
                Math.round(
                        emergencyMonths * 100.0
                ) / 100.0
        );

        //endregion

        //region Save to Google Sheets

        try {

            googleSheetsService.appendDiagnostic(
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

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to save diagnostic to Google Sheets.",
                    e
            );
        }

        //endregion

        return Map.of(
                "ok",
                true,

                "id",
                id
        );
    }

    //endregion

    //region Get diagnostic

    public Map<String, Object> getDiagnostic(
            String id) {

        try {

            Map<String, Object> diagnostic =
                    googleSheetsService.getDiagnostic(id);

            if (diagnostic == null) {

                return Map.of(
                        "ok",
                        false,

                        "error",
                        "Diagnostic not found."
                );
            }

            return Map.of(
                    "ok",
                    true,

                    "data",
                    diagnostic
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to read diagnostic from Google Sheets.",
                    e
            );
        }
    }

    //endregion

    //region Helpers

    private double toNumber(
            Object value) {

        if (value == null) {
            return 0;
        }

        if (value instanceof Number) {

            return ((Number) value)
                    .doubleValue();
        }

        try {

            return Double.parseDouble(
                    value.toString()
            );

        } catch (NumberFormatException e) {

            return 0;
        }
    }


    private double clamp(
            double value,
            double min,
            double max) {

        return Math.max(
                min,
                Math.min(
                        max,
                        value
                )
        );
    }

    //endregion
}
