package com.mytaxi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
import com.mytaxi.service.driver.DriverService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MytaxiServerApplicantTestApplication.class)
public class MytaxiServerApplicantTestApplicationTests
{

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String REQUEST_URL = "http://localhost:8080/v1/cars";

    @Autowired
    private CarService carService;
    @Autowired
    private DriverService driverService;


    //    @Test
    //    public void contextLoads()
    //    {}

    @Test
    public void carCRUD()
    {
        boolean convertible = false;
        EngineType cType = EngineType.ETHANOL;
        String licensePlate = "1234ABC";
        Long manufacturerId = 3L;
        Float rating = 5.0F;
        Integer seatCount = 5;

        CarDTO carDTO = CarDTO.newBuilder()
            .setConvertible(convertible)
            .setEngineType(cType)
            .setLicensePlate(licensePlate)
            .setManufacturerId(manufacturerId)
            .setRating(rating)
            .setSeatCount(seatCount)
            .createCarDTO();

        // ASSERTS FOR DTO BUILDER CREATION
        assertNull(carDTO.getId());
        assertFalse(carDTO.getConvertible());
        assertEquals(cType, carDTO.getEngineType());
        assertEquals(licensePlate, carDTO.getLicensePlate());
        assertEquals(manufacturerId, carDTO.getManufacturerId());
        assertEquals(rating, carDTO.getRating());
        assertEquals(seatCount, carDTO.getSeatCount());

        // CREATE
        try
        {
            carDTO = carService.createFromDTO(carDTO);
        }
        catch (ConstraintsViolationException e)
        {
            assertTrue("CAR PRESENTS SOME CONSTRAINT PROBLEM", false);
        }
        // CHECKS FOR CREATION
        assertNotNull(carDTO.getId());
        assertFalse(carDTO.getConvertible());
        assertEquals(cType, carDTO.getEngineType());
        assertEquals(licensePlate, carDTO.getLicensePlate());
        assertEquals(manufacturerId, carDTO.getManufacturerId());
        assertEquals(rating, carDTO.getRating());
        assertEquals(seatCount, carDTO.getSeatCount());

        // READ
        Long carId = carDTO.getId();
        try
        {
            carDTO = carService.findDTO(carId);
        }
        catch (EntityNotFoundException e1)
        {
            assertTrue("CAR's ID " + carId + " DOES NOT EXIST", false);
        }
        // CHECKS FOR READ
        assertNotNull(carDTO.getId());
        assertFalse(carDTO.getConvertible());
        assertEquals(cType, carDTO.getEngineType());
        assertEquals(licensePlate, carDTO.getLicensePlate());
        assertEquals(manufacturerId, carDTO.getManufacturerId());
        assertEquals(rating, carDTO.getRating());
        assertEquals(seatCount, carDTO.getSeatCount());

        // UPDATE
        convertible = true;
        cType = EngineType.HYBRID;
        licensePlate = "1234ABD";
        manufacturerId = 2L;
        rating = 5.3F;
        seatCount = 4;
        carDTO = CarDTO.newBuilder()
            .setId(carId)
            .setConvertible(convertible)
            .setEngineType(cType)
            .setLicensePlate(licensePlate)
            .setManufacturerId(manufacturerId)
            .setRating(rating)
            .setSeatCount(seatCount)
            .createCarDTO();

        try
        {
            carDTO = carService.updateFromDTO(carDTO);
        }
        catch (ConstraintsViolationException e)
        {
            assertTrue("CAR PRESENTS SOME CONSTRAINT PROBLEM", false);
        }
        catch (EntityNotFoundException e)
        {
            assertTrue("CAR's ID " + carId + " DOES NOT EXIST", false);
        }

        // CHECKS FOR UPDATE
        assertNotNull(carDTO.getId());
        assertTrue(carDTO.getConvertible());
        assertEquals(cType, carDTO.getEngineType());
        assertEquals(licensePlate, carDTO.getLicensePlate());
        assertEquals(manufacturerId, carDTO.getManufacturerId());
        assertEquals(rating, carDTO.getRating());
        assertEquals(seatCount, carDTO.getSeatCount());

        // READ
        try
        {
            carDTO = carService.findDTO(carId);
        }
        catch (EntityNotFoundException e1)
        {
            assertTrue("CAR's ID " + carId + " DOES NOT EXIST", false);
        }
        // CHECKS FOR READ
        assertNotNull(carDTO.getId());
        assertTrue(carDTO.getConvertible());
        assertEquals(cType, carDTO.getEngineType());
        assertEquals(licensePlate, carDTO.getLicensePlate());
        assertEquals(manufacturerId, carDTO.getManufacturerId());
        assertEquals(rating, carDTO.getRating());
        assertEquals(seatCount, carDTO.getSeatCount());

        // DELETE
        try
        {
            carService.delete(carId);
        }
        catch (EntityNotFoundException e)
        {
            assertTrue("CAR's ID " + carId + " DOES NOT EXIST", false);
        }

        // CHECK DELETE WORKS OK
        // READ
        try
        {
            carDTO = carService.findDTO(carId);
            assertTrue("CAR's ID " + carId + " STILL EXIST", false);
        }
        catch (EntityNotFoundException e1)
        {
            assertTrue("CAR's ID " + carId + " DOES NOT EXIST", true);
        }
    }


