package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.CreditAdjustRequestDTO;
import cn.bugstack.trigger.api.dto.req.CreditOrderPageRequestDTO;
import cn.bugstack.trigger.api.dto.resp.CreditAccountResponseDTO;
import cn.bugstack.trigger.api.dto.resp.CreditOrderResponseDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;

/**
 * 后台积分管理 API 接口
 */
public interface IAdminCreditService {

    /** 积分账户列表 */
    Response<PageResponseDTO<CreditAccountResponseDTO>> accountList(CreditOrderPageRequestDTO request);

    /** 积分账户详情 */
    Response<CreditAccountResponseDTO> accountDetail(String userId);

    /** 手动调额 */
    Response<Void> adjust(CreditAdjustRequestDTO request);

    /** 积分交易流水 */
    Response<PageResponseDTO<CreditOrderResponseDTO>> orderList(CreditOrderPageRequestDTO request);

}
