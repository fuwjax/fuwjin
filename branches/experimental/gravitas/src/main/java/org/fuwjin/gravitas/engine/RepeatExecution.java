package org.fuwjin.gravitas.engine;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(TYPE)
@Retention(RUNTIME)
public @interface RepeatExecution{
   long repeatEvery() default -1;

   TimeUnit unit() default MILLISECONDS;

   long waitBetween() default -1;
}
