package org.fuwjin.jon.builder;

import static java.lang.reflect.Array.set;
import static org.fuwjin.jon.BuilderRegistry.getBuilder;
import static org.fuwjin.jon.builder.util.ArrayIterable.iterable;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.fuwjin.jon.ref.BaseReference;
import org.fuwjin.jon.ref.ListReference;
import org.fuwjin.jon.ref.ReferenceStorage;

public class ArrayBuilder extends ElementsBuilder {
   private Object arr;

   public ArrayBuilder(final Class<?> type) {
      super(type);
   }

   @Override
   public Builder newElement() {
      return getBuilder(type().getComponentType());
   }

   @Override
   public BaseReference newReference(final Object obj, final Class<?> cls, final ReferenceStorage storage) {
      return new ListReference(storage.nextName(), storage, cast(storage, obj, cls), iterable(obj), obj.getClass()
            .getComponentType());
   }

   @Override
   protected Object newInstance(final Object... args) {
      return new ArrayList<Object>();
   }

   @Override
   protected void postAdd(final int index, final Object value) {
      Array.set(arr, index, value);
   }

   @Override
   protected Object toObjectImpl() {
      arr = Array.newInstance(type().getComponentType(), list.size());
      if(type().getComponentType().isPrimitive()) {
         for(int index = 0; index < list.size(); index++) {
            set(arr, index, list.get(index));
         }
         return arr;
      }
      return list.toArray((Object[])arr);
   }
}
