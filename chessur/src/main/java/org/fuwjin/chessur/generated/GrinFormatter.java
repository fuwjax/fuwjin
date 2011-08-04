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
    
    public String nextStr() throws GrinFormatterException {
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
                  LogStatement(sub72, env);
                  sub72.commit();
                  env[4] /*val*/= builder71.toString();
                } catch(final GrinFormatterException e73) {
                  try {
                    final StringBuilder builder74 = new StringBuilder();
                    final StringCursor sub75 = sub52.subOutput(builder74);
                    AbortStatement(sub75, env);
                    sub75.commit();
                    env[4] /*val*/= builder74.toString();
                  } catch(final GrinFormatterException e76) {
                    try {
                      final StringBuilder builder77 = new StringBuilder();
                      final StringCursor sub78 = sub52.subOutput(builder77);
                      Script(sub78, env);
                      sub78.commit();
                      env[4] /*val*/= builder77.toString();
                    } catch(final GrinFormatterException e79) {
                      try {
                        final StringBuilder builder80 = new StringBuilder();
                        final StringCursor sub81 = sub52.subOutput(builder80);
                        Block(sub81, env);
                        sub81.commit();
                        env[4] /*val*/= builder80.toString();
                      } catch(final GrinFormatterException e82) {
                        try {
                          final StringBuilder builder83 = new StringBuilder();
                          final StringCursor sub84 = sub52.subOutput(builder83);
                          Assignment(sub84, env);
                          sub84.commit();
                          env[4] /*val*/= builder83.toString();
                        } catch(final GrinFormatterException e85) {
                          final StringBuilder builder86 = new StringBuilder();
                          final StringCursor sub87 = sub52.subOutput(builder86);
                          Invocation(sub87, env);
                          sub87.commit();
                          env[4] /*val*/= builder86.toString();
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
    sub52.commit();
    return sub52.isSet("val", env[4]);
  }

  static Object AssumeStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub88 = input.sub();
    sub88.accept("assume");
    Sep(sub88, env);
    sub88.publish(String.valueOf("assume "));
    try {
      final StringCursor sub89 = sub88.sub();
      sub89.accept("not");
      Sep(sub89, env);
      sub89.publish(String.valueOf("not "));
      sub89.commit();
    } catch(final GrinFormatterException e90) {
      //continue
    }
    try {
      sub88.publish(Value(sub88, env));
    } catch(final GrinFormatterException e91) {
      try {
        sub88.publish(Statement(sub88, env));
      } catch(final GrinFormatterException e92) {
        sub88.abort(String.valueOf("assume keyword requires value or in keyword"), e92);
      }
    }
    sub88.commit();
    return null;
  }

  static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub93 = input.sub();
    sub93.accept("either");
    Sep(sub93, env);
    sub93.publish("either ");
    env[5] /*prefix*/= StatementWithPrefix(sub93, env);
    try {
      final StringCursor sub94 = sub93.sub();
      sub94.accept("or");
      Sep(sub94, env);
      sub94.publish(String.valueOf(sub94.isSet("prefix", env[5])) + String.valueOf("or "));
      env[5] /*prefix*/= StatementWithPrefix(sub94, env);
      sub94.commit();
      try {
        while(true) {
          final StringCursor sub95 = sub93.sub();
          sub95.accept("or");
          Sep(sub95, env);
          sub95.publish(String.valueOf(sub95.isSet("prefix", env[5])) + String.valueOf("or "));
          env[5] /*prefix*/= StatementWithPrefix(sub95, env);
          sub95.commit();
        }
      } catch(final GrinFormatterException e96) {
        //continue
      }
    } catch(final GrinFormatterException e97) {
      sub93.abort(String.valueOf("either keyword requires at least one or keyword"), e97);
    }
    sub93.commit();
    return null;
  }

  static Object StatementWithPrefix(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub98 = input.sub();
    try {
      final StringCursor sub99 = sub98.sub();
      Block(sub99, env);
      env[5] /*prefix*/= " ";
      sub99.commit();
    } catch(final GrinFormatterException e100) {
      final StringCursor sub101 = sub98.sub();
      sub101.publish(Statement(sub101, env));
      env[5] /*prefix*/= sub101.isSet("indent", env[3]);
      sub101.commit();
    }
    sub98.commit();
    return sub98.isSet("prefix", env[5]);
  }

  static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub102 = input.sub();
    sub102.accept("could");
    Sep(sub102, env);
    try {
      sub102.publish(String.valueOf("could ") + String.valueOf(Statement(sub102, env)));
    } catch(final GrinFormatterException e103) {
      sub102.abort(String.valueOf("could keyword requires a statement"), e103);
    }
    sub102.commit();
    return null;
  }

  static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub104 = input.sub();
    sub104.accept("repeat");
    Sep(sub104, env);
    try {
      sub104.publish(String.valueOf("repeat ") + String.valueOf(Statement(sub104, env)));
    } catch(final GrinFormatterException e105) {
      sub104.abort(String.valueOf("repeat keyword requires a statement"), e105);
    }
    sub104.commit();
    return null;
  }

  static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub106 = input.sub();
    sub106.accept("accept");
    Sep(sub106, env);
    sub106.publish(String.valueOf("accept "));
    try {
      final StringCursor sub107 = sub106.sub();
      sub107.accept("not");
      Sep(sub107, env);
      sub107.publish(String.valueOf("not "));
      sub107.commit();
    } catch(final GrinFormatterException e108) {
      //continue
    }
    try {
      InFilter(sub106, env);
    } catch(final GrinFormatterException e109) {
      try {
        sub106.publish(Value(sub106, env));
      } catch(final GrinFormatterException e110) {
        sub106.abort(String.valueOf("accept keyword requires a value or in keyword"), e110);
      }
    }
    sub106.commit();
    return null;
  }

  static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub111 = input.sub();
    sub111.accept("publish");
    Sep(sub111, env);
    try {
      sub111.publish(String.valueOf("publish ") + String.valueOf(Value(sub111, env)));
    } catch(final GrinFormatterException e112) {
      sub111.abort(String.valueOf("publish keyword requires a value"), e112);
    }
    sub111.commit();
    return null;
  }

  static Object LogStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub113 = input.sub();
    sub113.accept("log");
    Sep(sub113, env);
    try {
      sub113.publish(String.valueOf("log ") + String.valueOf(Value(sub113, env)));
    } catch(final GrinFormatterException e114) {
      sub113.abort(String.valueOf("log keyword requires a value"), e114);
    }
    sub113.commit();
    return null;
  }

  static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub115 = input.sub();
    sub115.accept("abort");
    Sep(sub115, env);
    try {
      sub115.publish(String.valueOf("abort ") + String.valueOf(Value(sub115, env)));
    } catch(final GrinFormatterException e116) {
      sub115.abort(String.valueOf("abort keyword requires a value"), e116);
    }
    sub115.commit();
    return null;
  }

  static Object Block(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub117 = input.sub();
    sub117.accept("{");
    S(sub117, env);
    sub117.publish(String.valueOf("{"));
    try {
      ((org.fuwjin.util.Indent)sub117.isSet("indent", env[3])).increase();
    } catch(final Exception e118) {
      throw sub117.ex(e118);
    }
    try {
      final StringCursor sub119 = sub117.sub();
      sub119.publish(String.valueOf(sub119.isSet("indent", env[3])) + String.valueOf(Statement(sub119, env)));
      sub119.commit();
      try {
        while(true) {
          final StringCursor sub120 = sub117.sub();
          sub120.publish(String.valueOf(sub120.isSet("indent", env[3])) + String.valueOf(Statement(sub120, env)));
          sub120.commit();
        }
      } catch(final GrinFormatterException e121) {
        //continue
      }
    } catch(final GrinFormatterException e122) {
      //continue
    }
    try {
      sub117.accept("}");
    } catch(final GrinFormatterException e123) {
      sub117.abort(String.valueOf("block must end with a brace"), e123);
    }
    S(sub117, env);
    try {
      ((org.fuwjin.util.Indent)sub117.isSet("indent", env[3])).decrease();
    } catch(final Exception e124) {
      throw sub117.ex(e124);
    }
    sub117.publish(String.valueOf(sub117.isSet("indent", env[3])) + String.valueOf("}"));
    sub117.commit();
    return null;
  }

  static Object Assignment(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub125 = input.sub();
    env[2] /*name*/= Name(sub125, env);
    sub125.accept("=");
    S(sub125, env);
    try {
      sub125.publish(String.valueOf(sub125.isSet("name", env[2])) + String.valueOf(" = ") + String.valueOf(Value(sub125, env)));
    } catch(final GrinFormatterException e126) {
      sub125.abort(String.valueOf("assignment to ") + String.valueOf(sub125.isSet("name", env[2])) + String.valueOf(" requires a value"), e126);
    }
    sub125.commit();
    return null;
  }

  static Object Invocation(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub127 = input.sub();
    final StringCursor sub128 = sub127.sub();
    boolean b129 = true;
    try {
      if((Object)sub128.accept("return") == Boolean.FALSE) {
        b129 = false;
      }
    } catch(final GrinFormatterException e130) {
      b129 = false;
    }
    if(b129){
      throw sub127.ex("unexpected value");
    }
    env[2] /*name*/= QualifiedName(sub127, env);
    sub127.accept("(");
    S(sub127, env);
    sub127.publish(String.valueOf(sub127.isSet("name", env[2])) + String.valueOf("("));
    try {
      final StringCursor sub131 = sub127.sub();
      sub131.publish(Value(sub131, env));
      try {
        final StringCursor sub132 = sub131.sub();
        sub132.accept(",");
        S(sub132, env);
        try {
          sub132.publish(String.valueOf(", ") + String.valueOf(Value(sub132, env)));
        } catch(final GrinFormatterException e133) {
          sub132.abort(String.valueOf("invocation parameter for ") + String.valueOf(sub132.isSet("name", env[2])) + String.valueOf(" must be a value"), e133);
        }
        sub132.commit();
        try {
          while(true) {
            final StringCursor sub134 = sub131.sub();
            sub134.accept(",");
            S(sub134, env);
            try {
              sub134.publish(String.valueOf(", ") + String.valueOf(Value(sub134, env)));
            } catch(final GrinFormatterException e135) {
              sub134.abort(String.valueOf("invocation parameter for ") + String.valueOf(sub134.isSet("name", env[2])) + String.valueOf(" must be a value"), e135);
            }
            sub134.commit();
          }
        } catch(final GrinFormatterException e136) {
          //continue
        }
      } catch(final GrinFormatterException e137) {
        //continue
      }
      sub131.commit();
    } catch(final GrinFormatterException e138) {
      //continue
    }
    try {
      sub127.accept(")");
    } catch(final GrinFormatterException e139) {
      sub127.abort(String.valueOf("invocation must end with a parenthesis"), e139);
    }
    S(sub127, env);
    sub127.publish(String.valueOf(")"));
    sub127.commit();
    return null;
  }

  static Object Script(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub140 = input.sub();
    ScriptIdent(sub140, env);
    try {
      final StringCursor sub141 = sub140.sub();
      sub141.accept("<<");
      S(sub141, env);
      sub141.publish(String.valueOf(" << ") + String.valueOf(Value(sub141, env)));
      sub141.commit();
    } catch(final GrinFormatterException e142) {
      //continue
    }
    try {
      final StringCursor sub143 = sub140.sub();
      sub143.accept(">>");
      S(sub143, env);
      sub143.publish(" >> ");
      ScriptIdent(sub143, env);
      sub143.commit();
      try {
        while(true) {
          final StringCursor sub144 = sub140.sub();
          sub144.accept(">>");
          S(sub144, env);
          sub144.publish(" >> ");
          ScriptIdent(sub144, env);
          sub144.commit();
        }
      } catch(final GrinFormatterException e145) {
        //continue
      }
    } catch(final GrinFormatterException e146) {
      //continue
    }
    try {
      final StringCursor sub147 = sub140.sub();
      sub147.accept(">>");
      S(sub147, env);
      sub147.publish(String.valueOf(" >> ") + String.valueOf(Name(sub147, env)));
      sub147.commit();
    } catch(final GrinFormatterException e148) {
      //continue
    }
    sub140.commit();
    return null;
  }

  static Object ScriptIdent(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub149 = input.sub();
    sub149.accept("<");
    try {
      env[6] /*id*/= Identifier(sub149, env);
    } catch(final GrinFormatterException e150) {
      sub149.abort(String.valueOf("scripts must be an identifier enclosed in angle brackets"), e150);
    }
    try {
      final StringCursor sub151 = sub149.sub();
      sub151.accept(":");
      try {
        env[6] /*id*/= String.valueOf(sub151.isSet("id", env[6])) + String.valueOf(":") + String.valueOf(Identifier(sub151, env));
      } catch(final GrinFormatterException e152) {
        sub151.abort(String.valueOf("namespaced script ideitifiers must have a valid name"), e152);
      }
      sub151.commit();
    } catch(final GrinFormatterException e153) {
      //continue
    }
    try {
      sub149.accept(">");
    } catch(final GrinFormatterException e154) {
      sub149.abort(String.valueOf("script id ") + String.valueOf(sub149.isSet("id", env[6])) + String.valueOf(" must end with an angle bracket"), e154);
    }
    S(sub149, env);
    sub149.publish(String.valueOf("<") + String.valueOf(sub149.isSet("id", env[6])) + String.valueOf(">"));
    sub149.commit();
    return null;
  }

  static Object Object(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub155 = input.sub();
    sub155.accept("(");
    S(sub155, env);
    try {
      env[7] /*type*/= QualifiedName(sub155, env);
    } catch(final GrinFormatterException e156) {
      sub155.abort(String.valueOf("objects must lead with a type cast"), e156);
    }
    try {
      sub155.accept(")");
    } catch(final GrinFormatterException e157) {
      sub155.abort(String.valueOf("object type cast ") + String.valueOf(sub155.isSet("type", env[7])) + String.valueOf(" must end with a parenthesis"), e157);
    }
    S(sub155, env);
    try {
      sub155.accept("{");
    } catch(final GrinFormatterException e158) {
      sub155.abort(String.valueOf("object data must be enclosed in braces"), e158);
    }
    S(sub155, env);
    sub155.publish(String.valueOf("(") + String.valueOf(sub155.isSet("type", env[7])) + String.valueOf("){"));
    try {
      ((org.fuwjin.util.Indent)sub155.isSet("indent", env[3])).increase();
    } catch(final Exception e159) {
      throw sub155.ex(e159);
    }
    try {
      final StringCursor sub160 = sub155.sub();
      sub160.publish(String.valueOf(sub160.isSet("indent", env[3])) + String.valueOf(Field(sub160, env)));
      try {
        final StringCursor sub161 = sub160.sub();
        sub161.accept(",");
        S(sub161, env);
        try {
          sub161.publish(String.valueOf(",") + String.valueOf(sub161.isSet("indent", env[3])) + String.valueOf(Field(sub161, env)));
        } catch(final GrinFormatterException e162) {
          sub161.abort(String.valueOf("field mapping must follow a comma in a object data"), e162);
        }
        sub161.commit();
        try {
          while(true) {
            final StringCursor sub163 = sub160.sub();
            sub163.accept(",");
            S(sub163, env);
            try {
              sub163.publish(String.valueOf(",") + String.valueOf(sub163.isSet("indent", env[3])) + String.valueOf(Field(sub163, env)));
            } catch(final GrinFormatterException e164) {
              sub163.abort(String.valueOf("field mapping must follow a comma in a object data"), e164);
            }
            sub163.commit();
          }
        } catch(final GrinFormatterException e165) {
          //continue
        }
      } catch(final GrinFormatterException e166) {
        //continue
      }
      sub160.commit();
    } catch(final GrinFormatterException e167) {
      //continue
    }
    try {
      sub155.accept("}");
    } catch(final GrinFormatterException e168) {
      sub155.abort(String.valueOf("object data must end with a brace"), e168);
    }
    S(sub155, env);
    try {
      ((org.fuwjin.util.Indent)sub155.isSet("indent", env[3])).decrease();
    } catch(final Exception e169) {
      throw sub155.ex(e169);
    }
    sub155.publish(String.valueOf(sub155.isSet("indent", env[3])) + String.valueOf("}"));
    sub155.commit();
    return null;
  }

  static Object Field(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub170 = input.sub();
    env[2] /*name*/= Name(sub170, env);
    try {
      sub170.accept(":");
    } catch(final GrinFormatterException e171) {
      sub170.abort(String.valueOf("field mappings must be separated by a colon"), e171);
    }
    S(sub170, env);
    try {
      env[8] /*value*/= Value(sub170, env);
    } catch(final GrinFormatterException e172) {
      sub170.abort(String.valueOf("field ") + String.valueOf(sub170.isSet("name", env[2])) + String.valueOf(" must be mapped to a value"), e172);
    }
    sub170.commit();
    return String.valueOf(sub170.isSet("name", env[2])) + String.valueOf(": ") + String.valueOf(sub170.isSet("value", env[8]));
  }

  static Object InFilter(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub173 = input.sub();
    sub173.accept("in");
    Sep(sub173, env);
    sub173.publish(String.valueOf("in "));
    try {
      FilterRange(sub173, env);
    } catch(final GrinFormatterException e174) {
      sub173.abort(String.valueOf("in keyword requires at least one filter"), e174);
    }
    try {
      final StringCursor sub175 = sub173.sub();
      sub175.accept(",");
      S(sub175, env);
      sub175.publish(String.valueOf(", "));
      try {
        FilterRange(sub175, env);
      } catch(final GrinFormatterException e176) {
        sub175.abort(String.valueOf("in keyword requires a filter after a comma"), e176);
      }
      sub175.commit();
      try {
        while(true) {
          final StringCursor sub177 = sub173.sub();
          sub177.accept(",");
          S(sub177, env);
          sub177.publish(String.valueOf(", "));
          try {
            FilterRange(sub177, env);
          } catch(final GrinFormatterException e178) {
            sub177.abort(String.valueOf("in keyword requires a filter after a comma"), e178);
          }
          sub177.commit();
        }
      } catch(final GrinFormatterException e179) {
        //continue
      }
    } catch(final GrinFormatterException e180) {
      //continue
    }
    sub173.commit();
    return null;
  }

  static Object FilterRange(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub181 = input.sub();
    FilterChar(sub181, env);
    S(sub181, env);
    try {
      final StringCursor sub182 = sub181.sub();
      sub182.accept("-");
      S(sub182, env);
      sub182.publish("-");
      try {
        FilterChar(sub182, env);
      } catch(final GrinFormatterException e183) {
        sub182.abort(String.valueOf("range must have an end character"), e183);
      }
      S(sub182, env);
      sub182.commit();
    } catch(final GrinFormatterException e184) {
      //continue
    }
    sub181.commit();
    return null;
  }

  static Object FilterChar(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub185 = input.sub();
    try {
      Escape(sub185, env);
    } catch(final GrinFormatterException e186) {
      sub185.acceptNot("\\");
    }
    sub185.publish(sub185.match());
    sub185.commit();
    return null;
  }

  static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub187 = input.sub();
    sub187.accept("'");
    try {
      try {
        final StringCursor sub188 = sub187.sub();
        sub188.acceptNotIn("'\\","'\\");
        sub188.commit();
      } catch(final GrinFormatterException e189) {
        final StringCursor sub190 = sub187.sub();
        Escape(sub190, env);
        sub190.commit();
      }
      try {
        while(true) {
          try {
            final StringCursor sub191 = sub187.sub();
            sub191.acceptNotIn("'\\","'\\");
            sub191.commit();
          } catch(final GrinFormatterException e192) {
            final StringCursor sub193 = sub187.sub();
            Escape(sub193, env);
            sub193.commit();
          }
        }
      } catch(final GrinFormatterException e194) {
        //continue
      }
    } catch(final GrinFormatterException e195) {
      //continue
    }
    try {
      sub187.accept("'");
    } catch(final GrinFormatterException e196) {
      sub187.abort(String.valueOf("static literals must end with a quote"), e196);
    }
    sub187.publish(sub187.match());
    S(sub187, env);
    sub187.commit();
    return null;
  }

  static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub197 = input.sub();
    sub197.accept("\"");
    try {
      try {
        final StringCursor sub198 = sub197.sub();
        sub198.accept("'");
        Value(sub198, env);
        sub198.accept("'");
        sub198.commit();
      } catch(final GrinFormatterException e199) {
        try {
          final StringCursor sub200 = sub197.sub();
          Escape(sub200, env);
          sub200.commit();
        } catch(final GrinFormatterException e201) {
          final StringCursor sub202 = sub197.sub();
          sub202.acceptNotIn("\"\\","\"\\");
          sub202.commit();
        }
      }
      try {
        while(true) {
          try {
            final StringCursor sub203 = sub197.sub();
            sub203.accept("'");
            Value(sub203, env);
            sub203.accept("'");
            sub203.commit();
          } catch(final GrinFormatterException e204) {
            try {
              final StringCursor sub205 = sub197.sub();
              Escape(sub205, env);
              sub205.commit();
            } catch(final GrinFormatterException e206) {
              final StringCursor sub207 = sub197.sub();
              sub207.acceptNotIn("\"\\","\"\\");
              sub207.commit();
            }
          }
        }
      } catch(final GrinFormatterException e208) {
        //continue
      }
    } catch(final GrinFormatterException e209) {
      //continue
    }
    try {
      sub197.accept("\"");
    } catch(final GrinFormatterException e210) {
      sub197.abort(String.valueOf("dynamic literals must end with a double quote"), e210);
    }
    sub197.publish(sub197.match());
    S(sub197, env);
    sub197.commit();
    return null;
  }

  static Object Escape(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub211 = input.sub();
    sub211.accept("\\");
    try {
      final StringCursor sub212 = sub211.sub();
      sub212.accept("x");
      HexDigits(sub212, env);
      sub212.commit();
    } catch(final GrinFormatterException e213) {
      final StringCursor sub214 = sub211.sub();
      sub214.accept();
      sub214.commit();
    }
    sub211.commit();
    return sub211.match();
  }

  static Object HexDigits(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub215 = input.sub();
    HexDigit(sub215, env);
    HexDigit(sub215, env);
    HexDigit(sub215, env);
    HexDigit(sub215, env);
    sub215.commit();
    return null;
  }

  static Object HexDigit(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub216 = input.sub();
    sub216.acceptIn("0-9A-Fa-f","0123456789ABCDEFabcdef");
    sub216.commit();
    return null;
  }

  static Object Number(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub217 = input.sub();
    try {
      sub217.accept("-");
    } catch(final GrinFormatterException e218) {
      //continue
    }
    try {
      final StringCursor sub219 = sub217.sub();
      sub219.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub219.acceptIn("0-9","0123456789");
        }
      } catch(final GrinFormatterException e220) {
        //continue
      }
      try {
        final StringCursor sub221 = sub219.sub();
        sub221.accept(".");
        try {
          sub221.acceptIn("0-9","0123456789");
          try {
            while(true) {
              sub221.acceptIn("0-9","0123456789");
            }
          } catch(final GrinFormatterException e222) {
            //continue
          }
        } catch(final GrinFormatterException e223) {
          //continue
        }
        sub221.commit();
      } catch(final GrinFormatterException e224) {
        //continue
      }
      sub219.commit();
    } catch(final GrinFormatterException e225) {
      final StringCursor sub226 = sub217.sub();
      sub226.accept(".");
      sub226.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub226.acceptIn("0-9","0123456789");
        }
      } catch(final GrinFormatterException e227) {
        //continue
      }
      sub226.commit();
    }
    try {
      final StringCursor sub228 = sub217.sub();
      sub228.acceptIn("Ee","Ee");
      try {
        sub228.accept("-");
      } catch(final GrinFormatterException e229) {
        //continue
      }
      sub228.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub228.acceptIn("0-9","0123456789");
        }
      } catch(final GrinFormatterException e230) {
        //continue
      }
      sub228.commit();
    } catch(final GrinFormatterException e231) {
      //continue
    }
    sub217.publish(sub217.match());
    Sep(sub217, env);
    sub217.commit();
    return null;
  }

  static Object Path(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub232 = input.sub();
    final StringCursor sub233 = sub232.sub();
    try {
      sub233.accept("/");
    } catch(final GrinFormatterException e234) {
      //continue
    }
    QualifiedIdentifier(sub233, env);
    sub233.commit();
    try {
      while(true) {
        final StringCursor sub235 = sub232.sub();
        try {
          sub235.accept("/");
        } catch(final GrinFormatterException e236) {
          //continue
        }
        QualifiedIdentifier(sub235, env);
        sub235.commit();
      }
    } catch(final GrinFormatterException e237) {
      //continue
    }
    try {
      sub232.accept("/");
    } catch(final GrinFormatterException e238) {
      //continue
    }
    sub232.commit();
    return sub232.match();
  }

  static Object PathName(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub239 = input.sub();
    env[0] /*path*/= Path(sub239, env);
    S(sub239, env);
    sub239.commit();
    return sub239.isSet("path", env[0]);
  }

  static Object Name(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub240 = input.sub();
    env[6] /*id*/= Identifier(sub240, env);
    S(sub240, env);
    sub240.commit();
    return sub240.isSet("id", env[6]);
  }

  static Object QualifiedName(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub241 = input.sub();
    env[6] /*id*/= QualifiedIdentifier(sub241, env);
    S(sub241, env);
    sub241.commit();
    return sub241.isSet("id", env[6]);
  }

  static Object QualifiedIdentifier(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub242 = input.sub();
    AnnotatedIdentifier(sub242, env);
    try {
      final StringCursor sub243 = sub242.sub();
      sub243.accept(".");
      AnnotatedIdentifier(sub243, env);
      sub243.commit();
      try {
        while(true) {
          final StringCursor sub244 = sub242.sub();
          sub244.accept(".");
          AnnotatedIdentifier(sub244, env);
          sub244.commit();
        }
      } catch(final GrinFormatterException e245) {
        //continue
      }
    } catch(final GrinFormatterException e246) {
      //continue
    }
    sub242.commit();
    return sub242.match();
  }

  static Object AnnotatedIdentifier(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub247 = input.sub();
    Identifier(sub247, env);
    try {
      final StringCursor sub248 = sub247.sub();
      sub248.accept("[");
      try {
        Identifier(sub248, env);
      } catch(final GrinFormatterException e249) {
        //continue
      }
      sub248.accept("]");
      sub248.commit();
      try {
        while(true) {
          final StringCursor sub250 = sub247.sub();
          sub250.accept("[");
          try {
            Identifier(sub250, env);
          } catch(final GrinFormatterException e251) {
            //continue
          }
          sub250.accept("]");
          sub250.commit();
        }
      } catch(final GrinFormatterException e252) {
        //continue
      }
    } catch(final GrinFormatterException e253) {
      //continue
    }
    sub247.commit();
    return null;
  }

  static Object Identifier(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub254 = input.sub();
    IdentifierChar(sub254, env);
    try {
      while(true) {
        IdentifierChar(sub254, env);
      }
    } catch(final GrinFormatterException e255) {
      //continue
    }
    sub254.commit();
    return sub254.match();
  }

  static Object IdentifierChar(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub256 = input.sub();
    final StringCursor sub257 = sub256.sub();
    if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub257.next()) == Boolean.FALSE) {
      throw sub257.ex("check failed");
    }
    sub256.accept();
    sub256.commit();
    return null;
  }

  static Object Sep(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub258 = input.sub();
    final StringCursor sub259 = sub258.sub();
    boolean b260 = true;
    try {
      if((Object)IdentifierChar(sub259, env) == Boolean.FALSE) {
        b260 = false;
      }
    } catch(final GrinFormatterException e261) {
      b260 = false;
    }
    if(b260){
      throw sub258.ex("unexpected value");
    }
    S(sub258, env);
    sub258.commit();
    return null;
  }

  static Object S(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub262 = input.sub();
    try {
      Space(sub262, env);
    } catch(final GrinFormatterException e263) {
      //continue
    }
    sub262.commit();
    return null;
  }

  static Object Space(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub264 = input.sub();
    try {
      sub264.acceptIn("\t-\n\r ","\t\n\r ");
    } catch(final GrinFormatterException e265) {
      Comment(sub264, env);
    }
    try {
      while(true) {
        try {
          sub264.acceptIn("\t-\n\r ","\t\n\r ");
        } catch(final GrinFormatterException e266) {
          Comment(sub264, env);
        }
      }
    } catch(final GrinFormatterException e267) {
      //continue
    }
    sub264.commit();
    return null;
  }

  static Object Comment(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub268 = input.sub();
    sub268.accept("#");
    try {
      sub268.acceptNotIn("\n\r","\n\r");
      try {
        while(true) {
          sub268.acceptNotIn("\n\r","\n\r");
        }
      } catch(final GrinFormatterException e269) {
        //continue
      }
    } catch(final GrinFormatterException e270) {
      //continue
    }
    try {
      sub268.accept("\r");
    } catch(final GrinFormatterException e271) {
      //continue
    }
    try {
      sub268.accept("\n");
    } catch(final GrinFormatterException e272) {
      EndOfFile(sub268, env);
    }
    sub268.commit();
    return null;
  }

  static Object EndOfFile(final StringCursor input, final Object... parentEnv) throws GrinFormatterException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub273 = input.sub();
    final StringCursor sub274 = sub273.sub();
    boolean b275 = true;
    try {
      if((Object)sub274.next() == Boolean.FALSE) {
        b275 = false;
      }
    } catch(final GrinFormatterException e276) {
      b275 = false;
    }
    if(b275){
      throw sub273.ex("unexpected value");
    }
    sub273.commit();
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
