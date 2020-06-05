package ch.usi.inf.sa4.sanmarinoes.smarthut.config;

import static org.assertj.core.api.Assertions.assertThat;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.AutomationFastUpdateRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.BooleanTriggerDTO;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.TriggerDTO;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.BooleanTrigger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GsonTests {

    private final Gson gson = GsonConfig.gson();

    @Test
    public void testGson() {
        BooleanTrigger b = new BooleanTrigger();
        b.setId(1L);
        String json = gson.toJson(b);
        JsonObject o = gson.fromJson(json, JsonObject.class);
        assertThat(o.get("kind")).isNotNull();
        assertThat(o.get("kind").getAsString()).isEqualTo("booleanTrigger");

        AutomationFastUpdateRequest a = new AutomationFastUpdateRequest();
        BooleanTriggerDTO bt = new BooleanTriggerDTO();
        bt.setDeviceId(42L);
        a.setTriggers(List.of(bt));

        AutomationFastUpdateRequest a2 =
                gson.fromJson(gson.toJson(a), AutomationFastUpdateRequest.class);
        TriggerDTO t = a2.getTriggers().get(0);

        assertThat(t).isExactlyInstanceOf(BooleanTriggerDTO.class);
        assertThat(t.getDeviceId()).isEqualTo(42L);
    }
}
