package org.fuwjin.bespect;

import static java.lang.Math.max;
import static org.fuwjin.bespect.BespectUtils.invoke;
import static org.fuwjin.bespect.BespectUtils.passReturn;
import static org.fuwjin.bespect.BespectUtils.pushArgs;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class PreCallAdvice extends MethodAdvice{
   private final MethodDef advice;
   private final MethodDef redirect;

   public PreCallAdvice(MethodDef target, MethodDef redirect, MethodDef advice){
      super(target);
      this.redirect = redirect;
      this.advice = advice;
   }
   
   @Override
   public int access(){
      return super.access() & ~ACC_NATIVE;
   }
   
   @Override
   public MethodVisitor build(ClassVisitor cv){
      MethodVisitor mv = super.build(cv);
      mv.visitCode();
      int count = pushArgs(mv, advice);
      invoke(mv, advice);
      pushArgs(mv, redirect);
      invoke(mv, redirect);
      passReturn(mv, redirect);
      mv.visitMaxs(max(count, 1), 2);
      mv.visitEnd();
      return mv;
   }
}
