package inspire2connect.inspire2connect.contactTracer.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.concurrent.TimeUnit;

import inspire2connect.inspire2connect.R;

public class BluetoothAdvertiserService extends Service {

    private static final String TAG = BluetoothAdvertiserService.class.getSimpleName();

    public static boolean running = false;

    public static final String ADVERTISE_FAILED =
            "com.example.android.bluetoothadvertisements.advertising_failed";

    public static final String ADVERTISE_FAIL_EXTRA_CODE = "failureCode";

    public static final int ADVERTISE_TIME_OUT = 6;

    private BluetoothLeAdvertiser bluetoothAdvertiser;

    private AdvertiseCallback bluetoothAdvertiseCallback;

    private Handler handler;

    private Runnable timeoutRunnable;

    /**
     * Length of time to allow advertising before automatically shutting off. (10 minutes)
     */
    private long TIMEOUT = TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES);

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        running = true;
        initialize();
        startAdvertising();
        setTimeout();
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDestroy() {

        running = false;
        stopAdvertising();
        handler.removeCallbacks(timeoutRunnable);
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initialize() {
        if (bluetoothAdvertiser == null) {

            BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager != null) {
                BluetoothAdapter bluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null) {
                    bluetoothAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
                } else {
                    Toast.makeText(this, getString(R.string.bt_null), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.bt_null), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void setTimeout(){
        handler = new Handler();
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "AdvertiserService has reached timeout of "+TIMEOUT+" milliseconds, stopping advertising.");
                sendFailureIntent(ADVERTISE_TIME_OUT);
                stopSelf();
            }
        };
        handler.postDelayed(timeoutRunnable, TIMEOUT);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startAdvertising() {
        //TODO make this foreground
        Log.d(TAG, "Service: Starting Advertising");
        if (bluetoothAdvertiseCallback == null) {
            AdvertiseSettings settings = buildAdvertiseSettings();
            AdvertiseData data = buildAdvertiseData();
            bluetoothAdvertiseCallback = new BluetoothAdvertiseCallback();

            if (bluetoothAdvertiser != null) {
                bluetoothAdvertiser.startAdvertising(settings, data,
                        bluetoothAdvertiseCallback);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopAdvertising() {
        Log.d(TAG, "Service: Stopping Advertising");
        if (bluetoothAdvertiser != null) {
            bluetoothAdvertiser.stopAdvertising(bluetoothAdvertiseCallback);
            bluetoothAdvertiseCallback = null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private AdvertiseData buildAdvertiseData() {

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);

        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
//        dataBuilder.addServiceData()

        String dataToShare = sharedPreferences.getString(getString(R.string.uuid_id_key), "null") + "-" +
                sharedPreferences.getInt(getString(R.string.state_key), 0);

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
        return settingsBuilder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class BluetoothAdvertiseCallback extends AdvertiseCallback {

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);

            Log.d(TAG, "Advertising failed");
            sendFailureIntent(errorCode);
            stopSelf();

        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.d(TAG, "Advertising successfully started");
        }
    }


    private void sendFailureIntent(int errorCode){
        Intent failureIntent = new Intent();
        failureIntent.setAction(ADVERTISE_FAILED);
        failureIntent.putExtra(ADVERTISE_FAIL_EXTRA_CODE, errorCode);
        sendBroadcast(failureIntent);
    }

}
