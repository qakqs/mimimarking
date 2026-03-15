package cn.bugstack.domain.activity.service.partake;

import cn.bugstack.domain.activity.model.entity.ActivityAccountDayEntity;
import cn.bugstack.domain.activity.model.entity.ActivityAccountEntity;
import cn.bugstack.domain.activity.model.entity.ActivityAccountMonthEntity;
import cn.bugstack.domain.activity.model.entity.UserRaffleOrderEntity;
import cn.bugstack.domain.strategy.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.aggreate.CreatePartakeOrderAggregate;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.domain.activity.model.valobj.UserRaffleOrderStateVO;
import cn.bugstack.types.exception.AppException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class RaffleActivityPartakeService extends AbstractRaffleActivityPartake {

    private final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    protected RaffleActivityPartakeService(IActivityRepository activityRepository) {
        super(activityRepository);
    }

    @Override
    protected CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date now) {

        // 查&校验总额度
        ActivityAccountEntity activityAccountEntity = activityRepository.queryActivityAccountByUserId(userId, activityId);
        if (activityAccountEntity == null) {
            throw new AppException(ResponseCode.ACTIVITY_NULL_ERROR);
        }
        if (activityAccountEntity.getTotalCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACTIVITY_COUNT_ZERO_ERROR);
        }
        String month = dateFormatMonth.format(now);
        // 查&校验月额度
        ActivityAccountMonthEntity activityAccountMonthEntity = activityRepository.queryActivityAccountMonthByUserId(userId, activityId, month);

        if (activityAccountMonthEntity != null && activityAccountMonthEntity.getMonthCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACTIVITY_COUNT_ZERO_ERROR);
        }
        // 如果没有月度账户 创建阅读用户额度
        boolean isExistAccountMonth = activityAccountMonthEntity != null;
        if (activityAccountMonthEntity == null) {
            activityAccountMonthEntity = new ActivityAccountMonthEntity();
            activityAccountMonthEntity.setUserId(userId);
            activityAccountMonthEntity.setActivityId(activityId);
            activityAccountMonthEntity.setMonth(month);
            activityAccountMonthEntity.setMonthCount(activityAccountEntity.getMonthCount());
            activityAccountMonthEntity.setMonthCountSurplus(activityAccountEntity.getMonthCountSurplus());
        }

        // 查&校验日额度
        String day = dateFormatDay.format(now);
        ActivityAccountDayEntity activityAccountDayEntity = activityRepository.queryActivityAccountDayByUserId(userId, activityId, day);
        if (activityAccountDayEntity != null && activityAccountDayEntity.getDayCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACTIVITY_COUNT_ZERO_ERROR);
        }
        // 如果没有日度账户 创建阅读用户额度
        boolean isExistAccountDay = activityAccountDayEntity != null;
        if (activityAccountDayEntity == null) {
            activityAccountDayEntity = new ActivityAccountDayEntity();
            activityAccountDayEntity.setUserId(userId);
            activityAccountDayEntity.setActivityId(activityId);
            activityAccountDayEntity.setDay(day);
            activityAccountDayEntity.setDayCount(activityAccountEntity.getDayCount());
            activityAccountDayEntity.setDayCountSurplus(activityAccountEntity.getDayCountSurplus());
        }
        // 创建订单
        // 构建对象
        CreatePartakeOrderAggregate createPartakeOrderAggregate = new CreatePartakeOrderAggregate();
        createPartakeOrderAggregate.setUserId(userId);
        createPartakeOrderAggregate.setActivityId(activityId);
        createPartakeOrderAggregate.setActivityAccountEntity(activityAccountEntity);
        createPartakeOrderAggregate.setExistAccountMonth(isExistAccountMonth);
        createPartakeOrderAggregate.setActivityAccountMonthEntity(activityAccountMonthEntity);
        createPartakeOrderAggregate.setExistAccountDay(isExistAccountDay);
        createPartakeOrderAggregate.setActivityAccountDayEntity(activityAccountDayEntity);

        return createPartakeOrderAggregate;
    }

    @Override
    protected UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date now) {
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);
        // 构建订单
        UserRaffleOrderEntity userRaffleOrder = new UserRaffleOrderEntity();
        userRaffleOrder.setUserId(userId);
        userRaffleOrder.setActivityId(activityId);
        userRaffleOrder.setActivityName(activityEntity.getActivityName());
        userRaffleOrder.setStrategyId(activityEntity.getStrategyId());
        userRaffleOrder.setOrderId(RandomStringUtils.randomNumeric(12));
        userRaffleOrder.setOrderTime(now);
        userRaffleOrder.setOrderState(UserRaffleOrderStateVO.create);
        userRaffleOrder.setEndDateTime(activityEntity.getEndDateTime());
        return userRaffleOrder;
    }

}
