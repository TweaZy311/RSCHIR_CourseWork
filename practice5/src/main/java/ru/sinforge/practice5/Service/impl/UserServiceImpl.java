package ru.sinforge.practice5.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sinforge.practice5.DTO.JwtResponse;
import ru.sinforge.practice5.Entity.User;
import ru.sinforge.practice5.Repository.UserRepository;
import ru.sinforge.practice5.Service.UserService;

import java.util.List;

@Service
class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        JwtResponse user = restTemplate.getForObject("http://auth:8081/api/auth/validate?jwt=" + username, JwtResponse.class);
        if (user == null || user.id == 0 || user.role == null || user.role.equals("") || user.email == null || user.email.equals("")) {
            throw new UsernameNotFoundException("Jwt token expired or incorrect");
        }
        UserDets userDetails = new UserDets();
        userDetails.email = String.valueOf(user.id);
        userDetails.authorities = List.of(user.role);
        userDetails.password = null;
        return userDetails;
    }

    @Override
    public User add(User user){
        return repository.save(user);
    }
    @Override
    public User findById(Long id){
        return repository.findById(id).orElse(null);
    }
    @Override
    public List<User> findAll(){
        return repository.findAll();
    }
    @Override
    public void deleteById(Long id){
        repository.deleteById(id);
    }
    @Override
    public User findByEmail(String email){
        return repository.findByEmail(email);
    }

}