package inspire2connect.inspire2connect.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

import inspire2connect.inspire2connect.R;

@SuppressWarnings("SpellCheckingInspection")
public class BaseActivity extends AppCompatActivity {

    // Firebase References
    public static final DatabaseReference infographicReference = FirebaseDatabase.getInstance("https://washkaro-infographic.firebaseio.com").getReference();
    public static final DatabaseReference guidelinesReference = FirebaseDatabase.getInstance("https://washkaro-guidelines.firebaseio.com").getReference();
    public static final DatabaseReference governmentReference = FirebaseDatabase.getInstance("https://washkaro-government.firebaseio.com").getReference();
    public static final DatabaseReference mythReference = FirebaseDatabase.getInstance("https://washkaro-myth.firebaseio.com").getReference();
    public static final DatabaseReference whoReference = FirebaseDatabase.getInstance("https://washkaro-who.firebaseio.com").getReference();
    public static final DatabaseReference statsReference = FirebaseDatabase.getInstance("https://washkaro-stats.firebaseio.com").getReference();

    // TODO: Add Code for New Language Here
    public static final String englishCode = "en";
    public static final String hindiCode = "hi";

    // TODO: Add Firebase Key for Language Here
    private static final String englishKey = "English";
    private static final String hindiKey = "Hindi";

    private static final HashMap<String, String> langKeyMap = new HashMap<String, String>() {
        {
            // TODO: Add put entry here
            put(englishCode, englishKey);
            put(hindiCode, hindiKey);
        }
    };

    public static DatabaseReference usersDB = FirebaseDatabase.getInstance().getReference("users");

    public static final void Logv(String TAG, String message) {
        Log.v(TAG, message);
    }

    public static final void Loge(String TAG, String message) {
        Log.e(TAG, message);
    }

    public static final void Logd(String TAG, String message) {
        Log.d(TAG, message);
    }

    public static void toggleLang(Activity mActivity) {

        String curLang = Locale.getDefault().getLanguage();

        switch (curLang) {
            case englishCode:
                LocaleHelper.setLocale(mActivity, hindiCode);
                break;
            case hindiCode:
                LocaleHelper.setLocale(mActivity, englishCode);
                break;
            default:
                Logv("language-washkaro", "wrong language: " + curLang);
                break;
        }
        Intent i = new Intent(mActivity, mActivity.getClass());
        mActivity.startActivity(i);
        mActivity.finish();
//        mActivity.getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
//        mActivity.recreate();
    }

    public static String getCurLang() {
        return Locale.getDefault().getLanguage();
    }

    public static String getCurLangKey() {
        return langKeyMap.get(getCurLang());
    }

    public static void openPrivacyPolicy(Activity activity) {
        Intent i = new Intent(activity, urlActivity.class);
        i.putExtra("url", activity.getString(R.string.privacy_policy_url));
        i.putExtra("name", activity.getString(R.string.privacy_policy));
        activity.startActivity(i);
    }

}
