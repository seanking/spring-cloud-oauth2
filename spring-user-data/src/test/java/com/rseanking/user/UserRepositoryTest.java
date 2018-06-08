package com.rseanking.user;

import static com.rseanking.user.Role.ADMIN;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataMongoTest
@EnableAutoConfiguration
@ContextConfiguration(classes= {MongoConfiguration.class})
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;
    
    @After
    public void tearDown() {
        repo.deleteAll();
    }
    
    @Test
    public void shouldFindUserByUsername() {
        // Given
        final User existingUser = new User();
        existingUser.setUsername("foo");
        existingUser.setPasword("foo");
        existingUser.setRoles(singleton(ADMIN));
        
        repo.save(existingUser);
        
        // When
        final User foundUser = repo.findByUsername(existingUser.getUsername());
        
        // Then
        assertThat(foundUser.getUsername()).isEqualTo(existingUser.getUsername());
        assertThat(foundUser.getPasword()).isEqualTo(existingUser.getPasword());
        assertThat(foundUser.getRoles()).containsOnly(ADMIN);
    }

}
