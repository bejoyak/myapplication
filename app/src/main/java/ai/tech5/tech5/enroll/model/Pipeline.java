package ai.tech5.tech5.enroll.model;

import com.google.gson.annotations.SerializedName;

public class Pipeline {

    @SerializedName("facePipeline")
    public FaceParams facePipeline;
    @SerializedName("barcodeGenerationParameters")
    public BarcodeParams barcodeGenerationParameters;
    @SerializedName("emailSender")
    public EmailParams emailParams;
}
