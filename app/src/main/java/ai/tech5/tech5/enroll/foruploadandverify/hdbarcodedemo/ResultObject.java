package ai.tech5.tech5.enroll.foruploadandverify.hdbarcodedemo;

public class ResultObject {

    private float timeTakenForTemplateCreation;
    private float timeTakenForMatching;
    private float matchingScore;

    public float getTimeTakenForTemplateCreation() {
        return timeTakenForTemplateCreation;
    }

    public void setTimeTakenForTemplateCreation(float timeTakenForTemplateCreation) {
        this.timeTakenForTemplateCreation = timeTakenForTemplateCreation;
    }

    public float getTimeTakenForMatching() {
        return timeTakenForMatching;
    }

    public void setTimeTakenForMatching(float timeTakenForMatching) {
        this.timeTakenForMatching = timeTakenForMatching;
    }

    public float getMatchingScore() {
        return matchingScore;
    }

    public void setMatchingScore(float matchingScore) {
        this.matchingScore = matchingScore;
    }
}
