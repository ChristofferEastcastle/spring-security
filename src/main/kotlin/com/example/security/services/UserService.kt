package com.example.security.services

import com.example.security.exceptions.CouldNotAuthenticateException
import com.example.security.exceptions.UsernameExistsException
import com.example.security.models.dtos.UserRegistrationDto
import com.example.security.models.entities.Authorities
import com.example.security.models.entities.AuthorityEntity
import com.example.security.models.entities.UserEntity
import com.example.security.repos.AuthorityRepo
import com.example.security.repos.UserRepo
import com.wrongwrong.mapk.core.KMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepo: UserRepo,
    @Autowired private val authorityRepo: AuthorityRepo,
    @Lazy @Autowired private val passwordEncoder: BCryptPasswordEncoder
) : UserDetailsService {

    fun fetchAllUsers(): List<UserEntity> {
        return userRepo.findAll()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        try {
            val user = userRepo.findByUsername(username)
            return User(user?.username, user?.password, user?.authorities?.map { SimpleGrantedAuthority(it.name) })
        } catch (e: Exception) {
            throw CouldNotAuthenticateException()
        }
    }

    fun registerUser(user: UserRegistrationDto): UserEntity {
        if (userRepo.existsByUsername(user.username)) {
            throw UsernameExistsException("Username does already exist")
        }
        val newUser = KMapper(::UserEntity)
            .map(
                mapOf("username" to user.username),
                mapOf("password" to passwordEncoder.encode(user.password)),
                mapOf("id" to null, "enabled" to true)
            )

        authorityRepo.findByName(Authorities.USER.name)?.let { newUser.authorities.add(it) }
        return userRepo.save(newUser)
    }

    fun createAuthority(authority: AuthorityEntity): AuthorityEntity {
        return authorityRepo.save(authority)
    }

    fun grantAuthorityToUser(username: String, authorityName: String) {
        val user = userRepo.findByUsername(username) ?: return
        val authority = authorityRepo.findByName(authorityName) ?: return

        user.authorities.add(authority)
        userRepo.save(user)
    }

    fun getUser(id: Long): UserEntity? {
        val user = userRepo.findById(id)
        if (user.isPresent) {
            return user.get()
        }
        return null
    }
}