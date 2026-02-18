package cn.bugstack.domain.strategy.model.vo;

import cn.bugstack.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {


    private String ruleModels;

    public String[] raffleCenterRuleModelList(){
        List<String> ruleModelList = new ArrayList<>();
        String[] ruleModelArray = ruleModels.split(Constants.SPLIT);
        for (String ruleModel : ruleModelArray) {
            if (LogicModel.isCenter(ruleModel)) {
                ruleModelList.add(ruleModel);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }
}
