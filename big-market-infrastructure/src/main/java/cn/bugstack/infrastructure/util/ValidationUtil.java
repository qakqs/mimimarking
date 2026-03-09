package cn.bugstack.infrastructure.util;

import cn.bugstack.types.entity.ActivityShopCartEntity;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ValidationUtil {

    public static void validate(Object object) {
        try {
            Field[] declaredFields = object.getClass().getDeclaredFields();

            for (Field field : declaredFields) {
                field.setAccessible(true);
                Annotation[] annotations = field.getAnnotations();
                if (annotations == null || annotations.length == 0) {
                    return;
                }
                Object fieldValue = field.get(object);
                Annotation fieldAnnotation = annotations[0];
                NotBlank fieldAnnotation1 = (NotBlank) fieldAnnotation;
                String message = fieldAnnotation1.message();
            }

        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        ActivityShopCartEntity activityShopCartEntity = new ActivityShopCartEntity();
        activityShopCartEntity.setSku(111L);
        validate(activityShopCartEntity);
    }
}
