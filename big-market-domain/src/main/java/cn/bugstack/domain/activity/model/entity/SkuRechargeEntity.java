package cn.bugstack.domain.activity.model.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 活动购物车实体对象
 * @create 2024-03-16 08:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SkuRechargeEntity {


    /**
     * 用户ID
     */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /**
     * 商品SKU - activity + activity count
     */
    @NotNull(message = "skuId不能为空")
    private Long sku;

    /**
     * 外部幂等键
     */
    @NotNull(message = "outBusinessNo不能为空")
    private String outBusinessNo;

}
