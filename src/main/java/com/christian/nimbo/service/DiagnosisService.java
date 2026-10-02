package com.christian.nimbo.service;

import com.christian.nimbo.constants.DiagnosisMessages;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DiagnosisService {

    //region Diagnose

    public Map<String, Object> diagnose(
            Map<String, Object> scores) {

        double totalScore =
                toNumber(
                        scores.get("total")
                );


        double expensesRate =
                toNumber(
                        scores.get("expensesRate")
                );


        double emergencyMonths =
                toNumber(
                        scores.get("emergencyMonths")
                );


        double investmentRate =
                toNumber(
                        scores.get("investmentRate")
                );


        double debtRateAnnual =
                toNumber(
                        scores.get("debtRateAnnual")
                );


        Map<String, Object> diagnosis =
                new HashMap<>();

        //region General status

        String status;

        if (totalScore >= 75) {

            status = "positive";

        } else if (totalScore >= 50) {

            status = "neutral";

        } else {

            status = "negative";
        }


        diagnosis.put(
                "status",
                status
        );

        //endregion

        //region Expenses

        String expensesLevel;


        if (expensesRate <= 50) {

            expensesLevel = "very_good";

        } else if (expensesRate <= 60) {

            expensesLevel = "good";

        } else if (expensesRate <= 70) {

            expensesLevel = "moderate";

        } else if (expensesRate <= 80) {

            expensesLevel = "high";

        } else {

            expensesLevel = "very_high";
        }


        diagnosis.put(
                "expensesLevel",
                expensesLevel
        );

        //endregion

        //region Emergency fund

        String emergencyLevel;


        if (emergencyMonths <= 0) {

            emergencyLevel = "none";

        } else if (emergencyMonths < 2) {

            emergencyLevel = "under_two_months";

        } else if (emergencyMonths < 3) {

            emergencyLevel = "two_to_three_months";

        } else if (emergencyMonths < 4) {

            emergencyLevel = "three_to_four_months";

        } else if (emergencyMonths < 6) {

            emergencyLevel = "four_to_six_months";

        } else {

            emergencyLevel = "six_plus_months";
        }


        diagnosis.put(
                "emergencyLevel",
                emergencyLevel
        );

    //endregion

        //region Investment

        String investmentLevel;


        if (investmentRate < 1) {

            investmentLevel = "very_low";

        } else if (investmentRate < 3) {

            investmentLevel = "low";

        } else if (investmentRate < 5) {

            investmentLevel = "moderate";

        } else if (investmentRate < 7) {

            investmentLevel = "good";

        } else if (investmentRate < 10) {

            investmentLevel = "high";

        } else {

            investmentLevel = "very_high";
        }


        diagnosis.put(
                "investmentLevel",
                investmentLevel
        );


        String investmentFoundation;


        if (emergencyMonths < 1) {

            investmentFoundation = "none";

        } else if (emergencyMonths < 3) {

            investmentFoundation = "building";

        } else if (emergencyMonths < 6) {

            investmentFoundation = "partial";

        } else {

            investmentFoundation = "complete";
        }


        diagnosis.put(
                "investmentFoundation",
                investmentFoundation
        );

        //endregion

        //region Debt

        String debtLevel;


        if (debtRateAnnual <= 0) {

            debtLevel = "none";

        } else if (debtRateAnnual <= 20) {

            debtLevel = "low";

        } else if (debtRateAnnual <= 40) {

            debtLevel = "moderate";

        } else if (debtRateAnnual <= 70) {

            debtLevel = "high";

        } else {

            debtLevel = "very_high";
        }


        diagnosis.put(
                "debtLevel",
                debtLevel
        );

        //endregion

        diagnosis.put(
                "expensesMessage",
                getExpensesMessage(
                        expensesLevel,
                        emergencyLevel
                )
        );

        diagnosis.put(
                "emergencyMessage",
                getEmergencyMessage(emergencyLevel)
        );

        diagnosis.put(
                "investmentMessage",
                getInvestmentMessage(
                        investmentLevel,
                        investmentFoundation
                )
        );

        diagnosis.put(
                "debtMessage",
                getDebtMessage(
                        debtLevel,
                        emergencyLevel,
                        investmentLevel
                )
        );

        return diagnosis;
    }

    //endregion

    //region Message selection

    private String getExpensesMessage(
            String expensesLevel,
            String emergencyLevel) {

        if (
                (
                        expensesLevel.equals("high") ||
                                expensesLevel.equals("very_high")
                ) &&
                        emergencyLevel.equals("none")
        ) {

            return DiagnosisMessages.HIGH_EXPENSES_WITH_NO_EMERGENCY;
        }


        if (
                (
                        expensesLevel.equals("high") ||
                                expensesLevel.equals("very_high")
                ) &&
                        !emergencyLevel.equals("none")
        ) {

            return DiagnosisMessages.HIGH_EXPENSES_WITH_EMERGENCY;
        }


        if (
                expensesLevel.equals("very_good") &&
                        emergencyLevel.equals("none")
        ) {

            return DiagnosisMessages.LOW_EXPENSES_WITHOUT_EMERGENCY;
        }


        if (
                expensesLevel.equals("very_good") &&
                        emergencyLevel.equals("six_plus_months")
        ) {

            return DiagnosisMessages.LOW_EXPENSES_WITH_FULL_EMERGENCY;
        }


        return switch (expensesLevel) {

            case "very_good" ->
                    DiagnosisMessages.EXPENSES_EXCELLENT;

            case "good" ->
                    DiagnosisMessages.EXPENSES_GOOD;

            case "moderate" ->
                    DiagnosisMessages.EXPENSES_MODERATE;

            case "high" ->
                    DiagnosisMessages.EXPENSES_HIGH;

            case "very_high" ->
                    DiagnosisMessages.EXPENSES_VERY_HIGH;

            default ->
                    "";
        };
    }


    private String getEmergencyMessage(
            String emergencyLevel) {

        return switch (emergencyLevel) {

            case "none" ->
                    DiagnosisMessages.EMERGENCY_ZERO;

            case "under_two_months" ->
                    DiagnosisMessages.EMERGENCY_UNDER_TWO_MONTHS;

            case "two_to_three_months" ->
                    DiagnosisMessages.EMERGENCY_TWO_TO_THREE_MONTHS;

            case "three_to_four_months" ->
                    DiagnosisMessages.EMERGENCY_THREE_TO_FOUR_MONTHS;

            case "four_to_six_months" ->
                    DiagnosisMessages.EMERGENCY_FOUR_TO_SIX_MONTHS;

            case "six_plus_months" ->
                    DiagnosisMessages.EMERGENCY_SIX_PLUS_MONTHS;

            default ->
                    "";
        };
    }


    private String getInvestmentMessage(
            String investmentLevel,
            String investmentFoundation) {

        if (
                investmentFoundation.equals("none") &&
                        (
                                investmentLevel.equals("moderate") ||
                                        investmentLevel.equals("good") ||
                                        investmentLevel.equals("high") ||
                                        investmentLevel.equals("very_high")
                        )
        ) {

            return DiagnosisMessages.NO_EMERGENCY_WITH_INVESTMENT;
        }


        if (
                investmentFoundation.equals("building") &&
                        (
                                investmentLevel.equals("high") ||
                                        investmentLevel.equals("very_high")
                        )
        ) {

            return DiagnosisMessages.LOW_EMERGENCY_WITH_HIGH_INVESTMENT;
        }


        if (
                investmentFoundation.equals("partial") &&
                        (
                                investmentLevel.equals("moderate") ||
                                        investmentLevel.equals("good") ||
                                        investmentLevel.equals("high") ||
                                        investmentLevel.equals("very_high")
                        )
        ) {

            return DiagnosisMessages.THREE_MONTHS_WITH_INVESTMENT;
        }

        if (
                investmentFoundation.equals("complete") &&
                        (
                                investmentLevel.equals("very_low") ||
                                        investmentLevel.equals("low")
                        )
        ) {

            return DiagnosisMessages.SIX_MONTHS_WITHOUT_INVESTMENT;
        }

        if (
                investmentFoundation.equals("complete") &&
                        !investmentLevel.equals("very_low") &&
                        !investmentLevel.equals("low")
        ) {

            return DiagnosisMessages.SIX_MONTHS_WITH_INVESTMENT;
        }


        return switch (investmentLevel) {

            case "very_low" ->
                    DiagnosisMessages.INVESTMENT_NONE;

            case "low" ->
                    DiagnosisMessages.INVESTMENT_LOW;

            case "moderate" ->
                    DiagnosisMessages.INVESTMENT_MODERATE_LOW;

            case "good" ->
                    DiagnosisMessages.INVESTMENT_MODERATE;

            case "high" ->
                    DiagnosisMessages.INVESTMENT_HIGH;

            case "very_high" ->
                    DiagnosisMessages.INVESTMENT_VERY_HIGH;

            default ->
                    "";
        };
    }


    private String getDebtMessage(
            String debtLevel,
            String emergencyLevel,
            String investmentLevel) {

        if (
                debtLevel.equals("none") &&
                        emergencyLevel.equals("six_plus_months")
        ) {

            return DiagnosisMessages.NO_DEBT_WITH_FULL_EMERGENCY;
        }


        if (
                debtLevel.equals("none") &&
                        (
                                investmentLevel.equals("moderate") ||
                                        investmentLevel.equals("good") ||
                                        investmentLevel.equals("high") ||
                                        investmentLevel.equals("very_high")
                        )
        ) {

            return DiagnosisMessages.NO_DEBT_WITH_INVESTMENT;
        }


        if (
                (
                        debtLevel.equals("high") ||
                                debtLevel.equals("very_high")
                ) &&
                        emergencyLevel.equals("none")
        ) {

            return DiagnosisMessages.HIGH_DEBT_WITHOUT_EMERGENCY;
        }


        if (
                (
                        debtLevel.equals("high") ||
                                debtLevel.equals("very_high")
                ) &&
                        (
                                investmentLevel.equals("moderate") ||
                                        investmentLevel.equals("good") ||
                                        investmentLevel.equals("high") ||
                                        investmentLevel.equals("very_high")
                        )
        ) {

            return DiagnosisMessages.HIGH_DEBT_WITH_INVESTMENT;
        }


        return switch (debtLevel) {

            case "none" ->
                    DiagnosisMessages.DEBT_NONE;

            case "low" ->
                    DiagnosisMessages.DEBT_LOW;

            case "moderate" ->
                    DiagnosisMessages.DEBT_MODERATE;

            case "high" ->
                    DiagnosisMessages.DEBT_HIGH;

            case "very_high" ->
                    DiagnosisMessages.DEBT_VERY_HIGH;

            default ->
                    "";
        };
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