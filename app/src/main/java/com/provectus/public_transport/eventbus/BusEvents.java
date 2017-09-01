package com.provectus.public_transport.eventbus;

import com.provectus.public_transport.model.TransportEntity;

/**
 * Created by Psihey on 18.08.2017.
 */

public class BusEvents {

    public static class DataBaseInitialized {
        public DataBaseInitialized() {
        }
    }

    public static class DataForCurrentRouteFetched {
        public DataForCurrentRouteFetched() {
        }
    }

    public static class SendChosenRouter {
        private TransportEntity mSelectRout;
        private boolean mCheckBoxState;

        public SendChosenRouter(TransportEntity selectRout, boolean checkBoxState) {
            this.mSelectRout = selectRout;
            this.mCheckBoxState = checkBoxState;
        }

        public TransportEntity getSelectRout() {
            return mSelectRout;
        }

        public boolean isCheckBoxState() {
            return mCheckBoxState;
        }

    }
}
