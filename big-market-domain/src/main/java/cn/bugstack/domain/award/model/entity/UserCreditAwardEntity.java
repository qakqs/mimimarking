package cn.bugstack.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserCreditAwardEntity {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 总积分，显示总账户值，记得一个人获得的总积分
     */
    private BigDecimal creditAmount;

    /**
     * 奖品设置值
     */
    private String awardConfig;


}
