package org.fuwjin.jon;

import java.lang.reflect.InvocationTargetException;
import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.dinah.Function;

public class SetterTask implements Task {
   private final Container obj;
   private final Function setter;

   public SetterTask(final Container obj, final Function setter) {
      this.obj = obj;
      this.setter = setter;
   }

   @Override
   public void resolve(final Object value) throws Exception {
      obj.set(setter, value);
   }
}
