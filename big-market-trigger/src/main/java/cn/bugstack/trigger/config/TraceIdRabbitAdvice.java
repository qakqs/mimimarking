package cn.bugstack.trigger.config;

import cn.bugstack.types.common.TraceContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;

/**
 * Stamps the inbound AMQP message's trace-id into the MDC for the whole listener
 * invocation, so consumer-side logs correlate with the producing request without any
 * per-listener boilerplate.
 *
 * <p>Registered on the listener container factory (see {@code RabbitMdcConfig}); applied
 * to every {@code @RabbitListener} via the container's advice chain, which wraps the
 * {@code MessagingMessageListenerAdapter#onMessage(Message, Channel)} call. The first
 * argument is the raw {@link Message}, so the header written by {@code EventPublisher}
 * is read back here.</p>
 */
public class TraceIdRabbitAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String traceId = extractTraceId(invocation.getArguments());
        try {
            TraceContext.begin(traceId);
            return invocation.proceed();
        } finally {
            TraceContext.end();
        }
    }

    private String extractTraceId(Object[] arguments) {
        if (arguments == null || arguments.length == 0) {
            return null;
        }
        for (Object arg : arguments) {
            if (arg instanceof Message message) {
                Object header = message.getMessageProperties().getHeader(TraceContext.TRACE_ID_HEADER);
                return header == null ? null : String.valueOf(header);
            }
        }
        return null;
    }
}
