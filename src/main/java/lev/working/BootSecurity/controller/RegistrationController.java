package lev.working.BootSecurity.controller;

import lev.working.BootSecurity.models.Role;
import lev.working.BootSecurity.models.User;
import lev.working.BootSecurity.service.RoleService;
import lev.working.BootSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class RegistrationController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RegistrationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user, Model model) {
        try {
            List<Role> roles = roleService.defaultRole();
            userService.save(user, roles);
            return "redirect:/login?registrationSuccess=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при создании пользователя: " + e.getMessage());
            return "createUser";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
