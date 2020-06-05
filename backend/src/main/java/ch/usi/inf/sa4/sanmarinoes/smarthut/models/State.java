package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents instructions on how to change the state of a particular device. Many states (plus
 * other properties) form a Scene
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"device_id", "scene_id"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class State {

    @ManyToOne(targetEntity = OutputDevice.class)
    @JoinColumn(name = "device_id", updatable = false, insertable = false)
    @GsonExclude
    @Getter
    private OutputDevice device;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @ApiModelProperty(hidden = true)
    @Getter
    private long id;

    /**
     * The device this state belongs in, as a foreign key id. To use when updating and inserting
     * from a REST call.
     */
    @Column(name = "device_id", nullable = false)
    @Getter
    @Setter
    private Long deviceId;

    @ManyToOne
    @JoinColumn(name = "scene_id", updatable = false, insertable = false)
    @GsonExclude
    @Getter
    @Setter
    private Scene scene;

    @Column(name = "scene_id", nullable = false)
    @Getter
    @Setter
    private Long sceneId;

    protected void setInnerDevice(OutputDevice device) {
        this.device = device;
    }

    /** Sets the state of the connected device to the state represented by this object. */
    public abstract void apply();

    /** Creates a perfect copy of this state, except for the id field and the sceneId/scene */
    protected abstract State copy();

    public State copyToSceneId(Long sceneId) {
        final State s = copy();
        s.setDeviceId(this.deviceId);
        s.device = this.device;
        s.setSceneId(sceneId);
        s.setScene(this.scene);
        return s;
    }

    @PreRemove
    public void removeDeviceAndScene() {
        this.setSceneId(null);
        this.setScene(null);
        this.setDeviceId(null);
        this.device = null;
    }
}
