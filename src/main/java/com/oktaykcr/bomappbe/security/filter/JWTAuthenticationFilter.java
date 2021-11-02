package com.oktaykcr.bomappbe.security.filter;

import com.oktaykcr.bomappbe.exception.ApiExceptionFactory;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import com.oktaykcr.bomappbe.model.user.User;
import com.oktaykcr.bomappbe.repository.user.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.oktaykcr.bomappbe.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        setFilterProcessesUrl(SIGN_IN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(username == null) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "username");
        }

        if(password == null) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "password");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {

        org.springframework.security.core.userdetails.User user = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal());

        User currentUser = userRepository.findByUsername(user.getUsername());

        if(currentUser == null) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "user");
        }

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("role", roles)
                .claim("username", currentUser.getUsername())
                .claim("email", currentUser.getEmail())
                .claim("id", currentUser.getId())
                .compact();

        response.addHeader(HEADER_TOKEN, TOKEN_PREFIX + token);
        //response.addHeader(HEADER_USERNAME, user.getUsername());
    }

}
