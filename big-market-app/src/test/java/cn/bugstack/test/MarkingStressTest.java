package cn.bugstack.test;

import com.alibaba.fastjson.JSON;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 大营销平台压测工具
 *
 * <p>使用方式：
 * <pre>
 *   mvn -pl big-market-app -DskipTests test-compile exec:java \
 *       -Dexec.mainClass=cn.bugstack.test.MarkingStressTest -Dexec.classpathScope=test
 *   # 可配置 JVM 参数: -Dstress.threads=50 -Dstress.iterations=20 -Dstress.activityId=100301
 * </pre>
 */
public class MarkingStressTest {

    // ---- 可配置参数 ----
    private static final int THREAD_COUNT = Integer.getInteger("stress.threads", 1000);
    private static final int ITERATIONS = Integer.getInteger("stress.iterations", 2000);
    private static final long ACTIVITY_ID = Long.getLong("stress.activityId", 100301L);
    private static final String BASE_URL = System.getProperty("stress.baseUrl", "http://localhost:8091");
    private static final int WARMUP_COUNT = Integer.getInteger("stress.warmup", 5800);

    // ---- HTTP 客户端 ----
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    // ---- 指标采集 ----
    private static final AtomicLong successCount = new AtomicLong(0);
    private static final AtomicLong failCount = new AtomicLong(0);
    private static final ConcurrentLinkedQueue<Long> latencies = new ConcurrentLinkedQueue<>();

    // ---- 线程池 ----
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            THREAD_COUNT, THREAD_COUNT,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws Exception {
        printBanner();

        // 阶段一：预热
        System.out.println("[压测] 阶段一：预热 (threads=" + WARMUP_COUNT + ", iterations=1)");
        runPhase(WARMUP_COUNT, 1, ACTIVITY_ID, "预热");
        resetMetrics();

        // 阶段二：正式压测
        System.out.printf("[压测] 阶段二：正式压测 (threads=%d, iterations=%d, activityId=%d)%n",
                THREAD_COUNT, ITERATIONS, ACTIVITY_ID);
        long start = System.currentTimeMillis();
        runPhase(THREAD_COUNT, ITERATIONS, ACTIVITY_ID, "正式");
        long duration = System.currentTimeMillis() - start;

        printReport(duration);
        executor.shutdown();
    }

    // ---- 阶段执行 ----
    private static void runPhase(int threads, int iterations, long activityId, String label)
            throws Exception {
        CountDownLatch latch = new CountDownLatch(threads);
        List<Future<?>> futures = new ArrayList<>();

        for (int t = 0; t < threads; t++) {
            final int threadIdx = t;
            Future<?> f = executor.submit(() -> {
                try {
                    for (int i = 0; i < iterations; i++) {
                        String userId = "realrun_" + threadIdx + "_" + i;
                        boolean ok = executeDrawFlow(userId, activityId);
                        if (ok) {
                            successCount.incrementAndGet();
                        } else {
                            failCount.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    failCount.addAndGet(iterations);
                    System.err.println("[ERROR] 线程-" + threadIdx + " 异常: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
            futures.add(f);
        }

        latch.await();
    }

    // ---- 核心压测流程：用户注册 → 登录 → 执行抽奖 ----
    private static boolean executeDrawFlow(String userId, long activityId) {
        try {
            // 1. 注册
            long t0 = System.nanoTime();
            int status = doPost("/api/user/register", Map.of("username", userId, "password", "123456"));
            if (status != 200) return false;

            // 2. 登录
            int loginStatus = doPost("/api/user/login", Map.of("username", userId, "password", "123456"));
            if (loginStatus != 200) return false;

            // 3. 执行抽奖
            int drawStatus = doPost("/api/raffle/activity/draw",
                    Map.of("userId", userId, "activityId", activityId));
            long latency = (System.nanoTime() - t0) / 1_000_000;
            latencies.add(latency);
            return drawStatus == 200;

        } catch (Exception e) {
            return false;
        }
    }

    // ---- HTTP 请求工具 ----
    private static int doPost(String path, Object body) throws Exception {
        String json = JSON.toJSONString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(30))
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }

    // ---- 指标重置 ----
    private static void resetMetrics() {
        successCount.set(0);
        failCount.set(0);
        latencies.clear();
    }

    // ---- 报告输出 ----
    private static void printReport(long durationMs) {
        long total = successCount.get() + failCount.get();
        List<Long> sorted = new ArrayList<>(latencies);
        Collections.sort(sorted);

        double tps = durationMs > 0 ? (total * 1000.0 / durationMs) : 0;
        double avg = sorted.isEmpty() ? 0 : sorted.stream().mapToLong(Long::longValue).average().orElse(0);
        long min = sorted.isEmpty() ? 0 : sorted.get(0);
        long max = sorted.isEmpty() ? 0 : sorted.get(sorted.size() - 1);
        long p50 = percentile(sorted, 50);
        long p95 = percentile(sorted, 95);
        long p99 = percentile(sorted, 99);

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println();
        System.out.println("================ 压测报告 ================");
        System.out.printf("时间         : %s%n", now);
        System.out.printf("目标地址     : %s%n", BASE_URL);
        System.out.printf("并发线程数   : %d%n", THREAD_COUNT);
        System.out.printf("每线程迭代   : %d%n", ITERATIONS);
        System.out.printf("活动ID       : %d%n", ACTIVITY_ID);
        System.out.println("------------------------------------------");
        System.out.printf("总请求数     : %d%n", total);
        System.out.printf("成功数       : %d%n", successCount.get());
        System.out.printf("失败数       : %d%n", failCount.get());
        System.out.printf("成功率       : %.2f%%%n",
                total > 0 ? (successCount.get() * 100.0 / total) : 0);
        System.out.printf("总耗时(ms)   : %d%n", durationMs);
        System.out.printf("TPS          : %.2f%n", tps);
        System.out.println("------------------------------------------");
        System.out.printf("平均延迟(ms) : %.1f%n", avg);
        System.out.printf("最小延迟(ms) : %d%n", min);
        System.out.printf("最大延迟(ms) : %d%n", max);
        System.out.printf("P50 (ms)     : %d%n", p50);
        System.out.printf("P95 (ms)     : %d%n", p95);
        System.out.printf("P99 (ms)     : %d%n", p99);
        System.out.println("==========================================");
    }

    private static long percentile(List<Long> sorted, int percentile) {
        if (sorted.isEmpty()) return 0;
        int index = (int) Math.ceil(sorted.size() * percentile / 100.0) - 1;
        return sorted.get(Math.max(0, Math.min(index, sorted.size() - 1)));
    }

    private static void printBanner() {
        System.out.println();
        System.out.println("===== 大营销平台 - 压力测试工具 v1.0 =====");
        System.out.println();
    }
}
