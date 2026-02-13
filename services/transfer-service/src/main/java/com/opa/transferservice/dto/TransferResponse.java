package com.opa.transferservice.dto;

public class TransferResponse {

    private String status;
    private String transactionId;
    private String username;
    private double amount;
    private String message;

    public TransferResponse() {}

    public TransferResponse(String status,
                            String transactionId,
                            String username,
                            double amount,
                            String message) {
        this.status = status;
        this.transactionId = transactionId;
        this.username = username;
        this.amount = amount;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
