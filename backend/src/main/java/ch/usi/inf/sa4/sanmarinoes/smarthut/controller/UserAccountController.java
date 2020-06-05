package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.EmailConfigurationService;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.InitPasswordResetRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.PasswordResetRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.UserRegistrationRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.DuplicateRegistrationException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.EmailTokenNotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.UserNotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ConfirmationToken;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ConfirmationTokenRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.UserRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.EmailSenderService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/** Unauthenticated set of endpoints to handle registration and password reset */
@RestController
@EnableAutoConfiguration
@RequestMapping("/register")
public class UserAccountController {

    private final UserRepository userRepository;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final EmailSenderService emailSenderService;

    private final BCryptPasswordEncoder encoder;

    private final EmailConfigurationService emailConfig;

    public UserAccountController(
            UserRepository userRepository,
            ConfirmationTokenRepository confirmationTokenRepository,
            EmailSenderService emailSenderService,
            BCryptPasswordEncoder encoder,
            EmailConfigurationService emailConfig) {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSenderService = emailSenderService;
        this.encoder = encoder;
        this.emailConfig = emailConfig;
    }

    private void sendEmail(String email, ConfirmationToken token, boolean isRegistration) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(
                isRegistration
                        ? emailConfig.getRegistrationSubject()
                        : emailConfig.getResetPasswordSubject());
        mailMessage.setFrom("smarthut.sm@gmail.com");
        mailMessage.setText(
                (isRegistration ? emailConfig.getRegistration() : emailConfig.getResetPassword())
                        + " "
                        + (isRegistration
                                ? emailConfig.getRegistrationPath()
                                : emailConfig.getResetPasswordPath())
                        + token.getConfirmToken());

        emailSenderService.sendEmail(mailMessage);
    }

    /**
     * Unauthenticated endpoint to call to send a password reset email
     *
     * @param registrationData registration data of the new user
     * @return success
     * @throws DuplicateRegistrationException if a user exists with same email or username
     */
    @PostMapping
    public void registerUser(@Valid @RequestBody UserRegistrationRequest registrationData)
            throws DuplicateRegistrationException {
        final User existingEmailUser =
                userRepository.findByEmailIgnoreCase(registrationData.getEmail());
        final User existingUsernameUser =
                userRepository.findByUsername(registrationData.getUsername());

        // Check if an User with the same email already exists
        if (existingEmailUser != null || existingUsernameUser != null) {
            throw new DuplicateRegistrationException();
        } else {
            final User toSave = new User();
            // disable the user (it will be enabled on email confiration)
            toSave.setEnabled(false);

            // encode user's password
            toSave.setPassword(encoder.encode(registrationData.getPassword()));

            // set other fields
            toSave.setName(registrationData.getName());
            toSave.setUsername(registrationData.getUsername());
            toSave.setEmail(registrationData.getEmail());
            userRepository.save(toSave);

            ConfirmationToken token = new ConfirmationToken(toSave);

            confirmationTokenRepository.save(token);

            sendEmail(toSave.getEmail(), token, true);
        }
    }

    /**
     * Unauthenticated endpoint to call to send a password reset email
     *
     * @param resetRequest a JSON object containing the email of the user to reset
     * @return success
     * @throws UserNotFoundException if given email does not belong to any user
     */
    @PostMapping("/init-reset-password")
    public void initResetPassword(@Valid @RequestBody InitPasswordResetRequest resetRequest)
            throws UserNotFoundException {
        final User toReset = userRepository.findByEmailIgnoreCase(resetRequest.getEmail());

        // Check if an User with the same email already exists
        if (toReset == null) {
            throw new UserNotFoundException();
        }

        ConfirmationToken token;
        do {
            token = new ConfirmationToken(toReset);
            token.setResetPassword(true);
        } while (confirmationTokenRepository.findByConfirmToken(token.getConfirmToken()) != null);

        // Delete existing email password reset tokens
        confirmationTokenRepository.deleteByUserAndResetPassword(toReset, true);

        // Save new token
        confirmationTokenRepository.save(token);

        sendEmail(toReset.getEmail(), token, false);
    }

    /**
     * Unauthenticated endpoint to call with token sent by email to reset password
     *
     * @param resetRequest the token given via email and the new password
     * @return success
     * @throws EmailTokenNotFoundException if given token is not a valid token for password reset
     */
    @PutMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody PasswordResetRequest resetRequest)
            throws EmailTokenNotFoundException {
        final ConfirmationToken token =
                confirmationTokenRepository.findByConfirmToken(resetRequest.getConfirmationToken());

        if (token == null || !token.isResetPassword()) {
            throw new EmailTokenNotFoundException();
        }

        final User user = token.getUser();
        user.setPassword(encoder.encode(resetRequest.getPassword()));
        userRepository.save(user);

        // Delete token to prevent further password changes
        confirmationTokenRepository.delete(token);
    }

    /**
     * Unauthenticated endpoint to call with token sent by email to enable user
     *
     * @param confirmationToken the token given via email
     * @return success
     * @throws EmailTokenNotFoundException if given token is not a valid token for email
     *     confirmation
     */
    @GetMapping(value = "/confirm-account")
    public void confirmUserAccount(
            @RequestParam("token") @NotNull String confirmationToken,
            final HttpServletResponse response)
            throws EmailTokenNotFoundException, IOException {
        final ConfirmationToken token =
                confirmationTokenRepository.findByConfirmToken(confirmationToken);

        if (token != null && !token.isResetPassword()) {
            token.getUser().setEnabled(true);
            userRepository.save(token.getUser());
            response.sendRedirect(emailConfig.getRegistrationRedirect());
        } else {
            throw new EmailTokenNotFoundException();
        }
    }
}
