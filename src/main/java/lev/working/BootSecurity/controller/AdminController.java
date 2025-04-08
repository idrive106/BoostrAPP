package lev.working.BootSecurity.controller;

import lev.working.BootSecurity.models.Role;
import lev.working.BootSecurity.models.User;
import lev.working.BootSecurity.service.RoleService;
import lev.working.BootSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public String showAdminPanel(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        List<User> users = userService.findAll();
        List<Role> roles = roleService.getRoles();
        model.addAttribute("activeTab", "admin");
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("roles", roles);
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
                             @RequestParam String name,
                             @RequestParam String jobFunction,
                             @RequestParam int age,
                             @RequestParam(required = false) String password,
                             @RequestParam(required = false, name = "roles") Long[] rolesArray) {

        User updatedUser = new User();
        updatedUser.setName(name);
        updatedUser.setJobFunction(jobFunction);
        updatedUser.setAge(age);
        updatedUser.setPassword(password);
        List<Long> roleIds = (rolesArray != null) ? Arrays.asList(rolesArray) : new ArrayList<>();

        userService.update(updatedUser, roleIds, id);

        return "redirect:/admin";
    }

    @GetMapping("/admin/adminProfile")
    public String showAdminProfile(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("activeTab", "adminProfile");
        model.addAttribute("user", currentUser);
        return "adminProfile";
    }

    @GetMapping("/admin/addUser")
    public String addUser(@RequestParam(value = "created", required = false) Boolean created, Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("activeTab", "admin");
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("created", created);
        return "/addUser";
    }

    @PostMapping("/admin/addUser")
    public String addUser(@ModelAttribute User user,
                          @RequestParam(value = "roles", required = false) List<Long> roleIds,
                          Model model) {
        try {
            List<Role> roles;

            if (roleIds == null || roleIds.isEmpty()) {
                roles = roleService.defaultRole();
            } else {
                roles = roleService.findRoleById(roleIds);
            }
            userService.save(user, roles);
            return "redirect:/admin/addUser?created=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при создании пользователя: " + e.getMessage());
            return "/addUser";
        }
    }
}