package com.mytaxi.service.car.criteria;

import java.util.ArrayList;
import java.util.List;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainvalue.EngineType;

public class CarCriteriaFilterEngineType implements CarCriteriaFilter
{
    @Override
    public List<CarDO> meetCriteria(List<CarDO> cars, Object object)
    {
        EngineType eType = (EngineType) object;
        List<CarDO> fromManufacturer = new ArrayList<>();

        for (CarDO car : cars)
        {
            if (car.getEngineType().equals(eType))
            {
                fromManufacturer.add(car);
            }
        }
        return fromManufacturer;

    }
}
