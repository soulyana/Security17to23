package me.soulyana.security.security;

import me.soulyana.security.repositories.UserRepository;
import me.soulyana.security.security.SSUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

    @Autowired
    private SSUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new SSUserDetailsService(userRepository);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                //.access("hasAuthority('USER')or hasAuthority('ADMIN')")
                // antMatchers is a route h2... means this route and anything below it
                // antMatchers allows access to that path
                .antMatchers("/", "/h2-console/**").permitAll()
                //allows access to sign in
                .anyRequest().authenticated()
                .antMatchers("/admin").access("hasAuthority('ADMIN')")
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll().permitAll()
                .and()
                .httpBasic(); //only used for testing purposes. Remove before web goes live.
        httpSecurity
                .csrf().disable();
        httpSecurity
                .headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("password").authorities("USER")
//                .and()
//                .withUser("dave").password("begreat").authorities("ADMIN");
        auth.userDetailsService(userDetailsServiceBean());
    }
}
