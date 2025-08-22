package com.ny.safeny.controller;

import com.ny.safeny.dto.AuthRequest;
import com.ny.safeny.dto.AuthResponse;
import com.ny.safeny.dto.RegisterRequest;
import com.ny.safeny.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            System.out.println("=== Login Request ===");
            System.out.println("Username: " + request.getUsername());
            
            AuthResponse response = authService.login(request);
            
            System.out.println("Login successful! Token: " + response.getToken().substring(0, 20) + "...");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            System.out.println("=== Registration Request ===");
            System.out.println("Username: " + request.getUsername());
            System.out.println("Full Name: " + request.getFullName());
            System.out.println("Email: " + request.getEmail());
            System.out.println("Phone: " + request.getPhone());
            
            AuthResponse response = authService.register(request);
            
            System.out.println("=== Registration Successful ===");
            System.out.println("Username: " + response.getUsername());
            System.out.println("Token: " + response.getToken().substring(0, 20) + "...");
            System.out.println("Returning response to client...");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            System.err.println("Registration error (RuntimeException): " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            System.err.println("Unexpected registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
