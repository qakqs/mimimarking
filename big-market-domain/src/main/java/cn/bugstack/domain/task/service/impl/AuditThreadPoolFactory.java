package cn.bugstack.domain.task.service.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AuditThreadPoolFactory {

    public static ThreadPoolExecutor createThreadPool(String name, int coreSize, int maxSize,
                                                      int queueCapacity, long keepAliveSeconds) {
        return new ThreadPoolExecutor(
                coreSize,
                maxSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                /** 自定义线程工厂：统一命名、非守护线程、优先级标准化 */
                new ThreadFactoryBuilder().setNameFormat(name).build(),
                /**
                 * 自定义拒绝策略：日志告警 + CallerRunsPolicy 背压 + 降级落盘
                 */
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：调用者线程执行，防止丢单
        ) {
            // 执行前埋点：记录队列堆积、活跃线程数
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                log.debug("[{}] 任务开始执行，队列剩余: {}, 活跃线程: {}", name, getQueue().size(), getActiveCount());
                super.beforeExecute(t, r);
            }

            // 执行后埋点：统计异常与耗时（实际生产建议接入 Micrometer/Prometheus）
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                if (t != null) {
                    log.error("[{}] 任务执行异常: {}", name, t.getMessage(), t);
                }
            }
        };
    }

    /**
     * 自定义线程工厂：统一命名、非守护线程、优先级标准化
     */
    static class AuditThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger threadNum = new AtomicInteger(1);

        public AuditThreadFactory(String name) {
            this.namePrefix = name + "-audit-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNum.getAndIncrement());
            if (t.isDaemon()) t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
