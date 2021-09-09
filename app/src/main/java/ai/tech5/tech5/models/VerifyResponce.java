package ai.tech5.tech5.models;

import com.google.gson.annotations.SerializedName;

public class VerifyResponce {
    @SerializedName("verificationResult")
    public boolean verificationResult;

    @SerializedName("errorMessage")
    public String errorMessage;

    @SerializedName("errorCode")
    public String errorCode;


    @SerializedName("verificationScore")
    public String verificationScore;

    @SerializedName("transactionId")
    public String transactionId;

    @SerializedName("component")
    public String component;

    @SerializedName("demographics")
    public EkycDemographics demographics;

    public boolean getVerificationResult() {
        return verificationResult;
    }

    public void setVerificationResult(boolean verificationResult) {
        this.verificationResult = verificationResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getVerificationScore() {
        return verificationScore;
    }

    public void setVerificationScore(String verificationScore) {
        this.verificationScore = verificationScore;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public EkycDemographics getDemographics() {
        return demographics;
    }

    public void setDemographics(EkycDemographics demographics) {
        this.demographics = demographics;
    }
}
