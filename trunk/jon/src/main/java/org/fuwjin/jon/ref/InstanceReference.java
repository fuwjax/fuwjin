package org.fuwjin.jon.ref;

import java.util.Iterator;

import org.fuwjin.jon.builder.util.SuperclassIterable;

public class InstanceReference extends BaseReference implements Iterable<BaseReference.MapReference> {
   private final ReferenceStorage storage;
   private final Iterable<Class<?>> iterable;
   private final Object value;

   public InstanceReference(final String name, final ReferenceStorage storage, final Object type, final Object value) {
      super(name, type);
      this.storage = storage;
      this.value = value;
      iterable = new SuperclassIterable(value.getClass());
   }

   @Override
   public Iterator<BaseReference.MapReference> iterator() {
      final Iterator<Class<?>> iter = iterable.iterator();
      return new Iterator<BaseReference.MapReference>() {
         @Override
         public boolean hasNext() {
            return iter.hasNext();
         }

         @Override
         public BaseReference.MapReference next() {
            return newReference(iter.next());
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   ObjectReference newReference(final Class<?> next) {
      return new ObjectReference(storage, next, value);
   }
}
