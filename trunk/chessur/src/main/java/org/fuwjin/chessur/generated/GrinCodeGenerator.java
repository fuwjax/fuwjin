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
    sub17.publish(String.valueOf("\n  \n  public static Object interpret(final CharSequence in, final Appendable out, final Appendable log, final Map<String, ?> environment) throws ExecutionException {\n    final StringCursor input = new StringCursor(in, out, log);"));
    try {
      env[4] /*size*/= ((org.fuwjin.util.NameIndex)sub17.isSet("indexer", env[1])).size();
    } catch(final Exception e18) {
      throw sub17.ex(e18);
    }
    sub17.publish(String.valueOf("\n    final Object[] env = new Object[") + String.valueOf(sub17.isSet("size", env[4])) + String.valueOf("];"));
    try {
      env[5] /*entries*/= ((java.lang.Iterable)((org.fuwjin.util.NameIndex)sub17.isSet("indexer", env[1])).entries()).iterator();
    } catch(final Exception e19) {
      throw sub17.ex(e19);
    }
    try {
      final StringCursor sub20 = sub17.sub();
      try {
        env[6] /*entry*/= ((java.util.Iterator)sub20.isSet("entries", env[5])).next();
      } catch(final Exception e21) {
        throw sub20.ex(e21);
      }
      try {
        env[7] /*name*/= ((java.util.Map.Entry)sub20.isSet("entry", env[6])).getKey();
      } catch(final Exception e22) {
        throw sub20.ex(e22);
      }
      try {
        env[8] /*index*/= ((java.util.Map.Entry)sub20.isSet("entry", env[6])).getValue();
      } catch(final Exception e23) {
        throw sub20.ex(e23);
      }
      sub20.publish(String.valueOf("\n    env[") + String.valueOf(sub20.isSet("index", env[8])) + String.valueOf("] = environment.containsKey(\"") + String.valueOf(sub20.isSet("name", env[7])) + String.valueOf("\") ? environment.get(\"") + String.valueOf(sub20.isSet("name", env[7])) + String.valueOf("\") : UNSET;"));
      sub20.commit();
      try {
        while(true) {
          final StringCursor sub24 = sub17.sub();
          try {
            env[6] /*entry*/= ((java.util.Iterator)sub24.isSet("entries", env[5])).next();
          } catch(final Exception e25) {
            throw sub24.ex(e25);
          }
          try {
            env[7] /*name*/= ((java.util.Map.Entry)sub24.isSet("entry", env[6])).getKey();
          } catch(final Exception e26) {
            throw sub24.ex(e26);
          }
          try {
            env[8] /*index*/= ((java.util.Map.Entry)sub24.isSet("entry", env[6])).getValue();
          } catch(final Exception e27) {
            throw sub24.ex(e27);
          }
          sub24.publish(String.valueOf("\n    env[") + String.valueOf(sub24.isSet("index", env[8])) + String.valueOf("] = environment.containsKey(\"") + String.valueOf(sub24.isSet("name", env[7])) + String.valueOf("\") ? environment.get(\"") + String.valueOf(sub24.isSet("name", env[7])) + String.valueOf("\") : UNSET;"));
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
      if((Object)((java.util.Iterator)sub30.isSet("entries", env[5])).hasNext() == Boolean.FALSE) {
        b31 = false;
      }
    } catch(final GrinCodeGeneratorException e32) {
      b31 = false;
    }
    if(b31){
      throw sub17.ex("unexpected value");
    }
    try {
      env[9] /*rootName*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub17.isSet("cat", env[3])).rootName();
    } catch(final Exception e33) {
      throw sub17.ex(e33);
    }
    sub17.publish(String.valueOf("\n    try {\n      return ") + String.valueOf(sub17.isSet("rootName", env[9])) + String.valueOf("(input, env);\n    } catch(") + String.valueOf(sub17.isSet("className", env[10])) + String.valueOf("Exception e) {\n      throw new ExecutionException(e.getMessage(), e);\n    }\n  }\n}\n"));
    sub17.commit();
    return null;
  }

  static Object Preamble(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub34 = input.sub();
    sub34.publish(String.valueOf("/*******************************************************************************\n * Copyright (c) 2011 Michael Doberenz.\n * All rights reserved. This program and the accompanying materials\n * are made available under the terms of the Eclipse Public License v1.0\n * which accompanies this distribution, and is available at\n * http://www.eclipse.org/legal/epl-v10.html\n * \n * Contributors:\n *     Michael Doberenz - initial API and implementation\n ******************************************************************************/\npackage ") + String.valueOf(sub34.isSet("package", env[11])) + String.valueOf(";\n\nimport java.io.IOException;\nimport java.util.Map;\nimport java.util.concurrent.ExecutionException;\n\npublic class ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf(" {\n  static final Object UNSET = new Object() {\n    public String toString() {\n      return \"UNSET\";\n    }\n  };\n\n  public static class ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception extends Exception {\n    private static final long serialVersionUID = 1; \n    ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception(final String message) {\n      super(message);\n    }\n    \n    ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception(final String message, final Throwable cause) {\n      super(message, cause);\n    }\n    \n    @Override\n    public synchronized Throwable fillInStackTrace() {\n      return this;\n    }\n  }\n  \n  static class StringCursor {\n    private int pos;\n    private int line;\n    private int column;\n    private final CharSequence seq;\n    private final int start;\n    private final StringCursor parent;\n    private final Appendable appender;\n    private final Appendable log;\n    \n    public StringCursor(final CharSequence seq, final Appendable appender, final Appendable log) {\n         start = 0;\n         pos = 0;\n         this.seq = seq;\n         parent = null;\n         line = 1;\n         column = 0;\n         this.appender = appender;\n         this.log = log;\n    }\n    \n    public StringCursor(final int start, final int line, final int column, final CharSequence seq, final StringCursor parent, final Appendable log) {\n      this.start = start;\n      pos = start;\n      this.seq = seq;\n      this.parent = parent;\n      this.line = line;\n      this.column = column;\n      this.appender = new StringBuilder();\n      this.log = log;\n    }\n    \n    public int accept() throws ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception {\n      checkBounds(pos);\n      return advance();\n    }\n    \n    public int accept(final String expected) throws ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception {\n      if(expected == null || expected.length() == 0) {\n        throw ex(\"UNSET\");\n      }\n      checkBounds(pos + expected.length() - 1);\n      final CharSequence sub = seq.subSequence(pos, pos + expected.length());\n      if(!sub.equals(expected)) {\n        throw ex(\"failed while matching \"+expected);\n      }\n      final int stop = pos + expected.length() - 1;\n      while(pos < stop) {\n        advance();\n      }\n      return advance();\n    }\n    \n    public int acceptIn(final String name, final String set) throws ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception {\n      checkBounds(pos);\n      if(set.indexOf(seq.charAt(pos)) < 0) {\n        throw ex(\"Did not match filter: \"+name);\n      }\n      return advance();\n    }\n    \n    public int acceptNot(final String expected) throws ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception {\n      if(expected == null || expected.length() == 0) {\n        throw ex(\"UNSET\");\n      }\n      if(pos + expected.length() - 1 >= seq.length()) {\n        return accept();\n      }       \n      if(seq.subSequence(pos, pos + expected.length()).equals(expected)) {\n        throw ex(\"failed while matching \"+expected);\n      }\n      return advance();\n    }\n    \n    public int acceptNotIn(final String name, final String set) throws ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception {\n      checkBounds(pos);\n      if(set.indexOf(seq.charAt(pos)) >= 0) {\n        throw ex(\"Unexpected match: \"+name);\n      }\n      return advance();\n    }\n    \n    public void publish(final Object value) throws ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception {\n      try {\n        appender.append(value.toString());\n      } catch(IOException e) {\n        throw ex(e);\n      }\n    }\n    \n    public void log(final Object value) throws ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception {\n      try {\n        log.append(value.toString());\n      } catch(IOException e) {\n        throw ex(e);\n      }\n    }\n    \n    public Object isSet(final String name, final Object value) throws ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception {\n      if(UNSET.equals(value)) {\n        throw ex(\"variable \"+name+\" is unset\");\n      }\n      return value;\n    }\n    \n    protected void checkBounds(final int p) throws ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception {\n      if(p >= seq.length()) {\n        throw ex(\"unexpected EOF\");\n      }\n    }\n    \n    public void commit() {\n      commitInput();\n      commitOutput();\n    }\n    \n    void commitInput() {\n      parent.pos = pos;\n      parent.line = line;\n      parent.column = column;\n    }\n    \n    void commitOutput() {\n      appendTo(parent.appender);\n    }\n    \n    void appendTo(final Appendable dest) {\n      try {\n        dest.append(appender.toString());\n      } catch(final IOException e) {\n        throw new RuntimeException(\"IOException never thrown by StringBuilder\", e);\n      }\n    }\n    \n    public ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception ex(final String message) {\n      return new ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception(message + context());\n    }\n    \n    public String context() {\n      if(pos == 0) {\n        return \": [\" + line + \",\" + column + \"] SOF -> [1,0] SOF\";\n      }\n      if(pos > seq.length()) {\n        return \": [\" + line + \",\" + column + \"] EOF -> [1,0] SOF\";\n      }\n      return \": [\" + line + \",\" + column + \"] '\"+ seq.charAt(pos - 1)+\"' -> [1,0] SOF\";\n    }\n    \n    public ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception ex(final Throwable cause) {\n      return new ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception(context(), cause);\n    }\n    \n    public void abort(final Object message) {\n      throw new RuntimeException(message + context());\n    }\n      \n    public void abort(final Object message, final Throwable cause) {\n      throw new RuntimeException(message + context(), cause);\n    }\n    \n    private int advance() {\n      final char ch = seq.charAt(pos++);\n       if(ch == '\\n') {\n         line++;\n         column = 0;\n       } else {\n         column++;\n       }\n       return ch;\n    }\n    \n    public int next() throws ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception {\n      checkBounds(pos);\n      return seq.charAt(pos);\n    }\n    \n    public String nextStr() throws ") + String.valueOf(sub34.isSet("className", env[10])) + String.valueOf("Exception {\n      checkBounds(pos);\n      return seq.subSequence(pos,pos+1).toString();\n    }\n    \n    public StringCursor sub() {\n      return new StringCursor(pos, line, column, seq, this, log);\n    }\n    \n    public StringCursor subOutput(final StringBuilder newOutput) {\n      return new StringCursor(pos, line, column, seq, this, log) {\n        public void commit() {\n          commitInput();\n          appendTo(newOutput);\n        }\n      };\n    }\n    \n    public StringCursor subInput(final CharSequence newInput) {\n      return new StringCursor(0, 1, 0, newInput, this, log) {\n        public void commit() {\n          commitOutput();\n        }\n      };\n    }\n    \n    public String match() {\n      return seq.subSequence(start, pos).toString();\n    }\n  }"));
    sub34.commit();
    return null;
  }

  static Object ModulePreamble(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub35 = input.sub();
    sub35.publish(String.valueOf("/*******************************************************************************\n * Copyright (c) 2011 Michael Doberenz.\n * All rights reserved. This program and the accompanying materials\n * are made available under the terms of the Eclipse Public License v1.0\n * which accompanies this distribution, and is available at\n * http://www.eclipse.org/legal/epl-v10.html\n * \n * Contributors:\n *     Michael Doberenz - initial API and implementation\n ******************************************************************************/\npackage ") + String.valueOf(sub35.isSet("package", env[11])) + String.valueOf(";\n\nimport java.io.IOException;\nimport java.util.Map;\nimport java.util.concurrent.ExecutionException;\nimport static ") + String.valueOf(sub35.isSet("package", env[11])) + String.valueOf(".") + String.valueOf(sub35.isSet("className", env[10])) + String.valueOf(".UNSET;\nimport ") + String.valueOf(sub35.isSet("package", env[11])) + String.valueOf(".") + String.valueOf(sub35.isSet("className", env[10])) + String.valueOf(".") + String.valueOf(sub35.isSet("className", env[10])) + String.valueOf("Exception;\nimport ") + String.valueOf(sub35.isSet("package", env[11])) + String.valueOf(".") + String.valueOf(sub35.isSet("className", env[10])) + String.valueOf(".StringCursor;\n\npublic class ") + String.valueOf(sub35.isSet("moduleName", env[12])) + String.valueOf(" {\n"));
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
      env[7] /*name*/= ((org.fuwjin.chessur.expression.Declaration)sub36.isSet("spec", env[13])).name();
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
    sub36.publish(String.valueOf("\n") + String.valueOf(sub36.isSet("indent", env[15])) + String.valueOf("static Object ") + String.valueOf(sub36.isSet("name", env[7])) + String.valueOf("(final StringCursor input, final Object... parentEnv) throws ") + String.valueOf(sub36.isSet("className", env[10])) + String.valueOf("Exception {\n    final Object[] env = new Object[parentEnv.length];\n    System.arraycopy(parentEnv, 0, env, 0, env.length);"));
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
                      LogStatement(sub70, env);
                    } catch(final GrinCodeGeneratorException e79) {
                      try {
                        ScriptStatement(sub70, env);
                      } catch(final GrinCodeGeneratorException e80) {
                        try {
                          Block(sub70, env);
                        } catch(final GrinCodeGeneratorException e81) {
                          try {
                            Assignment(sub70, env);
                          } catch(final GrinCodeGeneratorException e82) {
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
    }
    sub70.commit();
    return null;
  }

  static Object AssumeStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub83 = input.sub();
    final StringCursor sub84 = sub83.sub();
    if((Object)sub84.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.AssumeStatement == Boolean.FALSE) {
      throw sub84.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.AssumeStatement)sub83.isSet("statement", env[19])).value();
    } catch(final Exception e85) {
      throw sub83.ex(e85);
    }
    try {
      env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub83.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e86) {
      throw sub83.ex(e86);
    }
    env[21] /*oldInput*/= sub83.isSet("input", env[17]);
    env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub83.isSet("subIndex", env[16]));
    sub83.publish(String.valueOf(sub83.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub83.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub83.isSet("oldInput", env[21])) + String.valueOf(".sub();"));
    try {
      final StringCursor sub87 = sub83.sub();
      final StringCursor sub88 = sub87.sub();
      if((Object)((org.fuwjin.chessur.expression.AssumeStatement)sub88.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub88.ex("check failed");
      }
      try {
        env[22] /*bIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub87.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e89) {
        throw sub87.ex(e89);
      }
      sub87.publish(String.valueOf(sub87.isSet("indent", env[15])) + String.valueOf("boolean b") + String.valueOf(sub87.isSet("bIndex", env[22])) + String.valueOf(" = true;"));
      sub87.publish(String.valueOf(sub87.isSet("indent", env[15])) + String.valueOf("try {"));
      try {
        ((org.fuwjin.util.Indent)sub87.isSet("indent", env[15])).increase();
      } catch(final Exception e90) {
        throw sub87.ex(e90);
      }
      sub87.publish(String.valueOf(sub87.isSet("indent", env[15])) + String.valueOf("if((Object)") + String.valueOf(Value(sub87, env)) + String.valueOf(" == Boolean.FALSE) {"));
      sub87.publish(String.valueOf(sub87.isSet("indent", env[15])) + String.valueOf("  b") + String.valueOf(sub87.isSet("bIndex", env[22])) + String.valueOf(" = false;"));
      sub87.publish(String.valueOf(sub87.isSet("indent", env[15])) + String.valueOf("}"));
      try {
        ((org.fuwjin.util.Indent)sub87.isSet("indent", env[15])).decrease();
      } catch(final Exception e91) {
        throw sub87.ex(e91);
      }
      try {
        env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub87.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e92) {
        throw sub87.ex(e92);
      }
      env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub87.isSet("exIndex", env[23]));
      sub87.publish(String.valueOf(sub87.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub87.isSet("className", env[10])) + String.valueOf("Exception ") + String.valueOf(sub87.isSet("exception", env[24])) + String.valueOf(") {"));
      sub87.publish(String.valueOf(sub87.isSet("indent", env[15])) + String.valueOf("  b") + String.valueOf(sub87.isSet("bIndex", env[22])) + String.valueOf(" = false;"));
      sub87.publish(String.valueOf(sub87.isSet("indent", env[15])) + String.valueOf("}"));
      sub87.publish(String.valueOf(sub87.isSet("indent", env[15])) + String.valueOf("if(b") + String.valueOf(sub87.isSet("bIndex", env[22])) + String.valueOf("){"));
      sub87.publish(String.valueOf(sub87.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub87.isSet("oldInput", env[21])) + String.valueOf(".ex(\"unexpected value\");"));
      sub87.publish(String.valueOf(sub87.isSet("indent", env[15])) + String.valueOf("}"));
      sub87.commit();
    } catch(final GrinCodeGeneratorException e93) {
      final StringCursor sub94 = sub83.sub();
      sub94.publish(String.valueOf(sub94.isSet("indent", env[15])) + String.valueOf("if((Object)") + String.valueOf(Value(sub94, env)) + String.valueOf(" == Boolean.FALSE) {"));
      sub94.publish(String.valueOf(sub94.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub94.isSet("input", env[17])) + String.valueOf(".ex(\"check failed\");"));
      sub94.publish(String.valueOf(sub94.isSet("indent", env[15])) + String.valueOf("}"));
      sub94.commit();
    }
    sub83.commit();
    return null;
  }

  static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub95 = input.sub();
    final StringCursor sub96 = sub95.sub();
    if((Object)sub96.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.EitherOrStatement == Boolean.FALSE) {
      throw sub96.ex("check failed");
    }
    env[25] /*stmt*/= sub95.isSet("statement", env[19]);
    try {
      env[18] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.EitherOrStatement)sub95.isSet("stmt", env[25])).statements()).iterator();
    } catch(final Exception e97) {
      throw sub95.ex(e97);
    }
    sub95.publish(String.valueOf(sub95.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub95.isSet("indent", env[15])).increase();
    } catch(final Exception e98) {
      throw sub95.ex(e98);
    }
    try {
      env[19] /*statement*/= ((java.util.Iterator)sub95.isSet("statements", env[18])).next();
    } catch(final Exception e99) {
      throw sub95.ex(e99);
    }
    Statement(sub95, env);
    try {
      ((org.fuwjin.util.Indent)sub95.isSet("indent", env[15])).decrease();
    } catch(final Exception e100) {
      throw sub95.ex(e100);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub95.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e101) {
      throw sub95.ex(e101);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub95.isSet("exIndex", env[23]));
    sub95.publish(String.valueOf(sub95.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub95.isSet("className", env[10])) + String.valueOf("Exception ") + String.valueOf(sub95.isSet("exception", env[24])) + String.valueOf(") {"));
    try {
      ((org.fuwjin.util.Indent)sub95.isSet("indent", env[15])).increase();
    } catch(final Exception e102) {
      throw sub95.ex(e102);
    }
    OrStatement(sub95, env);
    final StringCursor sub103 = sub95.sub();
    boolean b104 = true;
    try {
      if((Object)((java.util.Iterator)sub103.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        b104 = false;
      }
    } catch(final GrinCodeGeneratorException e105) {
      b104 = false;
    }
    if(b104){
      throw sub95.ex("unexpected value");
    }
    try {
      ((org.fuwjin.util.Indent)sub95.isSet("indent", env[15])).decrease();
    } catch(final Exception e106) {
      throw sub95.ex(e106);
    }
    sub95.publish(String.valueOf(sub95.isSet("indent", env[15])) + String.valueOf("}"));
    sub95.commit();
    return null;
  }

  static Object OrStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub107 = input.sub();
    try {
      env[19] /*statement*/= ((java.util.Iterator)sub107.isSet("statements", env[18])).next();
    } catch(final Exception e108) {
      throw sub107.ex(e108);
    }
    try {
      final StringCursor sub109 = sub107.sub();
      final StringCursor sub110 = sub109.sub();
      if((Object)((java.util.Iterator)sub110.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        throw sub110.ex("check failed");
      }
      sub109.publish(String.valueOf(sub109.isSet("indent", env[15])) + String.valueOf("try {"));
      try {
        ((org.fuwjin.util.Indent)sub109.isSet("indent", env[15])).increase();
      } catch(final Exception e111) {
        throw sub109.ex(e111);
      }
      Statement(sub109, env);
      try {
        ((org.fuwjin.util.Indent)sub109.isSet("indent", env[15])).decrease();
      } catch(final Exception e112) {
        throw sub109.ex(e112);
      }
      try {
        env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub109.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e113) {
        throw sub109.ex(e113);
      }
      env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub109.isSet("exIndex", env[23]));
      sub109.publish(String.valueOf(sub109.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub109.isSet("className", env[10])) + String.valueOf("Exception ") + String.valueOf(sub109.isSet("exception", env[24])) + String.valueOf(") {"));
      try {
        ((org.fuwjin.util.Indent)sub109.isSet("indent", env[15])).increase();
      } catch(final Exception e114) {
        throw sub109.ex(e114);
      }
      OrStatement(sub109, env);
      try {
        ((org.fuwjin.util.Indent)sub109.isSet("indent", env[15])).decrease();
      } catch(final Exception e115) {
        throw sub109.ex(e115);
      }
      sub109.publish(String.valueOf(sub109.isSet("indent", env[15])) + String.valueOf("}"));
      sub109.commit();
    } catch(final GrinCodeGeneratorException e116) {
      final StringCursor sub117 = sub107.sub();
      Statement(sub117, env);
      sub117.commit();
    }
    sub107.commit();
    return null;
  }

  static Object ConditionalAbortStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub118 = input.sub();
    final StringCursor sub119 = sub118.sub();
    if((Object)sub119.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ConditionalAbortStatement == Boolean.FALSE) {
      throw sub119.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.ConditionalAbortStatement)sub118.isSet("statement", env[19])).value();
    } catch(final Exception e120) {
      throw sub118.ex(e120);
    }
    env[25] /*stmt*/= sub118.isSet("statement", env[19]);
    final StringCursor sub121 = sub118.sub();
    if((Object)sub121.isSet("stmt", env[25]) instanceof org.fuwjin.chessur.expression.EitherOrStatement == Boolean.FALSE) {
      throw sub121.ex("check failed");
    }
    try {
      env[18] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.EitherOrStatement)sub118.isSet("stmt", env[25])).statements()).iterator();
    } catch(final Exception e122) {
      throw sub118.ex(e122);
    }
    sub118.publish(String.valueOf(sub118.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub118.isSet("indent", env[15])).increase();
    } catch(final Exception e123) {
      throw sub118.ex(e123);
    }
    OrStatement(sub118, env);
    final StringCursor sub124 = sub118.sub();
    boolean b125 = true;
    try {
      if((Object)((java.util.Iterator)sub124.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        b125 = false;
      }
    } catch(final GrinCodeGeneratorException e126) {
      b125 = false;
    }
    if(b125){
      throw sub118.ex("unexpected value");
    }
    try {
      ((org.fuwjin.util.Indent)sub118.isSet("indent", env[15])).decrease();
    } catch(final Exception e127) {
      throw sub118.ex(e127);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub118.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e128) {
      throw sub118.ex(e128);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub118.isSet("exIndex", env[23]));
    sub118.publish(String.valueOf(sub118.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub118.isSet("className", env[10])) + String.valueOf("Exception ") + String.valueOf(sub118.isSet("exception", env[24])) + String.valueOf(") {"));
    try {
      ((org.fuwjin.util.Indent)sub118.isSet("indent", env[15])).increase();
    } catch(final Exception e129) {
      throw sub118.ex(e129);
    }
    sub118.publish(String.valueOf(sub118.isSet("indent", env[15])) + String.valueOf(sub118.isSet("input", env[17])) + String.valueOf(".abort(") + String.valueOf(Value(sub118, env)) + String.valueOf(", ") + String.valueOf(sub118.isSet("exception", env[24])) + String.valueOf(");"));
    try {
      ((org.fuwjin.util.Indent)sub118.isSet("indent", env[15])).decrease();
    } catch(final Exception e130) {
      throw sub118.ex(e130);
    }
    sub118.publish(String.valueOf(sub118.isSet("indent", env[15])) + String.valueOf("} catch(final RuntimeException ") + String.valueOf(sub118.isSet("exception", env[24])) + String.valueOf(") {"));
    try {
      ((org.fuwjin.util.Indent)sub118.isSet("indent", env[15])).increase();
    } catch(final Exception e131) {
      throw sub118.ex(e131);
    }
    sub118.publish(String.valueOf(sub118.isSet("indent", env[15])) + String.valueOf(sub118.isSet("input", env[17])) + String.valueOf(".abort(") + String.valueOf(Value(sub118, env)) + String.valueOf(", ") + String.valueOf(sub118.isSet("exception", env[24])) + String.valueOf(");"));
    try {
      ((org.fuwjin.util.Indent)sub118.isSet("indent", env[15])).decrease();
    } catch(final Exception e132) {
      throw sub118.ex(e132);
    }
    sub118.publish(String.valueOf(sub118.isSet("indent", env[15])) + String.valueOf("}"));
    sub118.commit();
    return null;
  }

  static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub133 = input.sub();
    final StringCursor sub134 = sub133.sub();
    if((Object)sub134.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.CouldStatement == Boolean.FALSE) {
      throw sub134.ex("check failed");
    }
    env[25] /*stmt*/= sub133.isSet("statement", env[19]);
    sub133.publish(String.valueOf(sub133.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub133.isSet("indent", env[15])).increase();
    } catch(final Exception e135) {
      throw sub133.ex(e135);
    }
    try {
      env[19] /*statement*/= ((org.fuwjin.chessur.expression.CouldStatement)sub133.isSet("stmt", env[25])).statement();
    } catch(final Exception e136) {
      throw sub133.ex(e136);
    }
    Statement(sub133, env);
    try {
      ((org.fuwjin.util.Indent)sub133.isSet("indent", env[15])).decrease();
    } catch(final Exception e137) {
      throw sub133.ex(e137);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub133.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e138) {
      throw sub133.ex(e138);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub133.isSet("exIndex", env[23]));
    sub133.publish(String.valueOf(sub133.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub133.isSet("className", env[10])) + String.valueOf("Exception ") + String.valueOf(sub133.isSet("exception", env[24])) + String.valueOf(") {"));
    sub133.publish(String.valueOf(sub133.isSet("indent", env[15])) + String.valueOf("  //continue"));
    sub133.publish(String.valueOf(sub133.isSet("indent", env[15])) + String.valueOf("}"));
    sub133.commit();
    return null;
  }

  static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub139 = input.sub();
    final StringCursor sub140 = sub139.sub();
    if((Object)sub140.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.RepeatStatement == Boolean.FALSE) {
      throw sub140.ex("check failed");
    }
    env[25] /*stmt*/= sub139.isSet("statement", env[19]);
    try {
      env[19] /*statement*/= ((org.fuwjin.chessur.expression.RepeatStatement)sub139.isSet("stmt", env[25])).statement();
    } catch(final Exception e141) {
      throw sub139.ex(e141);
    }
    Statement(sub139, env);
    sub139.publish(String.valueOf(sub139.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub139.isSet("indent", env[15])).increase();
    } catch(final Exception e142) {
      throw sub139.ex(e142);
    }
    sub139.publish(String.valueOf(sub139.isSet("indent", env[15])) + String.valueOf("while(true) {"));
    try {
      ((org.fuwjin.util.Indent)sub139.isSet("indent", env[15])).increase();
    } catch(final Exception e143) {
      throw sub139.ex(e143);
    }
    Statement(sub139, env);
    try {
      ((org.fuwjin.util.Indent)sub139.isSet("indent", env[15])).decrease();
    } catch(final Exception e144) {
      throw sub139.ex(e144);
    }
    sub139.publish(String.valueOf(sub139.isSet("indent", env[15])) + String.valueOf("}"));
    try {
      ((org.fuwjin.util.Indent)sub139.isSet("indent", env[15])).decrease();
    } catch(final Exception e145) {
      throw sub139.ex(e145);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub139.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e146) {
      throw sub139.ex(e146);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub139.isSet("exIndex", env[23]));
    sub139.publish(String.valueOf(sub139.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub139.isSet("className", env[10])) + String.valueOf("Exception ") + String.valueOf(sub139.isSet("exception", env[24])) + String.valueOf(") {"));
    sub139.publish(String.valueOf(sub139.isSet("indent", env[15])) + String.valueOf("  //continue"));
    sub139.publish(String.valueOf(sub139.isSet("indent", env[15])) + String.valueOf("}"));
    sub139.commit();
    return null;
  }

  static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub147 = input.sub();
    try {
      FilterAcceptStatement(sub147, env);
    } catch(final GrinCodeGeneratorException e148) {
      ValueAcceptStatement(sub147, env);
    }
    sub147.commit();
    return null;
  }

  static Object ValueAcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub149 = input.sub();
    final StringCursor sub150 = sub149.sub();
    if((Object)sub150.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ValueAcceptStatement == Boolean.FALSE) {
      throw sub150.ex("check failed");
    }
    sub149.publish(String.valueOf(sub149.isSet("indent", env[15])) + String.valueOf(sub149.isSet("input", env[17])) + String.valueOf(".accept"));
    try {
      final StringCursor sub151 = sub149.sub();
      final StringCursor sub152 = sub151.sub();
      if((Object)((org.fuwjin.chessur.expression.ValueAcceptStatement)sub152.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub152.ex("check failed");
      }
      sub151.publish("Not");
      sub151.commit();
    } catch(final GrinCodeGeneratorException e153) {
      //continue
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.ValueAcceptStatement)sub149.isSet("statement", env[19])).value();
    } catch(final Exception e154) {
      throw sub149.ex(e154);
    }
    sub149.publish("(");
    try {
      final StringCursor sub155 = sub149.sub();
      final StringCursor sub156 = sub155.sub();
      boolean b157 = true;
      try {
        if((Object)((java.lang.Object)org.fuwjin.chessur.expression.Variable.NEXT).equals((java.lang.Object)sub156.isSet("value", env[14])) == Boolean.FALSE) {
          b157 = false;
        }
      } catch(final GrinCodeGeneratorException e158) {
        b157 = false;
      }
      if(b157){
        throw sub155.ex("unexpected value");
      }
      sub155.publish(Value(sub155, env));
      sub155.commit();
    } catch(final GrinCodeGeneratorException e159) {
      //continue
    }
    sub149.publish(");");
    sub149.commit();
    return null;
  }

  static Object FilterAcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub160 = input.sub();
    final StringCursor sub161 = sub160.sub();
    if((Object)sub161.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.FilterAcceptStatement == Boolean.FALSE) {
      throw sub161.ex("check failed");
    }
    sub160.publish(String.valueOf(sub160.isSet("indent", env[15])) + String.valueOf(sub160.isSet("input", env[17])) + String.valueOf(".accept"));
    try {
      final StringCursor sub162 = sub160.sub();
      final StringCursor sub163 = sub162.sub();
      if((Object)((org.fuwjin.chessur.expression.FilterAcceptStatement)sub163.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub163.ex("check failed");
      }
      sub162.publish(String.valueOf("Not"));
      sub162.commit();
    } catch(final GrinCodeGeneratorException e164) {
      //continue
    }
    try {
      env[26] /*filter*/= ((org.fuwjin.chessur.expression.FilterAcceptStatement)sub160.isSet("statement", env[19])).filter();
    } catch(final Exception e165) {
      throw sub160.ex(e165);
    }
    sub160.publish(String.valueOf("In(\"") + String.valueOf(sub160.isSet("filter", env[26])) + String.valueOf("\",\"") + String.valueOf(Filter(sub160, env)) + String.valueOf("\");"));
    sub160.commit();
    return null;
  }

  static Object AcceptValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub166 = input.sub();
    try {
      env[14] /*value*/= FilterAcceptValue(sub166, env);
    } catch(final GrinCodeGeneratorException e167) {
      env[14] /*value*/= ValueAcceptValue(sub166, env);
    }
    sub166.commit();
    return sub166.isSet("value", env[14]);
  }

  static Object ValueAcceptValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub168 = input.sub();
    final StringCursor sub169 = sub168.sub();
    if((Object)sub169.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ValueAcceptStatement == Boolean.FALSE) {
      throw sub169.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.ValueAcceptStatement)sub168.isSet("statement", env[19])).value();
    } catch(final Exception e170) {
      throw sub168.ex(e170);
    }
    try {
      final StringCursor sub171 = sub168.sub();
      final StringCursor sub172 = sub171.sub();
      if((Object)((org.fuwjin.chessur.expression.ValueAcceptStatement)sub172.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub172.ex("check failed");
      }
      env[27] /*notted*/= "Not";
      env[28] /*val*/= Value(sub171, env);
      sub171.commit();
    } catch(final GrinCodeGeneratorException e173) {
      final StringCursor sub174 = sub168.sub();
      env[27] /*notted*/= "";
      try {
        final StringCursor sub175 = sub174.sub();
        final StringCursor sub176 = sub175.sub();
        boolean b177 = true;
        try {
          if((Object)((java.lang.Object)org.fuwjin.chessur.expression.Variable.NEXT).equals((java.lang.Object)sub176.isSet("value", env[14])) == Boolean.FALSE) {
            b177 = false;
          }
        } catch(final GrinCodeGeneratorException e178) {
          b177 = false;
        }
        if(b177){
          throw sub175.ex("unexpected value");
        }
        env[28] /*val*/= Value(sub175, env);
        sub175.commit();
      } catch(final GrinCodeGeneratorException e179) {
        final StringCursor sub180 = sub174.sub();
        env[28] /*val*/= "";
        sub180.commit();
      }
      sub174.commit();
    }
    sub168.commit();
    return String.valueOf(sub168.isSet("input", env[17])) + String.valueOf(".accept") + String.valueOf(sub168.isSet("notted", env[27])) + String.valueOf("(") + String.valueOf(sub168.isSet("val", env[28])) + String.valueOf(")");
  }

  static Object FilterAcceptValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub181 = input.sub();
    final StringCursor sub182 = sub181.sub();
    if((Object)sub182.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.FilterAcceptStatement == Boolean.FALSE) {
      throw sub182.ex("check failed");
    }
    try {
      final StringCursor sub183 = sub181.sub();
      final StringCursor sub184 = sub183.sub();
      if((Object)((org.fuwjin.chessur.expression.FilterAcceptStatement)sub184.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub184.ex("check failed");
      }
      env[27] /*notted*/= "Not";
      sub183.commit();
    } catch(final GrinCodeGeneratorException e185) {
      final StringCursor sub186 = sub181.sub();
      env[27] /*notted*/= "";
      sub186.commit();
    }
    try {
      env[26] /*filter*/= ((org.fuwjin.chessur.expression.FilterAcceptStatement)sub181.isSet("statement", env[19])).filter();
    } catch(final Exception e187) {
      throw sub181.ex(e187);
    }
    sub181.commit();
    return String.valueOf(sub181.isSet("input", env[17])) + String.valueOf(".accept") + String.valueOf(sub181.isSet("notted", env[27])) + String.valueOf("In(\"") + String.valueOf(sub181.isSet("filter", env[26])) + String.valueOf("\",\"") + String.valueOf(Filter(sub181, env)) + String.valueOf("\")");
  }

  static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub188 = input.sub();
    final StringCursor sub189 = sub188.sub();
    if((Object)sub189.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.PublishStatement == Boolean.FALSE) {
      throw sub189.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.PublishStatement)sub188.isSet("statement", env[19])).value();
    } catch(final Exception e190) {
      throw sub188.ex(e190);
    }
    sub188.publish(String.valueOf(sub188.isSet("indent", env[15])) + String.valueOf(sub188.isSet("input", env[17])) + String.valueOf(".publish(") + String.valueOf(Value(sub188, env)) + String.valueOf(");"));
    sub188.commit();
    return null;
  }

  static Object LogStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub191 = input.sub();
    final StringCursor sub192 = sub191.sub();
    if((Object)sub192.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.LogStatement == Boolean.FALSE) {
      throw sub192.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.LogStatement)sub191.isSet("statement", env[19])).value();
    } catch(final Exception e193) {
      throw sub191.ex(e193);
    }
    sub191.publish(String.valueOf(sub191.isSet("indent", env[15])) + String.valueOf(sub191.isSet("input", env[17])) + String.valueOf(".log(") + String.valueOf(Value(sub191, env)) + String.valueOf(");"));
    sub191.commit();
    return null;
  }

  static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub194 = input.sub();
    final StringCursor sub195 = sub194.sub();
    if((Object)sub195.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.AbortStatement == Boolean.FALSE) {
      throw sub195.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.AbortStatement)sub194.isSet("statement", env[19])).value();
    } catch(final Exception e196) {
      throw sub194.ex(e196);
    }
    sub194.publish(String.valueOf(sub194.isSet("indent", env[15])) + String.valueOf(sub194.isSet("input", env[17])) + String.valueOf(".abort(") + String.valueOf(Value(sub194, env)));
    try {
      sub194.publish(String.valueOf(", ") + String.valueOf(sub194.isSet("exception", env[24])));
    } catch(final GrinCodeGeneratorException e197) {
      //continue
    }
    sub194.publish(");");
    sub194.commit();
    return null;
  }

  static Object Block(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub198 = input.sub();
    final StringCursor sub199 = sub198.sub();
    if((Object)sub199.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Block == Boolean.FALSE) {
      throw sub199.ex("check failed");
    }
    env[29] /*block*/= sub198.isSet("statement", env[19]);
    try {
      env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub198.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e200) {
      throw sub198.ex(e200);
    }
    env[21] /*oldInput*/= sub198.isSet("input", env[17]);
    env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub198.isSet("subIndex", env[16]));
    sub198.publish(String.valueOf(sub198.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub198.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub198.isSet("oldInput", env[21])) + String.valueOf(".sub();"));
    try {
      env[18] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Block)sub198.isSet("block", env[29])).statements()).iterator();
    } catch(final Exception e201) {
      throw sub198.ex(e201);
    }
    try {
      final StringCursor sub202 = sub198.sub();
      try {
        env[19] /*statement*/= ((java.util.Iterator)sub202.isSet("statements", env[18])).next();
      } catch(final Exception e203) {
        throw sub202.ex(e203);
      }
      Statement(sub202, env);
      sub202.commit();
      try {
        while(true) {
          final StringCursor sub204 = sub198.sub();
          try {
            env[19] /*statement*/= ((java.util.Iterator)sub204.isSet("statements", env[18])).next();
          } catch(final Exception e205) {
            throw sub204.ex(e205);
          }
          Statement(sub204, env);
          sub204.commit();
        }
      } catch(final GrinCodeGeneratorException e206) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e207) {
      //continue
    }
    final StringCursor sub208 = sub198.sub();
    boolean b209 = true;
    try {
      if((Object)((java.util.Iterator)sub208.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        b209 = false;
      }
    } catch(final GrinCodeGeneratorException e210) {
      b209 = false;
    }
    if(b209){
      throw sub198.ex("unexpected value");
    }
    sub198.publish(String.valueOf(sub198.isSet("indent", env[15])) + String.valueOf(sub198.isSet("input", env[17])) + String.valueOf(".commit();"));
    sub198.commit();
    return null;
  }

  static Object Assignment(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub211 = input.sub();
    final StringCursor sub212 = sub211.sub();
    if((Object)sub212.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Assignment == Boolean.FALSE) {
      throw sub212.ex("check failed");
    }
    try {
      env[7] /*name*/= ((org.fuwjin.chessur.expression.Assignment)sub211.isSet("statement", env[19])).name();
    } catch(final Exception e213) {
      throw sub211.ex(e213);
    }
    try {
      env[8] /*index*/= ((org.fuwjin.util.NameIndex)sub211.isSet("indexer", env[1])).indexOf((java.lang.String)sub211.isSet("name", env[7]));
    } catch(final Exception e214) {
      throw sub211.ex(e214);
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.Assignment)sub211.isSet("statement", env[19])).value();
    } catch(final Exception e215) {
      throw sub211.ex(e215);
    }
    try {
      final StringCursor sub216 = sub211.sub();
      final StringCursor sub217 = sub216.sub();
      if((Object)sub217.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Invocation == Boolean.FALSE) {
        throw sub217.ex("check failed");
      }
      env[19] /*statement*/= sub216.isSet("value", env[14]);
      sub216.publish(String.valueOf(sub216.isSet("indent", env[15])) + String.valueOf("try {"));
      try {
        ((org.fuwjin.util.Indent)sub216.isSet("indent", env[15])).increase();
      } catch(final Exception e218) {
        throw sub216.ex(e218);
      }
      sub216.publish(String.valueOf(sub216.isSet("indent", env[15])) + String.valueOf("env[") + String.valueOf(sub216.isSet("index", env[8])) + String.valueOf("] /*") + String.valueOf(sub216.isSet("name", env[7])) + String.valueOf("*/= ") + String.valueOf(Invocation(sub216, env)) + String.valueOf(";"));
      try {
        ((org.fuwjin.util.Indent)sub216.isSet("indent", env[15])).decrease();
      } catch(final Exception e219) {
        throw sub216.ex(e219);
      }
      try {
        env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub216.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e220) {
        throw sub216.ex(e220);
      }
      env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub216.isSet("exIndex", env[23]));
      sub216.publish(String.valueOf(sub216.isSet("indent", env[15])) + String.valueOf("} catch(final Exception ") + String.valueOf(sub216.isSet("exception", env[24])) + String.valueOf(") {"));
      sub216.publish(String.valueOf(sub216.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub216.isSet("input", env[17])) + String.valueOf(".ex(") + String.valueOf(sub216.isSet("exception", env[24])) + String.valueOf(");"));
      sub216.publish(String.valueOf(sub216.isSet("indent", env[15])) + String.valueOf("}"));
      sub216.commit();
    } catch(final GrinCodeGeneratorException e221) {
      sub211.publish(String.valueOf(sub211.isSet("indent", env[15])) + String.valueOf("env[") + String.valueOf(sub211.isSet("index", env[8])) + String.valueOf("] /*") + String.valueOf(sub211.isSet("name", env[7])) + String.valueOf("*/= ") + String.valueOf(Value(sub211, env)) + String.valueOf(";"));
    }
    sub211.commit();
    return null;
  }

  static Object InvocationStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub222 = input.sub();
    final StringCursor sub223 = sub222.sub();
    if((Object)sub223.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Invocation == Boolean.FALSE) {
      throw sub223.ex("check failed");
    }
    try {
      env[30] /*function*/= ((org.fuwjin.chessur.expression.Invocation)sub222.isSet("statement", env[19])).function();
    } catch(final Exception e224) {
      throw sub222.ex(e224);
    }
    try {
      env[31] /*params*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Invocation)sub222.isSet("statement", env[19])).params()).iterator();
    } catch(final Exception e225) {
      throw sub222.ex(e225);
    }
    sub222.publish(String.valueOf(sub222.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub222.isSet("indent", env[15])).increase();
    } catch(final Exception e226) {
      throw sub222.ex(e226);
    }
    sub222.publish(sub222.isSet("indent", env[15]));
    sub222.publish(RenderFunction(sub222, env));
    sub222.publish(";");
    try {
      ((org.fuwjin.util.Indent)sub222.isSet("indent", env[15])).decrease();
    } catch(final Exception e227) {
      throw sub222.ex(e227);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub222.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e228) {
      throw sub222.ex(e228);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub222.isSet("exIndex", env[23]));
    sub222.publish(String.valueOf(sub222.isSet("indent", env[15])) + String.valueOf("} catch(final Exception ") + String.valueOf(sub222.isSet("exception", env[24])) + String.valueOf(") {"));
    sub222.publish(String.valueOf(sub222.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub222.isSet("input", env[17])) + String.valueOf(".ex(") + String.valueOf(sub222.isSet("exception", env[24])) + String.valueOf(");"));
    sub222.publish(String.valueOf(sub222.isSet("indent", env[15])) + String.valueOf("}"));
    sub222.commit();
    return null;
  }

  static Object Invocation(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub229 = input.sub();
    final StringCursor sub230 = sub229.sub();
    if((Object)sub230.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Invocation == Boolean.FALSE) {
      throw sub230.ex("check failed");
    }
    try {
      env[30] /*function*/= ((org.fuwjin.chessur.expression.Invocation)sub229.isSet("statement", env[19])).function();
    } catch(final Exception e231) {
      throw sub229.ex(e231);
    }
    try {
      env[31] /*params*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Invocation)sub229.isSet("statement", env[19])).params()).iterator();
    } catch(final Exception e232) {
      throw sub229.ex(e232);
    }
    sub229.commit();
    return RenderFunction(sub229, env);
  }

  static Object Number(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub233 = input.sub();
    final StringCursor sub234 = sub233.sub();
    if((Object)sub234.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Number == Boolean.FALSE) {
      throw sub234.ex("check failed");
    }
    sub233.commit();
    return ((org.fuwjin.chessur.expression.Number)sub233.isSet("value", env[14])).toString();
  }

  static Object Object(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub235 = input.sub();
    final StringCursor sub236 = sub235.sub();
    if((Object)sub236.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.ObjectTemplate == Boolean.FALSE) {
      throw sub236.ex("check failed");
    }
    sub235.publish(String.valueOf("new Object() {"));
    try {
      ((org.fuwjin.util.Indent)sub235.isSet("indent", env[15])).increase();
    } catch(final Exception e237) {
      throw sub235.ex(e237);
    }
    sub235.publish(String.valueOf(sub235.isSet("indent", env[15])) + String.valueOf("public Object value() {"));
    try {
      ((org.fuwjin.util.Indent)sub235.isSet("indent", env[15])).increase();
    } catch(final Exception e238) {
      throw sub235.ex(e238);
    }
    try {
      env[30] /*function*/= ((org.fuwjin.chessur.expression.ObjectTemplate)sub235.isSet("value", env[14])).constructor();
    } catch(final Exception e239) {
      throw sub235.ex(e239);
    }
    try {
      env[32] /*constructor*/= ((org.fuwjin.dinah.function.MemberFunction)sub235.isSet("function", env[30])).member();
    } catch(final Exception e240) {
      throw sub235.ex(e240);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Constructor)sub235.isSet("constructor", env[32])).getDeclaringClass();
    } catch(final Exception e241) {
      throw sub235.ex(e241);
    }
    sub235.publish(String.valueOf(sub235.isSet("indent", env[15])) + String.valueOf(Type(sub235, env)) + String.valueOf(" value = "));
    try {
      env[31] /*params*/= ((java.lang.Iterable)java.util.Collections.emptySet()).iterator();
    } catch(final Exception e242) {
      throw sub235.ex(e242);
    }
    Constructor(sub235, env);
    sub235.publish(String.valueOf(";"));
    try {
      env[34] /*setters*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.ObjectTemplate)sub235.isSet("value", env[14])).setters()).iterator();
    } catch(final Exception e243) {
      throw sub235.ex(e243);
    }
    try {
      final StringCursor sub244 = sub235.sub();
      try {
        env[35] /*field*/= ((java.util.Iterator)sub244.isSet("setters", env[34])).next();
      } catch(final Exception e245) {
        throw sub244.ex(e245);
      }
      try {
        env[30] /*function*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub244.isSet("field", env[35])).setter();
      } catch(final Exception e246) {
        throw sub244.ex(e246);
      }
      try {
        env[14] /*value*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub244.isSet("field", env[35])).value();
      } catch(final Exception e247) {
        throw sub244.ex(e247);
      }
      try {
        MemberFieldMutator(sub244, env);
      } catch(final GrinCodeGeneratorException e248) {
        try {
          MemberMethod(sub244, env);
        } catch(final GrinCodeGeneratorException e249) {
          Composite(sub244, env);
        }
      }
      sub244.commit();
      try {
        while(true) {
          final StringCursor sub250 = sub235.sub();
          try {
            env[35] /*field*/= ((java.util.Iterator)sub250.isSet("setters", env[34])).next();
          } catch(final Exception e251) {
            throw sub250.ex(e251);
          }
          try {
            env[30] /*function*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub250.isSet("field", env[35])).setter();
          } catch(final Exception e252) {
            throw sub250.ex(e252);
          }
          try {
            env[14] /*value*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub250.isSet("field", env[35])).value();
          } catch(final Exception e253) {
            throw sub250.ex(e253);
          }
          try {
            MemberFieldMutator(sub250, env);
          } catch(final GrinCodeGeneratorException e254) {
            try {
              MemberMethod(sub250, env);
            } catch(final GrinCodeGeneratorException e255) {
              Composite(sub250, env);
            }
          }
          sub250.commit();
        }
      } catch(final GrinCodeGeneratorException e256) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e257) {
      //continue
    }
    final StringCursor sub258 = sub235.sub();
    boolean b259 = true;
    try {
      if((Object)((java.util.Iterator)sub258.isSet("setters", env[34])).hasNext() == Boolean.FALSE) {
        b259 = false;
      }
    } catch(final GrinCodeGeneratorException e260) {
      b259 = false;
    }
    if(b259){
      throw sub235.ex("unexpected value");
    }
    sub235.publish(String.valueOf(sub235.isSet("indent", env[15])) + String.valueOf("return value;"));
    try {
      ((org.fuwjin.util.Indent)sub235.isSet("indent", env[15])).decrease();
    } catch(final Exception e261) {
      throw sub235.ex(e261);
    }
    sub235.publish(String.valueOf(sub235.isSet("indent", env[15])) + String.valueOf("}"));
    try {
      ((org.fuwjin.util.Indent)sub235.isSet("indent", env[15])).decrease();
    } catch(final Exception e262) {
      throw sub235.ex(e262);
    }
    sub235.publish(String.valueOf(sub235.isSet("indent", env[15])) + String.valueOf("}.value()"));
    sub235.commit();
    return null;
  }

  static Object MemberFieldMutator(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub263 = input.sub();
    final StringCursor sub264 = sub263.sub();
    if((Object)sub264.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.FieldMutatorFunction == Boolean.FALSE) {
      throw sub264.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.MemberFunction)sub263.isSet("function", env[30])).member();
    } catch(final Exception e265) {
      throw sub263.ex(e265);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub263.isSet("field", env[35])).getType();
    } catch(final Exception e266) {
      throw sub263.ex(e266);
    }
    sub263.publish(String.valueOf(sub263.isSet("indent", env[15])) + String.valueOf("value.") + String.valueOf(((java.lang.reflect.Field)sub263.isSet("field", env[35])).getName()) + String.valueOf(" = (") + String.valueOf(Type(sub263, env)) + String.valueOf(")") + String.valueOf(Value(sub263, env)) + String.valueOf(";"));
    sub263.commit();
    return null;
  }

  static Object MemberMethod(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub267 = input.sub();
    final StringCursor sub268 = sub267.sub();
    if((Object)sub268.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.MethodFunction == Boolean.FALSE) {
      throw sub268.ex("check failed");
    }
    try {
      env[36] /*method*/= ((org.fuwjin.dinah.function.MemberFunction)sub267.isSet("function", env[30])).member();
    } catch(final Exception e269) {
      throw sub267.ex(e269);
    }
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Method)sub267.isSet("method", env[36])).getParameterTypes())).iterator();
    } catch(final Exception e270) {
      throw sub267.ex(e270);
    }
    try {
      env[33] /*type*/= ((java.util.Iterator)sub267.isSet("types", env[37])).next();
    } catch(final Exception e271) {
      throw sub267.ex(e271);
    }
    final StringCursor sub272 = sub267.sub();
    boolean b273 = true;
    try {
      if((Object)((java.util.Iterator)sub272.isSet("types", env[37])).hasNext() == Boolean.FALSE) {
        b273 = false;
      }
    } catch(final GrinCodeGeneratorException e274) {
      b273 = false;
    }
    if(b273){
      throw sub267.ex("unexpected value");
    }
    sub267.publish(String.valueOf(sub267.isSet("indent", env[15])) + String.valueOf("value.") + String.valueOf(((java.lang.reflect.Method)sub267.isSet("method", env[36])).getName()) + String.valueOf("((") + String.valueOf(Type(sub267, env)) + String.valueOf(")") + String.valueOf(Value(sub267, env)) + String.valueOf(");"));
    sub267.commit();
    return null;
  }

  static Object RenderFunction(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub275 = input.sub();
    try {
      final StringBuilder builder276 = new StringBuilder();
      final StringCursor sub277 = sub275.subOutput(builder276);
      Constructor(sub277, env);
      sub277.commit();
      env[38] /*render*/= builder276.toString();
    } catch(final GrinCodeGeneratorException e278) {
      try {
        final StringBuilder builder279 = new StringBuilder();
        final StringCursor sub280 = sub275.subOutput(builder279);
        FieldAccess(sub280, env);
        sub280.commit();
        env[38] /*render*/= builder279.toString();
      } catch(final GrinCodeGeneratorException e281) {
        try {
          final StringBuilder builder282 = new StringBuilder();
          final StringCursor sub283 = sub275.subOutput(builder282);
          FieldMutator(sub283, env);
          sub283.commit();
          env[38] /*render*/= builder282.toString();
        } catch(final GrinCodeGeneratorException e284) {
          try {
            final StringBuilder builder285 = new StringBuilder();
            final StringCursor sub286 = sub275.subOutput(builder285);
            InstanceOf(sub286, env);
            sub286.commit();
            env[38] /*render*/= builder285.toString();
          } catch(final GrinCodeGeneratorException e287) {
            try {
              final StringBuilder builder288 = new StringBuilder();
              final StringCursor sub289 = sub275.subOutput(builder288);
              Method(sub289, env);
              sub289.commit();
              env[38] /*render*/= builder288.toString();
            } catch(final GrinCodeGeneratorException e290) {
              try {
                final StringBuilder builder291 = new StringBuilder();
                final StringCursor sub292 = sub275.subOutput(builder291);
                StaticFieldAccess(sub292, env);
                sub292.commit();
                env[38] /*render*/= builder291.toString();
              } catch(final GrinCodeGeneratorException e293) {
                try {
                  final StringBuilder builder294 = new StringBuilder();
                  final StringCursor sub295 = sub275.subOutput(builder294);
                  StaticFieldMutator(sub295, env);
                  sub295.commit();
                  env[38] /*render*/= builder294.toString();
                } catch(final GrinCodeGeneratorException e296) {
                  try {
                    final StringBuilder builder297 = new StringBuilder();
                    final StringCursor sub298 = sub275.subOutput(builder297);
                    StaticMethod(sub298, env);
                    sub298.commit();
                    env[38] /*render*/= builder297.toString();
                  } catch(final GrinCodeGeneratorException e299) {
                    env[38] /*render*/= Composite(sub275, env);
                  }
                }
              }
            }
          }
        }
      }
    }
    final StringCursor sub300 = sub275.sub();
    boolean b301 = true;
    try {
      if((Object)((java.util.Iterator)sub300.isSet("params", env[31])).hasNext() == Boolean.FALSE) {
        b301 = false;
      }
    } catch(final GrinCodeGeneratorException e302) {
      b301 = false;
    }
    if(b301){
      throw sub275.ex("unexpected value");
    }
    sub275.commit();
    return sub275.isSet("render", env[38]);
  }

  static Object Constructor(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub303 = input.sub();
    final StringCursor sub304 = sub303.sub();
    if((Object)sub304.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.ConstructorFunction == Boolean.FALSE) {
      throw sub304.ex("check failed");
    }
    try {
      env[32] /*constructor*/= ((org.fuwjin.dinah.function.MemberFunction)sub303.isSet("function", env[30])).member();
    } catch(final Exception e305) {
      throw sub303.ex(e305);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Constructor)sub303.isSet("constructor", env[32])).getDeclaringClass();
    } catch(final Exception e306) {
      throw sub303.ex(e306);
    }
    sub303.publish(String.valueOf("new ") + String.valueOf(Type(sub303, env)));
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Constructor)sub303.isSet("constructor", env[32])).getParameterTypes())).iterator();
    } catch(final Exception e307) {
      throw sub303.ex(e307);
    }
    Params(sub303, env);
    sub303.commit();
    return null;
  }

  static Object FieldAccess(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub308 = input.sub();
    final StringCursor sub309 = sub308.sub();
    if((Object)sub309.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.FieldAccessFunction == Boolean.FALSE) {
      throw sub309.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.MemberFunction)sub308.isSet("function", env[30])).member();
    } catch(final Exception e310) {
      throw sub308.ex(e310);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub308.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e311) {
      throw sub308.ex(e311);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub308.isSet("params", env[31])).next();
    } catch(final Exception e312) {
      throw sub308.ex(e312);
    }
    sub308.publish(String.valueOf("((") + String.valueOf(Type(sub308, env)) + String.valueOf(")") + String.valueOf(Value(sub308, env)) + String.valueOf(").") + String.valueOf(((java.lang.reflect.Field)sub308.isSet("field", env[35])).getName()));
    sub308.commit();
    return null;
  }

  static Object FieldMutator(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub313 = input.sub();
    final StringCursor sub314 = sub313.sub();
    if((Object)sub314.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.FieldMutatorFunction == Boolean.FALSE) {
      throw sub314.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.MemberFunction)sub313.isSet("function", env[30])).member();
    } catch(final Exception e315) {
      throw sub313.ex(e315);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub313.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e316) {
      throw sub313.ex(e316);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub313.isSet("params", env[31])).next();
    } catch(final Exception e317) {
      throw sub313.ex(e317);
    }
    sub313.publish(String.valueOf("((") + String.valueOf(Type(sub313, env)) + String.valueOf(")") + String.valueOf(Value(sub313, env)) + String.valueOf(").") + String.valueOf(((java.lang.reflect.Field)sub313.isSet("field", env[35])).getName()));
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub313.isSet("field", env[35])).getType();
    } catch(final Exception e318) {
      throw sub313.ex(e318);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub313.isSet("params", env[31])).next();
    } catch(final Exception e319) {
      throw sub313.ex(e319);
    }
    sub313.publish(String.valueOf(" = (") + String.valueOf(Type(sub313, env)) + String.valueOf(")") + String.valueOf(Value(sub313, env)));
    sub313.commit();
    return null;
  }

  static Object InstanceOf(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub320 = input.sub();
    final StringCursor sub321 = sub320.sub();
    if((Object)sub321.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.InstanceOfFunction == Boolean.FALSE) {
      throw sub321.ex("check failed");
    }
    try {
      env[33] /*type*/= ((org.fuwjin.dinah.function.InstanceOfFunction)sub320.isSet("function", env[30])).type();
    } catch(final Exception e322) {
      throw sub320.ex(e322);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub320.isSet("params", env[31])).next();
    } catch(final Exception e323) {
      throw sub320.ex(e323);
    }
    sub320.publish(String.valueOf(Value(sub320, env)) + String.valueOf(" instanceof ") + String.valueOf(Type(sub320, env)));
    sub320.commit();
    return null;
  }

  static Object Method(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub324 = input.sub();
    final StringCursor sub325 = sub324.sub();
    if((Object)sub325.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.MethodFunction == Boolean.FALSE) {
      throw sub325.ex("check failed");
    }
    try {
      env[36] /*method*/= ((org.fuwjin.dinah.function.MemberFunction)sub324.isSet("function", env[30])).member();
    } catch(final Exception e326) {
      throw sub324.ex(e326);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Method)sub324.isSet("method", env[36])).getDeclaringClass();
    } catch(final Exception e327) {
      throw sub324.ex(e327);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub324.isSet("params", env[31])).next();
    } catch(final Exception e328) {
      throw sub324.ex(e328);
    }
    sub324.publish(String.valueOf("((") + String.valueOf(Type(sub324, env)) + String.valueOf(")") + String.valueOf(Value(sub324, env)) + String.valueOf(").") + String.valueOf(((java.lang.reflect.Method)sub324.isSet("method", env[36])).getName()));
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Method)sub324.isSet("method", env[36])).getParameterTypes())).iterator();
    } catch(final Exception e329) {
      throw sub324.ex(e329);
    }
    Params(sub324, env);
    sub324.commit();
    return null;
  }

  static Object StaticFieldAccess(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub330 = input.sub();
    final StringCursor sub331 = sub330.sub();
    if((Object)sub331.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.StaticFieldAccessFunction == Boolean.FALSE) {
      throw sub331.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.MemberFunction)sub330.isSet("function", env[30])).member();
    } catch(final Exception e332) {
      throw sub330.ex(e332);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub330.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e333) {
      throw sub330.ex(e333);
    }
    sub330.publish(String.valueOf(Type(sub330, env)) + String.valueOf(".") + String.valueOf(((java.lang.reflect.Field)sub330.isSet("field", env[35])).getName()));
    sub330.commit();
    return null;
  }

  static Object StaticFieldMutator(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub334 = input.sub();
    final StringCursor sub335 = sub334.sub();
    if((Object)sub335.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.StaticFieldMutatorFunction == Boolean.FALSE) {
      throw sub335.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.MemberFunction)sub334.isSet("function", env[30])).member();
    } catch(final Exception e336) {
      throw sub334.ex(e336);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub334.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e337) {
      throw sub334.ex(e337);
    }
    sub334.publish(String.valueOf(Type(sub334, env)) + String.valueOf(".") + String.valueOf(((java.lang.reflect.Field)sub334.isSet("field", env[35])).getName()));
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub334.isSet("field", env[35])).getType();
    } catch(final Exception e338) {
      throw sub334.ex(e338);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub334.isSet("params", env[31])).next();
    } catch(final Exception e339) {
      throw sub334.ex(e339);
    }
    sub334.publish(String.valueOf(" = (") + String.valueOf(Type(sub334, env)) + String.valueOf(")") + String.valueOf(Value(sub334, env)));
    sub334.commit();
    return null;
  }

  static Object StaticMethod(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub340 = input.sub();
    final StringCursor sub341 = sub340.sub();
    if((Object)sub341.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.StaticMethodFunction == Boolean.FALSE) {
      throw sub341.ex("check failed");
    }
    try {
      env[36] /*method*/= ((org.fuwjin.dinah.function.MemberFunction)sub340.isSet("function", env[30])).member();
    } catch(final Exception e342) {
      throw sub340.ex(e342);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Method)sub340.isSet("method", env[36])).getDeclaringClass();
    } catch(final Exception e343) {
      throw sub340.ex(e343);
    }
    sub340.publish(String.valueOf(Type(sub340, env)) + String.valueOf(".") + String.valueOf(((java.lang.reflect.Method)sub340.isSet("method", env[36])).getName()));
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Method)sub340.isSet("method", env[36])).getParameterTypes())).iterator();
    } catch(final Exception e344) {
      throw sub340.ex(e344);
    }
    Params(sub340, env);
    sub340.commit();
    return null;
  }

  static Object Composite(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub345 = input.sub();
    try {
      final StringCursor sub346 = sub345.sub();
      if((Object)sub346.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.CompositeFunction == Boolean.FALSE) {
        throw sub346.ex("check failed");
      }
    } catch(final GrinCodeGeneratorException e347) {
      sub345.abort(String.valueOf("Unknown function type for ") + String.valueOf(((org.fuwjin.dinah.Function)sub345.isSet("function", env[30])).signature()), e347);
    }
    try {
      env[30] /*function*/= ((org.fuwjin.dinah.function.CompositeFunction)sub345.isSet("function", env[30])).bestOption();
    } catch(final Exception e348) {
      throw sub345.ex(e348);
    }
    sub345.commit();
    return RenderFunction(sub345, env);
  }

  static Object Params(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub349 = input.sub();
    sub349.publish("(");
    try {
      final StringCursor sub350 = sub349.sub();
      try {
        env[33] /*type*/= ((java.util.Iterator)sub350.isSet("types", env[37])).next();
      } catch(final Exception e351) {
        throw sub350.ex(e351);
      }
      try {
        env[14] /*value*/= ((java.util.Iterator)sub350.isSet("params", env[31])).next();
      } catch(final Exception e352) {
        throw sub350.ex(e352);
      }
      sub350.publish(String.valueOf("(") + String.valueOf(Type(sub350, env)) + String.valueOf(")") + String.valueOf(Value(sub350, env)));
      try {
        final StringCursor sub353 = sub350.sub();
        try {
          env[33] /*type*/= ((java.util.Iterator)sub353.isSet("types", env[37])).next();
        } catch(final Exception e354) {
          throw sub353.ex(e354);
        }
        try {
          env[14] /*value*/= ((java.util.Iterator)sub353.isSet("params", env[31])).next();
        } catch(final Exception e355) {
          throw sub353.ex(e355);
        }
        sub353.publish(String.valueOf(", (") + String.valueOf(Type(sub353, env)) + String.valueOf(")") + String.valueOf(Value(sub353, env)));
        sub353.commit();
        try {
          while(true) {
            final StringCursor sub356 = sub350.sub();
            try {
              env[33] /*type*/= ((java.util.Iterator)sub356.isSet("types", env[37])).next();
            } catch(final Exception e357) {
              throw sub356.ex(e357);
            }
            try {
              env[14] /*value*/= ((java.util.Iterator)sub356.isSet("params", env[31])).next();
            } catch(final Exception e358) {
              throw sub356.ex(e358);
            }
            sub356.publish(String.valueOf(", (") + String.valueOf(Type(sub356, env)) + String.valueOf(")") + String.valueOf(Value(sub356, env)));
            sub356.commit();
          }
        } catch(final GrinCodeGeneratorException e359) {
          //continue
        }
      } catch(final GrinCodeGeneratorException e360) {
        //continue
      }
      sub350.commit();
    } catch(final GrinCodeGeneratorException e361) {
      //continue
    }
    final StringCursor sub362 = sub349.sub();
    boolean b363 = true;
    try {
      if((Object)((java.util.Iterator)sub362.isSet("types", env[37])).hasNext() == Boolean.FALSE) {
        b363 = false;
      }
    } catch(final GrinCodeGeneratorException e364) {
      b363 = false;
    }
    if(b363){
      throw sub349.ex("unexpected value");
    }
    sub349.publish(")");
    sub349.commit();
    return null;
  }

  static Object Type(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub365 = input.sub();
    sub365.commit();
    return ((java.lang.Class)org.fuwjin.util.TypeUtils.toWrapper((java.lang.reflect.Type)sub365.isSet("type", env[33]))).getCanonicalName();
  }

  static Object Filter(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub366 = input.sub();
    final StringCursor sub367 = sub366.sub();
    if((Object)sub367.isSet("filter", env[26]) instanceof org.fuwjin.chessur.expression.Filter == Boolean.FALSE) {
      throw sub367.ex("check failed");
    }
    try {
      env[39] /*filterBuffer*/= new java.lang.StringBuilder();
    } catch(final Exception e368) {
      throw sub366.ex(e368);
    }
    try {
      env[40] /*ranges*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Filter)sub366.isSet("filter", env[26])).ranges()).iterator();
    } catch(final Exception e369) {
      throw sub366.ex(e369);
    }
    try {
      env[41] /*range*/= ((java.util.Iterator)sub366.isSet("ranges", env[40])).next();
    } catch(final Exception e370) {
      throw sub366.ex(e370);
    }
    Range(sub366, env);
    try {
      final StringCursor sub371 = sub366.sub();
      try {
        env[41] /*range*/= ((java.util.Iterator)sub371.isSet("ranges", env[40])).next();
      } catch(final Exception e372) {
        throw sub371.ex(e372);
      }
      Range(sub371, env);
      sub371.commit();
      try {
        while(true) {
          final StringCursor sub373 = sub366.sub();
          try {
            env[41] /*range*/= ((java.util.Iterator)sub373.isSet("ranges", env[40])).next();
          } catch(final Exception e374) {
            throw sub373.ex(e374);
          }
          Range(sub373, env);
          sub373.commit();
        }
      } catch(final GrinCodeGeneratorException e375) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e376) {
      //continue
    }
    final StringCursor sub377 = sub366.sub();
    boolean b378 = true;
    try {
      if((Object)((java.util.Iterator)sub377.isSet("ranges", env[40])).hasNext() == Boolean.FALSE) {
        b378 = false;
      }
    } catch(final GrinCodeGeneratorException e379) {
      b378 = false;
    }
    if(b378){
      throw sub366.ex("unexpected value");
    }
    sub366.commit();
    return ((java.lang.StringBuilder)sub366.isSet("filterBuffer", env[39])).toString();
  }

  static Object Range(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub380 = input.sub();
    final StringCursor sub381 = sub380.sub();
    if((Object)sub381.isSet("range", env[41]) instanceof org.fuwjin.util.CodePointSet.Range == Boolean.FALSE) {
      throw sub381.ex("check failed");
    }
    try {
      env[42] /*chars*/= ((java.lang.Iterable)((org.fuwjin.util.CodePointSet.Range)sub380.isSet("range", env[41])).chars()).iterator();
    } catch(final Exception e382) {
      throw sub380.ex(e382);
    }
    final StringCursor sub383 = sub380.sub();
    try {
      env[43] /*ch*/= ((java.util.Iterator)sub383.isSet("chars", env[42])).next();
    } catch(final Exception e384) {
      throw sub383.ex(e384);
    }
    try {
      ((java.lang.Appendable)sub383.isSet("filterBuffer", env[39])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub383.isSet("ch", env[43])));
    } catch(final Exception e385) {
      throw sub383.ex(e385);
    }
    sub383.commit();
    try {
      while(true) {
        final StringCursor sub386 = sub380.sub();
        try {
          env[43] /*ch*/= ((java.util.Iterator)sub386.isSet("chars", env[42])).next();
        } catch(final Exception e387) {
          throw sub386.ex(e387);
        }
        try {
          ((java.lang.Appendable)sub386.isSet("filterBuffer", env[39])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub386.isSet("ch", env[43])));
        } catch(final Exception e388) {
          throw sub386.ex(e388);
        }
        sub386.commit();
      }
    } catch(final GrinCodeGeneratorException e389) {
      //continue
    }
    final StringCursor sub390 = sub380.sub();
    boolean b391 = true;
    try {
      if((Object)((java.util.Iterator)sub390.isSet("chars", env[42])).hasNext() == Boolean.FALSE) {
        b391 = false;
      }
    } catch(final GrinCodeGeneratorException e392) {
      b391 = false;
    }
    if(b391){
      throw sub380.ex("unexpected value");
    }
    sub380.commit();
    return null;
  }

  static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub393 = input.sub();
    final StringCursor sub394 = sub393.sub();
    if((Object)sub394.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Literal == Boolean.FALSE) {
      throw sub394.ex("check failed");
    }
    try {
      env[42] /*chars*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Literal)sub393.isSet("value", env[14])).chars()).iterator();
    } catch(final Exception e395) {
      throw sub393.ex(e395);
    }
    try {
      env[44] /*builder*/= new java.lang.StringBuilder();
    } catch(final Exception e396) {
      throw sub393.ex(e396);
    }
    try {
      final StringCursor sub397 = sub393.sub();
      try {
        env[43] /*ch*/= ((java.util.Iterator)sub397.isSet("chars", env[42])).next();
      } catch(final Exception e398) {
        throw sub397.ex(e398);
      }
      try {
        ((java.lang.Appendable)sub397.isSet("builder", env[44])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub397.isSet("ch", env[43])));
      } catch(final Exception e399) {
        throw sub397.ex(e399);
      }
      sub397.commit();
      try {
        while(true) {
          final StringCursor sub400 = sub393.sub();
          try {
            env[43] /*ch*/= ((java.util.Iterator)sub400.isSet("chars", env[42])).next();
          } catch(final Exception e401) {
            throw sub400.ex(e401);
          }
          try {
            ((java.lang.Appendable)sub400.isSet("builder", env[44])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub400.isSet("ch", env[43])));
          } catch(final Exception e402) {
            throw sub400.ex(e402);
          }
          sub400.commit();
        }
      } catch(final GrinCodeGeneratorException e403) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e404) {
      //continue
    }
    final StringCursor sub405 = sub393.sub();
    boolean b406 = true;
    try {
      if((Object)((java.util.Iterator)sub405.isSet("chars", env[42])).hasNext() == Boolean.FALSE) {
        b406 = false;
      }
    } catch(final GrinCodeGeneratorException e407) {
      b406 = false;
    }
    if(b406){
      throw sub393.ex("unexpected value");
    }
    sub393.commit();
    return String.valueOf("\"") + String.valueOf(sub393.isSet("builder", env[44])) + String.valueOf("\"");
  }

  static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub408 = input.sub();
    final StringCursor sub409 = sub408.sub();
    if((Object)sub409.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.CompositeLiteral == Boolean.FALSE) {
      throw sub409.ex("check failed");
    }
    env[45] /*composite*/= sub408.isSet("value", env[14]);
    try {
      env[46] /*values*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.CompositeLiteral)sub408.isSet("composite", env[45])).values()).iterator();
    } catch(final Exception e410) {
      throw sub408.ex(e410);
    }
    try {
      final StringCursor sub411 = sub408.sub();
      try {
        env[14] /*value*/= ((java.util.Iterator)sub411.isSet("values", env[46])).next();
      } catch(final Exception e412) {
        throw sub411.ex(e412);
      }
      try {
        final StringCursor sub413 = sub411.sub();
        final StringCursor sub414 = sub413.sub();
        boolean b415 = true;
        try {
          if((Object)((java.util.Iterator)sub414.isSet("values", env[46])).hasNext() == Boolean.FALSE) {
            b415 = false;
          }
        } catch(final GrinCodeGeneratorException e416) {
          b415 = false;
        }
        if(b415){
          throw sub413.ex("unexpected value");
        }
        env[20] /*result*/= String.valueOf("String.valueOf(") + String.valueOf(Value(sub413, env)) + String.valueOf(")");
        sub413.commit();
      } catch(final GrinCodeGeneratorException e417) {
        final StringCursor sub418 = sub411.sub();
        try {
          env[47] /*list*/= new java.util.ArrayList();
        } catch(final Exception e419) {
          throw sub418.ex(e419);
        }
        try {
          ((java.util.ArrayList)sub418.isSet("list", env[47])).add((java.lang.Object)String.valueOf("String.valueOf(") + String.valueOf(Value(sub418, env)) + String.valueOf(")"));
        } catch(final Exception e420) {
          throw sub418.ex(e420);
        }
        final StringCursor sub421 = sub418.sub();
        try {
          env[14] /*value*/= ((java.util.Iterator)sub421.isSet("values", env[46])).next();
        } catch(final Exception e422) {
          throw sub421.ex(e422);
        }
        try {
          ((java.util.ArrayList)sub421.isSet("list", env[47])).add((java.lang.Object)String.valueOf("String.valueOf(") + String.valueOf(Value(sub421, env)) + String.valueOf(")"));
        } catch(final Exception e423) {
          throw sub421.ex(e423);
        }
        sub421.commit();
        try {
          while(true) {
            final StringCursor sub424 = sub418.sub();
            try {
              env[14] /*value*/= ((java.util.Iterator)sub424.isSet("values", env[46])).next();
            } catch(final Exception e425) {
              throw sub424.ex(e425);
            }
            try {
              ((java.util.ArrayList)sub424.isSet("list", env[47])).add((java.lang.Object)String.valueOf("String.valueOf(") + String.valueOf(Value(sub424, env)) + String.valueOf(")"));
            } catch(final Exception e426) {
              throw sub424.ex(e426);
            }
            sub424.commit();
          }
        } catch(final GrinCodeGeneratorException e427) {
          //continue
        }
        final StringCursor sub428 = sub418.sub();
        boolean b429 = true;
        try {
          if((Object)((java.util.Iterator)sub428.isSet("values", env[46])).hasNext() == Boolean.FALSE) {
            b429 = false;
          }
        } catch(final GrinCodeGeneratorException e430) {
          b429 = false;
        }
        if(b429){
          throw sub418.ex("unexpected value");
        }
        try {
          env[20] /*result*/= org.fuwjin.util.StringUtils.join((java.lang.String)String.valueOf(" + "), (java.lang.Iterable)sub418.isSet("list", env[47]));
        } catch(final Exception e431) {
          throw sub418.ex(e431);
        }
        sub418.commit();
      }
      sub411.commit();
    } catch(final GrinCodeGeneratorException e432) {
      //continue
    }
    sub408.commit();
    return sub408.isSet("result", env[20]);
  }

  static Object NextValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub433 = input.sub();
    final StringCursor sub434 = sub433.sub();
    if((Object)((java.lang.Object)org.fuwjin.chessur.expression.Variable.NEXT).equals((java.lang.Object)sub434.isSet("value", env[14])) == Boolean.FALSE) {
      throw sub434.ex("check failed");
    }
    sub433.commit();
    return String.valueOf(sub433.isSet("input", env[17])) + String.valueOf(".next()");
  }

  static Object MatchValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub435 = input.sub();
    final StringCursor sub436 = sub435.sub();
    if((Object)((java.lang.Object)String.valueOf("match")).equals((java.lang.Object)((org.fuwjin.chessur.expression.Variable)sub436.isSet("value", env[14])).name()) == Boolean.FALSE) {
      throw sub436.ex("check failed");
    }
    sub435.commit();
    return String.valueOf(sub435.isSet("input", env[17])) + String.valueOf(".match()");
  }

  static Object ScriptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub437 = input.sub();
    try {
      sub437.publish(String.valueOf(sub437.isSet("indent", env[15])) + String.valueOf(Script(sub437, env)) + String.valueOf(";"));
    } catch(final GrinCodeGeneratorException e438) {
      try {
        final StringCursor sub439 = sub437.sub();
        final StringCursor sub440 = sub439.sub();
        if((Object)sub440.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptOutput == Boolean.FALSE) {
          throw sub440.ex("check failed");
        }
        try {
          env[7] /*name*/= ((org.fuwjin.chessur.expression.ScriptOutput)sub439.isSet("statement", env[19])).name();
        } catch(final Exception e441) {
          throw sub439.ex(e441);
        }
        try {
          env[19] /*statement*/= ((org.fuwjin.chessur.expression.ScriptOutput)sub439.isSet("statement", env[19])).spec();
        } catch(final Exception e442) {
          throw sub439.ex(e442);
        }
        try {
          env[22] /*bIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub439.isSet("varIndex", env[0])).incrementAndGet();
        } catch(final Exception e443) {
          throw sub439.ex(e443);
        }
        env[44] /*builder*/= String.valueOf("builder") + String.valueOf(sub439.isSet("bIndex", env[22]));
        sub439.publish(String.valueOf(sub439.isSet("indent", env[15])) + String.valueOf("final StringBuilder ") + String.valueOf(sub439.isSet("builder", env[44])) + String.valueOf(" = new StringBuilder();"));
        try {
          env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub439.isSet("varIndex", env[0])).incrementAndGet();
        } catch(final Exception e444) {
          throw sub439.ex(e444);
        }
        env[21] /*oldInput*/= sub439.isSet("input", env[17]);
        env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub439.isSet("subIndex", env[16]));
        sub439.publish(String.valueOf(sub439.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub439.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub439.isSet("oldInput", env[21])) + String.valueOf(".subOutput(") + String.valueOf(sub439.isSet("builder", env[44])) + String.valueOf(");"));
        ScriptStatement(sub439, env);
        sub439.publish(String.valueOf(sub439.isSet("indent", env[15])) + String.valueOf(sub439.isSet("input", env[17])) + String.valueOf(".commit();"));
        try {
          env[8] /*index*/= ((org.fuwjin.util.NameIndex)sub439.isSet("indexer", env[1])).indexOf((java.lang.String)sub439.isSet("name", env[7]));
        } catch(final Exception e445) {
          throw sub439.ex(e445);
        }
        sub439.publish(String.valueOf(sub439.isSet("indent", env[15])) + String.valueOf("env[") + String.valueOf(sub439.isSet("index", env[8])) + String.valueOf("] /*") + String.valueOf(sub439.isSet("name", env[7])) + String.valueOf("*/= ") + String.valueOf(sub439.isSet("builder", env[44])) + String.valueOf(".toString();"));
        sub439.commit();
      } catch(final GrinCodeGeneratorException e446) {
        try {
          final StringCursor sub447 = sub437.sub();
          final StringCursor sub448 = sub447.sub();
          if((Object)sub448.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptInput == Boolean.FALSE) {
            throw sub448.ex("check failed");
          }
          try {
            env[14] /*value*/= ((org.fuwjin.chessur.expression.ScriptInput)sub447.isSet("statement", env[19])).value();
          } catch(final Exception e449) {
            throw sub447.ex(e449);
          }
          try {
            env[19] /*statement*/= ((org.fuwjin.chessur.expression.ScriptInput)sub447.isSet("statement", env[19])).spec();
          } catch(final Exception e450) {
            throw sub447.ex(e450);
          }
          try {
            env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub447.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e451) {
            throw sub447.ex(e451);
          }
          env[21] /*oldInput*/= sub447.isSet("input", env[17]);
          env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub447.isSet("subIndex", env[16]));
          sub447.publish(String.valueOf(sub447.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub447.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub447.isSet("oldInput", env[21])) + String.valueOf(".subInput(String.valueOf(") + String.valueOf(Value(sub447, env)) + String.valueOf("));"));
          ScriptStatement(sub447, env);
          sub447.publish(String.valueOf(sub447.isSet("indent", env[15])) + String.valueOf(sub447.isSet("input", env[17])) + String.valueOf(".commit();"));
          sub447.commit();
        } catch(final GrinCodeGeneratorException e452) {
          final StringCursor sub453 = sub437.sub();
          final StringCursor sub454 = sub453.sub();
          if((Object)sub454.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptPipe == Boolean.FALSE) {
            throw sub454.ex("check failed");
          }
          try {
            env[48] /*source*/= ((org.fuwjin.chessur.expression.ScriptPipe)sub453.isSet("statement", env[19])).source();
          } catch(final Exception e455) {
            throw sub453.ex(e455);
          }
          try {
            env[49] /*sink*/= ((org.fuwjin.chessur.expression.ScriptPipe)sub453.isSet("statement", env[19])).sink();
          } catch(final Exception e456) {
            throw sub453.ex(e456);
          }
          try {
            env[22] /*bIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub453.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e457) {
            throw sub453.ex(e457);
          }
          env[44] /*builder*/= String.valueOf("builder") + String.valueOf(sub453.isSet("bIndex", env[22]));
          sub453.publish(String.valueOf(sub453.isSet("indent", env[15])) + String.valueOf("final StringBuilder ") + String.valueOf(sub453.isSet("builder", env[44])) + String.valueOf(" = new StringBuilder();"));
          try {
            env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub453.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e458) {
            throw sub453.ex(e458);
          }
          env[21] /*oldInput*/= sub453.isSet("input", env[17]);
          env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub453.isSet("subIndex", env[16]));
          sub453.publish(String.valueOf(sub453.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub453.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub453.isSet("oldInput", env[21])) + String.valueOf(".subOutput(") + String.valueOf(sub453.isSet("builder", env[44])) + String.valueOf(");"));
          env[19] /*statement*/= sub453.isSet("source", env[48]);
          ScriptStatement(sub453, env);
          sub453.publish(String.valueOf(sub453.isSet("indent", env[15])) + String.valueOf(sub453.isSet("input", env[17])) + String.valueOf(".commit();"));
          try {
            env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub453.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e459) {
            throw sub453.ex(e459);
          }
          env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub453.isSet("subIndex", env[16]));
          sub453.publish(String.valueOf(sub453.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub453.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub453.isSet("oldInput", env[21])) + String.valueOf(".subInput(") + String.valueOf(sub453.isSet("builder", env[44])) + String.valueOf(");"));
          env[19] /*statement*/= sub453.isSet("sink", env[49]);
          ScriptStatement(sub453, env);
          sub453.publish(String.valueOf(sub453.isSet("indent", env[15])) + String.valueOf(sub453.isSet("input", env[17])) + String.valueOf(".commit();"));
          sub453.commit();
        }
      }
    }
    sub437.commit();
    return null;
  }

  static Object Script(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub460 = input.sub();
    try {
      final StringCursor sub461 = sub460.sub();
      final StringCursor sub462 = sub461.sub();
      if((Object)sub462.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptImpl == Boolean.FALSE) {
        throw sub462.ex("check failed");
      }
      try {
        env[7] /*name*/= ((org.fuwjin.chessur.expression.ScriptImpl)sub461.isSet("statement", env[19])).name();
      } catch(final Exception e463) {
        throw sub461.ex(e463);
      }
      env[20] /*result*/= String.valueOf(sub461.isSet("name", env[7])) + String.valueOf("(") + String.valueOf(sub461.isSet("input", env[17])) + String.valueOf(", env)");
      sub461.commit();
    } catch(final GrinCodeGeneratorException e464) {
      final StringCursor sub465 = sub460.sub();
      final StringCursor sub466 = sub465.sub();
      if((Object)sub466.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptProxy == Boolean.FALSE) {
        throw sub466.ex("check failed");
      }
      try {
        env[50] /*module*/= ((org.fuwjin.chessur.Module)((org.fuwjin.chessur.expression.ScriptProxy)sub465.isSet("statement", env[19])).module()).name();
      } catch(final Exception e467) {
        throw sub465.ex(e467);
      }
      try {
        env[7] /*name*/= ((org.fuwjin.chessur.expression.ScriptImpl)((org.fuwjin.chessur.expression.ScriptProxy)sub465.isSet("statement", env[19])).script()).name();
      } catch(final Exception e468) {
        throw sub465.ex(e468);
      }
      env[20] /*result*/= String.valueOf(sub465.isSet("package", env[11])) + String.valueOf(".") + String.valueOf(sub465.isSet("module", env[50])) + String.valueOf(".") + String.valueOf(sub465.isSet("name", env[7])) + String.valueOf("(") + String.valueOf(sub465.isSet("input", env[17])) + String.valueOf(", env)");
      sub465.commit();
    }
    sub460.commit();
    return sub460.isSet("result", env[20]);
  }

  static Object Variable(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub469 = input.sub();
    final StringCursor sub470 = sub469.sub();
    if((Object)sub470.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Variable == Boolean.FALSE) {
      throw sub470.ex("check failed");
    }
    try {
      env[7] /*name*/= ((org.fuwjin.chessur.expression.Variable)sub469.isSet("value", env[14])).name();
    } catch(final Exception e471) {
      throw sub469.ex(e471);
    }
    try {
      env[8] /*index*/= ((org.fuwjin.util.NameIndex)sub469.isSet("indexer", env[1])).indexOf((java.lang.String)sub469.isSet("name", env[7]));
    } catch(final Exception e472) {
      throw sub469.ex(e472);
    }
    sub469.commit();
    return String.valueOf(sub469.isSet("input", env[17])) + String.valueOf(".isSet(\"") + String.valueOf(sub469.isSet("name", env[7])) + String.valueOf("\", env[") + String.valueOf(sub469.isSet("index", env[8])) + String.valueOf("])");
  }
  
  public static Object interpret(final CharSequence in, final Appendable out, final Map<String, ?> environment) throws GrinCodeGeneratorException {
    final StringCursor input = new StringCursor(in, out);
    final Object[] env = new Object[51];
    env[0] = environment.containsKey("varIndex") ? environment.get("varIndex") : UNSET;
    env[1] = environment.containsKey("indexer") ? environment.get("indexer") : UNSET;
    env[2] = environment.containsKey("specs") ? environment.get("specs") : UNSET;
    env[3] = environment.containsKey("cat") ? environment.get("cat") : UNSET;
    env[4] = environment.containsKey("size") ? environment.get("size") : UNSET;
    env[5] = environment.containsKey("entries") ? environment.get("entries") : UNSET;
    env[6] = environment.containsKey("entry") ? environment.get("entry") : UNSET;
    env[7] = environment.containsKey("name") ? environment.get("name") : UNSET;
    env[8] = environment.containsKey("index") ? environment.get("index") : UNSET;
    env[9] = environment.containsKey("rootName") ? environment.get("rootName") : UNSET;
    env[10] = environment.containsKey("className") ? environment.get("className") : UNSET;
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
