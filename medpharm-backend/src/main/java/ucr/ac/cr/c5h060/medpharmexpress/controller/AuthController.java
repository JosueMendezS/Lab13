package ucr.ac.cr.c5h060.medpharmexpress.controller;

import ucr.ac.cr.c5h060.medpharmexpress.dto.AuthResponseDTO;
import ucr.ac.cr.c5h060.medpharmexpress.dto.LoginRequestDTO;
import ucr.ac.cr.c5h060.medpharmexpress.domain.Usuario;
import ucr.ac.cr.c5h060.medpharmexpress.repository.UsuarioRepository;
import ucr.ac.cr.c5h060.medpharmexpress.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        // Lanza BadCredentialsException si las credenciales son inválidas
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        Usuario u = usuarioRepository.findByUsername(request.username()).orElseThrow();
        String token = jwtUtils.generateToken(u.getUsername(), u.getRol());

        return ResponseEntity.ok(new AuthResponseDTO(token, u.getUsername(), u.getRol()));
    }
}