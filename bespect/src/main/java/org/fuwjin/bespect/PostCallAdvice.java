package org.fuwjin.bespect;

import static org.objectweb.asm.Opcodes.ACC_NATIVE;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Type.LONG_TYPE;
import static org.objectweb.asm.Type.getReturnType;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

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
   
   private static int invokeOp(int access){
      return (access & ACC_STATIC) != 0 ? INVOKESTATIC : INVOKEVIRTUAL;
   }
   
   private static int returnOp(Type returnType){
      if(LONG_TYPE.equals(returnType)){
         return LRETURN;
      }
      return ARETURN;
   }

   @Override
   public void build(MethodVisitor mv){
      mv.visitCode();
      mv.visitMethodInsn(invokeOp(redirect.access()), redirect.className(), redirect.name(), redirect.desc());
      mv.visitMethodInsn(invokeOp(advice.access()), advice.className(), advice.name(), advice.desc());
      mv.visitInsn(returnOp(getReturnType(advice.desc())));
      mv.visitMaxs(1, 2);
      mv.visitEnd();
   }
}
