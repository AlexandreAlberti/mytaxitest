package com.mytaxi.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class TaxiUserDetail extends User
{

    /**
     * UID
     */
    private static final long serialVersionUID = 8166834755985978347L;

    /**
     * ID of the user in the system
     */
    protected long id;


    public TaxiUserDetail(long id, String username, String password, Collection<? extends GrantedAuthority> authorities)
    {
        super(username, password, authorities);
        setId(id);
    }


    public long getId()
    {
        return id;
    }


    public void setId(long id)
    {
        this.id = id;
    }

}
