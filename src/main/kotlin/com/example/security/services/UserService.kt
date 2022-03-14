package com.example.security.services

import com.example.security.models.dtos.UserDto
import com.example.security.models.dtos.UserRegistrationDto
import com.example.security.models.entities.AuthorityEntity
import com.example.security.models.entities.UserEntity
import com.example.security.repos.AuthorityRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import com.example.security.repos.UserRepo
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Service
class UserService(
    @Autowired private val userRepo: UserRepo,
    @Autowired private val authorityRepo : AuthorityRepo
) : UserDetailsService {
    fun fetchAllUsers(): List<UserDto> {
        return userRepo
            .findAll()
            .map { UserDto(it.id, it.username, it.enabled) }
            .toList()
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        username?.let {
            val user = userRepo.findByUsername(username)
            return User(user.username, user.password, user.authorities.map { SimpleGrantedAuthority(it.name) })
        }
        throw UsernameNotFoundException("Error authenticating user")
    }

    fun registerUser(user: UserRegistrationDto): UserDto {
        val saved = userRepo.save(UserEntity(username = user.username, password = user.password, enabled = true))
        return UserDto(saved.id, saved.username, saved.enabled)
    }

    fun createAuthority(authority: AuthorityEntity): AuthorityEntity {
        return authorityRepo.save(authority)
    }

    fun grantAuthorityToUser(username: String, authorityName: String) {
        val user = userRepo.findByUsername(username)
        val authority = authorityRepo.findByName(authorityName)
        user.authorities.add(authority)
        userRepo.save(user)
    }
}