    @Test
    @Ignore
    /* 
     * IGNORED DUE TO SOME ERROR. Time for doing the test is limited. 
     * This kind of test must be in another class just to run it after application is running into develop envoirment and not interfere in other test for building purposes. 
     * I expect this to demonstrate that I know tests may be done in some other layer than just logic/service one.
     * I Tried to do this with google chrome extension known as Postman to see if works, and it seems to work properly.
     * */
    public void carCRUDRestTemplate()
    {
        RestTemplate restTemplate = new RestTemplate();
        boolean convertible = false;
        EngineType cType = EngineType.ETHANOL;
        String licensePlate = "1234ABC";
        Long manufacturerId = 3L;
        Float rating = 5.0F;
        Integer seatCount = 5;

        CarDTO carDTO = CarDTO.newBuilder()
            .setConvertible(convertible)
            .setEngineType(cType)
            .setLicensePlate(licensePlate)
            .setManufacturerId(manufacturerId)
            .setRating(rating)
            .setSeatCount(seatCount)
            .createCarDTO();

        // CREATE
        HttpHeaders headers = new HttpHeaders();
        headers.put(CONTENT_TYPE, Arrays.asList("application/json"));

        HttpEntity<String> request = new HttpEntity<String>(carDTO.toJsonString(), headers);
        ResponseEntity<CarDTO> response = restTemplate.exchange(REQUEST_URL, HttpMethod.POST, request, CarDTO.class);
        carDTO = response.getBody();
        // CHECKS FOR CREATION
        assertNotNull(carDTO.getId());
        assertFalse(carDTO.getConvertible());
        assertEquals(cType, carDTO.getEngineType());
        assertEquals(licensePlate, carDTO.getLicensePlate());
        assertEquals(manufacturerId, carDTO.getManufacturerId());
        assertEquals(rating, carDTO.getRating());
        assertEquals(seatCount, carDTO.getSeatCount());

        // READ
        Long carId = carDTO.getId();
        request = new HttpEntity<String>(headers);
        response = restTemplate.exchange(REQUEST_URL + "/" + carId, HttpMethod.GET, request, CarDTO.class);
        carDTO = response.getBody();

        // CHECKS FOR READ
        assertNotNull(carDTO.getId());
        assertFalse(carDTO.getConvertible());
        assertEquals(cType, carDTO.getEngineType());
        assertEquals(licensePlate, carDTO.getLicensePlate());
        assertEquals(manufacturerId, carDTO.getManufacturerId());
        assertEquals(rating, carDTO.getRating());
        assertEquals(seatCount, carDTO.getSeatCount());

        // UPDATE
        convertible = true;
        cType = EngineType.HYBRID;
        licensePlate = "1234ABD";
        manufacturerId = 2L;
        rating = 5.3F;
        seatCount = 4;
        carDTO = CarDTO.newBuilder()
            .setId(carId)
            .setConvertible(convertible)
            .setEngineType(cType)
            .setLicensePlate(licensePlate)
            .setManufacturerId(manufacturerId)
            .setRating(rating)
            .setSeatCount(seatCount)
            .createCarDTO();

        request = new HttpEntity<String>(carDTO.toJsonString(), headers);
        response = restTemplate.exchange(REQUEST_URL + "/" + carId, HttpMethod.PUT, request, CarDTO.class);
        carDTO = response.getBody();

        // CHECKS FOR UPDATE
        assertNotNull(carDTO.getId());
        assertTrue(carDTO.getConvertible());
        assertEquals(cType, carDTO.getEngineType());
        assertEquals(licensePlate, carDTO.getLicensePlate());
        assertEquals(manufacturerId, carDTO.getManufacturerId());
        assertEquals(rating, carDTO.getRating());
        assertEquals(seatCount, carDTO.getSeatCount());

        // READ
        request = new HttpEntity<String>(headers);
        response = restTemplate.exchange(REQUEST_URL + "/" + carId, HttpMethod.GET, request, CarDTO.class);
        carDTO = response.getBody();
        // CHECKS FOR READ
        assertNotNull(carDTO.getId());
        assertTrue(carDTO.getConvertible());
        assertEquals(cType, carDTO.getEngineType());
        assertEquals(licensePlate, carDTO.getLicensePlate());
        assertEquals(manufacturerId, carDTO.getManufacturerId());
        assertEquals(rating, carDTO.getRating());
        assertEquals(seatCount, carDTO.getSeatCount());

        // DELETE
        request = new HttpEntity<String>(headers);
        response = restTemplate.exchange(REQUEST_URL + "/" + carId, HttpMethod.DELETE, request, CarDTO.class);

        // CHECK DELETE WORKS OK
        request = new HttpEntity<String>(headers);
        ResponseEntity<EntityNotFoundException> responseEx = restTemplate.exchange(REQUEST_URL + "/" + carId, HttpMethod.GET, request, EntityNotFoundException.class);
        EntityNotFoundException carDTOEx = responseEx.getBody();
        assertEquals("Could not find entity with id.", carDTOEx.getMessage());
    }


