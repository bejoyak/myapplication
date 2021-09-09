package ai.tech5.tech5.enroll.model;

public class FingerDetectionModes {
    private boolean isRight4FSelected;
    private boolean isLeft4FSelected;
    private boolean isRightThumbSelected;
    private boolean isLeftThumbSelected;

    public FingerDetectionModes(boolean isRight4FSelected, boolean isLeft4FSelected, boolean isRightThumbSelected, boolean isLeftThumbSelected) {
        this.isRight4FSelected = isRight4FSelected;
        this.isLeft4FSelected = isLeft4FSelected;
        this.isRightThumbSelected = isRightThumbSelected;
        this.isLeftThumbSelected = isLeftThumbSelected;
    }

    private FingerDetectionModes(final Builder builder) {
        this.isRight4FSelected = builder.isRight4FSelected;
        this.isLeft4FSelected = builder.isLeft4FSelected;
        this.isRightThumbSelected = builder.isRightThumbSelected;
        this.isLeftThumbSelected = builder.isLeftThumbSelected;
    }

    private FingerDetectionModes() {
    }

    public boolean isRight4FSelected() {
        return isRight4FSelected;
    }

    public void setRight4FSelected(boolean right4FSelected) {
        isRight4FSelected = right4FSelected;
    }

    public boolean isLeft4FSelected() {
        return isLeft4FSelected;
    }

    public void setLeft4FSelected(boolean left4FSelected) {
        isLeft4FSelected = left4FSelected;
    }

    public boolean isRightThumbSelected() {
        return isRightThumbSelected;
    }

    public void setRightThumbSelected(boolean rightThumbSelected) {
        isRightThumbSelected = rightThumbSelected;
    }

    public boolean isLeftThumbSelected() {
        return isLeftThumbSelected;
    }

    public void setLeftThumbSelected(boolean leftThumbSelected) {
        isLeftThumbSelected = leftThumbSelected;
    }

    public static class Builder {
        private boolean isRight4FSelected;
        private boolean isLeft4FSelected;
        private boolean isRightThumbSelected;
        private boolean isLeftThumbSelected;

        public Builder setRight4FSelected(boolean right4FSelected) {
            isRight4FSelected = right4FSelected;
            return this;
        }

        public Builder setLeft4FSelected(boolean left4FSelected) {
            isLeft4FSelected = left4FSelected;
            return this;
        }

        public Builder setRightThumbSelected(boolean rightThumbSelected) {
            isRightThumbSelected = rightThumbSelected;
            return this;
        }

        public Builder setLeftThumbSelected(boolean leftThumbSelected) {
            isLeftThumbSelected = leftThumbSelected;
            return this;
        }

        public FingerDetectionModes create() {
            if (!isRight4FSelected&& !isLeft4FSelected&&!isRightThumbSelected&&!isLeftThumbSelected) {
                setLeft4FSelected(true);
            }
            return new FingerDetectionModes(this);
        }
    }
}


