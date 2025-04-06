package lev.working.BootSecurity.service;

import lev.working.BootSecurity.models.Role;
import lev.working.BootSecurity.models.User;
import lev.working.BootSecurity.repositories.RoleRepository;
import lev.working.BootSecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public List<Role> findRoleById(List<Long> id) {
        return roleRepository.findAllById(id);
    }

    public List<Role> defaultRole() {
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Роль по умолчанию не найдена"));
        return Collections.singletonList(defaultRole);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public List<Role> getRolesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return user.getRoles();
    }
}
