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

import java.util.ServiceLoader;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class BespectAdapter extends ClassAdapter{
   private final String className;
   private final Class<?> advice;
   private final String prefix;
   private final ServiceLoader<MethodAdvisor> advisors;
   
   public BespectAdapter(ClassVisitor visitor, String prefix, String className, Class<?> advice){
      super(visitor);
      this.prefix = prefix;
      this.className = className;
      this.advice = advice;
      advisors = ServiceLoader.load(MethodAdvisor.class);
   }

   @Override
   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
      MethodDef target = new MethodInfo(access, className, name, desc, signature, exceptions);
      Refactoring newMethod = getRefactoring(target);
      if(newMethod != null){
         for(MethodAdvice m: newMethod.helpers()){
            MethodVisitor mv = super.visitMethod(m.access(), m.name(), m.desc(), m.signature(), m.exceptions());
            m.build(mv);
         }
         return super.visitMethod(newMethod.access(), newMethod.name(), newMethod.desc(), newMethod.signature(), newMethod.exceptions());
      }
      return super.visitMethod(access, name, desc, signature, exceptions);
   }
   
   private Refactoring getRefactoring(MethodDef target){
      for(MethodAdvisor advisor: advisors){
         Refactoring weaver = advisor.advise(advice, prefix, target);
         if(weaver != null){
            return weaver;
         }
      }
      return null;
   }
}