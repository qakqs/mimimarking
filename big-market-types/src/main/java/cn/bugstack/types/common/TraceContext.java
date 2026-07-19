package cn.bugstack.types.common;

import org.slf4j.MDC;

/**
 * Trace-id context holder shared by HTTP and MQ entry points.
 *
 * <p>Centralizes the MDC keys (matching the {@code %X{trace-id}} / {@code %X{ServiceId}}
 * placeholders in {@code logback-spring.xml}) and the AMQP header name so the HTTP filter,
 * the event publisher and the rabbit listeners all agree on the same wire/MDC contract.</p>
 */
public final class TraceContext {

    /** AMQP message header carrying the trace-id across the MQ boundary. */
    public static final String TRACE_ID_HEADER = "x-trace-id";

    /** MDC key rendered by logback's {@code %X{trace-id}}. */
    public static final String MDC_TRACE_ID = "trace-id";

    /** MDC key rendered by logback's {@code %X{ServiceId}}. */
    public static final String MDC_SERVICE_ID = "ServiceId";

    private static final String SERVICE_ID = "big-market";

    private TraceContext() {
    }

    /** Current trace-id, or {@code null} when not in a traced scope. */
    public static String currentTraceId() {
        return MDC.get(MDC_TRACE_ID);
    }

    /** Bind a trace-id (and the static service-id) to the current thread's MDC. */
    public static void begin(String traceId) {
        if (traceId != null && !traceId.isBlank()) {
            MDC.put(MDC_TRACE_ID, traceId);
        }
        MDC.put(MDC_SERVICE_ID, SERVICE_ID);
    }

    /** Remove the keys owned by this scope, safe to call when nothing was bound. */
    public static void end() {
        MDC.remove(MDC_TRACE_ID);
        MDC.remove(MDC_SERVICE_ID);
    }
}
