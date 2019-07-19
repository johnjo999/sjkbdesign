package com.sjkb.config;

import com.sjkb.repositores.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/", "/onstage/**", "/css/**", "/assets/**", "/img/**", "/js/**", "/fonts/**", "/error.*", "/home.*").permitAll()
                .antMatchers("/backstage/**").access("hasRole('ROLE_USER')")
                .anyRequest().authenticated()
                .and().formLogin()
                    .loginProcessingUrl("/login.html")
                    .permitAll()
                    .defaultSuccessUrl("/backstage/dashboard")
                    .and()
                .logout()
                    .permitAll();
    }

    private PasswordEncoder getPasswordEncoder() {
        return new PasswordEncoder() {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            @Override
            public String encode(CharSequence charSequence) {               
                String encoded = encoder.encode(charSequence);
                return encoded;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String s) {
                boolean result = encoder.matches(rawPassword, s);
                return result;
            }
        };
    }
}

