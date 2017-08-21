package com.provectus.public_transport.eventbus;

import com.provectus.public_transport.model.TransportRoutes;
import com.provectus.public_transport.model.TransportType;

import java.util.List;

/**
 * Created by Psihey on 18.08.2017.
 */

public class BusEvents {

    public static class SendRoutesEvent {

        private List<TransportRoutes> mTransportRoutes;
        private TransportType mType;

        public SendRoutesEvent(List<TransportRoutes> routes, TransportType type) {
            this.mTransportRoutes = routes;
            this.mType = type;
        }

        public List<TransportRoutes> getTransportRoutes() {
            return mTransportRoutes;
        }

        public TransportType getTransportType() {
            return mType;
        }
    }

}
