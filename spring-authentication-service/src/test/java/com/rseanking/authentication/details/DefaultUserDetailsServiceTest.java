package com.rseanking.authentication.details;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rseanking.user.User;
import com.rseanking.user.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserDetailsServiceTest {
	@Mock
	private UserRepository repo;
	@InjectMocks
	private DefaultUserDetailsService uut;

	@Test
	public void shouldLoadUserByUsername() {
		// Given
		final User user = new User();
		user.setUsername("testusername");
		user.setPasword("testpassword");

		given(repo.findByUsername(user.getUsername())).willReturn(user);

		// When
		final UserDetailsAdapter userDetails = (UserDetailsAdapter) uut.loadUserByUsername(user.getUsername());

		// Then
		assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldNotLoadUserByUsername() {
		uut.loadUserByUsername("invalid-name");
	}

}
