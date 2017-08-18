package com.provectus.public_transport.eventbus;

import com.provectus.public_transport.model.TransportRoutes;

import java.util.List;

/**
 * Created by Psihey on 18.08.2017.
 */

public class BusEvents {

    public static class SendRoutesEvent {
        private List<TransportRoutes> mTransportRoutes;

        public SendRoutesEvent(List<TransportRoutes> routes){
            this.mTransportRoutes = routes;
        }

        public List<TransportRoutes> getmTransportRoutes() {
            return mTransportRoutes;
        }
    }

}
