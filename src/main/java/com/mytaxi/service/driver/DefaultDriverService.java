package com.mytaxi.service.driver;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.GeoCoordinate;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultDriverService implements DriverService
{

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private final DriverRepository driverRepository;
    private final CarRepository carRepository;


    public DefaultDriverService(final DriverRepository driverRepository, final CarRepository carRepository)
    {
        this.driverRepository = driverRepository;
        this.carRepository = carRepository;
    }


    /**
     * Selects a driver by id.
     *
     * @param driverId
     * @return found driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    public DriverDO find(Long driverId) throws EntityNotFoundException
    {
        return findDriverChecked(driverId);
    }


    /**
     * Creates a new driver.
     *
     * @param driverDO
     * @return
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException
    {
        DriverDO driver;
        try
        {
            driver = driverRepository.save(driverDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("Some constraints are thrown due to driver creation", e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return driver;
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long driverId) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setDeleted(true);
    }


    /**
     * Update the location for a driver.
     *
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
    }


    /**
     * Find all drivers by online state.
     *
     * @param onlineStatus
     */
    @Override
    public List<DriverDO> find(OnlineStatus onlineStatus)
    {
        return driverRepository.findByOnlineStatus(onlineStatus);
    }


    private DriverDO findDriverChecked(Long driverId) throws EntityNotFoundException
    {
        DriverDO driverDO = driverRepository.findOne(driverId);
        if (driverDO == null)
        {
            throw new EntityNotFoundException("Could not find entity with id: " + driverId);
        }
        return driverDO;
    }


    @Override
    public boolean selectCar(long driverId, long carId) throws EntityNotFoundException, CarAlreadyInUseException
    {
        DriverDO driverDO = driverRepository.findByIdAndOnlineStatus(driverId, OnlineStatus.ONLINE);
        if (driverDO == null)
        {
            throw new EntityNotFoundException("Could not find ONLINE driver with id: " + driverId);
        }

        // GET THE CAR
        CarDO carDO = carRepository.findOne(carId);
        if (carDO == null)
        {
            throw new EntityNotFoundException("Could not find car with id: " + carId);
        }
        if (carDO.getDriver() != null && !carDO.getDriver().getId().equals(driverId))
        {
            throw new CarAlreadyInUseException("Car is selected by driver with id: " + carDO.getDriver().getId() + " while your id is " + driverId);
        }

        if (driverDO.getSelectedCar() != null)
        {
            // ALREADY WITH A CAR
            CarDO c = driverDO.getSelectedCar();
            if (c.getId().equals(carId))
            {
                // IF IT IS THE SAME CAR, no ACTON, return false. FOR PERFORMANCE ISSUES, We don't want to access DB more than necessary
                return false;
            }
            else
            {
                // IF IT IS ANOTHER CAR, unselect the current.
                c.setDriver(null);
                try
                {
                    carRepository.save(c);
                }
                catch (DataIntegrityViolationException e)
                {
                    LOG.warn("Some constraints are thrown due to car driver change", e);
                    return false;
                }
            }
        }
        // PUT THE NEW DRIVER 
        carDO.setDriver(driverDO);
        try
        {
            carRepository.save(carDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("Some constraints are thrown due to car driver change", e);
            return false;
        }

        return true;
    }


    @Override
    public boolean deselectCar(long driverId, long carId) throws EntityNotFoundException
    {
        // CHECK DRIVER HAS A SELECTED CAR
        DriverDO driverDO = driverRepository.findOne(driverId);
        if (driverDO == null)
        {
            throw new EntityNotFoundException("Could not find driver with id: " + driverId);
        }

        if (driverDO.getSelectedCar() != null)
        {

            CarDO c = driverDO.getSelectedCar();
            // IF THE SELECTED CAR IS THE ONE WE SAID TO DESELECT
            if (c.getId().equals(carId))
            {
                c.setDriver(null);
                try
                {
                    carRepository.save(c);
                }
                catch (DataIntegrityViolationException e)
                {
                    LOG.warn("Some constraints are thrown due to car driver change", e);
                    return false;
                }

                return true;
            }
        }
        // IF ANYTHING WAS NOT PURE OK, return false.
        return false;
    }

}
