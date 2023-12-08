package ru.sinforge.practice5.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;


@Controller
public class UserController {

    @PatchMapping("/api/user/change-role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void changeUserRoleToAdmin(@RequestParam Long userId) {
        WebClient webClient = WebClient.create("http://auth:8081");
        webClient.patch()
                .uri("/api/auth/change-role?userId=" + userId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
