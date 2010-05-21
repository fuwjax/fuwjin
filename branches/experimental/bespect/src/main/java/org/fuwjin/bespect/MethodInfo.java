package org.fuwjin.bespect;

import static org.objectweb.asm.Type.getMethodDescriptor;

import java.lang.reflect.Method;


public class MethodInfo implements MethodDef {
   private final int access;
   private final String name;
   private final String desc;
   private final String signature;
   private final String[] exceptions;
   private final String className;
   
   public static MethodInfo direct(Class<?> type, String name, String desc){
      if(type == null || name == null || desc == null){
         return null;
      }
      for(Method method: type.getDeclaredMethods()){
         if(method.getName().equals(name) && getMethodDescriptor(method).equals(desc)){
            String[] exceptions = null;
            if(method.getExceptionTypes().length > 0){
               exceptions = new String[method.getExceptionTypes().length];
               for(int i=0;i<exceptions.length;i++){
                  exceptions[i] = method.getExceptionTypes()[i].getName();
               }
            }
            return new MethodInfo(method.getModifiers(), type.getName().replace('.','/'), name, desc, null, exceptions);
         }
      }
      return direct(type.getSuperclass(), name, desc);
   }


   public MethodInfo(int access, String className, String name, String desc, String signature, String[] exceptions){
      this.access = access;
      this.className = className;
      this.name = name;
      this.desc = desc;
      this.signature = signature;
      this.exceptions = exceptions;
   }

   public int access(){
      return access;
   }

   public String desc(){
      return desc;
   }

   public String[] exceptions(){
      return exceptions;
   }
   
   public String className(){
      return className;
   }

   public String name(){
      return name;
   }

   public String signature(){
      return signature;
   }
}
