package ch.usi.inf.sa4.sanmarinoes.smarthut.config;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import com.google.gson.*;
import java.lang.reflect.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import springfox.documentation.spring.web.json.Json;

/**
 * Spring configuration in order to register the GSON type adapter needed to avoid serializing twice
 * Springfox Swagger JSON output (see: https://stackoverflow.com/a/30220562)
 */
@Configuration
public class GsonConfig {
    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson());
        return converter;
    }

    private static GsonBuilder configureBuilder() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter());
        @SuppressWarnings({"rawTypes"})
        RuntimeTypeAdapterFactory<State> runtimeTypeAdapterFactory =
                RuntimeTypeAdapterFactory.of(State.class, "kind")
                        .registerSubtype(SwitchableState.class, "switchableState")
                        .registerSubtype(DimmableState.class, "dimmableState");
        RuntimeTypeAdapterFactory<TriggerDTO> runtimeTypeAdapterFactoryII =
                RuntimeTypeAdapterFactory.of(TriggerDTO.class, "kind")
                        .registerSubtype(BooleanTriggerDTO.class, "booleanTrigger")
                        .registerSubtype(RangeTriggerDTO.class, "rangeTrigger");

        RuntimeTypeAdapterFactory<ConditionDTO> runtimeTypeAdapterFactoryIII =
                RuntimeTypeAdapterFactory.of(ConditionDTO.class, "kind")
                        .registerSubtype(BooleanConditionDTO.class, "booleanCondition")
                        .registerSubtype(RangeConditionDTO.class, "rangeCondition")
                        .registerSubtype(ThermostatConditionDTO.class, "thermostatCondition");

        builder.registerTypeAdapterFactory(runtimeTypeAdapterFactory);
        builder.registerTypeAdapterFactory(runtimeTypeAdapterFactoryII);
        builder.registerTypeAdapterFactory(runtimeTypeAdapterFactoryIII);
        builder.registerTypeAdapter(
                Trigger.class,
                (JsonSerializer<Trigger<?>>)
                        (src, typeOfSrc, context) -> context.serialize((Object) src));
        builder.registerTypeAdapter(
                Condition.class,
                (JsonSerializer<Condition<?>>)
                        (src, typeOfSrc, context) -> context.serialize((Object) src));
        return builder;
    }

    public static Gson gson() {
        final GsonBuilder builder = configureBuilder();
        builder.addSerializationExclusionStrategy(new AnnotationExclusionStrategy());
        return builder.create();
    }

    public static Gson socketGson() {
        final GsonBuilder builder = configureBuilder();
        builder.addSerializationExclusionStrategy(new SocketAnnotationExclusionStrategy());
        return builder.create();
    }
}

/** GSON type adapter needed to avoid serializing twice Springfox Swagger JSON output */
class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {
    @Override
    public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
        return JsonParser.parseString(json.value());
    }
}

/** GSON exclusion strategy to exclude attributes with @GsonExclude */
class AnnotationExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(GsonExclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}

/** GSON exclusion strategy to exclude attributes with @SocketGsonExclude */
class SocketAnnotationExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(SocketGsonExclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
