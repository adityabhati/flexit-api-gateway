package com.flexit.api_gateway.config.filters;

import com.flexit.api_gateway.util.constants.StringLiteralConstants;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class UserAccessFilter implements GatewayFilterFactory<UserAccessFilter.Config> {

    public static class Config {
    }

    @Override
    public Config newConfig() {
        return new Config();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String requester = exchange.getRequest().getHeaders().getFirst("username");
            String role = exchange.getRequest().getHeaders().getFirst("role");

            String requestPath = exchange.getRequest().getPath().value();
            String queryUsername = exchange.getRequest().getQueryParams().getFirst("username");

            boolean isAdmin = StringLiteralConstants.ROLE_ADMIN.equalsIgnoreCase(role);
            boolean isSameUser = requester != null && requester.equalsIgnoreCase(queryUsername);

            if (isAdmin || isSameUser) {
                return chain.filter(exchange);
            }

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        });
    }


}