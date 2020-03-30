package inspire2connect.inspire2connect;

import android.media.MediaPlayer;

public class custom_media_Class {
    MediaPlayer mediaPlayer;
    Boolean paused;
    int current_time;

    public custom_media_Class(MediaPlayer mediaPlayer, Boolean paused) {
        this.mediaPlayer = mediaPlayer;
        this.paused = paused;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public Boolean getPaused() {
        return paused;
    }

    public void setPaused(Boolean paused) {
        this.paused = paused;
    }

    public int getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(int current_time) {
        this.current_time = current_time;
    }
}
