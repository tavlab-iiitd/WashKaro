package inspire2connect.inspire2connect.base;

import android.util.Log;

import androidx.fragment.app.ListFragment;

@SuppressWarnings("SpellCheckingInspection")
public class BaseListFragment extends ListFragment {

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
