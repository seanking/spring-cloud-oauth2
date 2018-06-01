package com.rseanking.user;

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
		final User user = new User();
		user.setUsername("foo");
		user.setPasword("foo");
		
		repo.save(user);
		
		// When
		final User foundUser = repo.findByUsername(user.getUsername());
		
		// Then
		assertThat(foundUser).isNotNull();
	}

}
