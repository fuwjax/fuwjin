package org.fuwjin.dinah;

import java.lang.reflect.Type;

public interface FunctionSignature {
   FunctionSignature accept(int paramCount);

   String category();

   boolean matchesFixed(Type... params);

   boolean matchesVarArgs(Type... params);

   String name();
}
