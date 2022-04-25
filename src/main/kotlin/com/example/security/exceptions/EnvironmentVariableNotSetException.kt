package com.example.security.exceptions

class EnvironmentVariableNotSetException: RuntimeException("COOKIE_SECRET env variable must be set")