package lev.working.BootSecurity.controller;

import lev.working.BootSecurity.models.User;
import lev.working.BootSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user/{id}")
    public String viewUser(Model model, Principal principal, @PathVariable Long id) {
        User user = userService.findById(id);
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        return "userProfile";
    }
}