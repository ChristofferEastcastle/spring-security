package com.example.security.repos

import com.example.security.models.entities.AuthorityEntity
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner


@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [UserRepo::class, AuthorityEntity::class])
@EnableJpaRepositories(basePackages = ["com.example.security.repos"])
/*

@ComponentScan(basePackages = ["com.example.security.*"])
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//@Import(AuthorityEntity::class)
@EntityScan("com.example.security.*")
*/
class UserRepoTest{

    @Autowired
    private lateinit var userRepo: UserRepo


    @Test
    fun findByUsernameTest() {
        val username = "batman"
        userRepo.findByUsername(username)
        val result = userRepo.findByUsername(username)


    }
}