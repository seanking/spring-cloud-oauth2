package com.rseanking.authentication.details;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.rseanking.user.User;

public class UserDetailAdapterTest {
	
	@Test
	public void shouldAdaptUser() {
		// Given
		final User user = new User();
		user.setUsername("testusername");
		user.setPasword("testpassword");
		
		// When
		final UserDetailsAdapter details = new UserDetailsAdapter(user);
		
		// Then
		assertThat(details.getUser()).isEqualTo(user);
		assertThat(details.getUsername()).isEqualTo(user.getUsername());
		assertThat(details.getPassword()).isEqualTo(user.getPasword());
		assertThat(details.getAuthorities()).isEmpty();
		assertThat(details.isAccountNonExpired()).isTrue();
		assertThat(details.isAccountNonLocked()).isTrue();
		assertThat(details.isCredentialsNonExpired()).isTrue();
		assertThat(details.isEnabled()).isTrue();
	}
	
}
