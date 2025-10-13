package com.security.securityg138.config;

import com.security.securityg138.model.User;
import com.security.securityg138.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){

        return username -> {
            var user = userRepository.getUserByEmail(username);

            if(user==null) throw new UsernameNotFoundException("USER NOT FOUND");
            else return user;
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        var auth = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

            auth
                    .userDetailsService(userDetailsService())
                    .passwordEncoder(passwordEncoder());

            httpSecurity.csrf(AbstractHttpConfigurer::disable);

            httpSecurity.exceptionHandling(e -> e.accessDeniedPage("/forbidden"));

            httpSecurity.formLogin(fl ->
                        fl.loginProcessingUrl("/auth")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .loginPage("/sign-in")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/sign-in?error"));

            httpSecurity.logout(lg -> lg.logoutUrl("/log-out").logoutSuccessUrl("/sign-in"));

            return httpSecurity.build();
    }


}
