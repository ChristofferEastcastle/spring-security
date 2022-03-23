package com.example.security.security

import com.example.security.security.SecurityConfig.Authorities.ADMIN
import com.example.security.security.filters.CustomAuthenticationFilter
import com.example.security.security.filters.CustomAuthorizationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig(
    @Autowired private val userDetailsService: UserDetailsService,
    @Autowired private val passwordEncoder: BCryptPasswordEncoder,
) : WebSecurityConfigurerAdapter() {
    private val routesWithoutAuth = listOf<String>()

    enum class Authorities {
        USER, ADMIN
    }


    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity) {
        val authFilter = CustomAuthenticationFilter(authenticationManagerBean())
        //authFilter.setFilterProcessesUrl("/")
        http
            .addFilter(authFilter)
            .sessionManagement()
            // No need for session because we are using our own JWT to control this.
            .sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and()
            .authorizeRequests()
            .antMatchers("/api/users/**").hasAuthority(ADMIN.name)
            .anyRequest()
            .authenticated()
            .and()
            .addFilterBefore(CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .formLogin()
            .and()
            .logout()
            .deleteCookies("access_token")

    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}