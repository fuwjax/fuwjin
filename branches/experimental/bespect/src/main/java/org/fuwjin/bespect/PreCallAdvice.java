package org.fuwjin.bespect;

import static java.lang.Math.max;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Type.LONG_TYPE;
import static org.objectweb.asm.Type.getReturnType;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

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
      int count = pushArgs(mv, advice);
      invoke(mv, advice);
      pushArgs(mv, redirect);
      invoke(mv, redirect);
      mv.visitInsn(returnOp(getReturnType(redirect.desc())));
      mv.visitMaxs(max(count, 1), 2);
      mv.visitEnd();
   }

   private static void invoke(MethodVisitor mv, MethodDef def){
      mv.visitMethodInsn(invokeOp(def.access()), def.className(), def.name(), def.desc());
   }

   private static int pushArgs(MethodVisitor mv, MethodDef def){
      Type[] args = Type.getArgumentTypes(def.desc());
      for(int i=0;i<args.length;i++){
         mv.visitVarInsn(ALOAD, i);
      }
      return args.length;
   }
}
