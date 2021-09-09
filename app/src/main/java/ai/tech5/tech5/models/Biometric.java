package ai.tech5.tech5.models;

import com.google.gson.annotations.SerializedName;

public class Biometric {

    @SerializedName("template")
    public String template;

    @SerializedName("image")
    public String image;

    @SerializedName("position")
    public String position;

    @SerializedName("type")
    public String type;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

