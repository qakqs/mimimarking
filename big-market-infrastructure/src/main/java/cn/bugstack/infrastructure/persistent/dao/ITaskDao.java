package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITaskDao {
    void insert(Task task);

    void updateTaskSendMessageCompleted(Task task);

    void updateTaskSendMessageFail(Task task);

    List<Task> queryNoSendMessageTaskList();
}
