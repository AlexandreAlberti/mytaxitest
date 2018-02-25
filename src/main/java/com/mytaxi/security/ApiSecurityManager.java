package com.mytaxi.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.DriverDO;

@Service
public class ApiSecurityManager
{

    @Autowired
    private DriverRepository driverRepository;

    public ApiSecurityManager()
    {
    }


    /**
     * Logins the user
     * 
     * @param username
     * @param password
     * @param serial
     * @return
     */
    @Transactional(readOnly = true)
    public TaxiUserDetail login(String username, String password)
    {
        DriverDO c = driverRepository.findByUsernameAndPassword(username, password);
        if (c != null)
        {
            return new TaxiUserDetail(c.getId(), c.getUsername(), "", new ArrayList<>());
        }

        return null;
    }

}
