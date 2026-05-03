package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SKU库存调整请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySkuStockRequestDTO {

    /** SKU */
    private Long sku;

    /** 调整数量（正数增加，负数减少） */
    private Integer delta;

}
