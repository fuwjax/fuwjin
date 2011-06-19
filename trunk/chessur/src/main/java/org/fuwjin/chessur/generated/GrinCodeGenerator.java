/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.chessur.generated;

import java.io.IOException;
import java.util.Map;

public class GrinCodeGenerator {
  static final Object UNSET = new Object() {
    public String toString() {
      return "UNSET";
    }
  };

  public static class GrinCodeGeneratorException extends Exception {
    private static final long serialVersionUID = 1; 
    GrinCodeGeneratorException(final String message) {
      super(message);
    }
    
    GrinCodeGeneratorException(final String message, final Throwable cause) {
      super(message, cause);
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
      return this;
    }
  }
  
  static class StringCursor {
    private int pos;
    private int line;
    private int column;
    private final CharSequence seq;
    private final int start;
    private final StringCursor parent;
    private final Appendable appender;
    
    public StringCursor(final CharSequence seq, final Appendable appender) {
         start = 0;
         pos = 0;
         this.seq = seq;
         parent = null;
         line = 1;
         column = 0;
         this.appender = appender;
    }
    
    public StringCursor(final int start, final int line, final int column, final CharSequence seq, final StringCursor parent) {
      this.start = start;
      pos = start;
      this.seq = seq;
      this.parent = parent;
      this.line = line;
      this.column = column;
      this.appender = new StringBuilder();
    }
    
    public int accept() throws GrinCodeGeneratorException {
      checkBounds(pos);
      return advance();
    }
    
    public int accept(final String expected) throws GrinCodeGeneratorException {
      if(expected == null || expected.length() == 0) {
        throw ex("UNSET");
      }
      checkBounds(pos + expected.length() - 1);
      final CharSequence sub = seq.subSequence(pos, pos + expected.length());
      if(!sub.equals(expected)) {
        throw ex("failed while matching "+expected);
      }
      final int stop = pos + expected.length() - 1;
      while(pos < stop) {
        advance();
      }
      return advance();
    }
    
    public int acceptIn(final String name, final String set) throws GrinCodeGeneratorException {
      checkBounds(pos);
      if(set.indexOf(seq.charAt(pos)) < 0) {
        throw ex("Did not match filter: "+name);
      }
      return advance();
    }
    
    public int acceptNot(final String expected) throws GrinCodeGeneratorException {
      if(expected == null || expected.length() == 0) {
        throw ex("UNSET");
      }
      if(pos + expected.length() - 1 >= seq.length()) {
        return accept();
      }       
      if(seq.subSequence(pos, pos + expected.length()).equals(expected)) {
        throw ex("failed while matching "+expected);
      }
      return advance();
    }
    
    public int acceptNotIn(final String name, final String set) throws GrinCodeGeneratorException {
      checkBounds(pos);
      if(set.indexOf(seq.charAt(pos)) >= 0) {
        throw ex("Unexpected match: "+name);
      }
      return advance();
    }
    
    public void publish(final Object value) throws GrinCodeGeneratorException {
      try {
        appender.append(value.toString());
      } catch(IOException e) {
        throw ex(e);
      }
    }
    
    public Object isSet(final String name, final Object value) throws GrinCodeGeneratorException {
      if(UNSET.equals(value)) {
        throw ex("variable "+name+" is unset");
      }
      return value;
    }
    
    protected void checkBounds(final int p) throws GrinCodeGeneratorException {
      if(p >= seq.length()) {
        throw ex("unexpected EOF");
      }
    }
    
    public void commit() {
      commitInput();
      commitOutput();
    }
    
    void commitInput() {
      parent.pos = pos;
      parent.line = line;
      parent.column = column;
    }
    
    void commitOutput() {
      appendTo(parent.appender);
    }
    
    void appendTo(final Appendable dest) {
      try {
        dest.append(appender.toString());
      } catch(final IOException e) {
        throw new RuntimeException("IOException never thrown by StringBuilder", e);
      }
    }
    
    public GrinCodeGeneratorException ex(final String message) {
      return new GrinCodeGeneratorException(message + context());
    }
    
    public String context() {
      if(pos == 0) {
        return ": [" + line + "," + column + "] SOF -> [1,0] SOF";
      }
      if(pos > seq.length()) {
        return ": [" + line + "," + column + "] EOF -> [1,0] SOF";
      }
      return ": [" + line + "," + column + "] '"+ seq.charAt(pos - 1)+"' -> [1,0] SOF";
    }
    
    public GrinCodeGeneratorException ex(final Throwable cause) {
      return new GrinCodeGeneratorException(context(), cause);
    }
    
    public void abort(final Object message) {
      throw new RuntimeException(message + context());
    }
      
    public void abort(final Object message, final Throwable cause) {
      throw new RuntimeException(message + context(), cause);
    }
    
    private int advance() {
      final char ch = seq.charAt(pos++);
       if(ch == '\n') {
         line++;
         column = 0;
       } else {
         column++;
       }
       return ch;
    }
    
    public int next() throws GrinCodeGeneratorException {
      checkBounds(pos);
      return seq.charAt(pos);
    }
    
    public String nextStr() throws GrinCodeGeneratorException {
      checkBounds(pos);
      return seq.subSequence(pos,pos+1).toString();
    }
    
    public StringCursor sub() {
      return new StringCursor(pos, line, column, seq, this);
    }
    
    public StringCursor subOutput(final StringBuilder newOutput) {
      return new StringCursor(pos, line, column, seq, this) {
        public void commit() {
          commitInput();
          appendTo(newOutput);
        }
      };
    }
    
    public StringCursor subInput(final CharSequence newInput) {
      return new StringCursor(0, 1, 0, newInput, this) {
        public void commit() {
          commitOutput();
        }
      };
    }
    
    public String match() {
      return seq.subSequence(start, pos).toString();
    }
  }

  static Object Catalog(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub1 = input.sub();
    Preamble(sub1, env);
    try {
      env[0] /*varIndex*/= new java.util.concurrent.atomic.AtomicInteger();
    } catch(final Exception e2) {
      throw sub1.ex(e2);
    }
    try {
      env[1] /*indexer*/= new org.fuwjin.util.NameIndex();
    } catch(final Exception e3) {
      throw sub1.ex(e3);
    }
    try {
      env[2] /*specs*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.CatalogImpl)sub1.isSet("cat", env[3])).scripts()).iterator();
    } catch(final Exception e4) {
      throw sub1.ex(e4);
    }
    ScriptDeclaration(sub1, env);
    try {
      while(true) {
        ScriptDeclaration(sub1, env);
      }
    } catch(final GrinCodeGeneratorException e5) {
      //continue
    }
    final StringCursor sub6 = sub1.sub();
    boolean b7 = true;
    try {
      if((Object)((java.util.Iterator)sub6.isSet("specs", env[2])).hasNext() == Boolean.FALSE) {
        b7 = false;
      }
    } catch(final GrinCodeGeneratorException e8) {
      b7 = false;
    }
    if(b7){
      throw sub1.ex("unexpected value");
    }
    Postscript(sub1, env);
    sub1.commit();
    return null;
  }

  static Object Module(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub9 = input.sub();
    ModulePreamble(sub9, env);
    try {
      env[0] /*varIndex*/= new java.util.concurrent.atomic.AtomicInteger();
    } catch(final Exception e10) {
      throw sub9.ex(e10);
    }
    try {
      env[1] /*indexer*/= new org.fuwjin.util.NameIndex();
    } catch(final Exception e11) {
      throw sub9.ex(e11);
    }
    try {
      env[2] /*specs*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.CatalogImpl)sub9.isSet("cat", env[3])).scripts()).iterator();
    } catch(final Exception e12) {
      throw sub9.ex(e12);
    }
    ScriptDeclaration(sub9, env);
    try {
      while(true) {
        ScriptDeclaration(sub9, env);
      }
    } catch(final GrinCodeGeneratorException e13) {
      //continue
    }
    final StringCursor sub14 = sub9.sub();
    boolean b15 = true;
    try {
      if((Object)((java.util.Iterator)sub14.isSet("specs", env[2])).hasNext() == Boolean.FALSE) {
        b15 = false;
      }
    } catch(final GrinCodeGeneratorException e16) {
      b15 = false;
    }
    if(b15){
      throw sub9.ex("unexpected value");
    }
    Postscript(sub9, env);
    sub9.commit();
    return null;
  }

  static Object Postscript(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub17 = input.sub();
    sub17.publish(String.valueOf("\n  \n  public static Object interpret(final CharSequence in, final Appendable out, final Map<String, ?> environment) throws ") + String.valueOf(sub17.isSet("className", env[4])) + String.valueOf("Exception {\n    final StringCursor input = new StringCursor(in, out);"));
    try {
      env[5] /*size*/= ((org.fuwjin.util.NameIndex)sub17.isSet("indexer", env[1])).size();
    } catch(final Exception e18) {
      throw sub17.ex(e18);
    }
    sub17.publish(String.valueOf("\n    final Object[] env = new Object[") + String.valueOf(sub17.isSet("size", env[5])) + String.valueOf("];"));
    try {
      env[6] /*entries*/= ((java.lang.Iterable)((org.fuwjin.util.NameIndex)sub17.isSet("indexer", env[1])).entries()).iterator();
    } catch(final Exception e19) {
      throw sub17.ex(e19);
    }
    try {
      final StringCursor sub20 = sub17.sub();
      try {
        env[7] /*entry*/= ((java.util.Iterator)sub20.isSet("entries", env[6])).next();
      } catch(final Exception e21) {
        throw sub20.ex(e21);
      }
      try {
        env[8] /*name*/= ((java.util.Map.Entry)sub20.isSet("entry", env[7])).getKey();
      } catch(final Exception e22) {
        throw sub20.ex(e22);
      }
      try {
        env[9] /*index*/= ((java.util.Map.Entry)sub20.isSet("entry", env[7])).getValue();
      } catch(final Exception e23) {
        throw sub20.ex(e23);
      }
      sub20.publish(String.valueOf("\n    env[") + String.valueOf(sub20.isSet("index", env[9])) + String.valueOf("] = environment.containsKey(\"") + String.valueOf(sub20.isSet("name", env[8])) + String.valueOf("\") ? environment.get(\"") + String.valueOf(sub20.isSet("name", env[8])) + String.valueOf("\") : UNSET;"));
      sub20.commit();
      try {
        while(true) {
          final StringCursor sub24 = sub17.sub();
          try {
            env[7] /*entry*/= ((java.util.Iterator)sub24.isSet("entries", env[6])).next();
          } catch(final Exception e25) {
            throw sub24.ex(e25);
          }
          try {
            env[8] /*name*/= ((java.util.Map.Entry)sub24.isSet("entry", env[7])).getKey();
          } catch(final Exception e26) {
            throw sub24.ex(e26);
          }
          try {
            env[9] /*index*/= ((java.util.Map.Entry)sub24.isSet("entry", env[7])).getValue();
          } catch(final Exception e27) {
            throw sub24.ex(e27);
          }
          sub24.publish(String.valueOf("\n    env[") + String.valueOf(sub24.isSet("index", env[9])) + String.valueOf("] = environment.containsKey(\"") + String.valueOf(sub24.isSet("name", env[8])) + String.valueOf("\") ? environment.get(\"") + String.valueOf(sub24.isSet("name", env[8])) + String.valueOf("\") : UNSET;"));
          sub24.commit();
        }
      } catch(final GrinCodeGeneratorException e28) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e29) {
      //continue
    }
    final StringCursor sub30 = sub17.sub();
    boolean b31 = true;
    try {
      if((Object)((java.util.Iterator)sub30.isSet("entries", env[6])).hasNext() == Boolean.FALSE) {
        b31 = false;
      }
    } catch(final GrinCodeGeneratorException e32) {
      b31 = false;
    }
    if(b31){
      throw sub17.ex("unexpected value");
    }
    try {
      env[10] /*rootName*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub17.isSet("cat", env[3])).rootName();
    } catch(final Exception e33) {
      throw sub17.ex(e33);
    }
    sub17.publish(String.valueOf("\n    return ") + String.valueOf(sub17.isSet("rootName", env[10])) + String.valueOf("(input, env);\n  }\n}\n"));
    sub17.commit();
    return null;
  }

  static Object Preamble(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub34 = input.sub();
    sub34.publish(String.valueOf("/*******************************************************************************\n * Copyright (c) 2011 Michael Doberenz.\n * All rights reserved. This program and the accompanying materials\n * are made available under the terms of the Eclipse Public License v1.0\n * which accompanies this distribution, and is available at\n * http://www.eclipse.org/legal/epl-v10.html\n * \n * Contributors:\n *     Michael Doberenz - initial API and implementation\n ******************************************************************************/\npackage ") + String.valueOf(sub34.isSet("package", env[11])) + String.valueOf(";\n\nimport java.io.IOException;\nimport java.util.Map;\n\npublic class ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf(" {\n  static final Object UNSET = new Object() {\n    public String toString() {\n      return \"UNSET\";\n    }\n  };\n\n  public static class ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception extends Exception {\n    private static final long serialVersionUID = 1; \n    ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception(final String message) {\n      super(message);\n    }\n    \n    ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception(final String message, final Throwable cause) {\n      super(message, cause);\n    }\n    \n    @Override\n    public synchronized Throwable fillInStackTrace() {\n      return this;\n    }\n  }\n  \n  static class StringCursor {\n    private int pos;\n    private int line;\n    private int column;\n    private final CharSequence seq;\n    private final int start;\n    private final StringCursor parent;\n    private final Appendable appender;\n    \n    public StringCursor(final CharSequence seq, final Appendable appender) {\n         start = 0;\n         pos = 0;\n         this.seq = seq;\n         parent = null;\n         line = 1;\n         column = 0;\n         this.appender = appender;\n    }\n    \n    public StringCursor(final int start, final int line, final int column, final CharSequence seq, final StringCursor parent) {\n      this.start = start;\n      pos = start;\n      this.seq = seq;\n      this.parent = parent;\n      this.line = line;\n      this.column = column;\n      this.appender = new StringBuilder();\n    }\n    \n    public int accept() throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      checkBounds(pos);\n      return advance();\n    }\n    \n    public int accept(final String expected) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      if(expected == null || expected.length() == 0) {\n        throw ex(\"UNSET\");\n      }\n      checkBounds(pos + expected.length() - 1);\n      final CharSequence sub = seq.subSequence(pos, pos + expected.length());\n      if(!sub.equals(expected)) {\n        throw ex(\"failed while matching \"+expected);\n      }\n      final int stop = pos + expected.length() - 1;\n      while(pos < stop) {\n        advance();\n      }\n      return advance();\n    }\n    \n    public int acceptIn(final String name, final String set) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      checkBounds(pos);\n      if(set.indexOf(seq.charAt(pos)) < 0) {\n        throw ex(\"Did not match filter: \"+name);\n      }\n      return advance();\n    }\n    \n    public int acceptNot(final String expected) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      if(expected == null || expected.length() == 0) {\n        throw ex(\"UNSET\");\n      }\n      if(pos + expected.length() - 1 >= seq.length()) {\n        return accept();\n      }       \n      if(seq.subSequence(pos, pos + expected.length()).equals(expected)) {\n        throw ex(\"failed while matching \"+expected);\n      }\n      return advance();\n    }\n    \n    public int acceptNotIn(final String name, final String set) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      checkBounds(pos);\n      if(set.indexOf(seq.charAt(pos)) >= 0) {\n        throw ex(\"Unexpected match: \"+name);\n      }\n      return advance();\n    }\n    \n    public void publish(final Object value) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      try {\n        appender.append(value.toString());\n      } catch(IOException e) {\n        throw ex(e);\n      }\n    }\n    \n    public Object isSet(final String name, final Object value) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      if(UNSET.equals(value)) {\n        throw ex(\"variable \"+name+\" is unset\");\n      }\n      return value;\n    }\n    \n    protected void checkBounds(final int p) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      if(p >= seq.length()) {\n        throw ex(\"unexpected EOF\");\n      }\n    }\n    \n    public void commit() {\n      commitInput();\n      commitOutput();\n    }\n    \n    void commitInput() {\n      parent.pos = pos;\n      parent.line = line;\n      parent.column = column;\n    }\n    \n    void commitOutput() {\n      appendTo(parent.appender);\n    }\n    \n    void appendTo(final Appendable dest) {\n      try {\n        dest.append(appender.toString());\n      } catch(final IOException e) {\n        throw new RuntimeException(\"IOException never thrown by StringBuilder\", e);\n      }\n    }\n    \n    public ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception ex(final String message) {\n      return new ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception(message + context());\n    }\n    \n    public String context() {\n      if(pos == 0) {\n        return \": [\" + line + \",\" + column + \"] SOF -> [1,0] SOF\";\n      }\n      if(pos > seq.length()) {\n        return \": [\" + line + \",\" + column + \"] EOF -> [1,0] SOF\";\n      }\n      return \": [\" + line + \",\" + column + \"] '\"+ seq.charAt(pos - 1)+\"' -> [1,0] SOF\";\n    }\n    \n    public ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception ex(final Throwable cause) {\n      return new ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception(context(), cause);\n    }\n    \n    public void abort(final Object message) {\n      throw new RuntimeException(message + context());\n    }\n      \n    public void abort(final Object message, final Throwable cause) {\n      throw new RuntimeException(message + context(), cause);\n    }\n    \n    private int advance() {\n      final char ch = seq.charAt(pos++);\n       if(ch == '\\n') {\n         line++;\n         column = 0;\n       } else {\n         column++;\n       }\n       return ch;\n    }\n    \n    public int next() throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      checkBounds(pos);\n      return seq.charAt(pos);\n    }\n    \n    public String nextStr() throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      checkBounds(pos);\n      return seq.subSequence(pos,pos+1).toString();\n    }\n    \n    public StringCursor sub() {\n      return new StringCursor(pos, line, column, seq, this);\n    }\n    \n    public StringCursor subOutput(final StringBuilder newOutput) {\n      return new StringCursor(pos, line, column, seq, this) {\n        public void commit() {\n          commitInput();\n          appendTo(newOutput);\n        }\n      };\n    }\n    \n    public StringCursor subInput(final CharSequence newInput) {\n      return new StringCursor(0, 1, 0, newInput, this) {\n        public void commit() {\n          commitOutput();\n        }\n      };\n    }\n    \n    public String match() {\n      return seq.subSequence(start, pos).toString();\n    }\n  }"));
    sub34.commit();
    return null;
  }

