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
          CouldStatement(sub55, env);
        } catch(final GrinSerializerException e58) {
          try {
            RepeatStatement(sub55, env);
          } catch(final GrinSerializerException e59) {
            try {
              AcceptStatement(sub55, env);
            } catch(final GrinSerializerException e60) {
              try {
                PublishStatement(sub55, env);
              } catch(final GrinSerializerException e61) {
                try {
                  AbortStatement(sub55, env);
                } catch(final GrinSerializerException e62) {
                  try {
                    Script(sub55, env);
                  } catch(final GrinSerializerException e63) {
                    try {
                      Block(sub55, env);
                    } catch(final GrinSerializerException e64) {
                      try {
                        Assignment(sub55, env);
                      } catch(final GrinSerializerException e65) {
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
    sub55.commit();
    return null;
  }

  static Object AssumeStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub66 = input.sub();
    final StringCursor sub67 = sub66.sub();
    if((Object)sub67.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.AssumeStatement == Boolean.FALSE) {
      throw sub67.ex("check failed");
    }
    sub66.publish(String.valueOf("assume "));
    try {
      final StringCursor sub68 = sub66.sub();
      final StringCursor sub69 = sub68.sub();
      if((Object)((org.fuwjin.chessur.expression.AssumeStatement)sub69.isSet("statement", env[12])).isNot() == Boolean.FALSE) {
        throw sub69.ex("check failed");
      }
      sub68.publish(String.valueOf("not "));
      sub68.commit();
    } catch(final GrinSerializerException e70) {
      //continue
    }
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.AssumeStatement)sub66.isSet("statement", env[12])).value();
    } catch(final Exception e71) {
      throw sub66.ex(e71);
    }
    try {
      Value(sub66, env);
    } catch(final GrinSerializerException e72) {
      final StringCursor sub73 = sub66.sub();
      env[12] /*statement*/= sub73.isSet("value", env[13]);
      Statement(sub73, env);
      sub73.commit();
    }
    sub66.commit();
    return null;
  }

  static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub74 = input.sub();
    final StringCursor sub75 = sub74.sub();
    if((Object)sub75.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.EitherOrStatement == Boolean.FALSE) {
      throw sub75.ex("check failed");
    }
    env[14] /*stmt*/= sub74.isSet("statement", env[12]);
    try {
      env[11] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.EitherOrStatement)sub74.isSet("stmt", env[14])).statements()).iterator();
    } catch(final Exception e76) {
      throw sub74.ex(e76);
    }
    sub74.publish(String.valueOf("either "));
    try {
      env[12] /*statement*/= ((java.util.Iterator)sub74.isSet("statements", env[11])).next();
    } catch(final Exception e77) {
      throw sub74.ex(e77);
    }
    Statement(sub74, env);
    final StringCursor sub78 = sub74.sub();
    env[15] /*old*/= sub78.isSet("statement", env[12]);
    try {
      env[12] /*statement*/= ((java.util.Iterator)sub78.isSet("statements", env[11])).next();
    } catch(final Exception e79) {
      throw sub78.ex(e79);
    }
    try {
      final StringCursor sub80 = sub78.sub();
      final StringCursor sub81 = sub80.sub();
      if((Object)sub81.isSet("old", env[15]) instanceof org.fuwjin.chessur.expression.Block == Boolean.FALSE) {
        throw sub81.ex("check failed");
      }
      sub80.publish(" ");
      sub80.commit();
    } catch(final GrinSerializerException e82) {
      sub78.publish(sub78.isSet("indent", env[10]));
    }
    sub78.publish(String.valueOf("or "));
    Statement(sub78, env);
    sub78.commit();
    try {
      while(true) {
        final StringCursor sub83 = sub74.sub();
        env[15] /*old*/= sub83.isSet("statement", env[12]);
        try {
          env[12] /*statement*/= ((java.util.Iterator)sub83.isSet("statements", env[11])).next();
        } catch(final Exception e84) {
          throw sub83.ex(e84);
        }
        try {
          final StringCursor sub85 = sub83.sub();
          final StringCursor sub86 = sub85.sub();
          if((Object)sub86.isSet("old", env[15]) instanceof org.fuwjin.chessur.expression.Block == Boolean.FALSE) {
            throw sub86.ex("check failed");
          }
          sub85.publish(" ");
          sub85.commit();
        } catch(final GrinSerializerException e87) {
          sub83.publish(sub83.isSet("indent", env[10]));
        }
        sub83.publish(String.valueOf("or "));
        Statement(sub83, env);
        sub83.commit();
      }
    } catch(final GrinSerializerException e88) {
      //continue
    }
    sub74.commit();
    return null;
  }

  static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub89 = input.sub();
    final StringCursor sub90 = sub89.sub();
    if((Object)sub90.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.CouldStatement == Boolean.FALSE) {
      throw sub90.ex("check failed");
    }
    env[14] /*stmt*/= sub89.isSet("statement", env[12]);
    sub89.publish(String.valueOf("could "));
    try {
      env[12] /*statement*/= ((org.fuwjin.chessur.expression.CouldStatement)sub89.isSet("stmt", env[14])).statement();
    } catch(final Exception e91) {
      throw sub89.ex(e91);
    }
    Statement(sub89, env);
    sub89.commit();
    return null;
  }

  static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub92 = input.sub();
    final StringCursor sub93 = sub92.sub();
    if((Object)sub93.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.RepeatStatement == Boolean.FALSE) {
      throw sub93.ex("check failed");
    }
    env[14] /*stmt*/= sub92.isSet("statement", env[12]);
    sub92.publish(String.valueOf("repeat "));
    try {
      env[12] /*statement*/= ((org.fuwjin.chessur.expression.RepeatStatement)sub92.isSet("stmt", env[14])).statement();
    } catch(final Exception e94) {
      throw sub92.ex(e94);
    }
    Statement(sub92, env);
    sub92.commit();
    return null;
  }

  static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub95 = input.sub();
    try {
      FilterAcceptStatement(sub95, env);
    } catch(final GrinSerializerException e96) {
      ValueAcceptStatement(sub95, env);
    }
    sub95.commit();
    return null;
  }

  static Object ValueAcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub97 = input.sub();
    final StringCursor sub98 = sub97.sub();
    if((Object)sub98.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.ValueAcceptStatement == Boolean.FALSE) {
      throw sub98.ex("check failed");
    }
    sub97.publish(String.valueOf("accept "));
    try {
      final StringCursor sub99 = sub97.sub();
      final StringCursor sub100 = sub99.sub();
      if((Object)((org.fuwjin.chessur.expression.ValueAcceptStatement)sub100.isSet("statement", env[12])).isNot() == Boolean.FALSE) {
        throw sub100.ex("check failed");
      }
      sub99.publish(String.valueOf("not "));
      sub99.commit();
    } catch(final GrinSerializerException e101) {
      //continue
    }
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.ValueAcceptStatement)sub97.isSet("statement", env[12])).value();
    } catch(final Exception e102) {
      throw sub97.ex(e102);
    }
    Value(sub97, env);
    sub97.commit();
    return null;
  }

  static Object FilterAcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub103 = input.sub();
    final StringCursor sub104 = sub103.sub();
    if((Object)sub104.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.FilterAcceptStatement == Boolean.FALSE) {
      throw sub104.ex("check failed");
    }
    sub103.publish(String.valueOf("accept "));
    try {
      final StringCursor sub105 = sub103.sub();
      final StringCursor sub106 = sub105.sub();
      if((Object)((org.fuwjin.chessur.expression.FilterAcceptStatement)sub106.isSet("statement", env[12])).isNot() == Boolean.FALSE) {
        throw sub106.ex("check failed");
      }
      sub105.publish(String.valueOf("not "));
      sub105.commit();
    } catch(final GrinSerializerException e107) {
      //continue
    }
    sub103.publish(String.valueOf("in "));
    try {
      env[16] /*filter*/= ((org.fuwjin.chessur.expression.FilterAcceptStatement)sub103.isSet("statement", env[12])).filter();
    } catch(final Exception e108) {
      throw sub103.ex(e108);
    }
    Filter(sub103, env);
    sub103.commit();
    return null;
  }

  static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub109 = input.sub();
    final StringCursor sub110 = sub109.sub();
    if((Object)sub110.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.PublishStatement == Boolean.FALSE) {
      throw sub110.ex("check failed");
    }
    sub109.publish(String.valueOf("publish "));
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.PublishStatement)sub109.isSet("statement", env[12])).value();
    } catch(final Exception e111) {
      throw sub109.ex(e111);
    }
    Value(sub109, env);
    sub109.commit();
    return null;
  }

  static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub112 = input.sub();
    final StringCursor sub113 = sub112.sub();
    if((Object)sub113.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.AbortStatement == Boolean.FALSE) {
      throw sub113.ex("check failed");
    }
    sub112.publish(String.valueOf("abort "));
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.AbortStatement)sub112.isSet("statement", env[12])).value();
    } catch(final Exception e114) {
      throw sub112.ex(e114);
    }
    Value(sub112, env);
    sub112.commit();
    return null;
  }

  static Object Block(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub115 = input.sub();
    final StringCursor sub116 = sub115.sub();
    if((Object)sub116.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.Block == Boolean.FALSE) {
      throw sub116.ex("check failed");
    }
    env[17] /*block*/= sub115.isSet("statement", env[12]);
    try {
      ((org.fuwjin.util.Indent)sub115.isSet("indent", env[10])).increase();
    } catch(final Exception e117) {
      throw sub115.ex(e117);
    }
    sub115.publish(String.valueOf("{"));
    try {
      env[11] /*statements*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Block)sub115.isSet("block", env[17])).statements()).iterator();
    } catch(final Exception e118) {
      throw sub115.ex(e118);
    }
    try {
      final StringCursor sub119 = sub115.sub();
      try {
        env[12] /*statement*/= ((java.util.Iterator)sub119.isSet("statements", env[11])).next();
      } catch(final Exception e120) {
        throw sub119.ex(e120);
      }
      sub119.publish(sub119.isSet("indent", env[10]));
      Statement(sub119, env);
      sub119.commit();
      try {
        while(true) {
          final StringCursor sub121 = sub115.sub();
          try {
            env[12] /*statement*/= ((java.util.Iterator)sub121.isSet("statements", env[11])).next();
          } catch(final Exception e122) {
            throw sub121.ex(e122);
          }
          sub121.publish(sub121.isSet("indent", env[10]));
          Statement(sub121, env);
          sub121.commit();
        }
      } catch(final GrinSerializerException e123) {
        //continue
      }
    } catch(final GrinSerializerException e124) {
      //continue
    }
    final StringCursor sub125 = sub115.sub();
    boolean b126 = true;
    try {
      if((Object)((java.util.Iterator)sub125.isSet("statements", env[11])).hasNext() == Boolean.FALSE) {
        b126 = false;
      }
    } catch(final GrinSerializerException e127) {
      b126 = false;
    }
    if(b126){
      throw sub115.ex("unexpected value");
    }
    try {
      ((org.fuwjin.util.Indent)sub115.isSet("indent", env[10])).decrease();
    } catch(final Exception e128) {
      throw sub115.ex(e128);
    }
    sub115.publish(sub115.isSet("indent", env[10]));
    sub115.publish(String.valueOf("}"));
    sub115.commit();
    return null;
  }

  static Object Assignment(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub129 = input.sub();
    final StringCursor sub130 = sub129.sub();
    if((Object)sub130.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.Assignment == Boolean.FALSE) {
      throw sub130.ex("check failed");
    }
    try {
      env[6] /*name*/= ((org.fuwjin.chessur.expression.Assignment)sub129.isSet("statement", env[12])).name();
    } catch(final Exception e131) {
      throw sub129.ex(e131);
    }
    sub129.publish(String.valueOf(sub129.isSet("name", env[6])) + String.valueOf(" = "));
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.Assignment)sub129.isSet("statement", env[12])).value();
    } catch(final Exception e132) {
      throw sub129.ex(e132);
    }
    Value(sub129, env);
    sub129.commit();
    return null;
  }

  static Object Invocation(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub133 = input.sub();
    final StringCursor sub134 = sub133.sub();
    if((Object)sub134.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.Invocation == Boolean.FALSE) {
      throw sub134.ex("check failed");
    }
    try {
      env[6] /*name*/= ((org.fuwjin.chessur.expression.Invocation)sub133.isSet("statement", env[12])).name();
    } catch(final Exception e135) {
      throw sub133.ex(e135);
    }
    try {
      env[8] /*alias*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub133.isSet("cat", env[1])).encode((java.lang.String)sub133.isSet("name", env[6]));
    } catch(final Exception e136) {
      throw sub133.ex(e136);
    }
    sub133.publish(String.valueOf(sub133.isSet("alias", env[8])) + String.valueOf("("));
    try {
      env[18] /*params*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Invocation)sub133.isSet("statement", env[12])).params()).iterator();
    } catch(final Exception e137) {
      throw sub133.ex(e137);
    }
    try {
      final StringCursor sub138 = sub133.sub();
      try {
        env[13] /*value*/= ((java.util.Iterator)sub138.isSet("params", env[18])).next();
      } catch(final Exception e139) {
        throw sub138.ex(e139);
      }
      Value(sub138, env);
      try {
        final StringCursor sub140 = sub138.sub();
        try {
          env[13] /*value*/= ((java.util.Iterator)sub140.isSet("params", env[18])).next();
        } catch(final Exception e141) {
          throw sub140.ex(e141);
        }
        sub140.publish(String.valueOf(", "));
        Value(sub140, env);
        sub140.commit();
        try {
          while(true) {
            final StringCursor sub142 = sub138.sub();
            try {
              env[13] /*value*/= ((java.util.Iterator)sub142.isSet("params", env[18])).next();
            } catch(final Exception e143) {
              throw sub142.ex(e143);
            }
            sub142.publish(String.valueOf(", "));
            Value(sub142, env);
            sub142.commit();
          }
        } catch(final GrinSerializerException e144) {
          //continue
        }
      } catch(final GrinSerializerException e145) {
        //continue
      }
      sub138.commit();
    } catch(final GrinSerializerException e146) {
      //continue
    }
    final StringCursor sub147 = sub133.sub();
    boolean b148 = true;
    try {
      if((Object)((java.util.Iterator)sub147.isSet("params", env[18])).hasNext() == Boolean.FALSE) {
        b148 = false;
      }
    } catch(final GrinSerializerException e149) {
      b148 = false;
    }
    if(b148){
      throw sub133.ex("unexpected value");
    }
    sub133.publish(String.valueOf(")"));
    sub133.commit();
    return null;
  }

  static Object Filter(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub150 = input.sub();
    final StringCursor sub151 = sub150.sub();
    if((Object)sub151.isSet("filter", env[16]) instanceof org.fuwjin.chessur.expression.Filter == Boolean.FALSE) {
      throw sub151.ex("check failed");
    }
    try {
      env[19] /*ranges*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Filter)sub150.isSet("filter", env[16])).ranges()).iterator();
    } catch(final Exception e152) {
      throw sub150.ex(e152);
    }
    try {
      env[20] /*range*/= ((java.util.Iterator)sub150.isSet("ranges", env[19])).next();
    } catch(final Exception e153) {
      throw sub150.ex(e153);
    }
    Range(sub150, env);
    try {
      final StringCursor sub154 = sub150.sub();
      try {
        env[20] /*range*/= ((java.util.Iterator)sub154.isSet("ranges", env[19])).next();
      } catch(final Exception e155) {
        throw sub154.ex(e155);
      }
      sub154.publish(String.valueOf(", "));
      Range(sub154, env);
      sub154.commit();
      try {
        while(true) {
          final StringCursor sub156 = sub150.sub();
          try {
            env[20] /*range*/= ((java.util.Iterator)sub156.isSet("ranges", env[19])).next();
          } catch(final Exception e157) {
            throw sub156.ex(e157);
          }
          sub156.publish(String.valueOf(", "));
          Range(sub156, env);
          sub156.commit();
        }
      } catch(final GrinSerializerException e158) {
        //continue
      }
    } catch(final GrinSerializerException e159) {
      //continue
    }
    final StringCursor sub160 = sub150.sub();
    boolean b161 = true;
    try {
      if((Object)((java.util.Iterator)sub160.isSet("ranges", env[19])).hasNext() == Boolean.FALSE) {
        b161 = false;
      }
    } catch(final GrinSerializerException e162) {
      b161 = false;
    }
    if(b161){
      throw sub150.ex("unexpected value");
    }
    sub150.commit();
    return null;
  }

  static Object Range(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub163 = input.sub();
    final StringCursor sub164 = sub163.sub();
    if((Object)sub164.isSet("range", env[20]) instanceof org.fuwjin.util.CodePointSet.Range == Boolean.FALSE) {
      throw sub164.ex("check failed");
    }
    try {
      env[21] /*ch*/= ((org.fuwjin.util.CodePointSet.Range)sub163.isSet("range", env[20])).start();
    } catch(final Exception e165) {
      throw sub163.ex(e165);
    }
    sub163.publish(org.fuwjin.chessur.expression.Filter.escape((java.lang.Integer)sub163.isSet("ch", env[21])));
    try {
      final StringCursor sub166 = sub163.sub();
      final StringCursor sub167 = sub166.sub();
      if((Object)((org.fuwjin.util.CodePointSet.Range)sub167.isSet("range", env[20])).isRange() == Boolean.FALSE) {
        throw sub167.ex("check failed");
      }
      sub166.publish("-");
      try {
        env[21] /*ch*/= ((org.fuwjin.util.CodePointSet.Range)sub166.isSet("range", env[20])).end();
      } catch(final Exception e168) {
        throw sub166.ex(e168);
      }
      sub166.publish(org.fuwjin.chessur.expression.Filter.escape((java.lang.Integer)sub166.isSet("ch", env[21])));
      sub166.commit();
    } catch(final GrinSerializerException e169) {
      //continue
    }
    sub163.commit();
    return null;
  }

  static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub170 = input.sub();
    final StringCursor sub171 = sub170.sub();
    if((Object)sub171.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.Literal == Boolean.FALSE) {
      throw sub171.ex("check failed");
    }
    sub170.publish("'");
    try {
      env[22] /*chars*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Literal)sub170.isSet("value", env[13])).chars()).iterator();
    } catch(final Exception e172) {
      throw sub170.ex(e172);
    }
    try {
      final StringCursor sub173 = sub170.sub();
      try {
        env[21] /*ch*/= ((java.util.Iterator)sub173.isSet("chars", env[22])).next();
      } catch(final Exception e174) {
        throw sub173.ex(e174);
      }
      sub173.publish(org.fuwjin.chessur.expression.Literal.escape((java.lang.Integer)sub173.isSet("ch", env[21])));
      sub173.commit();
      try {
        while(true) {
          final StringCursor sub175 = sub170.sub();
          try {
            env[21] /*ch*/= ((java.util.Iterator)sub175.isSet("chars", env[22])).next();
          } catch(final Exception e176) {
            throw sub175.ex(e176);
          }
          sub175.publish(org.fuwjin.chessur.expression.Literal.escape((java.lang.Integer)sub175.isSet("ch", env[21])));
          sub175.commit();
        }
      } catch(final GrinSerializerException e177) {
        //continue
      }
    } catch(final GrinSerializerException e178) {
      //continue
    }
    final StringCursor sub179 = sub170.sub();
    boolean b180 = true;
    try {
      if((Object)((java.util.Iterator)sub179.isSet("chars", env[22])).hasNext() == Boolean.FALSE) {
        b180 = false;
      }
    } catch(final GrinSerializerException e181) {
      b180 = false;
    }
    if(b180){
      throw sub170.ex("unexpected value");
    }
    sub170.publish("'");
    sub170.commit();
    return null;
  }

  static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub182 = input.sub();
    final StringCursor sub183 = sub182.sub();
    if((Object)sub183.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.CompositeLiteral == Boolean.FALSE) {
      throw sub183.ex("check failed");
    }
    env[23] /*composite*/= sub182.isSet("value", env[13]);
    sub182.publish("\"");
    try {
      env[24] /*values*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.CompositeLiteral)sub182.isSet("composite", env[23])).values()).iterator();
    } catch(final Exception e184) {
      throw sub182.ex(e184);
    }
    try {
      final StringCursor sub185 = sub182.sub();
      try {
        env[13] /*value*/= ((java.util.Iterator)sub185.isSet("values", env[24])).next();
      } catch(final Exception e186) {
        throw sub185.ex(e186);
      }
      try {
        final StringCursor sub187 = sub185.sub();
        final StringCursor sub188 = sub187.sub();
        if((Object)sub188.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.Literal == Boolean.FALSE) {
          throw sub188.ex("check failed");
        }
        try {
          env[22] /*chars*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Literal)sub187.isSet("value", env[13])).chars()).iterator();
        } catch(final Exception e189) {
          throw sub187.ex(e189);
        }
        try {
          final StringCursor sub190 = sub187.sub();
          try {
            env[21] /*ch*/= ((java.util.Iterator)sub190.isSet("chars", env[22])).next();
          } catch(final Exception e191) {
            throw sub190.ex(e191);
          }
          sub190.publish(org.fuwjin.chessur.expression.CompositeLiteral.escape((java.lang.Integer)sub190.isSet("ch", env[21])));
          sub190.commit();
          try {
            while(true) {
              final StringCursor sub192 = sub187.sub();
              try {
                env[21] /*ch*/= ((java.util.Iterator)sub192.isSet("chars", env[22])).next();
              } catch(final Exception e193) {
                throw sub192.ex(e193);
              }
              sub192.publish(org.fuwjin.chessur.expression.CompositeLiteral.escape((java.lang.Integer)sub192.isSet("ch", env[21])));
              sub192.commit();
            }
          } catch(final GrinSerializerException e194) {
            //continue
          }
        } catch(final GrinSerializerException e195) {
          //continue
        }
        final StringCursor sub196 = sub187.sub();
        boolean b197 = true;
        try {
          if((Object)((java.util.Iterator)sub196.isSet("chars", env[22])).hasNext() == Boolean.FALSE) {
            b197 = false;
          }
        } catch(final GrinSerializerException e198) {
          b197 = false;
        }
        if(b197){
          throw sub187.ex("unexpected value");
        }
        sub187.commit();
      } catch(final GrinSerializerException e199) {
        final StringCursor sub200 = sub185.sub();
        sub200.publish(String.valueOf("'"));
        Value(sub200, env);
        sub200.publish(String.valueOf("'"));
        sub200.commit();
      }
      sub185.commit();
      try {
        while(true) {
          final StringCursor sub201 = sub182.sub();
          try {
            env[13] /*value*/= ((java.util.Iterator)sub201.isSet("values", env[24])).next();
          } catch(final Exception e202) {
            throw sub201.ex(e202);
          }
          try {
            final StringCursor sub203 = sub201.sub();
            final StringCursor sub204 = sub203.sub();
            if((Object)sub204.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.Literal == Boolean.FALSE) {
              throw sub204.ex("check failed");
            }
            try {
              env[22] /*chars*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.Literal)sub203.isSet("value", env[13])).chars()).iterator();
            } catch(final Exception e205) {
              throw sub203.ex(e205);
            }
            try {
              final StringCursor sub206 = sub203.sub();
              try {
                env[21] /*ch*/= ((java.util.Iterator)sub206.isSet("chars", env[22])).next();
              } catch(final Exception e207) {
                throw sub206.ex(e207);
              }
              sub206.publish(org.fuwjin.chessur.expression.CompositeLiteral.escape((java.lang.Integer)sub206.isSet("ch", env[21])));
              sub206.commit();
              try {
                while(true) {
                  final StringCursor sub208 = sub203.sub();
                  try {
                    env[21] /*ch*/= ((java.util.Iterator)sub208.isSet("chars", env[22])).next();
                  } catch(final Exception e209) {
                    throw sub208.ex(e209);
                  }
                  sub208.publish(org.fuwjin.chessur.expression.CompositeLiteral.escape((java.lang.Integer)sub208.isSet("ch", env[21])));
                  sub208.commit();
                }
              } catch(final GrinSerializerException e210) {
                //continue
              }
            } catch(final GrinSerializerException e211) {
              //continue
            }
            final StringCursor sub212 = sub203.sub();
            boolean b213 = true;
            try {
              if((Object)((java.util.Iterator)sub212.isSet("chars", env[22])).hasNext() == Boolean.FALSE) {
                b213 = false;
              }
            } catch(final GrinSerializerException e214) {
              b213 = false;
            }
            if(b213){
              throw sub203.ex("unexpected value");
            }
            sub203.commit();
          } catch(final GrinSerializerException e215) {
            final StringCursor sub216 = sub201.sub();
            sub216.publish(String.valueOf("'"));
            Value(sub216, env);
            sub216.publish(String.valueOf("'"));
            sub216.commit();
          }
          sub201.commit();
        }
      } catch(final GrinSerializerException e217) {
        //continue
      }
    } catch(final GrinSerializerException e218) {
      //continue
    }
    final StringCursor sub219 = sub182.sub();
    boolean b220 = true;
    try {
      if((Object)((java.util.Iterator)sub219.isSet("values", env[24])).hasNext() == Boolean.FALSE) {
        b220 = false;
      }
    } catch(final GrinSerializerException e221) {
      b220 = false;
    }
    if(b220){
      throw sub182.ex("unexpected value");
    }
    sub182.publish("\"");
    sub182.commit();
    return null;
  }

  static Object Script(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub222 = input.sub();
    try {
      final StringCursor sub223 = sub222.sub();
      final StringCursor sub224 = sub223.sub();
      if((Object)sub224.isSet("statement", env[12]) instanceof org.fuwjin.chessur.Script == Boolean.FALSE) {
        throw sub224.ex("check failed");
      }
      try {
        env[6] /*name*/= ((org.fuwjin.chessur.Script)sub223.isSet("statement", env[12])).name();
      } catch(final Exception e225) {
        throw sub223.ex(e225);
      }
      sub223.publish(String.valueOf("<") + String.valueOf(sub223.isSet("name", env[6])) + String.valueOf(">"));
      sub223.commit();
    } catch(final GrinSerializerException e226) {
      try {
        final StringCursor sub227 = sub222.sub();
        final StringCursor sub228 = sub227.sub();
        if((Object)sub228.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.ScriptOutput == Boolean.FALSE) {
          throw sub228.ex("check failed");
        }
        try {
          env[6] /*name*/= ((org.fuwjin.chessur.expression.ScriptOutput)sub227.isSet("statement", env[12])).name();
        } catch(final Exception e229) {
          throw sub227.ex(e229);
        }
        try {
          env[12] /*statement*/= ((org.fuwjin.chessur.expression.ScriptOutput)sub227.isSet("statement", env[12])).spec();
        } catch(final Exception e230) {
          throw sub227.ex(e230);
        }
        Script(sub227, env);
        sub227.publish(String.valueOf(" >> ") + String.valueOf(sub227.isSet("name", env[6])));
        sub227.commit();
      } catch(final GrinSerializerException e231) {
        try {
          final StringCursor sub232 = sub222.sub();
          final StringCursor sub233 = sub232.sub();
          if((Object)sub233.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.ScriptPipe == Boolean.FALSE) {
            throw sub233.ex("check failed");
          }
          env[25] /*pipe*/= sub232.isSet("statement", env[12]);
          try {
            env[12] /*statement*/= ((org.fuwjin.chessur.expression.ScriptPipe)sub232.isSet("pipe", env[25])).source();
          } catch(final Exception e234) {
            throw sub232.ex(e234);
          }
          Script(sub232, env);
          sub232.publish(String.valueOf(" >> "));
          try {
            env[12] /*statement*/= ((org.fuwjin.chessur.expression.ScriptPipe)sub232.isSet("pipe", env[25])).sink();
          } catch(final Exception e235) {
            throw sub232.ex(e235);
          }
          Script(sub232, env);
          sub232.commit();
        } catch(final GrinSerializerException e236) {
          final StringCursor sub237 = sub222.sub();
          final StringCursor sub238 = sub237.sub();
          if((Object)sub238.isSet("statement", env[12]) instanceof org.fuwjin.chessur.expression.ScriptInput == Boolean.FALSE) {
            throw sub238.ex("check failed");
          }
          env[26] /*input*/= sub237.isSet("statement", env[12]);
          try {
            env[12] /*statement*/= ((org.fuwjin.chessur.expression.ScriptInput)sub237.isSet("input", env[26])).spec();
          } catch(final Exception e239) {
            throw sub237.ex(e239);
          }
          Script(sub237, env);
          sub237.publish(String.valueOf(" << "));
          try {
            env[13] /*value*/= ((org.fuwjin.chessur.expression.ScriptInput)sub237.isSet("input", env[26])).value();
          } catch(final Exception e240) {
            throw sub237.ex(e240);
          }
          Value(sub237, env);
          sub237.commit();
        }
      }
    }
    sub222.commit();
    return null;
  }

  static Object Object(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub241 = input.sub();
    final StringCursor sub242 = sub241.sub();
    if((Object)sub242.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.ObjectTemplate == Boolean.FALSE) {
      throw sub242.ex("check failed");
    }
    try {
      env[6] /*name*/= ((org.fuwjin.chessur.expression.ObjectTemplate)sub241.isSet("value", env[13])).type();
    } catch(final Exception e243) {
      throw sub241.ex(e243);
    }
    try {
      env[8] /*alias*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub241.isSet("cat", env[1])).encode((java.lang.String)sub241.isSet("name", env[6]));
    } catch(final Exception e244) {
      throw sub241.ex(e244);
    }
    sub241.publish(String.valueOf("(") + String.valueOf(sub241.isSet("alias", env[8])) + String.valueOf(")"));
    try {
      ((org.fuwjin.util.Indent)sub241.isSet("indent", env[10])).increase();
    } catch(final Exception e245) {
      throw sub241.ex(e245);
    }
    try {
      env[27] /*setters*/= ((java.lang.Iterable)((org.fuwjin.chessur.expression.ObjectTemplate)sub241.isSet("value", env[13])).setters()).iterator();
    } catch(final Exception e246) {
      throw sub241.ex(e246);
    }
    env[28] /*delim*/= "{";
    final StringCursor sub247 = sub241.sub();
    sub247.publish(sub247.isSet("delim", env[28]));
    env[28] /*delim*/= ",";
    try {
      env[29] /*setter*/= ((java.util.Iterator)sub247.isSet("setters", env[27])).next();
    } catch(final Exception e248) {
      throw sub247.ex(e248);
    }
    try {
      env[6] /*name*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub247.isSet("setter", env[29])).name();
    } catch(final Exception e249) {
      throw sub247.ex(e249);
    }
    sub247.publish(String.valueOf(sub247.isSet("indent", env[10])) + String.valueOf(sub247.isSet("name", env[6])) + String.valueOf(": "));
    try {
      env[13] /*value*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub247.isSet("setter", env[29])).value();
    } catch(final Exception e250) {
      throw sub247.ex(e250);
    }
    Value(sub247, env);
    sub247.commit();
    try {
      while(true) {
        final StringCursor sub251 = sub241.sub();
        sub251.publish(sub251.isSet("delim", env[28]));
        env[28] /*delim*/= ",";
        try {
          env[29] /*setter*/= ((java.util.Iterator)sub251.isSet("setters", env[27])).next();
        } catch(final Exception e252) {
          throw sub251.ex(e252);
        }
        try {
          env[6] /*name*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub251.isSet("setter", env[29])).name();
        } catch(final Exception e253) {
          throw sub251.ex(e253);
        }
        sub251.publish(String.valueOf(sub251.isSet("indent", env[10])) + String.valueOf(sub251.isSet("name", env[6])) + String.valueOf(": "));
        try {
          env[13] /*value*/= ((org.fuwjin.chessur.expression.FieldTemplate)sub251.isSet("setter", env[29])).value();
        } catch(final Exception e254) {
          throw sub251.ex(e254);
        }
        Value(sub251, env);
        sub251.commit();
      }
    } catch(final GrinSerializerException e255) {
      //continue
    }
    final StringCursor sub256 = sub241.sub();
    boolean b257 = true;
    try {
      if((Object)((java.util.Iterator)sub256.isSet("setters", env[27])).hasNext() == Boolean.FALSE) {
        b257 = false;
      }
    } catch(final GrinSerializerException e258) {
      b257 = false;
    }
    if(b257){
      throw sub241.ex("unexpected value");
    }
    try {
      ((org.fuwjin.util.Indent)sub241.isSet("indent", env[10])).decrease();
    } catch(final Exception e259) {
      throw sub241.ex(e259);
    }
    sub241.publish(String.valueOf(sub241.isSet("indent", env[10])) + String.valueOf("}"));
    sub241.commit();
    return null;
  }

  static Object Number(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub260 = input.sub();
    final StringCursor sub261 = sub260.sub();
    if((Object)sub261.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.Number == Boolean.FALSE) {
      throw sub261.ex("check failed");
    }
    sub260.publish(((org.fuwjin.chessur.expression.Number)sub260.isSet("value", env[13])).toString());
    sub260.commit();
    return null;
  }

  static Object Variable(final StringCursor input, final Object... parentEnv) throws GrinSerializerException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub262 = input.sub();
    final StringCursor sub263 = sub262.sub();
    if((Object)sub263.isSet("value", env[13]) instanceof org.fuwjin.chessur.expression.Variable == Boolean.FALSE) {
      throw sub263.ex("check failed");
    }
    sub262.publish(((org.fuwjin.chessur.expression.Variable)sub262.isSet("value", env[13])).name());
    sub262.commit();
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
