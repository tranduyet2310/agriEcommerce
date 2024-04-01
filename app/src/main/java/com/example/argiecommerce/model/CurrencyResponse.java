package com.example.argiecommerce.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class CurrencyResponse {
    @SerializedName("base_currency_code")
    private String currencyCode;
    @SerializedName("base_currency_name")
    private String currencyName;
    @SerializedName("amount")
    private Double amount;
    @SerializedName("updated_date")
    private String updatedDate;
    @SerializedName("rates")
    private CurrencyRate rates;
    @SerializedName("status")
    private Boolean status;

    public CurrencyResponse() {
    }

    public CurrencyResponse(String currencyCode, String currencyName, Double amount, String updatedDate, CurrencyRate rates, Boolean status) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.amount = amount;
        this.updatedDate = updatedDate;
        this.rates = rates;
        this.status = status;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public CurrencyRate getRates() {
        return rates;
    }

    public void setRates(CurrencyRate rates) {
        this.rates = rates;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
