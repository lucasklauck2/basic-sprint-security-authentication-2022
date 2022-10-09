package br.com.lucasklauck.basicspringsecurity.security;


import static br.com.lucasklauck.basicspringsecurity.service.JWTTokenAutenticacaoService.HEADER_STRING;
import static br.com.lucasklauck.basicspringsecurity.service.JWTTokenAutenticacaoService.SECRET_KEY;
import static br.com.lucasklauck.basicspringsecurity.service.JWTTokenAutenticacaoService.TOKEN_PREFIX;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import br.com.lucasklauck.basicspringsecurity.service.JWTTokenAutenticacaoService;
import io.jsonwebtoken.Jwts;

public class AuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse responseDois = (HttpServletResponse) response;
		
		Authentication authentication = null;
		
		try {
			
			authentication = JWTTokenAutenticacaoService.getAuthentication((HttpServletRequest) request);
			
		} catch (Exception e) {
			
			responseDois.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			return;
		}

		if (!isValidToken((HttpServletRequest) request)) {

			responseDois.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			return;
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		chain.doFilter(request, response);

	}

	private boolean isValidToken(HttpServletRequest request) {

		String token = request.getHeader(HEADER_STRING);

		if (token == null) {
			
			return false;
		}

		try {
			
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token.replace(TOKEN_PREFIX, ""));
			
		} catch (Exception e) {
			
			return false;
		}

		return true;
	}

}
