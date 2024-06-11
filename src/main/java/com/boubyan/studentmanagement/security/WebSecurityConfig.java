package com.boubyan.studentmanagement.security;

import com.boubyan.studentmanagement.security.jwt.AuthEntryPointJwt;
import com.boubyan.studentmanagement.security.jwt.AuthTokenFilter;
import com.boubyan.studentmanagement.security.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Configuration
    @Order(2)
    @Component("SecurityConfig")
    public static class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        UserDetailsServiceImpl userDetailsService;

        @Autowired
        private AuthEntryPointJwt unauthorizedHandler;

        @Autowired
        private AuthTokenFilter AuthTokenFilter;




        @Override
        public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
            authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }


        @Bean
        public SessionRegistry sessionRegistry() {
            return new SessionRegistryImpl();
        }

        @Bean
        public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            var sessionAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
            sessionAuthenticationStrategy.setMaximumSessions(1);
            sessionAuthenticationStrategy.setExceptionIfMaximumExceeded(true);
            return sessionAuthenticationStrategy;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                    .rememberMe()
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().sessionManagement().sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                    .and().authorizeRequests().antMatchers("/login","/signup","/auth/refresh-token").permitAll()
                    .anyRequest()
                    .authenticated();
            http.addFilterBefore(AuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOriginPatterns(List.of("*"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
            configuration.setAllowedHeaders(buildAllowedHeaders());
            configuration.setAllowCredentials(true);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }

        private static List<String> buildAllowedHeaders() {
            return List.of("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization");
        }
    }
}
