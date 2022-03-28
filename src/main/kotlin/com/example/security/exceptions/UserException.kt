package com.example.security.exceptions
class UserException {
    class AdminEnvUsernameNotFoundException :
        RuntimeException("Need to set ADMIN_USERNAME env variable to set admin user!")

    class AdminEnvPasswordNotFoundException :
        RuntimeException("Need to set ADMIN_PASSWORD env variable to set admin user!")
}