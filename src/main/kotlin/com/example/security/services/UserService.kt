package com.example.security.services

import com.example.security.models.dtos.UserRegistrationDto
import com.example.security.models.entities.Authorities
import com.example.security.models.entities.AuthorityEntity
import com.example.security.models.entities.UserEntity
import com.example.security.repos.AuthorityRepo
import com.example.security.repos.UserRepo
import com.wrongwrong.mapk.core.KMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepo: UserRepo,
    @Autowired private val authorityRepo: AuthorityRepo
) : UserDetailsService {
    fun fetchAllUsers(): List<UserEntity> {
        return userRepo.findAll()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        try {
            val user = userRepo.findByUsername(username)
            return User(user.username, user.password, user.authorities.map { SimpleGrantedAuthority(it.name) })
        } catch (e: Exception) {
            throw UsernameNotFoundException("Error authenticating user")
        }
    }

    fun registerUser(user: UserRegistrationDto): UserEntity {
        val newUser = KMapper(::UserEntity)
            .map(user, mapOf("id" to null, "enabled" to true))

        authorityRepo.findByName(Authorities.USER.name)?.let { newUser.authorities.add(it) }
        return userRepo.save(newUser)
    }

    fun createAuthority(authority: AuthorityEntity): AuthorityEntity {
        return authorityRepo.save(authority)
    }

    fun grantAuthorityToUser(username: String, authorityName: String) {
        val user = userRepo.findByUsername(username)
        val authority = authorityRepo.findByName(authorityName)
        authority?.let { user.authorities.add(it) }
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