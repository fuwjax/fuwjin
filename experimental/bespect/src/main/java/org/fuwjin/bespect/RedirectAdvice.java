package org.fuwjin.bespect;

import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

public class RedirectAdvice extends MethodAdvice{
   private String prefix;

   public RedirectAdvice(MethodDef target, String prefix){
      super(target);
      this.prefix = prefix;
   }
   
   @Override
   public int access(){
      return super.access() & ~ACC_PUBLIC & ~ACC_PROTECTED;
   }
   
   @Override
   public String name(){
      return prefix+super.name();
   }
}
