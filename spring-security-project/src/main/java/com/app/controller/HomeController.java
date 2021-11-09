package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.AuthRequest;
import com.app.model.AuthResponse;
import com.app.service.CustomUserDetailsService;
import com.app.utils.JwtUtils;

@RestController
public class HomeController {

	@Autowired
	AuthenticationManager authenticationmanager;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	JwtUtils jwtUtils;

	@GetMapping("/home")
	public String homeController() {
		return "Inside private HomeController";

	}

	@PostMapping("/auth")
	public ResponseEntity<?> createAuthToken(@RequestBody AuthRequest authReq) throws Exception {

		System.out.println("inside auth");
		try {
			
			authenticationmanager
					.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword())

					);

		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authReq.getUsername());

		final String jwt = jwtUtils.generateToken(userDetails);

		return ResponseEntity.ok(new AuthResponse(jwt));

	}

}
