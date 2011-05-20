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

public class GrinFormatter {
  static final Object UNSET = new Object() {
    public String toString() {
      return "UNSET";
    }
  };

  public static class GrinFormatterException extends Exception {
    private static final long serialVersionUID = 1; 
    GrinFormatterException(final String message) {
      super(message);
    }
    
    GrinFormatterException(final String message, final Throwable cause) {
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
    
    public int accept() throws GrinFormatterException {
      checkBounds(pos);
      return advance();
    }
    
    public int accept(final String expected) throws GrinFormatterException {
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
    
    public int acceptIn(final String name, final String set) throws GrinFormatterException {
      checkBounds(pos);
      if(set.indexOf(seq.charAt(pos)) < 0) {
        throw ex("Did not match filter: "+name);
      }
      return advance();
    }
    
    public int acceptNot(final String expected) throws GrinFormatterException {
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
    
    public int acceptNotIn(final String name, final String set) throws GrinFormatterException {
      checkBounds(pos);
      if(set.indexOf(seq.charAt(pos)) >= 0) {
        throw ex("Unexpected match: "+name);
      }
      return advance();
    }
    
    public void publish(final Object value) throws GrinFormatterException {
      try {
        appender.append(value.toString());
      } catch(IOException e) {
        throw ex(e);
      }
    }
    
    public Object isSet(final String name, final Object value) throws GrinFormatterException {
      if(UNSET.equals(value)) {
        throw ex("variable "+name+" is unset");
      }
      return value;
    }
    
    protected void checkBounds(final int p) throws GrinFormatterException {
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
    
    public GrinFormatterException ex(final String message) {
      return new GrinFormatterException(message + context());
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
    
    public GrinFormatterException ex(final Throwable cause) {
      return new GrinFormatterException(context(), cause);
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
    
    public int next() throws GrinFormatterException {
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

  static Object Catalog(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub1 = input.sub();
    S(sub1, env);
    try {
      final StringCursor sub2 = sub1.sub();
      LoadDeclaration(sub2, env);
      try {
        while(true) {
          LoadDeclaration(sub2, env);
        }
      } catch(final GrinFormatterException e3) {
        //continue
      }
      sub2.publish(String.valueOf("\n"));
      sub2.commit();
    } catch(final GrinFormatterException e4) {
      //continue
    }
    try {
      final StringCursor sub5 = sub1.sub();
      AliasDeclaration(sub5, env);
      try {
        while(true) {
          AliasDeclaration(sub5, env);
        }
      } catch(final GrinFormatterException e6) {
        //continue
      }
      sub5.publish(String.valueOf("\n"));
      sub5.commit();
    } catch(final GrinFormatterException e7) {
      //continue
    }
    ScriptDeclaration(sub1, env);
    try {
      while(true) {
        ScriptDeclaration(sub1, env);
      }
    } catch(final GrinFormatterException e8) {
      //continue
    }
    EndOfFile(sub1, env);
    sub1.commit();
    return null;
  }

  static Object LoadDeclaration(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub9 = input.sub();
    sub9.accept("load");
    Sep(sub9, env);
    try {
      env[0] /*path*/= PathName(sub9, env);
    } catch(final GrinFormatterException e10) {
      sub9.abort(String.valueOf("load keyword requires a file path"), e10);
    }
    try {
      sub9.accept("as");
    } catch(final GrinFormatterException e11) {
      sub9.abort(String.valueOf("load keyword requires as keyword"), e11);
    }
    Sep(sub9, env);
    try {
      env[1] /*alias*/= Name(sub9, env);
    } catch(final GrinFormatterException e12) {
      sub9.abort(String.valueOf("load-as keywords require a name"), e12);
    }
    sub9.publish(String.valueOf("load ") + String.valueOf(sub9.isSet("path", env[0])) + String.valueOf(" as ") + String.valueOf(sub9.isSet("alias", env[1])) + String.valueOf("\n"));
    sub9.commit();
    return null;
  }

  static Object AliasDeclaration(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub13 = input.sub();
    sub13.accept("alias");
    Sep(sub13, env);
    try {
      env[2] /*name*/= QualifiedName(sub13, env);
    } catch(final GrinFormatterException e14) {
      sub13.abort(String.valueOf("alias keyword requires a qualified name"), e14);
    }
    try {
      sub13.accept("as");
    } catch(final GrinFormatterException e15) {
      sub13.abort(String.valueOf("alias keyword requires as keyword"), e15);
    }
    Sep(sub13, env);
    try {
      env[1] /*alias*/= Name(sub13, env);
    } catch(final GrinFormatterException e16) {
      sub13.abort(String.valueOf("alias-as keywords require a name"), e16);
    }
    sub13.publish(String.valueOf("alias ") + String.valueOf(sub13.isSet("name", env[2])) + String.valueOf(" as ") + String.valueOf(sub13.isSet("alias", env[1])) + String.valueOf("\n"));
    sub13.commit();
    return null;
  }

  static Object ScriptDeclaration(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub17 = input.sub();
    sub17.accept("<");
    try {
      env[2] /*name*/= Identifier(sub17, env);
    } catch(final GrinFormatterException e18) {
      sub17.abort(String.valueOf("script identifiers must be enclosed in angle brackets"), e18);
    }
    try {
      sub17.accept(">");
    } catch(final GrinFormatterException e19) {
      sub17.abort(String.valueOf("script identifier ") + String.valueOf(sub17.isSet("name", env[2])) + String.valueOf(" must end with an angle bracket"), e19);
    }
    S(sub17, env);
    try {
      sub17.accept("{");
    } catch(final GrinFormatterException e20) {
      sub17.abort(String.valueOf("script declaration ") + String.valueOf(sub17.isSet("name", env[2])) + String.valueOf(" must start with a brace"), e20);
    }
    S(sub17, env);
    sub17.publish(String.valueOf("<") + String.valueOf(sub17.isSet("name", env[2])) + String.valueOf("> {"));
    try {
      env[3] /*indent*/= new org.fuwjin.util.Indent();
    } catch(final Exception e21) {
      throw sub17.ex(e21);
    }
    try {
      final StringCursor sub22 = sub17.sub();
      sub22.publish(String.valueOf(sub22.isSet("indent", env[3])) + String.valueOf(Statement(sub22, env)));
      sub22.commit();
      try {
        while(true) {
          final StringCursor sub23 = sub17.sub();
          sub23.publish(String.valueOf(sub23.isSet("indent", env[3])) + String.valueOf(Statement(sub23, env)));
          sub23.commit();
        }
      } catch(final GrinFormatterException e24) {
        //continue
      }
    } catch(final GrinFormatterException e25) {
      //continue
    }
    try {
      final StringCursor sub26 = sub17.sub();
      sub26.accept("return");
      Sep(sub26, env);
      try {
        sub26.publish(String.valueOf(sub26.isSet("indent", env[3])) + String.valueOf("return ") + String.valueOf(Value(sub26, env)));
      } catch(final GrinFormatterException e27) {
        sub26.abort(String.valueOf("return keyword in script ") + String.valueOf(sub26.isSet("name", env[2])) + String.valueOf(" requires a value"), e27);
      }
      sub26.commit();
    } catch(final GrinFormatterException e28) {
      //continue
    }
    try {
      sub17.accept("}");
    } catch(final GrinFormatterException e29) {
      sub17.abort(String.valueOf("script declaration ") + String.valueOf(sub17.isSet("name", env[2])) + String.valueOf(" must end with a brace"), e29);
    }
    S(sub17, env);
    sub17.publish(String.valueOf("\n}\n"));
    sub17.commit();
    return null;
  }

  static Object Value(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub30 = input.sub();
    try {
      final StringBuilder builder31 = new StringBuilder();
      final StringCursor sub32 = sub30.subOutput(builder31);
      Script(sub32, env);
      sub32.commit();
      env[4] /*val*/= builder31.toString();
    } catch(final GrinFormatterException e33) {
      try {
        final StringBuilder builder34 = new StringBuilder();
        final StringCursor sub35 = sub30.subOutput(builder34);
        StaticLiteral(sub35, env);
        sub35.commit();
        env[4] /*val*/= builder34.toString();
      } catch(final GrinFormatterException e36) {
        try {
          final StringBuilder builder37 = new StringBuilder();
          final StringCursor sub38 = sub30.subOutput(builder37);
          DynamicLiteral(sub38, env);
          sub38.commit();
          env[4] /*val*/= builder37.toString();
        } catch(final GrinFormatterException e39) {
          try {
            final StringBuilder builder40 = new StringBuilder();
            final StringCursor sub41 = sub30.subOutput(builder40);
            AcceptStatement(sub41, env);
            sub41.commit();
            env[4] /*val*/= builder40.toString();
          } catch(final GrinFormatterException e42) {
            try {
              final StringBuilder builder43 = new StringBuilder();
              final StringCursor sub44 = sub30.subOutput(builder43);
              Invocation(sub44, env);
              sub44.commit();
              env[4] /*val*/= builder43.toString();
            } catch(final GrinFormatterException e45) {
              try {
                final StringBuilder builder46 = new StringBuilder();
                final StringCursor sub47 = sub30.subOutput(builder46);
                Number(sub47, env);
                sub47.commit();
                env[4] /*val*/= builder46.toString();
              } catch(final GrinFormatterException e48) {
                try {
                  final StringBuilder builder49 = new StringBuilder();
                  final StringCursor sub50 = sub30.subOutput(builder49);
                  Object(sub50, env);
                  sub50.commit();
                  env[4] /*val*/= builder49.toString();
                } catch(final GrinFormatterException e51) {
                  env[4] /*val*/= Name(sub30, env);
                }
              }
            }
          }
        }
      }
    }
    sub30.commit();
    return sub30.isSet("val", env[4]);
  }

  static Object Statement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub52 = input.sub();
    try {
      final StringBuilder builder53 = new StringBuilder();
      final StringCursor sub54 = sub52.subOutput(builder53);
      AssumeStatement(sub54, env);
      sub54.commit();
      env[4] /*val*/= builder53.toString();
    } catch(final GrinFormatterException e55) {
      try {
        final StringBuilder builder56 = new StringBuilder();
        final StringCursor sub57 = sub52.subOutput(builder56);
        EitherOrStatement(sub57, env);
        sub57.commit();
        env[4] /*val*/= builder56.toString();
      } catch(final GrinFormatterException e58) {
        try {
          final StringBuilder builder59 = new StringBuilder();
          final StringCursor sub60 = sub52.subOutput(builder59);
          CouldStatement(sub60, env);
          sub60.commit();
          env[4] /*val*/= builder59.toString();
        } catch(final GrinFormatterException e61) {
          try {
            final StringBuilder builder62 = new StringBuilder();
            final StringCursor sub63 = sub52.subOutput(builder62);
            RepeatStatement(sub63, env);
            sub63.commit();
            env[4] /*val*/= builder62.toString();
          } catch(final GrinFormatterException e64) {
            try {
              final StringBuilder builder65 = new StringBuilder();
              final StringCursor sub66 = sub52.subOutput(builder65);
              AcceptStatement(sub66, env);
              sub66.commit();
              env[4] /*val*/= builder65.toString();
            } catch(final GrinFormatterException e67) {
              try {
                final StringBuilder builder68 = new StringBuilder();
                final StringCursor sub69 = sub52.subOutput(builder68);
                PublishStatement(sub69, env);
                sub69.commit();
                env[4] /*val*/= builder68.toString();
              } catch(final GrinFormatterException e70) {
                try {
                  final StringBuilder builder71 = new StringBuilder();
                  final StringCursor sub72 = sub52.subOutput(builder71);
                  AbortStatement(sub72, env);
                  sub72.commit();
                  env[4] /*val*/= builder71.toString();
                } catch(final GrinFormatterException e73) {
                  try {
                    final StringBuilder builder74 = new StringBuilder();
                    final StringCursor sub75 = sub52.subOutput(builder74);
                    Script(sub75, env);
                    sub75.commit();
                    env[4] /*val*/= builder74.toString();
                  } catch(final GrinFormatterException e76) {
                    try {
                      final StringBuilder builder77 = new StringBuilder();
                      final StringCursor sub78 = sub52.subOutput(builder77);
                      Block(sub78, env);
                      sub78.commit();
                      env[4] /*val*/= builder77.toString();
                    } catch(final GrinFormatterException e79) {
                      try {
                        final StringBuilder builder80 = new StringBuilder();
                        final StringCursor sub81 = sub52.subOutput(builder80);
                        Assignment(sub81, env);
                        sub81.commit();
                        env[4] /*val*/= builder80.toString();
                      } catch(final GrinFormatterException e82) {
                        final StringBuilder builder83 = new StringBuilder();
                        final StringCursor sub84 = sub52.subOutput(builder83);
                        Invocation(sub84, env);
                        sub84.commit();
                        env[4] /*val*/= builder83.toString();
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
    sub52.commit();
    return sub52.isSet("val", env[4]);
  }

  static Object AssumeStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub85 = input.sub();
    sub85.accept("assume");
    Sep(sub85, env);
    sub85.publish(String.valueOf("assume "));
    try {
      final StringCursor sub86 = sub85.sub();
      sub86.accept("not");
      Sep(sub86, env);
      sub86.publish(String.valueOf("not "));
      sub86.commit();
    } catch(final GrinFormatterException e87) {
      //continue
    }
    try {
      sub85.publish(Value(sub85, env));
    } catch(final GrinFormatterException e88) {
      try {
        sub85.publish(Statement(sub85, env));
      } catch(final GrinFormatterException e89) {
        sub85.abort(String.valueOf("assume keyword requires value or in keyword"), e89);
      }
    }
    sub85.commit();
    return null;
  }

  static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub90 = input.sub();
    sub90.accept("either");
    Sep(sub90, env);
    sub90.publish("either ");
    env[5] /*prefix*/= StatementWithPrefix(sub90, env);
    try {
      final StringCursor sub91 = sub90.sub();
      sub91.accept("or");
      Sep(sub91, env);
      sub91.publish(String.valueOf(sub91.isSet("prefix", env[5])) + String.valueOf("or "));
      env[5] /*prefix*/= StatementWithPrefix(sub91, env);
      sub91.commit();
      try {
        while(true) {
          final StringCursor sub92 = sub90.sub();
          sub92.accept("or");
          Sep(sub92, env);
          sub92.publish(String.valueOf(sub92.isSet("prefix", env[5])) + String.valueOf("or "));
          env[5] /*prefix*/= StatementWithPrefix(sub92, env);
          sub92.commit();
        }
      } catch(final GrinFormatterException e93) {
        //continue
      }
    } catch(final GrinFormatterException e94) {
      sub90.abort(String.valueOf("either keyword requires at least one or keyword"), e94);
    }
    sub90.commit();
    return null;
  }

  static Object StatementWithPrefix(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub95 = input.sub();
    try {
      final StringCursor sub96 = sub95.sub();
      Block(sub96, env);
      env[5] /*prefix*/= " ";
      sub96.commit();
    } catch(final GrinFormatterException e97) {
      final StringCursor sub98 = sub95.sub();
      sub98.publish(Statement(sub98, env));
      env[5] /*prefix*/= sub98.isSet("indent", env[3]);
      sub98.commit();
    }
    sub95.commit();
    return sub95.isSet("prefix", env[5]);
  }

  static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub99 = input.sub();
    sub99.accept("could");
    Sep(sub99, env);
    try {
      sub99.publish(String.valueOf("could ") + String.valueOf(Statement(sub99, env)));
    } catch(final GrinFormatterException e100) {
      sub99.abort(String.valueOf("could keyword requires a statement"), e100);
    }
    sub99.commit();
    return null;
  }

  static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub101 = input.sub();
    sub101.accept("repeat");
    Sep(sub101, env);
    try {
      sub101.publish(String.valueOf("repeat ") + String.valueOf(Statement(sub101, env)));
    } catch(final GrinFormatterException e102) {
      sub101.abort(String.valueOf("repeat keyword requires a statement"), e102);
    }
    sub101.commit();
    return null;
  }

  static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub103 = input.sub();
    sub103.accept("accept");
    Sep(sub103, env);
    sub103.publish(String.valueOf("accept "));
    try {
      final StringCursor sub104 = sub103.sub();
      sub104.accept("not");
      Sep(sub104, env);
      sub104.publish(String.valueOf("not "));
      sub104.commit();
    } catch(final GrinFormatterException e105) {
      //continue
    }
    try {
      InFilter(sub103, env);
    } catch(final GrinFormatterException e106) {
      try {
        sub103.publish(Value(sub103, env));
      } catch(final GrinFormatterException e107) {
        sub103.abort(String.valueOf("accept keyword requires a value or in keyword"), e107);
      }
    }
    sub103.commit();
    return null;
  }

  static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub108 = input.sub();
    sub108.accept("publish");
    Sep(sub108, env);
    try {
      sub108.publish(String.valueOf("publish ") + String.valueOf(Value(sub108, env)));
    } catch(final GrinFormatterException e109) {
      sub108.abort(String.valueOf("publish keyword requires a value"), e109);
    }
    sub108.commit();
    return null;
  }

  static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub110 = input.sub();
    sub110.accept("abort");
    Sep(sub110, env);
    try {
      sub110.publish(String.valueOf("abort ") + String.valueOf(Value(sub110, env)));
    } catch(final GrinFormatterException e111) {
      sub110.abort(String.valueOf("abort keyword requires a value"), e111);
    }
    sub110.commit();
    return null;
  }

  static Object Block(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub112 = input.sub();
    sub112.accept("{");
    S(sub112, env);
    sub112.publish(String.valueOf("{"));
    try {
      ((org.fuwjin.util.Indent)sub112.isSet("indent", env[3])).increase();
    } catch(final Exception e113) {
      throw sub112.ex(e113);
    }
    try {
      final StringCursor sub114 = sub112.sub();
      sub114.publish(String.valueOf(sub114.isSet("indent", env[3])) + String.valueOf(Statement(sub114, env)));
      sub114.commit();
      try {
        while(true) {
          final StringCursor sub115 = sub112.sub();
          sub115.publish(String.valueOf(sub115.isSet("indent", env[3])) + String.valueOf(Statement(sub115, env)));
          sub115.commit();
        }
      } catch(final GrinFormatterException e116) {
        //continue
      }
    } catch(final GrinFormatterException e117) {
      //continue
    }
    try {
      sub112.accept("}");
    } catch(final GrinFormatterException e118) {
      sub112.abort(String.valueOf("block must end with a brace"), e118);
    }
    S(sub112, env);
    try {
      ((org.fuwjin.util.Indent)sub112.isSet("indent", env[3])).decrease();
    } catch(final Exception e119) {
      throw sub112.ex(e119);
    }
    sub112.publish(String.valueOf(sub112.isSet("indent", env[3])) + String.valueOf("}"));
    sub112.commit();
    return null;
  }

  static Object Assignment(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub120 = input.sub();
    env[2] /*name*/= Name(sub120, env);
    sub120.accept("=");
    S(sub120, env);
    try {
      sub120.publish(String.valueOf(sub120.isSet("name", env[2])) + String.valueOf(" = ") + String.valueOf(Value(sub120, env)));
    } catch(final GrinFormatterException e121) {
      sub120.abort(String.valueOf("assignment to ") + String.valueOf(sub120.isSet("name", env[2])) + String.valueOf(" requires a value"), e121);
    }
    sub120.commit();
    return null;
  }

  static Object Invocation(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub122 = input.sub();
    final StringCursor sub123 = sub122.sub();
    boolean b124 = true;
    try {
      if((Object)sub123.accept("return") == Boolean.FALSE) {
        b124 = false;
      }
    } catch(final GrinFormatterException e125) {
      b124 = false;
    }
    if(b124){
      throw sub122.ex("unexpected value");
    }
    env[2] /*name*/= QualifiedName(sub122, env);
    sub122.accept("(");
    S(sub122, env);
    sub122.publish(String.valueOf(sub122.isSet("name", env[2])) + String.valueOf("("));
    try {
      final StringCursor sub126 = sub122.sub();
      sub126.publish(Value(sub126, env));
      try {
        final StringCursor sub127 = sub126.sub();
        sub127.accept(",");
        S(sub127, env);
        try {
          sub127.publish(String.valueOf(", ") + String.valueOf(Value(sub127, env)));
        } catch(final GrinFormatterException e128) {
          sub127.abort(String.valueOf("invocation parameter for ") + String.valueOf(sub127.isSet("name", env[2])) + String.valueOf(" must be a value"), e128);
        }
        sub127.commit();
        try {
          while(true) {
            final StringCursor sub129 = sub126.sub();
            sub129.accept(",");
            S(sub129, env);
            try {
              sub129.publish(String.valueOf(", ") + String.valueOf(Value(sub129, env)));
            } catch(final GrinFormatterException e130) {
              sub129.abort(String.valueOf("invocation parameter for ") + String.valueOf(sub129.isSet("name", env[2])) + String.valueOf(" must be a value"), e130);
            }
            sub129.commit();
          }
        } catch(final GrinFormatterException e131) {
          //continue
        }
      } catch(final GrinFormatterException e132) {
        //continue
      }
      sub126.commit();
    } catch(final GrinFormatterException e133) {
      //continue
    }
    try {
      sub122.accept(")");
    } catch(final GrinFormatterException e134) {
      sub122.abort(String.valueOf("invocation must end with a parenthesis"), e134);
    }
    S(sub122, env);
    sub122.publish(String.valueOf(")"));
    sub122.commit();
    return null;
  }

  static Object Script(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub135 = input.sub();
    ScriptIdent(sub135, env);
    try {
      final StringCursor sub136 = sub135.sub();
      sub136.accept("<<");
      S(sub136, env);
      sub136.publish(String.valueOf(" << ") + String.valueOf(Value(sub136, env)));
      sub136.commit();
    } catch(final GrinFormatterException e137) {
      //continue
    }
    try {
      final StringCursor sub138 = sub135.sub();
      sub138.accept(">>");
      S(sub138, env);
      sub138.publish(" >> ");
      ScriptIdent(sub138, env);
      sub138.commit();
      try {
        while(true) {
          final StringCursor sub139 = sub135.sub();
          sub139.accept(">>");
          S(sub139, env);
          sub139.publish(" >> ");
          ScriptIdent(sub139, env);
          sub139.commit();
        }
      } catch(final GrinFormatterException e140) {
        //continue
      }
    } catch(final GrinFormatterException e141) {
      //continue
    }
    try {
      final StringCursor sub142 = sub135.sub();
      sub142.accept(">>");
      S(sub142, env);
      sub142.publish(String.valueOf(" >> ") + String.valueOf(Name(sub142, env)));
      sub142.commit();
    } catch(final GrinFormatterException e143) {
      //continue
    }
    sub135.commit();
    return null;
  }

  static Object ScriptIdent(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub144 = input.sub();
    sub144.accept("<");
    try {
      env[6] /*id*/= Identifier(sub144, env);
    } catch(final GrinFormatterException e145) {
      sub144.abort(String.valueOf("scripts must be an identifier enclosed in angle brackets"), e145);
    }
    try {
      final StringCursor sub146 = sub144.sub();
      sub146.accept(":");
      try {
        env[6] /*id*/= String.valueOf(sub146.isSet("id", env[6])) + String.valueOf(":") + String.valueOf(Identifier(sub146, env));
      } catch(final GrinFormatterException e147) {
        sub146.abort(String.valueOf("namespaced script ideitifiers must have a valid name"), e147);
      }
      sub146.commit();
    } catch(final GrinFormatterException e148) {
      //continue
    }
    try {
      sub144.accept(">");
    } catch(final GrinFormatterException e149) {
      sub144.abort(String.valueOf("script id ") + String.valueOf(sub144.isSet("id", env[6])) + String.valueOf(" must end with an angle bracket"), e149);
    }
    S(sub144, env);
    sub144.publish(String.valueOf("<") + String.valueOf(sub144.isSet("id", env[6])) + String.valueOf(">"));
    sub144.commit();
    return null;
  }

  static Object Object(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub150 = input.sub();
    sub150.accept("(");
    S(sub150, env);
    try {
      env[7] /*type*/= QualifiedName(sub150, env);
    } catch(final GrinFormatterException e151) {
      sub150.abort(String.valueOf("objects must lead with a type cast"), e151);
    }
    try {
      sub150.accept(")");
    } catch(final GrinFormatterException e152) {
      sub150.abort(String.valueOf("object type cast ") + String.valueOf(sub150.isSet("type", env[7])) + String.valueOf(" must end with a parenthesis"), e152);
    }
    S(sub150, env);
    try {
      sub150.accept("{");
    } catch(final GrinFormatterException e153) {
      sub150.abort(String.valueOf("object data must be enclosed in braces"), e153);
    }
    S(sub150, env);
    sub150.publish(String.valueOf("(") + String.valueOf(sub150.isSet("type", env[7])) + String.valueOf("){"));
    try {
      ((org.fuwjin.util.Indent)sub150.isSet("indent", env[3])).increase();
    } catch(final Exception e154) {
      throw sub150.ex(e154);
    }
    try {
      final StringCursor sub155 = sub150.sub();
      sub155.publish(String.valueOf(sub155.isSet("indent", env[3])) + String.valueOf(Field(sub155, env)));
      try {
        final StringCursor sub156 = sub155.sub();
        sub156.accept(",");
        S(sub156, env);
        try {
          sub156.publish(String.valueOf(",") + String.valueOf(sub156.isSet("indent", env[3])) + String.valueOf(Field(sub156, env)));
        } catch(final GrinFormatterException e157) {
          sub156.abort(String.valueOf("field mapping must follow a comma in a object data"), e157);
        }
        sub156.commit();
        try {
          while(true) {
            final StringCursor sub158 = sub155.sub();
            sub158.accept(",");
            S(sub158, env);
            try {
              sub158.publish(String.valueOf(",") + String.valueOf(sub158.isSet("indent", env[3])) + String.valueOf(Field(sub158, env)));
            } catch(final GrinFormatterException e159) {
              sub158.abort(String.valueOf("field mapping must follow a comma in a object data"), e159);
            }
            sub158.commit();
          }
        } catch(final GrinFormatterException e160) {
          //continue
        }
      } catch(final GrinFormatterException e161) {
        //continue
      }
      sub155.commit();
    } catch(final GrinFormatterException e162) {
      //continue
    }
    try {
      sub150.accept("}");
    } catch(final GrinFormatterException e163) {
      sub150.abort(String.valueOf("object data must end with a brace"), e163);
    }
    S(sub150, env);
    try {
      ((org.fuwjin.util.Indent)sub150.isSet("indent", env[3])).decrease();
    } catch(final Exception e164) {
      throw sub150.ex(e164);
    }
    sub150.publish(String.valueOf(sub150.isSet("indent", env[3])) + String.valueOf("}"));
    sub150.commit();
    return null;
  }

  static Object Field(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub165 = input.sub();
    env[2] /*name*/= Name(sub165, env);
    try {
      sub165.accept(":");
    } catch(final GrinFormatterException e166) {
      sub165.abort(String.valueOf("field mappings must be separated by a colon"), e166);
    }
    S(sub165, env);
    try {
      env[8] /*value*/= Value(sub165, env);
    } catch(final GrinFormatterException e167) {
      sub165.abort(String.valueOf("field ") + String.valueOf(sub165.isSet("name", env[2])) + String.valueOf(" must be mapped to a value"), e167);
    }
    sub165.commit();
    return String.valueOf(sub165.isSet("name", env[2])) + String.valueOf(": ") + String.valueOf(sub165.isSet("value", env[8]));
  }

  static Object InFilter(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub168 = input.sub();
    sub168.accept("in");
    Sep(sub168, env);
    sub168.publish(String.valueOf("in "));
    try {
      FilterRange(sub168, env);
    } catch(final GrinFormatterException e169) {
      sub168.abort(String.valueOf("in keyword requires at least one filter"), e169);
    }
    try {
      final StringCursor sub170 = sub168.sub();
      sub170.accept(",");
      S(sub170, env);
      sub170.publish(String.valueOf(", "));
      try {
        FilterRange(sub170, env);
      } catch(final GrinFormatterException e171) {
        sub170.abort(String.valueOf("in keyword requires a filter after a comma"), e171);
      }
      sub170.commit();
      try {
        while(true) {
          final StringCursor sub172 = sub168.sub();
          sub172.accept(",");
          S(sub172, env);
          sub172.publish(String.valueOf(", "));
          try {
            FilterRange(sub172, env);
          } catch(final GrinFormatterException e173) {
            sub172.abort(String.valueOf("in keyword requires a filter after a comma"), e173);
          }
          sub172.commit();
        }
      } catch(final GrinFormatterException e174) {
        //continue
      }
    } catch(final GrinFormatterException e175) {
      //continue
    }
    sub168.commit();
    return null;
  }

  static Object FilterRange(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub176 = input.sub();
    FilterChar(sub176, env);
    S(sub176, env);
    try {
      final StringCursor sub177 = sub176.sub();
      sub177.accept("-");
      S(sub177, env);
      sub177.publish("-");
      try {
        FilterChar(sub177, env);
      } catch(final GrinFormatterException e178) {
        sub177.abort(String.valueOf("range must have an end character"), e178);
      }
      S(sub177, env);
      sub177.commit();
    } catch(final GrinFormatterException e179) {
      //continue
    }
    sub176.commit();
    return null;
  }

  static Object FilterChar(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub180 = input.sub();
    try {
      Escape(sub180, env);
    } catch(final GrinFormatterException e181) {
      sub180.acceptNot("\\");
    }
    sub180.publish(sub180.match());
    sub180.commit();
    return null;
  }

  static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub182 = input.sub();
    sub182.accept("'");
    try {
      try {
        final StringCursor sub183 = sub182.sub();
        sub183.acceptNotIn("'\\","'\\");
        sub183.commit();
      } catch(final GrinFormatterException e184) {
        final StringCursor sub185 = sub182.sub();
        Escape(sub185, env);
        sub185.commit();
      }
      try {
        while(true) {
          try {
            final StringCursor sub186 = sub182.sub();
            sub186.acceptNotIn("'\\","'\\");
            sub186.commit();
          } catch(final GrinFormatterException e187) {
            final StringCursor sub188 = sub182.sub();
            Escape(sub188, env);
            sub188.commit();
          }
        }
      } catch(final GrinFormatterException e189) {
        //continue
      }
    } catch(final GrinFormatterException e190) {
      //continue
    }
    try {
      sub182.accept("'");
    } catch(final GrinFormatterException e191) {
      sub182.abort(String.valueOf("static literals must end with a quote"), e191);
    }
    sub182.publish(sub182.match());
    S(sub182, env);
    sub182.commit();
    return null;
  }

  static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub192 = input.sub();
    sub192.accept("\"");
    try {
      try {
        final StringCursor sub193 = sub192.sub();
        sub193.accept("'");
        Value(sub193, env);
        sub193.accept("'");
        sub193.commit();
      } catch(final GrinFormatterException e194) {
        try {
          final StringCursor sub195 = sub192.sub();
          Escape(sub195, env);
          sub195.commit();
        } catch(final GrinFormatterException e196) {
          final StringCursor sub197 = sub192.sub();
          sub197.acceptNotIn("\"\\","\"\\");
          sub197.commit();
        }
      }
      try {
        while(true) {
          try {
            final StringCursor sub198 = sub192.sub();
            sub198.accept("'");
            Value(sub198, env);
            sub198.accept("'");
            sub198.commit();
          } catch(final GrinFormatterException e199) {
            try {
              final StringCursor sub200 = sub192.sub();
              Escape(sub200, env);
              sub200.commit();
            } catch(final GrinFormatterException e201) {
              final StringCursor sub202 = sub192.sub();
              sub202.acceptNotIn("\"\\","\"\\");
              sub202.commit();
            }
          }
        }
      } catch(final GrinFormatterException e203) {
        //continue
      }
    } catch(final GrinFormatterException e204) {
      //continue
    }
    try {
      sub192.accept("\"");
    } catch(final GrinFormatterException e205) {
      sub192.abort(String.valueOf("dynamic literals must end with a double quote"), e205);
    }
    sub192.publish(sub192.match());
    S(sub192, env);
    sub192.commit();
    return null;
  }

  static Object Escape(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub206 = input.sub();
    sub206.accept("\\");
    try {
      final StringCursor sub207 = sub206.sub();
      sub207.accept("x");
      HexDigits(sub207, env);
      sub207.commit();
    } catch(final GrinFormatterException e208) {
      final StringCursor sub209 = sub206.sub();
      sub209.accept();
      sub209.commit();
    }
    sub206.commit();
    return sub206.match();
  }

  static Object HexDigits(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub210 = input.sub();
    HexDigit(sub210, env);
    HexDigit(sub210, env);
    HexDigit(sub210, env);
    HexDigit(sub210, env);
    sub210.commit();
    return null;
  }

  static Object HexDigit(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub211 = input.sub();
    sub211.acceptIn("0-9A-Fa-f","0123456789ABCDEFabcdef");
    sub211.commit();
    return null;
  }

  static Object Number(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub212 = input.sub();
    try {
      sub212.accept("-");
    } catch(final GrinFormatterException e213) {
      //continue
    }
    try {
      final StringCursor sub214 = sub212.sub();
      sub214.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub214.acceptIn("0-9","0123456789");
        }
      } catch(final GrinFormatterException e215) {
        //continue
      }
      try {
        final StringCursor sub216 = sub214.sub();
        sub216.accept(".");
        try {
          sub216.acceptIn("0-9","0123456789");
          try {
            while(true) {
              sub216.acceptIn("0-9","0123456789");
            }
          } catch(final GrinFormatterException e217) {
            //continue
          }
        } catch(final GrinFormatterException e218) {
          //continue
        }
        sub216.commit();
      } catch(final GrinFormatterException e219) {
        //continue
      }
      sub214.commit();
    } catch(final GrinFormatterException e220) {
      final StringCursor sub221 = sub212.sub();
      sub221.accept(".");
      sub221.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub221.acceptIn("0-9","0123456789");
        }
      } catch(final GrinFormatterException e222) {
        //continue
      }
      sub221.commit();
    }
    try {
      final StringCursor sub223 = sub212.sub();
      sub223.acceptIn("Ee","Ee");
      try {
        sub223.accept("-");
      } catch(final GrinFormatterException e224) {
        //continue
      }
      sub223.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub223.acceptIn("0-9","0123456789");
        }
      } catch(final GrinFormatterException e225) {
        //continue
      }
      sub223.commit();
    } catch(final GrinFormatterException e226) {
      //continue
    }
    sub212.publish(sub212.match());
    Sep(sub212, env);
    sub212.commit();
    return null;
  }

  static Object Path(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub227 = input.sub();
    final StringCursor sub228 = sub227.sub();
    try {
      sub228.accept("/");
    } catch(final GrinFormatterException e229) {
      //continue
    }
    QualifiedIdentifier(sub228, env);
    sub228.commit();
    try {
      while(true) {
        final StringCursor sub230 = sub227.sub();
        try {
          sub230.accept("/");
        } catch(final GrinFormatterException e231) {
          //continue
        }
        QualifiedIdentifier(sub230, env);
        sub230.commit();
      }
    } catch(final GrinFormatterException e232) {
      //continue
    }
    try {
      sub227.accept("/");
    } catch(final GrinFormatterException e233) {
      //continue
    }
    sub227.commit();
    return sub227.match();
  }

  static Object PathName(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub234 = input.sub();
    env[0] /*path*/= Path(sub234, env);
    S(sub234, env);
    sub234.commit();
    return sub234.isSet("path", env[0]);
  }

  static Object Name(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub235 = input.sub();
    env[6] /*id*/= Identifier(sub235, env);
    S(sub235, env);
    sub235.commit();
    return sub235.isSet("id", env[6]);
  }

  static Object QualifiedName(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub236 = input.sub();
    env[6] /*id*/= QualifiedIdentifier(sub236, env);
    S(sub236, env);
    sub236.commit();
    return sub236.isSet("id", env[6]);
  }

  static Object QualifiedIdentifier(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub237 = input.sub();
    AnnotatedIdentifier(sub237, env);
    try {
      final StringCursor sub238 = sub237.sub();
      sub238.accept(".");
      AnnotatedIdentifier(sub238, env);
      sub238.commit();
      try {
        while(true) {
          final StringCursor sub239 = sub237.sub();
          sub239.accept(".");
          AnnotatedIdentifier(sub239, env);
          sub239.commit();
        }
      } catch(final GrinFormatterException e240) {
        //continue
      }
    } catch(final GrinFormatterException e241) {
      //continue
    }
    sub237.commit();
    return sub237.match();
  }

  static Object AnnotatedIdentifier(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub242 = input.sub();
    Identifier(sub242, env);
    try {
      final StringCursor sub243 = sub242.sub();
      sub243.accept("[");
      try {
        Identifier(sub243, env);
      } catch(final GrinFormatterException e244) {
        //continue
      }
      sub243.accept("]");
      sub243.commit();
      try {
        while(true) {
          final StringCursor sub245 = sub242.sub();
          sub245.accept("[");
          try {
            Identifier(sub245, env);
          } catch(final GrinFormatterException e246) {
            //continue
          }
          sub245.accept("]");
          sub245.commit();
        }
      } catch(final GrinFormatterException e247) {
        //continue
      }
    } catch(final GrinFormatterException e248) {
      //continue
    }
    sub242.commit();
    return null;
  }

  static Object Identifier(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub249 = input.sub();
    IdentifierChar(sub249, env);
    try {
      while(true) {
        IdentifierChar(sub249, env);
      }
    } catch(final GrinFormatterException e250) {
      //continue
    }
    sub249.commit();
    return sub249.match();
  }

  static Object IdentifierChar(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub251 = input.sub();
    final StringCursor sub252 = sub251.sub();
    if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub252.next()) == Boolean.FALSE) {
      throw sub252.ex("check failed");
    }
    sub251.accept();
    sub251.commit();
    return null;
  }

