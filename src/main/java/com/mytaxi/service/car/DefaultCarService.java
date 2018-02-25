package com.mytaxi.service.car;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.criteria.CarCriteriaFilterConvertible;
import com.mytaxi.service.car.criteria.CarCriteriaFilterEngineType;
import com.mytaxi.service.car.criteria.CarCriteriaFilterLicensePlate;
import com.mytaxi.service.car.criteria.CarCriteriaFilterManufacturer;
import com.mytaxi.service.car.criteria.CarCriteriaFilterSeats;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some car specific things.
 * <p/>
 */
@Service
public class DefaultCarService implements CarService
{

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

    private final CarRepository carRepository;
    private final DriverRepository driverRepository;
    private final ManufacturerRepository manufacturerRepository;


    public DefaultCarService(final CarRepository carRepository, final ManufacturerRepository manufacturerRepository, final DriverRepository driverRepository)
    {
        this.carRepository = carRepository;
        this.driverRepository = driverRepository;
        this.manufacturerRepository = manufacturerRepository;
    }


    //    /**
    //     * Selects a car by id.
    //     *
    //     * @param carId
    //     * @return found car
    //     * @throws EntityNotFoundException if no car with the given id was found.
    //     */
    //    @Override
    //    private CarDO find(Long carId) throws EntityNotFoundException
    //    {
    //        return findCarChecked(carId);
    //    }

    /**
     * Creates a new car.
     *
     * @param carDO
     * @return
     * @throws ConstraintsViolationException if a car already exists with the given username, ... .
     */
    //    @Override
    private CarDO create(CarDTO carDTO) throws ConstraintsViolationException
    {
        CarDO car = CarMapper.makeCarDO(carDTO, manufacturerRepository, driverRepository);
        try
        {
            car = carRepository.save(car);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("Some constraints are thrown due to car creation", e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        catch (Exception e)
        {
            LOG.warn("Some error is thrown due to car creation", e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }


    /**
     * Deletes an existing car by id.
     *
     * @param carId
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long carId) throws EntityNotFoundException
    {
        CarDO carDO = findCarChecked(carId);
        carRepository.delete(carDO);
    }


    //    /**
    //     * Find all cars by engine type.
    //     *
    //     * @param engineType
    //     */
    //    @Override
    //    public List<CarDO> findByEngineType(EngineType engineType)
    //    {
    //        return carRepository.findByEngineType(engineType);
    //    }
    //
    //
    //    /**
    //     * Find all cars by manufacturer id.
    //     *
    //     * @param manufacturerId
    //     */
    //    @Override
    //    public List<CarDO> findByManufacturerId(Long manufacturerId)
    //    {
    //        return carRepository.findByManufacturer_Id(manufacturerId);
    //    }

    /**
     * Find all cars.
     * This pattern may not be the fastest at first sight, taking into account that the car database may be incredibly large. 
     * Using cache techniques, you may get easily all records in much less time, than asking lots of query's. 
     * Bottle neck may be on Memory/CPU, rather than DB for this case. 
     */
    @Override
    public List<CarDO> findAll(Boolean convertible, EngineType eType, String licensePlate, Long manufacturerId, Integer seatCount)
    {
        Iterable<CarDO> findAll = carRepository.findAll();
        List<CarDO> result = new ArrayList<>();
        findAll.forEach(result::add);

        if (convertible != null)
        {
            result = new CarCriteriaFilterConvertible().meetCriteria(result, convertible);
        }
        if (eType != null)
        {
            result = new CarCriteriaFilterEngineType().meetCriteria(result, eType);
        }
        if (licensePlate != null)
        {
            result = new CarCriteriaFilterLicensePlate().meetCriteria(result, licensePlate);
        }
        if (manufacturerId != null)
        {
            result = new CarCriteriaFilterManufacturer().meetCriteria(result, manufacturerId);
        }
        if (seatCount != null)
        {
            result = new CarCriteriaFilterSeats().meetCriteria(result, seatCount);
        }

        return result;
    }


    private CarDO findCarChecked(Long carId) throws EntityNotFoundException
    {
        CarDO carDO = carRepository.findOne(carId);
        if (carDO == null)
        {
            throw new EntityNotFoundException("Could not find entity with id: " + carId);
        }
        return carDO;
    }


    @Override
    public CarDTO findDTO(Long carId) throws EntityNotFoundException
    {
        return CarMapper.makeCarDTO(findCarChecked(carId));
    }


    @Override
    public CarDTO createFromDTO(CarDTO carDTO) throws ConstraintsViolationException
    {
        if (carDTO.getId() != null && carDTO.getId().longValue() > 0L)
        {
            throw new ConstraintsViolationException("Could not save NEW car due to existing id: " + carDTO.getId());
        }

        CarDO carDO = create(carDTO);
        return CarMapper.makeCarDTO(carDO);
    }


    @Override
    public CarDTO updateFromDTO(CarDTO carDTO) throws ConstraintsViolationException, EntityNotFoundException
    {
        if (carDTO.getId() == null || carDTO.getId().longValue() <= 0L)
        {
            throw new EntityNotFoundException("Could not find entity with id: " + carDTO.getId());
        }

        CarDO carDO = create(carDTO);

        return CarMapper.makeCarDTO(carDO);
    }

}
