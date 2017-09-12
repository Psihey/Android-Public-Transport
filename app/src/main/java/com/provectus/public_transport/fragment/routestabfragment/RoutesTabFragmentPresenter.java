package com.provectus.public_transport.fragment.routestabfragment;


public interface RoutesTabFragmentPresenter {
    /**
     * A method which binds a view to a presenter.
     * @param routesTabFragment a view whats binds to presenter
     */
    void bindView(RoutesTabFragment routesTabFragment);

    /**
     * A method which unbinds a view to a presenter.
     */
    void unbindView();
}
