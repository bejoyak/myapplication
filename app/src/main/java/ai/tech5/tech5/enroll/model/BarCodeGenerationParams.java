package ai.tech5.tech5.enroll.model;

public class BarCodeGenerationParams {

    public int blockCols;
    public int blockRows;
    public int gridSize;
    public int thickness;
    public String style;

    public BarcodeTitle title;
    public ExpiryDate expirationDate;
    public ErrorCorrection errorCorrection;

    public QuantityAndSize cryptographID;
    public QuantityAndSize compressedFaceImage;
    public QuantityAndSize faceBiometricTemplate;
    public QuantityAndSize fingerprintBiometricTemplates;
    public QuantityAndSize voiceBiometricTemplate;
    public QuantityAndSize demographicData;
    public QuantityAndSize digitalCertificate;
    public QuantityAndSize extra;
    public QuantityAndSize binaryBlob;
}