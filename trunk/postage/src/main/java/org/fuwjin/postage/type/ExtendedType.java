package org.fuwjin.postage.type;

import java.lang.reflect.Type;

public interface ExtendedType extends Type {
   Type getComponentType();

   Type[] getUpperBounds();

   boolean isAssignableFrom(Class<?> test);

   boolean isAssignableTo(Type test);

   boolean isClass();

   boolean isInstance(Object object);
}
