package org.fuwjin.dinah.registry;

public interface Binding<T> {
   String name();

   Class<T> type();

   T value();
}
