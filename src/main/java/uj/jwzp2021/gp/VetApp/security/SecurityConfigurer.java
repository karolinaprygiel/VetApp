package uj.jwzp2021.gp.VetApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uj.jwzp2021.gp.VetApp.security.filters.JWTFilter;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

  @Autowired
  private MyUserDetailsService myUserDetailsService;

  @Autowired
  private JWTFilter jwtFilter;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(myUserDetailsService);
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.authorizeRequests()
        .antMatchers(HttpMethod.POST,"/api/users/**").permitAll()  // anyone can create a user - register
        .antMatchers("/api/users/**").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.POST, "/api/visits/**").hasAnyAuthority("ADMIN", "CLIENT", "VET")
        .antMatchers(HttpMethod.PATCH, "/api/visits/**").hasAnyAuthority("ADMIN", "VET")
        .antMatchers(HttpMethod.DELETE, "/api/visits/**").hasAnyAuthority("ADMIN")
        .antMatchers("/api/clients/**").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.GET,"/api/vets/**").hasAnyAuthority("ADMIN", "VET", "CLIENT")
        .antMatchers(HttpMethod.POST,"/api/vets/**").hasAnyAuthority("ADMIN")
        .antMatchers(HttpMethod.PATCH,"/api/vets/**").hasAnyAuthority("ADMIN", "VET")
        .antMatchers(HttpMethod.DELETE,"/api/vets/**").hasAnyAuthority("ADMIN")
        .antMatchers("/hello").permitAll()
        .antMatchers("/hello-admin").hasAuthority("ADMIN")
        .antMatchers("/hello-vet").hasAuthority("VET")
        .antMatchers("/hello-client").hasAuthority("CLIENT")
        .antMatchers("/authenticate").permitAll()
        .anyRequest().authenticated();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
