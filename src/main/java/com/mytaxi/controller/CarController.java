package com.mytaxi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;

/**
 * All operations with a car will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/cars")
public class CarController
{

    private final CarService carService;


    @Autowired
    public CarController(final CarService carService)
    {
        this.carService = carService;
    }


    @GetMapping("/{carId}")
    public CarDTO getCar(@Valid @PathVariable long carId) throws EntityNotFoundException
    {
        return carService.findDTO(carId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@Valid @RequestBody CarDTO carDTO) throws ConstraintsViolationException
    {
        return carService.createFromDTO(carDTO);
    }


    @PutMapping
    public CarDTO updateCar(@Valid @RequestBody CarDTO carDTO) throws ConstraintsViolationException
    {
        return carService.createFromDTO(carDTO);
    }


    @DeleteMapping("/{carId}")
    public void deleteCar(@Valid @PathVariable long carId) throws EntityNotFoundException
    {
        carService.delete(carId);
    }


    @GetMapping
    public List<CarDTO> findCars(@RequestParam(required = false) EngineType engineType, @RequestParam(required = false) Long manufacturerId)
        throws ConstraintsViolationException, EntityNotFoundException
    {
        if (engineType != null)
        {
            return CarMapper.makeCarDTOList(carService.findByEngineType(engineType));
        }
        else if (manufacturerId != null)
        {
            return CarMapper.makeCarDTOList(carService.findByManufacturerId(manufacturerId));
        }
        else
        {
            return CarMapper.makeCarDTOList(carService.findAll());
        }
    }
}
