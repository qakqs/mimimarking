package cn.bugstack.trigger.config;

import cn.bugstack.types.common.TraceContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Request-level trace-id filter.
 *
 * <p>Activates the {@code %X{trace-id}} and {@code %X{ServiceId}} placeholders already
 * declared in {@code logback-spring.xml} but never populated. Each inbound HTTP request
 * gets a trace-id (inherited from the {@code x-trace-id} header when the caller supplies
 * one, otherwise generated), bound to the MDC for the whole request scope, and cleared on
 * exit so every log line produced across register/login/draw can be correlated.</p>
 *
 * <p>The trace-id is echoed back via the {@code x-trace-id} response header so clients
 * (and the stress-test harness) can map a request to its logs.</p>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_HEADER = TraceContext.TRACE_ID_HEADER;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = resolveTraceId(request);
        try {
            TraceContext.begin(traceId);
            response.setHeader(TRACE_ID_HEADER, traceId);
            filterChain.doFilter(request, response);
        } finally {
            TraceContext.end();
        }
    }

    private String resolveTraceId(HttpServletRequest request) {
        String header = request.getHeader(TRACE_ID_HEADER);
        if (StringUtils.hasText(header)) {
            return header.trim();
        }
        return UUID.randomUUID().toString().replace("-", "");
    }
}
