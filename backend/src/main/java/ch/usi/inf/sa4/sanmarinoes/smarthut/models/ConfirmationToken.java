package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "confirmation_token", unique = true)
    private String confirmToken;

    public Date getCreatedDate() {
        return new Date(createdDate.getTime());
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = new Date(createdDate.getTime());
    }

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private boolean resetPassword;

    public ConfirmationToken(User user) {
        this.user = user;
        createdDate = new Date();
        confirmToken = UUID.randomUUID().toString();
        resetPassword = false;
    }
}
