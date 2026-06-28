package cn.bugstack.trigger.http;

import cn.bugstack.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.bugstack.domain.strategy.model.entity.RaffleAwardEntity;
import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.strategy.model.valobj.RuleWeightVO;
import cn.bugstack.domain.strategy.service.IRaffleAward;
import cn.bugstack.domain.strategy.service.IRaffleRule;
import cn.bugstack.domain.strategy.service.IRaffleStrategy;
import cn.bugstack.domain.strategy.service.armory.IStrategyArmory;
import cn.bugstack.trigger.api.IRaffleStrategyService;
import cn.bugstack.trigger.api.dto.req.RaffleAwardListRequestDTO;
import cn.bugstack.trigger.api.dto.req.RaffleStrategyRequestDTO;
import cn.bugstack.trigger.api.dto.req.RaffleStrategyRuleWeightRequestDTO;
import cn.bugstack.trigger.api.dto.resp.RaffleAwardListResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RaffleStrategyResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RaffleStrategyRuleWeightResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.trigger.api.dto.resp.StrategyAward;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.utils.Validator;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/raffle/")

public class RaffleStrategyController implements IRaffleStrategyService {
    private static final Log log = Log.get(RaffleStrategyController.class);
    @Resource
    private IRaffleAward raffleAward;
    @Resource
    private IRaffleStrategy raffleStrategy;
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IRaffleRule raffleRule;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    /**
     * 策略装配，将策略信息装配到缓存中
     */
    @RequestMapping(value = "strategy_armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> strategyArmory(@RequestParam Long strategyId) {
        log.info("抽奖策略装配开始 strategyId：{}", strategyId);
        boolean armoryStatus = strategyArmory.assembleLotteryStrategy(strategyId);
        Response<Boolean> response = Response.<Boolean>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(armoryStatus)
                .build();
        log.info("抽奖策略装配完成 strategyId：{} response: {}", strategyId, JSON.toJSONString(response));
        return response;
    }

    /**
     * 查询奖品列表
     */
    @RequestMapping(value = "query_raffle_award_list", method = RequestMethod.POST)
    @Override
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(@RequestBody RaffleAwardListRequestDTO request) {
        log.info("查询抽奖奖品列表开始 requestDTO：{}", request);
        Validator.validateOrThrow(request);

        // 查询奖品配置
        List<StrategyAwardEntity> strategyAwardEntities = raffleAward.queryRaffleStrategyAwardListByActivityId(request.getActivityId());

        // 获取规则配置
        String[] ruleModelList = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getRuleModels)
                .filter(StringUtils::isNoneBlank)
                .distinct()
                .toArray(String[]::new);

        // 重新规则配置-获取奖品解锁限制
        Map<String, Integer> ruleLockCountMap = raffleRule.queryAwardRuleLockCount(ruleModelList);

        // 查询抽奖次数 用户已经参与抽奖次数
        Integer dayPartTakeCount = raffleActivityAccountQuotaService.queryRaffleActivityAccountDayPartakeCount(request.getActivityId(), request.getUserId());

