package inspire2connect.inspire2connect.contactTracer.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import inspire2connect.inspire2connect.R;



public class FirebaseBroadcastReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = context.getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.notif_icon)
                        .setContentTitle(context.getString(R.string.fcm_message))
                        .setContentText(context.getString(R.string.alert_message))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setFullScreenIntent(pendingIntent,true)
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
