package com.snik.loftmoney;


import com.google.gson.annotations.SerializedName;

class AuthResult {
    public String status;
    public int id;
    @SerializedName("auth_token")
    public String token;

}
