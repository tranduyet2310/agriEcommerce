package com.example.argiecommerce.model;

import com.google.gson.annotations.SerializedName;

public class MessageResponse {
    @SerializedName("successful")
    private boolean isSuccessful;
    private String message;

    public MessageResponse() {
    }

    public MessageResponse(boolean isSuccessful, String message) {
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
