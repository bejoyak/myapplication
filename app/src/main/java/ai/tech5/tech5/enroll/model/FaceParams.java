package ai.tech5.tech5.enroll.model;

import com.google.gson.annotations.SerializedName;

public class FaceParams {


    @SerializedName("performTemplateExtraction")
    public boolean performTemplateExtraction;
    @SerializedName("faceDetectorConfidence")
    public float faceDetectorConfidence = 0.6f;
    @SerializedName("faceSelectorAlg")
    public int faceSelectorAlg = 1;
    @SerializedName("performCompression")
    public boolean performCompression;
    @SerializedName("compressionLevel")
    public int compressionLevel;

    public FaceParams(boolean performTemplateExtraction, float faceDetectorConfidence, int faceSelectorAlg, boolean performCompression, int compressionLevel) {


        this.performTemplateExtraction = performTemplateExtraction;
        this.faceDetectorConfidence = faceDetectorConfidence;
        this.faceSelectorAlg = faceSelectorAlg;
        this.performCompression = performCompression;
        this.compressionLevel = compressionLevel;
    }
}
