package com.example.demosecurity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootApplication
@RestController
public class DemosecurityApplication {
	NetHttpTransport transport = new NetHttpTransport();
	JsonFactory jsonFactory = new GsonFactory();
	GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
	.setAudience(Collections.singletonList("1035876984835-2kavhl9icu4ib93jttmbabk3qvhokdh9.apps.googleusercontent.com"))
	.build();
	
	public static void main(String[] args) {
		SpringApplication.run(DemosecurityApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello";
	}

	@PostMapping("/api/auth/login")
	public void login(HttpServletResponse response,@RequestBody LoginRequest data) {
	
		String token =data.getIdToken();
		System.out.println(token);
		GoogleIdToken idToken = null;	
		try {
			idToken = verifier.verify(token);
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		System.out.println(idToken);
			Payload payload = idToken.getPayload();
		
			// Print user identifier
			String userId = payload.getSubject();
			System.out.println("User ID: " + userId);
			// Get profile information from payload
			String email = ((com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload) payload).getEmail();
			boolean emailVerified = Boolean.valueOf(((com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload) payload).getEmailVerified());
			String name = (String) payload.get("name");
			String pictureUrl = (String) payload.get("picture");
			String locale = (String) payload.get("locale");
			String familyName = (String) payload.get("family_name");
			String givenName = (String) payload.get("given_name");
		

		// System.out.println(data.getIdToken());
		System.out.println("here under");
		Cookie cookie = new Cookie("token", "abcToken.xyzMNK");
		cookie.setHttpOnly(true);
		cookie.setMaxAge((60 * 60 * 24 * 30));
		response.addCookie(cookie);
	}

}
