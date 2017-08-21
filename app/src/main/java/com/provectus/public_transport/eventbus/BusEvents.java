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

        public SendRoutesEvent(List<TransportRoutes> mTransportRoutes, TransportType mType) {
            this.mTransportRoutes = mTransportRoutes;
            this.mType = mType;
        }

        public TransportType getTransportType() {
            return mType;
        }

        public List<TransportRoutes> getTransportRoutes() {
            return mTransportRoutes;
        }
    }

}
