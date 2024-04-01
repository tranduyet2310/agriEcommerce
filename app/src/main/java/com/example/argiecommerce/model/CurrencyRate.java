package com.example.argiecommerce.model;

import com.google.gson.annotations.SerializedName;

public class CurrencyRate {
    @SerializedName("USD")
    private CurrencyRateDetail usd;

    public CurrencyRate() {
    }

    public CurrencyRate(CurrencyRateDetail usd) {
        this.usd = usd;
    }

    public CurrencyRateDetail getUsd() {
        return usd;
    }

    public void setUsd(CurrencyRateDetail usd) {
        this.usd = usd;
    }

    public static class CurrencyRateDetail{
        @SerializedName("currency_name")
        private String currencyName;
        @SerializedName("rate")
        private Double rate;
        @SerializedName("rate_for_amount")
        private Double result;

        public CurrencyRateDetail() {
        }

        public CurrencyRateDetail(String currencyName, Double rate, Double result) {
            this.currencyName = currencyName;
            this.rate = rate;
            this.result = result;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName;
        }

        public Double getRate() {
            return rate;
        }

        public void setRate(Double rate) {
            this.rate = rate;
        }

        public Double getResult() {
            return result;
        }

        public void setResult(Double result) {
            this.result = result;
        }
    }
}


