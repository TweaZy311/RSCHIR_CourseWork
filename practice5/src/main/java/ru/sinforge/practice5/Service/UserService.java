package ru.sinforge.practice5.Service;

import ru.sinforge.practice5.Entity.User;

import java.util.List;

public interface UserService{
    User add(User user);
    User findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
    User findByEmail(String email);
}
