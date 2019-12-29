package com.sjkb.config;

import com.sjkb.repositores.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());        
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/", "/onstage/**", "/css/**", "/assets/**", "/img/**", "/js/**", "/fonts/**", "/error", "/loginFailed.html",
                        "/home.*", "favicon.ico", "/admin/grabtoken").permitAll()
                .antMatchers("/backstage/**").access("hasRole('ROLE_USER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/sysadm/**").access("hasRole('ROLE_SYS')")
                .antMatchers("/protal/**").access("hasRole('ROLE_CUST')")
                .anyRequest().authenticated().and().formLogin().loginPage("/login.html").failureUrl("/loginFailed.html")
                .loginProcessingUrl("/login.html").permitAll().defaultSuccessUrl("/portal/dashboard").and().logout()
                .permitAll();
        http.authorizeRequests().expressionHandler(webExpressionHandler());
        http.requiresChannel().anyRequest().requiresSecure();
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_SYS > ROLE_ADMIN > ROLE_USER > ROLE_CUST");
        return roleHierarchy;
    }

    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
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
