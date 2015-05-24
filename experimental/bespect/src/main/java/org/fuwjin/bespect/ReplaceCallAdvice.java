package org.fuwjin.bespect;

import static org.fuwjin.bespect.BespectUtils.invoke;
import static org.fuwjin.bespect.BespectUtils.passReturn;
import static org.fuwjin.bespect.BespectUtils.pushArgs;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class ReplaceCallAdvice extends MethodAdvice{
   private final MethodDef advice;

   public ReplaceCallAdvice(MethodDef target, MethodDef advice){
      super(target);
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
      passReturn(mv, advice);
      mv.visitMaxs(count, 2);
      mv.visitEnd();
      return mv;
   }
}
