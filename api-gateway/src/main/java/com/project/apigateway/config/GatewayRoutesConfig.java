package com.project.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.stripPrefix;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        return route("product-service")
            .route(path("/api/product/**"), http())
            .filter(lb("product-service"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return route("order-service")
            .route(path("/api/order/**"), http())
            .filter(lb("order-service"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> discoveryServiceRoute() {
        return route("discovery-service")
            .route(path("/eureka/web"), http())
            .filter(uri("http://localhost:8761"))
            .filter(setPath("/"))
            .build();
    }

    // The discovery dashboard's relative asset hrefs (no leading slash) resolve, per the
    // browser, against "/eureka/web" -> "/eureka/eureka/...". Strip the duplicated segment
    // before the general static catch-all below (this bean must be registered first).
    @Bean
    public RouterFunction<ServerResponse> discoveryDashboardAssetsRoute() {
        return route("discovery-service-dashboard-assets")
            .route(path("/eureka/eureka/**"), http())
            .filter(uri("http://localhost:8761"))
            .filter(stripPrefix(1))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> discoveryServiceStaticRoute() {
        return route("discovery-service-static")
            .route(path("/eureka/**"), http())
            .filter(uri("http://localhost:8761"))
            .build();
    }
}
