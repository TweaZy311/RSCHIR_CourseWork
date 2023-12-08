package com.example.auth.Service;

import com.example.auth.Entity.Client;
import com.example.auth.Entity.ClientDto;
import com.example.auth.Entity.JwtResponse;
import com.example.auth.Repository.AuthRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;
    private final AuthRepository authRepository;

    @Autowired
    public AuthService(JwtProvider jwtProvider, AuthRepository authRepository) {
        this.jwtProvider = jwtProvider;
        this.authRepository = authRepository;
    }

    public ResponseEntity<String> login(ClientDto clientDto) {
        Client client = authRepository.getClientByEmailAndPassword(clientDto.getEmail(), clientDto.getPassword());
        if(client != null) {
            return new ResponseEntity<>(jwtProvider.generateToken(client), HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(400));

    }

    public String register(Client client) {
        if (authRepository.findByEmail(client.getEmail()) != null){
            return "USER WITH THIS LOGIN ALREADY EXISTS";
        }
        authRepository.save(client);
        return "SUCCESS";
    }

    public JwtResponse validateToken(String jwt) {
        Claims claims = jwtProvider.validateToken(jwt);
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.id = claims.get("id", Integer.class);
        jwtResponse.role = claims.get("role", String.class);
        jwtResponse.email = claims.get("email", String.class);
        return jwtResponse;
    }

    public String changeUserRoleToAdmin(Long userId) {
        Client client = authRepository.findById(userId).orElse(null);
        if (client != null) {
            client.role = "ADMIN";
            authRepository.saveAndFlush(client);
            return "SUCCESS";
        }
        return "NO SUCH USER";
    }

    public List<Client> getAllUsers(){
        return authRepository.findAll();
    }

    public Client getClientByEmail(String email){
        return authRepository.findByEmail(email);
    }
    public Client getUserById(Long id){
        return authRepository.findById(id).orElse(null);
    }
}
