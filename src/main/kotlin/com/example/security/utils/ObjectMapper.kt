package com.example.security.utils

import kotlin.reflect.KClass

class ObjectMapper<T : KClass<T>> (val clazz: KClass<T>) {
    fun map(src: Any) {

    }
}