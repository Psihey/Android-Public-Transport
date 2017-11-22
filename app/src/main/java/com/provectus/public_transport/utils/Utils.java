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
import com.provectus.public_transport.model.converter.TransportType;

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

    public static Bitmap drawVehicleDirection(MapsFragmentImpl context, float angle, TransportType type) {
        Bitmap bitmap = null;
        if (angle == 0) {
            if (type == TransportType.TRAM_TYPE) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_static_direction);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_static_direction);
        } else if (angle > 30 && angle <= 60) {
            if (type == TransportType.TRAM_TYPE) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_north_east_direction);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_north_east_direction);
        } else if (angle > 60 && angle <= 120) {
            if (type == TransportType.TRAM_TYPE) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_east_direction);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_east_direction);
        } else if (angle > 120 && angle <= 150) {
            if (type == TransportType.TRAM_TYPE) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_south_east_direction);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_south_east_direction);
        } else if (angle > 150 && angle <= 210) {
            if (type == TransportType.TRAM_TYPE) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_south_direction);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_south_direction);
        } else if (angle > 210 && angle <= 240) {
            if (type == TransportType.TRAM_TYPE) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_south_west_direction);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_south_west_direction);
        } else if (angle > 240 && angle <= 300) {
            if (type == TransportType.TRAM_TYPE) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_west_direction);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_west_direction);
        } else if (angle > 300 && angle <= 330) {
            if (type == TransportType.TRAM_TYPE) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_north_west_direction);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_north_west_direction);
        } else if (angle > 330 || angle <= 30) {
            if (type == TransportType.TRAM_TYPE) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tram_north_direction);
            } else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trolleybus_north_direction);
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
