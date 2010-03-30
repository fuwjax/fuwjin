/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.bespect;

import static org.ow2.util.asm.Opcodes.ACC_NATIVE;
import static org.ow2.util.asm.Opcodes.ACC_PROTECTED;
import static org.ow2.util.asm.Opcodes.ACC_PUBLIC;
import static org.ow2.util.asm.Opcodes.ACC_STATIC;
import static org.ow2.util.asm.Opcodes.ARETURN;
import static org.ow2.util.asm.Opcodes.INVOKESTATIC;
import static org.ow2.util.asm.Opcodes.INVOKEVIRTUAL;
import static org.ow2.util.asm.Opcodes.LRETURN;
import static org.ow2.util.asm.Type.LONG_TYPE;
import static org.ow2.util.asm.Type.getMethodDescriptor;
import static org.ow2.util.asm.Type.getReturnType;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.ow2.util.asm.ClassAdapter;
import org.ow2.util.asm.ClassVisitor;
import org.ow2.util.asm.MethodVisitor;
import org.ow2.util.asm.Type;

public class RedirectAdapter extends ClassAdapter{
   private static int returnOp(Type returnType){
      if(LONG_TYPE.equals(returnType)){
         return LRETURN;
      }
      return ARETURN;
   }
   
   private static int nonNative(int access){
      return access & ~ACC_NATIVE;
   }
   
   private static int invokeOp(int access){
      return (access & ACC_STATIC) != 0 ? INVOKESTATIC : INVOKEVIRTUAL;
   }
   
   private static int makePrivate(int access){
      return access & ~ACC_PUBLIC & ~ACC_PROTECTED;
   }

   private static boolean hasMethod(Class<?> type, String name, String desc){
      if(type == null || name == null || desc == null){
         return false;
      }
      for(Method method: type.getDeclaredMethods()){
         if(method.getName().equals(name) && getMethodDescriptor(method).equals(desc)){
            return true;
         }
      }
      return hasMethod(type.getSuperclass(), name, desc);
   }

   private final String className;
   private final Class<?> advisor;
   private List<MethodRedirect> redirects = new LinkedList<MethodRedirect>();
   private final String prefix;

   public RedirectAdapter(ClassVisitor visitor, String prefix, String className, Class<?> advisor){
      super(visitor);
      this.prefix = prefix;
      this.className = className;
      this.advisor = advisor;
   }

   @Override
   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
      Type returnType = getReturnType(desc);
      String methodDesc = getMethodDescriptor(returnType, new Type[]{returnType});
      if(hasMethod(advisor, name, methodDesc)){
         redirects.add(new MethodRedirect(access,name,desc,signature,exceptions, methodDesc, returnType));
         return super.visitMethod(makePrivate(access), prefix+name, desc, signature, exceptions);
      }
      return super.visitMethod(access, name, desc, signature, exceptions);
   }

   @Override
   public void visitEnd(){
      for(MethodRedirect redirect: redirects){
         MethodVisitor mv = super.visitMethod(nonNative(redirect.access), redirect.name, redirect.desc, redirect.signature, redirect.exceptions);
         mv.visitCode();
         //push args
         mv.visitMethodInsn(invokeOp(redirect.access), className, prefix+redirect.name, redirect.desc);
         mv.visitMethodInsn(invokeOp(redirect.access), advisor.getName().replace('.', '/'), redirect.name, redirect.advisorDesc);
         mv.visitInsn(returnOp(redirect.returnType));
      }
      super.visitEnd();
   }
}