package com.christian.nimbo.service;

import com.christian.nimbo.repository.NimboRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class NimboService {

    private final NimboRepository nimboRepository;
    private final ScoresService scoresService;
    private final DiagnosisService diagnosisService;

    public NimboService(
            NimboRepository nimboRepository,
            ScoresService scoresService,
            DiagnosisService diagnosisService) {

        this.nimboRepository =
                nimboRepository;

        this.scoresService =
                scoresService;

        this.diagnosisService =
                diagnosisService;
    }

    //region Create diagnostic

    public Map<String, Object> createDiagnostic(
            String userId,
            Map<String, Object> data) {

        String id =
                UUID.randomUUID().toString();

        //region Get input data

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

        //region Calculate scores

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

        //region Save public test snapshot

        Map<String, Object> snapshot =
                Map.of(
                        "id",
                        id,

                        "income",
                        income,

                        "expenses",
                        expenses,

                        "monthly_saving",
                        monthlySaving,

                        "monthly_investment",
                        monthlyInvestment,

                        "debt",
                        debt,

                        "emergency_fund",
                        emergencyFund
                );

        nimboRepository.savePublicTestSnapshot(
                snapshot
        );

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

        Map<String, Object> diagnostic =
                nimboRepository.findPublicTestSnapshotById(
                        id
                );

        if (diagnostic == null) {

            return Map.of(
                    "ok",
                    false,

                    "error",
                    "Diagnostic not found."
            );
        }

        double income =
                toNumber(
                        diagnostic.get("income")
                );

        double expenses =
                toNumber(
                        diagnostic.get("expenses")
                );

        double monthlyInvestment =
                toNumber(
                        diagnostic.get("monthly_investment")
                );

        double debt =
                toNumber(
                        diagnostic.get("debt")
                );

        double emergencyFund =
                toNumber(
                        diagnostic.get("emergency_fund")
                );

        //region Calculate scores

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
    }

    //endregion

    //region Helpers

    private double toNumber(
            Object value) {

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