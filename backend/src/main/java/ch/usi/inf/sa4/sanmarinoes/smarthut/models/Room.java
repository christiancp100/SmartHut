package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/** Represents a room in the house owned by the user */
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @ApiModelProperty(hidden = true)
    private Long id;

    /** The room icon, out of a set of Semantic UI icons */
    @Column private Icon icon;

    /**
     * Image is to be given as byte[]. In order to get an encoded string from it, the
     * Base64.getEncoder().encodeToString(byte[] content) should be used. For further information:
     * https://www.baeldung.com/java-base64-image-string
     * https://docs.oracle.com/javase/8/docs/api/java/util/Base64.html
     */
    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    @GsonExclude
    private User user;

    @OneToMany(mappedBy = "room", orphanRemoval = true)
    @GsonExclude
    private Set<Device> devices = new HashSet<>();

    /**
     * User that owns the house this room is in as a foreign key id. To use when updating and
     * inserting from a REST call.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** The user given name of this room (e.g. 'Master bedroom') */
    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", name='" + name + "\'}";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
