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

    public static class SendChosenRoute {
        private TransportEntity mSelectRout;

        public SendChosenRoute(TransportEntity selectRout) {
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

    public static class OpenRouteInformation{
        private TransportEntity mSelectRout;

        public OpenRouteInformation(TransportEntity selectRoute){
            this.mSelectRout = selectRoute;
        }

        public TransportEntity getmSelectRout() {
            return mSelectRout;
        }
    }
}
