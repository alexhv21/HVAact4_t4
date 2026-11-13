package com.ito.hva_act4_t4.service;

import com.ito.hva_act4_t4.dto.AuthResponse;
import com.ito.hva_act4_t4.dto.LoginRequest;
import com.ito.hva_act4_t4.dto.RegisterRequest;
import com.ito.hva_act4_t4.entity.Rol;
import com.ito.hva_act4_t4.entity.Usuario;
import com.ito.hva_act4_t4.repository.UsuarioRepository;
import com.ito.hva_act4_t4.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Rol.ROLE_USER
        );

        usuarioRepository.save(usuario);
        String jwtToken = jwtService.generateToken(usuario);
        return new AuthResponse(jwtToken, usuario.getUsername());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        String jwtToken = jwtService.generateToken(usuario);
        return new AuthResponse(jwtToken, usuario.getUsername());
    }
}