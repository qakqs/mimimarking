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
public class RaffleStrategyRequestDTO implements Serializable {
    private static final long serialVersionUID = -8673599924401711390L;

    // 抽奖策略ID
    private Long strategyId;

}


