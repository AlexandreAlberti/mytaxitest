package com.mytaxi.dataaccessobject;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface DriverRepository extends CrudRepository<DriverDO, Long>
{

    DriverDO findByIdAndOnlineStatus(Long id, OnlineStatus onlineStatus);
    List<DriverDO> findByOnlineStatus(OnlineStatus onlineStatus);
    DriverDO findByUsernameAndPassword(String username, String password);
}
