package com.dev.alarm.app;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class NotificationService extends Service {

    //common members
    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getCanonicalName());
    private final Calendar time = Calendar.getInstance();
    private Object monitor = new Object();

    //notification members
    private NotificationManager nm;
    private Timer timer = new Timer();
    private TimerTask task = null;
    private static int notificationId = 0;

    //location update members
    private LocationManager lm;
    private String provider = null;
    private LocationListener listener = new LocationListenerImpl();
    private volatile Location currentLocation = null;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        startLocationUpdates();
        Toast.makeText(this, "Service created at " + time.getTime(), Toast.LENGTH_LONG).show();
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.service_started);
        showNotification(text);
        startTimer();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cancel the persistent notification.
        timer.cancel();
        lm.removeUpdates(listener);
        nm.cancel(R.string.service_started);
        Toast.makeText(this, "Service destroyed at " + time.getTime(), Toast.LENGTH_LONG).show();
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification(CharSequence text) {

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, AlarmActivity.class), 0);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setAutoCancel(false);
        //        builder.setTicker("this is ticker text");
        builder.setContentTitle("Notification");
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setContentIntent(contentIntent);
        builder.setOngoing(true);
        builder.setSubText("This is subtext...");
        builder.setNumber(100);
        builder.build();

        Notification notication = builder.getNotification();
        this.nm.notify(++notificationId, notication);
        // Set the icon, scrolling text and timestamp
        //        Notification notification = new Notification(R.drawable.ic_stat_name, text,
        //                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        //        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        //                new Intent(this, AlarmActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        //        notification.setLatestEventInfo(this, getText(R.string.service_label),
        //                text, contentIntent);

        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        //        nm.notify(R.string.service_started, notification);
    }

    private void startLocationUpdates() {
        synchronized (monitor) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.NO_REQUIREMENT);
            provider = lm.getBestProvider(criteria, true);
            if (provider == null) {
                LOGGER.info(" no location provider");
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(provider, 5000L, -1, listener);
        }
    }

    private void startTimer() {
        task =  new TimerTask(){
            public void run() {
                synchronized (monitor) {
                    if(!lm.isProviderEnabled(provider)) {
                        showNotification("Location updates have stopped since "+ provider+" is not accessible");
                    }
                    if(currentLocation != null) {
                        showNotification(
                                "location changed"+
                                        AddressConvertUtil.convertLocationToAddress(
                                                getApplicationContext(), currentLocation));
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 7000L);
    }


    class LocationListenerImpl implements LocationListener {
        private Logger LOGGER = Logger.getLogger(LocationListenerImpl.class.getCanonicalName());

        @Override
        public void onLocationChanged(Location location) {
            LOGGER.info("Location update received");
            synchronized (monitor) {
                currentLocation = location;
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

    }

}