  static Object Sep(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub253 = input.sub();
    final StringCursor sub254 = sub253.sub();
    boolean b255 = true;
    try {
      if((Object)IdentifierChar(sub254, env) == Boolean.FALSE) {
        b255 = false;
      }
    } catch(final GrinFormatterException e256) {
      b255 = false;
    }
    if(b255){
      throw sub253.ex("unexpected value");
    }
    S(sub253, env);
    sub253.commit();
    return null;
  }

  static Object S(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub257 = input.sub();
    try {
      Space(sub257, env);
    } catch(final GrinFormatterException e258) {
      //continue
    }
    sub257.commit();
    return null;
  }

  static Object Space(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub259 = input.sub();
    try {
      sub259.acceptIn("\t-\n\r ","\t\n\r ");
    } catch(final GrinFormatterException e260) {
      Comment(sub259, env);
    }
    try {
      while(true) {
        try {
          sub259.acceptIn("\t-\n\r ","\t\n\r ");
        } catch(final GrinFormatterException e261) {
          Comment(sub259, env);
        }
      }
    } catch(final GrinFormatterException e262) {
      //continue
    }
    sub259.commit();
    return null;
  }

  static Object Comment(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub263 = input.sub();
    sub263.accept("#");
    try {
      sub263.acceptNotIn("\n\r","\n\r");
      try {
        while(true) {
          sub263.acceptNotIn("\n\r","\n\r");
        }
      } catch(final GrinFormatterException e264) {
        //continue
      }
    } catch(final GrinFormatterException e265) {
      //continue
    }
    try {
      sub263.accept("\r");
    } catch(final GrinFormatterException e266) {
      //continue
    }
    try {
      sub263.accept("\n");
    } catch(final GrinFormatterException e267) {
      EndOfFile(sub263, env);
    }
    sub263.commit();
    return null;
  }

