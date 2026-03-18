package cn.bugstack.domain.award.model.aggregate;

import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.model.entity.UserCreditAwardEntity;
import cn.bugstack.domain.award.model.valobj.AwardStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiveOutPrizesAggregate {

    private String userId;
    /**
     * 发奖记录
     */
    private UserAwardRecordEntity  userAwardRecord;

    /**
     * 用户积分
     */
    private UserCreditAwardEntity userCreditAwardEntity;

    public static UserAwardRecordEntity buildDistributeUserAwardRecordEntity(String userId, String orderId, Integer awardId, AwardStateVO awardStateVO) {
        UserAwardRecordEntity  userAwardRecordEntity = new UserAwardRecordEntity();
        userAwardRecordEntity.setUserId(userId);
        userAwardRecordEntity.setOrderId(orderId);
        userAwardRecordEntity.setAwardId(awardId);
        userAwardRecordEntity.setAwardState(awardStateVO);
        return userAwardRecordEntity;
    }

    public static UserCreditAwardEntity buildUserCreditAwardEntity(String userId, BigDecimal creditAmount) {
        UserCreditAwardEntity userCreditAward = new UserCreditAwardEntity();
        userCreditAward.setUserId(userId);
        userCreditAward.setCreditAmount(creditAmount);
        return userCreditAward;
    }
}
