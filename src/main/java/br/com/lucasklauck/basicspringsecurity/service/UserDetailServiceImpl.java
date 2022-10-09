package br.com.lucasklauck.basicspringsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.lucasklauck.basicspringsecurity.model.UserModel;
import br.com.lucasklauck.basicspringsecurity.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserModel user = userRepository.findByEmail(username);
		
		if(user == null) {
			
			throw new UsernameNotFoundException("User not found");
		}
		
		return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
	}

	
}
