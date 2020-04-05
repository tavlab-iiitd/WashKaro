package inspire2connect.inspire2connect.contactTracer.bluetooth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.contactTracer.MainActivity;
import inspire2connect.inspire2connect.contactTracer.sqlDB.InteractionRepository;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;



public class BluetoothScanResultAdapter extends BaseAdapter {

    private ArrayList<ScanResult> scanResultArrayList;

    private Context mContext;

    private LayoutInflater layoutInflater;

    private InteractionRepository dbRepo;

    private int state;
    BluetoothScanResultAdapter(Context context, LayoutInflater inflater) {
        super();
        mContext = context;
        layoutInflater = inflater;
        scanResultArrayList = new ArrayList<>();
        dbRepo = new InteractionRepository(mContext.getApplicationContext());
    }

    @Override
    public int getCount() {
        return scanResultArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return scanResultArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return scanResultArrayList.get(position).getDevice().getAddress().hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // Reuse an old view if we can, otherwise create a new one.
        boolean isPresent = true;
        if (view == null) {
            isPresent = false;
            view = layoutInflater.inflate(R.layout.ca_listitem_scan_result, null);
        }

        TextView deviceNameView = view.findViewById(R.id.device_name);
        TextView deviceAddressView = view.findViewById(R.id.device_address);
        TextView lastSeenView = view.findViewById(R.id.last_seen);
        TextView deviceUuid = view.findViewById(R.id.unique_id);

        ScanResult scanResult = scanResultArrayList.get(position);

        ScanRecord rec = scanResult.getScanRecord();

        //TODO: Add Room DB in ScanResultAdapter
        String receivedStr = new String(rec.getServiceData().get(Constants.Service_UUID), StandardCharsets.UTF_8);

        String UUID = receivedStr.split("-")[0];
        state = Integer.parseInt(receivedStr.split("-")[1]);

        dbRepo.insertTask(UUID, new Date(), state);
//        Logging.updateLog(UUID);

        // TODO: Add Notification when two devices come in contact according to different state
        if (state != 0 && !isPresent) {

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = mContext.getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(mContext, channelId)
                            .setSmallIcon(R.drawable.notif_icon)
                            .setContentTitle(mContext.getString(R.string.fcm_message))
                            .setContentText(mContext.getString(R.string.covid_avengers_message))
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setFullScreenIntent(pendingIntent, true)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(mContext.getString(R.string.user_user_state_notif),
                        "ContactAlert",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
        }

        String name = scanResult.getDevice().getName();
        if (name == null) {
            name = mContext.getResources().getString(R.string.no_name);
        }
        deviceNameView.setText(name);
        deviceAddressView.setText(mContext.getString(R.string.contact_result) + " " + position+1);
        lastSeenView.setText(getTimeSinceString(mContext, scanResult.getTimestampNanos()));
        deviceUuid.setText(receivedStr);


        return view;
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



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void add(ScanResult scanResult) {

        ScanRecord rec = scanResult.getScanRecord();
        String uuidInfo = new String(rec.getServiceData().get(Constants.Service_UUID), StandardCharsets.UTF_8);
        int existingPosition = getPosition(uuidInfo);

        if (existingPosition >= 0) {
            // Device is already in list, update its record.
            scanResultArrayList.set(existingPosition, scanResult);
        } else {
            // Add new Device's ScanResult to list.
            scanResultArrayList.add(scanResult);
        }
    }


    public void clear() {
        scanResultArrayList.clear();
    }


    public static String getTimeSinceString(Context context, long timeNanoseconds) {
        String lastSeenText = context.getResources().getString(R.string.last_seen) + " ";

        long timeSince = SystemClock.elapsedRealtimeNanos() - timeNanoseconds;
        long secondsSince = TimeUnit.SECONDS.convert(timeSince, TimeUnit.NANOSECONDS);

        if (secondsSince < 5) {
            lastSeenText += context.getResources().getString(R.string.just_now);
        } else if (secondsSince < 60) {
            lastSeenText += secondsSince + " " + context.getResources()
                    .getString(R.string.seconds_ago);
        } else {
            long minutesSince = TimeUnit.MINUTES.convert(secondsSince, TimeUnit.SECONDS);
            if (minutesSince < 60) {
                if (minutesSince == 1) {
                    lastSeenText += minutesSince + " " + context.getResources()
                            .getString(R.string.minute_ago);
                } else {
                    lastSeenText += minutesSince + " " + context.getResources()
                            .getString(R.string.minutes_ago);
                }
            } else {
                long hoursSince = TimeUnit.HOURS.convert(minutesSince, TimeUnit.MINUTES);
                if (hoursSince == 1) {
                    lastSeenText += hoursSince + " " + context.getResources()
                            .getString(R.string.hour_ago);
                } else {
                    lastSeenText += hoursSince + " " + context.getResources()
                            .getString(R.string.hours_ago);
                }
            }
        }

        return lastSeenText;
    }
}

