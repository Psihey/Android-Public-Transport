package com.provectus.public_transport.eventbus;

import com.provectus.public_transport.model.TransportEntity;


public class BusEvents {
    private BusEvents() {
    }

    public static class DataBaseInitialized {
        public DataBaseInitialized() {
            // Say to the subscriber that Database have initialized
        }
    }

    public static class DataForCurrentRouteFetched {
        public DataForCurrentRouteFetched() {
            //Say to the subscriber that data have fetched from Database and we can connect them
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
        public ServiceEndWorked() {
            // Say to the subscriber that service have ended work
        }
    }
}
