package com.provectus.public_transport.eventbus;

import com.provectus.public_transport.model.TransportEntity;

import java.util.List;


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

    public static class OpenRouteInformation {
        private TransportEntity mSelectRout;

        public OpenRouteInformation(TransportEntity selectRoute) {
            this.mSelectRout = selectRoute;
        }

        public TransportEntity getSelectRout() {
            return mSelectRout;
        }
    }

    public static class DeleteFavourites {
        private TransportEntity mSelectRout;

        public DeleteFavourites(TransportEntity mSelectRout) {
            this.mSelectRout = mSelectRout;
        }

        public TransportEntity getSelectRout() {
            return mSelectRout;
        }
    }

    public static class UpdateDataTransportsRecyclerView {
        private TransportEntity mSelectRout;

        public UpdateDataTransportsRecyclerView(TransportEntity mSelectRout) {
            this.mSelectRout = mSelectRout;
        }

        public TransportEntity getSelectRout() {
            return mSelectRout;
        }
    }
    public static class UpdateDataFavouritesRecyclerView {
        private List<TransportEntity> mTransportData;

        public UpdateDataFavouritesRecyclerView(List<TransportEntity> mTransportData) {
            this.mTransportData = mTransportData;
        }

        public List<TransportEntity> getTransportData() {
            return mTransportData;
        }
    }
}
