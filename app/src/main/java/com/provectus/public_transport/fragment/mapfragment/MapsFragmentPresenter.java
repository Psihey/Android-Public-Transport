package com.provectus.public_transport.fragment.mapfragment;


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

    /**
     * A method which unregistered Event Bus event
     */
    void unregisteredEventBus();
}
