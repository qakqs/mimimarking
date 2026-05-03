package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 活动SKU保存请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySkuSaveRequestDTO {

    /** SKU（编辑时传入） */
    private Long sku;

    /** 活动ID */
    private Long activityId;

    /** 活动个人参与次数ID */
    private Long activityCountId;

    /** 库存总量 */
    private Integer stockCount;

    /** 商品金额（积分） */
    private BigDecimal productAmount;

}
