package com.rseanking.authentication.details;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.rseanking.user.User;
import com.rseanking.user.UserRepository;

@Component
public class DefaultUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user = repo.findByUsername(username);
		if(user != null) {
			return new UserDetailsAdapter(user);
		}
		
		final String errorMessage = format("Couldn't find the user for username: %s", username);
		throw new UsernameNotFoundException(errorMessage);
	}

}
