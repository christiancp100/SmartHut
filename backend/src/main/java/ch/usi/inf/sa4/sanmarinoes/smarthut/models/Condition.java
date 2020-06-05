package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Condition<D> {

    @Transient private String kind;

    protected Condition(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @ApiModelProperty(hidden = true)
    private long id;

    @ManyToOne(targetEntity = Device.class)
    @JoinColumn(name = "device_id", updatable = false, insertable = false)
    @GsonExclude
    private D device;

    /**
     * The device this condition belongs to, as a foreign key id. To use when updating and inserting
     * from a REST call.
     */
    @Column(name = "device_id", nullable = false)
    private Long deviceId;

    @ManyToOne
    @JoinColumn(name = "automation_id", updatable = false, insertable = false)
    @GsonExclude
    @EqualsAndHashCode.Exclude
    private Automation automation;

    @Column(name = "automation_id", nullable = false)
    private Long automationId;

    public abstract boolean triggered();

    public Condition<D> setAutomationId(Long automationId) {
        this.automationId = automationId;
        return this;
    }
}
