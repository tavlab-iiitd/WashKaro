package inspire2connect.inspire2connect.contactTracer.bluetooth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.contactTracer.MainActivity;
import inspire2connect.inspire2connect.contactTracer.sqlDB.InteractionRepository;

import static inspire2connect.inspire2connect.contactTracer.base.Base.Logd;


public class BluetoothScanner {
    private static final String TAG = BluetoothScanner.class.getSimpleName();
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    // TODO: Scanning Period is set here. Change the same
    private static final long SCAN_PERIOD = 600000;
    private ArrayList<ScanResult> scanResultArrayList;
    private BluetoothLeScanner bluetoothScanner;
    private ScanCallback bluetoothScanCallback;
    private Handler handler;

    private boolean isCurrentlyScanning = false;
    private InteractionRepository dbRepo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  BluetoothScanner(Context mContext, BluetoothAdapter mBluetoothAdapter) {
        this.context = mContext;
        this.bluetoothAdapter = mBluetoothAdapter;
        bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();
        handler = new Handler();
        dbRepo = new InteractionRepository(mContext.getApplicationContext());
        scanResultArrayList = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startScanning() {
        if(isCurrentlyScanning) return;
        bluetoothScanCallback = new BluetoothScanCallback();
        isCurrentlyScanning = true;
        try {
            bluetoothScanner.startScan(buildScanFilters(), buildScanSettings(), bluetoothScanCallback);
            String toastText = context.getString(R.string.scan_start_toast) + " "
                    + TimeUnit.SECONDS.convert(SCAN_PERIOD, TimeUnit.MILLISECONDS) + " "
                    + context.getString(R.string.seconds);
            // Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Log.d( " Scanning did not start", e.getMessage());
        }
        handler.removeCallbacksAndMessages(null);
        Logd(TAG, "Starting Scanning again");
        // Will stop the scanning after a set time.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isCurrentlyScanning) {
                    Logd(TAG,"Start scanning again");
                    stopScanning();
                    startScanning();
                }
            }
        },SCAN_PERIOD);
    }



    public void stopScanning() {
        isCurrentlyScanning = false;
        // Stop the scan, wipe the callback.
        bluetoothScanner.stopScan(bluetoothScanCallback);
        scanResultArrayList = new ArrayList<>();
        bluetoothScanCallback = null;

        // Even if no new results, update 'last seen' times.
      //  bluetoothScanResultAdapter.notifyDataSetChanged();
    }

    /**
     * Return a List of {@link ScanFilter} objects to filter by Service UUID.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();

        ScanFilter.Builder builder = new ScanFilter.Builder();
        // Comment out the below line to see all BLE devices around you
        builder.setServiceUuid(Constants.Service_UUID);
        scanFilters.add(builder.build());

        return scanFilters;
    }

    /**
     * Return a {@link ScanSettings} object set to use low power (to preserve battery life).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ScanSettings buildScanSettings() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        // TO DO - check different scan modes and devices
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        // builder.setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES);
        // builder.setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE);
        return builder.build();
    }

    /**
     * Custom ScanCallback object - adds to adapter on success, displays error on failure.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class BluetoothScanCallback extends ScanCallback {

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);

            for (ScanResult result : results) {
                sendNotificationOnResult(result);
            }
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            sendNotificationOnResult(result);
        }


        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Toast.makeText(context, "Scan failed with error: " + errorCode, Toast.LENGTH_LONG)
                    .show();
        }
    }

        private int getPosition(String uuidInfo) {
            int position = -1;
            for (int i = 0; i < scanResultArrayList.size(); i++) {
                String currentInfo = new String(scanResultArrayList.get(i).getScanRecord().getServiceData().get(Constants.Service_UUID), StandardCharsets.UTF_8);
                if (currentInfo.equals(uuidInfo)) {
                    position = i;
                    break;
                }
            }
            return position;
        }

        private void sendNotificationOnResult(ScanResult result) {
            ScanRecord rec = result.getScanRecord();
            String uuidInfo = new String(rec.getServiceData().get(Constants.Service_UUID), StandardCharsets.UTF_8);
            int existingPosition = getPosition(uuidInfo);
            boolean isPresent = false;

            if (existingPosition >= 0) {
                // Device is already in list, update its record.
                //scanResultArrayList.set(existingPosition, scanResult);
                isPresent = true;
            } else {
                // Add new Device's ScanResult to list.
                scanResultArrayList.add(result);
            }

            String receivedStr = new String(rec.getServiceData().get(Constants.Service_UUID), StandardCharsets.UTF_8);

            String UUID = receivedStr.split("-")[0];
            int state = Integer.parseInt(receivedStr.split("-")[1]);

            dbRepo.insertTask(UUID, new Date(), state);
//        Logging.updateLog(UUID);

            // TODO: Add Notification when two devices come in contact according to different state
            if (!isPresent) {

                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                String channelId = context.getString(R.string.default_notification_channel_id);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(context, channelId)
                                .setSmallIcon(R.drawable.notif_icon)
                                .setContentTitle(context.getString(R.string.app_name))
                                .setContentText(context.getString(R.string.alert_message))
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setFullScreenIntent(pendingIntent, true)
                                .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }
        }
}
