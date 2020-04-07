package inspire2connect.inspire2connect.contactTracer;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.contactTracer.base.BaseActivity;
import inspire2connect.inspire2connect.contactTracer.bluetooth.Constants;
import inspire2connect.inspire2connect.contactTracer.models.User;
import inspire2connect.inspire2connect.contactTracer.sqlDB.InteractionRepository;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 0;
    public static final int REQUEST_LOGIN = 0;
    public static final int REQUEST_LOGOUT = 1;
    public static boolean isAuthEnabled = false;

    private Button login;
    private Button settings;

    private FirebaseAuth mAuth;
    private BluetoothAdapter bluetoothAdapter;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    private boolean isLoggedIn;
    private boolean isSafe;

    private ImageView traceImage;
    private MaterialTextView traceTitle;
    private MaterialTextView traceContent;

    private InteractionRepository dbRepo;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ca_activity_main);

        traceImage = findViewById(R.id.tracer_photo);
        traceTitle = findViewById(R.id.tracer_title);
        traceContent = findViewById(R.id.tracer_content);

        getSupportActionBar().hide();

        mContext = this;

        isSafe = true;

        dbRepo = new InteractionRepository(this.getApplicationContext());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final String res = dbRepo.getUUIDs();
                int count = Integer.parseInt(res);
                if(count>0) {
                    isSafe = false;
                    updateSafe();
                } else {
                    isSafe = true;
                    updateSafe();
                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        devices.setText(res);
//                    }
//                });
//                interactionDatabase.interactionDao().insertTask(entity);
                return null;
            }
        }.execute();

        updateSafe();

        if(isAuthEnabled) {
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            usersDB.keepSynced(true);
            mAuth = FirebaseAuth.getInstance();
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);

            final SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.user_id_key), mAuth.getUid());

            isLoggedIn = mAuth.getCurrentUser() != null;

            if (isLoggedIn) {
                final String phoneNo = mAuth.getCurrentUser().getPhoneNumber();
                usersDB.orderByChild("phoneNo").equalTo(phoneNo).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User me = null;

                        for (DataSnapshot obj : dataSnapshot.getChildren()) {
                            me = obj.getValue(User.class);
                            break;
                        }
                        if (me != null) {
                            editor.putString(getString(R.string.uuid_id_key), me.UUID);
                            editor.putString(getString(R.string.phone_key), me.phoneNo);
                            editor.putInt(getString(R.string.state_key), me.state);
                            editor.apply();
//                    editor.commit();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        }
        else {
            //TODO create an id if not exists with state = 1
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
            String userExists = sharedPreferences.getString(getString(R.string.uuid_id_key), "null");
            String id;
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            if(userExists == "null") {
                id = createId(8);
                editor.putString(getString(R.string.uuid_id_key), id);
                editor.putString(getString(R.string.phone_key), "00000000");
                editor.putInt(getString(R.string.state_key), 1);
            }
        }
//        this.settings = findViewById(R.id.settings_bt);
//        this.settings.setOnClickListener(this);

        this.login = findViewById(R.id.login_bt);
        this.login.setOnClickListener(this);

        initBluetooth();
        initLocation();
        updateUI();
    }

    private void updateSafe() {
        if(isSafe) {
//            Picasso.get().load(R.drawable.ca_safe).into(traceImage);
            traceImage.setImageResource(R.drawable.ca_safe);
            traceTitle.setText(R.string.you_are_safe);
            traceContent.setText(R.string.you_are_safe_content);
        } else {
//            Picasso.get().load(R.drawable.ca_unsafe).into(traceImage);
            traceImage.setImageResource(R.drawable.ca_unsafe);
            traceTitle.setText(R.string.you_are_unsafe);
            traceContent.setText(R.string.you_are_unsafe_content);
        }
    }

    private void updateUI() {

        if(isAuthEnabled) {
            isLoggedIn = mAuth.getCurrentUser() != null;
        } else {
            isLoggedIn = true;
        }
        if(isLoggedIn) {
            login.setVisibility(View.GONE);
        } else {
            login.setVisibility(View.VISIBLE);
        }
    }



    private void showLoginActivity() {
         Intent intent = new Intent(this, PhoneAuthActivity.class);
         startActivityForResult(intent, REQUEST_LOGIN);
    }

//    private void showInteractionsActiviy() {
//        Intent intent = new Intent(this, InteractionsActivity.class);
//        startActivity(intent);
//    }
//
//    private void showAboutActivity() {
//        Intent intent = new Intent(this, AboutActivity.class);
//        startActivity(intent);
//    }
//
//    private void showSettingsActivtiy() {
//        Intent intent = new Intent(this, SettingsActivity.class);
//        startActivityForResult(intent, REQUEST_LOGOUT);
//        updateUI();
//    }
//
//    private void exitApp () {
//        finishAffinity();
//        System.exit(0);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_LOGIN) {
            updateUI();
        } else if(requestCode == REQUEST_LOGOUT) {
            updateUI();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.login_bt:
                showLoginActivity();
                break;
//            case R.id.settings_bt:
//                showSettingsActivtiy();
//                break;
//            case R.id.logout_bt:
//                FirebaseAuth.getInstance().signOut();
//                updateUI();
//                break;
//            case R.id.about_bt:
//                showAboutActivity();
//                break;
        }
    }

    private String createId(int n) {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    private void initBluetooth() {
        bluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE))
                .getAdapter();
        // Is Bluetooth supported on this device?
        if (bluetoothAdapter != null) {
            // Is Bluetooth turned on?
            if (bluetoothAdapter.isEnabled()) {
                // Are Bluetooth Advertisements supported on this device?
                if (bluetoothAdapter.isMultipleAdvertisementSupported()) {
                } else {
                    // Bluetooth Advertisements are not supported.
                    Toast.makeText(this, getString(R.string.bt_ads_not_supported), Toast.LENGTH_LONG ).show();
                }
            } else {
                // Prompt user to turn on Bluetooth (logic continues in onActivityResult()).
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
            }
        } else {
            // Bluetooth is not supported.
            Toast.makeText(this, getString(R.string.bt_not_supported), Toast.LENGTH_LONG ).show();
        }
    }

    private void initLocation() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            Toast.makeText(this, getString(R.string.location_is_not_enabled),Toast.LENGTH_LONG).show();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            Toast.makeText(this, getString(R.string.location_is_not_enabled),Toast.LENGTH_LONG).show();

        }

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(this)
                    .setMessage(R.string.network_not_enabled)
                    .setPositiveButton(R.string.show_location_setting, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 1);

    }
}