  static Object EndOfFile(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub268 = input.sub();
    final StringCursor sub269 = sub268.sub();
    boolean b270 = true;
    try {
      if((Object)sub269.next() == Boolean.FALSE) {
        b270 = false;
      }
    } catch(final GrinFormatterException e271) {
      b270 = false;
    }
    if(b270){
      throw sub268.ex("unexpected value");
    }
    sub268.commit();
    return null;
  }
  
  public static Object interpret(final CharSequence in, final Appendable out, final Map<String, ?> environment) throws GrinFormatterException {
    final StringCursor input = new StringCursor(in, out);
    final Object[] env = new Object[9];
    env[0] = environment.containsKey("path") ? environment.get("path") : UNSET;
    env[1] = environment.containsKey("alias") ? environment.get("alias") : UNSET;
    env[2] = environment.containsKey("name") ? environment.get("name") : UNSET;
    env[3] = environment.containsKey("indent") ? environment.get("indent") : UNSET;
    env[4] = environment.containsKey("val") ? environment.get("val") : UNSET;
    env[5] = environment.containsKey("prefix") ? environment.get("prefix") : UNSET;
    env[6] = environment.containsKey("id") ? environment.get("id") : UNSET;
    env[7] = environment.containsKey("type") ? environment.get("type") : UNSET;
    env[8] = environment.containsKey("value") ? environment.get("value") : UNSET;
    return Catalog(input, env);
  }
}
