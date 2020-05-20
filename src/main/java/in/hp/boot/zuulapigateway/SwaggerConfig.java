package in.hp.boot.zuulapigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Added @Primary to override default SwaggerResourceProvider
 */
@Configuration
@Primary
@Slf4j
public class SwaggerConfig implements SwaggerResourcesProvider {

    @Autowired
    private RouteLocator routeLocator;

    @Override
    public List<SwaggerResource> get() {
        // Fetching all routes from RouteLocator and adding the Swagger url
        return routeLocator.getRoutes().stream().map(route -> {
            SwaggerResource resource = new SwaggerResource();
            resource.setName(route.getId());
            resource.setLocation(route.getFullPath().replace("**", "v2/api-docs"));
            resource.setSwaggerVersion("2.0");
            return resource;
        }).peek(x -> log.info(String.valueOf(x)))
                .collect(Collectors.toList());
    }
}
