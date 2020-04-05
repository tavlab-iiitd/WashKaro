package inspire2connect.inspire2connect.contactTracer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.contactTracer.base.BaseActivity;
import inspire2connect.inspire2connect.contactTracer.bluetooth.BluetoothStartActivity;
import inspire2connect.inspire2connect.contactTracer.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 0;
    public static final int REQUEST_LOGIN = 0;
    public static final int REQUEST_LOGOUT = 1;

    private Button discover;
    private Button interactions;
    private Button login;
    private Button settings;

    private FirebaseAuth mAuth;

    private boolean isLoggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usersDB.keepSynced(true);

        setContentView(R.layout.ca_activity_main);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        this.discover = findViewById(R.id.discover);
        this.interactions = findViewById(R.id.interactions);
        this.login = findViewById(R.id.login_bt);
        this.settings = findViewById(R.id.settings_bt);
        this.discover.setOnClickListener(this);
        this.interactions.setOnClickListener(this);
        this.login.setOnClickListener(this);
        this.settings.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.user_id_key), mAuth.getUid());

        isLoggedIn = mAuth.getCurrentUser()!=null;

        if(isLoggedIn) {
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

        updateUI();
    }

    private void updateUI() {

        isLoggedIn = mAuth.getCurrentUser()!=null;

        if(isLoggedIn) {
            discover.setVisibility(View.VISIBLE);
            interactions.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        } else {
            discover.setVisibility(View.INVISIBLE);
            interactions.setVisibility(View.INVISIBLE);
            login.setVisibility(View.VISIBLE);
        }
    }



    private void showDiscoverActivity() {
        Intent intent = new Intent(this, BluetoothStartActivity.class);
        startActivity(intent);
    }

    private void showLoginActivity() {
        Intent intent = new Intent(this, PhoneAuthActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    private void showInteractionsActiviy() {
        Intent intent = new Intent(this, InteractionsActivity.class);
        startActivity(intent);
    }

    private void showAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void showSettingsActivtiy() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, REQUEST_LOGOUT);
        updateUI();
    }

    private void exitApp () {
        finishAffinity();
        System.exit(0);
    }

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
            case R.id.discover:
                showDiscoverActivity();
                break;
            case R.id.interactions:
                showInteractionsActiviy();
                break;
            case R.id.login_bt:
                showLoginActivity();
                break;
            case R.id.settings_bt:
                showSettingsActivtiy();
                break;
//            case R.id.logout_bt:
//                FirebaseAuth.getInstance().signOut();
//                updateUI();
//                break;
//            case R.id.about_bt:
//                showAboutActivity();
//                break;
        }
    }
}
