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

public class GrinSerializer {
  static final Object UNSET = new Object() {
    public String toString() {
      return "UNSET";
    }
  };

  public static class GrinSerializerException extends Exception {
    private static final long serialVersionUID = 1; 
    GrinSerializerException(final String message) {
      super(message);
    }
    
    GrinSerializerException(final String message, final Throwable cause) {
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
    
    public int accept() throws GrinSerializerException {
      checkBounds(pos);
      return advance();
    }
    
    public int accept(final String expected) throws GrinSerializerException {
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
    
    public int acceptIn(final String name, final String set) throws GrinSerializerException {
      checkBounds(pos);
      if(set.indexOf(seq.charAt(pos)) < 0) {
        throw ex("Did not match filter: "+name);
      }
      return advance();
    }
    
    public int acceptNot(final String expected) throws GrinSerializerException {
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
    
    public int acceptNotIn(final String name, final String set) throws GrinSerializerException {
      checkBounds(pos);
      if(set.indexOf(seq.charAt(pos)) >= 0) {
        throw ex("Unexpected match: "+name);
      }
      return advance();
    }
    
    public void publish(final Object value) throws GrinSerializerException {
      try {
        appender.append(value.toString());
      } catch(IOException e) {
        throw ex(e);
      }
    }
    
    public Object isSet(final String name, final Object value) throws GrinSerializerException {
      if(UNSET.equals(value)) {
        throw ex("variable "+name+" is unset");
      }
      return value;
    }
    
    protected void checkBounds(final int p) throws GrinSerializerException {
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
    
    public GrinSerializerException ex(final String message) {
      return new GrinSerializerException(message + context());
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
    
    public GrinSerializerException ex(final Throwable cause) {
      return new GrinSerializerException(context(), cause);
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
    
    public int next() throws GrinSerializerException {
      checkBounds(pos);
      return seq.charAt(pos);
    }
    
    public String nextStr() throws GrinSerializerException {
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

  static Object Catalog(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub1 = input.sub();
    try {
      env[0] /*modules*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.CatalogImpl)sub1.isSet("cat", env[1])).modules()).iterator();
    } catch(final Exception e2) {
      throw sub1.ex(e2);
    }
    try {
      final StringCursor sub3 = sub1.sub();
      LoadDeclaration(sub3, env);
      try {
        while(true) {
          LoadDeclaration(sub3, env);
        }
      } catch(final GrinSerializerException e4) {
        //continue
      }
      sub3.publish("\n");
      sub3.commit();
    } catch(final GrinSerializerException e5) {
      //continue
    }
    final StringCursor sub6 = sub1.sub();
    boolean b7 = true;
    try {
      if((Object)((java.util.Iterator)sub6.isSet("modules", env[0])).hasNext() == Boolean.FALSE) {
        b7 = false;
      }
    } catch(final GrinSerializerException e8) {
      b7 = false;
    }
    if(b7){
      throw sub1.ex("unexpected value");
    }
    try {
      env[2] /*aliases*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.CatalogImpl)sub1.isSet("cat", env[1])).aliases()).iterator();
    } catch(final Exception e9) {
      throw sub1.ex(e9);
    }
    try {
      final StringCursor sub10 = sub1.sub();
      AliasDeclaration(sub10, env);
      try {
        while(true) {
          AliasDeclaration(sub10, env);
        }
      } catch(final GrinSerializerException e11) {
        //continue
      }
      sub10.publish("\n");
      sub10.commit();
    } catch(final GrinSerializerException e12) {
      //continue
    }
    final StringCursor sub13 = sub1.sub();
    boolean b14 = true;
    try {
      if((Object)((java.util.Iterator)sub13.isSet("aliases", env[2])).hasNext() == Boolean.FALSE) {
        b14 = false;
      }
    } catch(final GrinSerializerException e15) {
      b14 = false;
    }
    if(b14){
      throw sub1.ex("unexpected value");
    }
    try {
      env[3] /*specs*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.CatalogImpl)sub1.isSet("cat", env[1])).scripts()).iterator();
    } catch(final Exception e16) {
      throw sub1.ex(e16);
    }
    ScriptDeclaration(sub1, env);
    try {
      while(true) {
        ScriptDeclaration(sub1, env);
      }
    } catch(final GrinSerializerException e17) {
      //continue
    }
    final StringCursor sub18 = sub1.sub();
    boolean b19 = true;
    try {
      if((Object)((java.util.Iterator)sub18.isSet("specs", env[3])).hasNext() == Boolean.FALSE) {
        b19 = false;
      }
    } catch(final GrinSerializerException e20) {
      b19 = false;
    }
    if(b19){
      throw sub1.ex("unexpected value");
    }
    sub1.commit();
    return null;
  }

  static Object LoadDeclaration(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub21 = input.sub();
    try {
      env[4] /*module*/= ((java.util.Iterator)sub21.isSet("modules", env[0])).next();
    } catch(final Exception e22) {
      throw sub21.ex(e22);
    }
    try {
      env[5] /*path*/= ((org.fuwjin.chessur.Module)sub21.isSet("module", env[4])).source();
    } catch(final Exception e23) {
      throw sub21.ex(e23);
    }
    try {
      env[6] /*name*/= ((org.fuwjin.chessur.Module)sub21.isSet("module", env[4])).name();
    } catch(final Exception e24) {
      throw sub21.ex(e24);
    }
    sub21.publish(String.valueOf("load ") + String.valueOf(sub21.isSet("path", env[5])) + String.valueOf(" as ") + String.valueOf(sub21.isSet("name", env[6])) + String.valueOf("\n"));
    sub21.commit();
    return null;
  }

  static Object AliasDeclaration(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub25 = input.sub();
    try {
      env[7] /*entry*/= ((java.util.Iterator)sub25.isSet("aliases", env[2])).next();
    } catch(final Exception e26) {
      throw sub25.ex(e26);
    }
    try {
      env[6] /*name*/= ((java.util.Map.Entry)sub25.isSet("entry", env[7])).getKey();
    } catch(final Exception e27) {
      throw sub25.ex(e27);
    }
    try {
      env[8] /*alias*/= ((java.util.Map.Entry)sub25.isSet("entry", env[7])).getValue();
    } catch(final Exception e28) {
      throw sub25.ex(e28);
    }
    sub25.publish(String.valueOf("alias ") + String.valueOf(sub25.isSet("alias", env[8])) + String.valueOf(" as ") + String.valueOf(sub25.isSet("name", env[6])) + String.valueOf("\n"));
    sub25.commit();
    return null;
  }

  static Object ScriptDeclaration(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub29 = input.sub();
    try {
      env[9] /*spec*/= ((org.fuwjin.chessur.expression.ScriptImpl)((java.util.Iterator)sub29.isSet("specs", env[3])).next()).declaration();
    } catch(final Exception e30) {
      throw sub29.ex(e30);
    }
    try {
      env[6] /*name*/= ((org.fuwjin.chessur.expression.Declaration)sub29.isSet("spec", env[9])).name();
    } catch(final Exception e31) {
      throw sub29.ex(e31);
    }
    sub29.publish(String.valueOf("<") + String.valueOf(sub29.isSet("name", env[6])) + String.valueOf("> {"));
    try {
      env[10] /*indent*/= new org.fuwjin.util.Indent();
    } catch(final Exception e32) {
      throw sub29.ex(e32);
    }
    try {
      env[11] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Declaration)sub29.isSet("spec", env[9])).statements()).iterator();
    } catch(final Exception e33) {
      throw sub29.ex(e33);
    }
    try {
      final StringCursor sub34 = sub29.sub();
      try {
        env[12] /*statement*/= ((java.util.Iterator)sub34.isSet("statements", env[11])).next();
      } catch(final Exception e35) {
        throw sub34.ex(e35);
      }
      sub34.publish(String.valueOf(sub34.isSet("indent", env[10])));
      Statement(sub34, env);
      sub34.commit();
      try {
        while(true) {
          final StringCursor sub36 = sub29.sub();
          try {
            env[12] /*statement*/= ((java.util.Iterator)sub36.isSet("statements", env[11])).next();
          } catch(final Exception e37) {
            throw sub36.ex(e37);
          }
          sub36.publish(String.valueOf(sub36.isSet("indent", env[10])));
          Statement(sub36, env);
          sub36.commit();
        }
      } catch(final GrinSerializerException e38) {
        //continue
      }
    } catch(final GrinSerializerException e39) {
      //continue
    }
    final StringCursor sub40 = sub29.sub();
    boolean b41 = true;
    try {
      if((Object)((java.util.Iterator)sub40.isSet("statements", env[11])).hasNext() == Boolean.FALSE) {
        b41 = false;
      }
    } catch(final GrinSerializerException e42) {
      b41 = false;
    }
    if(b41){
      throw sub29.ex("unexpected value");
    }
    try {
      final StringCursor sub43 = sub29.sub();
      try {
        env[13] /*value*/= ((org.fuwjin.chessur.expression.Declaration)sub43.isSet("spec", env[9])).returns();
      } catch(final Exception e44) {
        throw sub43.ex(e44);
      }
      final StringCursor sub45 = sub43.sub();
      if((Object)sub45.isSet("value", env[13]) == Boolean.FALSE) {
        throw sub45.ex("check failed");
      }
      sub43.publish(String.valueOf(sub43.isSet("indent", env[10])) + String.valueOf("return "));
      Value(sub43, env);
      sub43.commit();
    } catch(final GrinSerializerException e46) {
      //continue
    }
    sub29.publish(String.valueOf("\n}\n"));
    sub29.commit();
    return null;
  }

  static Object Value(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub47 = input.sub();
    env[12] /*statement*/= sub47.isSet("value", env[13]);
    try {
      Script(sub47, env);
    } catch(final GrinSerializerException e48) {
      try {
        StaticLiteral(sub47, env);
      } catch(final GrinSerializerException e49) {
        try {
          DynamicLiteral(sub47, env);
        } catch(final GrinSerializerException e50) {
          try {
            AcceptStatement(sub47, env);
          } catch(final GrinSerializerException e51) {
            try {
              Invocation(sub47, env);
            } catch(final GrinSerializerException e52) {
              try {
                Object(sub47, env);
              } catch(final GrinSerializerException e53) {
                try {
                  Number(sub47, env);
                } catch(final GrinSerializerException e54) {
                  Variable(sub47, env);
                }
              }
            }
          }
        }
      }
    }
    sub47.commit();
    return null;
  }

  static Object Statement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub55 = input.sub();
    try {
      AssumeStatement(sub55, env);
    } catch(final GrinSerializerException e56) {
      try {
        EitherOrStatement(sub55, env);
      } catch(final GrinSerializerException e57) {
        try {
          ConditionalAbortStatement(sub55, env);
        } catch(final GrinSerializerException e58) {
          try {
            CouldStatement(sub55, env);
          } catch(final GrinSerializerException e59) {
            try {
              RepeatStatement(sub55, env);
            } catch(final GrinSerializerException e60) {
              try {
                AcceptStatement(sub55, env);
              } catch(final GrinSerializerException e61) {
                try {
                  PublishStatement(sub55, env);
                } catch(final GrinSerializerException e62) {
                  try {
                    LogStatement(sub55, env);
                  } catch(final GrinSerializerException e63) {
                    try {
                      AbortStatement(sub55, env);
                    } catch(final GrinSerializerException e64) {
                      try {
                        Script(sub55, env);
                      } catch(final GrinSerializerException e65) {
                        try {
                          Block(sub55, env);
                        } catch(final GrinSerializerException e66) {
                          try {
                            Assignment(sub55, env);
                          } catch(final GrinSerializerException e67) {
                            Invocation(sub55, env);
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
    sub55.commit();
    return null;
  }

  static Object AssumeStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub68 = input.sub();
    final StringCursor sub69 = sub68.sub();
    if((Object)sub69.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.AssumeStatement == Boolean.FALSE) {
      throw sub69.ex("check failed");
    }
    sub68.publish(String.valueOf("assume "));
    try {
      final StringCursor sub70 = sub68.sub();
      final StringCursor sub71 = sub70.sub();
      if((Object)((org.fuwjin.chessur.expression.AssumeStatement)sub71.isSet("statement", env[12])).isNot() == Boolean.FALSE) {
        throw sub71.ex("check failed");
      }
      sub70.publish(String.valueOf("not "));
      sub70.commit();
    } catch(final GrinSerializerException e72) {
      //continue
    }
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.AssumeStatement)sub68.isSet("statement", env[12])).value();
    } catch(final Exception e73) {
      throw sub68.ex(e73);
    }
    try {
      Value(sub68, env);
    } catch(final GrinSerializerException e74) {
      final StringCursor sub75 = sub68.sub();
      env[12] /*statement*/= sub75.isSet("value", env[13]);
      Statement(sub75, env);
      sub75.commit();
    }
    sub68.commit();
    return null;
  }

  static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub76 = input.sub();
    final StringCursor sub77 = sub76.sub();
    if((Object)sub77.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.EitherOrStatement == Boolean.FALSE) {
      throw sub77.ex("check failed");
    }
    env[14] /*stmt*/= sub76.isSet("statement", env[12]);
    try {
      env[11] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.EitherOrStatement)sub76.isSet("stmt", env[14])).statements()).iterator();
    } catch(final Exception e78) {
      throw sub76.ex(e78);
    }
    sub76.publish(String.valueOf("either "));
    try {
      env[12] /*statement*/= ((java.util.Iterator)sub76.isSet("statements", env[11])).next();
    } catch(final Exception e79) {
      throw sub76.ex(e79);
    }
    Statement(sub76, env);
    final StringCursor sub80 = sub76.sub();
    env[15] /*old*/= sub80.isSet("statement", env[12]);
    try {
      env[12] /*statement*/= ((java.util.Iterator)sub80.isSet("statements", env[11])).next();
    } catch(final Exception e81) {
      throw sub80.ex(e81);
    }
    try {
      final StringCursor sub82 = sub80.sub();
      final StringCursor sub83 = sub82.sub();
      if((Object)sub83.isSet("old", env[15]) instanceof org.fuwjin.chessur.expression.Block == Boolean.FALSE) {
        throw sub83.ex("check failed");
      }
      sub82.publish(" ");
      sub82.commit();
    } catch(final GrinSerializerException e84) {
      sub80.publish(sub80.isSet("indent", env[10]));
    }
    sub80.publish(String.valueOf("or "));
    Statement(sub80, env);
    sub80.commit();
    try {
      while(true) {
        final StringCursor sub85 = sub76.sub();
        env[15] /*old*/= sub85.isSet("statement", env[12]);
        try {
          env[12] /*statement*/= ((java.util.Iterator)sub85.isSet("statements", env[11])).next();
        } catch(final Exception e86) {
          throw sub85.ex(e86);
        }
        try {
          final StringCursor sub87 = sub85.sub();
          final StringCursor sub88 = sub87.sub();
          if((Object)sub88.isSet("old", env[15]) instanceof org.fuwjin.chessur.expression.Block == Boolean.FALSE) {
            throw sub88.ex("check failed");
          }
          sub87.publish(" ");
          sub87.commit();
        } catch(final GrinSerializerException e89) {
          sub85.publish(sub85.isSet("indent", env[10]));
        }
        sub85.publish(String.valueOf("or "));
        Statement(sub85, env);
        sub85.commit();
      }
    } catch(final GrinSerializerException e90) {
      //continue
    }
    sub76.commit();
    return null;
  }

  static Object ConditionalAbortStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub91 = input.sub();
    final StringCursor sub92 = sub91.sub();
    if((Object)sub92.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.ConditionalAbortStatement == Boolean.FALSE) {
      throw sub92.ex("check failed");
    }
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.ConditionalAbortStatement)sub91.isSet("statement", env[12])).value();
    } catch(final Exception e93) {
      throw sub91.ex(e93);
    }
    env[14] /*stmt*/= sub91.isSet("statement", env[12]);
    final StringCursor sub94 = sub91.sub();
    if((Object)sub94.isSet("stmt", env[14]) instanceof org.fuwjin.chessur.expression.EitherOrStatement == Boolean.FALSE) {
      throw sub94.ex("check failed");
    }
    try {
      env[11] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.EitherOrStatement)sub91.isSet("stmt", env[14])).statements()).iterator();
    } catch(final Exception e95) {
      throw sub91.ex(e95);
    }
    sub91.publish(String.valueOf("either "));
    final StringCursor sub96 = sub91.sub();
    try {
      env[12] /*statement*/= ((java.util.Iterator)sub96.isSet("statements", env[11])).next();
    } catch(final Exception e97) {
      throw sub96.ex(e97);
    }
    Statement(sub96, env);
    try {
      final StringCursor sub98 = sub96.sub();
      final StringCursor sub99 = sub98.sub();
      if((Object)sub99.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.Block == Boolean.FALSE) {
        throw sub99.ex("check failed");
      }
      sub98.publish(" ");
      sub98.commit();
    } catch(final GrinSerializerException e100) {
      sub96.publish(sub96.isSet("indent", env[10]));
    }
    sub96.publish(String.valueOf("or "));
    sub96.commit();
    try {
      while(true) {
        final StringCursor sub101 = sub91.sub();
        try {
          env[12] /*statement*/= ((java.util.Iterator)sub101.isSet("statements", env[11])).next();
        } catch(final Exception e102) {
          throw sub101.ex(e102);
        }
        Statement(sub101, env);
        try {
          final StringCursor sub103 = sub101.sub();
          final StringCursor sub104 = sub103.sub();
          if((Object)sub104.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.Block == Boolean.FALSE) {
            throw sub104.ex("check failed");
          }
          sub103.publish(" ");
          sub103.commit();
        } catch(final GrinSerializerException e105) {
          sub101.publish(sub101.isSet("indent", env[10]));
        }
        sub101.publish(String.valueOf("or "));
        sub101.commit();
      }
    } catch(final GrinSerializerException e106) {
      //continue
    }
    sub91.publish(String.valueOf("abort "));
    Value(sub91, env);
    sub91.commit();
    return null;
  }