  static Object ModulePreamble(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub35 = input.sub();
    sub35.publish(String.valueOf("/*******************************************************************************\n * Copyright (c) 2011 Michael Doberenz.\n * All rights reserved. This program and the accompanying materials\n * are made available under the terms of the Eclipse Public License v1.0\n * which accompanies this distribution, and is available at\n * http://www.eclipse.org/legal/epl-v10.html\n * \n * Contributors:\n *     Michael Doberenz - initial API and implementation\n ******************************************************************************/\npackage ") + String.valueOf(sub35.isSet("package", env[11])) + String.valueOf(";\n\nimport java.io.IOException;\nimport java.util.Map;\nimport static ") + String.valueOf(sub35.isSet("package", env[11])) + String.valueOf(".") + String.valueOf(sub35.isSet("className", env[4])) + String.valueOf(".UNSET;\nimport ") + String.valueOf(sub35.isSet("package", env[11])) + String.valueOf(".") + String.valueOf(sub35.isSet("className", env[4])) + String.valueOf(".") + String.valueOf(sub35.isSet("className", env[4])) + String.valueOf("Exception;\nimport ") + String.valueOf(sub35.isSet("package", env[11])) + String.valueOf(".") + String.valueOf(sub35.isSet("className", env[4])) + String.valueOf(".StringCursor;\n\npublic class ") + String.valueOf(sub35.isSet("moduleName", env[12])) + String.valueOf(" {\n"));
    sub35.commit();
    return null;
  }

