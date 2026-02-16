package com.opa.transferservice.dto.opa;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class OpaDecision {

    private boolean allow;
    private OpaError error;
    
    @JsonProperty("fee_multiplier")
    private BigDecimal feeMultiplier;

    @JsonProperty("approval_required")
    private Boolean approvalRequired;

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

    public BigDecimal getFeeMultiplier() {
        return feeMultiplier;
    }

    public void setFeeMultiplier(BigDecimal feeMultiplier) {
        this.feeMultiplier = feeMultiplier;
    }

    public Boolean getApprovalRequired() {
        return approvalRequired;
    }

    public void setApprovalRequired(Boolean approvalRequired) {
        this.approvalRequired = approvalRequired;
    }
}
