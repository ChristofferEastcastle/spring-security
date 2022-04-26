package com.exam.enterprise.exceptions

class CouldNotAuthenticateException: RuntimeException("Could not authenticate")
class UsernameExistsException(message: String) : RuntimeException(message)