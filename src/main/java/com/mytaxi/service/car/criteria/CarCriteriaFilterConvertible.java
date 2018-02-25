package com.mytaxi.service.car.criteria;

import java.util.ArrayList;
import java.util.List;

import com.mytaxi.domainobject.CarDO;

public class CarCriteriaFilterConvertible implements CarCriteriaFilter
{
    @Override
    public List<CarDO> meetCriteria(List<CarDO> cars, Object object)
    {
        Boolean convertible = (Boolean) object;
        List<CarDO> fromManufacturer = new ArrayList<>();

        for (CarDO car : cars)
        {
            if (car.getConvertible().equals(convertible))
            {
                fromManufacturer.add(car);
            }
        }
        return fromManufacturer;

    }
}
