package inspire2connect.inspire2connect.contactTracer.base;

import android.util.Log;

@SuppressWarnings("SpellCheckingInspection")
public class Base {

    public static final void Logv(String TAG, String message) {
        Log.v(TAG, message);
    }

    public static final void Loge(String TAG, String message) {
        Log.e(TAG, message);
    }

    public static final void Logd(String TAG, String message) {
        Log.d(TAG, message);
    }
}
