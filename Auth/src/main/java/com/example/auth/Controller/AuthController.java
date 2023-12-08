package com.example.auth.Controller;

import com.example.auth.Entity.Client;
import com.example.auth.Entity.ClientDTOAdmin;
import com.example.auth.Entity.ClientDto;
import com.example.auth.Entity.JwtResponse;
import com.example.auth.Repository.AuthRepository;
import com.example.auth.Service.AuthService;
import com.example.auth.Service.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody ClientDto clientDto) {
        return authService.login(clientDto);
    }

    @PostMapping("/reg")
    public String register(@RequestBody Client client) {
        return authService.register(client);
    }

    @GetMapping("/validate")
    public JwtResponse validateToken(@RequestParam String jwt) {
        return authService.validateToken(jwt);
    }

    @PatchMapping("/change-role")
    public String changeUserRoleToSeller(@RequestParam Long userId) {
        return authService.changeUserRoleToAdmin(userId);
    }

    @GetMapping("/users")
    public List<ClientDTOAdmin> getAllUsers() {
        List<Client> clients = authService.getAllUsers();
        return clients.stream()
                .map(client -> {
                    ClientDTOAdmin clientDTOAdmin = new ClientDTOAdmin();
                    clientDTOAdmin.setEmail(client.getEmail());
                    clientDTOAdmin.setRole(client.getRole());
                    return clientDTOAdmin;
                })
                .collect(Collectors.toList());
    }
    @GetMapping("/user")
    public Client getUserByEmail(@RequestParam Long id){
        return authService.getUserById(id);
    }
}