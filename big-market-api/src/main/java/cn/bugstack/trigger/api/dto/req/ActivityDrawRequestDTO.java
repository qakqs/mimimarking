package cn.bugstack.trigger.api.dto.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class ActivityDrawRequestDTO implements Serializable {
    private static final long serialVersionUID = 4217901585257070303L;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 活动id
     */
    private Long activityId;
}
