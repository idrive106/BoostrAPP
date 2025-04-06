package lev.working.BootSecurity.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage;

        if (exception instanceof LockedException) {
            errorMessage = "Ваш аккаунт заблокирован";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Неверный логин или пароль";
        } else {
            errorMessage = "Ошибка аутентификации";
        }

        request.getSession().setAttribute("error", errorMessage);
        response.sendRedirect("/login?error=true");
    }
}
