package com.flexit.api_gateway.config;

import com.flexit.api_gateway.config.filters.JwtValidationFilter;
import com.flexit.api_gateway.config.filters.RoleBasedFilter;
import com.flexit.api_gateway.config.filters.UserAccessFilter;
import com.flexit.api_gateway.util.constants.StringLiteralConstants;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudGatewayRouting {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, JwtValidationFilter jwtValidationFilter, RoleBasedFilter roleBasedFilter, UserAccessFilter userAccessFilter) {
        RoleBasedFilter.Config adminRoleConfig = new RoleBasedFilter.Config();
        adminRoleConfig.setRole(StringLiteralConstants.ROLE_ADMIN);
        RoleBasedFilter.Config userRoleConfig = new RoleBasedFilter.Config();
        userRoleConfig.setRole(StringLiteralConstants.ROLE_USER);
        return builder.routes()
                .route("flexit-auth-backend", r -> r.path("/flexit/user/user-info")
                        .filters(f -> f.filter(jwtValidationFilter.apply(jwtValidationFilter.newConfig()))
                                .filter(userAccessFilter.apply(userAccessFilter.newConfig()))
                                .circuitBreaker(c -> c.setName("flexit-auth")
                                        .setFallbackUri("/fallback/flexit-auth")))
                        .uri("http://localhost:8091"))
                .route("inventory-management-backend", r -> r.path("/flexit/inventory/admin")
                        .filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationFilter.Config()))
                                .filter(roleBasedFilter.apply(adminRoleConfig))
                                .circuitBreaker(c -> c.setName("flexit-inventory")
                                        .setFallbackUri("forward:/fallback/flexit-inventory-management")))
                        .uri("http://localhost:8092"))
                .route("inventory-management-backend", r -> r.path("/flexit/inventory/user")
                        .filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationFilter.Config()))
                                .filter(roleBasedFilter.apply(userRoleConfig)).circuitBreaker(c -> c.setName("flexit-inventory")
                                        .setFallbackUri("forward:/fallback/flexit-inventory-management")))
                        .uri("http://localhost:8092"))
                .build();
    }
}