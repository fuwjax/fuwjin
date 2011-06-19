package org.fuwjin.dinah.adapter;

import org.fuwjin.dinah.Adapter.AdaptException;

public interface ClassAdapter {
   <T>T adapt(Object value, Class<T> type) throws AdaptException;

   boolean canAdapt(Class<?> fromClass, Class<?> toClass);

   boolean canDefault(Class<?> type);

   <T>T defaultValue(Class<T> type) throws AdaptException;
}
