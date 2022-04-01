package com.example.security.configs

import com.example.security.configs.SecurityConfig.Authorities.ADMIN
import com.example.security.security.filters.CustomAuthenticationFilter
import com.example.security.security.filters.CustomAuthorizationFilter
import com.example.security.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
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
    @Qualifier("userService") @Autowired private val userDetailsService: UserDetailsService,
    @Autowired private val userService: UserService,
    @Lazy @Autowired private val passwordEncoder: BCryptPasswordEncoder,
    @Autowired private val env: Environment
) : WebSecurityConfigurerAdapter() {

    val noAuthPages = listOf("/login, /register")

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

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
            .sessionManagement()
            // No need for session because we are using our own JWT to control this.
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
            .authorizeRequests()
            .antMatchers("/api/auth/register", "/api/auth/login")
            .permitAll()

        //noAuthPages.forEach { http.authorizeRequests().antMatchers(it).permitAll() }
        /*http
            .formLogin()
            .loginPage(LOGIN_PAGE_URL)
            .loginProcessingUrl(LOGIN_PROCESSING_URL)
            .successForwardUrl("/api/users")
            .permitAll().and().apply(AdditionalFormLoginConfigurer())*/
        http
            .authorizeRequests()
            .antMatchers("/api/users/**").hasAuthority(ADMIN.name)
            .anyRequest()
            .authenticated()


        http
            .addFilterBefore(CustomAuthorizationFilter(userService), UsernamePasswordAuthenticationFilter::class.java)
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessUrl("/api/login")
            .deleteCookies("access_token")
            .and()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}