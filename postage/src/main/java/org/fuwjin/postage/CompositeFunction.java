package org.fuwjin.postage;

import static org.fuwjin.postage.Failure.assertResult;
import static org.fuwjin.postage.Failure.typesOf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.fuwjin.postage.Failure.FailureException;
import org.fuwjin.postage.function.MappedTarget;
import org.fuwjin.postage.type.Optional;
import org.fuwjin.postage.type.TypeUtils;

/**
 * A function that merges several functions.
 */
public class CompositeFunction implements Function {
   private final List<FunctionTarget> functions;
   private final String name;

   public CompositeFunction(final String name) {
      this.name = name;
      functions = new LinkedList<FunctionTarget>();
   }

   public CompositeFunction(final String name, final FunctionTarget function) {
      this.name = name;
      functions = Collections.singletonList(function);
   }

   private void addSignature(final CompositeFunction composite, final int index, final Type returnType,
         final Type[] parameters) {
      if(index == parameters.length) {
         for(final FunctionTarget target: functions) {
            final FunctionTarget mapped = MappedTarget.mapped(target, returnType, parameters);
            if(mapped != null) {
               composite.addTarget(mapped);
            }
         }
      } else {
         addSignature(composite, index + 1, returnType, parameters);
         if(parameters[index] instanceof Optional) {
            final Type[] voided = new Type[parameters.length];
            System.arraycopy(parameters, 0, voided, 0, parameters.length);
            voided[index] = void.class;
            addSignature(composite, index + 1, returnType, voided);
         }
      }
   }

   public void addTarget(final FunctionTarget function) {
      functions.add(function);
   }

   @Override
   public Function curry(final Object... args) {
      return filter(new CurryTransform(args));
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final CompositeFunction o = (CompositeFunction)obj;
         return super.equals(obj) && name.equals(o.name) && functions.equals(o.functions);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Function filter(final TargetTransform filter) {
      final CompositeFunction composite = new CompositeFunction(name);
      for(final FunctionTarget target: functions) {
         final FunctionTarget newTarget = filter.transform(target);
         if(newTarget != null) {
            composite.addTarget(newTarget);
         }
      }
      if(composite.functions.isEmpty()) {
         throw new IllegalArgumentException("No such function found");
      }
      return composite;
   }

   @Override
   public int hashCode() {
      return functions.hashCode();
   }

   @Override
   public Object invoke(final Object... args) throws FailureException {
      return assertResult(invokeSafe(args));
   }

   @Override
   public Object invokeSafe(final Object... args) {
      for(final FunctionTarget function: functions) {
         try {
            if(!isAppropriate(function, args)) {
               continue;
            }
            final Object result = function.invoke(args);
            if(!Failure.isRecoverable(result)) {
               return result;
            }
         } catch(final InvocationTargetException e) {
            return new Failure(true, e.getCause(), "Unexpected invocation failure");
         } catch(final Exception e) {
            // recoverable
         }
      }
      return new Failure("could not find appropriate function for %s", typesOf(args));
   }

   private boolean isAppropriate(final FunctionTarget function, final Object[] args) {
      if(args.length < function.requiredArguments()) {
         return false;
      }
      for(int index = 0; index < args.length; index++) {
         final Type t = function.parameterType(index);
         if(!void.class.equals(t) && (Optional.UNSET.equals(args[index]) || !TypeUtils.isAssignable(t, args[index]))) {
            return false;
         }
      }
      return true;
   }

   protected boolean isEmpty() {
      return functions.isEmpty();
   }

   @Override
   public String name() {
      return name;
   }

   @Override
   public Type parameterType(final int index) {
      if(functions.isEmpty()) {
         return null;
      }
      Type type = functions.get(0).parameterType(index);
      for(final FunctionTarget target: functions) {
         type = TypeUtils.union(type, target.parameterType(index));
      }
      return type;
   }

   @Override
   public Type returnType() {
      if(functions.isEmpty()) {
         return null;
      }
      Type type = functions.get(0).returnType();
      for(final FunctionTarget target: functions) {
         type = TypeUtils.union(type, target.returnType());
      }
      return type;
   }

   @Override
   public String toString() {
      return functions.toString();
   }

   @Override
   public Function withSignature(final Type returnType, final Type... parameters) {
      final CompositeFunction composite = new CompositeFunction(name);
      addSignature(composite, 0, returnType, parameters);
      if(composite.functions.isEmpty()) {
         throw new IllegalArgumentException("No such function found: " + returnType + " " + name + " "
               + Arrays.toString(parameters));
      }
      return composite;
   }
}
