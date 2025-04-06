package lev.working.BootSecurity.config;

import lev.working.BootSecurity.models.Role;
import lev.working.BootSecurity.models.User;
import lev.working.BootSecurity.repositories.RoleRepository;
import lev.working.BootSecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        if (userRepository.findByName("admin").isEmpty()) {
            User admin = new User();
            admin.setName("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setJobFunction("SysAdmin");
            admin.setAge(0);
            admin.setRoles(List.of(adminRole, userRole));
            userRepository.save(admin);
        }
    }
}
