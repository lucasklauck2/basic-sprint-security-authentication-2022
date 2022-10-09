package br.com.lucasklauck.basicspringsecurity.config;

import java.util.Arrays;

import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.lucasklauck.basicspringsecurity.security.AuthenticationFilter;
import br.com.lucasklauck.basicspringsecurity.security.LoginFilter;
import br.com.lucasklauck.basicspringsecurity.service.UserDetailServiceImpl;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebConfigSecurity implements HttpSessionListener{

	@Autowired
	private UserDetailServiceImpl userDetailsService;
	
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
    	
         AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
         
         authenticationManagerBuilder
               .userDetailsService(userDetailsService)
               .passwordEncoder(new BCryptPasswordEncoder());
         
         return authenticationManagerBuilder.build();
     }
	
	
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
    	
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.cors().configurationSource(this.corsConfigurationSource()).and().csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/**").authenticated();
        http.authorizeRequests().anyRequest().permitAll();
        
        http.addFilterBefore(new LoginFilter("/login"),
				UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

	
	private CorsConfigurationSource corsConfigurationSource() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("*"));
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.applyPermitDefaultValues();

		source.registerCorsConfiguration("/**", config);

		return source;
	}
}
