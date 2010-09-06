package org.fuwjin.postage;

public interface TypeHandler {
   Object getDefault();

   boolean isWrapper(Class<?> wrapper);

   Object toObject(String str);
}
