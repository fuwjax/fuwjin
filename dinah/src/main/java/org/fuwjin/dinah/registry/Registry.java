package org.fuwjin.dinah.registry;

public interface Registry {
   <T>Binding<T> lookup(QualifiedName name, Class<T> type);

   <T>Binding<T> register(QualifiedName name, Class<T> type, T value);
}
