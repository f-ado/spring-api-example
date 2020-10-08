package com.springapi.configuration;

import com.springapi.authentication.CustomUserDetailsService;
import com.springapi.authentication.JwtAuthEntryPoint;
import com.springapi.authentication.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.BeanIds;
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
@EnableGlobalMethodSecurity(
		securedEnabled = true, // @Secured Annotation
		jsr250Enabled = true, // @RoleAllowed Annotation
		prePostEnabled = true // @PreAuthorize and @PostAuthorize Annotations
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtAuthEntryPoint authEntryPoint;

	@Bean
	public JwtAuthFilter jwtAuthenticationFilter() {
		return new JwtAuthFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
				.userDetailsService(customUserDetailsService)
				.passwordEncoder(passwordEncoder());
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http
			.cors()
			.and()
			.csrf()
			.disable()
			.exceptionHandling()
			.authenticationEntryPoint(authEntryPoint)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests(authorize -> {
				authorize
					.antMatchers(
						"/",
						"/favicon.ico",
						"/**/*.png",
						"/**/*.gif",
						"/**/*.svg",
						"/**/*.jpg",
						"/**/*.html",
						"/**/*.css",
						"/**/*.js"
					).permitAll()
					.antMatchers("/h2/**").permitAll()
					.antMatchers("/api/auth/**").permitAll()
					.antMatchers(
						"/api/users/checkUsernameAvailability",
						"/api/users/checkEmailAvailability"
					).permitAll()
					.antMatchers(
						HttpMethod.GET,
						"/api/posts/**",
						"/api/categories/all",
						"/api/tags/all"
					)
					.permitAll();
			})
			.authorizeRequests()
			.anyRequest()
			.authenticated();

		// Custom JWT security filter
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		// For h2-console
		http.headers().frameOptions().sameOrigin();
	}

	@Bean
	public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
	}
}
