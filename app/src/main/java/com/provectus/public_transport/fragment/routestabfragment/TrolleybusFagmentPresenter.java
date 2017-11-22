package com.provectus.public_transport.fragment.routestabfragment;

import com.provectus.public_transport.fragment.routestabfragment.impl.TrolleybusFragmentImpl;
import com.provectus.public_transport.model.TransportEntity;


public interface TrolleybusFagmentPresenter {
    /**
     * A method which binds a view to a presenter.
     *
     * @param trolleybusFragment a view whats binds to presenter
     */
    void bindView(TrolleybusFragmentImpl trolleybusFragment);

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
