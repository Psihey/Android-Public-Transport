package com.provectus.public_transport.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Psihey on 18.08.2017.
 */

public class BusProvider {

    private static EventBus sEventBus;

    private BusProvider() {
    }

    public static EventBus getBus() {
        if (sEventBus == null)
            sEventBus = EventBus.getDefault();
        return sEventBus;
    }
}
