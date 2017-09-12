package com.provectus.public_transport.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.Random;

public class Utils {

    private Utils() {
    }

    @ColorInt
    public static int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.isAssignableFrom(service.service.getClass())) {
                return true;
            }
        }
        return false;
    }

}
