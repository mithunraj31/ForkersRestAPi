package com.mbel.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CustomerValidator implements ConstraintValidator<CustomerType, String> {

    List<String> customers = Arrays.asList("Customer", "Sales Destination", "Contractor");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return customers.contains(value);

    }
}