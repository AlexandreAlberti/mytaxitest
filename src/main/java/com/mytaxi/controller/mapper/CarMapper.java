package com.mytaxi.controller.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;

public class CarMapper
{
    public static CarDO makeCarDO(CarDTO carDTO, ManufacturerRepository manufacturerRepository, DriverRepository driverRepository)
    {
        return new CarDO(
            carDTO.getId(), carDTO.getLicensePlate(), carDTO.getSeatCount(), carDTO.getConvertible(), carDTO.getRating(), carDTO.getEngineType(),
            carDTO.getManufacturerId() == null ? null : manufacturerRepository.findOne(carDTO.getManufacturerId()),
            carDTO.getDriverId() == null ? null : driverRepository.findOne(carDTO.getDriverId()));
    }


    public static CarDTO makeCarDTO(CarDO carDO)
    {
        CarDTO.CarDTOBuilder driverDTOBuilder = CarDTO.newBuilder()
            .setId(carDO.getId())
            .setConvertible(carDO.getConvertible())
            .setEngineType(carDO.getEngineType())
            .setLicensePlate(carDO.getLicensePlate())
            .setManufacturerId(carDO.getManufacturer() != null ? carDO.getManufacturer().getId() : null)
            .setManufacturerName(carDO.getManufacturer()!= null ? carDO.getManufacturer().getName() : null)
            .setRating(carDO.getRating())
            .setSeatCount(carDO.getSeatCount())
            .setDriverId(carDO.getDriver() != null ? carDO.getDriver().getId() : null);
        return driverDTOBuilder.createCarDTO();
    }


    public static List<CarDTO> makeCarDTOList(Collection<CarDO> cars)
    {
        return cars.stream()
            .map(CarMapper::makeCarDTO)
            .collect(Collectors.toList());
    }
}
