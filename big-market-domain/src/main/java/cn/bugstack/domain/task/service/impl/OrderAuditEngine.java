package cn.bugstack.domain.task.service.impl;

import cn.bugstack.domain.task.repository.ITaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderAuditEngine {

    // 生产环境建议通过 @ConfigurationProperties 注入
    private ThreadPoolExecutor auditPool;
    private final int batchSize = 500; // 可根据压测动态调整

    @Resource
    ITaskRepository taskRepository;
    @PostConstruct
    public void init() {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        // IO密集型建议: N_CPU * 2 ~ N_CPU * 4；此处以 3 倍为例
        auditPool = AuditThreadPoolFactory.createThreadPool(
                "OrderAuditPool",
                cpuCores * 2,   // core
                cpuCores * 4,   // max
                2,           // queue capacity (有界防OOM)
                60L             // keep-alive
        );
        log.info("订单审核线程池初始化完成 | Core: {}, Max: {}, Queue: {}",
                cpuCores * 2, cpuCores * 4, 2000);
    }

    /** 主入口：提交海量订单审核 */
    public List<String> auditOrders(List<List<String>> batches) {

        // 1. 分组拆分

        // 2. 异步提交
        List<CompletableFuture<String>> futures = batches.stream()
                .map(batch -> CompletableFuture.supplyAsync(
                        () -> new OrderAuditTask(batch, taskRepository).call(),
                        auditPool))
                .toList();

        // 3. 等待全部完成（生产建议加超时：.orTimeout(5, TimeUnit.MINUTES)）
        CompletableFuture<Void> allDone = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])).orTimeout(2, TimeUnit.SECONDS);
            allDone.join();

        // 4. 聚合结果
        return AuditSummary.aggregate(
                futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    @PreDestroy
    public void shutdown() {
        log.info("开始优雅关闭审核线程池...");
        auditPool.shutdown();
        try {
            if (!auditPool.awaitTermination(60, TimeUnit.SECONDS)) {
                auditPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            auditPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("审核线程池已关闭");
    }
}
