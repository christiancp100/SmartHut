package ch.usi.inf.sa4.sanmarinoes.smarthut.config;

import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "camera")
public class CameraConfigurationService {

    @NotNull private String videoUrl;

    public synchronized String getVideoUrl() {
        return videoUrl;
    }

    public synchronized void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
