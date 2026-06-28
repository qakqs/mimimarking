package cn.bugstack.test;

import cn.bugstack.Application;
import cn.bugstack.domain.task.service.impl.OrderAuditEngine;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.hibernate.validator.internal.metadata.facets.Validatable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Validator;

import java.util.*;

@SpringBootTest(classes = {Application.class})

public class Base {
    private static final Log log = Log.get(Base.class);

    @Resource
    private OrderAuditEngine orderAuditEngine;

    @Test
    public void tett() {

        List<List<String>> batches = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            batches.add(Arrays.asList("12", "12", "123132", "123123", "123123123"));

        }
        List<String> strings = orderAuditEngine.auditOrders(batches);
        System.out.println(strings);
    }

    public static void main(String[] args) {
        String s = "  a good   example  ";
        System.out.println(s);
    }


}

