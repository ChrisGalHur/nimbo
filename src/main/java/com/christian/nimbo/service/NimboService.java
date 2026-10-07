package com.christian.nimbo.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class NimboService {

    private final GoogleSheetsService googleSheetsService;
    private final ScoresService scoresService;
    private final DiagnosisService diagnosisService;

    public NimboService(
            GoogleSheetsService googleSheetsService,
            ScoresService scoresService, DiagnosisService diagnosisService) {

        this.googleSheetsService =
                googleSheetsService;

        this.scoresService =
                scoresService;
        this.diagnosisService = diagnosisService;
    }

    //region Create diagnostic
    public Map<String, Object> createDiagnostic(
            String userId,
            Map<String, Object> data) {

        String id =
                UUID.randomUUID().toString();


        //region Obtener datos

        double income =
                toNumber(
                        data.get("income")
                );


        double expenses =
                toNumber(
                        data.get("expenses")
                );


        double monthlySaving =
                toNumber(
                        data.get("monthlySaving")
                );


        double monthlyInvestment =
                toNumber(
                        data.get("monthlyInvestment")
                );


        double debt =
                toNumber(
                        data.get("debt")
                );


        double emergencyFund =
                toNumber(
                        data.get("emergencyFund")
                );

        //endregion


        //region Calcular scores

        Map<String, Object> scores =
                scoresService.calculate(
                        income,
                        expenses,
                        monthlyInvestment,
                        debt,
                        emergencyFund
                );

        Map<String, Object> diagnosis =
                diagnosisService.diagnose(
                        scores
                );

        //endregion


        //region Guardar

        try {

            googleSheetsService.appendDiagnostic(
                    id,
                    income,
                    expenses,
                    monthlySaving,
                    monthlyInvestment,
                    debt,
                    emergencyFund,
                    ((Number) scores.get("expenses"))
                            .doubleValue(),
                    monthlySaving,
                    ((Number) scores.get("investment"))
                            .doubleValue(),
                    ((Number) scores.get("emergency"))
                            .doubleValue(),
                    ((Number) scores.get("debt"))
                            .doubleValue(),
                    ((Number) scores.get("total"))
                            .intValue()
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
                id,

                "scores",
                scores,

                "diagnosis",
                diagnosis
        );
    }
    //endregion

    //region Get diagnostic
    public Map<String, Object> getDiagnostic(
            String userId,
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


            double income =
                    ((Number) diagnostic.get("income"))
                            .doubleValue();


            double expenses =
                    ((Number) diagnostic.get("expenses"))
                            .doubleValue();


            double monthlyInvestment =
                    ((Number) diagnostic.get("monthlyInvestment"))
                            .doubleValue();


            double debt =
                    ((Number) diagnostic.get("debt"))
                            .doubleValue();


            double emergencyFund =
                    ((Number) diagnostic.get("emergencyFund"))
                            .doubleValue();


            Map<String, Object> scores =
                    scoresService.calculate(
                            income,
                            expenses,
                            monthlyInvestment,
                            debt,
                            emergencyFund
                    );

            Map<String, Object> diagnosis =
                    diagnosisService.diagnose(
                            scores
                    );


            diagnostic.put(
                    "scores",
                    scores
            );

            diagnostic.put(
                    "diagnosis",
                    diagnosis
            );

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

    //endregion
}