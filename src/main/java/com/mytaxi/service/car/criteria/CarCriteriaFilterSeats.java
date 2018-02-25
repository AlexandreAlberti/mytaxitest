package com.mytaxi.service.car.criteria;

import java.util.ArrayList;
import java.util.List;

import com.mytaxi.domainobject.CarDO;

public class CarCriteriaFilterSeats implements CarCriteriaFilter
{
    @Override
    public List<CarDO> meetCriteria(List<CarDO> cars, Object object)
    {
        Integer seats = (Integer) object;
        List<CarDO> fromManufacturer = new ArrayList<>();

        for (CarDO car : cars)
        {
            if (car.getSeatCount().equals(seats))
            {
                fromManufacturer.add(car);
            }
        }
        return fromManufacturer;

    }
}
