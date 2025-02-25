package com.aplazo.bnpl.challenge.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to indicate that a method should be logged by the LoggingAspect.
 * The `method` attribute allows specifying a custom method name for logging purposes.
 */
@Target(ElementType.METHOD) // This annotation can only be applied to methods.
@Retention(RetentionPolicy.RUNTIME) // This annotation is retained at runtime.
public @interface Loggable {

    /**
     * Custom method name to use for logging.
     * If not specified, the method name will be derived automatically.
     *
     * @return the custom method name for logging
     */
    String method() default "";

}
