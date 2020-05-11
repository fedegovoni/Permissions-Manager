package com.federicogovoni.permissionmanager.utils;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.federicogovoni.permissionmanager.controller.location.LocationJobService;
import com.federicogovoni.permissionmanager.controller.location.LocationService;
import com.federicogovoni.permissionmanager.controller.location.OreoLocationListener;

import timber.log.Timber;

public class GeneralUtils {

    private static final String TAG = "GeneralUtils";
    public static final int JOB_ID = 1932;

    public static void checkAndStartLocationService(Context context) {

        if(GeneralUtils.isMyServiceRunning(LocationService.class, context)) {
            Timber.d("LocationService already running");
            return;
        }

        Log.d(TAG, "checkAndStartLocationService invoked");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Timber.d("start oreo location listener");
            new OreoLocationListener(context).startLocationUpdates();
        }
        else {
            Log.d(TAG, "Starting LocationService in background");
            Intent startServiceIntent = new Intent(context, LocationService.class);
            context.startService(startServiceIntent);
        }
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }
}