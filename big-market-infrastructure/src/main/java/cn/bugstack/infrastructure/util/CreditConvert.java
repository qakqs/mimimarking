package cn.bugstack.infrastructure.util;

import cn.bugstack.domain.credit.model.entity.CreditAccountEntity;
import cn.bugstack.domain.credit.model.entity.CreditOrderEntity;
import cn.bugstack.infrastructure.persistent.po.Task;
import cn.bugstack.infrastructure.persistent.po.UserCreditAccount;
import cn.bugstack.infrastructure.persistent.po.UserCreditOrder;
import cn.bugstack.types.common.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CreditConvert {

    @Mappings({
            @Mapping(target = "totalAmount", source = "adjustAmount"),
            @Mapping(target = "availableAmount", source = "adjustAmount"),
    })
    UserCreditAccount creditAccountConvert(CreditAccountEntity creditAccountEntity);

    UserCreditOrder userCreditOrderConvert(CreditOrderEntity creditOrderEntity);

    Task taskConvert(TaskEntity taskEntity);

}
