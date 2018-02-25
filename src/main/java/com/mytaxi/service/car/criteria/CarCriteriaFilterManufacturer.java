package com.mytaxi.service.car.criteria;

import java.util.ArrayList;
import java.util.List;

import com.mytaxi.domainobject.CarDO;

public class CarCriteriaFilterManufacturer implements CarCriteriaFilter
{
    @Override
    public List<CarDO> meetCriteria(List<CarDO> cars, Object object)
    {
        Long idManufacturer = (Long) object;
        List<CarDO> fromManufacturer = new ArrayList<>();

        for (CarDO car : cars)
        {
            if (car.getManufacturer().getId().equals(idManufacturer))
            {
                fromManufacturer.add(car);
            }
        }
        return fromManufacturer;

    }
}