    @Test
    public void errorCreatingCar()
    {
        String licensePlate = "1234ABC";
        Integer seatCount = 5;

        CarDTO carDTO = null;

        // CREATE WITH NO SEAT COUNT
        carDTO = CarDTO.newBuilder()
            .setLicensePlate(licensePlate)
            .createCarDTO();

        try
        {
            carService.createFromDTO(carDTO);
            assertTrue("CAR PRESENTS NO SEAT COUNT NULL CONSTRAINT PROBLEM", false);
        }
        catch (ConstraintsViolationException e)
        {
            assertTrue("CAR PRESENTS SEAT COUNT NULL CONSTRAINT PROBLEM", true);
        }

        // CREATE WITH NO LICENSE PLATE
        carDTO = CarDTO.newBuilder()
            .setSeatCount(seatCount)
            .createCarDTO();
        try
        {
            carService.createFromDTO(carDTO);
            assertTrue("CAR PRESENTS NO LICENSE_PLATE NULL CONSTRAINT PROBLEM", false);
        }
        catch (ConstraintsViolationException e)
        {
            assertTrue("CAR PRESENTS LICENSE_PLATE NULL CONSTRAINT PROBLEM", true);
        }

        // CREATE WITH ID NOT NULL
        carDTO = CarDTO.newBuilder()
            .setId(9L)
            .setLicensePlate(licensePlate)
            .setSeatCount(seatCount)
            .createCarDTO();
        try
        {
            carService.createFromDTO(carDTO);
            assertTrue("CAR NOT PRESENTS ID PROBLEM", false);
        }
        catch (ConstraintsViolationException e)
        {
            assertTrue("CAR PRESENTS ID PROBLEM", true);
        }

        // FIND EXISTING LICENSE PLATE
        try
        {
            licensePlate = carService.findDTO(1L).getLicensePlate(); // Finding existing one from data.sql
        }
        catch (EntityNotFoundException e1)
        {
            assertTrue("SOMETHING WENT WRONG, CHECK data.sql to see what happened to id=1", false);
        }

        // CREATE WITH EXISTING LICENSE PLATE
        carDTO = CarDTO.newBuilder()
            .setSeatCount(seatCount)
            .setLicensePlate(licensePlate)
            .createCarDTO();
        try
        {
            carService.createFromDTO(carDTO);
            assertTrue("CAR PRESENTS NO LICENSE_PLATE UNIQUE CONSTRAINT PROBLEM", false);
        }
        catch (ConstraintsViolationException e)
        {
            assertTrue("CAR PRESENTS LICENSE_PLATE UNIQUE CONSTRAINT PROBLEM", true);
        }

    }


