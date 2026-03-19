package cn.bugstack.domain.credit.service;

import cn.bugstack.domain.credit.model.entity.TradeEntity;

/**
 * 积分调额接口【正逆向，增减积分】
 */
public interface ICreditAdjustService {
    /**
     * 创建增加积分额度订单
     * @param tradeEntity 交易实体对象
     * @return 单号
     */

    String createOrder(TradeEntity tradeEntity);
}
