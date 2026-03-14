package cn.bugstack.trigger.api.dto.req;

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


    // 抽奖策略ID
    private Long strategyId;

}

