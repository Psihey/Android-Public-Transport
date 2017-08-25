package com.provectus.public_transport.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Evgeniy on 8/10/2017.
 */

public final class Const {

    private Const() {
    }

   public interface DefaultCameraPosition {
        LatLng ODESSA_FIRST_POINTS = new LatLng(46.348612, 30.671341);
        LatLng ODESSA_SECOND_POINTS = new LatLng(46.499907, 30.781572);
        Integer ZOOM_ON_MAP = 30;
    }

}
