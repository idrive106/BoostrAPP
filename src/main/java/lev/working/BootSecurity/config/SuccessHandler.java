package lev.working.BootSecurity.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lev.working.BootSecurity.models.User;
import lev.working.BootSecurity.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class SuccessHandler implements AuthenticationSuccessHandler {

    UserService userService;

    public SuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String username = authentication.getName();

        User user = userService.findByUsername(username);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));

        if (isAdmin) {
            response.sendRedirect("/admin");
        } else if (isUser) {
            response.sendRedirect("/user/" + user.getId());
        } else {
            response.sendRedirect("/login");
        }
    }
}