package com.example.security.configs;

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter


class AdditionalFormLoginConfigurer :
    AbstractHttpConfigurer<AdditionalFormLoginConfigurer, HttpSecurity?>() {
    companion object {
        const val LOGIN_PAGE_URL = "/api/users"
        const val LOGIN_PROCESSING_URL = "/api/authentication/login"
        const val LOGOUT_PAGE_URL = "/api/authentication/logout"
    }

    @Throws(Exception::class)
    override fun init(http: HttpSecurity?) {
        val loginPageGeneratingFilter = http?.getSharedObject(
            DefaultLoginPageGeneratingFilter::class.java
        ) ?: return
        loginPageGeneratingFilter.setFormLoginEnabled(true)
        loginPageGeneratingFilter.setUsernameParameter("username")
        loginPageGeneratingFilter.setPasswordParameter("password")
        loginPageGeneratingFilter.loginPageUrl = LOGIN_PAGE_URL
        loginPageGeneratingFilter.setLogoutSuccessUrl("$LOGIN_PAGE_URL?logout")
        loginPageGeneratingFilter.setFailureUrl("$LOGIN_PAGE_URL?error")
        loginPageGeneratingFilter.setAuthenticationUrl(LOGIN_PAGE_URL)
    }

}