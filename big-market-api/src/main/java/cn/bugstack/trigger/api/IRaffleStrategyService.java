package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.RaffleAwardListRequestDTO;
import cn.bugstack.trigger.api.dto.resp.RaffleAwardListResponseDTO;
import cn.bugstack.trigger.api.dto.req.RaffleStrategyRequestDTO;
import cn.bugstack.trigger.api.dto.resp.RaffleStrategyResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;

import java.util.List;

public interface IRaffleStrategyService {

        /**
     * 策略装配接口
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * 查询抽奖奖品列表配置
     *
     * @param requestDTO 抽奖奖品列表查询请求参数
     * @return 奖品列表数据
     */
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);

    /**
     * 随机抽奖接口
     *
     * @param requestDTO 请求参数
     * @return 抽奖结果
     */
    Response<RaffleStrategyResponseDTO> randomRaffle(RaffleStrategyRequestDTO requestDTO);

}
