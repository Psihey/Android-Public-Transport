package com.provectus.public_transport.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Psihey on 15.08.2017.
 */

public enum TransportType {

    @SerializedName("tram")
    TRAM_TYPE,
    @SerializedName("trolleybuses")
    TROLLEYBUSES_TYPE,
    @SerializedName("taxi")
    PARKING_TYPE;

}
