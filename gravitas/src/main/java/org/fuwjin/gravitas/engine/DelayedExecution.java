package org.fuwjin.gravitas.engine;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fuwjin.gravitas.engine.ExecutionEngine.DO_NOT_DELAY;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(TYPE)
@Retention(RUNTIME)
public @interface DelayedExecution{
   long delay() default DO_NOT_DELAY;

   TimeUnit unit() default MILLISECONDS;
}
