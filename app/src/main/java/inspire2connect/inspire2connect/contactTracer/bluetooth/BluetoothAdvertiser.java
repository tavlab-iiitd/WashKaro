package inspire2connect.inspire2connect.contactTracer.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


import java.util.concurrent.TimeUnit;

import inspire2connect.inspire2connect.R;

import static inspire2connect.inspire2connect.contactTracer.base.Base.Logd;


public class BluetoothAdvertiser {
    private Context context;
    private BluetoothAdapter bluetoothAdapter;


    private static final String TAG = BluetoothAdvertiser.class.getSimpleName();


    public static final String ADVERTISE_FAILED =
            "com.example.android.bluetoothadvertisements.advertising_failed";

    public static final String ADVERTISE_FAIL_EXTRA_CODE = "failureCode";

    public static final int ADVERTISE_TIME_OUT = 6;

    private BluetoothLeAdvertiser bluetoothAdvertiser;

    private AdvertiseCallback bluetoothAdvertiseCallback;

    private Handler handler;

    private Runnable timeoutRunnable;

    private boolean isAdvertising = false;
    private static final long ADVERTISE_PERIOD = 900000;


    /**
     * Length of time to allow advertising before automatically shutting off. (10 minutes)
     */
    private long TIMEOUT = TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES);

    public BluetoothAdvertiser(Context mContext, BluetoothAdapter mBluetoothAdapter) {
        this.context = mContext;
        this.bluetoothAdapter = mBluetoothAdapter;
        this.bluetoothAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        handler = new Handler();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startAdvertising() {
        if(isAdvertising) return;
        bluetoothAdvertiseCallback = new BluetoothAdvertiseCallback();
        isAdvertising = true;
        AdvertiseSettings settings = buildAdvertiseSettings();
        AdvertiseData data = buildAdvertiseData();
        try {
            bluetoothAdvertiser.startAdvertising(settings, data,
                    bluetoothAdvertiseCallback);
        } catch (Exception e){
            Log.d( "Advertise did not start", e.getMessage());
        }
        handler.removeCallbacksAndMessages(null);
        Logd(TAG, "Starting advertising again");
        // Will stop the scanning after a set time.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isAdvertising) {
                    Logd(TAG,"Start advertising again");
                    stopAdvertising();
                    startAdvertising();
                }
            }
        },ADVERTISE_PERIOD);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void stopAdvertising() {
        Log.d(TAG, "Service: Stopping Advertising");
        isAdvertising = false;
        if (bluetoothAdvertiser != null) {
            bluetoothAdvertiser.stopAdvertising(bluetoothAdvertiseCallback);
            bluetoothAdvertiseCallback = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private AdvertiseData buildAdvertiseData() {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);

        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
//        dataBuilder.addServiceData()

        String dataToShare = sharedPreferences.getString(context.getString(R.string.uuid_id_key), "null") + "-" +
                sharedPreferences.getInt(context.getString(R.string.state_key), 0);

        dataBuilder.setIncludeDeviceName(false);

        dataBuilder.addServiceUuid(Constants.Service_UUID);
        dataBuilder.addServiceData(Constants.Service_UUID, dataToShare.getBytes());

        return dataBuilder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private AdvertiseSettings buildAdvertiseSettings() {
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        //TODO figure out what settings to use
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW);
        settingsBuilder.setTimeout(0);
        settingsBuilder.setConnectable(true);
        return settingsBuilder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class BluetoothAdvertiseCallback extends AdvertiseCallback {

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            handleError(errorCode);
            Log.d(TAG, "Advertising failed");

        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.d(TAG, "Advertising successfully started");
        }
    }
    private void handleError(int errorCode) {
        String errorMessage = context.getString(R.string.start_error_prefix);
        switch (errorCode) {
            case AdvertiseCallback.ADVERTISE_FAILED_ALREADY_STARTED:
                errorMessage += " " + context.getString(R.string.start_error_already_started);
                break;
            case AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE:
                errorMessage += " " + context.getString(R.string.start_error_too_large);
                break;
            case AdvertiseCallback.ADVERTISE_FAILED_FEATURE_UNSUPPORTED:
                errorMessage += " " + context.getString(R.string.start_error_unsupported);
                break;
            case AdvertiseCallback.ADVERTISE_FAILED_INTERNAL_ERROR:
                errorMessage += " " + context.getString(R.string.start_error_internal);
                break;
            case AdvertiseCallback.ADVERTISE_FAILED_TOO_MANY_ADVERTISERS:
                errorMessage += " " + context.getString(R.string.start_error_too_many);
                break;
            case ADVERTISE_TIME_OUT:
                errorMessage = " " + context.getString(R.string.advertising_timedout);
                break;
            default:
                errorMessage += " " + context.getString(R.string.start_error_unknown);
        }

        Toast.makeText(context.getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
    }



}
