package ru.sinforge.practice5.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String email;
    public String firstName;
    public String lastName;
    public String password;
    public String role;

    @OneToMany(fetch = FetchType.LAZY)
    public List<Application> applicationList;
}
