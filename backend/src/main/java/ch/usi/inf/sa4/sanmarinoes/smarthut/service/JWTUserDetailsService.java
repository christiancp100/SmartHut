package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.UserRepository;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JWTUserDetailsService implements UserDetailsService {
    @Autowired private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User toReturn = repository.findByUsername(username);
        if (toReturn != null && toReturn.isEnabled()) {
            return new org.springframework.security.core.userdetails.User(
                    toReturn.getUsername(), toReturn.getPassword(), Set.of());
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
