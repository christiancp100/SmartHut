package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;

/**
 * Represent a collection of state changes to devices even in different rooms but belonging to the
 * same user
 */
@Data
@Entity
public class Scene {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @ApiModelProperty(hidden = true)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    @GsonExclude
    private User user;

    @Column(name = "user_id", nullable = false)
    @GsonExclude
    private Long userId;

    @OneToMany(mappedBy = "scene", orphanRemoval = true)
    @GsonExclude
    private Set<State> states = new HashSet<>();

    /** The user given name of this room (e.g. 'Master bedroom') */
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Icon icon;

    /** Determines whether a guest can access this scene */
    @Column private boolean guestAccessEnabled;
}
