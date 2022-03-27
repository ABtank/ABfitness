package ru.abfitness.oapi.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EqualFieldsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EqualFieldsConstraint {
    String message() default "Пароль и его подтверждение не совпадают";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String baseField() default "passwordConfirmation";
    String matchField() default "password";
}
