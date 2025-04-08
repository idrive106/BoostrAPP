package lev.working.BootSecurity.service;

import jakarta.persistence.EntityNotFoundException;
import lev.working.BootSecurity.models.Role;
import lev.working.BootSecurity.models.User;
import lev.working.BootSecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService{

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(RoleService roleService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        Optional<User> foundPerson = userRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void toggleLock(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setAccountNonLocked(!user.isAccountNonLocked());
        userRepository.save(user);
    }

    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void save(User user, List<Role> roles) {

        if (userRepository.findByName(user.getUsername()).isPresent()) {
            throw new RuntimeException("User with this login already exists!");
        }

        if (roles.isEmpty()) {
            roles.addAll(roleService.defaultRole());
        }
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Transactional
    public void update(User updatedUser, List<Long> roles, Long id) {
        User updUser = findById(id);
        if (updUser != null) {
            updUser.setName(updatedUser.getName());
            updUser.setJobFunction(updatedUser.getJobFunction());
            updUser.setAge(updatedUser.getAge());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                updUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            if (roles != null && !roles.isEmpty()) {
                updUser.setRoles(roleService.findRoleById(roles));
            }
            userRepository.save(updUser);
        } else {
             throw new EntityNotFoundException("User not found with ID " + id);
        }
    }


    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}