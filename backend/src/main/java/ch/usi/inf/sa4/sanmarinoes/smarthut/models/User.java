package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** A user of the Smarthut application */
@Entity(name = "smarthutuser")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @ApiModelProperty(hidden = true)
    @Getter
    @Setter
    private Long id;

    /** The full name of the user */
    @Column(nullable = false)
    @Getter
    @Setter
    private String name;

    /** The full username of the user */
    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String username;

    /** A properly salted way to store the password */
    @Column(nullable = false)
    @GsonExclude
    @Getter
    @Setter
    private String password;

    /**
     * The user's email (validated according to criteria used in <code>&gt;input type="email"&lt;>
     * </code>, technically not RFC 5322 compliant
     */
    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String email;

    /** Guests invited by this user */
    @ManyToMany(mappedBy = "hosts", cascade = CascadeType.DETACH)
    @GsonExclude
    @Getter
    @ToString.Exclude
    private Set<User> guests = new HashSet<>();

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "invited",
            joinColumns = @JoinColumn(name = "guest_id"),
            inverseJoinColumns = @JoinColumn(name = "host_id"))
    @GsonExclude
    @Getter
    @ToString.Exclude
    private Set<User> hosts = new HashSet<>();

    /** Determines whether a guest can access security cameras */
    @Column(nullable = false)
    @Getter
    @Setter
    private boolean cameraEnabled;

    @Column(nullable = false)
    @GsonExclude
    @Getter
    @Setter
    private boolean isEnabled = false;

    @Override
    public int hashCode() {
        if (id == null) return Integer.MAX_VALUE;
        return (int) (id % Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    public void addGuest(User guest) {
        this.guests.add(guest);
    }

    public void addHost(User host) {
        this.hosts.add(host);
    }
}
