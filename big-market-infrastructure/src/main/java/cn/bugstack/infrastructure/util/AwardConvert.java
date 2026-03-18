package cn.bugstack.infrastructure.util;

import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.model.entity.UserCreditAwardEntity;
import cn.bugstack.infrastructure.persistent.po.UserAwardRecord;
import cn.bugstack.infrastructure.persistent.po.UserCreditAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")

public interface AwardConvert {

    @Mappings({
              @Mapping(source = "creditAmount ", target = "totalAmount"),
    @Mapping(source = "creditAmount", target = "availableAmount")
    })
    UserCreditAccount UserCreditAccountConvert(UserCreditAwardEntity userCreditAwardEntity);

    UserAwardRecord userAwardRecordConvert(UserAwardRecordEntity userAwardRecordEntity);
}
