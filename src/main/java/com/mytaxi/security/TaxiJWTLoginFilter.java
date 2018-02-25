package com.mytaxi.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mytaxi.ApplicationContextProvider;

public class TaxiJWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	public TaxiJWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#attemptAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {
		return getAuthenticationManager().authenticate(
				new TaxiAuthToken(
						req.getParameter("username"),
						req.getParameter("password"),
						new Long(req.getParameter("serial")),
						Collections.emptyList()
						)
				);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#successfulAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain, org.springframework.security.core.Authentication)
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest req, 
			HttpServletResponse res, FilterChain chain, Authentication auth) 
					throws IOException, ServletException {
		TaxiAuthProvider prov = ApplicationContextProvider.getApplicationContext().getBean(TaxiAuthProvider.class);
		prov.addAuthentication(res, (TaxiAuthToken)auth);
	}
}