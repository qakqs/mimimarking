package cn.bugstack.trigger.api.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖奖品列表，请求对象
 * @create 2024-02-14 09:46
 */
@Data
public class RaffleAwardListRequestDTO implements Serializable {

    private static final long serialVersionUID = 6227145633719326140L;


    // 用户
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @Deprecated
    private Long strategyId;


}

