package com.mytaxi.service.car.criteria;

import java.util.ArrayList;
import java.util.List;

import com.mytaxi.domainobject.CarDO;

public class CarCriteriaFilterLicensePlate implements CarCriteriaFilter
{
    @Override
    public List<CarDO> meetCriteria(List<CarDO> cars, Object object)
    {
        String licensePlate = (String) object;
        List<CarDO> fromManufacturer = new ArrayList<>();

        for (CarDO car : cars)
        {
            if (car.getLicensePlate().contains(licensePlate))
            {
                fromManufacturer.add(car);
            }
        }
        return fromManufacturer;

    }
}
