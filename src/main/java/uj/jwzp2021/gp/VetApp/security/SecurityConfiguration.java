package uj.jwzp2021.gp.VetApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("user")
        .password("password")
        .roles(String.valueOf(UserType.CLIENT));
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/api/users")
        .hasRole(UserType.ADMIN.name())
        .antMatchers("/api/visits")
        .hasAnyRole(UserType.CLIENT.name(), UserType.ADMIN.name())
        .antMatchers("/ui/visits")
        .hasRole(UserType.CLIENT.name())
        .and()
        .formLogin();
  }
}
