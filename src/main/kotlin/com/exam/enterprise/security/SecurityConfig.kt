package com.exam.enterprise.security

import com.exam.enterprise.models.entities.Authorities.*
import com.exam.enterprise.security.filters.CustomAuthenticationFilter
import com.exam.enterprise.security.filters.CustomAuthorizationFilter
import com.exam.enterprise.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpMethod.*
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

    @Qualifier("userService")
    @Autowired private val userDetailsService: UserDetailsService,
    @Autowired private val userService: UserService,
    @Lazy @Autowired private val passwordEncoder: BCryptPasswordEncoder
) : WebSecurityConfigurerAdapter() {
    companion object {
        const val LOGIN_URL = "/api/authentication/login"
        const val LOGIN_PAGE = "/api/authentication/login-page"
        const val REGISTER_URL = "/api/authentication/register"
        const val LOGOUT_URL = "/api/authentication/logout"
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
            .antMatchers("/", LOGIN_URL, LOGIN_PAGE, REGISTER_URL).permitAll()
            .antMatchers("/api/users/**").hasAnyAuthority(ADMIN.name)
            .antMatchers(GET, "/api/shelter/animals/**").hasAnyAuthority(ADMIN.name, USER.name, TRAINEE.name)
            .antMatchers(POST, "/api/shelter/animals").hasAnyAuthority(ADMIN.name, USER.name)
            .antMatchers(DELETE, "/api/shelter/animals").hasAnyAuthority(ADMIN.name)
            .antMatchers("/api/shelter/**").hasAnyAuthority(ADMIN.name, USER.name)
            .anyRequest()
            .authenticated()

        http
            .addFilterBefore(CustomAuthorizationFilter(userService), UsernamePasswordAuthenticationFilter::class.java)
            .logout()
            .logoutUrl(LOGOUT_URL)
            .logoutSuccessUrl(LOGIN_PAGE)
            .deleteCookies("access_token")
            .and()
            .formLogin()
            .loginProcessingUrl(LOGIN_URL)
            .loginPage(LOGIN_PAGE)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}