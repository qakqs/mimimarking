package cn.bugstack.domain.task.service.impl;

import cn.bugstack.domain.task.repository.ITaskRepository;
import cn.bugstack.types.common.TaskEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class OrderAuditTask implements Callable<String> {
    List<String> batch = new ArrayList<>();
    private ITaskRepository taskRepository;

    public OrderAuditTask(List<String> batch, ITaskRepository taskRepository) {
        this.batch = batch;
        this.taskRepository = taskRepository;
    }

    @Override
    public String call() {
        try {
            for (String s : batch) {
                List<TaskEntity> taskEntities = taskRepository.queryNoSendMessageTaskList();
                log.info(taskEntities.toString());
                System.out.println(s);
                Integer i = Integer.valueOf(s);
            }
            return "1";

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
