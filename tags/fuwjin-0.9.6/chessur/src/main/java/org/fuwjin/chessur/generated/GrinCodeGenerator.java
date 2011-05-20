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
    sub34.publish(String.valueOf("/*******************************************************************************\n * Copyright (c) 2011 Michael Doberenz.\n * All rights reserved. This program and the accompanying materials\n * are made available under the terms of the Eclipse Public License v1.0\n * which accompanies this distribution, and is available at\n * http://www.eclipse.org/legal/epl-v10.html\n * \n * Contributors:\n *     Michael Doberenz - initial API and implementation\n ******************************************************************************/\npackage ") + String.valueOf(sub34.isSet("package", env[11])) + String.valueOf(";\n\nimport java.io.IOException;\nimport java.util.Map;\n\npublic class ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf(" {\n  static final Object UNSET = new Object() {\n    public String toString() {\n      return \"UNSET\";\n    }\n  };\n\n  public static class ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception extends Exception {\n    private static final long serialVersionUID = 1; \n    ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception(final String message) {\n      super(message);\n    }\n    \n    ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception(final String message, final Throwable cause) {\n      super(message, cause);\n    }\n    \n    @Override\n    public synchronized Throwable fillInStackTrace() {\n      return this;\n    }\n  }\n  \n  static class StringCursor {\n    private int pos;\n    private int line;\n    private int column;\n    private final CharSequence seq;\n    private final int start;\n    private final StringCursor parent;\n    private final Appendable appender;\n    \n    public StringCursor(final CharSequence seq, final Appendable appender) {\n         start = 0;\n         pos = 0;\n         this.seq = seq;\n         parent = null;\n         line = 1;\n         column = 0;\n         this.appender = appender;\n    }\n    \n    public StringCursor(final int start, final int line, final int column, final CharSequence seq, final StringCursor parent) {\n      this.start = start;\n      pos = start;\n      this.seq = seq;\n      this.parent = parent;\n      this.line = line;\n      this.column = column;\n      this.appender = new StringBuilder();\n    }\n    \n    public int accept() throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      checkBounds(pos);\n      return advance();\n    }\n    \n    public int accept(final String expected) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      if(expected == null || expected.length() == 0) {\n        throw ex(\"UNSET\");\n      }\n      checkBounds(pos + expected.length() - 1);\n      final CharSequence sub = seq.subSequence(pos, pos + expected.length());\n      if(!sub.equals(expected)) {\n        throw ex(\"failed while matching \"+expected);\n      }\n      final int stop = pos + expected.length() - 1;\n      while(pos < stop) {\n        advance();\n      }\n      return advance();\n    }\n    \n    public int acceptIn(final String name, final String set) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      checkBounds(pos);\n      if(set.indexOf(seq.charAt(pos)) < 0) {\n        throw ex(\"Did not match filter: \"+name);\n      }\n      return advance();\n    }\n    \n    public int acceptNot(final String expected) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      if(expected == null || expected.length() == 0) {\n        throw ex(\"UNSET\");\n      }\n      if(pos + expected.length() - 1 >= seq.length()) {\n        return accept();\n      }       \n      if(seq.subSequence(pos, pos + expected.length()).equals(expected)) {\n        throw ex(\"failed while matching \"+expected);\n      }\n      return advance();\n    }\n    \n    public int acceptNotIn(final String name, final String set) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      checkBounds(pos);\n      if(set.indexOf(seq.charAt(pos)) >= 0) {\n        throw ex(\"Unexpected match: \"+name);\n      }\n      return advance();\n    }\n    \n    public void publish(final Object value) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      try {\n        appender.append(value.toString());\n      } catch(IOException e) {\n        throw ex(e);\n      }\n    }\n    \n    public Object isSet(final String name, final Object value) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      if(UNSET.equals(value)) {\n        throw ex(\"variable \"+name+\" is unset\");\n      }\n      return value;\n    }\n    \n    protected void checkBounds(final int p) throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      if(p >= seq.length()) {\n        throw ex(\"unexpected EOF\");\n      }\n    }\n    \n    public void commit() {\n      commitInput();\n      commitOutput();\n    }\n    \n    void commitInput() {\n      parent.pos = pos;\n      parent.line = line;\n      parent.column = column;\n    }\n    \n    void commitOutput() {\n      appendTo(parent.appender);\n    }\n    \n    void appendTo(final Appendable dest) {\n      try {\n        dest.append(appender.toString());\n      } catch(final IOException e) {\n        throw new RuntimeException(\"IOException never thrown by StringBuilder\", e);\n      }\n    }\n    \n    public ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception ex(final String message) {\n      return new ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception(message + context());\n    }\n    \n    public String context() {\n      if(pos == 0) {\n        return \": [\" + line + \",\" + column + \"] SOF -> [1,0] SOF\";\n      }\n      if(pos > seq.length()) {\n        return \": [\" + line + \",\" + column + \"] EOF -> [1,0] SOF\";\n      }\n      return \": [\" + line + \",\" + column + \"] '\"+ seq.charAt(pos - 1)+\"' -> [1,0] SOF\";\n    }\n    \n    public ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception ex(final Throwable cause) {\n      return new ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception(context(), cause);\n    }\n    \n    public void abort(final Object message) {\n      throw new RuntimeException(message + context());\n    }\n      \n    public void abort(final Object message, final Throwable cause) {\n      throw new RuntimeException(message + context(), cause);\n    }\n    \n    private int advance() {\n      final char ch = seq.charAt(pos++);\n       if(ch == '\\n') {\n         line++;\n         column = 0;\n       } else {\n         column++;\n       }\n       return ch;\n    }\n    \n    public int next() throws ") + String.valueOf(sub34.isSet("className", env[4])) + String.valueOf("Exception {\n      checkBounds(pos);\n      return seq.charAt(pos);\n    }\n    \n    public StringCursor sub() {\n      return new StringCursor(pos, line, column, seq, this);\n    }\n    \n    public StringCursor subOutput(final StringBuilder newOutput) {\n      return new StringCursor(pos, line, column, seq, this) {\n        public void commit() {\n          commitInput();\n          appendTo(newOutput);\n        }\n      };\n    }\n    \n    public StringCursor subInput(final CharSequence newInput) {\n      return new StringCursor(0, 1, 0, newInput, this) {\n        public void commit() {\n          commitOutput();\n        }\n      };\n    }\n    \n    public String match() {\n      return seq.subSequence(start, pos).toString();\n    }\n  }"));
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
          CouldStatement(sub70, env);
        } catch(final GrinCodeGeneratorException e73) {
          try {
            RepeatStatement(sub70, env);
          } catch(final GrinCodeGeneratorException e74) {
            try {
              AcceptStatement(sub70, env);
            } catch(final GrinCodeGeneratorException e75) {
              try {
                PublishStatement(sub70, env);
              } catch(final GrinCodeGeneratorException e76) {
                try {
                  AbortStatement(sub70, env);
                } catch(final GrinCodeGeneratorException e77) {
                  try {
                    ScriptStatement(sub70, env);
                  } catch(final GrinCodeGeneratorException e78) {
                    try {
                      Block(sub70, env);
                    } catch(final GrinCodeGeneratorException e79) {
                      try {
                        Assignment(sub70, env);
                      } catch(final GrinCodeGeneratorException e80) {
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
    sub70.commit();
    return null;
  }

  static Object AssumeStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub81 = input.sub();
    final StringCursor sub82 = sub81.sub();
    if((Object)sub82.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.AssumeStatement == Boolean.FALSE) {
      throw sub82.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.AssumeStatement)sub81.isSet("statement", env[19])).value();
    } catch(final Exception e83) {
      throw sub81.ex(e83);
    }
    try {
      env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub81.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e84) {
      throw sub81.ex(e84);
    }
    env[21] /*oldInput*/= sub81.isSet("input", env[17]);
    env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub81.isSet("subIndex", env[16]));
    sub81.publish(String.valueOf(sub81.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub81.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub81.isSet("oldInput", env[21])) + String.valueOf(".sub();"));
    try {
      final StringCursor sub85 = sub81.sub();
      final StringCursor sub86 = sub85.sub();
      if((Object)((org.fuwjin.chessur.expression.AssumeStatement)sub86.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub86.ex("check failed");
      }
      try {
        env[22] /*bIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub85.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e87) {
        throw sub85.ex(e87);
      }
      sub85.publish(String.valueOf(sub85.isSet("indent", env[15])) + String.valueOf("boolean b") + String.valueOf(sub85.isSet("bIndex", env[22])) + String.valueOf(" = true;"));
      sub85.publish(String.valueOf(sub85.isSet("indent", env[15])) + String.valueOf("try {"));
      try {
        ((org.fuwjin.util.Indent)sub85.isSet("indent", env[15])).increase();
      } catch(final Exception e88) {
        throw sub85.ex(e88);
      }
      sub85.publish(String.valueOf(sub85.isSet("indent", env[15])) + String.valueOf("if((Object)") + String.valueOf(Value(sub85, env)) + String.valueOf(" == Boolean.FALSE) {"));
      sub85.publish(String.valueOf(sub85.isSet("indent", env[15])) + String.valueOf("  b") + String.valueOf(sub85.isSet("bIndex", env[22])) + String.valueOf(" = false;"));
      sub85.publish(String.valueOf(sub85.isSet("indent", env[15])) + String.valueOf("}"));
      try {
        ((org.fuwjin.util.Indent)sub85.isSet("indent", env[15])).decrease();
      } catch(final Exception e89) {
        throw sub85.ex(e89);
      }
      try {
        env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub85.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e90) {
        throw sub85.ex(e90);
      }
      env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub85.isSet("exIndex", env[23]));
      sub85.publish(String.valueOf(sub85.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub85.isSet("className", env[4])) + String.valueOf("Exception ") + String.valueOf(sub85.isSet("exception", env[24])) + String.valueOf(") {"));
      sub85.publish(String.valueOf(sub85.isSet("indent", env[15])) + String.valueOf("  b") + String.valueOf(sub85.isSet("bIndex", env[22])) + String.valueOf(" = false;"));
      sub85.publish(String.valueOf(sub85.isSet("indent", env[15])) + String.valueOf("}"));
      sub85.publish(String.valueOf(sub85.isSet("indent", env[15])) + String.valueOf("if(b") + String.valueOf(sub85.isSet("bIndex", env[22])) + String.valueOf("){"));
      sub85.publish(String.valueOf(sub85.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub85.isSet("oldInput", env[21])) + String.valueOf(".ex(\"unexpected value\");"));
      sub85.publish(String.valueOf(sub85.isSet("indent", env[15])) + String.valueOf("}"));
      sub85.commit();
    } catch(final GrinCodeGeneratorException e91) {
      final StringCursor sub92 = sub81.sub();
      sub92.publish(String.valueOf(sub92.isSet("indent", env[15])) + String.valueOf("if((Object)") + String.valueOf(Value(sub92, env)) + String.valueOf(" == Boolean.FALSE) {"));
      sub92.publish(String.valueOf(sub92.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub92.isSet("input", env[17])) + String.valueOf(".ex(\"check failed\");"));
      sub92.publish(String.valueOf(sub92.isSet("indent", env[15])) + String.valueOf("}"));
      sub92.commit();
    }
    sub81.commit();
    return null;
  }

  static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub93 = input.sub();
    final StringCursor sub94 = sub93.sub();
    if((Object)sub94.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.EitherOrStatement == Boolean.FALSE) {
      throw sub94.ex("check failed");
    }
    env[25] /*stmt*/= sub93.isSet("statement", env[19]);
    try {
      env[18] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.EitherOrStatement)sub93.isSet("stmt", env[25])).statements()).iterator();
    } catch(final Exception e95) {
      throw sub93.ex(e95);
    }
    sub93.publish(String.valueOf(sub93.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub93.isSet("indent", env[15])).increase();
    } catch(final Exception e96) {
      throw sub93.ex(e96);
    }
    try {
      env[19] /*statement*/= ((java.util.Iterator)sub93.isSet("statements", env[18])).next();
    } catch(final Exception e97) {
      throw sub93.ex(e97);
    }
    Statement(sub93, env);
    try {
      ((org.fuwjin.util.Indent)sub93.isSet("indent", env[15])).decrease();
    } catch(final Exception e98) {
      throw sub93.ex(e98);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub93.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e99) {
      throw sub93.ex(e99);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub93.isSet("exIndex", env[23]));
    sub93.publish(String.valueOf(sub93.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub93.isSet("className", env[4])) + String.valueOf("Exception ") + String.valueOf(sub93.isSet("exception", env[24])) + String.valueOf(") {"));
    try {
      ((org.fuwjin.util.Indent)sub93.isSet("indent", env[15])).increase();
    } catch(final Exception e100) {
      throw sub93.ex(e100);
    }
    OrStatement(sub93, env);
    final StringCursor sub101 = sub93.sub();
    boolean b102 = true;
    try {
      if((Object)((java.util.Iterator)sub101.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        b102 = false;
      }
    } catch(final GrinCodeGeneratorException e103) {
      b102 = false;
    }
    if(b102){
      throw sub93.ex("unexpected value");
    }
    try {
      ((org.fuwjin.util.Indent)sub93.isSet("indent", env[15])).decrease();
    } catch(final Exception e104) {
      throw sub93.ex(e104);
    }
    sub93.publish(String.valueOf(sub93.isSet("indent", env[15])) + String.valueOf("}"));
    sub93.commit();
    return null;
  }

  static Object OrStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub105 = input.sub();
    try {
      env[19] /*statement*/= ((java.util.Iterator)sub105.isSet("statements", env[18])).next();
    } catch(final Exception e106) {
      throw sub105.ex(e106);
    }
    try {
      final StringCursor sub107 = sub105.sub();
      final StringCursor sub108 = sub107.sub();
      if((Object)((java.util.Iterator)sub108.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        throw sub108.ex("check failed");
      }
      sub107.publish(String.valueOf(sub107.isSet("indent", env[15])) + String.valueOf("try {"));
      try {
        ((org.fuwjin.util.Indent)sub107.isSet("indent", env[15])).increase();
      } catch(final Exception e109) {
        throw sub107.ex(e109);
      }
      Statement(sub107, env);
      try {
        ((org.fuwjin.util.Indent)sub107.isSet("indent", env[15])).decrease();
      } catch(final Exception e110) {
        throw sub107.ex(e110);
      }
      try {
        env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub107.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e111) {
        throw sub107.ex(e111);
      }
      env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub107.isSet("exIndex", env[23]));
      sub107.publish(String.valueOf(sub107.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub107.isSet("className", env[4])) + String.valueOf("Exception ") + String.valueOf(sub107.isSet("exception", env[24])) + String.valueOf(") {"));
      try {
        ((org.fuwjin.util.Indent)sub107.isSet("indent", env[15])).increase();
      } catch(final Exception e112) {
        throw sub107.ex(e112);
      }
      OrStatement(sub107, env);
      try {
        ((org.fuwjin.util.Indent)sub107.isSet("indent", env[15])).decrease();
      } catch(final Exception e113) {
        throw sub107.ex(e113);
      }
      sub107.publish(String.valueOf(sub107.isSet("indent", env[15])) + String.valueOf("}"));
      sub107.commit();
    } catch(final GrinCodeGeneratorException e114) {
      final StringCursor sub115 = sub105.sub();
      Statement(sub115, env);
      sub115.commit();
    }
    sub105.commit();
    return null;
  }

  static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub116 = input.sub();
    final StringCursor sub117 = sub116.sub();
    if((Object)sub117.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.CouldStatement == Boolean.FALSE) {
      throw sub117.ex("check failed");
    }
    env[25] /*stmt*/= sub116.isSet("statement", env[19]);
    sub116.publish(String.valueOf(sub116.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub116.isSet("indent", env[15])).increase();
    } catch(final Exception e118) {
      throw sub116.ex(e118);
    }
    try {
      env[19] /*statement*/= ((org.fuwjin.chessur.expression.CouldStatement)sub116.isSet("stmt", env[25])).statement();
    } catch(final Exception e119) {
      throw sub116.ex(e119);
    }
    Statement(sub116, env);
    try {
      ((org.fuwjin.util.Indent)sub116.isSet("indent", env[15])).decrease();
    } catch(final Exception e120) {
      throw sub116.ex(e120);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub116.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e121) {
      throw sub116.ex(e121);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub116.isSet("exIndex", env[23]));
    sub116.publish(String.valueOf(sub116.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub116.isSet("className", env[4])) + String.valueOf("Exception ") + String.valueOf(sub116.isSet("exception", env[24])) + String.valueOf(") {"));
    sub116.publish(String.valueOf(sub116.isSet("indent", env[15])) + String.valueOf("  //continue"));
    sub116.publish(String.valueOf(sub116.isSet("indent", env[15])) + String.valueOf("}"));
    sub116.commit();
    return null;
  }

  static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub122 = input.sub();
    final StringCursor sub123 = sub122.sub();
    if((Object)sub123.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.RepeatStatement == Boolean.FALSE) {
      throw sub123.ex("check failed");
    }
    env[25] /*stmt*/= sub122.isSet("statement", env[19]);
    try {
      env[19] /*statement*/= ((org.fuwjin.chessur.expression.RepeatStatement)sub122.isSet("stmt", env[25])).statement();
    } catch(final Exception e124) {
      throw sub122.ex(e124);
    }
    Statement(sub122, env);
    sub122.publish(String.valueOf(sub122.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub122.isSet("indent", env[15])).increase();
    } catch(final Exception e125) {
      throw sub122.ex(e125);
    }
    sub122.publish(String.valueOf(sub122.isSet("indent", env[15])) + String.valueOf("while(true) {"));
    try {
      ((org.fuwjin.util.Indent)sub122.isSet("indent", env[15])).increase();
    } catch(final Exception e126) {
      throw sub122.ex(e126);
    }
    Statement(sub122, env);
    try {
      ((org.fuwjin.util.Indent)sub122.isSet("indent", env[15])).decrease();
    } catch(final Exception e127) {
      throw sub122.ex(e127);
    }
    sub122.publish(String.valueOf(sub122.isSet("indent", env[15])) + String.valueOf("}"));
    try {
      ((org.fuwjin.util.Indent)sub122.isSet("indent", env[15])).decrease();
    } catch(final Exception e128) {
      throw sub122.ex(e128);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub122.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e129) {
      throw sub122.ex(e129);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub122.isSet("exIndex", env[23]));
    sub122.publish(String.valueOf(sub122.isSet("indent", env[15])) + String.valueOf("} catch(final ") + String.valueOf(sub122.isSet("className", env[4])) + String.valueOf("Exception ") + String.valueOf(sub122.isSet("exception", env[24])) + String.valueOf(") {"));
    sub122.publish(String.valueOf(sub122.isSet("indent", env[15])) + String.valueOf("  //continue"));
    sub122.publish(String.valueOf(sub122.isSet("indent", env[15])) + String.valueOf("}"));
    sub122.commit();
    return null;
  }

  static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub130 = input.sub();
    try {
      FilterAcceptStatement(sub130, env);
    } catch(final GrinCodeGeneratorException e131) {
      ValueAcceptStatement(sub130, env);
    }
    sub130.commit();
    return null;
  }

  static Object ValueAcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub132 = input.sub();
    final StringCursor sub133 = sub132.sub();
    if((Object)sub133.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ValueAcceptStatement == Boolean.FALSE) {
      throw sub133.ex("check failed");
    }
    sub132.publish(String.valueOf(sub132.isSet("indent", env[15])) + String.valueOf(sub132.isSet("input", env[17])) + String.valueOf(".accept"));
    try {
      final StringCursor sub134 = sub132.sub();
      final StringCursor sub135 = sub134.sub();
      if((Object)((org.fuwjin.chessur.expression.ValueAcceptStatement)sub135.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub135.ex("check failed");
      }
      sub134.publish("Not");
      sub134.commit();
    } catch(final GrinCodeGeneratorException e136) {
      //continue
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.ValueAcceptStatement)sub132.isSet("statement", env[19])).value();
    } catch(final Exception e137) {
      throw sub132.ex(e137);
    }
    sub132.publish("(");
    try {
      final StringCursor sub138 = sub132.sub();
      final StringCursor sub139 = sub138.sub();
      boolean b140 = true;
      try {
        if((Object)((java.lang.Object)org.fuwjin.chessur.expression.Variable.NEXT).equals((java.lang.Object)sub139.isSet("value", env[14])) == Boolean.FALSE) {
          b140 = false;
        }
      } catch(final GrinCodeGeneratorException e141) {
        b140 = false;
      }
      if(b140){
        throw sub138.ex("unexpected value");
      }
      sub138.publish(Value(sub138, env));
      sub138.commit();
    } catch(final GrinCodeGeneratorException e142) {
      //continue
    }
    sub132.publish(");");
    sub132.commit();
    return null;
  }

  static Object FilterAcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub143 = input.sub();
    final StringCursor sub144 = sub143.sub();
    if((Object)sub144.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.FilterAcceptStatement == Boolean.FALSE) {
      throw sub144.ex("check failed");
    }
    sub143.publish(String.valueOf(sub143.isSet("indent", env[15])) + String.valueOf(sub143.isSet("input", env[17])) + String.valueOf(".accept"));
    try {
      final StringCursor sub145 = sub143.sub();
      final StringCursor sub146 = sub145.sub();
      if((Object)((org.fuwjin.chessur.expression.FilterAcceptStatement)sub146.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub146.ex("check failed");
      }
      sub145.publish(String.valueOf("Not"));
      sub145.commit();
    } catch(final GrinCodeGeneratorException e147) {
      //continue
    }
    try {
      env[26] /*filter*/= ((org.fuwjin.chessur.expression.FilterAcceptStatement)sub143.isSet("statement", env[19])).filter();
    } catch(final Exception e148) {
      throw sub143.ex(e148);
    }
    sub143.publish(String.valueOf("In(\"") + String.valueOf(sub143.isSet("filter", env[26])) + String.valueOf("\",\"") + String.valueOf(Filter(sub143, env)) + String.valueOf("\");"));
    sub143.commit();
    return null;
  }

  static Object AcceptValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub149 = input.sub();
    try {
      env[14] /*value*/= FilterAcceptValue(sub149, env);
    } catch(final GrinCodeGeneratorException e150) {
      env[14] /*value*/= ValueAcceptValue(sub149, env);
    }
    sub149.commit();
    return sub149.isSet("value", env[14]);
  }

  static Object ValueAcceptValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub151 = input.sub();
    final StringCursor sub152 = sub151.sub();
    if((Object)sub152.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ValueAcceptStatement == Boolean.FALSE) {
      throw sub152.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.ValueAcceptStatement)sub151.isSet("statement", env[19])).value();
    } catch(final Exception e153) {
      throw sub151.ex(e153);
    }
    try {
      final StringCursor sub154 = sub151.sub();
      final StringCursor sub155 = sub154.sub();
      if((Object)((org.fuwjin.chessur.expression.ValueAcceptStatement)sub155.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub155.ex("check failed");
      }
      env[27] /*notted*/= "Not";
      env[28] /*val*/= Value(sub154, env);
      sub154.commit();
    } catch(final GrinCodeGeneratorException e156) {
      final StringCursor sub157 = sub151.sub();
      env[27] /*notted*/= "";
      try {
        final StringCursor sub158 = sub157.sub();
        final StringCursor sub159 = sub158.sub();
        boolean b160 = true;
        try {
          if((Object)((java.lang.Object)org.fuwjin.chessur.expression.Variable.NEXT).equals((java.lang.Object)sub159.isSet("value", env[14])) == Boolean.FALSE) {
            b160 = false;
          }
        } catch(final GrinCodeGeneratorException e161) {
          b160 = false;
        }
        if(b160){
          throw sub158.ex("unexpected value");
        }
        env[28] /*val*/= Value(sub158, env);
        sub158.commit();
      } catch(final GrinCodeGeneratorException e162) {
        final StringCursor sub163 = sub157.sub();
        env[28] /*val*/= "";
        sub163.commit();
      }
      sub157.commit();
    }
    sub151.commit();
    return String.valueOf(sub151.isSet("input", env[17])) + String.valueOf(".accept") + String.valueOf(sub151.isSet("notted", env[27])) + String.valueOf("(") + String.valueOf(sub151.isSet("val", env[28])) + String.valueOf(")");
  }

  static Object FilterAcceptValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub164 = input.sub();
    final StringCursor sub165 = sub164.sub();
    if((Object)sub165.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.FilterAcceptStatement == Boolean.FALSE) {
      throw sub165.ex("check failed");
    }
    try {
      final StringCursor sub166 = sub164.sub();
      final StringCursor sub167 = sub166.sub();
      if((Object)((org.fuwjin.chessur.expression.FilterAcceptStatement)sub167.isSet("statement", env[19])).isNot() == Boolean.FALSE) {
        throw sub167.ex("check failed");
      }
      env[27] /*notted*/= "Not";
      sub166.commit();
    } catch(final GrinCodeGeneratorException e168) {
      final StringCursor sub169 = sub164.sub();
      env[27] /*notted*/= "";
      sub169.commit();
    }
    try {
      env[26] /*filter*/= ((org.fuwjin.chessur.expression.FilterAcceptStatement)sub164.isSet("statement", env[19])).filter();
    } catch(final Exception e170) {
      throw sub164.ex(e170);
    }
    sub164.commit();
    return String.valueOf(sub164.isSet("input", env[17])) + String.valueOf(".accept") + String.valueOf(sub164.isSet("notted", env[27])) + String.valueOf("In(\"") + String.valueOf(sub164.isSet("filter", env[26])) + String.valueOf("\",\"") + String.valueOf(Filter(sub164, env)) + String.valueOf("\")");
  }

  static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub171 = input.sub();
    final StringCursor sub172 = sub171.sub();
    if((Object)sub172.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.PublishStatement == Boolean.FALSE) {
      throw sub172.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.PublishStatement)sub171.isSet("statement", env[19])).value();
    } catch(final Exception e173) {
      throw sub171.ex(e173);
    }
    sub171.publish(String.valueOf(sub171.isSet("indent", env[15])) + String.valueOf(sub171.isSet("input", env[17])) + String.valueOf(".publish(") + String.valueOf(Value(sub171, env)) + String.valueOf(");"));
    sub171.commit();
    return null;
  }

  static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub174 = input.sub();
    final StringCursor sub175 = sub174.sub();
    if((Object)sub175.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.AbortStatement == Boolean.FALSE) {
      throw sub175.ex("check failed");
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.AbortStatement)sub174.isSet("statement", env[19])).value();
    } catch(final Exception e176) {
      throw sub174.ex(e176);
    }
    sub174.publish(String.valueOf(sub174.isSet("indent", env[15])) + String.valueOf(sub174.isSet("input", env[17])) + String.valueOf(".abort(") + String.valueOf(Value(sub174, env)));
    try {
      sub174.publish(String.valueOf(", ") + String.valueOf(sub174.isSet("exception", env[24])));
    } catch(final GrinCodeGeneratorException e177) {
      //continue
    }
    sub174.publish(");");
    sub174.commit();
    return null;
  }

  static Object Block(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub178 = input.sub();
    final StringCursor sub179 = sub178.sub();
    if((Object)sub179.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Block == Boolean.FALSE) {
      throw sub179.ex("check failed");
    }
    env[29] /*block*/= sub178.isSet("statement", env[19]);
    try {
      env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub178.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e180) {
      throw sub178.ex(e180);
    }
    env[21] /*oldInput*/= sub178.isSet("input", env[17]);
    env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub178.isSet("subIndex", env[16]));
    sub178.publish(String.valueOf(sub178.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub178.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub178.isSet("oldInput", env[21])) + String.valueOf(".sub();"));
    try {
      env[18] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Block)sub178.isSet("block", env[29])).statements()).iterator();
    } catch(final Exception e181) {
      throw sub178.ex(e181);
    }
    try {
      final StringCursor sub182 = sub178.sub();
      try {
        env[19] /*statement*/= ((java.util.Iterator)sub182.isSet("statements", env[18])).next();
      } catch(final Exception e183) {
        throw sub182.ex(e183);
      }
      Statement(sub182, env);
      sub182.commit();
      try {
        while(true) {
          final StringCursor sub184 = sub178.sub();
          try {
            env[19] /*statement*/= ((java.util.Iterator)sub184.isSet("statements", env[18])).next();
          } catch(final Exception e185) {
            throw sub184.ex(e185);
          }
          Statement(sub184, env);
          sub184.commit();
        }
      } catch(final GrinCodeGeneratorException e186) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e187) {
      //continue
    }
    final StringCursor sub188 = sub178.sub();
    boolean b189 = true;
    try {
      if((Object)((java.util.Iterator)sub188.isSet("statements", env[18])).hasNext() == Boolean.FALSE) {
        b189 = false;
      }
    } catch(final GrinCodeGeneratorException e190) {
      b189 = false;
    }
    if(b189){
      throw sub178.ex("unexpected value");
    }
    sub178.publish(String.valueOf(sub178.isSet("indent", env[15])) + String.valueOf(sub178.isSet("input", env[17])) + String.valueOf(".commit();"));
    sub178.commit();
    return null;
  }

  static Object Assignment(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub191 = input.sub();
    final StringCursor sub192 = sub191.sub();
    if((Object)sub192.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Assignment == Boolean.FALSE) {
      throw sub192.ex("check failed");
    }
    try {
      env[8] /*name*/= ((org.fuwjin.chessur.expression.Assignment)sub191.isSet("statement", env[19])).name();
    } catch(final Exception e193) {
      throw sub191.ex(e193);
    }
    try {
      env[9] /*index*/= ((org.fuwjin.util.NameIndex)sub191.isSet("indexer", env[1])).indexOf((java.lang.String)sub191.isSet("name", env[8]));
    } catch(final Exception e194) {
      throw sub191.ex(e194);
    }
    try {
      env[14] /*value*/= ((org.fuwjin.chessur.expression.Assignment)sub191.isSet("statement", env[19])).value();
    } catch(final Exception e195) {
      throw sub191.ex(e195);
    }
    try {
      final StringCursor sub196 = sub191.sub();
      final StringCursor sub197 = sub196.sub();
      if((Object)sub197.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Invocation == Boolean.FALSE) {
        throw sub197.ex("check failed");
      }
      env[19] /*statement*/= sub196.isSet("value", env[14]);
      sub196.publish(String.valueOf(sub196.isSet("indent", env[15])) + String.valueOf("try {"));
      try {
        ((org.fuwjin.util.Indent)sub196.isSet("indent", env[15])).increase();
      } catch(final Exception e198) {
        throw sub196.ex(e198);
      }
      sub196.publish(String.valueOf(sub196.isSet("indent", env[15])) + String.valueOf("env[") + String.valueOf(sub196.isSet("index", env[9])) + String.valueOf("] /*") + String.valueOf(sub196.isSet("name", env[8])) + String.valueOf("*/= ") + String.valueOf(Invocation(sub196, env)) + String.valueOf(";"));
      try {
        ((org.fuwjin.util.Indent)sub196.isSet("indent", env[15])).decrease();
      } catch(final Exception e199) {
        throw sub196.ex(e199);
      }
      try {
        env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub196.isSet("varIndex", env[0])).incrementAndGet();
      } catch(final Exception e200) {
        throw sub196.ex(e200);
      }
      env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub196.isSet("exIndex", env[23]));
      sub196.publish(String.valueOf(sub196.isSet("indent", env[15])) + String.valueOf("} catch(final Exception ") + String.valueOf(sub196.isSet("exception", env[24])) + String.valueOf(") {"));
      sub196.publish(String.valueOf(sub196.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub196.isSet("input", env[17])) + String.valueOf(".ex(") + String.valueOf(sub196.isSet("exception", env[24])) + String.valueOf(");"));
      sub196.publish(String.valueOf(sub196.isSet("indent", env[15])) + String.valueOf("}"));
      sub196.commit();
    } catch(final GrinCodeGeneratorException e201) {
      sub191.publish(String.valueOf(sub191.isSet("indent", env[15])) + String.valueOf("env[") + String.valueOf(sub191.isSet("index", env[9])) + String.valueOf("] /*") + String.valueOf(sub191.isSet("name", env[8])) + String.valueOf("*/= ") + String.valueOf(Value(sub191, env)) + String.valueOf(";"));
    }
    sub191.commit();
    return null;
  }

  static Object InvocationStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub202 = input.sub();
    final StringCursor sub203 = sub202.sub();
    if((Object)sub203.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Invocation == Boolean.FALSE) {
      throw sub203.ex("check failed");
    }
    try {
      env[30] /*function*/= ((org.fuwjin.chessur.expression.Invocation)sub202.isSet("statement", env[19])).function();
    } catch(final Exception e204) {
      throw sub202.ex(e204);
    }
    try {
      env[31] /*params*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Invocation)sub202.isSet("statement", env[19])).params()).iterator();
    } catch(final Exception e205) {
      throw sub202.ex(e205);
    }
    sub202.publish(String.valueOf(sub202.isSet("indent", env[15])) + String.valueOf("try {"));
    try {
      ((org.fuwjin.util.Indent)sub202.isSet("indent", env[15])).increase();
    } catch(final Exception e206) {
      throw sub202.ex(e206);
    }
    sub202.publish(sub202.isSet("indent", env[15]));
    sub202.publish(RenderFunction(sub202, env));
    sub202.publish(";");
    try {
      ((org.fuwjin.util.Indent)sub202.isSet("indent", env[15])).decrease();
    } catch(final Exception e207) {
      throw sub202.ex(e207);
    }
    try {
      env[23] /*exIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub202.isSet("varIndex", env[0])).incrementAndGet();
    } catch(final Exception e208) {
      throw sub202.ex(e208);
    }
    env[24] /*exception*/= String.valueOf("e") + String.valueOf(sub202.isSet("exIndex", env[23]));
    sub202.publish(String.valueOf(sub202.isSet("indent", env[15])) + String.valueOf("} catch(final Exception ") + String.valueOf(sub202.isSet("exception", env[24])) + String.valueOf(") {"));
    sub202.publish(String.valueOf(sub202.isSet("indent", env[15])) + String.valueOf("  throw ") + String.valueOf(sub202.isSet("input", env[17])) + String.valueOf(".ex(") + String.valueOf(sub202.isSet("exception", env[24])) + String.valueOf(");"));
    sub202.publish(String.valueOf(sub202.isSet("indent", env[15])) + String.valueOf("}"));
    sub202.commit();
    return null;
  }

  static Object Invocation(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub209 = input.sub();
    final StringCursor sub210 = sub209.sub();
    if((Object)sub210.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.Invocation == Boolean.FALSE) {
      throw sub210.ex("check failed");
    }
    try {
      env[30] /*function*/= ((org.fuwjin.chessur.expression.Invocation)sub209.isSet("statement", env[19])).function();
    } catch(final Exception e211) {
      throw sub209.ex(e211);
    }
    try {
      env[31] /*params*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Invocation)sub209.isSet("statement", env[19])).params()).iterator();
    } catch(final Exception e212) {
      throw sub209.ex(e212);
    }
    sub209.commit();
    return RenderFunction(sub209, env);
  }

  static Object Number(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub213 = input.sub();
    final StringCursor sub214 = sub213.sub();
    if((Object)sub214.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Number == Boolean.FALSE) {
      throw sub214.ex("check failed");
    }
    sub213.commit();
    return ((org.fuwjin.chessur.expression.Number)sub213.isSet("value", env[14])).toString();
  }

  static Object Object(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub215 = input.sub();
    final StringCursor sub216 = sub215.sub();
    if((Object)sub216.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.ObjectTemplate == Boolean.FALSE) {
      throw sub216.ex("check failed");
    }
    sub215.publish(String.valueOf("new Object() {"));
    try {
      ((org.fuwjin.util.Indent)sub215.isSet("indent", env[15])).increase();
    } catch(final Exception e217) {
      throw sub215.ex(e217);
    }
    sub215.publish(String.valueOf(sub215.isSet("indent", env[15])) + String.valueOf("public Object value() {"));
    try {
      ((org.fuwjin.util.Indent)sub215.isSet("indent", env[15])).increase();
    } catch(final Exception e218) {
      throw sub215.ex(e218);
    }
    try {
      env[30] /*function*/= ((org.fuwjin.chessur.expression.ObjectTemplate)sub215.isSet("value", env[14])).constructor();
    } catch(final Exception e219) {
      throw sub215.ex(e219);
    }
    try {
      env[32] /*constructor*/= ((org.fuwjin.dinah.function.FixedArgsFunction)sub215.isSet("function", env[30])).member();
    } catch(final Exception e220) {
      throw sub215.ex(e220);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Constructor)sub215.isSet("constructor", env[32])).getDeclaringClass();
    } catch(final Exception e221) {
      throw sub215.ex(e221);
    }
    sub215.publish(String.valueOf(sub215.isSet("indent", env[15])) + String.valueOf(Type(sub215, env)) + String.valueOf(" value = "));
    try {
      env[31] /*params*/= ((java.lang.Iterable)java.util.Collections.emptySet()).iterator();
    } catch(final Exception e222) {
      throw sub215.ex(e222);
    }
    Constructor(sub215, env);
    sub215.publish(String.valueOf(";"));
    try {
      env[34] /*setters*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.ObjectTemplate)sub215.isSet("value", env[14])).setters()).iterator();
    } catch(final Exception e223) {
      throw sub215.ex(e223);
    }
    try {
      final StringCursor sub224 = sub215.sub();
      try {
        env[35] /*field*/= ((java.util.Iterator)sub224.isSet("setters", env[34])).next();
      } catch(final Exception e225) {
        throw sub224.ex(e225);
      }
      try {
        env[30] /*function*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub224.isSet("field", env[35])).setter();
      } catch(final Exception e226) {
        throw sub224.ex(e226);
      }
      try {
        env[14] /*value*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub224.isSet("field", env[35])).value();
      } catch(final Exception e227) {
        throw sub224.ex(e227);
      }
      try {
        MemberFieldMutator(sub224, env);
      } catch(final GrinCodeGeneratorException e228) {
        try {
          MemberMethod(sub224, env);
        } catch(final GrinCodeGeneratorException e229) {
          VarArgs(sub224, env);
        }
      }
      sub224.commit();
      try {
        while(true) {
          final StringCursor sub230 = sub215.sub();
          try {
            env[35] /*field*/= ((java.util.Iterator)sub230.isSet("setters", env[34])).next();
          } catch(final Exception e231) {
            throw sub230.ex(e231);
          }
          try {
            env[30] /*function*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub230.isSet("field", env[35])).setter();
          } catch(final Exception e232) {
            throw sub230.ex(e232);
          }
          try {
            env[14] /*value*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub230.isSet("field", env[35])).value();
          } catch(final Exception e233) {
            throw sub230.ex(e233);
          }
          try {
            MemberFieldMutator(sub230, env);
          } catch(final GrinCodeGeneratorException e234) {
            try {
              MemberMethod(sub230, env);
            } catch(final GrinCodeGeneratorException e235) {
              VarArgs(sub230, env);
            }
          }
          sub230.commit();
        }
      } catch(final GrinCodeGeneratorException e236) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e237) {
      //continue
    }
    final StringCursor sub238 = sub215.sub();
    boolean b239 = true;
    try {
      if((Object)((java.util.Iterator)sub238.isSet("setters", env[34])).hasNext() == Boolean.FALSE) {
        b239 = false;
      }
    } catch(final GrinCodeGeneratorException e240) {
      b239 = false;
    }
    if(b239){
      throw sub215.ex("unexpected value");
    }
    sub215.publish(String.valueOf(sub215.isSet("indent", env[15])) + String.valueOf("return value;"));
    try {
      ((org.fuwjin.util.Indent)sub215.isSet("indent", env[15])).decrease();
    } catch(final Exception e241) {
      throw sub215.ex(e241);
    }
    sub215.publish(String.valueOf(sub215.isSet("indent", env[15])) + String.valueOf("}"));
    try {
      ((org.fuwjin.util.Indent)sub215.isSet("indent", env[15])).decrease();
    } catch(final Exception e242) {
      throw sub215.ex(e242);
    }
    sub215.publish(String.valueOf(sub215.isSet("indent", env[15])) + String.valueOf("}.value()"));
    sub215.commit();
    return null;
  }

  static Object MemberFieldMutator(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub243 = input.sub();
    final StringCursor sub244 = sub243.sub();
    if((Object)sub244.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.FieldMutatorFunction == Boolean.FALSE) {
      throw sub244.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.FixedArgsFunction)sub243.isSet("function", env[30])).member();
    } catch(final Exception e245) {
      throw sub243.ex(e245);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub243.isSet("field", env[35])).getType();
    } catch(final Exception e246) {
      throw sub243.ex(e246);
    }
    sub243.publish(String.valueOf(sub243.isSet("indent", env[15])) + String.valueOf("value.") + String.valueOf(((java.lang.reflect.Field)sub243.isSet("field", env[35])).getName()) + String.valueOf(" = (") + String.valueOf(Type(sub243, env)) + String.valueOf(")") + String.valueOf(Value(sub243, env)) + String.valueOf(";"));
    sub243.commit();
    return null;
  }

  static Object MemberMethod(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub247 = input.sub();
    final StringCursor sub248 = sub247.sub();
    if((Object)sub248.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.MethodFunction == Boolean.FALSE) {
      throw sub248.ex("check failed");
    }
    try {
      env[36] /*method*/= ((org.fuwjin.dinah.function.FixedArgsFunction)sub247.isSet("function", env[30])).member();
    } catch(final Exception e249) {
      throw sub247.ex(e249);
    }
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Method)sub247.isSet("method", env[36])).getParameterTypes())).iterator();
    } catch(final Exception e250) {
      throw sub247.ex(e250);
    }
    try {
      env[33] /*type*/= ((java.util.Iterator)sub247.isSet("types", env[37])).next();
    } catch(final Exception e251) {
      throw sub247.ex(e251);
    }
    final StringCursor sub252 = sub247.sub();
    boolean b253 = true;
    try {
      if((Object)((java.util.Iterator)sub252.isSet("types", env[37])).hasNext() == Boolean.FALSE) {
        b253 = false;
      }
    } catch(final GrinCodeGeneratorException e254) {
      b253 = false;
    }
    if(b253){
      throw sub247.ex("unexpected value");
    }
    sub247.publish(String.valueOf(sub247.isSet("indent", env[15])) + String.valueOf("value.") + String.valueOf(((java.lang.reflect.Method)sub247.isSet("method", env[36])).getName()) + String.valueOf("((") + String.valueOf(Type(sub247, env)) + String.valueOf(")") + String.valueOf(Value(sub247, env)) + String.valueOf(");"));
    sub247.commit();
    return null;
  }

  static Object RenderFunction(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub255 = input.sub();
    try {
      final StringBuilder builder256 = new StringBuilder();
      final StringCursor sub257 = sub255.subOutput(builder256);
      Constructor(sub257, env);
      sub257.commit();
      env[38] /*render*/= builder256.toString();
    } catch(final GrinCodeGeneratorException e258) {
      try {
        final StringBuilder builder259 = new StringBuilder();
        final StringCursor sub260 = sub255.subOutput(builder259);
        FieldAccess(sub260, env);
        sub260.commit();
        env[38] /*render*/= builder259.toString();
      } catch(final GrinCodeGeneratorException e261) {
        try {
          final StringBuilder builder262 = new StringBuilder();
          final StringCursor sub263 = sub255.subOutput(builder262);
          FieldMutator(sub263, env);
          sub263.commit();
          env[38] /*render*/= builder262.toString();
        } catch(final GrinCodeGeneratorException e264) {
          try {
            final StringBuilder builder265 = new StringBuilder();
            final StringCursor sub266 = sub255.subOutput(builder265);
            InstanceOf(sub266, env);
            sub266.commit();
            env[38] /*render*/= builder265.toString();
          } catch(final GrinCodeGeneratorException e267) {
            try {
              final StringBuilder builder268 = new StringBuilder();
              final StringCursor sub269 = sub255.subOutput(builder268);
              Method(sub269, env);
              sub269.commit();
              env[38] /*render*/= builder268.toString();
            } catch(final GrinCodeGeneratorException e270) {
              try {
                final StringBuilder builder271 = new StringBuilder();
                final StringCursor sub272 = sub255.subOutput(builder271);
                StaticFieldAccess(sub272, env);
                sub272.commit();
                env[38] /*render*/= builder271.toString();
              } catch(final GrinCodeGeneratorException e273) {
                try {
                  final StringBuilder builder274 = new StringBuilder();
                  final StringCursor sub275 = sub255.subOutput(builder274);
                  StaticFieldMutator(sub275, env);
                  sub275.commit();
                  env[38] /*render*/= builder274.toString();
                } catch(final GrinCodeGeneratorException e276) {
                  try {
                    final StringBuilder builder277 = new StringBuilder();
                    final StringCursor sub278 = sub255.subOutput(builder277);
                    StaticMethod(sub278, env);
                    sub278.commit();
                    env[38] /*render*/= builder277.toString();
                  } catch(final GrinCodeGeneratorException e279) {
                    final StringBuilder builder280 = new StringBuilder();
                    final StringCursor sub281 = sub255.subOutput(builder280);
                    VarArgs(sub281, env);
                    sub281.commit();
                    env[38] /*render*/= builder280.toString();
                  }
                }
              }
            }
          }
        }
      }
    }
    final StringCursor sub282 = sub255.sub();
    boolean b283 = true;
    try {
      if((Object)((java.util.Iterator)sub282.isSet("params", env[31])).hasNext() == Boolean.FALSE) {
        b283 = false;
      }
    } catch(final GrinCodeGeneratorException e284) {
      b283 = false;
    }
    if(b283){
      throw sub255.ex("unexpected value");
    }
    sub255.commit();
    return sub255.isSet("render", env[38]);
  }

  static Object Constructor(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub285 = input.sub();
    final StringCursor sub286 = sub285.sub();
    if((Object)sub286.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.ConstructorFunction == Boolean.FALSE) {
      throw sub286.ex("check failed");
    }
    try {
      env[32] /*constructor*/= ((org.fuwjin.dinah.function.FixedArgsFunction)sub285.isSet("function", env[30])).member();
    } catch(final Exception e287) {
      throw sub285.ex(e287);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Constructor)sub285.isSet("constructor", env[32])).getDeclaringClass();
    } catch(final Exception e288) {
      throw sub285.ex(e288);
    }
    sub285.publish(String.valueOf("new ") + String.valueOf(Type(sub285, env)));
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Constructor)sub285.isSet("constructor", env[32])).getParameterTypes())).iterator();
    } catch(final Exception e289) {
      throw sub285.ex(e289);
    }
    Params(sub285, env);
    sub285.commit();
    return null;
  }

  static Object FieldAccess(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub290 = input.sub();
    final StringCursor sub291 = sub290.sub();
    if((Object)sub291.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.FieldAccessFunction == Boolean.FALSE) {
      throw sub291.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.FixedArgsFunction)sub290.isSet("function", env[30])).member();
    } catch(final Exception e292) {
      throw sub290.ex(e292);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub290.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e293) {
      throw sub290.ex(e293);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub290.isSet("params", env[31])).next();
    } catch(final Exception e294) {
      throw sub290.ex(e294);
    }
    sub290.publish(String.valueOf("((") + String.valueOf(Type(sub290, env)) + String.valueOf(")") + String.valueOf(Value(sub290, env)) + String.valueOf(").") + String.valueOf(((java.lang.reflect.Field)sub290.isSet("field", env[35])).getName()));
    sub290.commit();
    return null;
  }

  static Object FieldMutator(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub295 = input.sub();
    final StringCursor sub296 = sub295.sub();
    if((Object)sub296.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.FieldMutatorFunction == Boolean.FALSE) {
      throw sub296.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.FixedArgsFunction)sub295.isSet("function", env[30])).member();
    } catch(final Exception e297) {
      throw sub295.ex(e297);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub295.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e298) {
      throw sub295.ex(e298);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub295.isSet("params", env[31])).next();
    } catch(final Exception e299) {
      throw sub295.ex(e299);
    }
    sub295.publish(String.valueOf("((") + String.valueOf(Type(sub295, env)) + String.valueOf(")") + String.valueOf(Value(sub295, env)) + String.valueOf(").") + String.valueOf(((java.lang.reflect.Field)sub295.isSet("field", env[35])).getName()));
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub295.isSet("field", env[35])).getType();
    } catch(final Exception e300) {
      throw sub295.ex(e300);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub295.isSet("params", env[31])).next();
    } catch(final Exception e301) {
      throw sub295.ex(e301);
    }
    sub295.publish(String.valueOf(" = (") + String.valueOf(Type(sub295, env)) + String.valueOf(")") + String.valueOf(Value(sub295, env)));
    sub295.commit();
    return null;
  }

  static Object InstanceOf(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub302 = input.sub();
    final StringCursor sub303 = sub302.sub();
    if((Object)sub303.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.InstanceOfFunction == Boolean.FALSE) {
      throw sub303.ex("check failed");
    }
    try {
      env[33] /*type*/= ((org.fuwjin.dinah.function.InstanceOfFunction)sub302.isSet("function", env[30])).type();
    } catch(final Exception e304) {
      throw sub302.ex(e304);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub302.isSet("params", env[31])).next();
    } catch(final Exception e305) {
      throw sub302.ex(e305);
    }
    sub302.publish(String.valueOf(Value(sub302, env)) + String.valueOf(" instanceof ") + String.valueOf(Type(sub302, env)));
    sub302.commit();
    return null;
  }

  static Object Method(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub306 = input.sub();
    final StringCursor sub307 = sub306.sub();
    if((Object)sub307.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.MethodFunction == Boolean.FALSE) {
      throw sub307.ex("check failed");
    }
    try {
      env[36] /*method*/= ((org.fuwjin.dinah.function.FixedArgsFunction)sub306.isSet("function", env[30])).member();
    } catch(final Exception e308) {
      throw sub306.ex(e308);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Method)sub306.isSet("method", env[36])).getDeclaringClass();
    } catch(final Exception e309) {
      throw sub306.ex(e309);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub306.isSet("params", env[31])).next();
    } catch(final Exception e310) {
      throw sub306.ex(e310);
    }
    sub306.publish(String.valueOf("((") + String.valueOf(Type(sub306, env)) + String.valueOf(")") + String.valueOf(Value(sub306, env)) + String.valueOf(").") + String.valueOf(((java.lang.reflect.Method)sub306.isSet("method", env[36])).getName()));
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Method)sub306.isSet("method", env[36])).getParameterTypes())).iterator();
    } catch(final Exception e311) {
      throw sub306.ex(e311);
    }
    Params(sub306, env);
    sub306.commit();
    return null;
  }

  static Object StaticFieldAccess(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub312 = input.sub();
    final StringCursor sub313 = sub312.sub();
    if((Object)sub313.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.StaticFieldAccessFunction == Boolean.FALSE) {
      throw sub313.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.FixedArgsFunction)sub312.isSet("function", env[30])).member();
    } catch(final Exception e314) {
      throw sub312.ex(e314);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub312.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e315) {
      throw sub312.ex(e315);
    }
    sub312.publish(String.valueOf(Type(sub312, env)) + String.valueOf(".") + String.valueOf(((java.lang.reflect.Field)sub312.isSet("field", env[35])).getName()));
    sub312.commit();
    return null;
  }

  static Object StaticFieldMutator(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub316 = input.sub();
    final StringCursor sub317 = sub316.sub();
    if((Object)sub317.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.StaticFieldMutatorFunction == Boolean.FALSE) {
      throw sub317.ex("check failed");
    }
    try {
      env[35] /*field*/= ((org.fuwjin.dinah.function.FixedArgsFunction)sub316.isSet("function", env[30])).member();
    } catch(final Exception e318) {
      throw sub316.ex(e318);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub316.isSet("field", env[35])).getDeclaringClass();
    } catch(final Exception e319) {
      throw sub316.ex(e319);
    }
    sub316.publish(String.valueOf(Type(sub316, env)) + String.valueOf(".") + String.valueOf(((java.lang.reflect.Field)sub316.isSet("field", env[35])).getName()));
    try {
      env[33] /*type*/= ((java.lang.reflect.Field)sub316.isSet("field", env[35])).getType();
    } catch(final Exception e320) {
      throw sub316.ex(e320);
    }
    try {
      env[14] /*value*/= ((java.util.Iterator)sub316.isSet("params", env[31])).next();
    } catch(final Exception e321) {
      throw sub316.ex(e321);
    }
    sub316.publish(String.valueOf(" = (") + String.valueOf(Type(sub316, env)) + String.valueOf(")") + String.valueOf(Value(sub316, env)));
    sub316.commit();
    return null;
  }

  static Object StaticMethod(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub322 = input.sub();
    final StringCursor sub323 = sub322.sub();
    if((Object)sub323.isSet("function", env[30]) instanceof org.fuwjin.dinah.function.StaticMethodFunction == Boolean.FALSE) {
      throw sub323.ex("check failed");
    }
    try {
      env[36] /*method*/= ((org.fuwjin.dinah.function.FixedArgsFunction)sub322.isSet("function", env[30])).member();
    } catch(final Exception e324) {
      throw sub322.ex(e324);
    }
    try {
      env[33] /*type*/= ((java.lang.reflect.Method)sub322.isSet("method", env[36])).getDeclaringClass();
    } catch(final Exception e325) {
      throw sub322.ex(e325);
    }
    sub322.publish(String.valueOf(Type(sub322, env)) + String.valueOf(".") + String.valueOf(((java.lang.reflect.Method)sub322.isSet("method", env[36])).getName()));
    try {
      env[37] /*types*/= ((java.lang.Iterable)java.util.Arrays.asList((java.lang.Object[])((java.lang.reflect.Method)sub322.isSet("method", env[36])).getParameterTypes())).iterator();
    } catch(final Exception e326) {
      throw sub322.ex(e326);
    }
    Params(sub322, env);
    sub322.commit();
    return null;
  }

  static Object VarArgs(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub327 = input.sub();
    sub327.abort(String.valueOf("VarArgs is currently unsupported: ") + String.valueOf(((org.fuwjin.dinah.Function)sub327.isSet("function", env[30])).name()));
    sub327.commit();
    return null;
  }

  static Object Params(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub328 = input.sub();
    sub328.publish("(");
    try {
      final StringCursor sub329 = sub328.sub();
      try {
        env[33] /*type*/= ((java.util.Iterator)sub329.isSet("types", env[37])).next();
      } catch(final Exception e330) {
        throw sub329.ex(e330);
      }
      try {
        env[14] /*value*/= ((java.util.Iterator)sub329.isSet("params", env[31])).next();
      } catch(final Exception e331) {
        throw sub329.ex(e331);
      }
      sub329.publish(String.valueOf("(") + String.valueOf(Type(sub329, env)) + String.valueOf(")") + String.valueOf(Value(sub329, env)));
      try {
        final StringCursor sub332 = sub329.sub();
        try {
          env[33] /*type*/= ((java.util.Iterator)sub332.isSet("types", env[37])).next();
        } catch(final Exception e333) {
          throw sub332.ex(e333);
        }
        try {
          env[14] /*value*/= ((java.util.Iterator)sub332.isSet("params", env[31])).next();
        } catch(final Exception e334) {
          throw sub332.ex(e334);
        }
        sub332.publish(String.valueOf(", (") + String.valueOf(Type(sub332, env)) + String.valueOf(")") + String.valueOf(Value(sub332, env)));
        sub332.commit();
        try {
          while(true) {
            final StringCursor sub335 = sub329.sub();
            try {
              env[33] /*type*/= ((java.util.Iterator)sub335.isSet("types", env[37])).next();
            } catch(final Exception e336) {
              throw sub335.ex(e336);
            }
            try {
              env[14] /*value*/= ((java.util.Iterator)sub335.isSet("params", env[31])).next();
            } catch(final Exception e337) {
              throw sub335.ex(e337);
            }
            sub335.publish(String.valueOf(", (") + String.valueOf(Type(sub335, env)) + String.valueOf(")") + String.valueOf(Value(sub335, env)));
            sub335.commit();
          }
        } catch(final GrinCodeGeneratorException e338) {
          //continue
        }
      } catch(final GrinCodeGeneratorException e339) {
        //continue
      }
      sub329.commit();
    } catch(final GrinCodeGeneratorException e340) {
      //continue
    }
    final StringCursor sub341 = sub328.sub();
    boolean b342 = true;
    try {
      if((Object)((java.util.Iterator)sub341.isSet("types", env[37])).hasNext() == Boolean.FALSE) {
        b342 = false;
      }
    } catch(final GrinCodeGeneratorException e343) {
      b342 = false;
    }
    if(b342){
      throw sub328.ex("unexpected value");
    }
    sub328.publish(")");
    sub328.commit();
    return null;
  }

  static Object Type(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub344 = input.sub();
    sub344.commit();
    return ((java.lang.Class)org.fuwjin.util.TypeUtils.toWrapper((java.lang.reflect.Type)sub344.isSet("type", env[33]))).getCanonicalName();
  }

  static Object Filter(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub345 = input.sub();
    final StringCursor sub346 = sub345.sub();
    if((Object)sub346.isSet("filter", env[26]) instanceof org.fuwjin.chessur.expression.Filter == Boolean.FALSE) {
      throw sub346.ex("check failed");
    }
    try {
      env[39] /*filterBuffer*/= new java.lang.StringBuilder();
    } catch(final Exception e347) {
      throw sub345.ex(e347);
    }
    try {
      env[40] /*ranges*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Filter)sub345.isSet("filter", env[26])).ranges()).iterator();
    } catch(final Exception e348) {
      throw sub345.ex(e348);
    }
    try {
      env[41] /*range*/= ((java.util.Iterator)sub345.isSet("ranges", env[40])).next();
    } catch(final Exception e349) {
      throw sub345.ex(e349);
    }
    Range(sub345, env);
    try {
      final StringCursor sub350 = sub345.sub();
      try {
        env[41] /*range*/= ((java.util.Iterator)sub350.isSet("ranges", env[40])).next();
      } catch(final Exception e351) {
        throw sub350.ex(e351);
      }
      Range(sub350, env);
      sub350.commit();
      try {
        while(true) {
          final StringCursor sub352 = sub345.sub();
          try {
            env[41] /*range*/= ((java.util.Iterator)sub352.isSet("ranges", env[40])).next();
          } catch(final Exception e353) {
            throw sub352.ex(e353);
          }
          Range(sub352, env);
          sub352.commit();
        }
      } catch(final GrinCodeGeneratorException e354) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e355) {
      //continue
    }
    final StringCursor sub356 = sub345.sub();
    boolean b357 = true;
    try {
      if((Object)((java.util.Iterator)sub356.isSet("ranges", env[40])).hasNext() == Boolean.FALSE) {
        b357 = false;
      }
    } catch(final GrinCodeGeneratorException e358) {
      b357 = false;
    }
    if(b357){
      throw sub345.ex("unexpected value");
    }
    sub345.commit();
    return ((java.lang.StringBuilder)sub345.isSet("filterBuffer", env[39])).toString();
  }

  static Object Range(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub359 = input.sub();
    final StringCursor sub360 = sub359.sub();
    if((Object)sub360.isSet("range", env[41]) instanceof org.fuwjin.util.CodePointSet.Range == Boolean.FALSE) {
      throw sub360.ex("check failed");
    }
    try {
      env[42] /*chars*/= ((java.lang.Iterable)((org.fuwjin.util.CodePointSet.Range)sub359.isSet("range", env[41])).chars()).iterator();
    } catch(final Exception e361) {
      throw sub359.ex(e361);
    }
    final StringCursor sub362 = sub359.sub();
    try {
      env[43] /*ch*/= ((java.util.Iterator)sub362.isSet("chars", env[42])).next();
    } catch(final Exception e363) {
      throw sub362.ex(e363);
    }
    try {
      ((java.lang.Appendable)sub362.isSet("filterBuffer", env[39])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub362.isSet("ch", env[43])));
    } catch(final Exception e364) {
      throw sub362.ex(e364);
    }
    sub362.commit();
    try {
      while(true) {
        final StringCursor sub365 = sub359.sub();
        try {
          env[43] /*ch*/= ((java.util.Iterator)sub365.isSet("chars", env[42])).next();
        } catch(final Exception e366) {
          throw sub365.ex(e366);
        }
        try {
          ((java.lang.Appendable)sub365.isSet("filterBuffer", env[39])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub365.isSet("ch", env[43])));
        } catch(final Exception e367) {
          throw sub365.ex(e367);
        }
        sub365.commit();
      }
    } catch(final GrinCodeGeneratorException e368) {
      //continue
    }
    final StringCursor sub369 = sub359.sub();
    boolean b370 = true;
    try {
      if((Object)((java.util.Iterator)sub369.isSet("chars", env[42])).hasNext() == Boolean.FALSE) {
        b370 = false;
      }
    } catch(final GrinCodeGeneratorException e371) {
      b370 = false;
    }
    if(b370){
      throw sub359.ex("unexpected value");
    }
    sub359.commit();
    return null;
  }

  static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub372 = input.sub();
    final StringCursor sub373 = sub372.sub();
    if((Object)sub373.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Literal == Boolean.FALSE) {
      throw sub373.ex("check failed");
    }
    try {
      env[42] /*chars*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Literal)sub372.isSet("value", env[14])).chars()).iterator();
    } catch(final Exception e374) {
      throw sub372.ex(e374);
    }
    try {
      env[44] /*builder*/= new java.lang.StringBuilder();
    } catch(final Exception e375) {
      throw sub372.ex(e375);
    }
    try {
      final StringCursor sub376 = sub372.sub();
      try {
        env[43] /*ch*/= ((java.util.Iterator)sub376.isSet("chars", env[42])).next();
      } catch(final Exception e377) {
        throw sub376.ex(e377);
      }
      try {
        ((java.lang.Appendable)sub376.isSet("builder", env[44])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub376.isSet("ch", env[43])));
      } catch(final Exception e378) {
        throw sub376.ex(e378);
      }
      sub376.commit();
      try {
        while(true) {
          final StringCursor sub379 = sub372.sub();
          try {
            env[43] /*ch*/= ((java.util.Iterator)sub379.isSet("chars", env[42])).next();
          } catch(final Exception e380) {
            throw sub379.ex(e380);
          }
          try {
            ((java.lang.Appendable)sub379.isSet("builder", env[44])).append((java.lang.CharSequence)org.fuwjin.chessur.expression.Literal.dynamicEscape((java.lang.Integer)sub379.isSet("ch", env[43])));
          } catch(final Exception e381) {
            throw sub379.ex(e381);
          }
          sub379.commit();
        }
      } catch(final GrinCodeGeneratorException e382) {
        //continue
      }
    } catch(final GrinCodeGeneratorException e383) {
      //continue
    }
    final StringCursor sub384 = sub372.sub();
    boolean b385 = true;
    try {
      if((Object)((java.util.Iterator)sub384.isSet("chars", env[42])).hasNext() == Boolean.FALSE) {
        b385 = false;
      }
    } catch(final GrinCodeGeneratorException e386) {
      b385 = false;
    }
    if(b385){
      throw sub372.ex("unexpected value");
    }
    sub372.commit();
    return String.valueOf("\"") + String.valueOf(sub372.isSet("builder", env[44])) + String.valueOf("\"");
  }

  static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub387 = input.sub();
    final StringCursor sub388 = sub387.sub();
    if((Object)sub388.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.CompositeLiteral == Boolean.FALSE) {
      throw sub388.ex("check failed");
    }
    env[45] /*composite*/= sub387.isSet("value", env[14]);
    try {
      env[46] /*values*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.CompositeLiteral)sub387.isSet("composite", env[45])).values()).iterator();
    } catch(final Exception e389) {
      throw sub387.ex(e389);
    }
    try {
      final StringCursor sub390 = sub387.sub();
      try {
        env[14] /*value*/= ((java.util.Iterator)sub390.isSet("values", env[46])).next();
      } catch(final Exception e391) {
        throw sub390.ex(e391);
      }
      try {
        final StringCursor sub392 = sub390.sub();
        final StringCursor sub393 = sub392.sub();
        boolean b394 = true;
        try {
          if((Object)((java.util.Iterator)sub393.isSet("values", env[46])).hasNext() == Boolean.FALSE) {
            b394 = false;
          }
        } catch(final GrinCodeGeneratorException e395) {
          b394 = false;
        }
        if(b394){
          throw sub392.ex("unexpected value");
        }
        env[20] /*result*/= String.valueOf("String.valueOf(") + String.valueOf(Value(sub392, env)) + String.valueOf(")");
        sub392.commit();
      } catch(final GrinCodeGeneratorException e396) {
        final StringCursor sub397 = sub390.sub();
        try {
          env[47] /*list*/= new java.util.ArrayList();
        } catch(final Exception e398) {
          throw sub397.ex(e398);
        }
        try {
          ((java.util.ArrayList)sub397.isSet("list", env[47])).add((java.lang.Object)String.valueOf("String.valueOf(") + String.valueOf(Value(sub397, env)) + String.valueOf(")"));
        } catch(final Exception e399) {
          throw sub397.ex(e399);
        }
        final StringCursor sub400 = sub397.sub();
        try {
          env[14] /*value*/= ((java.util.Iterator)sub400.isSet("values", env[46])).next();
        } catch(final Exception e401) {
          throw sub400.ex(e401);
        }
        try {
          ((java.util.ArrayList)sub400.isSet("list", env[47])).add((java.lang.Object)String.valueOf("String.valueOf(") + String.valueOf(Value(sub400, env)) + String.valueOf(")"));
        } catch(final Exception e402) {
          throw sub400.ex(e402);
        }
        sub400.commit();
        try {
          while(true) {
            final StringCursor sub403 = sub397.sub();
            try {
              env[14] /*value*/= ((java.util.Iterator)sub403.isSet("values", env[46])).next();
            } catch(final Exception e404) {
              throw sub403.ex(e404);
            }
            try {
              ((java.util.ArrayList)sub403.isSet("list", env[47])).add((java.lang.Object)String.valueOf("String.valueOf(") + String.valueOf(Value(sub403, env)) + String.valueOf(")"));
            } catch(final Exception e405) {
              throw sub403.ex(e405);
            }
            sub403.commit();
          }
        } catch(final GrinCodeGeneratorException e406) {
          //continue
        }
        final StringCursor sub407 = sub397.sub();
        boolean b408 = true;
        try {
          if((Object)((java.util.Iterator)sub407.isSet("values", env[46])).hasNext() == Boolean.FALSE) {
            b408 = false;
          }
        } catch(final GrinCodeGeneratorException e409) {
          b408 = false;
        }
        if(b408){
          throw sub397.ex("unexpected value");
        }
        try {
          env[20] /*result*/= org.fuwjin.util.StringUtils.join((java.lang.String)String.valueOf(" + "), (java.lang.Iterable)sub397.isSet("list", env[47]));
        } catch(final Exception e410) {
          throw sub397.ex(e410);
        }
        sub397.commit();
      }
      sub390.commit();
    } catch(final GrinCodeGeneratorException e411) {
      //continue
    }
    sub387.commit();
    return sub387.isSet("result", env[20]);
  }

  static Object NextValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub412 = input.sub();
    final StringCursor sub413 = sub412.sub();
    if((Object)((java.lang.Object)org.fuwjin.chessur.expression.Variable.NEXT).equals((java.lang.Object)sub413.isSet("value", env[14])) == Boolean.FALSE) {
      throw sub413.ex("check failed");
    }
    sub412.commit();
    return String.valueOf(sub412.isSet("input", env[17])) + String.valueOf(".next()");
  }

  static Object MatchValue(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub414 = input.sub();
    final StringCursor sub415 = sub414.sub();
    if((Object)((java.lang.Object)org.fuwjin.chessur.expression.Variable.MATCH).equals((java.lang.Object)sub415.isSet("value", env[14])) == Boolean.FALSE) {
      throw sub415.ex("check failed");
    }
    sub414.commit();
    return String.valueOf(sub414.isSet("input", env[17])) + String.valueOf(".match()");
  }

  static Object ScriptStatement(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub416 = input.sub();
    try {
      sub416.publish(String.valueOf(sub416.isSet("indent", env[15])) + String.valueOf(Script(sub416, env)) + String.valueOf(";"));
    } catch(final GrinCodeGeneratorException e417) {
      try {
        final StringCursor sub418 = sub416.sub();
        final StringCursor sub419 = sub418.sub();
        if((Object)sub419.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptOutput == Boolean.FALSE) {
          throw sub419.ex("check failed");
        }
        try {
          env[8] /*name*/= ((org.fuwjin.chessur.expression.ScriptOutput)sub418.isSet("statement", env[19])).name();
        } catch(final Exception e420) {
          throw sub418.ex(e420);
        }
        try {
          env[19] /*statement*/= ((org.fuwjin.chessur.expression.ScriptOutput)sub418.isSet("statement", env[19])).spec();
        } catch(final Exception e421) {
          throw sub418.ex(e421);
        }
        try {
          env[22] /*bIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub418.isSet("varIndex", env[0])).incrementAndGet();
        } catch(final Exception e422) {
          throw sub418.ex(e422);
        }
        env[44] /*builder*/= String.valueOf("builder") + String.valueOf(sub418.isSet("bIndex", env[22]));
        sub418.publish(String.valueOf(sub418.isSet("indent", env[15])) + String.valueOf("final StringBuilder ") + String.valueOf(sub418.isSet("builder", env[44])) + String.valueOf(" = new StringBuilder();"));
        try {
          env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub418.isSet("varIndex", env[0])).incrementAndGet();
        } catch(final Exception e423) {
          throw sub418.ex(e423);
        }
        env[21] /*oldInput*/= sub418.isSet("input", env[17]);
        env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub418.isSet("subIndex", env[16]));
        sub418.publish(String.valueOf(sub418.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub418.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub418.isSet("oldInput", env[21])) + String.valueOf(".subOutput(") + String.valueOf(sub418.isSet("builder", env[44])) + String.valueOf(");"));
        ScriptStatement(sub418, env);
        sub418.publish(String.valueOf(sub418.isSet("indent", env[15])) + String.valueOf(sub418.isSet("input", env[17])) + String.valueOf(".commit();"));
        try {
          env[9] /*index*/= ((org.fuwjin.util.NameIndex)sub418.isSet("indexer", env[1])).indexOf((java.lang.String)sub418.isSet("name", env[8]));
        } catch(final Exception e424) {
          throw sub418.ex(e424);
        }
        sub418.publish(String.valueOf(sub418.isSet("indent", env[15])) + String.valueOf("env[") + String.valueOf(sub418.isSet("index", env[9])) + String.valueOf("] /*") + String.valueOf(sub418.isSet("name", env[8])) + String.valueOf("*/= ") + String.valueOf(sub418.isSet("builder", env[44])) + String.valueOf(".toString();"));
        sub418.commit();
      } catch(final GrinCodeGeneratorException e425) {
        try {
          final StringCursor sub426 = sub416.sub();
          final StringCursor sub427 = sub426.sub();
          if((Object)sub427.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptInput == Boolean.FALSE) {
            throw sub427.ex("check failed");
          }
          try {
            env[14] /*value*/= ((org.fuwjin.chessur.expression.ScriptInput)sub426.isSet("statement", env[19])).value();
          } catch(final Exception e428) {
            throw sub426.ex(e428);
          }
          try {
            env[19] /*statement*/= ((org.fuwjin.chessur.expression.ScriptInput)sub426.isSet("statement", env[19])).spec();
          } catch(final Exception e429) {
            throw sub426.ex(e429);
          }
          try {
            env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub426.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e430) {
            throw sub426.ex(e430);
          }
          env[21] /*oldInput*/= sub426.isSet("input", env[17]);
          env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub426.isSet("subIndex", env[16]));
          sub426.publish(String.valueOf(sub426.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub426.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub426.isSet("oldInput", env[21])) + String.valueOf(".subInput(String.valueOf(") + String.valueOf(Value(sub426, env)) + String.valueOf("));"));
          ScriptStatement(sub426, env);
          sub426.publish(String.valueOf(sub426.isSet("indent", env[15])) + String.valueOf(sub426.isSet("input", env[17])) + String.valueOf(".commit();"));
          sub426.commit();
        } catch(final GrinCodeGeneratorException e431) {
          final StringCursor sub432 = sub416.sub();
          final StringCursor sub433 = sub432.sub();
          if((Object)sub433.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptPipe == Boolean.FALSE) {
            throw sub433.ex("check failed");
          }
          try {
            env[48] /*source*/= ((org.fuwjin.chessur.expression.ScriptPipe)sub432.isSet("statement", env[19])).source();
          } catch(final Exception e434) {
            throw sub432.ex(e434);
          }
          try {
            env[49] /*sink*/= ((org.fuwjin.chessur.expression.ScriptPipe)sub432.isSet("statement", env[19])).sink();
          } catch(final Exception e435) {
            throw sub432.ex(e435);
          }
          try {
            env[22] /*bIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub432.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e436) {
            throw sub432.ex(e436);
          }
          env[44] /*builder*/= String.valueOf("builder") + String.valueOf(sub432.isSet("bIndex", env[22]));
          sub432.publish(String.valueOf(sub432.isSet("indent", env[15])) + String.valueOf("final StringBuilder ") + String.valueOf(sub432.isSet("builder", env[44])) + String.valueOf(" = new StringBuilder();"));
          try {
            env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub432.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e437) {
            throw sub432.ex(e437);
          }
          env[21] /*oldInput*/= sub432.isSet("input", env[17]);
          env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub432.isSet("subIndex", env[16]));
          sub432.publish(String.valueOf(sub432.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub432.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub432.isSet("oldInput", env[21])) + String.valueOf(".subOutput(") + String.valueOf(sub432.isSet("builder", env[44])) + String.valueOf(");"));
          env[19] /*statement*/= sub432.isSet("source", env[48]);
          ScriptStatement(sub432, env);
          sub432.publish(String.valueOf(sub432.isSet("indent", env[15])) + String.valueOf(sub432.isSet("input", env[17])) + String.valueOf(".commit();"));
          try {
            env[16] /*subIndex*/= ((java.util.concurrent.atomic.AtomicInteger)sub432.isSet("varIndex", env[0])).incrementAndGet();
          } catch(final Exception e438) {
            throw sub432.ex(e438);
          }
          env[17] /*input*/= String.valueOf("sub") + String.valueOf(sub432.isSet("subIndex", env[16]));
          sub432.publish(String.valueOf(sub432.isSet("indent", env[15])) + String.valueOf("final StringCursor ") + String.valueOf(sub432.isSet("input", env[17])) + String.valueOf(" = ") + String.valueOf(sub432.isSet("oldInput", env[21])) + String.valueOf(".subInput(") + String.valueOf(sub432.isSet("builder", env[44])) + String.valueOf(");"));
          env[19] /*statement*/= sub432.isSet("sink", env[49]);
          ScriptStatement(sub432, env);
          sub432.publish(String.valueOf(sub432.isSet("indent", env[15])) + String.valueOf(sub432.isSet("input", env[17])) + String.valueOf(".commit();"));
          sub432.commit();
        }
      }
    }
    sub416.commit();
    return null;
  }

  static Object Script(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub439 = input.sub();
    try {
      final StringCursor sub440 = sub439.sub();
      final StringCursor sub441 = sub440.sub();
      if((Object)sub441.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptImpl == Boolean.FALSE) {
        throw sub441.ex("check failed");
      }
      try {
        env[8] /*name*/= ((org.fuwjin.chessur.expression.ScriptImpl)sub440.isSet("statement", env[19])).name();
      } catch(final Exception e442) {
        throw sub440.ex(e442);
      }
      env[20] /*result*/= String.valueOf(sub440.isSet("name", env[8])) + String.valueOf("(") + String.valueOf(sub440.isSet("input", env[17])) + String.valueOf(", env)");
      sub440.commit();
    } catch(final GrinCodeGeneratorException e443) {
      final StringCursor sub444 = sub439.sub();
      final StringCursor sub445 = sub444.sub();
      if((Object)sub445.isSet("statement", env[19]) instanceof org.fuwjin.chessur.expression.ScriptProxy == Boolean.FALSE) {
        throw sub445.ex("check failed");
      }
      try {
        env[50] /*module*/= ((org.fuwjin.chessur.Module)((org.fuwjin.chessur.expression.ScriptProxy)sub444.isSet("statement", env[19])).module()).name();
      } catch(final Exception e446) {
        throw sub444.ex(e446);
      }
      try {
        env[8] /*name*/= ((org.fuwjin.chessur.expression.ScriptImpl)((org.fuwjin.chessur.expression.ScriptProxy)sub444.isSet("statement", env[19])).script()).name();
      } catch(final Exception e447) {
        throw sub444.ex(e447);
      }
      env[20] /*result*/= String.valueOf(sub444.isSet("package", env[11])) + String.valueOf(".") + String.valueOf(sub444.isSet("module", env[50])) + String.valueOf(".") + String.valueOf(sub444.isSet("name", env[8])) + String.valueOf("(") + String.valueOf(sub444.isSet("input", env[17])) + String.valueOf(", env)");
      sub444.commit();
    }
    sub439.commit();
    return sub439.isSet("result", env[20]);
  }

  static Object Variable(final StringCursor input, final Object... parentEnv) throws GrinCodeGeneratorException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub448 = input.sub();
    final StringCursor sub449 = sub448.sub();
    if((Object)sub449.isSet("value", env[14]) instanceof org.fuwjin.chessur.expression.Variable == Boolean.FALSE) {
      throw sub449.ex("check failed");
    }
    try {
      env[8] /*name*/= ((org.fuwjin.chessur.expression.Variable)sub448.isSet("value", env[14])).name();
    } catch(final Exception e450) {
      throw sub448.ex(e450);
    }
    try {
      env[9] /*index*/= ((org.fuwjin.util.NameIndex)sub448.isSet("indexer", env[1])).indexOf((java.lang.String)sub448.isSet("name", env[8]));
    } catch(final Exception e451) {
      throw sub448.ex(e451);
    }
    sub448.commit();
    return String.valueOf(sub448.isSet("input", env[17])) + String.valueOf(".isSet(\"") + String.valueOf(sub448.isSet("name", env[8])) + String.valueOf("\", env[") + String.valueOf(sub448.isSet("index", env[9])) + String.valueOf("])");
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
