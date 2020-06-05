package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
public class Automation {

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

    @OneToMany(mappedBy = "automation", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Trigger<?>> triggers = new HashSet<>();

    @OneToMany(mappedBy = "automation", cascade = CascadeType.REMOVE)
    private Set<ScenePriority> scenes = new HashSet<>();

    @OneToMany(mappedBy = "automation", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Condition<?>> conditions = new HashSet<>();

    @NotEmpty private String name;
}
