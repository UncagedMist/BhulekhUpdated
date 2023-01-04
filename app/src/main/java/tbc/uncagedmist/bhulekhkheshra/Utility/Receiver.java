package tbc.uncagedmist.bhulekhkheshra.Utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import tbc.uncagedmist.bhulekhkheshra.MainActivity;
import tbc.uncagedmist.bhulekhkheshra.R;
import tbc.uncagedmist.bhulekhkheshra.SplashActivity;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendNotificationAPI26(context);
        }
        else    {
            sendNotification(context);
        }
    }

    private void sendNotificationAPI26(Context context) {
        String title = "भूलेख खाता खेसरा";
        String message = "अपना जमीन पंजीकरण और परिमार्जन अभी करें.";

        NotificationHelper helper;
        Notification.Builder builder;

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        helper = new NotificationHelper(context);
        builder = helper.getNotification(title,message,defaultSoundUri,contentIntent,true);

        helper.getManager().notify(1,builder.build());
    }

    private void sendNotification(Context context) {
        String title = "भूलेख खाता खेसरा";
        String message = "अपना जमीन पंजीकरण और परिमार्जन अभी करें.";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, SplashActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .setSound(defaultSoundUri);

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,builder.build());
    }
}