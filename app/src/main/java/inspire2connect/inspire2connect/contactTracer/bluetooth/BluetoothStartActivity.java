package inspire2connect.inspire2connect.contactTracer.bluetooth;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.contactTracer.base.BaseFragmentActivity;

public class BluetoothStartActivity extends BaseFragmentActivity {

    private BluetoothAdapter mBluetoothAdapter;
    boolean gps_enabled = false;
    boolean network_enabled = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ca_activity_bluetooth_start);
        setTitle(R.string.activity_main_title);

        if (savedInstanceState == null) {

            mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE))
                    .getAdapter();

            // Is Bluetooth supported on this device?
            if (mBluetoothAdapter != null) {


                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) {}

                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch(Exception ex) {}

                if(!gps_enabled && !network_enabled) {
                    // notify user
                    new AlertDialog.Builder(this)
                            .setMessage(R.string.network_not_enabled)
                            .setNegativeButton( R.string.Cancel,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    showErrorText(R.string.location_is_not_enabled);

                                }})
                            .setPositiveButton(R.string.show_location_setting, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })

                            .show();
                }
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
                // Is Bluetooth turned on?
                if (mBluetoothAdapter.isEnabled()) {

                    // Are Bluetooth Advertisements supported on this device?
                    if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {

                        // Everything is supported and enabled, load the fragments.
                        setupFragments();

                    } else {

                        // Bluetooth Advertisements are not supported.
                        showErrorText(R.string.bt_ads_not_supported);
                    }
                } else {

                    // Prompt user to turn on Bluetooth (logic continues in onActivityResult()).
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);


                }
            } else {

                // Bluetooth is not supported.
                showErrorText(R.string.bt_not_supported);
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_ENABLE_BT:

                if (resultCode == RESULT_OK) {

                    // Bluetooth is now Enabled, are Bluetooth Advertisements supported on
                    // this device?
                    if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {

                        // Everything is supported and enabled, load the fragments.
                        setupFragments();

                    } else {

                        // Bluetooth Advertisements are not supported.
                        showErrorText(R.string.bt_ads_not_supported);
                    }
                } else {

                    // User declined to enable Bluetooth, exit the app.
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        BluetoothScannerFragment bluetoothScannerFragment = new BluetoothScannerFragment();
        // Fragments can't access system services directly, so pass it the BluetoothAdapter
        bluetoothScannerFragment.setBluetoothAdapter(mBluetoothAdapter);
        transaction.replace(R.id.scanner_fragment_container, bluetoothScannerFragment);

        BluetoothAdvertisingFragment bluetoothAdvertisingFragment = new BluetoothAdvertisingFragment();
        transaction.replace(R.id.advertiser_fragment_container, bluetoothAdvertisingFragment);

        transaction.commit();
    }

    private void showErrorText(int messageId) {

        TextView view = findViewById(R.id.error_textview);
        view.setText(getString(messageId));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
