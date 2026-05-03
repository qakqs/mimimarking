package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ITaskDao {

    void insert(Task task);

    void updateTaskSendMessageCompleted(Task task);

    void updateTaskSendMessageFail(Task task);

    List<Task> queryNoSendMessageTaskList();

    List<Task> queryPage(@Param("offset") int offset, @Param("limit") int limit,
                         @Param("userId") String userId, @Param("state") String state,
                         @Param("topic") String topic);

    int count(@Param("userId") String userId, @Param("state") String state,
              @Param("topic") String topic);

}