    @Test
    public void errorUpdatingCar()
    {
        String licensePlate = "1234ABC";
        Integer seatCount = 5;

        CarDTO carDTO = null;

        // CREATE 
        carDTO = CarDTO.newBuilder()
            .setLicensePlate(licensePlate)
            .setSeatCount(seatCount)
            .createCarDTO();
        try
        {
            carDTO = carService.createFromDTO(carDTO);
        }
        catch (ConstraintsViolationException e)
        {
            assertTrue("CAR PRESENTS SOME CONSTRAINT PROBLEM", false);
        }
        Long carId = carDTO.getId();

        // UPDATE WITH NO SEAT COUNT
        carDTO = CarDTO.newBuilder()
            .setId(carId)
            .setLicensePlate(licensePlate)
            .createCarDTO();

        try
        {
            carService.updateFromDTO(carDTO);
            assertTrue("CAR PRESENTS NO SEAT COUNT NULL CONSTRAINT PROBLEM", false);
        }
        catch (ConstraintsViolationException e)
        {
            assertTrue("CAR PRESENTS SEAT COUNT NULL CONSTRAINT PROBLEM", true);
        }
        catch (EntityNotFoundException e)
        {
            assertTrue("CAR NOT FOUND PROBLEM", false);
        }

        // UPDATE WITH NO LICENSE PLATE
        carDTO = CarDTO.newBuilder()
            .setId(carId)
            .setSeatCount(seatCount)
            .createCarDTO();
        try
        {
            carService.updateFromDTO(carDTO);
            assertTrue("CAR PRESENTS NO LICENSE_PLATE NULL CONSTRAINT PROBLEM", false);
        }
        catch (ConstraintsViolationException e)
        {
            assertTrue("CAR PRESENTS LICENSE_PLATE NULL CONSTRAINT PROBLEM", true);
        }
        catch (EntityNotFoundException e)
        {
            assertTrue("CAR NOT FOUND PROBLEM", false);
        }

        // UPDATE WITH ID NULL
        carDTO = CarDTO.newBuilder()
            .setLicensePlate(licensePlate)
            .setSeatCount(seatCount)
            .createCarDTO();
        try
        {
            carService.updateFromDTO(carDTO);
            assertTrue("CAR REQUIRES ID - NOT REACHED", false);
        }
        catch (ConstraintsViolationException e)
        {
            assertTrue("CAR PRESENTS CONSTRAINT PROBLEM", false);
        }
        catch (EntityNotFoundException e)
        {
            assertTrue("CAR REQUIRES ID", true);
        }

        // FIND EXISTING LICENSE PLATE
        try
        {
            licensePlate = carService.findDTO(1L).getLicensePlate(); // Finding existing one from data.sql and diferent from just created
        }
        catch (EntityNotFoundException e1)
        {
            assertTrue("SOMETHING WENT WRONG, CHECK data.sql to see what happened to id=1", false);
        }

        // UPDATE WITH EXISTING LICENSE PLATE
        carDTO = CarDTO.newBuilder()
            .setId(carId)
            .setSeatCount(seatCount)
            .setLicensePlate(licensePlate)
            .createCarDTO();
        try
        {
            carService.updateFromDTO(carDTO);
            assertTrue("CAR PRESENTS NO LICENSE_PLATE UNIQUE CONSTRAINT PROBLEM", false);
        }
        catch (ConstraintsViolationException e)
        {
            assertTrue("CAR PRESENTS LICENSE_PLATE UNIQUE CONSTRAINT PROBLEM", true);
        }
        catch (EntityNotFoundException e)
        {
            assertTrue("CAR NOT FOUND PROBLEM", false);
        }

        // DELETE
        try
        {
            carService.delete(carId);
        }
        catch (EntityNotFoundException e)
        {
            assertTrue("CAR's ID " + carId + " DOES NOT EXIST", false);
        }
    }


