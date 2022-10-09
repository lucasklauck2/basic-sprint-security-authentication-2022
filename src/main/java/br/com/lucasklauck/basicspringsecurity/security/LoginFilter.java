package br.com.lucasklauck.basicspringsecurity.security;

import static java.util.Collections.emptyList;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.lucasklauck.basicspringsecurity.dto.LoginDTO;
import br.com.lucasklauck.basicspringsecurity.service.JWTTokenAutenticacaoService;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	public LoginFilter(String url) {
		super(new AntPathRequestMatcher(url));

		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		LoginDTO loginDTO = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
		
		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword(), emptyList()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		response.setContentType("application/json");
		JWTTokenAutenticacaoService.addToken(response, authResult.getName());
	}

}
