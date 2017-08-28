package com.provectus.public_transport.utils;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.Random;

/**
 * Created by Evgeniy on 8/23/2017.
 */

public class Utils {

    private Utils() {
    }

    @ColorInt
    public static int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

}
