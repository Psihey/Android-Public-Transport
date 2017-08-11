package com.provectus.public_transport;

/**
 * Created by Evgeniy on 8/10/2017.
 */

public interface Maps {

    interface View {

    }

    interface Presenter {

        void onBind(MapsFragmentImpl mapsFragment);

        void unBind();

    }


}
