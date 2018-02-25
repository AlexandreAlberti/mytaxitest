package com.mytaxi.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.mytaxi.ApplicationContextProvider;

public class TaxiJWTAuthFilter extends GenericFilterBean
{

    public TaxiJWTAuthFilter()
    {
        super();
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException
    {

        TaxiAuthProvider prov = ApplicationContextProvider.getApplicationContext().getBean(TaxiAuthProvider.class);

        SecurityContextHolder.getContext().setAuthentication(prov.getAuthentication((HttpServletRequest) request));

        filterChain.doFilter(request, response);
    }
}
