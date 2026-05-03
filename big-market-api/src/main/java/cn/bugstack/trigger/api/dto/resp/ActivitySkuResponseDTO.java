package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 活动SKU响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySkuResponseDTO {

    /** SKU */
    private Long sku;

    /** 活动ID */
    private Long activityId;

    /** 活动个人参与次数ID */
    private Long activityCountId;

    /** 库存总量 */
    private Integer stockCount;

    /** 剩余库存 */
    private Integer stockCountSurplus;

    /** 商品金额（积分） */
    private BigDecimal productAmount;

}
