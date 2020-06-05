package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class SecurityCamera extends Switchable implements BooleanTriggerable {

    public SecurityCamera() {
        super("securityCamera");
        this.on = false;
    }

    @Column(name = "camera_on", nullable = false)
    private boolean on;

    @Column(name = "video", nullable = false)
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean isOn() {
        return on;
    }

    @Override
    public void setOn(boolean on) {
        this.on = on;
    }

    @Override
    public boolean readTriggerState() {
        return on;
    }
}
