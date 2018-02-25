package com.mytaxi.dataaccessobject;

import org.springframework.data.repository.CrudRepository;

import com.mytaxi.domainobject.ManufacturerDO;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface ManufacturerRepository extends CrudRepository<ManufacturerDO, Long>
{

}
