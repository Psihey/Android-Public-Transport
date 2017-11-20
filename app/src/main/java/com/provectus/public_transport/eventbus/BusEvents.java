package com.provectus.public_transport.eventbus;

import com.provectus.public_transport.adapter.TramsAndTrolleyAdapter;
import com.provectus.public_transport.fragment.routestabfragment.impl.TramFragmentPresenterImpl;
import com.provectus.public_transport.fragment.routestabfragment.impl.TrolleybusFragmentPresenterImpl;
import com.provectus.public_transport.fragment.favouritesfragment.FavouritesFragmentPresenter;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
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

    public static class SendMapsFragmentPresenter {
        private MapsFragmentPresenter mMapsFragmentPresenter;

        public SendMapsFragmentPresenter(MapsFragmentPresenter mMapsFragmentPresenter) {
            this.mMapsFragmentPresenter = mMapsFragmentPresenter;
        }

        public MapsFragmentPresenter getMapsFragmentPresenter() {
            return mMapsFragmentPresenter;
        }
    }

    public static class SendFavouriteFragmentPresenter {
        private FavouritesFragmentPresenter mFavouritesFragmentPresenter;

        public SendFavouriteFragmentPresenter(FavouritesFragmentPresenter mFavouritesFragmentPresenter) {
            this.mFavouritesFragmentPresenter = mFavouritesFragmentPresenter;
        }

        public FavouritesFragmentPresenter getFavouritesFragmentPresenter() {
            return mFavouritesFragmentPresenter;
        }
    }

    public static class SendTramFragmentPresenter {
        private TramFragmentPresenterImpl mTramFragmentPresenter;

        public SendTramFragmentPresenter(TramFragmentPresenterImpl mRoutesTabFragmentPresenter) {
            this.mTramFragmentPresenter = mRoutesTabFragmentPresenter;
        }

        public TramFragmentPresenterImpl getRoutesTabFragmentPresenter() {
            return mTramFragmentPresenter;
        }
    }

    public static class SendTrolleybusFragmentPresenter {
        private TrolleybusFragmentPresenterImpl trolleybusFragmentPresenter;

        public SendTrolleybusFragmentPresenter(TrolleybusFragmentPresenterImpl trolleybusFragmentPresenter) {
            this.trolleybusFragmentPresenter = trolleybusFragmentPresenter;
        }

        public TrolleybusFragmentPresenterImpl getTrolleybusFragmentPresenter() {
            return trolleybusFragmentPresenter;
        }
    }

    public static class UnselectedAllItems {

        public UnselectedAllItems() {

        }
    }

    public static class SendTramsAndTrolleyAdapter {
        private TramsAndTrolleyAdapter mTramsAndTrolleyAdapter;
        private int type;

        public SendTramsAndTrolleyAdapter(TramsAndTrolleyAdapter tramsAndTrolleyAdapter, int type) {
            this.mTramsAndTrolleyAdapter = tramsAndTrolleyAdapter;
            this.type = type;
        }

        public TramsAndTrolleyAdapter getTramsAndTrolleyAdapter() {
            return mTramsAndTrolleyAdapter;
        }

        public int getType() {
            return type;
        }
    }

    public static class ServerNotResponding {

        public ServerNotResponding() {

        }
    }
}

