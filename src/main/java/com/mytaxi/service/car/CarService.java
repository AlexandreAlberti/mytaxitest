package com.mytaxi.service.car;

import java.util.List;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

public interface CarService
{

    CarDTO findDTO(Long carId) throws EntityNotFoundException;


    CarDTO createFromDTO(CarDTO carDTO) throws ConstraintsViolationException;


    void delete(Long carId) throws EntityNotFoundException;


    List<CarDO> findByEngineType(EngineType onlineStatus);


    List<CarDO> findByManufacturerId(Long manufacturerId);


    List<CarDO> findAll();


    CarDTO updateFromDTO(CarDTO carDTO) throws ConstraintsViolationException, EntityNotFoundException;

}
