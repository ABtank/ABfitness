package ru.abfitness.oapi.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class EqualFieldsValidator implements ConstraintValidator<EqualFieldsConstraint, Object> {
    private String baseField;
    private String matchField;

    @Override
    public void initialize(EqualFieldsConstraint constraint) {
        baseField = constraint.baseField();
        matchField = constraint.matchField();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Object field1 = getFieldValue(object, baseField);
            Object field2 = getFieldValue(object, matchField);
            return field1 != null && field1.equals(field2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> aClass = object.getClass();
        Field field = aClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
