package ru.sinforge.practice5.DTO;

import lombok.Data;

@Data
public class UserRegisterDTO {
    public String email;
    public String firstName;
    public String lastName;
    public String password;
    public final String role = "USER";
}
