package architecture.geyerk.sensorlab.firsteap;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class service extends Service {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    BroadcastReceiver broadcastReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callNotification();
        initializePreferences();
        initializeBroadcastReceiver();
        return START_STICKY;
    }

    private void callNotification() {
        Notification note = initializeService();
        startForeground(101, note);
    }

    private void initializePreferences() {
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        editor = prefs.edit();
        editor.apply();
    }
   
    private Notification initializeService() {
        String contentTitle = "ecological assessment psych app",
                contentText = "ecological assessment psych app is recording data";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("ecological assessment psych app", getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setSound(null, null);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setShowBadge(true);
            if (manager != null) { manager.createNotificationChannel(channel); }
        }
        NotificationCompat.Builder nfc = new NotificationCompat.Builder(getApplicationContext(), "ecological assessment psych app")
                .setSmallIcon(R.drawable.ic_prospective_logger)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_prospective_logger))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET) //This hides the notification from lock screen
                .setContentTitle(contentTitle)
                .setContentText("Usage Logger is collecting data")
                .setOngoing(true);
        nfc.setContentTitle(contentTitle);
        nfc.setContentText(contentText);
        nfc.setStyle(new NotificationCompat.BigTextStyle().bigText(contentText).setBigContentTitle(contentTitle));
        nfc.setWhen(System.currentTimeMillis());
        return nfc.build();
    }

    private void initializeBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction() != null)
                    switch (intent.getAction()){
                        case Intent.ACTION_SCREEN_ON: editor.putLong("screen on since", System.currentTimeMillis()).apply();
                            break;
                        case Intent.ACTION_SCREEN_OFF:
                            long screenOnSince = prefs.getLong("screen on since", 0);
                            if(screenOnSince != 0){ editor.putLong("total screen on", (prefs.getLong("total screen on", 0) + System.currentTimeMillis() - screenOnSince)).apply(); }
                    }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
