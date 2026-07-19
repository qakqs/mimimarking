package cn.bugstack.trigger.config;

import org.springframework.amqp.rabbit.config.ContainerCustomizer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers a trace-id advice on every RabbitMQ listener container so each
 * {@code @RabbitListener} picks the inbound trace-id from the AMQP header into the MDC
 * automatically — no per-listener boilerplate.
 *
 * <p>Spring Boot's autoconfiguration injects this {@link ContainerCustomizer} into the
 * default listener container factory; the advice then wraps
 * {@code MessagingMessageListenerAdapter#onMessage(Message, Channel)} so the header
 * written by {@code EventPublisher} is read back here and bound to the MDC for the whole
 * listener invocation.</p>
 */
@Configuration
public class RabbitMdcConfig {

    @Bean
    public ContainerCustomizer<SimpleMessageListenerContainer> traceIdContainerCustomizer() {
        return container -> container.setAdviceChain(new TraceIdRabbitAdvice());
    }
}
