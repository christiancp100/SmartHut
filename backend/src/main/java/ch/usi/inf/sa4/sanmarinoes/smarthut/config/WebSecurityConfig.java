package ch.usi.inf.sa4.sanmarinoes.smarthut.config;

import ch.usi.inf.sa4.sanmarinoes.smarthut.service.JWTUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired private JWTUserDetailsService jwtUserDetailsService;
    @Autowired private JWTRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // We don't need CSRF for this example
        httpSecurity
                .csrf()
                .disable()
                // dont authenticate this particular request
                .authorizeRequests()
                .antMatchers(
                        "/security_camera_videos/**",
                        "/sensor-socket",
                        "/auth/login",
                        "/swagger-ui.html",
                        "/register",
                        "/register/confirm-account",
                        "/register/init-reset-password",
                        "/register/reset-password",
                        "/v2/api-docs",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/csrf")
                .permitAll()
                // all other requests need to be authenticated
                .anyRequest()
                .authenticated()
                .and()
                .
                // make sure we use stateless session; session won't be used to
                // store user's state.
                exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(
                        SessionCreationPolicy
                                .STATELESS); // Add a filter to validate the tokens with every
        // request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
