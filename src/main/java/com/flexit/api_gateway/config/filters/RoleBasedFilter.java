package com.flexit.api_gateway.config.filters;

import com.flexit.api_gateway.util.JwtUtil;
import com.flexit.api_gateway.util.constants.StringLiteralConstants;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RoleBasedFilter implements GatewayFilterFactory<RoleBasedFilter.Config> {

    public static class Config {
        public String role;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    @Override
    public Config newConfig() {
        return new Config();
    }

    @Override
    public GatewayFilter apply(RoleBasedFilter.Config config) {
        return ((exchange, chain) -> {
            String userRole = exchange.getRequest().getHeaders().getFirst("role");
            if (!config.getRole().equalsIgnoreCase(userRole)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        });
    }
}