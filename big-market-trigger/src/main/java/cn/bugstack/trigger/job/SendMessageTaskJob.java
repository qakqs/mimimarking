package cn.bugstack.trigger.job;

import cn.bugstack.types.common.TaskEntity;
import cn.bugstack.domain.task.service.ITaskService;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class SendMessageTaskJob {
    private static final Log log = Log.get(SendMessageTaskJob.class);
    @Resource
    private ITaskService taskService;
    @Resource
    private ThreadPoolExecutor executor;

    @Scheduled(cron = "0/5 * * * * ?")
    public void execute() {
        try {
            List<TaskEntity> taskEntities = taskService.queryNoSendMessageTaskList();
            if (taskEntities.isEmpty()) return;

            for (TaskEntity taskEntity : taskEntities) {
                executor.execute(() -> {
                    try {
                        taskService.sendMessage(taskEntity);
                        taskService.updateTaskSendMessageCompleted(taskEntity.getUserId(), taskEntity.getMessageId());

                    } catch (Exception e) {
                        log.error("定时任务，发送MQ消息失败 userId: {} topic: {}", taskEntity.getUserId(), taskEntity.getTopic(), e);
                        taskService.updateTaskSendMessageFail(taskEntity.getUserId(), taskEntity.getMessageId());
                    }
                });
            }
        } catch (Exception e) {
            log.error("定时任务，扫描MQ任务表发送消息失败。", e);

        }
    }
}
