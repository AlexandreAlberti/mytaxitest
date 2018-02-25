package com.mytaxi.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class TaxiAuthToken extends UsernamePasswordAuthenticationToken
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Id UID
     */
    private Long id;

    public TaxiAuthToken(Object principal, Object credentials, Long id)
    {
        super(principal, credentials, null);
        this.id = id;
    }


    public TaxiAuthToken(Object principal, Object credentials,
        Long id, Collection<? extends GrantedAuthority> authorities)
    {
        super(principal, credentials, authorities);
        this.id = id;
    }


    public Long getId()
    {
        return this.id;
    }
}
