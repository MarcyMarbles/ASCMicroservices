package kz.saya.finals.authservice.Controllers;

import kz.saya.finals.authservice.Service.JwtUtility;
import kz.saya.finals.common.DTOs.AuthRequestDTO;
import kz.saya.finals.common.DTOs.RegisterRequestDTO;
import kz.saya.finals.common.DTOs.UserDTO;
import kz.saya.finals.feigns.Clients.UserServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;
    private final JwtUtility jwtUtils;

    public AuthController(PasswordEncoder passwordEncoder,
                          UserServiceClient userServiceClient,
                          JwtUtility jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userServiceClient = userServiceClient;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequestDTO) {
        UserDTO user = null;
        try {
            user = userServiceClient.getByLogin(authRequestDTO.getLogin());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("User not found");
        }

        boolean passwordMatch = passwordEncoder.matches(
                authRequestDTO.getPassword(),
                user.getPassword()
        );

        if (!passwordMatch) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtils.generateToken(user.getLogin(), user.getRoles());

        return ResponseEntity.ok(Map.of("token", token));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        if (userServiceClient.userExists(registerRequestDTO.getLogin())) {
            return ResponseEntity.badRequest().body("User already exists!");
        }
        registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        boolean response = userServiceClient.createUser(registerRequestDTO);
        if (!response) {
            return ResponseEntity.badRequest().body("Unexpected error occurred! Try again later.");
        }
        return ResponseEntity.ok("Registration successful!");
    }
}
