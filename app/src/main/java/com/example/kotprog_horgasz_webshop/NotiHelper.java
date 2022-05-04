package com.example.kotprog_horgasz_webshop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class NotiHelper {
    private static final String CHANNEL_ID = "channel1";
    private final int NOTIFICATION_ID = 1;
    private NotificationManager notiManager;
    private Context ctx;


    public NotiHelper(Context context) {
        this.ctx = context;
        this.notiManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        NotificationChannel channel = new NotificationChannel
                (CHANNEL_ID, "Shop Notification", NotificationManager.IMPORTANCE_DEFAULT);
        notiManager.createNotificationChannel(channel);
    }

    public void send(String message) {
        Intent intent = new Intent(ctx, WebshopActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, NOTIFICATION_ID, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setContentTitle("Hohohohorgász Webshop")
                .setContentText("Sikeresen a kosárba raktad: "+message)
                .setSmallIcon(R.drawable.fishing_icon)
                .setContentIntent(pendingIntent);

        notiManager.notify(NOTIFICATION_ID, builder.build());
    }
}
