package ai.tech5.tech5.enroll.model;

import com.google.gson.annotations.SerializedName;

public class BarcodeParams {

    @SerializedName("blockCols")
    public int blockCols = 4;
    @SerializedName("blockRows")
    public int blockRows = 4;
    @SerializedName("errorCorrection")
    public int errorCorrection = 10;
    @SerializedName("gridSize")
    public int gridSize = 6;
    @SerializedName("thickness")
    public int thickness = 2;
    @SerializedName("title")
    public BarcodeTitle title;

    @SerializedName("expirationdate")
    public String expirationDate;
}
