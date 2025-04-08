package lev.working.BootSecurity.service;

import lev.working.BootSecurity.models.Role;
import lev.working.BootSecurity.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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
}
