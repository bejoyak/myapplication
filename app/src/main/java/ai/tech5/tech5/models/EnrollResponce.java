package ai.tech5.tech5.models;

import com.google.gson.annotations.SerializedName;

public class EnrollResponce {
    @SerializedName("errorMessage")
    private String errorMessage;

    @SerializedName("errorCode")
    private String errorCode;

    @SerializedName("transactionId")
    private String transactionId;

//    @SerializedName("component")
//    private String component;

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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

//    public String getComponent() {
//        return component;
//    }
//
//    public void setComponent(String component) {
//        this.component = component;
//    }
}