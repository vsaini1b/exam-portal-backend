package com.saini_vinit.portal.exam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saini_vinit.portal.exam.config.JwtUtil;
import com.saini_vinit.portal.exam.entity.JwtRequest;
import com.saini_vinit.portal.exam.service.impl.UserDetailsServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthenticateController {

	private final AuthenticationManager authenticationManager;
	private final UserDetailsServiceImpl UserDetailsServiceImpl;
	private final JwtUtil jwtUtil;

	// genrateToken
	@PostMapping("/genrate-token")
	public ResponseEntity genrateToken(@RequestBody JwtRequest jwtRequest) throws Exception {

		try {

			this.authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

		} catch (UsernameNotFoundException e) {
			e.printStackTrace();

			throw new Exception("User not found");
		}

		// authenticate

		UserDetails loadUserByUsername = this.UserDetailsServiceImpl.loadUserByUsername(jwtRequest.getUsername());

		String generatedToken = this.jwtUtil.generateToken(loadUserByUsername);

		return ResponseEntity.ok(generatedToken);

	}

	private void authenticate(String username, String password) throws Exception {

		try {

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		} catch (DisabledException e) {
			throw new Exception("USER DISABLED");
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid Credentials" + e.getMessage());
		}

	}
}