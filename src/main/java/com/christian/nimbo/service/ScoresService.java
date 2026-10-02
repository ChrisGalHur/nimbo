package com.christian.nimbo.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScoresService {

    //region Calculate

    public Map<String, Object> calculate(
            double income,
            double expenses,
            double monthlyInvestment,
            double debt,
            double emergencyFund) {

        //region Expenses

        double expensesRate = 0;

        if (income > 0) {

            expensesRate =
                    (expenses / income) * 100;
        }


        double expensesScore;


        if (expensesRate <= 50) {

            expensesScore = 100;

        } else if (expensesRate <= 60) {

            expensesScore = 85;

        } else if (expensesRate <= 70) {

            expensesScore = 60;

        } else if (expensesRate <= 80) {

            expensesScore = 35;

        } else {

            expensesScore = 10;
        }

        //endregion


        //region Emergency fund

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


        //region Investment

        double investmentRate = 0;

        if (income > 0) {

            investmentRate =
                    (monthlyInvestment / income) * 100;
        }


        double investmentBaseScore;


        if (investmentRate >= 10) {

            investmentBaseScore = 100;

        } else if (investmentRate >= 7) {

            investmentBaseScore = 85;

        } else if (investmentRate >= 5) {

            investmentBaseScore = 70;

        } else if (investmentRate >= 3) {

            investmentBaseScore = 45;

        } else if (investmentRate >= 1) {

            investmentBaseScore = 25;

        } else {

            investmentBaseScore = 5;
        }


        double emergencyFactor =
                0.40 +
                        (Math.min(emergencyMonths, 6) / 6) * 0.60;


        double investmentScore =
                investmentBaseScore *
                        emergencyFactor;


        investmentScore =
                clamp(
                        investmentScore,
                        0,
                        100
                );

        //endregion


        //region Debt

        double debtRateAnnual = 0;

        if (income > 0) {

            debtRateAnnual =
                    (
                            debt /
                                    (income * 12)
                    ) * 100;
        }


        double debtScore;


        if (debt <= 0) {

            debtScore = 100;

        } else if (debtRateAnnual <= 20) {

            debtScore = 50;

        } else if (debtRateAnnual <= 40) {

            debtScore = 35;

        } else if (debtRateAnnual <= 70) {

            debtScore = 15;

        } else {

            debtScore = 0;
        }

        //endregion


        //region Total

        double totalScoreBase =

                (expensesScore * 0.40) +

                        (emergencyScore * 0.30) +

                        (investmentScore * 0.15) +

                        (debtScore * 0.15);


        double penalties = 0;


        if (emergencyMonths < 1) {

            penalties += 15;
        }


        if (
                emergencyMonths < 2 &&
                        investmentBaseScore >= 70
        ) {

            penalties += 10;
        }


        if (expensesScore < 40) {

            penalties += 10;
        }


        if (debtRateAnnual > 50) {

            penalties += 10;
        }


        double totalScore =
                totalScoreBase - penalties;


        totalScore =
                clamp(
                        totalScore,
                        0,
                        100
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
                "emergency",
                Math.round(emergencyScore)
        );


        scores.put(
                "investment",
                Math.round(investmentScore)
        );


        scores.put(
                "debt",
                Math.round(debtScore)
        );


        scores.put(
                "total",
                Math.round(totalScore)
        );


        scores.put(
                "expensesRate",
                Math.round(
                        expensesRate * 100
                ) / 100.0
        );


        scores.put(
                "emergencyMonths",
                Math.round(
                        emergencyMonths * 100
                ) / 100.0
        );


        scores.put(
                "investmentRate",
                Math.round(
                        investmentRate * 100
                ) / 100.0
        );


        scores.put(
                "investmentBase",
                Math.round(
                        investmentBaseScore
                )
        );


        scores.put(
                "emergencyFactor",
                Math.round(
                        emergencyFactor * 100
                ) / 100.0
        );


        scores.put(
                "debtRateAnnual",
                Math.round(
                        debtRateAnnual * 100
                ) / 100.0
        );


        scores.put(
                "penalties",
                Math.round(
                        penalties
                )
        );

        //endregion


        return scores;
    }

    //endregion


    //region Compatibility

    public Map<String, Object> calculate(
            double income,
            double expenses,
            double monthlySaving,
            double monthlyInvestment,
            double debt,
            double emergencyFund) {

        Map<String, Object> scores =
                calculate(
                        income,
                        expenses,
                        monthlyInvestment,
                        debt,
                        emergencyFund
                );


        // Compatibilidad temporal con
        // el código anterior.
        scores.put(
                "saving",
                0
        );


        return scores;
    }

    //endregion


    //region Helpers

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