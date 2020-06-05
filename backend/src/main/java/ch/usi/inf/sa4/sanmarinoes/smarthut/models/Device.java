package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import ch.usi.inf.sa4.sanmarinoes.smarthut.config.SocketGsonExclude;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;

/** Generic abstraction for a smart home device */
@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Device {

    /** Ways a device can behave in the automation flow. For now only input/output */
    public enum FlowType {
        @SerializedName("INPUT")
        INPUT,

        @SerializedName("OUTPUT")
        OUTPUT
    }

    /** Device identifier */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @ApiModelProperty(hidden = true)
    private long id;

    @ManyToOne
    @JoinColumn(name = "room_id", updatable = false, insertable = false)
    @GsonExclude
    @SocketGsonExclude
    private Room room;

    @OneToMany(mappedBy = "device", orphanRemoval = true)
    @GsonExclude
    @SocketGsonExclude
    private Set<Trigger<? extends Device>> triggers;

    /**
     * The room this device belongs in, as a foreign key id. To use when updating and inserting from
     * a REST call.
     */
    @Column(name = "room_id", nullable = false)
    private Long roomId;

    /** The name of the device as assigned by the user (e.g. 'Master bedroom light') */
    @Column(nullable = false)
    private String name;

    /**
     * The name for the category of this particular device (e.g 'dimmer'). Not stored in the
     * database but set thanks to constructors
     */
    @Transient private final String kind;

    /**
     * The way this device behaves in the automation flow. Not stored in the database but set thanks
     * to constructors
     */
    @Transient private final FlowType flowType;

    @OneToMany(mappedBy = "device", orphanRemoval = true)
    @GsonExclude
    @SocketGsonExclude
    private Set<State> states;

    @Transient @GsonExclude private Long fromHostId = null;

    @Transient @GsonExclude private boolean fromGuest = false;

    @Transient @GsonExclude private boolean deleted = false;

    protected Device(String kind, FlowType flowType) {
        this.kind = kind;
        this.flowType = flowType;
    }
}