  static Object ScriptDeclaration(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub36 = input.sub();
    try {
      env[13] /*spec*/= ((org.fuwjin.chessur.expression.ScriptImpl)((java.util.Iterator)sub36.isSet("specs", env[2])).next()).declaration();
    } catch(final Exception e37) {
      throw sub36.ex(e37);
    }
    try {
      env[8] /*name*/= ((org.fuwjin.chessur.expression.Declaration)sub36.isSet("spec", env[13])).name();
    } catch(final Exception e38) {
      throw sub36.ex(e38);
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.Declaration)sub36.isSet("spec", env[13])).returns();
    } catch(final Exception e39) {
      throw sub36.ex(e39);
    }
    try {
      env[15] /*indent*/= new org.fuwjin.util.Indent();
    } catch(final Exception e40) {
      throw sub36.ex(e40);
    }
    sub36.publish(String.valueOf("\n") + String.valueOf(sub36.isSet("indent", env[15])) + String.valueOf("static Object ") + String.valueOf(sub36.isSet("name", env[8])) + String.valueOf("(final StringCursor input, final Object... parentEnv) throws ") + String.valueOf(sub36.isSet("className", env[4])) + String.valueOf("Exception {\n    final Object[] env = new Object[parentEnv.length];\n    System.arraycopy(parentEnv, 0, env, 0, env.length);"));
    try {
      ((org.fuwjin.util.Indent)sub36.isSet("indent", env[15])).increase();
    } catch(final Exception e41) {
      throw sub36.ex(e41);
    }
    try {
      env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub36.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e42) {
      throw sub36.ex(e42);
    }
    env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub36.isSet("subIndex", env[16]));
    sub36.publish(String.valueOf(sub36.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub36.isSet("input", env[17])) + String.valueOf(" = input.sub();"));
    try {
      env[18] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Declaration)sub36.isSet("spec", env[13])).statements()).iterator();
    } catch(final Exception e43) {
      throw sub36.ex(e43);
    }
    try {
      final StringCursor sub44 = sub36.sub();
      try {
        env[19] /*statement*/= ((java.util.Iterator)sub44.isSet("statements", env[18])).next();
      } catch(final Exception e45) {
        throw sub44.ex(e45);
      }
      Statement(sub44, env);
      sub44.commit();
      try {
        while(true) {
          final StringCursor sub46 = sub36.sub();
          try {
            env[19] /*statement*/= ((java.util.Iterator)sub46.isSet("statements", env[18])).next();
          } catch(final Exception e47) {
            throw sub46.ex(e47);
          }
          Statement(sub46, env);
          sub46.commit();
        }
      } catch(final GrinCodeGeneratorException e48) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e49) {
      //continue
    }
    sub36.publish(String.valueOf(sub36.isSet("indent", env[15])) + String.valueOf(sub36.isSet("input", env[17])) + String.valueOf(".commit();"));
    final StringCursor sub50 = sub36.sub();
    boolean b51 = true;
    try {
      if((Object)((java.util.Iterator)sub50.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        b51 = false;
      }
    } catch(final GrinCodeGeneratorException e52) {
      b51 = false;
    }
    if(b51){
      throw sub36.ex("unexpected value");
    }
    try {
      final StringCursor sub53 = sub36.sub();
      final StringCursor sub54 = sub53.sub();
      if((Object)sub54.isSet("value", env[14]) == Boolean.FALSE) {
        throw sub54.ex("check failed");
      }
      sub53.publish(String.valueOf(sub53.isSet("indent", env[15])) + String.valueOf("return ") + String.valueOf(Value(sub53, env)) + String.valueOf(";"));
      sub53.commit();
    } catch(final GrinCodeGeneratorException e55) {
      final StringCursor sub56 = sub36.sub();
      sub56.publish(String.valueOf(sub56.isSet("indent", env[15])) + String.valueOf("return null;"));
      sub56.commit();
    }
    try {
      ((org.fuwjin.util.Indent)sub36.isSet("indent", env[15])).decrease();
    } catch(final Exception e57) {
      throw sub36.ex(e57);
    }
    sub36.publish(String.valueOf(sub36.isSet("indent", env[15])) + String.valueOf("}"));
    sub36.commit();
    return null;
  }

  static Object Value(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub58 = input.sub();
    env[19] /*statement*/= sub58.isSet("value", env[14]);
    try {
      env[20] /*result*/= Script(sub58, env);
    } catch(final GrinCodeGeneratorException e59) {
      try {
        env[20] /*result*/= StaticLiteral(sub58, env);
      } catch(final GrinCodeGeneratorException e60) {
        try {
          env[20] /*result*/= DynamicLiteral(sub58, env);
        } catch(final GrinCodeGeneratorException e61) {
          try {
            env[20] /*result*/= AcceptValue(sub58, env);
          } catch(final GrinCodeGeneratorException e62) {
            try {
              env[20] /*result*/= NextValue(sub58, env);
            } catch(final GrinCodeGeneratorException e63) {
              try {
                env[20] /*result*/= MatchValue(sub58, env);
              } catch(final GrinCodeGeneratorException e64) {
                try {
                  final StringBuilder builder65 = new StringBuilder();
                  final StringCursor sub66 = sub58.subOutput(builder65);
                  Object(sub66, env);
                  sub66.commit();
                  env[20] /*result*/= builder65.toString();
                } catch(final GrinCodeGeneratorException e67) {
                  try {
                    env[20] /*result*/= Number(sub58, env);
                  } catch(final GrinCodeGeneratorException e68) {
                    try {
                      env[20] /*result*/= Invocation(sub58, env);
                    } catch(final GrinCodeGeneratorException e69) {
                      env[20] /*result*/= Variable(sub58, env);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    sub58.commit();
    return sub58.isSet("result", env[20]);
  }

  static Object Statement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub70 = input.sub();
    try {
      AssumeStatement(sub70, env);
    } catch(final GrinCodeGeneratorException e71) {
      try {
        EitherOrStatement(sub70, env);
      } catch(final GrinCodeGeneratorException e72) {
        try {
          ConditionalAbortStatement(sub70, env);
        } catch(final GrinCodeGeneratorException e73) {
          try {
            CouldStatement(sub70, env);
          } catch(final GrinCodeGeneratorException e74) {
            try {
              RepeatStatement(sub70, env);
            } catch(final GrinCodeGeneratorException e75) {
              try {
                AcceptStatement(sub70, env);
              } catch(final GrinCodeGeneratorException e76) {
                try {
                  PublishStatement(sub70, env);
                } catch(final GrinCodeGeneratorException e77) {
                  try {
                    AbortStatement(sub70, env);
                  } catch(final GrinCodeGeneratorException e78) {
                    try {
                      ScriptStatement(sub70, env);
                    } catch(final GrinCodeGeneratorException e79) {
                      try {
                        Block(sub70, env);
                      } catch(final GrinCodeGeneratorException e80) {
                        try {
                          Assignment(sub70, env);
                        } catch(final GrinCodeGeneratorException e81) {
                          InvocationStatement(sub70, env);
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    sub70.commit();
    return null;
  }

  static Object AssumeStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub82 = input.sub();
    final StringCursor sub83 = sub82.sub();
    if((Object)sub83.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.AssumeStatement == Boolean.FALSE) {
      throw sub83.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.AssumeStatement)sub82.isSet("statement", env[19])).value();
    } catch(final Exception e84) {
      throw sub82.ex(e84);
    }
    try {
      env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub82.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e85) {
      throw sub82.ex(e85);
    }
    env[21] /*oldInput*/= sub82.isSet("input", env[17]);
    env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub82.isSet("subIndex", env[16]));
    sub82.publish(String.valueOf(sub82.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub82.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub82.isSet("oldInput", env[21])) + String.valueOf(".sub();"));
    try {
      final StringCursor sub86 = sub82.sub();
      final StringCursor sub87 = sub86.sub();
      if((Object)((org.fuwjin.chessur.expression.AssumeStatement)sub87.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub87.ex("check failed");
      }
      try {
        env[22] /*bIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub86.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e88) {
        throw sub86.ex(e88);
      }
      sub86.publish(String.valueOf(sub86.isSet("indent", env[15])) + String.valueOf("boolean b") + String.valueOf(sub86.isSet("bIndex", env[22])) + String.valueOf(" = true;"));
      sub86.publish(String.valueOf(sub86.isSet("indent", env[15])) + String.valueOf("try {"));
      try {
        ((org.fuwjin.util.Indent)sub86.isSet("indent", env[15])).increase();
      } catch(final Exception e89) {
        throw sub86.ex(e89);
      }
      sub86.publish(String.valueOf(sub86.isSet("indent", env[15])) + String.valueOf("if((Object)") + String.valueOf(Value(sub86, env)) + String.valueOf(" == Boolean.FALSE) {"));
      sub86.publish(String.valueOf(sub86.isSet("indent", env[15])) + String.valueOf("  b") + String.valueOf(sub86.isSet("bIndex", env[22])) + String.valueOf(" = false;"));
      sub86.publish(String.valueOf(sub86.isSet("indent", env[15])) + String.valueOf("}"));
      try {
        ((org.fuwjin.util.Indent)sub86.isSet("indent", env[15])).decrease();
      } catch(final Exception e90) {
        throw sub86.ex(e90);
      }
      try {
        env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub86.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e91) {
        throw sub86.ex(e91);
      }
      env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub86.isSet("exIndex", env[23]));
      sub86.publish(String.valueOf(sub86.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub86.isSet("className", env[4])) + String.valueOf("Exception ") + String.valueOf(sub86.isSet("exception", env[24])) + String.valueOf(") {"));
      sub86.publish(String.valueOf(sub86.isSet("indent", env[15])) + String.valueOf("  b") + String.valueOf(sub86.isSet("bIndex", env[22])) + String.valueOf(" = false;"));
      sub86.publish(String.valueOf(sub86.isSet("indent", env[15])) + String.valueOf("}"));
      sub86.publish(String.valueOf(sub86.isSet("indent", env[15])) + String.valueOf("if(b") + String.valueOf(sub86.isSet("bIndex", env[22])) + String.valueOf("){"));
      sub86.publish(String.valueOf(sub86.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub86.isSet("oldInput", env[21])) + String.valueOf(".ex(\"unexpected value\");"));
      sub86.publish(String.valueOf(sub86.isSet("indent", env[15])) + String.valueOf("}"));
      sub86.commit();
    } catch(final GrinCodeGeneratorException e92) {
      final StringCursor sub93 = sub82.sub();
      sub93.publish(String.valueOf(sub93.isSet("indent", env[15])) + String.valueOf("if((Object)") + String.valueOf(Value(sub93, env)) + String.valueOf(" == Boolean.FALSE) {"));
      sub93.publish(String.valueOf(sub93.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub93.isSet("input", env[17])) + String.valueOf(".ex(\"check failed\");"));
      sub93.publish(String.valueOf(sub93.isSet("indent", env[15])) + String.valueOf("}"));
      sub93.commit();
    }
    sub82.commit();
    return null;
  }

  static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub94 = input.sub();
    final StringCursor sub95 = sub94.sub();
    if((Object)sub95.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.EitherOrStatement == Boolean.FALSE) {
      throw sub95.ex("check failed");
    }
    env[25] /*stmt*/= sub94.isSet("statement", env[19]);
    try {
      env[18] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.EitherOrStatement)sub94.isSet("stmt", env[25])).statements()).iterator();
    } catch(final Exception e96) {
      throw sub94.ex(e96);
    }
    sub94.publish(String.valueOf(sub94.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub94.isSet("indent", env[15])).increase();
    } catch(final Exception e97) {
      throw sub94.ex(e97);
    }
    try {
      env[19] /*statement*/= ((java.util.Iterator)sub94.isSet("statements", env[18])).next();
    } catch(final Exception e98) {
      throw sub94.ex(e98);
    }
    Statement(sub94, env);
    try {
      ((org.fuwjin.util.Indent)sub94.isSet("indent", env[15])).decrease();
    } catch(final Exception e99) {
      throw sub94.ex(e99);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub94.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e100) {
      throw sub94.ex(e100);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub94.isSet("exIndex", env[23]));
    sub94.publish(String.valueOf(sub94.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub94.isSet("className", env[4])) + String.valueOf("Exception ") + String.valueOf(sub94.isSet("exception", env[24])) + String.valueOf(") {"));
    try {
      ((org.fuwjin.util.Indent)sub94.isSet("indent", env[15])).increase();
    } catch(final Exception e101) {
      throw sub94.ex(e101);
    }
    OrStatement(sub94, env);
    final StringCursor sub102 = sub94.sub();
    boolean b103 = true;
    try {
      if((Object)((java.util.Iterator)sub102.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        b103 = false;
      }
    } catch(final GrinCodeGeneratorException e104) {
      b103 = false;
    }
    if(b103){
      throw sub94.ex("unexpected value");
    }
    try {
      ((org.fuwjin.util.Indent)sub94.isSet("indent", env[15])).decrease();
    } catch(final Exception e105) {
      throw sub94.ex(e105);
    }
    sub94.publish(String.valueOf(sub94.isSet("indent", env[15])) + String.valueOf("}"));
    sub94.commit();
    return null;
  }

  static Object OrStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub106 = input.sub();
    try {
      env[19] /*statement*/= ((java.util.Iterator)sub106.isSet("statements", env[18])).next();
    } catch(final Exception e107) {
      throw sub106.ex(e107);
    }
    try {
      final StringCursor sub108 = sub106.sub();
      final StringCursor sub109 = sub108.sub();
      if((Object)((java.util.Iterator)sub109.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        throw sub109.ex("check failed");
      }
      sub108.publish(String.valueOf(sub108.isSet("indent", env[15])) + String.valueOf("try {"));
      try {
        ((org.fuwjin.util.Indent)sub108.isSet("indent", env[15])).increase();
      } catch(final Exception e110) {
        throw sub108.ex(e110);
      }
      Statement(sub108, env);
      try {
        ((org.fuwjin.util.Indent)sub108.isSet("indent", env[15])).decrease();
      } catch(final Exception e111) {
        throw sub108.ex(e111);
      }
      try {
        env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub108.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e112) {
        throw sub108.ex(e112);
      }
      env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub108.isSet("exIndex", env[23]));
      sub108.publish(String.valueOf(sub108.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub108.isSet("className", env[4])) + String.valueOf("Exception ") + String.valueOf(sub108.isSet("exception", env[24])) + String.valueOf(") {"));
      try {
        ((org.fuwjin.util.Indent)sub108.isSet("indent", env[15])).increase();
      } catch(final Exception e113) {
        throw sub108.ex(e113);
      }
      OrStatement(sub108, env);
      try {
        ((org.fuwjin.util.Indent)sub108.isSet("indent", env[15])).decrease();
      } catch(final Exception e114) {
        throw sub108.ex(e114);
      }
      sub108.publish(String.valueOf(sub108.isSet("indent", env[15])) + String.valueOf("}"));
      sub108.commit();
    } catch(final GrinCodeGeneratorException e115) {
      final StringCursor sub116 = sub106.sub();
      Statement(sub116, env);
      sub116.commit();
    }
    sub106.commit();
    return null;
  }

  static Object ConditionalAbortStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub117 = input.sub();
    final StringCursor sub118 = sub117.sub();
    if((Object)sub118.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ConditionalAbortStatement == Boolean.FALSE) {
      throw sub118.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.ConditionalAbortStatement)sub117.isSet("statement", env[19])).value();
    } catch(final Exception e119) {
      throw sub117.ex(e119);
    }
    env[25] /*stmt*/= sub117.isSet("statement", env[19]);
    final StringCursor sub120 = sub117.sub();
    if((Object)sub120.isSet("stmt", env[25]) instanceof org.fuwjin.chessur.expression.EitherOrStatement == Boolean.FALSE) {
      throw sub120.ex("check failed");
    }
    try {
      env[18] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.EitherOrStatement)sub117.isSet("stmt", env[25])).statements()).iterator();
    } catch(final Exception e121) {
      throw sub117.ex(e121);
    }
    sub117.publish(String.valueOf(sub117.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub117.isSet("indent", env[15])).increase();
    } catch(final Exception e122) {
      throw sub117.ex(e122);
    }
    OrStatement(sub117, env);
    final StringCursor sub123 = sub117.sub();
    boolean b124 = true;
    try {
      if((Object)((java.util.Iterator)sub123.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        b124 = false;
      }
    } catch(final GrinCodeGeneratorException e125) {
      b124 = false;
    }
    if(b124){
      throw sub117.ex("unexpected value");
    }
    try {
      ((org.fuwjin.util.Indent)sub117.isSet("indent", env[15])).decrease();
    } catch(final Exception e126) {
      throw sub117.ex(e126);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub117.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e127) {
      throw sub117.ex(e127);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub117.isSet("exIndex", env[23]));
    sub117.publish(String.valueOf(sub117.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub117.isSet("className", env[4])) + String.valueOf("Exception ") + String.valueOf(sub117.isSet("exception", env[24])) + String.valueOf(") {"));
    try {
      ((org.fuwjin.util.Indent)sub117.isSet("indent", env[15])).increase();
    } catch(final Exception e128) {
      throw sub117.ex(e128);
    }
    sub117.publish(String.valueOf(sub117.isSet("indent", env[15])) + String.valueOf(sub117.isSet("input", env[17])) + String.valueOf(".abort(") + String.valueOf(Value(sub117, env)) + String.valueOf(", ") + String.valueOf(sub117.isSet("exception", env[24])) + String.valueOf(");"));
    try {
      ((org.fuwjin.util.Indent)sub117.isSet("indent", env[15])).decrease();
    } catch(final Exception e129) {
      throw sub117.ex(e129);
    }
    sub117.publish(String.valueOf(sub117.isSet("indent", env[15])) + String.valueOf("} catch(final RuntimeException ") + String.valueOf(sub117.isSet("exception", env[24])) + String.valueOf(") {"));
    try {
      ((org.fuwjin.util.Indent)sub117.isSet("indent", env[15])).increase();
    } catch(final Exception e130) {
      throw sub117.ex(e130);
    }
    sub117.publish(String.valueOf(sub117.isSet("indent", env[15])) + String.valueOf(sub117.isSet("input", env[17])) + String.valueOf(".abort(") + String.valueOf(Value(sub117, env)) + String.valueOf(", ") + String.valueOf(sub117.isSet("exception", env[24])) + String.valueOf(");"));
    try {
      ((org.fuwjin.util.Indent)sub117.isSet("indent", env[15])).decrease();
    } catch(final Exception e131) {
      throw sub117.ex(e131);
    }
    sub117.publish(String.valueOf(sub117.isSet("indent", env[15])) + String.valueOf("}"));
    sub117.commit();
    return null;
  }

  static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub132 = input.sub();
    final StringCursor sub133 = sub132.sub();
    if((Object)sub133.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.CouldStatement == Boolean.FALSE) {
      throw sub133.ex("check failed");
    }
    env[25] /*stmt*/= sub132.isSet("statement", env[19]);
    sub132.publish(String.valueOf(sub132.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub132.isSet("indent", env[15])).increase();
    } catch(final Exception e134) {
      throw sub132.ex(e134);
    }
    try {
      env[19] /*statement*/= ((org.fuwjin.chessur.expression.CouldStatement)sub132.isSet("stmt", env[25])).statement();
    } catch(final Exception e135) {
      throw sub132.ex(e135);
    }
    Statement(sub132, env);
    try {
      ((org.fuwjin.util.Indent)sub132.isSet("indent", env[15])).decrease();
    } catch(final Exception e136) {
      throw sub132.ex(e136);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub132.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e137) {
      throw sub132.ex(e137);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub132.isSet("exIndex", env[23]));
    sub132.publish(String.valueOf(sub132.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub132.isSet("className", env[4])) + String.valueOf("Exception ") + String.valueOf(sub132.isSet("exception", env[24])) + String.valueOf(") {"));
    sub132.publish(String.valueOf(sub132.isSet("indent", env[15])) + String.valueOf("  //continue"));
    sub132.publish(String.valueOf(sub132.isSet("indent", env[15])) + String.valueOf("}"));
    sub132.commit();
    return null;
  }

  static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub138 = input.sub();
    final StringCursor sub139 = sub138.sub();
    if((Object)sub139.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.RepeatStatement == Boolean.FALSE) {
      throw sub139.ex("check failed");
    }
    env[25] /*stmt*/= sub138.isSet("statement", env[19]);
    try {
      env[19] /*statement*/= ((org.fuwjin.chessur.expression.RepeatStatement)sub138.isSet("stmt", env[25])).statement();
    } catch(final Exception e140) {
      throw sub138.ex(e140);
    }
    Statement(sub138, env);
    sub138.publish(String.valueOf(sub138.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub138.isSet("indent", env[15])).increase();
    } catch(final Exception e141) {
      throw sub138.ex(e141);
    }
    sub138.publish(String.valueOf(sub138.isSet("indent", env[15])) + String.valueOf("while(true) {"));
    try {
      ((org.fuwjin.util.Indent)sub138.isSet("indent", env[15])).increase();
    } catch(final Exception e142) {
      throw sub138.ex(e142);
    }
    Statement(sub138, env);
    try {
      ((org.fuwjin.util.Indent)sub138.isSet("indent", env[15])).decrease();
    } catch(final Exception e143) {
      throw sub138.ex(e143);
    }
    sub138.publish(String.valueOf(sub138.isSet("indent", env[15])) + String.valueOf("}"));
    try {
      ((org.fuwjin.util.Indent)sub138.isSet("indent", env[15])).decrease();
    } catch(final Exception e144) {
      throw sub138.ex(e144);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub138.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e145) {
      throw sub138.ex(e145);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub138.isSet("exIndex", env[23]));
    sub138.publish(String.valueOf(sub138.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub138.isSet("className", env[4])) + String.valueOf("Exception ") + String.valueOf(sub138.isSet("exception", env[24])) + String.valueOf(") {"));
    sub138.publish(String.valueOf(sub138.isSet("indent", env[15])) + String.valueOf("  //continue"));
    sub138.publish(String.valueOf(sub138.isSet("indent", env[15])) + String.valueOf("}"));
    sub138.commit();
    return null;
  }

  static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub146 = input.sub();
    try {
      FilterAcceptStatement(sub146, env);
    } catch(final GrinCodeGeneratorException e147) {
      ValueAcceptStatement(sub146, env);
    }
    sub146.commit();
    return null;
  }

  static Object ValueAcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub148 = input.sub();
    final StringCursor sub149 = sub148.sub();
    if((Object)sub149.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ValueAcceptStatement == Boolean.FALSE) {
      throw sub149.ex("check failed");
    }
    sub148.publish(String.valueOf(sub148.isSet("indent", env[15])) + String.valueOf(sub148.isSet("input", env[17])) + String.valueOf(".accept"));
    try {
      final StringCursor sub150 = sub148.sub();
      final StringCursor sub151 = sub150.sub();
      if((Object)((org.fuwjin.chessur.expression.ValueAcceptStatement)sub151.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub151.ex("check failed");
      }
      sub150.publish("Not");
      sub150.commit();
    } catch(final GrinCodeGeneratorException e152) {
      //continue
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.ValueAcceptStatement)sub148.isSet("statement", env[19])).value();
    } catch(final Exception e153) {
      throw sub148.ex(e153);
    }
    sub148.publish("(");
    try {
      final StringCursor sub154 = sub148.sub();
      final StringCursor sub155 = sub154.sub();
      boolean b156 = true;
      try {
        if((Object)((java.lang.Object)org.fuwjin.chessur.expression.Variable.NEXT).equals((java.lang.Object)sub155.isSet("value", env[14])) == Boolean.FALSE) {
          b156 = false;
        }
      } catch(final GrinCodeGeneratorException e157) {
        b156 = false;
      }
      if(b156){
        throw sub154.ex("unexpected value");
      }
      sub154.publish(Value(sub154, env));
      sub154.commit();
    } catch(final GrinCodeGeneratorException e158) {
      //continue
    }
    sub148.publish(");");
    sub148.commit();
    return null;
  }

  static Object FilterAcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub159 = input.sub();
    final StringCursor sub160 = sub159.sub();
    if((Object)sub160.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.FilterAcceptStatement == Boolean.FALSE) {
      throw sub160.ex("check failed");
    }
    sub159.publish(String.valueOf(sub159.isSet("indent", env[15])) + String.valueOf(sub159.isSet("input", env[17])) + String.valueOf(".accept"));
    try {
      final StringCursor sub161 = sub159.sub();
      final StringCursor sub162 = sub161.sub();
      if((Object)((org.fuwjin.chessur.expression.FilterAcceptStatement)sub162.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub162.ex("check failed");
      }
      sub161.publish(String.valueOf("Not"));
      sub161.commit();
    } catch(final GrinCodeGeneratorException e163) {
      //continue
    }
    try {
      env[26] /*filter*/= ((org.fuwjin.chessur.expression.FilterAcceptStatement)sub159.isSet("statement", env[19])).filter();
    } catch(final Exception e164) {
      throw sub159.ex(e164);
    }
    sub159.publish(String.valueOf("In(\"") + String.valueOf(sub159.isSet("filter", env[26])) + String.valueOf("\",\"") + String.valueOf(Filter(sub159, env)) + String.valueOf("\");"));
    sub159.commit();
    return null;
  }

  static Object AcceptValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub165 = input.sub();
    try {
      env[14] /*value*/= FilterAcceptValue(sub165, env);
    } catch(final GrinCodeGeneratorException e166) {
      env[14] /*value*/= ValueAcceptValue(sub165, env);
    }
    sub165.commit();
    return sub165.isSet("value", env[14]);
  }

  static Object ValueAcceptValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub167 = input.sub();
    final StringCursor sub168 = sub167.sub();
    if((Object)sub168.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ValueAcceptStatement == Boolean.FALSE) {
      throw sub168.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.ValueAcceptStatement)sub167.isSet("statement", env[19])).value();
    } catch(final Exception e169) {
      throw sub167.ex(e169);
    }
    try {
      final StringCursor sub170 = sub167.sub();
      final StringCursor sub171 = sub170.sub();
      if((Object)((org.fuwjin.chessur.expression.ValueAcceptStatement)sub171.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub171.ex("check failed");
      }
      env[27] /*notted*/= "Not";
      env[28] /*val*/= Value(sub170, env);
      sub170.commit();
    } catch(final GrinCodeGeneratorException e172) {
      final StringCursor sub173 = sub167.sub();
      env[27] /*notted*/= "";
      try {
        final StringCursor sub174 = sub173.sub();
        final StringCursor sub175 = sub174.sub();
        boolean b176 = true;
        try {
          if((Object)((java.lang.Object)org.fuwjin.chessur.expression.Variable.NEXT).equals((java.lang.Object)sub175.isSet("value", env[14])) == Boolean.FALSE) {
            b176 = false;
          }
        } catch(final GrinCodeGeneratorException e177) {
          b176 = false;
        }
        if(b176){
          throw sub174.ex("unexpected value");
        }
        env[28] /*val*/= Value(sub174, env);
        sub174.commit();
      } catch(final GrinCodeGeneratorException e178) {
        final StringCursor sub179 = sub173.sub();
        env[28] /*val*/= "";
        sub179.commit();
      }
      sub173.commit();
    }
    sub167.commit();
    return String.valueOf(sub167.isSet("input", env[17])) + String.valueOf(".accept") + String.valueOf(sub167.isSet("notted", env[27])) + String.valueOf("(") + String.valueOf(sub167.isSet("val", env[28])) + String.valueOf(")");
  }

  static Object FilterAcceptValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub180 = input.sub();
    final StringCursor sub181 = sub180.sub();
    if((Object)sub181.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.FilterAcceptStatement == Boolean.FALSE) {
      throw sub181.ex("check failed");
    }
    try {
      final StringCursor sub182 = sub180.sub();
      final StringCursor sub183 = sub182.sub();
      if((Object)((org.fuwjin.chessur.expression.FilterAcceptStatement)sub183.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub183.ex("check failed");
      }
      env[27] /*notted*/= "Not";
      sub182.commit();
    } catch(final GrinCodeGeneratorException e184) {
      final StringCursor sub185 = sub180.sub();
      env[27] /*notted*/= "";
      sub185.commit();
    }
    try {
      env[26] /*filter*/= ((org.fuwjin.chessur.expression.FilterAcceptStatement)sub180.isSet("statement", env[19])).filter();
    } catch(final Exception e186) {
      throw sub180.ex(e186);
    }
    sub180.commit();
    return String.valueOf(sub180.isSet("input", env[17])) + String.valueOf(".accept") + String.valueOf(sub180.isSet("notted", env[27])) + String.valueOf("In(\"") + String.valueOf(sub180.isSet("filter", env[26])) + String.valueOf("\",\"") + String.valueOf(Filter(sub180, env)) + String.valueOf("\")");
  }

  static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub187 = input.sub();
    final StringCursor sub188 = sub187.sub();
    if((Object)sub188.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.PublishStatement == Boolean.FALSE) {
      throw sub188.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.PublishStatement)sub187.isSet("statement", env[19])).value();
    } catch(final Exception e189) {
      throw sub187.ex(e189);
    }
    sub187.publish(String.valueOf(sub187.isSet("indent", env[15])) + String.valueOf(sub187.isSet("input", env[17])) + String.valueOf(".publish(") + String.valueOf(Value(sub187, env)) + String.valueOf(");"));
    sub187.commit();
    return null;
  }

  static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub190 = input.sub();
    final StringCursor sub191 = sub190.sub();
    if((Object)sub191.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.AbortStatement == Boolean.FALSE) {
      throw sub191.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.AbortStatement)sub190.isSet("statement", env[19])).value();
    } catch(final Exception e192) {
      throw sub190.ex(e192);
    }
    sub190.publish(String.valueOf(sub190.isSet("indent", env[15])) + String.valueOf(sub190.isSet("input", env[17])) + String.valueOf(".abort(") + String.valueOf(Value(sub190, env)));
    try {
      sub190.publish(String.valueOf(", ") + String.valueOf(sub190.isSet("exception", env[24])));
    } catch(final GrinCodeGeneratorException e193) {
      //continue
    }
    sub190.publish(");");
    sub190.commit();
    return null;
  }

  static Object Block(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub194 = input.sub();
    final StringCursor sub195 = sub194.sub();
    if((Object)sub195.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Block == Boolean.FALSE) {
      throw sub195.ex("check failed");
    }
    env[29] /*block*/= sub194.isSet("statement", env[19]);
    try {
      env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub194.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e196) {
      throw sub194.ex(e196);
    }
    env[21] /*oldInput*/= sub194.isSet("input", env[17]);
    env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub194.isSet("subIndex", env[16]));
    sub194.publish(String.valueOf(sub194.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub194.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub194.isSet("oldInput", env[21])) + String.valueOf(".sub();"));
    try {
      env[18] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Block)sub194.isSet("block", env[29])).statements()).iterator();
    } catch(final Exception e197) {
      throw sub194.ex(e197);
    }
    try {
      final StringCursor sub198 = sub194.sub();
      try {
        env[19] /*statement*/= ((java.util.Iterator)sub198.isSet("statements", env[18])).next();
      } catch(final Exception e199) {
        throw sub198.ex(e199);
      }
      Statement(sub198, env);
      sub198.commit();
      try {
        while(true) {
          final StringCursor sub200 = sub194.sub();
          try {
            env[19] /*statement*/= ((java.util.Iterator)sub200.isSet("statements", env[18])).next();
          } catch(final Exception e201) {
            throw sub200.ex(e201);
          }
          Statement(sub200, env);
          sub200.commit();
        }
      } catch(final GrinCodeGeneratorException e202) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e203) {
      //continue
    }
    final StringCursor sub204 = sub194.sub();
    boolean b205 = true;
    try {
      if((Object)((java.util.Iterator)sub204.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        b205 = false;
      }
    } catch(final GrinCodeGeneratorException e206) {
      b205 = false;
    }
    if(b205){
      throw sub194.ex("unexpected value");
    }
    sub194.publish(String.valueOf(sub194.isSet("indent", env[15])) + String.valueOf(sub194.isSet("input", env[17])) + String.valueOf(".commit();"));
    sub194.commit();
    return null;
  }

  static Object Assignment(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub207 = input.sub();
    final StringCursor sub208 = sub207.sub();
    if((Object)sub208.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Assignment == Boolean.FALSE) {
      throw sub208.ex("check failed");
    }
    try {
      env[8] /*name*/= ((org.fuwjin.chessur.expression.Assignment)sub207.isSet("statement", env[19])).name();
    } catch(final Exception e209) {
      throw sub207.ex(e209);
    }
    try {
      env[9] /*index*/= ((org.fuwjin.util.NameIndex)sub207.isSet("indexer", env[1])).indexOf((java.lang.String)sub207.isSet("name", env[8]));
    } catch(final Exception e210) {
      throw sub207.ex(e210);
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.Assignment)sub207.isSet("statement", env[19])).value();
    } catch(final Exception e211) {
      throw sub207.ex(e211);
    }
    try {
      final StringCursor sub212 = sub207.sub();
      final StringCursor sub213 = sub212.sub();
      if((Object)sub213.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Invocation == Boolean.FALSE) {
        throw sub213.ex("check failed");
      }
      env[19] /*statement*/= sub212.isSet("value", env[14]);
      sub212.publish(String.valueOf(sub212.isSet("indent", env[15])) + String.valueOf("try {"));
      try {
        ((org.fuwjin.util.Indent)sub212.isSet("indent", env[15])).increase();
      } catch(final Exception e214) {
        throw sub212.ex(e214);
      }
      sub212.publish(String.valueOf(sub212.isSet("indent", env[15])) + String.valueOf("env[") + String.valueOf(sub212.isSet("index", env[9])) + String.valueOf("] /*") + String.valueOf(sub212.isSet("name", env[8])) + String.valueOf("*/= ") + String.valueOf(Invocation(sub212, env)) + String.valueOf(";"));
      try {
        ((org.fuwjin.util.Indent)sub212.isSet("indent", env[15])).decrease();
      } catch(final Exception e215) {
        throw sub212.ex(e215);
      }
      try {
        env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub212.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e216) {
        throw sub212.ex(e216);
      }
      env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub212.isSet("exIndex", env[23]));
      sub212.publish(String.valueOf(sub212.isSet("indent", env[15])) + String.valueOf("} catch(final Exception ") + String.valueOf(sub212.isSet("exception", env[24])) + String.valueOf(") {"));
      sub212.publish(String.valueOf(sub212.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub212.isSet("input", env[17])) + String.valueOf(".ex(") + String.valueOf(sub212.isSet("exception", env[24])) + String.valueOf(");"));
      sub212.publish(String.valueOf(sub212.isSet("indent", env[15])) + String.valueOf("}"));
      sub212.commit();
    } catch(final GrinCodeGeneratorException e217) {
      sub207.publish(String.valueOf(sub207.isSet("indent", env[15])) + String.valueOf("env[") + String.valueOf(sub207.isSet("index", env[9])) + String.valueOf("] /*") + String.valueOf(sub207.isSet("name", env[8])) + String.valueOf("*/= ") + String.valueOf(Value(sub207, env)) + String.valueOf(";"));
    }
    sub207.commit();
    return null;
  }

  static Object InvocationStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub218 = input.sub();
    final StringCursor sub219 = sub218.sub();
    if((Object)sub219.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Invocation == Boolean.FALSE) {
      throw sub219.ex("check failed");
    }
    try {
      env[30] /*function*/= ((org.fuwjin.chessur.expression.Invocation)sub218.isSet("statement", env[19])).function();
    } catch(final Exception e220) {
      throw sub218.ex(e220);
    }
    try {
      env[31] /*params*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Invocation)sub218.isSet("statement", env[19])).params()).iterator();
    } catch(final Exception e221) {
      throw sub218.ex(e221);
    }
    sub218.publish(String.valueOf(sub218.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub218.isSet("indent", env[15])).increase();
    } catch(final Exception e222) {
      throw sub218.ex(e222);
    }
    sub218.publish(sub218.isSet("indent", env[15]));
    sub218.publish(RenderFunction(sub218, env));
    sub218.publish(";");
    try {
      ((org.fuwjin.util.Indent)sub218.isSet("indent", env[15])).decrease();
    } catch(final Exception e223) {
      throw sub218.ex(e223);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub218.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e224) {
      throw sub218.ex(e224);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub218.isSet("exIndex", env[23]));
    sub218.publish(String.valueOf(sub218.isSet("indent", env[15])) + String.valueOf("} catch(final Exception ") + String.valueOf(sub218.isSet("exception", env[24])) + String.valueOf(") {"));
    sub218.publish(String.valueOf(sub218.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub218.isSet("input", env[17])) + String.valueOf(".ex(") + String.valueOf(sub218.isSet("exception", env[24])) + String.valueOf(");"));
    sub218.publish(String.valueOf(sub218.isSet("indent", env[15])) + String.valueOf("}"));
    sub218.commit();
    return null;
  }

  static Object Invocation(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub225 = input.sub();
    final StringCursor sub226 = sub225.sub();
    if((Object)sub226.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Invocation == Boolean.FALSE) {
      throw sub226.ex("check failed");
    }
    try {
      env[30] /*function*/= ((org.fuwjin.chessur.expression.Invocation)sub225.isSet("statement", env[19])).function();
    } catch(final Exception e227) {
      throw sub225.ex(e227);
    }
    try {
      env[31] /*params*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Invocation)sub225.isSet("statement", env[19])).params()).iterator();
    } catch(final Exception e228) {
      throw sub225.ex(e228);
    }
    sub225.commit();
    return RenderFunction(sub225, env);
  }

  static Object Number(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub229 = input.sub();
    final StringCursor sub230 = sub229.sub();
    if((Object)sub230.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Number == Boolean.FALSE) {
      throw sub230.ex("check failed");
    }
    sub229.commit();
    return ((org.fuwjin.chessur.expression.Number)sub229.isSet("value", env[14])).toString();
  }

  static Object Object(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub231 = input.sub();
    final StringCursor sub232 = sub231.sub();
    if((Object)sub232.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.ObjectTemplate == Boolean.FALSE) {
      throw sub232.ex("check failed");
    }
    sub231.publish(String.valueOf("new Object() {"));
    try {
      ((org.fuwjin.util.Indent)sub231.isSet("indent", env[15])).increase();
    } catch(final Exception e233) {
      throw sub231.ex(e233);
    }
    sub231.publish(String.valueOf(sub231.isSet("indent", env[15])) + String.valueOf("public Object value() {"));
    try {
      ((org.fuwjin.util.Indent)sub231.isSet("indent", env[15])).increase();
    } catch(final Exception e234) {
      throw sub231.ex(e234);
    }
    try {
      env[30] /*function*/= ((org.fuwjin.chessur.expression.ObjectTemplate)sub231.isSet("value", env[14])).constructor();
    } catch(final Exception e235) {
      throw sub231.ex(e235);
    }
    try {
      env[32] /*constructor*/= ((org.fuwjin.dinah.function.MemberFunction)sub231.isSet("function", env[30])).member();
    } catch(final Exception e236) {
      throw sub231.ex(e236);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Constructor)sub231.isSet("constructor", env[32])).getDeclaringClass();
    } catch(final Exception e237) {
      throw sub231.ex(e237);
    }
    sub231.publish(String.valueOf(sub231.isSet("indent", env[15])) + String.valueOf(Type(sub231, env)) + String.valueOf(" value = "));
    try {
      env[31] /*params*/= ((java.lang.Iterable)java.util.Collections.emptySet()).iterator();
    } catch(final Exception e238) {
      throw sub231.ex(e238);
    }
    Constructor(sub231, env);
    sub231.publish(String.valueOf(";"));
    try {
      env[34] /*setters*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.ObjectTemplate)sub231.isSet("value", env[14])).setters()).iterator();
    } catch(final Exception e239) {
      throw sub231.ex(e239);
    }
    try {
      final StringCursor sub240 = sub231.sub();
      try {
        env[35] /*field*/= ((java.util.Iterator)sub240.isSet("setters", env[34])).next();
      } catch(final Exception e241) {
        throw sub240.ex(e241);
      }
      try {
        env[30] /*function*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub240.isSet("field", env[35])).setter();
      } catch(final Exception e242) {
        throw sub240.ex(e242);
      }
      try {
        env[14] /*value*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub240.isSet("field", env[35])).value();
      } catch(final Exception e243) {
        throw sub240.ex(e243);
      }
      try {
        MemberFieldMutator(sub240, env);
      } catch(final GrinCodeGeneratorException e244) {
        try {
          MemberMethod(sub240, env);
        } catch(final GrinCodeGeneratorException e245) {
          VarArgs(sub240, env);
        }
      }
      sub240.commit();
      try {
        while(true) {
          final StringCursor sub246 = sub231.sub();
          try {
            env[35] /*field*/= ((java.util.Iterator)sub246.isSet("setters", env[34])).next();
          } catch(final Exception e247) {
            throw sub246.ex(e247);
          }
          try {
            env[30] /*function*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub246.isSet("field", env[35])).setter();
          } catch(final Exception e248) {
            throw sub246.ex(e248);
          }
          try {
            env[14] /*value*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub246.isSet("field", env[35])).value();
          } catch(final Exception e249) {
            throw sub246.ex(e249);
          }
          try {
            MemberFieldMutator(sub246, env);
          } catch(final GrinCodeGeneratorException e250) {
            try {
              MemberMethod(sub246, env);
            } catch(final GrinCodeGeneratorException e251) {
              VarArgs(sub246, env);
            }
          }
          sub246.commit();
        }
      } catch(final GrinCodeGeneratorException e252) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e253) {
      //continue
    }
    final StringCursor sub254 = sub231.sub();
    boolean b255 = true;
    try {
      if((Object)((java.util.Iterator)sub254.isSet("setters", env[34])).hasNext() == Boolean.FALSE) {
        b255 = false;
      }
    } catch(final GrinCodeGeneratorException e256) {
      b255 = false;
    }
    if(b255){
      throw sub231.ex("unexpected value");
    }
    sub231.publish(String.valueOf(sub231.isSet("indent", env[15])) + String.valueOf("return value;"));
    try {
      ((org.fuwjin.util.Indent)sub231.isSet("indent", env[15])).decrease();
    } catch(final Exception e257) {
      throw sub231.ex(e257);
    }
    sub231.publish(String.valueOf(sub231.isSet("indent", env[15])) + String.valueOf("}"));
    try {
      ((org.fuwjin.util.Indent)sub231.isSet("indent", env[15])).decrease();
    } catch(final Exception e258) {
      throw sub231.ex(e258);
    }
    sub231.publish(String.valueOf(sub231.isSet("indent", env[15])) + String.valueOf("}.value()"));
    sub231.commit();
    return null;
  }

  static Object MemberFieldMutator(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub259 = input.sub();
    final StringCursor sub260 = sub259.sub();
    if((Object)sub260.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.FieldMutatorFunction == Boolean.FALSE) {
      throw sub260.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.MemberFunction)sub259.isSet("function", env[30])).member();
    } catch(final Exception e261) {
      throw sub259.ex(e261);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub259.isSet("field", env[35])).getType();
    } catch(final Exception e262) {
      throw sub259.ex(e262);
    }
    sub259.publish(String.valueOf(sub259.isSet("indent", env[15])) + String.valueOf("value.") + String.valueOf(((java.lang.reflect.Field)sub259.isSet("field", env[35])).getName()) + String.valueOf(" = (") + String.valueOf(Type(sub259, env)) + String.valueOf(")") + String.valueOf(Value(sub259, env)) + String.valueOf(";"));
    sub259.commit();
    return null;
  }

  static Object MemberMethod(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub263 = input.sub();
    final StringCursor sub264 = sub263.sub();
    if((Object)sub264.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.MethodFunction == Boolean.FALSE) {
      throw sub264.ex("check failed");
    }
    try {
      env[36] /*method*/= ((org.fuwjin.dinah.function.MemberFunction)sub263.isSet("function", env[30])).member();
    } catch(final Exception e265) {
      throw sub263.ex(e265);
    }
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Method)sub263.isSet("method", env[36])).getParameterTypes())).iterator();
    } catch(final Exception e266) {
      throw sub263.ex(e266);
    }
    try {
      env[33] /*type*/= ((java.util.Iterator)sub263.isSet("types", env[37])).next();
    } catch(final Exception e267) {
      throw sub263.ex(e267);
    }
    final StringCursor sub268 = sub263.sub();
    boolean b269 = true;
    try {
      if((Object)((java.util.Iterator)sub268.isSet("types", env[37])).hasNext() == Boolean.FALSE) {
        b269 = false;
      }
    } catch(final GrinCodeGeneratorException e270) {
      b269 = false;
    }
    if(b269){
      throw sub263.ex("unexpected value");
    }
    sub263.publish(String.valueOf(sub263.isSet("indent", env[15])) + String.valueOf("value.") + String.valueOf(((java.lang.reflect.Method)sub263.isSet("method", env[36])).getName()) + String.valueOf("((") + String.valueOf(Type(sub263, env)) + String.valueOf(")") + String.valueOf(Value(sub263, env)) + String.valueOf(");"));
    sub263.commit();
    return null;
  }

  static Object RenderFunction(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub271 = input.sub();
    try {
      final StringBuilder builder272 = new StringBuilder();
      final StringCursor sub273 = sub271.subOutput(builder272);
      Constructor(sub273, env);
      sub273.commit();
      env[38] /*render*/= builder272.toString();
    } catch(final GrinCodeGeneratorException e274) {
      try {
        final StringBuilder builder275 = new StringBuilder();
        final StringCursor sub276 = sub271.subOutput(builder275);
        FieldAccess(sub276, env);
        sub276.commit();
        env[38] /*render*/= builder275.toString();
      } catch(final GrinCodeGeneratorException e277) {
        try {
          final StringBuilder builder278 = new StringBuilder();
          final StringCursor sub279 = sub271.subOutput(builder278);
          FieldMutator(sub279, env);
          sub279.commit();
          env[38] /*render*/= builder278.toString();
        } catch(final GrinCodeGeneratorException e280) {
          try {
            final StringBuilder builder281 = new StringBuilder();
            final StringCursor sub282 = sub271.subOutput(builder281);
            InstanceOf(sub282, env);
            sub282.commit();
            env[38] /*render*/= builder281.toString();
          } catch(final GrinCodeGeneratorException e283) {
            try {
              final StringBuilder builder284 = new StringBuilder();
              final StringCursor sub285 = sub271.subOutput(builder284);
              Method(sub285, env);
              sub285.commit();
              env[38] /*render*/= builder284.toString();
            } catch(final GrinCodeGeneratorException e286) {
              try {
                final StringBuilder builder287 = new StringBuilder();
                final StringCursor sub288 = sub271.subOutput(builder287);
                StaticFieldAccess(sub288, env);
                sub288.commit();
                env[38] /*render*/= builder287.toString();
              } catch(final GrinCodeGeneratorException e289) {
                try {
                  final StringBuilder builder290 = new StringBuilder();
                  final StringCursor sub291 = sub271.subOutput(builder290);
                  StaticFieldMutator(sub291, env);
                  sub291.commit();
                  env[38] /*render*/= builder290.toString();
                } catch(final GrinCodeGeneratorException e292) {
                  try {
                    final StringBuilder builder293 = new StringBuilder();
                    final StringCursor sub294 = sub271.subOutput(builder293);
                    StaticMethod(sub294, env);
                    sub294.commit();
                    env[38] /*render*/= builder293.toString();
                  } catch(final GrinCodeGeneratorException e295) {
                    final StringBuilder builder296 = new StringBuilder();
                    final StringCursor sub297 = sub271.subOutput(builder296);
                    VarArgs(sub297, env);
                    sub297.commit();
                    env[38] /*render*/= builder296.toString();
                  }
                }
              }
            }
          }
        }
      }
    }
    final StringCursor sub298 = sub271.sub();
    boolean b299 = true;
    try {
      if((Object)((java.util.Iterator)sub298.isSet("params", env[31])).hasNext() == Boolean.FALSE) {
        b299 = false;
      }
    } catch(final GrinCodeGeneratorException e300) {
      b299 = false;
    }
    if(b299){
      throw sub271.ex("unexpected value");
    }
    sub271.commit();
    return sub271.isSet("render", env[38]);
  }

  static Object Constructor(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub301 = input.sub();
    final StringCursor sub302 = sub301.sub();
    if((Object)sub302.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.ConstructorFunction == Boolean.FALSE) {
      throw sub302.ex("check failed");
    }
    try {
      env[32] /*constructor*/= ((org.fuwjin.dinah.function.MemberFunction)sub301.isSet("function", env[30])).member();
    } catch(final Exception e303) {
      throw sub301.ex(e303);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Constructor)sub301.isSet("constructor", env[32])).getDeclaringClass();
    } catch(final Exception e304) {
      throw sub301.ex(e304);
    }
    sub301.publish(String.valueOf("new ") + String.valueOf(Type(sub301, env)));
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Constructor)sub301.isSet("constructor", env[32])).getParameterTypes())).iterator();
    } catch(final Exception e305) {
      throw sub301.ex(e305);
    }
    Params(sub301, env);
    sub301.commit();
    return null;
  }

  static Object FieldAccess(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub306 = input.sub();
    final StringCursor sub307 = sub306.sub();
    if((Object)sub307.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.FieldAccessFunction == Boolean.FALSE) {
      throw sub307.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.MemberFunction)sub306.isSet("function", env[30])).member();
    } catch(final Exception e308) {
      throw sub306.ex(e308);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub306.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e309) {
      throw sub306.ex(e309);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub306.isSet("params", env[31])).next();
    } catch(final Exception e310) {
      throw sub306.ex(e310);
    }
    sub306.publish(String.valueOf("((") + String.valueOf(Type(sub306, env)) + String.valueOf(")") + String.valueOf(Value(sub306, env)) + String.valueOf(").") + String.valueOf(((java.lang.reflect.Field)sub306.isSet("field", env[35])).getName()));
    sub306.commit();
    return null;
  }

  static Object FieldMutator(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub311 = input.sub();
    final StringCursor sub312 = sub311.sub();
    if((Object)sub312.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.FieldMutatorFunction == Boolean.FALSE) {
      throw sub312.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.MemberFunction)sub311.isSet("function", env[30])).member();
    } catch(final Exception e313) {
      throw sub311.ex(e313);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub311.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e314) {
      throw sub311.ex(e314);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub311.isSet("params", env[31])).next();
    } catch(final Exception e315) {
      throw sub311.ex(e315);
    }
    sub311.publish(String.valueOf("((") + String.valueOf(Type(sub311, env)) + String.valueOf(")") + String.valueOf(Value(sub311, env)) + String.valueOf(").") + String.valueOf(((java.lang.reflect.Field)sub311.isSet("field", env[35])).getName()));
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub311.isSet("field", env[35])).getType();
    } catch(final Exception e316) {
      throw sub311.ex(e316);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub311.isSet("params", env[31])).next();
    } catch(final Exception e317) {
      throw sub311.ex(e317);
    }
    sub311.publish(String.valueOf(" = (") + String.valueOf(Type(sub311, env)) + String.valueOf(")") + String.valueOf(Value(sub311, env)));
    sub311.commit();
    return null;
  }

  static Object InstanceOf(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub318 = input.sub();
    final StringCursor sub319 = sub318.sub();
    if((Object)sub319.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.InstanceOfFunction == Boolean.FALSE) {
      throw sub319.ex("check failed");
    }
    try {
      env[33] /*type*/= ((org.fuwjin.dinah.function.InstanceOfFunction)sub318.isSet("function", env[30])).type();
    } catch(final Exception e320) {
      throw sub318.ex(e320);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub318.isSet("params", env[31])).next();
    } catch(final Exception e321) {
      throw sub318.ex(e321);
    }
    sub318.publish(String.valueOf(Value(sub318, env)) + String.valueOf(" instanceof ") + String.valueOf(Type(sub318, env)));
    sub318.commit();
    return null;
  }

  static Object Method(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub322 = input.sub();
    final StringCursor sub323 = sub322.sub();
    if((Object)sub323.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.MethodFunction == Boolean.FALSE) {
      throw sub323.ex("check failed");
    }
    try {
      env[36] /*method*/= ((org.fuwjin.dinah.function.MemberFunction)sub322.isSet("function", env[30])).member();
    } catch(final Exception e324) {
      throw sub322.ex(e324);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Method)sub322.isSet("method", env[36])).getDeclaringClass();
    } catch(final Exception e325) {
      throw sub322.ex(e325);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub322.isSet("params", env[31])).next();
    } catch(final Exception e326) {
      throw sub322.ex(e326);
    }
    sub322.publish(String.valueOf("((") + String.valueOf(Type(sub322, env)) + String.valueOf(")") + String.valueOf(Value(sub322, env)) + String.valueOf(").") + String.valueOf(((java.lang.reflect.Method)sub322.isSet("method", env[36])).getName()));
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Method)sub322.isSet("method", env[36])).getParameterTypes())).iterator();
    } catch(final Exception e327) {
      throw sub322.ex(e327);
    }
    Params(sub322, env);
    sub322.commit();
    return null;
  }

  static Object StaticFieldAccess(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub328 = input.sub();
    final StringCursor sub329 = sub328.sub();
    if((Object)sub329.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.StaticFieldAccessFunction == Boolean.FALSE) {
      throw sub329.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.MemberFunction)sub328.isSet("function", env[30])).member();
    } catch(final Exception e330) {
      throw sub328.ex(e330);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub328.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e331) {
      throw sub328.ex(e331);
    }
    sub328.publish(String.valueOf(Type(sub328, env)) + String.valueOf(".") + String.valueOf(((java.lang.reflect.Field)sub328.isSet("field", env[35])).getName()));
    sub328.commit();
    return null;
  }

  static Object StaticFieldMutator(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub332 = input.sub();
    final StringCursor sub333 = sub332.sub();
    if((Object)sub333.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.StaticFieldMutatorFunction == Boolean.FALSE) {
      throw sub333.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.MemberFunction)sub332.isSet("function", env[30])).member();
    } catch(final Exception e334) {
      throw sub332.ex(e334);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub332.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e335) {
      throw sub332.ex(e335);
    }
    sub332.publish(String.valueOf(Type(sub332, env)) + String.valueOf(".") + String.valueOf(((java.lang.reflect.Field)sub332.isSet("field", env[35])).getName()));
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub332.isSet("field", env[35])).getType();
    } catch(final Exception e336) {
      throw sub332.ex(e336);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub332.isSet("params", env[31])).next();
    } catch(final Exception e337) {
      throw sub332.ex(e337);
    }
    sub332.publish(String.valueOf(" = (") + String.valueOf(Type(sub332, env)) + String.valueOf(")") + String.valueOf(Value(sub332, env)));
    sub332.commit();
    return null;
  }

  static Object StaticMethod(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub338 = input.sub();
    final StringCursor sub339 = sub338.sub();
    if((Object)sub339.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.StaticMethodFunction == Boolean.FALSE) {
      throw sub339.ex("check failed");
    }
    try {
      env[36] /*method*/= ((org.fuwjin.dinah.function.MemberFunction)sub338.isSet("function", env[30])).member();
    } catch(final Exception e340) {
      throw sub338.ex(e340);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Method)sub338.isSet("method", env[36])).getDeclaringClass();
    } catch(final Exception e341) {
      throw sub338.ex(e341);
    }
    sub338.publish(String.valueOf(Type(sub338, env)) + String.valueOf(".") + String.valueOf(((java.lang.reflect.Method)sub338.isSet("method", env[36])).getName()));
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Method)sub338.isSet("method", env[36])).getParameterTypes())).iterator();
    } catch(final Exception e342) {
      throw sub338.ex(e342);
    }
    Params(sub338, env);
    sub338.commit();
    return null;
  }

  static Object VarArgs(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub343 = input.sub();
    sub343.abort(String.valueOf("VarArgs is currently unsupported: ") + String.valueOf(((org.fuwjin.dinah.Function)sub343.isSet("function", env[30])).signature()));
    sub343.commit();
    return null;
  }

  static Object Params(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub344 = input.sub();
    sub344.publish("(");
    try {
      final StringCursor sub345 = sub344.sub();
      try {
        env[33] /*type*/= ((java.util.Iterator)sub345.isSet("types", env[37])).next();
      } catch(final Exception e346) {
        throw sub345.ex(e346);
      }
      try {
        env[14] /*value*/= ((java.util.Iterator)sub345.isSet("params", env[31])).next();
      } catch(final Exception e347) {
        throw sub345.ex(e347);
      }
      sub345.publish(String.valueOf("(") + String.valueOf(Type(sub345, env)) + String.valueOf(")") + String.valueOf(Value(sub345, env)));
      try {
        final StringCursor sub348 = sub345.sub();
        try {
          env[33] /*type*/= ((java.util.Iterator)sub348.isSet("types", env[37])).next();
        } catch(final Exception e349) {
          throw sub348.ex(e349);
        }
        try {
          env[14] /*value*/= ((java.util.Iterator)sub348.isSet("params", env[31])).next();
        } catch(final Exception e350) {
          throw sub348.ex(e350);
        }
        sub348.publish(String.valueOf(", (") + String.valueOf(Type(sub348, env)) + String.valueOf(")") + String.valueOf(Value(sub348, env)));
        sub348.commit();
        try {
          while(true) {
            final StringCursor sub351 = sub345.sub();
            try {
              env[33] /*type*/= ((java.util.Iterator)sub351.isSet("types", env[37])).next();
            } catch(final Exception e352) {
              throw sub351.ex(e352);
            }
            try {
              env[14] /*value*/= ((java.util.Iterator)sub351.isSet("params", env[31])).next();
            } catch(final Exception e353) {
              throw sub351.ex(e353);
            }
            sub351.publish(String.valueOf(", (") + String.valueOf(Type(sub351, env)) + String.valueOf(")") + String.valueOf(Value(sub351, env)));
            sub351.commit();
          }
        } catch(final GrinCodeGeneratorException e354) {
          //continue
        }
      } catch(final GrinCodeGeneratorException e355) {
        //continue
      }
      sub345.commit();
    } catch(final GrinCodeGeneratorException e356) {
      //continue
    }
    final StringCursor sub357 = sub344.sub();
    boolean b358 = true;
    try {
      if((Object)((java.util.Iterator)sub357.isSet("types", env[37])).hasNext() == Boolean.FALSE) {
        b358 = false;
      }
    } catch(final GrinCodeGeneratorException e359) {
      b358 = false;
    }
    if(b358){
      throw sub344.ex("unexpected value");
    }
    sub344.publish(")");
    sub344.commit();
    return null;
  }

  static Object Type(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub360 = input.sub();
    sub360.commit();
    return ((java.lang.Class)org.fuwjin.util.TypeUtils.toWrapper((java.lang.reflect.Type)sub360.isSet("type", env[33]))).getCanonicalName();
  }

  static Object Filter(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub361 = input.sub();
    final StringCursor sub362 = sub361.sub();
    if((Object)sub362.isSet("filter", env[26]) instanceof org.fuwjin.chessur.expression.Filter == Boolean.FALSE) {
      throw sub362.ex("check failed");
    }
    try {
      env[39] /*filterBuffer*/= new java.lang.StringBuilder();
    } catch(final Exception e363) {
      throw sub361.ex(e363);
    }
    try {
      env[40] /*ranges*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Filter)sub361.isSet("filter", env[26])).ranges()).iterator();
    } catch(final Exception e364) {
      throw sub361.ex(e364);
    }
    try {
      env[41] /*range*/= ((java.util.Iterator)sub361.isSet("ranges", env[40])).next();
    } catch(final Exception e365) {
      throw sub361.ex(e365);
    }
    Range(sub361, env);
    try {
      final StringCursor sub366 = sub361.sub();
      try {
        env[41] /*range*/= ((java.util.Iterator)sub366.isSet("ranges", env[40])).next();
      } catch(final Exception e367) {
        throw sub366.ex(e367);
      }
      Range(sub366, env);
      sub366.commit();
      try {
        while(true) {
          final StringCursor sub368 = sub361.sub();
          try {
            env[41] /*range*/= ((java.util.Iterator)sub368.isSet("ranges", env[40])).next();
          } catch(final Exception e369) {
            throw sub368.ex(e369);
          }
          Range(sub368, env);
          sub368.commit();
        }
      } catch(final GrinCodeGeneratorException e370) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e371) {
      //continue
    }
    final StringCursor sub372 = sub361.sub();
    boolean b373 = true;
    try {
      if((Object)((java.util.Iterator)sub372.isSet("ranges", env[40])).hasNext() == Boolean.FALSE) {
        b373 = false;
      }
    } catch(final GrinCodeGeneratorException e374) {
      b373 = false;
    }
    if(b373){
      throw sub361.ex("unexpected value");
    }
    sub361.commit();
    return ((java.lang.StringBuilder)sub361.isSet("filterBuffer", env[39])).toString();
  }

  static Object Range(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub375 = input.sub();
    final StringCursor sub376 = sub375.sub();
    if((Object)sub376.isSet("range", env[41]) instanceof org.fuwjin.util.CodePointSet.Range == Boolean.FALSE) {
      throw sub376.ex("check failed");
    }
    try {
      env[42] /*chars*/= ((java.lang.Iterable)((org.fuwjin.util.CodePointSet.Range)sub375.isSet("range", env[41])).chars()).iterator();
    } catch(final Exception e377) {
      throw sub375.ex(e377);
    }
    final StringCursor sub378 = sub375.sub();
    try {
      env[43] /*ch*/= ((java.util.Iterator)sub378.isSet("chars", env[42])).next();
    } catch(final Exception e379) {
      throw sub378.ex(e379);
    }
    try {
      ((java.lang.Appendable)sub378.isSet("filterBuffer", env[39])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub378.isSet("ch", env[43])));
    } catch(final Exception e380) {
      throw sub378.ex(e380);
    }
    sub378.commit();
    try {
      while(true) {
        final StringCursor sub381 = sub375.sub();
        try {
          env[43] /*ch*/= ((java.util.Iterator)sub381.isSet("chars", env[42])).next();
        } catch(final Exception e382) {
          throw sub381.ex(e382);
        }
        try {
          ((java.lang.Appendable)sub381.isSet("filterBuffer", env[39])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub381.isSet("ch", env[43])));
        } catch(final Exception e383) {
          throw sub381.ex(e383);
        }
        sub381.commit();
      }
    } catch(final GrinCodeGeneratorException e384) {
      //continue
    }
    final StringCursor sub385 = sub375.sub();
    boolean b386 = true;
    try {
      if((Object)((java.util.Iterator)sub385.isSet("chars", env[42])).hasNext() == Boolean.FALSE) {
        b386 = false;
      }
    } catch(final GrinCodeGeneratorException e387) {
      b386 = false;
    }
    if(b386){
      throw sub375.ex("unexpected value");
    }
    sub375.commit();
    return null;
  }

  static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub388 = input.sub();
    final StringCursor sub389 = sub388.sub();
    if((Object)sub389.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Literal == Boolean.FALSE) {
      throw sub389.ex("check failed");
    }
    try {
      env[42] /*chars*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Literal)sub388.isSet("value", env[14])).chars()).iterator();
    } catch(final Exception e390) {
      throw sub388.ex(e390);
    }
    try {
      env[44] /*builder*/= new java.lang.StringBuilder();
    } catch(final Exception e391) {
      throw sub388.ex(e391);
    }
    try {
      final StringCursor sub392 = sub388.sub();
      try {
        env[43] /*ch*/= ((java.util.Iterator)sub392.isSet("chars", env[42])).next();
      } catch(final Exception e393) {
        throw sub392.ex(e393);
      }
      try {
        ((java.lang.Appendable)sub392.isSet("builder", env[44])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub392.isSet("ch", env[43])));
      } catch(final Exception e394) {
        throw sub392.ex(e394);
      }
      sub392.commit();
      try {
        while(true) {
          final StringCursor sub395 = sub388.sub();
          try {
            env[43] /*ch*/= ((java.util.Iterator)sub395.isSet("chars", env[42])).next();
          } catch(final Exception e396) {
            throw sub395.ex(e396);
          }
          try {
            ((java.lang.Appendable)sub395.isSet("builder", env[44])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub395.isSet("ch", env[43])));
          } catch(final Exception e397) {
            throw sub395.ex(e397);
          }
          sub395.commit();
        }
      } catch(final GrinCodeGeneratorException e398) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e399) {
      //continue
    }
    final StringCursor sub400 = sub388.sub();
    boolean b401 = true;
    try {
      if((Object)((java.util.Iterator)sub400.isSet("chars", env[42])).hasNext() == Boolean.FALSE) {
        b401 = false;
      }
    } catch(final GrinCodeGeneratorException e402) {
      b401 = false;
    }
    if(b401){
      throw sub388.ex("unexpected value");
    }
    sub388.commit();
    return String.valueOf("\"") + String.valueOf(sub388.isSet("builder", env[44])) + String.valueOf("\"");
  }

  static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub403 = input.sub();
    final StringCursor sub404 = sub403.sub();
    if((Object)sub404.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.CompositeLiteral == Boolean.FALSE) {
      throw sub404.ex("check failed");
    }
    env[45] /*composite*/= sub403.isSet("value", env[14]);
    try {
      env[46] /*values*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.CompositeLiteral)sub403.isSet("composite", env[45])).values()).iterator();
    } catch(final Exception e405) {
      throw sub403.ex(e405);
    }
    try {
      final StringCursor sub406 = sub403.sub();
      try {
        env[14] /*value*/= ((java.util.Iterator)sub406.isSet("values", env[46])).next();
      } catch(final Exception e407) {
        throw sub406.ex(e407);
      }
      try {
        final StringCursor sub408 = sub406.sub();
        final StringCursor sub409 = sub408.sub();
        boolean b410 = true;
        try {
          if((Object)((java.util.Iterator)sub409.isSet("values", env[46])).hasNext() == Boolean.FALSE) {
            b410 = false;
          }
        } catch(final GrinCodeGeneratorException e411) {
          b410 = false;
        }
        if(b410){
          throw sub408.ex("unexpected value");
        }
        env[20] /*result*/= String.valueOf("String.valueOf(") + String.valueOf(Value(sub408, env)) + String.valueOf(")");
        sub408.commit();
      } catch(final GrinCodeGeneratorException e412) {
        final StringCursor sub413 = sub406.sub();
        try {
          env[47] /*list*/= new java.util.ArrayList();
        } catch(final Exception e414) {
          throw sub413.ex(e414);
        }
        try {
          ((java.util.ArrayList)sub413.isSet("list", env[47])).add((java.lang.Object)String.valueOf("String.valueOf(") + String.valueOf(Value(sub413, env)) + String.valueOf(")"));
        } catch(final Exception e415) {
          throw sub413.ex(e415);
        }
        final StringCursor sub416 = sub413.sub();
        try {
          env[14] /*value*/= ((java.util.Iterator)sub416.isSet("values", env[46])).next();
        } catch(final Exception e417) {
          throw sub416.ex(e417);
        }
        try {
          ((java.util.ArrayList)sub416.isSet("list", env[47])).add((java.lang.Object)String.valueOf("String.valueOf(") + String.valueOf(Value(sub416, env)) + String.valueOf(")"));
        } catch(final Exception e418) {
          throw sub416.ex(e418);
        }
        sub416.commit();
        try {
          while(true) {
            final StringCursor sub419 = sub413.sub();
            try {
              env[14] /*value*/= ((java.util.Iterator)sub419.isSet("values", env[46])).next();
            } catch(final Exception e420) {
              throw sub419.ex(e420);
            }
            try {
              ((java.util.ArrayList)sub419.isSet("list", env[47])).add((java.lang.Object)String.valueOf("String.valueOf(") + String.valueOf(Value(sub419, env)) + String.valueOf(")"));
            } catch(final Exception e421) {
              throw sub419.ex(e421);
            }
            sub419.commit();
          }
        } catch(final GrinCodeGeneratorException e422) {
          //continue
        }
        final StringCursor sub423 = sub413.sub();
        boolean b424 = true;
        try {
          if((Object)((java.util.Iterator)sub423.isSet("values", env[46])).hasNext() == Boolean.FALSE) {
            b424 = false;
          }
        } catch(final GrinCodeGeneratorException e425) {
          b424 = false;
        }
        if(b424){
          throw sub413.ex("unexpected value");
        }
        try {
          env[20] /*result*/= org.fuwjin.util.StringUtils.join((java.lang.String)String.valueOf(" + "), (java.lang.Iterable)sub413.isSet("list", env[47]));
        } catch(final Exception e426) {
          throw sub413.ex(e426);
        }
        sub413.commit();
      }
      sub406.commit();
    } catch(final GrinCodeGeneratorException e427) {
      //continue
    }
    sub403.commit();
    return sub403.isSet("result", env[20]);
  }

  static Object NextValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub428 = input.sub();
    final StringCursor sub429 = sub428.sub();
    if((Object)((java.lang.Object)org.fuwjin.chessur.expression.Variable.NEXT).equals((java.lang.Object)sub429.isSet("value", env[14])) == Boolean.FALSE) {
      throw sub429.ex("check failed");
    }
    sub428.commit();
    return String.valueOf(sub428.isSet("input", env[17])) + String.valueOf(".next()");
  }

  static Object MatchValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub430 = input.sub();
    final StringCursor sub431 = sub430.sub();
    if((Object)((java.lang.Object)String.valueOf("match")).equals((java.lang.Object)((org.fuwjin.chessur.expression.Variable)sub431.isSet("value", env[14])).name()) == Boolean.FALSE) {
      throw sub431.ex("check failed");
    }
    sub430.commit();
    return String.valueOf(sub430.isSet("input", env[17])) + String.valueOf(".match()");
  }

  static Object ScriptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub432 = input.sub();
    try {
      sub432.publish(String.valueOf(sub432.isSet("indent", env[15])) + String.valueOf(Script(sub432, env)) + String.valueOf(";"));
    } catch(final GrinCodeGeneratorException e433) {
      try {
        final StringCursor sub434 = sub432.sub();
        final StringCursor sub435 = sub434.sub();
        if((Object)sub435.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptOutput == Boolean.FALSE) {
          throw sub435.ex("check failed");
        }
        try {
          env[8] /*name*/= ((org.fuwjin.chessur.expression.ScriptOutput)sub434.isSet("statement", env[19])).name();
        } catch(final Exception e436) {
          throw sub434.ex(e436);
        }
        try {
          env[19] /*statement*/= ((org.fuwjin.chessur.expression.ScriptOutput)sub434.isSet("statement", env[19])).spec();
        } catch(final Exception e437) {
          throw sub434.ex(e437);
        }
        try {
          env[22] /*bIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub434.isSet("varIndex", env[0])).incrementAndGet();
        } catch(final Exception e438) {
          throw sub434.ex(e438);
        }
        env[44] /*builder*/= String.valueOf("builder") + String.valueOf(sub434.isSet("bIndex", env[22]));
        sub434.publish(String.valueOf(sub434.isSet("indent", env[15])) + String.valueOf("final StringBuilder ") + String.valueOf(sub434.isSet("builder", env[44])) + String.valueOf(" = new StringBuilder();"));
        try {
          env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub434.isSet("varIndex", env[0])).incrementAndGet();
        } catch(final Exception e439) {
          throw sub434.ex(e439);
        }
        env[21] /*oldInput*/= sub434.isSet("input", env[17]);
        env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub434.isSet("subIndex", env[16]));
        sub434.publish(String.valueOf(sub434.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub434.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub434.isSet("oldInput", env[21])) + String.valueOf(".subOutput(") + String.valueOf(sub434.isSet("builder", env[44])) + String.valueOf(");"));
        ScriptStatement(sub434, env);
        sub434.publish(String.valueOf(sub434.isSet("indent", env[15])) + String.valueOf(sub434.isSet("input", env[17])) + String.valueOf(".commit();"));
        try {
          env[9] /*index*/= ((org.fuwjin.util.NameIndex)sub434.isSet("indexer", env[1])).indexOf((java.lang.String)sub434.isSet("name", env[8]));
        } catch(final Exception e440) {
          throw sub434.ex(e440);
        }
        sub434.publish(String.valueOf(sub434.isSet("indent", env[15])) + String.valueOf("env[") + String.valueOf(sub434.isSet("index", env[9])) + String.valueOf("] /*") + String.valueOf(sub434.isSet("name", env[8])) + String.valueOf("*/= ") + String.valueOf(sub434.isSet("builder", env[44])) + String.valueOf(".toString();"));
        sub434.commit();
      } catch(final GrinCodeGeneratorException e441) {
        try {
          final StringCursor sub442 = sub432.sub();
          final StringCursor sub443 = sub442.sub();
          if((Object)sub443.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptInput == Boolean.FALSE) {
            throw sub443.ex("check failed");
          }
          try {
            env[14] /*value*/= ((org.fuwjin.chessur.expression.ScriptInput)sub442.isSet("statement", env[19])).value();
          } catch(final Exception e444) {
            throw sub442.ex(e444);
          }
          try {
            env[19] /*statement*/= ((org.fuwjin.chessur.expression.ScriptInput)sub442.isSet("statement", env[19])).spec();
          } catch(final Exception e445) {
            throw sub442.ex(e445);
          }
          try {
            env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub442.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e446) {
            throw sub442.ex(e446);
          }
          env[21] /*oldInput*/= sub442.isSet("input", env[17]);
          env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub442.isSet("subIndex", env[16]));
          sub442.publish(String.valueOf(sub442.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub442.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub442.isSet("oldInput", env[21])) + String.valueOf(".subInput(String.valueOf(") + String.valueOf(Value(sub442, env)) + String.valueOf("));"));
          ScriptStatement(sub442, env);
          sub442.publish(String.valueOf(sub442.isSet("indent", env[15])) + String.valueOf(sub442.isSet("input", env[17])) + String.valueOf(".commit();"));
          sub442.commit();
        } catch(final GrinCodeGeneratorException e447) {
          final StringCursor sub448 = sub432.sub();
          final StringCursor sub449 = sub448.sub();
          if((Object)sub449.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptPipe == Boolean.FALSE) {
            throw sub449.ex("check failed");
          }
          try {
            env[48] /*source*/= ((org.fuwjin.chessur.expression.ScriptPipe)sub448.isSet("statement", env[19])).source();
          } catch(final Exception e450) {
            throw sub448.ex(e450);
          }
          try {
            env[49] /*sink*/= ((org.fuwjin.chessur.expression.ScriptPipe)sub448.isSet("statement", env[19])).sink();
          } catch(final Exception e451) {
            throw sub448.ex(e451);
          }
          try {
            env[22] /*bIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub448.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e452) {
            throw sub448.ex(e452);
          }
          env[44] /*builder*/= String.valueOf("builder") + String.valueOf(sub448.isSet("bIndex", env[22]));
          sub448.publish(String.valueOf(sub448.isSet("indent", env[15])) + String.valueOf("final StringBuilder ") + String.valueOf(sub448.isSet("builder", env[44])) + String.valueOf(" = new StringBuilder();"));
          try {
            env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub448.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e453) {
            throw sub448.ex(e453);
          }
          env[21] /*oldInput*/= sub448.isSet("input", env[17]);
          env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub448.isSet("subIndex", env[16]));
          sub448.publish(String.valueOf(sub448.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub448.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub448.isSet("oldInput", env[21])) + String.valueOf(".subOutput(") + String.valueOf(sub448.isSet("builder", env[44])) + String.valueOf(");"));
          env[19] /*statement*/= sub448.isSet("source", env[48]);
          ScriptStatement(sub448, env);
          sub448.publish(String.valueOf(sub448.isSet("indent", env[15])) + String.valueOf(sub448.isSet("input", env[17])) + String.valueOf(".commit();"));
          try {
            env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub448.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e454) {
            throw sub448.ex(e454);
          }
          env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub448.isSet("subIndex", env[16]));
          sub448.publish(String.valueOf(sub448.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub448.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub448.isSet("oldInput", env[21])) + String.valueOf(".subInput(") + String.valueOf(sub448.isSet("builder", env[44])) + String.valueOf(");"));
          env[19] /*statement*/= sub448.isSet("sink", env[49]);
          ScriptStatement(sub448, env);
          sub448.publish(String.valueOf(sub448.isSet("indent", env[15])) + String.valueOf(sub448.isSet("input", env[17])) + String.valueOf(".commit();"));
          sub448.commit();
        }
      }
    }
    sub432.commit();
    return null;
  }

  static Object Script(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub455 = input.sub();
    try {
      final StringCursor sub456 = sub455.sub();
      final StringCursor sub457 = sub456.sub();
      if((Object)sub457.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptImpl == Boolean.FALSE) {
        throw sub457.ex("check failed");
      }
      try {
        env[8] /*name*/= ((org.fuwjin.chessur.expression.ScriptImpl)sub456.isSet("statement", env[19])).name();
      } catch(final Exception e458) {
        throw sub456.ex(e458);
      }
      env[20] /*result*/= String.valueOf(sub456.isSet("name", env[8])) + String.valueOf("(") + String.valueOf(sub456.isSet("input", env[17])) + String.valueOf(", env)");
      sub456.commit();
    } catch(final GrinCodeGeneratorException e459) {
      final StringCursor sub460 = sub455.sub();
      final StringCursor sub461 = sub460.sub();
      if((Object)sub461.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptProxy == Boolean.FALSE) {
        throw sub461.ex("check failed");
      }
      try {
        env[50] /*module*/= ((org.fuwjin.chessur.Module)((org.fuwjin.chessur.expression.ScriptProxy)sub460.isSet("statement", env[19])).module()).name();
      } catch(final Exception e462) {
        throw sub460.ex(e462);
      }
      try {
        env[8] /*name*/= ((org.fuwjin.chessur.expression.ScriptImpl)((org.fuwjin.chessur.expression.ScriptProxy)sub460.isSet("statement", env[19])).script()).name();
      } catch(final Exception e463) {
        throw sub460.ex(e463);
      }
      env[20] /*result*/= String.valueOf(sub460.isSet("package", env[11])) + String.valueOf(".") + String.valueOf(sub460.isSet("module", env[50])) + String.valueOf(".") + String.valueOf(sub460.isSet("name", env[8])) + String.valueOf("(") + String.valueOf(sub460.isSet("input", env[17])) + String.valueOf(", env)");
      sub460.commit();
    }
    sub455.commit();
    return sub455.isSet("result", env[20]);
  }

  static Object Variable(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub464 = input.sub();
    final StringCursor sub465 = sub464.sub();
    if((Object)sub465.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Variable == Boolean.FALSE) {
      throw sub465.ex("check failed");
    }
    try {
      env[8] /*name*/= ((org.fuwjin.chessur.expression.Variable)sub464.isSet("value", env[14])).name();
    } catch(final Exception e466) {
      throw sub464.ex(e466);
    }
    try {
      env[9] /*index*/= ((org.fuwjin.util.NameIndex)sub464.isSet("indexer", env[1])).indexOf((java.lang.String)sub464.isSet("name", env[8]));
    } catch(final Exception e467) {
      throw sub464.ex(e467);
    }
    sub464.commit();
    return String.valueOf(sub464.isSet("input", env[17])) + String.valueOf(".isSet(\"") + String.valueOf(sub464.isSet("name", env[8])) + String.valueOf("\", env[") + String.valueOf(sub464.isSet("index", env[9])) + String.valueOf("])");
  }
  
  public static Object interpret(final CharSequence in, final Appendable out, final Map<String, ?> environment) throws GrinCodeGeneratorException {
    final StringCursor input = new StringCursor(in, out);
    final Object[] env = new Object[51];
    env[0] = environment.containsKey("varIndex") ? environment.get("varIndex") : UNSET;
    env[1] = environment.containsKey("indexer") ? environment.get("indexer") : UNSET;
    env[2] = environment.containsKey("specs") ? environment.get("specs") : UNSET;
    env[3] = environment.containsKey("cat") ? environment.get("cat") : UNSET;
    env[4] = environment.containsKey("className") ? environment.get("className") : UNSET;
    env[5] = environment.containsKey("size") ? environment.get("size") : UNSET;
    env[6] = environment.containsKey("entries") ? environment.get("entries") : UNSET;
    env[7] = environment.containsKey("entry") ? environment.get("entry") : UNSET;
    env[8] = environment.containsKey("name") ? environment.get("name") : UNSET;
    env[9] = environment.containsKey("index") ? environment.get("index") : UNSET;
    env[10] = environment.containsKey("rootName") ? environment.get("rootName") : UNSET;
    env[11] = environment.containsKey("package") ? environment.get("package") : UNSET;
    env[12] = environment.containsKey("moduleName") ? environment.get("moduleName") : UNSET;
    env[13] = environment.containsKey("spec") ? environment.get("spec") : UNSET;
    env[14] = environment.containsKey("value") ? environment.get("value") : UNSET;
    env[15] = environment.containsKey("indent") ? environment.get("indent") : UNSET;
    env[16] = environment.containsKey("subIndex") ? environment.get("subIndex") : UNSET;
    env[17] = environment.containsKey("input") ? environment.get("input") : UNSET;
    env[18] = environment.containsKey("statements") ? environment.get("statements") : UNSET;
    env[19] = environment.containsKey("statement") ? environment.get("statement") : UNSET;
    env[20] = environment.containsKey("result") ? environment.get("result") : UNSET;
    env[21] = environment.containsKey("oldInput") ? environment.get("oldInput") : UNSET;
    env[22] = environment.containsKey("bIndex") ? environment.get("bIndex") : UNSET;
    env[23] = environment.containsKey("exIndex") ? environment.get("exIndex") : UNSET;
    env[24] = environment.containsKey("exception") ? environment.get("exception") : UNSET;
    env[25] = environment.containsKey("stmt") ? environment.get("stmt") : UNSET;
    env[26] = environment.containsKey("filter") ? environment.get("filter") : UNSET;
    env[27] = environment.containsKey("notted") ? environment.get("notted") : UNSET;
    env[28] = environment.containsKey("val") ? environment.get("val") : UNSET;
    env[29] = environment.containsKey("block") ? environment.get("block") : UNSET;
    env[30] = environment.containsKey("function") ? environment.get("function") : UNSET;
    env[31] = environment.containsKey("params") ? environment.get("params") : UNSET;
    env[32] = environment.containsKey("constructor") ? environment.get("constructor") : UNSET;
    env[33] = environment.containsKey("type") ? environment.get("type") : UNSET;
    env[34] = environment.containsKey("setters") ? environment.get("setters") : UNSET;
    env[35] = environment.containsKey("field") ? environment.get("field") : UNSET;
    env[36] = environment.containsKey("method") ? environment.get("method") : UNSET;
    env[37] = environment.containsKey("types") ? environment.get("types") : UNSET;
    env[38] = environment.containsKey("render") ? environment.get("render") : UNSET;
    env[39] = environment.containsKey("filterBuffer") ? environment.get("filterBuffer") : UNSET;
    env[40] = environment.containsKey("ranges") ? environment.get("ranges") : UNSET;
    env[41] = environment.containsKey("range") ? environment.get("range") : UNSET;
    env[42] = environment.containsKey("chars") ? environment.get("chars") : UNSET;
    env[43] = environment.containsKey("ch") ? environment.get("ch") : UNSET;
    env[44] = environment.containsKey("builder") ? environment.get("builder") : UNSET;
    env[45] = environment.containsKey("composite") ? environment.get("composite") : UNSET;
    env[46] = environment.containsKey("values") ? environment.get("values") : UNSET;
    env[47] = environment.containsKey("list") ? environment.get("list") : UNSET;
    env[48] = environment.containsKey("source") ? environment.get("source") : UNSET;
    env[49] = environment.containsKey("sink") ? environment.get("sink") : UNSET;
    env[50] = environment.containsKey("module") ? environment.get("module") : UNSET;
    return Catalog(input, env);
  }
}
