package com.flexit.api_gateway.config.filters;

import org.springframework.stereotype.Component;

@Component
public class BlacklistTokenFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Log incoming request
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        String remoteAddr = httpRequest.getRemoteAddr();

        logger.info("Incoming request: {} {} from {}", method, uri, remoteAddr);

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = httpResponse.getStatus();

            logger.info("Response: {} {} - Status: {} - Duration: {}ms",
                    method, uri, status, duration);
        }
    }
}