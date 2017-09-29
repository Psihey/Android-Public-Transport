package com.provectus.public_transport.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import com.provectus.public_transport.R;
import com.provectus.public_transport.fragment.mapfragment.impl.MapsFragmentImpl;

public class Utils {

    private Utils() {
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

    public static Bitmap drawVehicleDirection(MapsFragmentImpl context, float angle, String type) {
        Bitmap bitmap = null;
        if (angle == 0) {
            if (type.equals("tram")) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.temp_transport);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.temp_transport);
        } else if (angle > 30 && angle <= 60) {
            if (type.equals("tram")) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_north_east_direction_48_px_7);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_north_east);
        } else if (angle > 60 && angle <= 120) {
            if (type.equals("tram")) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_east_direction_48_px);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_east);
        } else if (angle > 120 && angle <= 150) {
            if (type.equals("tram")) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_south_east_direction_48_px_6);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_south_east);
        } else if (angle > 150 && angle <= 210) {
            if (type.equals("tram")) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_south_direction_48_px_2);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_south);
        } else if (angle > 210 && angle <= 240) {
            if (type.equals("tram")) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_south_west_direction_48_px_5);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_south_west);
        } else if (angle > 240 && angle <= 300) {
            if (type.equals("tram")) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_west_direction_48_px_3);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_west);
        } else if (angle > 300 && angle <= 330) {
            if (type.equals("tram")) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_north_west_direction_48_px_4);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_north_west);
        } else if (angle > 330 || angle <= 30) {
            if (type.equals("tram")) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_north_direction_48_px);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_north);
        }
        return bitmap;
    }

    public static Bitmap tintImage(Bitmap bitmap, int color) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        Bitmap bitmapResult = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap mutualBitmap = bitmapResult.copy(bitmapResult.getConfig(), true);
        Canvas canvas = new Canvas(mutualBitmap);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return mutualBitmap;
    }

}
