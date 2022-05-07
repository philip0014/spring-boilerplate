package com.example.configuration;

import com.example.enumeration.PermittedMethod;
import com.example.enumeration.UserRole;
import com.example.properties.SecurityProperties;
import com.example.security.JwtSecurityConfigurer;
import com.example.security.JwtTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http
            .cors()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests();
        applyAntMatchersAndSecurityConfigurer(urlRegistry);
    }

    private void applyAntMatchersAndSecurityConfigurer(
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry)
        throws Exception {
        for (SecurityProperties.PermittedEndpoint endpoint : securityProperties.getPermittedEndpoints()) {
            String modifiedPath = String.format("%s/**", endpoint.getPath());
            if (endpoint.getMethod() == PermittedMethod.ALL) {
                urlRegistry = urlRegistry
                    .antMatchers(modifiedPath)
                    .permitAll();
            } else {
                urlRegistry = urlRegistry
                    .antMatchers(endpoint.getMethod().getHttpMethod(), modifiedPath)
                    .permitAll();
            }
        }

        urlRegistry
//            .antMatchers("/api/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/**").hasAuthority(UserRole.ADMIN.toString())
            .antMatchers(HttpMethod.PUT, "/api/**").hasAuthority(UserRole.ADMIN.toString())
            .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority(UserRole.ADMIN.toString())
            .and()
            .apply(new JwtSecurityConfigurer(jwtTokenAuthenticationFilter));
    }

}
