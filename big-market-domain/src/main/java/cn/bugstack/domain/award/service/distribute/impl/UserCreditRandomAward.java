package cn.bugstack.domain.award.service.distribute.impl;

import cn.bugstack.domain.award.model.aggregate.GiveOutPrizesAggregate;
import cn.bugstack.domain.award.model.entity.DistributeAwardEntity;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.model.entity.UserCreditAwardEntity;
import cn.bugstack.domain.award.model.valobj.AwardStateVO;
import cn.bugstack.domain.award.repository.IAwardRepository;
import cn.bugstack.domain.award.service.distribute.IDistributeAward;
import cn.bugstack.types.common.Constants;
import cn.bugstack.types.exception.AppException;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;

@Component("user_credit_random")
public class UserCreditRandomAward implements IDistributeAward {
    private static final Log log = Log.get(UserCreditRandomAward.class);

    @Resource
    private IAwardRepository awardRepository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        Integer awardId = distributeAwardEntity.getAwardId();
        String awardConfig = distributeAwardEntity.getAwardConfig();
        if (StringUtils.isBlank(awardConfig)) {
            // 0.01~1or 1,100
            awardConfig = awardRepository.queryAwardConfig(awardId);
        }
        String[] awardConfigSplit = awardConfig.split(Constants.SPLIT);

        if (awardConfigSplit.length != 2) {
            throw new AppException("积分范围配置错误");
        }

        // 构建聚合对象
        UserAwardRecordEntity userAwardRecordEntity = GiveOutPrizesAggregate.buildDistributeUserAwardRecordEntity(
                distributeAwardEntity.getUserId(),
                distributeAwardEntity.getOrderId(),
                distributeAwardEntity.getAwardId(),
                AwardStateVO.complete
        );
        // 生成随机积分
        BigDecimal creditAmount = generateRandom(new BigDecimal(awardConfigSplit[0]), new BigDecimal(awardConfigSplit[1]));

        UserCreditAwardEntity userCreditAwardEntity = GiveOutPrizesAggregate.buildUserCreditAwardEntity(distributeAwardEntity.getUserId(), creditAmount);


        GiveOutPrizesAggregate prizesAggregate = GiveOutPrizesAggregate
                .builder()
                .userId(distributeAwardEntity.getUserId())
                .userAwardRecord(userAwardRecordEntity)
                .userCreditAwardEntity(userCreditAwardEntity)
                .build();

        awardRepository.saveOutPrizesAggregate(prizesAggregate);

    }

    private BigDecimal generateRandom(BigDecimal min, BigDecimal max) {
        if (min.equals(max)) {
            return min;
        }
        BigDecimal random = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return random.round(new MathContext(3));
    }
}
