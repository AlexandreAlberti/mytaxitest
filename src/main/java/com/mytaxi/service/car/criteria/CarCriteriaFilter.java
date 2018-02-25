package com.mytaxi.service.car.criteria;

import java.util.List;

import com.mytaxi.domainobject.CarDO;

public interface CarCriteriaFilter
{
    public List<CarDO> meetCriteria(List<CarDO> cars, Object object);

}
