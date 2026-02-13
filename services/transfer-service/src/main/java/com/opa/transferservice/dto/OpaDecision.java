package com.opa.transferservice.dto;

public class OpaDecision {

    private boolean allow;
    private OpaError error;

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public OpaError getError() {
        return error;
    }

    public void setError(OpaError error) {
        this.error = error;
    }
}
