package com.example.calculator;

public class CalculationHistoryItem {
    private String solution;
    private String result;

    public CalculationHistoryItem() {}
    public CalculationHistoryItem(String solution,String result) {
        this.solution = solution;
        this.result = result;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String expression) {
        this.solution = expression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
