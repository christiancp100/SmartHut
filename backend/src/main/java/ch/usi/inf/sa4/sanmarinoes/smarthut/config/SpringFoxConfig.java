package ch.usi.inf.sa4.sanmarinoes.smarthut.config;

import java.util.List;
import java.util.function.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class configures the automated REST documentation tool Swagger for this project. The
 * documentation can be seen by going to http://localhost:8080/swaggeer-ui.html
 */
@Configuration
@EnableSwagger2
@ComponentScan("ch.usi.inf.sa4.sanmarinoes.smarthut")
public class SpringFoxConfig {

    /**
     * Main definition of Springfox / swagger configuration
     *
     * @return a Docket object containing the swagger configuration
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(paths()::test)
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(securitySchemes())
                .securityContexts(List.of(securityContext()));
    }

    /**
     * Configures the documentation about the smarthut authentication system
     *
     * @return a list of springfox authentication configurations
     */
    private static List<? extends SecurityScheme> securitySchemes() {
        return List.of(new ApiKey("Bearer", "Authorization", "header"));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(authenticatedPaths()::test)
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope authorizationScope =
                new AuthorizationScope("global", "accessEverything");
        return List.of(
                new SecurityReference("Bearer", new AuthorizationScope[] {authorizationScope}));
    }

    private Predicate<String> authenticatedPaths() {
        return ((Predicate<String>) PathSelectors.regex("/auth/update")::apply)
                .or(PathSelectors.regex("/room.*")::apply)
                .or(PathSelectors.regex("/device.*")::apply)
                .or(PathSelectors.regex("/buttonDimmer.*")::apply)
                .or(PathSelectors.regex("/thermostat.*")::apply)
                .or(PathSelectors.regex("/dimmableLight.*")::apply)
                .or(PathSelectors.regex("/knobDimmer.*")::apply)
                .or(PathSelectors.regex("/regularLight.*")::apply)
                .or(PathSelectors.regex("/securityCamera.*")::apply)
                .or(PathSelectors.regex("/sensor.*")::apply)
                .or(PathSelectors.regex("/smartPlug.*")::apply)
                .or(PathSelectors.regex("/scene.*")::apply)
                .or(PathSelectors.regex("/switchableState.*")::apply)
                .or(PathSelectors.regex("/dimmableState.*")::apply)
                .or(PathSelectors.regex("/switch.*")::apply)
                .or(PathSelectors.regex("/motionSensor.*")::apply)
                .or(PathSelectors.regex("/curtains.*")::apply)
                .or(PathSelectors.regex("/user.*")::apply)
                .or(PathSelectors.regex("/automation.*")::apply)
                .or(PathSelectors.regex("/auth/profile.*")::apply);
    }

    /**
     * Configures the paths the documentation must be generated for. Add a path here only when the
     * spec has been totally defined.
     *
     * @return A predicate that tests whether a path must be included or not
     */
    private Predicate<String> paths() {
        return PathSelectors.any()::apply;
    }

    /**
     * Returns the metadata about the smarthut project
     *
     * @return metadata about smarthut
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SmartHut.sm API")
                .description("Backend API for the SanMariones version of the SA4 SmartHut project")
                .termsOfServiceUrl("https://www.youtube.com/watch?v=9KxTcDsy9Gs")
                .license("WTFPL")
                .version("dev branch")
                .build();
    }
}
