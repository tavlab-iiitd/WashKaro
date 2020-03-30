package inspire2connect.inspire2connect;

import android.text.Spanned;

public class daily_update_single_object {
    Spanned daily_update;
    String position;
    String audio_url;


    public daily_update_single_object(Spanned daily_update, String position, String audio_url) {
        this.daily_update = daily_update;
        this.position = position;
        this.audio_url = audio_url;

    }

    public Spanned getDaily_update() {
        return daily_update;
    }

    public void setDaily_update(Spanned daily_update) {
        this.daily_update = daily_update;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }


}