        // 遍历填充数据
        List<RaffleAwardListResponseDTO> raffleAwardListResponseDTOS = new ArrayList<>(strategyAwardEntities.size());
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Integer awardRuleLockCount = ruleLockCountMap.get(strategyAward.getRuleModels());
            raffleAwardListResponseDTOS.add(RaffleAwardListResponseDTO.builder()
                    .awardId(strategyAward.getAwardId())
                    .awardTitle(strategyAward.getAwardTitle())
                    .awardSubtitle(strategyAward.getAwardSubtitle())
                    .sort(strategyAward.getSort())
                    .awardRuleLockCount(awardRuleLockCount)
                    .isAwardUnlock(null == awardRuleLockCount || dayPartTakeCount > awardRuleLockCount)
                    .waitUnLockCount(null == awardRuleLockCount || dayPartTakeCount > awardRuleLockCount ? 0 : awardRuleLockCount - dayPartTakeCount)
                    .build());
        }

        log.info("查询抽奖奖品列表完成 request:{} size:{}", request, raffleAwardListResponseDTOS.size());
        return Response.<List<RaffleAwardListResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(raffleAwardListResponseDTOS)
                .build();
    }

    /**
     * 随机抽奖接口(已弃用)
     */
    @Deprecated
    @RequestMapping(value = "random_raffle", method = RequestMethod.POST)
    @Override
    public Response<RaffleStrategyResponseDTO> randomRaffle(@RequestBody RaffleStrategyRequestDTO requestDTO) {
        log.info("随机抽奖开始 strategyId: {}", requestDTO.getStrategyId());

        // 调用抽奖接口
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                .userId("system")
                .strategyId(requestDTO.getStrategyId())
                .build());

        Response<RaffleStrategyResponseDTO> response = Response.<RaffleStrategyResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(RaffleStrategyResponseDTO.builder()
                        .awardId(raffleAwardEntity.getAwardId())
                        .awardIndex(raffleAwardEntity.getSort())
                        .build())
                .build();
        log.info("随机抽奖完成 strategyId: {} response: {}", requestDTO.getStrategyId(), JSON.toJSONString(response));
        return response;
    }

    /**
     * 查询抽奖策略权重规则
     */
    @RequestMapping(value = "query_raffle_strategy_rule_weight", method = RequestMethod.POST)
    @Override
    public Response<List<RaffleStrategyRuleWeightResponseDTO>> queryRaffleStrategyRuleWeight(@RequestBody RaffleStrategyRuleWeightRequestDTO request) {
        log.info("查询抽奖策略权重规则 request: {}", request);


        // 查询用户抽奖次数
        Integer accountUserCount = raffleActivityAccountQuotaService.queryRaffleActivityAccountPartakeCount(request.getActivityId(), request.getUserId());

        List<RuleWeightVO> ruleWeightVOList = raffleRule.queryAwardRuleWeightListByActivityId(request.getActivityId());
        List<RaffleStrategyRuleWeightResponseDTO> raffleStrategyRuleWeightList = getRaffleStrategyRuleWeightResponseDTOS(ruleWeightVOList, accountUserCount);

        log.info("查询抽奖策略权重规则完成 request:{} size:{}", request, raffleStrategyRuleWeightList.size());
        return Response.<List<RaffleStrategyRuleWeightResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(raffleStrategyRuleWeightList)
                .build();
    }

    private List<RaffleStrategyRuleWeightResponseDTO> getRaffleStrategyRuleWeightResponseDTOS(List<RuleWeightVO> ruleWeightVOList, Integer accountUserCount) {
        List<RaffleStrategyRuleWeightResponseDTO> raffleStrategyRuleWeightList = new ArrayList<>();
        for (RuleWeightVO ruleWeightVO : ruleWeightVOList) {
            // 转换对象
            List<StrategyAward> strategyAwards = new ArrayList<>();
            List<RuleWeightVO.Award> awardList = ruleWeightVO.getAwardList();
            for (RuleWeightVO.Award award : awardList) {
                StrategyAward strategyAward = new StrategyAward();
                strategyAward.setAwardId(award.getAwardId());
                strategyAward.setAwardTitle(award.getAwardTitle());
                strategyAwards.add(strategyAward);
            }
            // 封装对象
            RaffleStrategyRuleWeightResponseDTO raffleStrategyRuleWeightResponseDTO = new RaffleStrategyRuleWeightResponseDTO();
            raffleStrategyRuleWeightResponseDTO.setRuleWeightCount(ruleWeightVO.getWeight());
            raffleStrategyRuleWeightResponseDTO.setStrategyAwards(strategyAwards);
            raffleStrategyRuleWeightResponseDTO.setUseActivityAccountTotalUseCount(accountUserCount);

            raffleStrategyRuleWeightList.add(raffleStrategyRuleWeightResponseDTO);
        }
        return raffleStrategyRuleWeightList;
    }
}