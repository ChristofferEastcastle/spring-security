package com.example.security.configs

import com.example.security.models.entities.Authorities.ADMIN
import com.example.security.security.filters.CustomAuthenticationFilter
import com.example.security.security.filters.CustomAuthorizationFilter
import com.example.security.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@EnableWebSecurity
@Configuration
class SecurityConfig(

    @Qualifier("userService") @Autowired private val userDetailsService: UserDetailsService,
    @Autowired private val userService: UserService,
    @Lazy @Autowired private val passwordEncoder: BCryptPasswordEncoder
) : WebSecurityConfigurerAdapter() {
    companion object {
        const val HOST = "localhost:8080/api"
        const val LOGIN_URL = "/api/login"
        const val LOGOUT_URL = "/api/logout"
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity) {
        val authFilter = CustomAuthenticationFilter(authenticationManagerBean())
        authFilter.setFilterProcessesUrl(LOGIN_URL)
        authFilter.setAllowSessionCreation(false)

        http
            .csrf().disable()
            // No need for session because we are using our own JWT to control this.
            .sessionManagement()
            .sessionCreationPolicy(STATELESS)

        http
            .addFilter(authFilter)
            .authorizeRequests()
            .antMatchers("/", "/api/login", "/api/login-page").permitAll()
            .antMatchers("/api/users/**").hasAnyAuthority(ADMIN.name)
            .antMatchers("/api/shelter/**").hasAnyAuthority(ADMIN.name)
            .anyRequest()
            .authenticated()


        http
            .addFilterBefore(CustomAuthorizationFilter(userService), UsernamePasswordAuthenticationFilter::class.java)
            .logout()
            .logoutUrl(LOGOUT_URL)
            .logoutSuccessUrl("/")
            .deleteCookies("access_token")
            .and()
            .formLogin()
            .loginProcessingUrl(LOGIN_URL)
            .loginPage("/api/login-page")
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}