  static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub107 = input.sub();
    final StringCursor sub108 = sub107.sub();
    if((Object)sub108.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.CouldStatement == Boolean.FALSE) {
      throw sub108.ex("check failed");
    }
    env[14] /*stmt*/= sub107.isSet("statement", env[12]);
    sub107.publish(String.valueOf("could "));
    try {
      env[12] /*statement*/= ((org.fuwjin.chessur.expression.CouldStatement)sub107.isSet("stmt", env[14])).statement();
    } catch(final Exception e109) {
      throw sub107.ex(e109);
    }
    Statement(sub107, env);
    sub107.commit();
    return null;
  }

  static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub110 = input.sub();
    final StringCursor sub111 = sub110.sub();
    if((Object)sub111.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.RepeatStatement == Boolean.FALSE) {
      throw sub111.ex("check failed");
    }
    env[14] /*stmt*/= sub110.isSet("statement", env[12]);
    sub110.publish(String.valueOf("repeat "));
    try {
      env[12] /*statement*/= ((org.fuwjin.chessur.expression.RepeatStatement)sub110.isSet("stmt", env[14])).statement();
    } catch(final Exception e112) {
      throw sub110.ex(e112);
    }
    Statement(sub110, env);
    sub110.commit();
    return null;
  }

  static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub113 = input.sub();
    try {
      FilterAcceptStatement(sub113, env);
    } catch(final GrinSerializerException e114) {
      ValueAcceptStatement(sub113, env);
    }
    sub113.commit();
    return null;
  }

  static Object ValueAcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub115 = input.sub();
    final StringCursor sub116 = sub115.sub();
    if((Object)sub116.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.ValueAcceptStatement == Boolean.FALSE) {
      throw sub116.ex("check failed");
    }
    sub115.publish(String.valueOf("accept "));
    try {
      final StringCursor sub117 = sub115.sub();
      final StringCursor sub118 = sub117.sub();
      if((Object)((org.fuwjin.chessur.expression.ValueAcceptStatement)sub118.isSet("statement", env[12])).isNot() == Boolean.FALSE) {
        throw sub118.ex("check failed");
      }
      sub117.publish(String.valueOf("not "));
      sub117.commit();
    } catch(final GrinSerializerException e119) {
      //continue
    }
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.ValueAcceptStatement)sub115.isSet("statement", env[12])).value();
    } catch(final Exception e120) {
      throw sub115.ex(e120);
    }
    Value(sub115, env);
    sub115.commit();
    return null;
  }

  static Object FilterAcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub121 = input.sub();
    final StringCursor sub122 = sub121.sub();
    if((Object)sub122.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.FilterAcceptStatement == Boolean.FALSE) {
      throw sub122.ex("check failed");
    }
    sub121.publish(String.valueOf("accept "));
    try {
      final StringCursor sub123 = sub121.sub();
      final StringCursor sub124 = sub123.sub();
      if((Object)((org.fuwjin.chessur.expression.FilterAcceptStatement)sub124.isSet("statement", env[12])).isNot() == Boolean.FALSE) {
        throw sub124.ex("check failed");
      }
      sub123.publish(String.valueOf("not "));
      sub123.commit();
    } catch(final GrinSerializerException e125) {
      //continue
    }
    sub121.publish(String.valueOf("in "));
    try {
      env[16] /*filter*/= ((org.fuwjin.chessur.expression.FilterAcceptStatement)sub121.isSet("statement", env[12])).filter();
    } catch(final Exception e126) {
      throw sub121.ex(e126);
    }
    Filter(sub121, env);
    sub121.commit();
    return null;
  }

  static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub127 = input.sub();
    final StringCursor sub128 = sub127.sub();
    if((Object)sub128.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.PublishStatement == Boolean.FALSE) {
      throw sub128.ex("check failed");
    }
    sub127.publish(String.valueOf("publish "));
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.PublishStatement)sub127.isSet("statement", env[12])).value();
    } catch(final Exception e129) {
      throw sub127.ex(e129);
    }
    Value(sub127, env);
    sub127.commit();
    return null;
  }

  static Object LogStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub130 = input.sub();
    final StringCursor sub131 = sub130.sub();
    if((Object)sub131.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.LogStatement == Boolean.FALSE) {
      throw sub131.ex("check failed");
    }
    sub130.publish(String.valueOf("log "));
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.LogStatement)sub130.isSet("statement", env[12])).value();
    } catch(final Exception e132) {
      throw sub130.ex(e132);
    }
    Value(sub130, env);
    sub130.commit();
    return null;
  }

  static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub133 = input.sub();
    final StringCursor sub134 = sub133.sub();
    if((Object)sub134.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.AbortStatement == Boolean.FALSE) {
      throw sub134.ex("check failed");
    }
    sub133.publish(String.valueOf("abort "));
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.AbortStatement)sub133.isSet("statement", env[12])).value();
    } catch(final Exception e135) {
      throw sub133.ex(e135);
    }
    Value(sub133, env);
    sub133.commit();
    return null;
  }

  static Object Block(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub136 = input.sub();
    final StringCursor sub137 = sub136.sub();
    if((Object)sub137.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.Block == Boolean.FALSE) {
      throw sub137.ex("check failed");
    }
    env[17] /*block*/= sub136.isSet("statement", env[12]);
    try {
      ((org.fuwjin.util.Indent)sub136.isSet("indent", env[10])).increase();
    } catch(final Exception e138) {
      throw sub136.ex(e138);
    }
    sub136.publish(String.valueOf("{"));
    try {
      env[11] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Block)sub136.isSet("block", env[17])).statements()).iterator();
    } catch(final Exception e139) {
      throw sub136.ex(e139);
    }
    try {
      final StringCursor sub140 = sub136.sub();
      try {
        env[12] /*statement*/= ((java.util.Iterator)sub140.isSet("statements", env[11])).next();
      } catch(final Exception e141) {
        throw sub140.ex(e141);
      }
      sub140.publish(sub140.isSet("indent", env[10]));
      Statement(sub140, env);
      sub140.commit();
      try {
        while(true) {
          final StringCursor sub142 = sub136.sub();
          try {
            env[12] /*statement*/= ((java.util.Iterator)sub142.isSet("statements", env[11])).next();
          } catch(final Exception e143) {
            throw sub142.ex(e143);
          }
          sub142.publish(sub142.isSet("indent", env[10]));
          Statement(sub142, env);
          sub142.commit();
        }
      } catch(final GrinSerializerException e144) {
        //continue
      }
    } catch(final GrinSerializerException e145) {
      //continue
    }
    final StringCursor sub146 = sub136.sub();
    boolean b147 = true;
    try {
      if((Object)((java.util.Iterator)sub146.isSet("statements", env[11])).hasNext() == Boolean.FALSE) {
        b147 = false;
      }
    } catch(final GrinSerializerException e148) {
      b147 = false;
    }
    if(b147){
      throw sub136.ex("unexpected value");
    }
    try {
      ((org.fuwjin.util.Indent)sub136.isSet("indent", env[10])).decrease();
    } catch(final Exception e149) {
      throw sub136.ex(e149);
    }
    sub136.publish(sub136.isSet("indent", env[10]));
    sub136.publish(String.valueOf("}"));
    sub136.commit();
    return null;
  }

  static Object Assignment(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub150 = input.sub();
    final StringCursor sub151 = sub150.sub();
    if((Object)sub151.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.Assignment == Boolean.FALSE) {
      throw sub151.ex("check failed");
    }
    try {
      env[6] /*name*/= ((org.fuwjin.chessur.expression.Assignment)sub150.isSet("statement", env[12])).name();
    } catch(final Exception e152) {
      throw sub150.ex(e152);
    }
    sub150.publish(String.valueOf(sub150.isSet("name", env[6])) + String.valueOf(" = "));
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.Assignment)sub150.isSet("statement", env[12])).value();
    } catch(final Exception e153) {
      throw sub150.ex(e153);
    }
    Value(sub150, env);
    sub150.commit();
    return null;
  }

  static Object Invocation(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub154 = input.sub();
    final StringCursor sub155 = sub154.sub();
    if((Object)sub155.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.Invocation == Boolean.FALSE) {
      throw sub155.ex("check failed");
    }
    try {
      env[6] /*name*/= ((org.fuwjin.chessur.expression.Invocation)sub154.isSet("statement", env[12])).name();
    } catch(final Exception e156) {
      throw sub154.ex(e156);
    }
    try {
      env[8] /*alias*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub154.isSet("cat", env[1])).encode((java.lang.String)sub154.isSet("name", env[6]));
    } catch(final Exception e157) {
      throw sub154.ex(e157);
    }
    sub154.publish(String.valueOf(sub154.isSet("alias", env[8])) + String.valueOf("("));
    try {
      env[18] /*params*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Invocation)sub154.isSet("statement", env[12])).params()).iterator();
    } catch(final Exception e158) {
      throw sub154.ex(e158);
    }
    try {
      final StringCursor sub159 = sub154.sub();
      try {
        env[13] /*value*/= ((java.util.Iterator)sub159.isSet("params", env[18])).next();
      } catch(final Exception e160) {
        throw sub159.ex(e160);
      }
      Value(sub159, env);
      try {
        final StringCursor sub161 = sub159.sub();
        try {
          env[13] /*value*/= ((java.util.Iterator)sub161.isSet("params", env[18])).next();
        } catch(final Exception e162) {
          throw sub161.ex(e162);
        }
        sub161.publish(String.valueOf(", "));
        Value(sub161, env);
        sub161.commit();
        try {
          while(true) {
            final StringCursor sub163 = sub159.sub();
            try {
              env[13] /*value*/= ((java.util.Iterator)sub163.isSet("params", env[18])).next();
            } catch(final Exception e164) {
              throw sub163.ex(e164);
            }
            sub163.publish(String.valueOf(", "));
            Value(sub163, env);
            sub163.commit();
          }
        } catch(final GrinSerializerException e165) {
          //continue
        }
      } catch(final GrinSerializerException e166) {
        //continue
      }
      sub159.commit();
    } catch(final GrinSerializerException e167) {
      //continue
    }
    final StringCursor sub168 = sub154.sub();
    boolean b169 = true;
    try {
      if((Object)((java.util.Iterator)sub168.isSet("params", env[18])).hasNext() == Boolean.FALSE) {
        b169 = false;
      }
    } catch(final GrinSerializerException e170) {
      b169 = false;
    }
    if(b169){
      throw sub154.ex("unexpected value");
    }
    sub154.publish(String.valueOf(")"));
    sub154.commit();
    return null;
  }

  static Object Filter(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub171 = input.sub();
    final StringCursor sub172 = sub171.sub();
    if((Object)sub172.isSet("filter", env[16]) instanceof org.fuwjin.chessur.expression.Filter == Boolean.FALSE) {
      throw sub172.ex("check failed");
    }
    try {
      env[19] /*ranges*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Filter)sub171.isSet("filter", env[16])).ranges()).iterator();
    } catch(final Exception e173) {
      throw sub171.ex(e173);
    }
    try {
      env[20] /*range*/= ((java.util.Iterator)sub171.isSet("ranges", env[19])).next();
    } catch(final Exception e174) {
      throw sub171.ex(e174);
    }
    Range(sub171, env);
    try {
      final StringCursor sub175 = sub171.sub();
      try {
        env[20] /*range*/= ((java.util.Iterator)sub175.isSet("ranges", env[19])).next();
      } catch(final Exception e176) {
        throw sub175.ex(e176);
      }
      sub175.publish(String.valueOf(", "));
      Range(sub175, env);
      sub175.commit();
      try {
        while(true) {
          final StringCursor sub177 = sub171.sub();
          try {
            env[20] /*range*/= ((java.util.Iterator)sub177.isSet("ranges", env[19])).next();
          } catch(final Exception e178) {
            throw sub177.ex(e178);
          }
          sub177.publish(String.valueOf(", "));
          Range(sub177, env);
          sub177.commit();
        }
      } catch(final GrinSerializerException e179) {
        //continue
      }
    } catch(final GrinSerializerException e180) {
      //continue
    }
    final StringCursor sub181 = sub171.sub();
    boolean b182 = true;
    try {
      if((Object)((java.util.Iterator)sub181.isSet("ranges", env[19])).hasNext() == Boolean.FALSE) {
        b182 = false;
      }
    } catch(final GrinSerializerException e183) {
      b182 = false;
    }
    if(b182){
      throw sub171.ex("unexpected value");
    }
    sub171.commit();
    return null;
  }

  static Object Range(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub184 = input.sub();
    final StringCursor sub185 = sub184.sub();
    if((Object)sub185.isSet("range", env[20]) instanceof org.fuwjin.util.CodePointSet.Range == Boolean.FALSE) {
      throw sub185.ex("check failed");
    }
    try {
      env[21] /*ch*/= ((org.fuwjin.util.CodePointSet.Range)sub184.isSet("range", env[20])).start();
    } catch(final Exception e186) {
      throw sub184.ex(e186);
    }
    sub184.publish(org.fuwjin.chessur.expression.Filter.escape((java.lang.Integer)sub184.isSet("ch", env[21])));
    try {
      final StringCursor sub187 = sub184.sub();
      final StringCursor sub188 = sub187.sub();
      if((Object)((org.fuwjin.util.CodePointSet.Range)sub188.isSet("range", env[20])).isRange() == Boolean.FALSE) {
        throw sub188.ex("check failed");
      }
      sub187.publish("-");
      try {
        env[21] /*ch*/= ((org.fuwjin.util.CodePointSet.Range)sub187.isSet("range", env[20])).end();
      } catch(final Exception e189) {
        throw sub187.ex(e189);
      }
      sub187.publish(org.fuwjin.chessur.expression.Filter.escape((java.lang.Integer)sub187.isSet("ch", env[21])));
      sub187.commit();
    } catch(final GrinSerializerException e190) {
      //continue
    }
    sub184.commit();
    return null;
  }

  static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub191 = input.sub();
    final StringCursor sub192 = sub191.sub();
    if((Object)sub192.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.Literal == Boolean.FALSE) {
      throw sub192.ex("check failed");
    }
    sub191.publish("'");
    try {
      env[22] /*chars*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Literal)sub191.isSet("value", env[13])).chars()).iterator();
    } catch(final Exception e193) {
      throw sub191.ex(e193);
    }
    try {
      final StringCursor sub194 = sub191.sub();
      try {
        env[21] /*ch*/= ((java.util.Iterator)sub194.isSet("chars", env[22])).next();
      } catch(final Exception e195) {
        throw sub194.ex(e195);
      }
      sub194.publish(org.fuwjin.chessur.expression.Literal.escape((java.lang.Integer)sub194.isSet("ch", env[21])));
      sub194.commit();
      try {
        while(true) {
          final StringCursor sub196 = sub191.sub();
          try {
            env[21] /*ch*/= ((java.util.Iterator)sub196.isSet("chars", env[22])).next();
          } catch(final Exception e197) {
            throw sub196.ex(e197);
          }
          sub196.publish(org.fuwjin.chessur.expression.Literal.escape((java.lang.Integer)sub196.isSet("ch", env[21])));
          sub196.commit();
        }
      } catch(final GrinSerializerException e198) {
        //continue
      }
    } catch(final GrinSerializerException e199) {
      //continue
    }
    final StringCursor sub200 = sub191.sub();
    boolean b201 = true;
    try {
      if((Object)((java.util.Iterator)sub200.isSet("chars", env[22])).hasNext() == Boolean.FALSE) {
        b201 = false;
      }
    } catch(final GrinSerializerException e202) {
      b201 = false;
    }
    if(b201){
      throw sub191.ex("unexpected value");
    }
    sub191.publish("'");
    sub191.commit();
    return null;
  }

  static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub203 = input.sub();
    final StringCursor sub204 = sub203.sub();
    if((Object)sub204.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.CompositeLiteral == Boolean.FALSE) {
      throw sub204.ex("check failed");
    }
    env[23] /*composite*/= sub203.isSet("value", env[13]);
    sub203.publish("\"");
    try {
      env[24] /*values*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.CompositeLiteral)sub203.isSet("composite", env[23])).values()).iterator();
    } catch(final Exception e205) {
      throw sub203.ex(e205);
    }
    try {
      final StringCursor sub206 = sub203.sub();
      try {
        env[13] /*value*/= ((java.util.Iterator)sub206.isSet("values", env[24])).next();
      } catch(final Exception e207) {
        throw sub206.ex(e207);
      }
      try {
        final StringCursor sub208 = sub206.sub();
        final StringCursor sub209 = sub208.sub();
        if((Object)sub209.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.Literal == Boolean.FALSE) {
          throw sub209.ex("check failed");
        }
        try {
          env[22] /*chars*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Literal)sub208.isSet("value", env[13])).chars()).iterator();
        } catch(final Exception e210) {
          throw sub208.ex(e210);
        }
        try {
          final StringCursor sub211 = sub208.sub();
          try {
            env[21] /*ch*/= ((java.util.Iterator)sub211.isSet("chars", env[22])).next();
          } catch(final Exception e212) {
            throw sub211.ex(e212);
          }
          sub211.publish(org.fuwjin.chessur.expression.CompositeLiteral.escape((java.lang.Integer)sub211.isSet("ch", env[21])));
          sub211.commit();
          try {
            while(true) {
              final StringCursor sub213 = sub208.sub();
              try {
                env[21] /*ch*/= ((java.util.Iterator)sub213.isSet("chars", env[22])).next();
              } catch(final Exception e214) {
                throw sub213.ex(e214);
              }
              sub213.publish(org.fuwjin.chessur.expression.CompositeLiteral.escape((java.lang.Integer)sub213.isSet("ch", env[21])));
              sub213.commit();
            }
          } catch(final GrinSerializerException e215) {
            //continue
          }
        } catch(final GrinSerializerException e216) {
          //continue
        }
        final StringCursor sub217 = sub208.sub();
        boolean b218 = true;
        try {
          if((Object)((java.util.Iterator)sub217.isSet("chars", env[22])).hasNext() == Boolean.FALSE) {
            b218 = false;
          }
        } catch(final GrinSerializerException e219) {
          b218 = false;
        }
        if(b218){
          throw sub208.ex("unexpected value");
        }
        sub208.commit();
      } catch(final GrinSerializerException e220) {
        final StringCursor sub221 = sub206.sub();
        sub221.publish(String.valueOf("'"));
        Value(sub221, env);
        sub221.publish(String.valueOf("'"));
        sub221.commit();
      }
      sub206.commit();
      try {
        while(true) {
          final StringCursor sub222 = sub203.sub();
          try {
            env[13] /*value*/= ((java.util.Iterator)sub222.isSet("values", env[24])).next();
          } catch(final Exception e223) {
            throw sub222.ex(e223);
          }
          try {
            final StringCursor sub224 = sub222.sub();
            final StringCursor sub225 = sub224.sub();
            if((Object)sub225.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.Literal == Boolean.FALSE) {
              throw sub225.ex("check failed");
            }
            try {
              env[22] /*chars*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Literal)sub224.isSet("value", env[13])).chars()).iterator();
            } catch(final Exception e226) {
              throw sub224.ex(e226);
            }
            try {
              final StringCursor sub227 = sub224.sub();
              try {
                env[21] /*ch*/= ((java.util.Iterator)sub227.isSet("chars", env[22])).next();
              } catch(final Exception e228) {
                throw sub227.ex(e228);
              }
              sub227.publish(org.fuwjin.chessur.expression.CompositeLiteral.escape((java.lang.Integer)sub227.isSet("ch", env[21])));
              sub227.commit();
              try {
                while(true) {
                  final StringCursor sub229 = sub224.sub();
                  try {
                    env[21] /*ch*/= ((java.util.Iterator)sub229.isSet("chars", env[22])).next();
                  } catch(final Exception e230) {
                    throw sub229.ex(e230);
                  }
                  sub229.publish(org.fuwjin.chessur.expression.CompositeLiteral.escape((java.lang.Integer)sub229.isSet("ch", env[21])));
                  sub229.commit();
                }
              } catch(final GrinSerializerException e231) {
                //continue
              }
            } catch(final GrinSerializerException e232) {
              //continue
            }
            final StringCursor sub233 = sub224.sub();
            boolean b234 = true;
            try {
              if((Object)((java.util.Iterator)sub233.isSet("chars", env[22])).hasNext() == Boolean.FALSE) {
                b234 = false;
              }
            } catch(final GrinSerializerException e235) {
              b234 = false;
            }
            if(b234){
              throw sub224.ex("unexpected value");
            }
            sub224.commit();
          } catch(final GrinSerializerException e236) {
            final StringCursor sub237 = sub222.sub();
            sub237.publish(String.valueOf("'"));
            Value(sub237, env);
            sub237.publish(String.valueOf("'"));
            sub237.commit();
          }
          sub222.commit();
        }
      } catch(final GrinSerializerException e238) {
        //continue
      }
    } catch(final GrinSerializerException e239) {
      //continue
    }
    final StringCursor sub240 = sub203.sub();
    boolean b241 = true;
    try {
      if((Object)((java.util.Iterator)sub240.isSet("values", env[24])).hasNext() == Boolean.FALSE) {
        b241 = false;
      }
    } catch(final GrinSerializerException e242) {
      b241 = false;
    }
    if(b241){
      throw sub203.ex("unexpected value");
    }
    sub203.publish("\"");
    sub203.commit();
    return null;
  }

  static Object Script(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub243 = input.sub();
    try {
      final StringCursor sub244 = sub243.sub();
      final StringCursor sub245 = sub244.sub();
      if((Object)sub245.isSet("statement", env[12]) instanceof org.fuwjin.chessur.Script == Boolean.FALSE) {
        throw sub245.ex("check failed");
      }
      try {
        env[6] /*name*/= ((org.fuwjin.chessur.Script)sub244.isSet("statement", env[12])).name();
      } catch(final Exception e246) {
        throw sub244.ex(e246);
      }
      sub244.publish(String.valueOf("<") + String.valueOf(sub244.isSet("name", env[6])) + String.valueOf(">"));
      sub244.commit();
    } catch(final GrinSerializerException e247) {
      try {
        final StringCursor sub248 = sub243.sub();
        final StringCursor sub249 = sub248.sub();
        if((Object)sub249.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.ScriptOutput == Boolean.FALSE) {
          throw sub249.ex("check failed");
        }
        try {
          env[6] /*name*/= ((org.fuwjin.chessur.expression.ScriptOutput)sub248.isSet("statement", env[12])).name();
        } catch(final Exception e250) {
          throw sub248.ex(e250);
        }
        try {
          env[12] /*statement*/= ((org.fuwjin.chessur.expression.ScriptOutput)sub248.isSet("statement", env[12])).spec();
        } catch(final Exception e251) {
          throw sub248.ex(e251);
        }
        Script(sub248, env);
        sub248.publish(String.valueOf(" >> ") + String.valueOf(sub248.isSet("name", env[6])));
        sub248.commit();
      } catch(final GrinSerializerException e252) {
        try {
          final StringCursor sub253 = sub243.sub();
          final StringCursor sub254 = sub253.sub();
          if((Object)sub254.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.ScriptPipe == Boolean.FALSE) {
            throw sub254.ex("check failed");
          }
          env[25] /*pipe*/= sub253.isSet("statement", env[12]);
          try {
            env[12] /*statement*/= ((org.fuwjin.chessur.expression.ScriptPipe)sub253.isSet("pipe", env[25])).source();
          } catch(final Exception e255) {
            throw sub253.ex(e255);
          }
          Script(sub253, env);
          sub253.publish(String.valueOf(" >> "));
          try {
            env[12] /*statement*/= ((org.fuwjin.chessur.expression.ScriptPipe)sub253.isSet("pipe", env[25])).sink();
          } catch(final Exception e256) {
            throw sub253.ex(e256);
          }
          Script(sub253, env);
          sub253.commit();
        } catch(final GrinSerializerException e257) {
          final StringCursor sub258 = sub243.sub();
          final StringCursor sub259 = sub258.sub();
          if((Object)sub259.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.ScriptInput == Boolean.FALSE) {
            throw sub259.ex("check failed");
          }
          env[26] /*input*/= sub258.isSet("statement", env[12]);
          try {
            env[12] /*statement*/= ((org.fuwjin.chessur.expression.ScriptInput)sub258.isSet("input", env[26])).spec();
          } catch(final Exception e260) {
            throw sub258.ex(e260);
          }
          Script(sub258, env);
          sub258.publish(String.valueOf(" << "));
          try {
            env[13] /*value*/= ((org.fuwjin.chessur.expression.ScriptInput)sub258.isSet("input", env[26])).value();
          } catch(final Exception e261) {
            throw sub258.ex(e261);
          }
          Value(sub258, env);
          sub258.commit();
        }
      }
    }
    sub243.commit();
    return null;
  }

  static Object Object(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub262 = input.sub();
    final StringCursor sub263 = sub262.sub();
    if((Object)sub263.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.ObjectTemplate == Boolean.FALSE) {
      throw sub263.ex("check failed");
    }
    try {
      env[6] /*name*/= ((org.fuwjin.chessur.expression.ObjectTemplate)sub262.isSet("value", env[13])).type();
    } catch(final Exception e264) {
      throw sub262.ex(e264);
    }
    try {
      env[8] /*alias*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub262.isSet("cat", env[1])).encode((java.lang.String)sub262.isSet("name", env[6]));
    } catch(final Exception e265) {
      throw sub262.ex(e265);
    }
    sub262.publish(String.valueOf("(") + String.valueOf(sub262.isSet("alias", env[8])) + String.valueOf(")"));
    try {
      ((org.fuwjin.util.Indent)sub262.isSet("indent", env[10])).increase();
    } catch(final Exception e266) {
      throw sub262.ex(e266);
    }
    try {
      env[27] /*setters*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.ObjectTemplate)sub262.isSet("value", env[13])).setters()).iterator();
    } catch(final Exception e267) {
      throw sub262.ex(e267);
    }
    env[28] /*delim*/= "{";
    final StringCursor sub268 = sub262.sub();
    sub268.publish(sub268.isSet("delim", env[28]));
    env[28] /*delim*/= ",";
    try {
      env[29] /*setter*/= ((java.util.Iterator)sub268.isSet("setters", env[27])).next();
    } catch(final Exception e269) {
      throw sub268.ex(e269);
    }
    try {
      env[6] /*name*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub268.isSet("setter", env[29])).name();
    } catch(final Exception e270) {
      throw sub268.ex(e270);
    }
    sub268.publish(String.valueOf(sub268.isSet("indent", env[10])) + String.valueOf(sub268.isSet("name", env[6])) + String.valueOf(": "));
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub268.isSet("setter", env[29])).value();
    } catch(final Exception e271) {
      throw sub268.ex(e271);
    }
    Value(sub268, env);
    sub268.commit();
    try {
      while(true) {
        final StringCursor sub272 = sub262.sub();
        sub272.publish(sub272.isSet("delim", env[28]));
        env[28] /*delim*/= ",";
        try {
          env[29] /*setter*/= ((java.util.Iterator)sub272.isSet("setters", env[27])).next();
        } catch(final Exception e273) {
          throw sub272.ex(e273);
        }
        try {
          env[6] /*name*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub272.isSet("setter", env[29])).name();
        } catch(final Exception e274) {
          throw sub272.ex(e274);
        }
        sub272.publish(String.valueOf(sub272.isSet("indent", env[10])) + String.valueOf(sub272.isSet("name", env[6])) + String.valueOf(": "));
        try {
          env[13] /*value*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub272.isSet("setter", env[29])).value();
        } catch(final Exception e275) {
          throw sub272.ex(e275);
        }
        Value(sub272, env);
        sub272.commit();
      }
    } catch(final GrinSerializerException e276) {
      //continue
    }
    final StringCursor sub277 = sub262.sub();
    boolean b278 = true;
    try {
      if((Object)((java.util.Iterator)sub277.isSet("setters", env[27])).hasNext() == Boolean.FALSE) {
        b278 = false;
      }
    } catch(final GrinSerializerException e279) {
      b278 = false;
    }
    if(b278){
      throw sub262.ex("unexpected value");
    }
    try {
      ((org.fuwjin.util.Indent)sub262.isSet("indent", env[10])).decrease();
    } catch(final Exception e280) {
      throw sub262.ex(e280);
    }
    sub262.publish(String.valueOf(sub262.isSet("indent", env[10])) + String.valueOf("}"));
    sub262.commit();
    return null;
  }

  static Object Number(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub281 = input.sub();
    final StringCursor sub282 = sub281.sub();
    if((Object)sub282.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.Number == Boolean.FALSE) {
      throw sub282.ex("check failed");
    }
    sub281.publish(((org.fuwjin.chessur.expression.Number)sub281.isSet("value", env[13])).toString());
    sub281.commit();
    return null;
  }

  static Object Variable(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub283 = input.sub();
    final StringCursor sub284 = sub283.sub();
    if((Object)sub284.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.Variable == Boolean.FALSE) {
      throw sub284.ex("check failed");
    }
    sub283.publish(((org.fuwjin.chessur.expression.Variable)sub283.isSet("value", env[13])).name());
    sub283.commit();
    return null;
  }
  
  public static Object interpret(final CharSequence in, final Appendable out, final Map<String, ?> environment) throws GrinSerializerException {
    final StringCursor input = new StringCursor(in, out);
    final Object[] env = new Object[30];
    env[0] = environment.containsKey("modules") ? environment.get("modules") : UNSET;
    env[1] = environment.containsKey("cat") ? environment.get("cat") : UNSET;
    env[2] = environment.containsKey("aliases") ? environment.get("aliases") : UNSET;
    env[3] = environment.containsKey("specs") ? environment.get("specs") : UNSET;
    env[4] = environment.containsKey("module") ? environment.get("module") : UNSET;
    env[5] = environment.containsKey("path") ? environment.get("path") : UNSET;
    env[6] = environment.containsKey("name") ? environment.get("name") : UNSET;
    env[7] = environment.containsKey("entry") ? environment.get("entry") : UNSET;
    env[8] = environment.containsKey("alias") ? environment.get("alias") : UNSET;
    env[9] = environment.containsKey("spec") ? environment.get("spec") : UNSET;
    env[10] = environment.containsKey("indent") ? environment.get("indent") : UNSET;
    env[11] = environment.containsKey("statements") ? environment.get("statements") : UNSET;
    env[12] = environment.containsKey("statement") ? environment.get("statement") : UNSET;
    env[13] = environment.containsKey("value") ? environment.get("value") : UNSET;
    env[14] = environment.containsKey("stmt") ? environment.get("stmt") : UNSET;
    env[15] = environment.containsKey("old") ? environment.get("old") : UNSET;
    env[16] = environment.containsKey("filter") ? environment.get("filter") : UNSET;
    env[17] = environment.containsKey("block") ? environment.get("block") : UNSET;
    env[18] = environment.containsKey("params") ? environment.get("params") : UNSET;
    env[19] = environment.containsKey("ranges") ? environment.get("ranges") : UNSET;
    env[20] = environment.containsKey("range") ? environment.get("range") : UNSET;
    env[21] = environment.containsKey("ch") ? environment.get("ch") : UNSET;
    env[22] = environment.containsKey("chars") ? environment.get("chars") : UNSET;
    env[23] = environment.containsKey("composite") ? environment.get("composite") : UNSET;
    env[24] = environment.containsKey("values") ? environment.get("values") : UNSET;
    env[25] = environment.containsKey("pipe") ? environment.get("pipe") : UNSET;
    env[26] = environment.containsKey("input") ? environment.get("input") : UNSET;
    env[27] = environment.containsKey("setters") ? environment.get("setters") : UNSET;
    env[28] = environment.containsKey("delim") ? environment.get("delim") : UNSET;
    env[29] = environment.containsKey("setter") ? environment.get("setter") : UNSET;
    return Catalog(input, env);
  }
}
