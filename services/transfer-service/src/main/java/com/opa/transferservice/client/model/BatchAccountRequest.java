package com.opa.transferservice.client.model;

import java.util.List;

public class BatchAccountRequest {
    private List<String> accountNumbers;

    public BatchAccountRequest() {
    }

    public BatchAccountRequest(List<String> accountNumbers) {
        this.accountNumbers = accountNumbers;
    }

    public List<String> getAccountNumbers() {
        return accountNumbers;
    }

    public void setAccountNumbers(List<String> accountNumbers) {
        this.accountNumbers = accountNumbers;
    }
}
