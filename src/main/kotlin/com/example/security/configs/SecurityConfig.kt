package com.example.security.configs

import com.example.security.configs.SecurityConfig.Authorities.ADMIN
import com.example.security.security.filters.CustomAuthenticationFilter
import com.example.security.security.filters.CustomAuthorizationFilter
import com.example.security.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.core.env.Environment
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
    @Autowired private val userService: UserService,
    @Lazy @Autowired private val passwordEncoder: BCryptPasswordEncoder,
    @Autowired private val env: Environment
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    companion object {
        const val LOGIN_PAGE_URL = "/login"
        const val LOGIN_URL = "/"
        const val LOGOUT_PAGE_URL = "/api/auth/logout"
    }

    enum class Authorities {
        USER, ADMIN
    }


    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity) {

        if (env.activeProfiles.contains("test")) {
            http.csrf().disable()
        }
        val authFilter = CustomAuthenticationFilter(authenticationManagerBean())
        http
            .addFilter(authFilter)
            .authorizeRequests()
            .antMatchers("/api/users/**").hasAuthority(ADMIN.name)
            .anyRequest()
            .authenticated()
            .and()
            .addFilterBefore(CustomAuthorizationFilter(userService), UsernamePasswordAuthenticationFilter::class.java)
            .formLogin()
            .and()
            .logout()
            .deleteCookies("access_token")
            .and()
            .sessionManagement()
            // No need for session because we are using our own JWT to control this.
            .sessionCreationPolicy(SessionCreationPolicy.NEVER)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}