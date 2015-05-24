package org.fuwjin.bespect;

import static org.fuwjin.bespect.BespectUtils.invoke;
import static org.fuwjin.bespect.BespectUtils.passReturn;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class PostCallAdvice extends MethodAdvice{
   private final MethodDef advice;
   private final MethodDef redirect;

   public PostCallAdvice(MethodDef target, MethodDef redirect, MethodDef advice){
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
      invoke(mv, redirect);
      invoke(mv, advice);
      passReturn(mv, advice);
      mv.visitMaxs(1, 2);
      mv.visitEnd();
      return mv;
   }
}
