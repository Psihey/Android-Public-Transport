package com.provectus.public_transport.fragment.mapfragment;


/**
 * Created by Psihey on 11.08.2017.
 */

public interface MapsFragmentPresenter {

    /**
     * A method which binds a view to a presenter.
     *
     * @param mapsFragment a view whats binds to presenter
     */
    void bindView(MapsFragment mapsFragment);

    /**
     * A method which unbinds a view to a presenter.
     */
    void unbindView();

}
