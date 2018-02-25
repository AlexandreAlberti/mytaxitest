package com.mytaxi.controller.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;

public class CarMapper
{
    public static CarDO makeCarDO(CarDTO driverDTO, ManufacturerRepository manufacturerRepository)
    {
        return new CarDO(
            driverDTO.getId(), driverDTO.getLicensePlate(), driverDTO.getSeatCount(), driverDTO.getConvertible(), driverDTO.getRating(), driverDTO.getEngineType(),
            driverDTO.getManufacturerId() == null ? null : manufacturerRepository.findOne(driverDTO.getManufacturerId()));
    }


    public static CarDTO makeCarDTO(CarDO driverDO)
    {
        CarDTO.CarDTOBuilder driverDTOBuilder = CarDTO.newBuilder()
            .setId(driverDO.getId())
            .setConvertible(driverDO.getConvertible())
            .setEngineType(driverDO.getEngineType())
            .setLicensePlate(driverDO.getLicensePlate())
            .setManufacturerId(driverDO.getManufacturer() != null ? driverDO.getManufacturer().getId() : null)
            .setManufacturerName(driverDO.getManufacturer()!= null ? driverDO.getManufacturer().getName() : null)
            .setRating(driverDO.getRating())
            .setSeatCount(driverDO.getSeatCount());
        return driverDTOBuilder.createCarDTO();
    }


    public static List<CarDTO> makeCarDTOList(Collection<CarDO> cars)
    {
        return cars.stream()
            .map(CarMapper::makeCarDTO)
            .collect(Collectors.toList());
    }
}
