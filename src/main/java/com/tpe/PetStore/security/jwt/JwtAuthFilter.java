package com.tpe.PetStore.security.jwt;

import com.tpe.PetStore.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/auth/login".equals(path) || "/auth/register".equals(path) || path.startsWith("/actuator")){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = extractJWTFromRequest(request);

            if (jwt != null && jwtUtils.validateJWT(jwt)) {
                //!!!elimizde bu uygulamanin olusturdugu bir token var demektir!!!
                String username = jwtUtils.extractUsernameFromJwt(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (UsernameNotFoundException e) {
            LOGGER.warn("{} The user might be deleted.", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String extractJWTFromRequest(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization"); //Value: Bearer <token>
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }else {
            return null;
        }
    }
}
