package com.snik.loftmoney.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BalanceResult {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("total_expenses")
    @Expose
    private int totalExpenses;
    @SerializedName("total_income")
    @Expose
    private int totalIncomes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(int totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public int getTotalIncomes() {
        return totalIncomes;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncomes = totalIncome;
    }
}
