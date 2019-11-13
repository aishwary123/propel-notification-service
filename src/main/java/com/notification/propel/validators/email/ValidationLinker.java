package com.notification.propel.validators.email;

import com.notification.propel.validators.IValidator;

/**
 * This class will help in creating a decorator of validators.
 * 
 * @author aishwaryt
 * @param <T>
 */
public abstract class ValidationLinker<T> implements IValidator<T> {

    protected IValidator<T> nextValidator;

    public ValidationLinker(IValidator<T> validator) {
        nextValidator = validator;
    }
}
