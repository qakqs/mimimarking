package cn.bugstack.domain.credit.model.entity;

import cn.bugstack.domain.activity.model.valobj.OrderTradeTypeVO;
import cn.bugstack.domain.credit.model.valobj.TradeNameVO;
import cn.bugstack.domain.credit.model.valobj.TradeTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeEntity {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 交易名称
     */
    private TradeNameVO tradeName;
    /**
     * 交易类型；交易类型；forward-正向、reverse-逆向
     */
    private TradeTypeVO tradeType;
    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 业务仿重ID - 外部透传。返利、行为等唯一标识
     */
    private String outBusinessNo;

    /**
     * 交易类型
     */
    private OrderTradeTypeVO orderTradeType = OrderTradeTypeVO.rebate_no_pay_trade;


}
