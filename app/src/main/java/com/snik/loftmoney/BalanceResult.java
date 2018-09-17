package com.snik.loftmoney;

import com.google.gson.annotations.SerializedName;

class BalanceResult {

    public String status;
    @SerializedName("total_expenses")
    public int expense;
    @SerializedName("total_income")
    public int income;
}
