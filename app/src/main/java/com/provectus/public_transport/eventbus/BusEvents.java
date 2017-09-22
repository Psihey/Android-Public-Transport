package com.provectus.public_transport.eventbus;

import com.provectus.public_transport.model.TransportEntity;


public class BusEvents {
    private BusEvents() {
    }

    public static class DataBaseInitialized {
        /**
         * A method which says to the subscriber that Database has initialized
         */
        public DataBaseInitialized() {
        }
    }

    public static class SendChosenRouter {
        private TransportEntity mSelectRout;

        public SendChosenRouter(TransportEntity selectRout) {
            this.mSelectRout = selectRout;
        }

        public TransportEntity getSelectRout() {
            return mSelectRout;
        }

    }

    public static class ServiceEndWorked {
        /**
         * A method which says to the subscriber that service has ended work
         */
        public ServiceEndWorked() {
        }
    }
}
