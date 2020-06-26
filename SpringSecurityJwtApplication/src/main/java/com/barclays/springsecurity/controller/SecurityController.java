package com.barclays.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.springsecurity.model.AuthenticationRequest;
import com.barclays.springsecurity.model.AuthenticationResponse;
import com.barclays.springsecurity.security.MyUserDetailsService;
import com.barclays.springsecurity.util.JwtUtil;

@RestController
public class SecurityController {

		@Autowired
		private AuthenticationManager authenticationManager;
		@Autowired
		private MyUserDetailsService myUserDetailsService;
		@Autowired
		private JwtUtil jwtTokenUtil;
		
		@RequestMapping("/hello")
		public String checkIndex() {
			return "Hello Index";
		}
		
		@RequestMapping(value="/authenticate",method=RequestMethod.POST)
		public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
			try {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
			}
			catch(BadCredentialsException e) {
				throw new Exception("Incorrect Username or Password"+e);
			}
			final UserDetails userDetails=myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			final String jwt=jwtTokenUtil.generateToken(userDetails);
			return ResponseEntity.ok(new AuthenticationResponse(jwt));
		}
}
