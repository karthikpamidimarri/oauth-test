package de.frontierpsychiatrist.example.oauth.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Moritz Schulze
 */
// @Component("userDetailsService")
public class JdbcUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<de.frontierpsychiatrist.example.oauth.domain.User> userfromdb = userRepository
				.findOneByLogin(username);
		if (userfromdb == null) {
			throw new UsernameNotFoundException("User " + username + " not found in database.");
		}
		de.frontierpsychiatrist.example.oauth.domain.User user = userfromdb.get();
		// return new User(user.getLogin(), user.getPassword(),
		// user.isActivated(), true, true, true, user.getAuthorities());

		// return new UserRepositoryUserDetails(user);
		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
		System.out.println("Granted authoriteis are" + grantedAuthorities);
		
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(),user.isActivated(),true,true,true, grantedAuthorities);
	}
}