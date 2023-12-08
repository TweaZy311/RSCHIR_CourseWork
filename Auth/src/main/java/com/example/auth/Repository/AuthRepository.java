package com.example.auth.Repository;


import com.example.auth.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Client, Long> {
    Client getClientByEmailAndPassword(String email, String password);
    Client findByEmail(String email);
}