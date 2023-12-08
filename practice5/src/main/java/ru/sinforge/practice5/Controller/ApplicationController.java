package ru.sinforge.practice5.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.sinforge.practice5.DTO.ApplicationDTO;
import ru.sinforge.practice5.DTO.UserDTO;
import ru.sinforge.practice5.Entity.Application;
import ru.sinforge.practice5.Entity.Maintenance;
import ru.sinforge.practice5.Entity.User;
import ru.sinforge.practice5.Service.ApplicationService;
import ru.sinforge.practice5.Service.MaintenanceService;
import ru.sinforge.practice5.Service.UserService;
import ru.sinforge.practice5.Service.impl.EmailService;

import java.util.List;
import java.util.Objects;

@Controller
public class ApplicationController {
    private final ApplicationService applicationService;
    private final UserService userService;
    private final EmailService emailService;
    private final MaintenanceService maintenanceService;

    @Autowired
    public ApplicationController(ApplicationService applicationService,
                                 UserService userService,
                                 EmailService emailService,
                                 MaintenanceService maintenanceService) {
        this.applicationService = applicationService;
        this.userService = userService;
        this.emailService = emailService;
        this.maintenanceService = maintenanceService;
    }

    @PostMapping("/application")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String createApplication(@ModelAttribute("application") ApplicationDTO applicationDTO,
                                         Authentication authentication) {
        String id = authentication.getPrincipal().toString();

        RestTemplate restTemplate = new RestTemplate();
        UserDTO existingUser = restTemplate.getForObject("http://auth:8081/api/auth/user?id=" + id,
                UserDTO.class);

        Maintenance maintenance = maintenanceService.findById(applicationDTO.getId_maintenance());
        User user = userService.findByEmail(Objects.requireNonNull(existingUser).getEmail());

        emailService.sendEmail(Objects.requireNonNull(existingUser).getEmail(),
                "Созданная заявка",
                "Уважаемый " + existingUser.getFirstName()
                        + " " + existingUser.getLastName() +
                        ", ваша заявка на услугу " + maintenance.getName() +
                        " на следующую дату: " + applicationDTO.getDate() +
                        " была принята. Ожидаем вас в указанный день!");
        Application application = new Application(null, applicationDTO.getDate(), maintenance);
        applicationService.add(application);
        user.applicationList.add(application);
        userService.add(user);
        return "redirect:/application";
    }
    @GetMapping("/application")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String getApplicationPage(Model model, ApplicationDTO applicationDTO) {

        List<Maintenance> maintenances = maintenanceService.findAll();
        model.addAttribute("application", applicationDTO);
        model.addAttribute("maintenances", maintenances);
        return "applicationPage";
    }

}
