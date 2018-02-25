package com.mytaxi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import com.mytaxi.dataaccessobject.DriverRepository;

@Component
public class TaxiAuthProvider implements AuthenticationProvider
{

    static final long EXPIRATIONTIME = 86_400_000; // 1 day
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    @Autowired
    private ApiSecurityManager secMgr;
    @Autowired
    private DriverRepository driverRepo;


    public TaxiAuthProvider()
    {}


    /**
     * Set authentication JWT token in the request
     * 
     * @param res
     * @param auth
     */
    public void addAuthentication(HttpServletResponse res, TaxiAuthToken auth)
    {

        if (auth != null)
        {
            final TaxiUserDetail user = (TaxiUserDetail) auth.getPrincipal();
            List<String> lroles = new ArrayList<String>();
            for (GrantedAuthority g : user.getAuthorities())
                lroles.add(g.toString());
            String JWT = Jwts.builder().setId(String.valueOf(user.getId())).setSubject(user.getUsername()).claim("taxi-ser", auth.getId())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, Base64.encode(auth.getId().toString().getBytes())).compact();
            res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
        }
    }


    /**
     * Get authentication from the token
     * 
     * @param request
     * @return
     */
    public Authentication getAuthentication(HttpServletRequest request)
    {
        String token = request.getHeader(HEADER_STRING);
        if (token != null && token.contains("."))
        {
            String strJws = token.replace(TOKEN_PREFIX, "").trim();

            // Get serial from a previouly fake token
            Integer serial = Jwts.parser().parseClaimsJwt("eyJhbGciOiJub25lIn0." + strJws.split("\\.")[1] + ".").getBody().get("taxi-ser", Integer.class);

            // Get claims
            Claims c = Jwts.parser().setSigningKey(Base64.encode(serial.toString().getBytes())).parseClaimsJws(strJws).getBody();

            if (c != null)
            {
                // Build user
                TaxiUserDetail user =
                    new TaxiUserDetail(Long.valueOf(c.getId()), c.getSubject(), "[PROTECTED]", new ArrayList<>());

                // Return authentication
//                byte[] decode = Base64.decode(serial.getBytes());
//                String s = new String(decode);
//                Long id = new Long(s);
                return c.getSubject() != null ? new TaxiAuthToken(user, null, serial.longValue(), new ArrayList<GrantedAuthority>()) : null;
            }
        }
        return null;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        TaxiAuthToken auth = (TaxiAuthToken) authentication;
        TaxiUserDetail user = secMgr.login(auth.getName(), (String) auth.getCredentials());
        if (user != null)
        {
            return new TaxiAuthToken(user, null, auth.getId(), user.getAuthorities());
        }
        return null;
    }


    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(TaxiAuthToken.class);
    }
}
