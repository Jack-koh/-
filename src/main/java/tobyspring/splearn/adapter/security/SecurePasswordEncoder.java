package tobyspring.splearn.adapter.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import tobyspring.splearn.domain.member.PasswordEncoder;

@Component
public class SecurePasswordEncoder implements PasswordEncoder {
    private final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String password) {
        return bcryptPasswordEncoder.encode(password);
    }

    @Override
    public boolean matches(String password, String passwordHash) {
        return bcryptPasswordEncoder.matches(password, passwordHash);
    }
}
