package cn.bugstack.domain.user.model.aggregate;

import cn.bugstack.domain.user.model.entity.UserEntity;
import cn.bugstack.domain.user.model.valobj.UserStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * 用户聚合根
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAggregate {

    /** 用户实体 */
    private UserEntity userEntity;

    // ===== 工厂方法 =====

    /**
     * 创建注册用户聚合
     */
    public static UserAggregate createForRegister(String username, String encodedPassword) {
        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID().toString().replace("-", "").substring(0, 16))
                .username(username)
                .password(encodedPassword)
                .name(username)
                .status(UserStatusVO.ACTIVE.getCode())
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        return UserAggregate.builder()
                .userEntity(userEntity)
                .build();
    }

}
