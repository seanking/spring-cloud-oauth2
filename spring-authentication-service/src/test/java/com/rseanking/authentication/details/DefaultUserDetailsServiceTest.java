package com.rseanking.authentication.details;

import static com.rseanking.user.Role.ADMIN;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rseanking.user.Role;
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
		user.setRoles(singleton(Role.ADMIN));

		given(repo.findByUsername(user.getUsername())).willReturn(user);

		// When
		final UserDetails details = uut.loadUserByUsername(user.getUsername());

		// Then
		assertThat(details.getUsername()).isEqualTo(user.getUsername());
		assertThat(details.getPassword()).isEqualTo(user.getPasword());
		
		final Set<SimpleGrantedAuthority> expectedAuthorities = singleton(new SimpleGrantedAuthority("ROLE_" + ADMIN.name()));
		assertThat(details.getAuthorities()).isEqualTo(expectedAuthorities);
		
		assertThat(details.isAccountNonExpired()).isTrue();
		assertThat(details.isAccountNonLocked()).isTrue();
		assertThat(details.isCredentialsNonExpired()).isTrue();
		assertThat(details.isEnabled()).isTrue();
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldNotLoadUserByUsername() {
		uut.loadUserByUsername("invalid-name");
	}

}
