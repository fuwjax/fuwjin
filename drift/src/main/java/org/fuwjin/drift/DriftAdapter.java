package org.fuwjin.drift;

import static org.ow2.util.asm.Opcodes.GETSTATIC;
import static org.ow2.util.asm.Opcodes.INVOKEVIRTUAL;

import org.ow2.util.asm.ClassAdapter;
import org.ow2.util.asm.ClassVisitor;
import org.ow2.util.asm.MethodAdapter;
import org.ow2.util.asm.MethodVisitor;

public class DriftAdapter extends ClassAdapter{
   private String className;

   public DriftAdapter(String className, ClassVisitor visitor){
      super(visitor);
      this.className = className;
   }
   
   @Override
   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
      return new DriftMethod(name, super.visitMethod(access, name, desc, signature, exceptions));
   }
   
   private class DriftMethod extends MethodAdapter{
      private final String methodName;

      public DriftMethod(String methodName, MethodVisitor visitor){
         super(visitor);
         this.methodName = methodName;
      }
      
      @Override
      public void visitCode(){
         super.visitCode();
         super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
         super.visitLdcInsn(className+'#'+methodName);
         super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
      }
   }

}
