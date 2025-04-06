package lev.working.BootSecurity.controller;

import lev.working.BootSecurity.models.User;
import lev.working.BootSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        try {
            User user = userService.findById(id);
            model.addAttribute("user", user);
            return "userProfile";
        } catch (Exception e) {
            model.addAttribute("error", "Пользователь не найден: " + e.getMessage());
            return "error";
        }
    }
}
