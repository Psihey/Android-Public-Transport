package com.provectus.public_transport.eventbus;

import com.provectus.public_transport.fragment.favouritesfragment.FavouritesFragmentPresenter;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.fragment.routestabfragment.RoutesTabFragmentPresenter;
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

    public static class ServiceEndWorked {
        /**
         * A method which says to the subscriber that service has ended work
         */
        public ServiceEndWorked() {
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

    public static class SendMapsFragmentPresenter{
        private MapsFragmentPresenter mMapsFragmentPresenter;

        public SendMapsFragmentPresenter(MapsFragmentPresenter mMapsFragmentPresenter) {
            this.mMapsFragmentPresenter = mMapsFragmentPresenter;
        }

        public MapsFragmentPresenter getMapsFragmentPresenter() {
            return mMapsFragmentPresenter;
        }
    }

    public static class SendFavouriteFragmentPresenter{
        private FavouritesFragmentPresenter mFavouritesFragmentPresenter;

        public SendFavouriteFragmentPresenter(FavouritesFragmentPresenter mFavouritesFragmentPresenter) {
            this.mFavouritesFragmentPresenter = mFavouritesFragmentPresenter;
        }

        public FavouritesFragmentPresenter getFavouritesFragmentPresenter() {
            return mFavouritesFragmentPresenter;
        }
    }

    public static class SendRoutesTabFragmentPresenter{
        private RoutesTabFragmentPresenter mRoutesTabFragmentPresenter;

        public SendRoutesTabFragmentPresenter(RoutesTabFragmentPresenter mRoutesTabFragmentPresenter) {
            this.mRoutesTabFragmentPresenter = mRoutesTabFragmentPresenter;
        }

        public RoutesTabFragmentPresenter getRoutesTabFragmentPresenter() {
            return mRoutesTabFragmentPresenter;
        }
    }
}
