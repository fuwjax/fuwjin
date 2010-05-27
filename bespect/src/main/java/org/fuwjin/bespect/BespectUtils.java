package org.fuwjin.bespect;

import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Type.INT_TYPE;
import static org.objectweb.asm.Type.LONG_TYPE;
import static org.objectweb.asm.Type.getReturnType;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class BespectUtils{

   static void invoke(MethodVisitor mv, MethodDef def){
      mv.visitMethodInsn(BespectUtils.invokeOp(def.access()), def.className(), def.name(), def.desc());
   }

   static int pushArgs(MethodVisitor mv, MethodDef def){
      Type[] args = Type.getArgumentTypes(def.desc());
      for(int i=0;i<args.length;i++){
         mv.visitVarInsn(loadOp(args[i]), i);
      }
      return args.length;
   }

   private static int loadOp(Type type){
      if(LONG_TYPE.equals(type)){
         return LLOAD;
      }
      if(INT_TYPE.equals(type)){
         return ILOAD;
      }
      return ALOAD;
   }

   static int invokeOp(int access){
      return (access & ACC_STATIC) != 0 ? INVOKESTATIC : INVOKEVIRTUAL;
   }

   static int returnOp(Type type){
      if(LONG_TYPE.equals(type)){
         return LRETURN;
      }
      if(INT_TYPE.equals(type)){
         return IRETURN;
      }
      return ARETURN;
   }

   public static void passReturn(MethodVisitor mv, MethodDef def){
      mv.visitInsn(returnOp(getReturnType(def.desc())));
   }
}
