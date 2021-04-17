package rombuulean.buuleanBook.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.naming.AuthenticationException;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class BuuleanBookSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    User systemUser(){
        return new User("systemUser", "", List.of( new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        //GET catalog, GET catalog/ID
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/catalog/**", "/uploads/**", "/authors/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/orders").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("marek@example.org")
                .password("{noop}xxx")
                .roles("USER")
                .and()
                .withUser("admin")
                .password("{noop}xxx")
                .roles("ADMIN");
    }
}
