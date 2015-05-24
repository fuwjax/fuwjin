Impl =java.lang.Class
    <-
'package ' Package~getPackage ';

import java.util.concurrent.Executor;

public final class ' ClassDecl~this ' implements ' Interface~this ' {
   private final Executor executor;
   private final ' Interface~this ' target;

   public ' Name~this '(final Executor executor, final ' Interface~this ' target) {
      this.executor = executor;
      this.target = target;
   }
   
   @Override
   public String toString(){
      return target.toString();
   }
   
   @Override
   public int hashCode(){
      return 31 * target.hashCode() + executor.hashCode();
   }
   
   @Override
   public boolean equals(final Object obj) {
      if(this == obj){
         return true;
      }
      if(obj instanceof ' Name~this '){
         final ' Name~this ' o = (' Name~this ')obj;
         return executor.equals(o.executor) && target.equals(o.target);
      }
      return false;
   }' Methods~getMethods? '
}
'

Package =java.lang.Package
    <- Lit~getName
Name =java.lang.Class
    <- Lit~getSimpleName 'Async'

ClassDecl =java.lang.Class
    <- Name~this ('<' TypeVars~getTypeParameters '>')?

Interface =java.lang.Class
    <- Lit~getCanonicalName ('<' Types~getTypeParameters '>')?

TypeVars =java.util.Arrays~asList
    <- TypeVarsImpl~this
TypeVarsImpl =java.lang.Iterable~iterator
    <- TypeVariable~next ( ', ' TypeVariable~next )*
TypeVariable =java.lang.reflect.TypeVariable
    <- Lit~getName ' extends ' Types~getBounds
Types =java.util.Arrays~asList
    <- TypesImpl~this
TypesImpl =java.lang.Iterable~iterator
    <- Type~next ( ', ' Type~next )*
Type <- Class~this / Lit~this
Class =java.lang.Class
    <- Lit~getCanonicalName

Methods =java.util.Arrays~asList
    <- MethodsImpl~this
MethodsImpl =java.lang.Iterable~iterator
    <- Method~next+
Method <- AsyncMethod~this / VoidMethod~this / SyncMethod~this

AsyncMethod =org.fuwjin.xuter.scheduler.AsyncBinder~isAsync
    <-
'

   @Override
   ' Signature~this ' {
      executor.execute(new Runnable(){
         @Override
         public void run(){
            target.' Invocation~this ';
         }
      });
   }'

VoidMethod =org.fuwjin.xuter.scheduler.AsyncBinder~isVoid
    <-
'

   @Override
   ' Signature~this ' {
      target.' Invocation~this ';
   }'

SyncMethod
    <-
'

   @Override
   ' Signature~this ' {
      return target.' Invocation~this ';
   }'

Signature =java.lang.reflect.Method
    <- 'public ' ('<' TypeVars~getTypeParameters '> ')? Type~getGenericReturnType ' ' Lit~getName '(' Parameters~getGenericParameterTypes? ')' ( ' throws ' Types~getGenericExceptionTypes)?
Invocation =java.lang.reflect.Method
    <- Lit~getName '(' Args~getGenericParameterTypes? ')'

Args =org.fuwjin.xuter.scheduler.gen.IndexedIterator~new
    <- Arg~next ( ', ' Arg~next )*
Arg =org.fuwjin.xuter.scheduler.gen.IndexedValue
    <- 'arg' Lit~getIndex

Parameters =org.fuwjin.xuter.scheduler.gen.IndexedIterator~new
    <- Parameter~next ( ', ' Parameter~next )*
Parameter =org.fuwjin.xuter.scheduler.gen.IndexedValue
    <- 'final ' Type~getValue ' ' Arg~this

Lit <- .
