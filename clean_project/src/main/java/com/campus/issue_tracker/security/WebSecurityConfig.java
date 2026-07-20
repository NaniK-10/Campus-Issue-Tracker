package com.campus.issue_tracker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

        @Autowired
        UserDetailsServiceImpl userDetailsService;

        @Autowired
        CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

        @Bean
        public AuthTokenFilter authenticationJwtTokenFilter() {
                return new AuthTokenFilter();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder
                                .userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder());
                return authenticationManagerBuilder.build();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())) // CSRF protection
                                                                                                     // enabled with
                                                                                                     // cookie-based
                                                                                                     // token for AJAX
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/login", "/signup", "/verify-otp",
                                                                "/api/auth/**", "/css/**", "/js/**", "/images/**")
                                                .permitAll()

                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login") // This matches the form action
                                                .defaultSuccessUrl("/dashboard", true)
                                                .failureHandler(customAuthenticationFailureHandler)
                                                .permitAll())
                                .logout(logout -> logout.logoutSuccessUrl("/"))
                                .sessionManagement(session -> session
                                                .invalidSessionUrl("/login?timeout=true")
                                                .maximumSessions(1))
                                .userDetailsService(userDetailsService);

                http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}