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

import org.objectweb.asm.Type;

class MethodRedirect{

   final int access;
   final String name;
   final String desc;
   final String signature;
   final String[] exceptions;
   final String advisorDesc;
   final Type returnType;

   public MethodRedirect(int access, String name, String desc, String signature, String[] exceptions, String advisorDesc, Type returnType){
      this.access = access;
      this.name = name;
      this.desc = desc;
      this.signature = signature;
      this.exceptions = exceptions;
      this.advisorDesc = advisorDesc;
      this.returnType = returnType;
   }
   
}