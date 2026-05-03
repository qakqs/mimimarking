package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返利配置保存请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RebateConfigSaveRequestDTO {

    /** 自增ID（编辑时传入） */
    private Long id;

    /** 行为类型（sign 签到、openai_pay 支付） */
    private String behaviorType;

    /** 返利描述 */
    private String rebateDesc;

    /** 返利类型（sku 活动库存充值商品、integral 用户活动积分） */
    private String rebateType;

    /** 返利配置 */
    private String rebateConfig;

    /** 状态（open 开启、close 关闭） */
    private String state;

}
