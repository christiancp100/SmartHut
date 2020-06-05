package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.JWTTokenUtils;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.JWTRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.JWTResponse;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.UnauthorizedException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.UserNotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.UserRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.JWTUserDetailsService;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JWTTokenUtils jwtTokenUtils;

    private final JWTUserDetailsService userDetailsService;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JWTTokenUtils jwtTokenUtils,
            JWTUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public JWTResponse login(@Valid @RequestBody JWTRequest authenticationRequest)
            throws UnauthorizedException, UserNotFoundException {
        final UserDetails userDetails;
        if (authenticationRequest.getUsernameOrEmail().contains("@")) {
            // usernameOrEmail contains an email, so fetch the corresponding username
            final User user =
                    userRepository.findByEmailIgnoreCase(
                            authenticationRequest.getUsernameOrEmail());
            if (user == null) {
                throw new UserNotFoundException();
            }

            authenticate(user.getUsername(), authenticationRequest.getPassword());
            userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        } else {
            // usernameOrEmail contains a username, authenticate with that then
            authenticate(
                    authenticationRequest.getUsernameOrEmail(),
                    authenticationRequest.getPassword());
            userDetails =
                    userDetailsService.loadUserByUsername(
                            authenticationRequest.getUsernameOrEmail());
        }

        final String token = jwtTokenUtils.generateToken(userDetails);
        return new JWTResponse(token);
    }

    @GetMapping("/profile")
    public User profile(final Principal principal) {
        return userRepository.findByUsername(principal.getName());
    }

    private void authenticate(String username, String password) throws UnauthorizedException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new UnauthorizedException(true, e);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException(false, e);
        }
    }
}
