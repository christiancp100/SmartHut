package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
public class ScenePriority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @ApiModelProperty(hidden = true)
    private long id;

    @ManyToOne
    @JoinColumn(name = "automation_id", updatable = false, insertable = false)
    @GsonExclude
    private Automation automation;

    @Column(name = "automation_id", nullable = false)
    private Long automationId;

    @Min(0)
    @Column(nullable = false)
    private Integer priority;

    @ManyToOne
    @JoinColumn(name = "scene_id", updatable = false, insertable = false)
    @GsonExclude
    private Scene scene;

    @Column(name = "scene_id", nullable = false, updatable = false)
    private Long sceneId;

    public long getId() {
        return id;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getAutomationId() {
        return automationId;
    }

    public void setAutomationId(Long automationId) {
        this.automationId = automationId;
    }

    public void setAutomation(Automation automation) {
        this.automation = automation;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    @PreRemove
    public void preRemove() {
        this.automation = null;
        this.automationId = null;

        this.scene = null;
        this.sceneId = null;
    }

    public Automation getAutomation() {
        return automation;
    }
}