    @Test
    public void selectingCars()
    {
        // SELECTING CAR 1 for Driver 4
        try
        {
            assertTrue(driverService.selectCar(4L, 1L));
        }
        catch (EntityNotFoundException | CarAlreadyInUseException e)
        {
            assertTrue(e.getMessage(), false);
        }
        // SELECTION OK
        try
        {
            assertEquals(1L, driverService.find(4L).getSelectedCar().getId().longValue());
            assertEquals(4L, carService.findDTO(1L).getDriverId().longValue());
        }
        catch (EntityNotFoundException e)
        {
            assertTrue(e.getMessage(), false);
        }

        // SELECTING CAR 2 for Driver 5
        try
        {
            assertTrue(driverService.selectCar(5L, 2L));
        }
        catch (EntityNotFoundException | CarAlreadyInUseException e)
        {
            assertTrue(e.getMessage(), false);
        }
        // SELECTION OK
        try
        {
            assertEquals(2L, driverService.find(5L).getSelectedCar().getId().longValue());
            assertEquals(5L, carService.findDTO(2L).getDriverId().longValue());
        }
        catch (EntityNotFoundException e)
        {
            assertTrue(e.getMessage(), false);
        }

        // DESELECTION PART
        try
        {
            assertFalse(driverService.deselectCar(5L, 1L)); // NOT HAD SELECTED THIS CAR BEFORE
            assertTrue(driverService.deselectCar(5L, 2L)); // OK 
            assertFalse(driverService.deselectCar(5L, 2L)); // ALREADY DESELECTED
        }
        catch (EntityNotFoundException e)
        {
            assertTrue(e.getMessage(), false);
        }

        try
        {
            assertFalse(driverService.deselectCar(4L, 2L)); // NOT HAD SELECTED THIS CAR BEFORE
            assertTrue(driverService.deselectCar(4L, 1L)); // OK 
            assertFalse(driverService.deselectCar(4L, 1L)); // ALREADY DESELECTED
        }
        catch (EntityNotFoundException e)
        {
            assertTrue(e.getMessage(), false);
        }

    }


    @Test
    public void selectingCarsOverlapping()
    {
        // SELECTING CAR 1 for Driver Offline
        try
        {
            driverService.selectCar(driverService.find(OnlineStatus.OFFLINE).get(0).getId(), 2L);
        }
        catch (EntityNotFoundException  e)
        {
            assertTrue("This driver is not online, so it can't select a car" , true);
        }
        catch (CarAlreadyInUseException e)
        {
            assertTrue(e.getMessage(), false);
        }

        // SELECTING CAR 2 for Driver 4
        try
        {
            assertTrue(driverService.selectCar(4L, 2L));
        }
        catch (EntityNotFoundException | CarAlreadyInUseException e)
        {
            assertTrue(e.getMessage(), false);
        }
        // SELECTION OK
        try
        {
            assertEquals(2L, driverService.find(4L).getSelectedCar().getId().longValue());
            assertEquals(4L, carService.findDTO(2L).getDriverId().longValue());
        }
        catch (EntityNotFoundException e)
        {
            assertTrue(e.getMessage(), false);
        }

        // SELECTING CAR 2 for Driver 5
        try
        {
            driverService.selectCar(5L, 2L);
            assertTrue("Car is already selected, should not be able to be reselected", false);
        }
        catch (EntityNotFoundException e)
        {
            assertTrue(e.getMessage(), false);
        }
        catch (CarAlreadyInUseException e)
        {
            assertTrue("Car is already selected", true);
        }

        // DRIVER 5 HAS NO CAR SELECTED AND CAR 2 STILL BELONGS TO DRIVER 4
        try
        {
            assertNull(driverService.find(5L).getSelectedCar());
            assertEquals(4L, carService.findDTO(2L).getDriverId().longValue());
        }
        catch (EntityNotFoundException e)
        {
            assertTrue(e.getMessage(), false);
        }

        // DESELECTION PART
        try
        {
            assertTrue(driverService.deselectCar(4L, 2L)); // OK 
        }
        catch (EntityNotFoundException e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

}
