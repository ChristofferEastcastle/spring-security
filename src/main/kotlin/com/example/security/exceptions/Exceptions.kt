package com.example.security.exceptions

class CouldNotAuthenticateException: RuntimeException("Could not authenticate")
class UsernameExistsException(message: String) : RuntimeException(message)