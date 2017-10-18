package com.provectus.public_transport.fragment.routestabfragment;


import com.provectus.public_transport.fragment.routestabfragment.impl.TramFragmentImpl;
import com.provectus.public_transport.model.TransportEntity;

public interface TramFragmentPresenter {
    /**
     * A method which binds a view to a presenter.
     *
     * @param tramFragment a view whats binds to presenter
     */
    void bindView(TramFragmentImpl tramFragment);

    /**
     * A method which unbinds a view to a presenter.
     */
    void unbindView();

    /**
     * A method which unregistered Event Bus event
     */
    void unregisteredEventBus();

    void getDataForUpdateRecyclerView(TransportEntity transportEntity);

}