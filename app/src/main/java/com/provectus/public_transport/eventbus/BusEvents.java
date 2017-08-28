package com.provectus.public_transport.eventbus;

import com.provectus.public_transport.model.TransportEntity;

/**
 * Created by Psihey on 18.08.2017.
 */

public class BusEvents {

    public static class SendRoutesEvent {
        public SendRoutesEvent() {
        }
    }

    public static class SendRoutesErrorEvent{
        public SendRoutesErrorEvent() {
        }
    }

    public static class SendChosenRouter{
        private TransportEntity mTransportEntity;

        public SendChosenRouter(TransportEntity mTransportEntity) {
            this.mTransportEntity = mTransportEntity;
        }

        public TransportEntity getmTransportEntity() {
            return mTransportEntity;
        }
    }


}
