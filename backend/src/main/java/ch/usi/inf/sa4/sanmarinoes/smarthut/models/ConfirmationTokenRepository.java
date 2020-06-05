package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmToken(String confirmToken);

    ConfirmationToken findByUser(User user);

    @Transactional
    void deleteByUserAndResetPassword(User user, boolean resetPassword);
}
