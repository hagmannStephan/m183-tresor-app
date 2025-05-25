package ch.bbw.pr.tresorbackend.service.impl;

import ch.bbw.pr.tresorbackend.model.PasswordResetToken;
import ch.bbw.pr.tresorbackend.model.User;
import ch.bbw.pr.tresorbackend.repository.PasswordResetTokenRepository;
import ch.bbw.pr.tresorbackend.service.EmailService;
import ch.bbw.pr.tresorbackend.service.UserService;
import ch.bbw.pr.tresorbackend.service.PasswordResetService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private PasswordResetTokenRepository tokenRepository;
    private EmailService emailService;
    
    @Autowired
    private UserService userService;

    @Override
    public void createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(token);
        passwordResetToken.setExpiresAt(expiresAt);

        tokenRepository.save(passwordResetToken);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        String body = "Hi " + user.getFirstName() + ",\n\nClick this link to reset your password:\n" + resetLink;

        emailService.sendEmail(user.getEmail(), "Reset your password", body);
    }

    @Override
    public void resetPassword(String token, String password) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        String hashedPassword = new BCryptPasswordEncoder().encode(password);

        // Save updated userW
        userService.updatePassword(user.getId(), hashedPassword);

        // Invalidate token
        tokenRepository.delete(resetToken);
    }
}