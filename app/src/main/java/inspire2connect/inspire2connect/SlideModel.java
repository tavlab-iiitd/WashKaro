package inspire2connect.inspire2connect;

import android.util.Log;
import android.widget.Toast;

public class SlideModel {
    private String InfoURL;
    Long Sno;

    public SlideModel() {
        Log.d("FlipperFirebase", " empty init");
    }

    public SlideModel(String imageUrl, Long sno) {
        this.InfoURL = imageUrl;
        this.Sno = sno;
        Log.d("FlipperFirebase", "got datat");
    }

    public String getImageUrl() {
        return InfoURL;
    }

    public void setImageUrl(String imageUrl) {
        this.InfoURL = imageUrl;
    }

    public Long getName() {
        return Sno;
    }

    public void setName(Long sno) {
        this.Sno = sno;
    }
}