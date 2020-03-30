package inspire2connect.inspire2connect;

public class guideline_sigle_object {
    String guideline;
    String position;
    String audio_url;

    public guideline_sigle_object(String guideline, String position, String audio_url) {
        this.guideline = guideline;
        this.position = position;
        this.audio_url = audio_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGuideline() {
        return guideline;
    }

    public void setGuideline(String guideline) {
        this.guideline = guideline;
    }
}
