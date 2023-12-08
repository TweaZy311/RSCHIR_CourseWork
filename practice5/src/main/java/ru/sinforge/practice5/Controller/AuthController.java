package ru.sinforge.practice5.Controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.sinforge.practice5.DTO.JwtResponse;
import ru.sinforge.practice5.DTO.UserLoginDTO;
import ru.sinforge.practice5.DTO.UserRegisterDTO;
import ru.sinforge.practice5.Entity.User;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String getLoginPage(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") User user){
        return "auth/registration";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserLoginDTO userLoginDTO,
                        HttpServletResponse response){
        RestTemplate restTemplate = new RestTemplate();
        String jwt = restTemplate.postForObject("http://auth:8081/api/auth/login", userLoginDTO, String.class);
        Cookie cookie = new Cookie("token", jwt);
        cookie.setMaxAge(600);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
        return "redirect:/";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") UserRegisterDTO user){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://auth:8081/api/auth/reg", user, String.class);
        return "redirect:/auth/login";
    }

}
