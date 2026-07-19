package cn.bugstack.domain.task.service.impl;

import cn.bugstack.domain.task.repository.ITaskRepository;
import cn.bugstack.types.common.TaskEntity;
import cn.bugstack.types.common.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class OrderAuditTask implements Callable<String> {
    private static final Log log = Log.get(OrderAuditTask.class);
    List<String> batch = new ArrayList<>();
    private ITaskRepository taskRepository;

    public OrderAuditTask(List<String> batch, ITaskRepository taskRepository) {
        this.batch = batch;
        this.taskRepository = taskRepository;
    }

    @Override
    public String call() {
        try {
            Thread.sleep(100);
            for (String s : batch) {
                List<TaskEntity> taskEntities = taskRepository.queryNoSendMessageTaskList();
                log.info(taskEntities.toString());
                System.out.println(s);
                Integer i = Integer.valueOf(s);
            }
            return "1";

        } catch (Exception e) {
            log.error("订单审核任务执行失败", e);
        }
        return null;
    }
}
