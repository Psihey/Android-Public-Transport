package com.provectus.public_transport.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;

import com.provectus.public_transport.R;
import com.provectus.public_transport.fragment.mapfragment.impl.MapsFragmentImpl;

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

    public static Bitmap drawVehicleDirection(MapsFragmentImpl context, float angle) {
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.temp_transport, myOptions);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int height = mutableBitmap.getHeight();
        int width = mutableBitmap.getWidth();

        Canvas canvas = new Canvas(mutableBitmap);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        if (angle > 30 && angle <= 60) {
            path.moveTo(width / 2, 0);
            path.lineTo(width, 0);
            path.lineTo(width, height / 2);
            path.close();
            canvas.drawPath(path, paint);
        } else if (angle > 60 && angle <= 120) {
            path.moveTo((float) (width / 1.4), (float) (height/5.7));
            path.lineTo(width, height / 2);
            path.lineTo((float) (width / 1.4), (float) (height/1.5));
            path.close();
            canvas.drawPath(path, paint);
        } else if (angle > 120 && angle <= 150) {
            path.moveTo(width, height / 2);
            path.lineTo(width, height);
            path.lineTo(width / 2, height);
            path.close();
            canvas.drawPath(path, paint);
        } else if (angle > 150 && angle <= 210) {
            path.moveTo(width/6, (float) (height / 1.4));
            path.lineTo(width / 2, height);
            path.lineTo((float) (width /1.2), (float) (height / 1.4));
            path.close();
            canvas.drawPath(path, paint);
        } else if (angle > 210 && angle <= 240) {
            path.moveTo(0, height / 2);
            path.lineTo(0, height);
            path.lineTo(width / 2, height);
            path.close();
            canvas.drawPath(path, paint);
        } else if (angle > 240 && angle <= 300) {
            path.moveTo(0, height / 2);
            path.lineTo((float) (width / 4), (float) (height/5.7));
            path.lineTo((float) (width / 4), (float) (height/1.5));
            path.close();
            canvas.drawPath(path, paint);
        } else if (angle > 300 && angle <= 330) {
            path.moveTo(0, 0);
            path.lineTo(width / 3, 0);
            path.lineTo(0, height / 2);
            path.close();
            canvas.drawPath(path, paint);
        } else if (angle > 330 || angle < 30) {
            path.moveTo(width/2, 0);
            path.lineTo(width/6, height/5);
            path.lineTo((float) (width/1.2), height/5);
            path.close();
            canvas.drawPath(path, paint);
        }

        return mutableBitmap;
    }


}
