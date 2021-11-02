package com.oktaykcr.bomappbe.service;

import com.oktaykcr.bomappbe.exception.ApiExceptionFactory;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import com.oktaykcr.bomappbe.model.user.Role;
import com.oktaykcr.bomappbe.model.user.User;
import com.oktaykcr.bomappbe.repository.user.UserRepository;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.oktaykcr.bomappbe.security.SecurityConstants.SECRET;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user == null) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "user");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                true,
                getAuthorities(user));
    }

    public Boolean isTokenValid(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.LOGIN_FAILURE, "invalid credentials");
        } catch (ExpiredJwtException ex) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.LOGIN_FAILURE, "token expired");
        }
    }

    public Boolean save(User user){
        if(StringUtils.isEmpty(user.getUsername())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "username");
        }

        if(StringUtils.isEmpty(user.getPassword())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "password");
        }

        if(StringUtils.isEmpty(user.getEmail())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "email");
        }

        User registeredUser = new User();
        registeredUser.setUsername(user.getUsername());
        registeredUser.setPassword(passwordEncoder.encode(user.getPassword()));
        registeredUser.setEmail(user.getEmail());
        registeredUser.setEnabled(true);
        registeredUser.setRoles(user.getRoles());

        User savedUser = userRepository.save(registeredUser);

        return savedUser != null;
    }

    private Set<GrantedAuthority> getAuthorities(User user){
        Set<GrantedAuthority> authorities = new HashSet<>();
        for(Role role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName().getRole());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}
