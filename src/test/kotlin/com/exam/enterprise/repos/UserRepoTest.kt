package com.exam.enterprise.repos

import com.exam.enterprise.models.entities.UserEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertEquals


@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [UserRepo::class])
@EnableJpaRepositories(basePackages = ["com.exam.enterprise.*"])
@EntityScan("com.exam.enterprise.models")

class UserRepoTest {
    @Autowired
    private lateinit var userRepo: UserRepo

    @BeforeEach
    fun init() {
        userRepo.save(UserEntity(username = "jim@bob.com", password = "password", enabled = true))
    }

    @Test
    fun findByUsernameTest() {
        val username = "jim@bob.com"
        val result = userRepo.findByUsername(username)
        assertThat(result)
            .isNotNull
            .hasFieldOrPropertyWithValue("username", username)
    }

    @Test
    fun doNotFindByUsernameTest() {
        assertEquals(
            null, userRepo.findByUsername("something")
        )
    }

    @Test
    fun findById() {
        assertThat(userRepo.findById(1))
            .isNotNull
            .isNotEmpty
    }

    @Test
    fun doNotFindById() {
        assertThat(userRepo.findById(8271398723))
            .isEmpty
    }
}