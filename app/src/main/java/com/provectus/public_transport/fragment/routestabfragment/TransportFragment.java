package com.provectus.public_transport.fragment.routestabfragment;

import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.service.TransportRoutesService;

import java.util.List;

public interface TransportFragment {
    /**
     * A method which creates recyclerview
     *
     * @param transportEntity a receiving collection for recyclerview
     */
    void initRecyclerView(List<TransportEntity> transportEntity);

    /**
     * A method which checks if service alive , and if not alive
     * restart {@link TransportRoutesService}
     */
    void checkMyServiceRunning();

    /**
     * A method which hides loading button, and show recyclerview
     */
    void serviceEndWorked();

    void updateData(TransportEntity transportEntity);
}
