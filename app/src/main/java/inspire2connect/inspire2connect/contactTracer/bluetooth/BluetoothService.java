package inspire2connect.inspire2connect.contactTracer.bluetooth;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.contactTracer.BluetoothApplication;
import inspire2connect.inspire2connect.contactTracer.MainActivity;


public class BluetoothService extends Service {

    private static final Long TIMEOUT = Long.valueOf(2000);
    private Application bluetoothApplication = null;
    private static final String CHANNEL_ID = "BluetoothServiceChannel";
    private static final String TAG = "BluetoothService";
    private BluetoothScanner bluetoothScanner = null;
    private BluetoothAdvertiser bluetoothAdvertiser = null;

    public static final String ADVERTISE_FAILED =
            "com.example.android.bluetoothadvertisements.advertising_failed";



    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothApplication = (BluetoothApplication) getApplication();
        bluetoothAdvertiser = new BluetoothAdvertiser(bluetoothApplication.getApplicationContext(), BluetoothAdapter.getDefaultAdapter());
        bluetoothScanner = new BluetoothScanner(bluetoothApplication.getApplicationContext(), BluetoothAdapter.getDefaultAdapter());

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private void sendFailureIntent(int errorCode) {
        Intent failureIntent = new Intent();
        failureIntent.setAction(ADVERTISE_FAILED);
        // failureIntent.putExtra(ADVERTISE_FAIL_EXTRA_CODE, errorCode);
        sendBroadcast(failureIntent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            this.CreateChannelIfNeeded();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);


        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.notif_icon)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        startForeground(6, notification);
        bluetoothAdvertiser.startAdvertising();
        bluetoothScanner.startScanning();
        return START_STICKY;
    }

    private void CreateChannelIfNeeded() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        // running = false;
       bluetoothAdvertiser.stopAdvertising();
        bluetoothScanner.stopScanning();
        stopForeground(true);
        super.onDestroy();
    }
}

