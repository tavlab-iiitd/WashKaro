//package inspire2connect.inspire2connect.contactTracer;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.ActionBar;
//import androidx.preference.ListPreference;
//import androidx.preference.Preference;
//import androidx.preference.PreferenceCategory;
//import androidx.preference.PreferenceFragmentCompat;
//import androidx.preference.PreferenceManager;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//import java.util.Locale;
//
//import inspire2connect.inspire2connect.R;
//import inspire2connect.inspire2connect.contactTracer.base.BaseActivity;
//
//public class SettingsActivity extends BaseActivity {
//
//    public static boolean isLoggedIn = false;
//    public static Context mContext;
//    public static Activity mActivity;
//
//    public static String currentLanguage = "en", currentLang;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ca_settings_activity);
//
//        currentLanguage = Locale.getDefault().getDisplayLanguage().toLowerCase();
//
//        Log.v("chiraglang", currentLanguage.toLowerCase());
//
//        mContext = this;
//        mActivity = this;
//        if(MainActivity.isAuthEnabled) {
//            isLoggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;
//        }
//        if(!MainActivity.isAuthEnabled) {
//            isLoggedIn = false;
//        }
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.settingsFrame, new SettingsFragment())
//                .commit();
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public static class SettingsFragment extends PreferenceFragmentCompat {
//
//        private final String TAG = SettingsActivity.class.getSimpleName();
//        private Preference logoutPref;
//        private PreferenceCategory authCat;
//
//        @Override
//        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            setPreferencesFromResource(R.xml.preferences_main, rootKey);
//        }
//
//        @Override
//        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//            super.onViewCreated(view, savedInstanceState);
//
//            Preference aboutPref = findPreference(getString(R.string.about_key));
//            Preference privacyPref = findPreference(getString(R.string.privacy_policy_key));
//            ListPreference langPref = findPreference(getString(R.string.lang_key));
//
//            // TODO: Correct Issue of Lang
//            langPref.setValue(currentLanguage);
//
//            authCat = findPreference(getString(R.string.security_key));
//            logoutPref = findPreference(getString(R.string.logout_key));
//
//            updateUI();
//
//            bindPreferencesToValue(langPref);
//
//            logoutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    FirebaseAuth.getInstance().signOut();
//                    updateUI();
//                    getActivity().onBackPressed();
//                    return true;
//                }
//            });
//
//            aboutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    Intent intent = new Intent(mContext, AboutActivity.class);
//                    startActivity(intent);
//                    return true;
//                }
//            });
//
//            // TODO: Set Privacy Page
//            privacyPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    Intent intent = new Intent(mContext, AboutActivity.class);
//                    startActivity(intent);
//                    return true;
//                }
//            });
//
//        }
//
//        private void updateUI() {
//            if (isLoggedIn) {
//                logoutPref.setVisible(true);
//                authCat.setVisible(true);
//            } else {
//                logoutPref.setVisible(false);
//                authCat.setVisible(false);
//            }
//        }
//
//        private void bindPreferencesToValue(Preference preference) {
//            preference.setOnPreferenceChangeListener(bindPreferencesToValueListener);
//            bindPreferencesToValueListener.onPreferenceChange(preference,
//                    PreferenceManager
//                            .getDefaultSharedPreferences(preference.getContext())
//                            .getString(preference.getKey(), ""));
//        }
//
//        private Preference.OnPreferenceChangeListener bindPreferencesToValueListener = new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                String newVal = newValue.toString();
//                if (preference instanceof ListPreference) {
//                    setLocale(mContext, newVal.toLowerCase());
//                }
//                return true;
//            }
//        };
//    }
//
//    @Override
//    public void onBackPressed() {
//        Intent returnIntent = new Intent();
//        setResult(RESULT_OK, returnIntent);
//        finish();
//    }
//
//    public static void setLocale(Context context, String localeName) {
//        if (!localeName.equals(currentLanguage)) {
//            Locale myLocale = new Locale(localeName);
//            Locale.setDefault(myLocale);
//            Resources res = context.getResources();
//            DisplayMetrics dm = res.getDisplayMetrics();
//            Configuration conf = res.getConfiguration();
//            conf.locale = myLocale;
//            res.updateConfiguration(conf, dm);
//            Intent refresh = new Intent(context, MainActivity.class);
//            refresh.putExtra(currentLang, localeName);
//            context.startActivity(refresh);
//
//        }
//    }
//
//}