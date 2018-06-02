package com.rseanking.authentication.details;

import static com.rseanking.user.Role.ADMIN;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.rseanking.user.User;

public class UserDetailAdapterTest {
	
	@Test
	public void shouldAdaptUser() {
		// Given
		final User user = new User();
		user.setUsername("testusername");
		user.setPasword("testpassword");
		user.setRoles(singleton(ADMIN));
		
		// When
		final UserDetailsAdapter details = new UserDetailsAdapter(user);
		
		// Then
		assertThat(details.getUsername()).isEqualTo(user.getUsername());
		assertThat(details.getPassword()).isEqualTo(user.getPasword());
		
		final Set<SimpleGrantedAuthority> expectedAuthorities = singleton(new SimpleGrantedAuthority(ADMIN.name()));
		assertThat(details.getAuthorities()).isEqualTo(expectedAuthorities);
		
		assertThat(details.isAccountNonExpired()).isTrue();
		assertThat(details.isAccountNonLocked()).isTrue();
		assertThat(details.isCredentialsNonExpired()).isTrue();
		assertThat(details.isEnabled()).isTrue();
	}
	
}
