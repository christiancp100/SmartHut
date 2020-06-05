package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.EqualsAndHashCode;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Trigger<D extends Triggerable> {

    @Transient private String kind;

    protected Trigger(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

    public abstract boolean triggered();

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
     * The device this trigger belongs to, as a foreign key id. To use when updating and inserting
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public D getDevice() {
        return device;
    }

    public void setDevice(D device) {
        this.device = device;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Automation getAutomation() {
        return automation;
    }

    public void setAutomation(Automation automation) {
        this.automation = automation;
    }

    public Long getAutomationId() {
        return automationId;
    }

    public Trigger<D> setAutomationId(Long automationId) {
        this.automationId = automationId;
        return this;
    }

    @PreRemove
    public void removeDeviceAndScene() {
        this.setDevice(null);
        this.setDeviceId(null);

        this.setAutomation(null);
        this.setAutomationId(null);
    }
}
