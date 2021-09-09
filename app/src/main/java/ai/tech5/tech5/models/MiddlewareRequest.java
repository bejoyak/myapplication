package ai.tech5.tech5.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MiddlewareRequest {

    @SerializedName("transactionId")
    public String transactionId;

    @SerializedName("digital_Id")
    public String digitalID;

    @SerializedName("requestType")
    public String requestType;


    @SerializedName("passiveLiveness")
    public boolean passiveLiveness;

    @SerializedName("faceThreshold")
    public int faceThreshold;

    @SerializedName("biometrics")
    public ArrayList<Biometric> biometrics;

    @SerializedName("demographics")
    public EkycDemographics demographics;

    public EkycDemographics getDemographics() {
        return demographics;
    }

    public void setDemographics(EkycDemographics demographics) {
        this.demographics = demographics;
    }

    public ArrayList<Biometric> getBiometrics() {
        return biometrics;
    }

    public void setBiometrics(ArrayList<Biometric> biometrics) {
        this.biometrics = biometrics;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getDigitalID() {
        return digitalID;
    }

    public void setDigitalID(String digitalID) {
        this.digitalID = digitalID;
    }


//    @SerializedName("component")
//    private String component;

//    @SerializedName("transactionSource")
//    private String transactionSource;

//    @SerializedName("customer_Id")
//    private String customerId;

//    @SerializedName("NIK")
//    private String NIK;

//    @SerializedName("device_Id")
//    private String deviceId;

//    @SerializedName("app_Version")
//    private String appVersion;

//    @SerializedName("sdk_Version")
//    private String sdkVersion;

//    @SerializedName("liveness")
//    private String liveness;

//    @SerializedName("passiveLiveness")
//    private String passiveLiveness;

//    @SerializedName("verifyIdCardFaceImage")
//    private String verifyIDCardFaceImage;

//    @SerializedName("verifyWithImage")
//    private String isVerifyWithImage;

//    @SerializedName("localVerification")
//    private String isLocalVerification;

//    @SerializedName("verifyBeforeEnroll")
//    private String verifyBeforeEnroll;

//    @SerializedName("faceThreshold")
//    private String faceThreshold;

//    @SerializedName("demographicsToVerify")
//    private VerifyDemographicsRequest demographicsToVerify;

//    public VerifyDemographicsRequest getDemographicsToVerify() {
//        return demographicsToVerify;
//    }
//
//    public void setDemographicsToVerify(VerifyDemographicsRequest demographicsToVerify) {
//        this.demographicsToVerify = demographicsToVerify;
//    }

//    public String getComponent() {
//        return component;
//    }
//
//    public void setComponent(String transactionSource) {
//        this.component = transactionSource;
//    }

//    public String getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(String customerId) {
//        this.customerId = customerId;
//    }

//    public String getNIK() {
//        return NIK;
//    }
//
//    public void setNIK(String nIK) {
//        NIK = nIK;
//    }

//    public String getDeviceId() {
//        return deviceId;
//    }
//
//    public void setDeviceId(String deviceId) {
//        this.deviceId = deviceId;
//    }
//
//    public String getAppVersion() {
//        return appVersion;
//    }
//
//    public void setAppVersion(String appVersion) {
//        this.appVersion = appVersion;
//    }
//
//    public String getSdkVersion() {
//        return sdkVersion;
//    }
//
//    public void setSdkVersion(String sdkVersion) {
//        this.sdkVersion = sdkVersion;
//    }
//
//    public String getFaceThreshold() {
//        return faceThreshold;
//    }
//
//    public void setFaceThreshold(String faceThreshold) {
//        this.faceThreshold = faceThreshold;
//    }
//    public String getLivenessString() {
//        return liveness;
//    }
//
//    public void setLiveness(String liveness) {
//        this.liveness = liveness;
//    }
//
//    public String getPassiveLivenessString() {
//        return passiveLiveness;
//    }
//
//    public void setPassiveLiveness(String passiveLiveness) {
//        this.passiveLiveness = passiveLiveness;
//    }

//    public boolean isLivenessEnabled() {
//
//        if (liveness != null && liveness.equalsIgnoreCase("true")) {
//            return true;
//        }
//
//        return false;
//
//    }
//
//    public String getVerifyIDCardFaceImageString() {
//        return verifyIDCardFaceImage;
//    }
//
//    public boolean isVerifyIDCardFaceImageEnabled() {
//
//        if (verifyIDCardFaceImage != null && verifyIDCardFaceImage.equalsIgnoreCase("true")) {
//            return true;
//        }
//
//        return false;
//
//    }
//
//
//    public boolean isPassiveLivenessEnabled() {
//
//        if (passiveLiveness != null && passiveLiveness.equalsIgnoreCase("true")) {
//            return true;
//        }
//
//        return false;
//
//    }
//
//    public String getVerifyWithImageString() {
//
//        return isVerifyWithImage;
//
//    }
//
//    public boolean isVerifyWithImageEnabled() {
//
//        if (isVerifyWithImage != null && isVerifyWithImage.equalsIgnoreCase("true")) {
//            return true;
//        }
//
//        return false;
//    }
//
//    public boolean isLocalVerification() {
//
//        if (isLocalVerification != null && isLocalVerification.equalsIgnoreCase("true")) {
//            return true;
//        }
//
//        return false;
//
//    }
//
//    public boolean doVerificationBeforeEnroll() {
//
//        if (verifyBeforeEnroll != null && verifyBeforeEnroll.equalsIgnoreCase("true")) {
//
//            return true;
//
//        }
//
//        return false;
//    }
//
//    public String getVerifyBeforeEnrollString() {
//
//        return verifyBeforeEnroll;
//    }
//
//    public String getTransactionSource() {
//        return transactionSource;
//    }
//
//    public void setTransactionSource(String transactionSource) {
//        this.transactionSource = transactionSource;
//    }
//    public String getLocalVerificationString() {
//        return isLocalVerification;
//    }
//
//    public void setLocalVerification(String isLocalVerification) {
//        this.isLocalVerification = isLocalVerification;
//    }

}
