package com.raizlabs.android.broker.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: Defines the ResponseHandler for a {@link com.raizlabs.android.broker.core.RestService}.
 * All RestService must have this annotation in its definition.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface ResponseHandler {

    /**
     * @return The class to create as the response handler. Must implement ResponseHandler and
     * have an accessible default constructor.
     */
    Class<?> value();
}
