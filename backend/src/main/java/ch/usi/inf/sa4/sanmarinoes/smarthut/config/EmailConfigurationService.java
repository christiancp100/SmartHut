package ch.usi.inf.sa4.sanmarinoes.smarthut.config;

import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Class to interface with `email.*` properties in application.properties. This properties are used
 * for generating the email to send on password reset or registration
 *
 * @see ch.usi.inf.sa4.sanmarinoes.smarthut.controller.UserAccountController
 */
@Component
@Validated
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "email")
public class EmailConfigurationService {

    /** The email subject for a registration email */
    @NotNull private String registrationSubject;

    /** The text in the email body preceding the confirmation URL for a registration email */
    @NotNull private String registration;

    /**
     * The URL to follow for registration email confirmation. Has to end with the start of a query
     * parameter
     */
    @NotNull private String registrationPath;

    /**
     * The URL to follow for password reset email confirmation. Has to end with the start of a query
     * parameter
     */
    @NotNull private String resetPasswordPath;

    /** The email subject for a reset password email */
    @NotNull private String resetPasswordSubject;

    /** The text in the email body preceding the confirmation URL for a reset password email */
    @NotNull private String resetPassword;

    @NotNull private String resetPasswordRedirect;

    @NotNull private String registrationRedirect;

    public synchronized String getRegistrationSubject() {
        return registrationSubject;
    }

    public synchronized void setRegistrationSubject(String registrationSubject) {
        this.registrationSubject = registrationSubject;
    }

    public synchronized String getRegistration() {
        return registration;
    }

    public synchronized void setRegistration(String registration) {
        this.registration = registration;
    }

    public synchronized String getRegistrationPath() {
        return registrationPath;
    }

    public synchronized void setRegistrationPath(String registrationPath) {
        this.registrationPath = registrationPath;
    }

    public synchronized String getResetPasswordSubject() {
        return resetPasswordSubject;
    }

    public synchronized void setResetPasswordSubject(String resetPasswordSubject) {
        this.resetPasswordSubject = resetPasswordSubject;
    }

    public synchronized String getResetPassword() {
        return resetPassword;
    }

    public synchronized void setResetPassword(String resetPassword) {
        this.resetPassword = resetPassword;
    }

    public synchronized String getResetPasswordPath() {
        return resetPasswordPath;
    }

    public synchronized void setResetPasswordPath(String resetPasswordPath) {
        this.resetPasswordPath = resetPasswordPath;
    }

    public synchronized String getResetPasswordRedirect() {
        return resetPasswordRedirect;
    }

    public synchronized void setResetPasswordRedirect(String resetPasswordRedirect) {
        this.resetPasswordRedirect = resetPasswordRedirect;
    }

    public synchronized String getRegistrationRedirect() {
        return registrationRedirect;
    }

    public synchronized void setRegistrationRedirect(String registrationRedirect) {
        this.registrationRedirect = registrationRedirect;
    }
}
