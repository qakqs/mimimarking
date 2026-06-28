package cn.bugstack.types.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * 统一日志门面，封装 SLF4J 并提供 MDC 上下文管理。
 *
 * <h3>日志格式规范</h3>
 * <pre>{@code
 * // 标准格式：[操作描述] key1:value1, key2:value2
 * log.info("创建订单 userId:{}", userId);
 * log.info("创建订单 完成 userId:{}, orderId:{}", userId, orderId);
 * log.error("创建订单 失败 userId:{}, orderId:{}", userId, orderId, e);
 * }</pre>
 *
 * <h3>格式规则</h3>
 * <ul>
 *   <li>操作描述在前，用中文</li>
 *   <li>键值对紧贴冒号，不空格：{@code key:value}</li>
 *   <li>多个键值对用英文逗号+空格分隔：{@code key1:value1, key2:value2}</li>
 *   <li>生命周期标记（开始/完成/失败）放在操作描述后</li>
 *   <li>异常日志必须携带关键业务参数</li>
 * </ul>
 *
 * <h3>用法</h3>
 * <pre>{@code
 * public class MyService {
 *     private static final Log log = Log.get(MyService.class);
 *
 *     public void doSomething(String userId) {
 *         log.info("创建订单 开始 userId:{}", userId);
 *         // ...
 *         log.info("创建订单 完成 userId:{}, orderId:{}", userId, orderId);
 *     }
 * }
 * }</pre>
 */
public final class Log {

    private final Logger logger;

    private Log(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public static Log get(Class<?> clazz) {
        return new Log(clazz);
    }

    // ---- info ----

    public void info(String msg) {
        if (logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }

    public void info(String format, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(format, args);
        }
    }

    /**
     * 快捷方法：info("操作 xxx", "key", value)
     * <p>输出：操作 xxx key:value
     */
    public void info(String action, String k1, Object v1) {
        if (logger.isInfoEnabled()) {
            logger.info("{} {}:{}", action, k1, v1);
        }
    }

    /**
     * 快捷方法：info("操作 xxx", "k1", v1, "k2", v2)
     * <p>输出：操作 xxx k1:v1, k2:v2
     */
    public void info(String action, String k1, Object v1, String k2, Object v2) {
        if (logger.isInfoEnabled()) {
            logger.info("{} {}:{}, {}:{}", action, k1, v1, k2, v2);
        }
    }

    // ---- warn ----

    public void warn(String msg) {
        if (logger.isWarnEnabled()) {
            logger.warn(msg);
        }
    }

    public void warn(String format, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, args);
        }
    }

    public void warn(String action, String k1, Object v1) {
        if (logger.isWarnEnabled()) {
            logger.warn("{} {}:{}", action, k1, v1);
        }
    }

    // ---- error ----

    public void error(String msg) {
        if (logger.isErrorEnabled()) {
            logger.error(msg);
        }
    }

    /**
     * 参数化错误日志。最后一个参数如果是 Throwable 会被 SLF4J 识别为异常堆栈。
     * <pre>{@code
     * log.error("创建订单 失败 userId:{}", userId, e);
     * log.error("创建订单 失败 userId:{}, orderId:{}", userId, orderId, e);
     * }</pre>
     */
    public void error(String format, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(format, args);
        }
    }

    public void error(String action, String k1, Object v1, Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error("{} {}:{}, {}", action, k1, v1, t.getMessage(), t);
        }
    }

    public void error(String action, String k1, Object v1, String k2, Object v2, Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error("{} {}:{}, {}:{}, {}", action, k1, v1, k2, v2, t.getMessage(), t);
        }
    }

    // ---- debug ----

    public void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    public void debug(String format, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, args);
        }
    }

    // ---- MDC ----

    public static void put(String key, String value) {
        MDC.put(key, value);
    }

    public static void remove(String key) {
        MDC.remove(key);
    }

    public static void clear() {
        MDC.clear();
    }
}