package cn.bugstack.domain.activity.service.armory;

/**
 * 活动装配预热
 */
public interface IActivityArmory {

    /**
     * 预热sku
     * @param sku
     * @return
     */
    boolean assembleActivitySku(Long sku);

}
