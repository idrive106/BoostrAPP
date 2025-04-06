package lev.working.BootSecurity.controller;

import lev.working.BootSecurity.models.Role;
import lev.working.BootSecurity.models.User;
import lev.working.BootSecurity.service.RoleService;
import lev.working.BootSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String showAdminPanel(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/toggleLock/{id}")
    public String toggleUserLock(@PathVariable Long id) {
        userService.toggleLock(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getRoles());
            return "editUser";
        } else {
            return "redirect:/admin?error=userNotFound";
        }
    }

    @PostMapping("/admin/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") User updatedUser,
                             @RequestParam(value = "roles", required = false) List<Long> roles,
                             Model model) {
        try {
            if (roles == null || roles.isEmpty()) {
                roles = roleService.getRolesByUserId(id)
                        .stream()
                        .map(Role::getId)
                        .collect(Collectors.toList());
            }

            userService.update(updatedUser, roles, id);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка обновления пользователя.");
            return "editUser";
        }
    }
}
