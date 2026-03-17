package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserActivityAccountRequestDTO implements Serializable {
    private static final long serialVersionUID = -3192198875572990423L;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 活动id
     */
    private Long activityId;
}
