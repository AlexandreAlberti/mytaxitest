package com.mytaxi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mytaxi.security.TaxiAuthProvider;
import com.mytaxi.security.TaxiJWTAuthFilter;
import com.mytaxi.security.TaxiJWTLoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private TaxiAuthProvider authProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		try {
			http.authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll().anyRequest().authenticated().and()
					// We filter the api/login requests
					.addFilterBefore(new TaxiJWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
					// And filter other requests to check the presence of JWT in header
					.addFilterBefore(new TaxiJWTAuthFilter(), UsernamePasswordAuthenticationFilter.class).csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		} catch (Exception e) {
			throw new Exception("server.inconsistent.title", e);
		}
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/actuator/**", "/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**");
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		try {
			auth.authenticationProvider(authProvider);
		} catch (Exception e) {
			e.printStackTrace();
            throw new Exception("server.inconsistent.title", e);
		}
	}

	@Bean
	@Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
		AuthenticationManager mgr = null;
		try {
			mgr = super.authenticationManagerBean();
		} catch (Exception e) {
			throw new Exception("server.inconsistent.title", e);
		}
		return mgr;
	}

}
