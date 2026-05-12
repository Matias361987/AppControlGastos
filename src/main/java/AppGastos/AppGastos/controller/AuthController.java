package AppGastos.AppGastos.controller;

import AppGastos.AppGastos.dto.LoginRequest;
import AppGastos.AppGastos.model.User;
import AppGastos.AppGastos.repository.UserRepository;
import AppGastos.AppGastos.security.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        // 1. Autenticación con Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2. Generar el JWT
        String jwt = jwtUtils.generateTokenFromUsername(authentication.getName());

        // 3. Crear y configurar la Cookie HttpOnly para Thymeleaf
        Cookie cookie = new Cookie("jwt-token", jwt);
        cookie.setHttpOnly(true); // Protege contra XSS
        cookie.setSecure(false);  // Cambiar a true cuando uses HTTPS en Render
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // Expira en 1 día

        response.addCookie(cookie);

        return ResponseEntity.ok("Sesión iniciada correctamente");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Error: El nombre de usuario ya está en uso");
        }

        // Encriptar la contraseña antes de guardarla (SOLID: Seguridad primero)
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Usuario registrado con éxito");
    }
}