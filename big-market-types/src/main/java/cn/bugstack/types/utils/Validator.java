package cn.bugstack.types.utils;

import cn.bugstack.types.common.Log;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.exception.AppException;
import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * General-purpose object validator wrapping Jakarta Bean Validation.
 * <p>
 * Validates fields annotated with {@code @NotNull}, {@code @NotBlank},
 * {@code @NotEmpty}, {@code @Size}, {@code @Email}, {@code @Min}, {@code @Max},
 * and any other {@code jakarta.validation.constraints.*} or custom constraint annotations.
 * </p>
 *
 * <pre>
 * // Check and get error list
 * List&lt;String&gt; errors = Validator.validate(entity);
 *
 * // Check and throw immediately
 * Validator.validateOrThrow(entity);
 * </pre>
 */
@Component
public class Validator {

    private static final Log log = Log.get(Validator.class);

    private static volatile jakarta.validation.Validator instance;

    public Validator(jakarta.validation.Validator validator) {
        Validator.instance = validator;
    }

    /**
     * Validate all constraint annotations on the object.
     *
     * @return list of error messages, empty if valid
     */
    public static <T> List<String> validate(T obj) {
        jakarta.validation.Validator v = instance;
        if (v == null) {
            log.warn("Validator not initialized, skip validation");
            return Collections.emptyList();
        }
        Set<ConstraintViolation<T>> violations = v.validate(obj);
        if (violations.isEmpty()) {
            return Collections.emptyList();
        }
        return violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
    }

    /**
     * Validate and throw {@link AppException} with ILLEGAL_PARAMETER code if errors exist.
     */
    public static <T> void validateOrThrow(T obj) {
        List<String> errors = validate(obj);
        if (!errors.isEmpty()) {
            String message = String.join("; ", errors);
            log.warn("对象校验失败: {}", message);
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), message);
        }
    }
}