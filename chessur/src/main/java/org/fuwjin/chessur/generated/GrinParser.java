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

public class GrinParser {
  static final Object UNSET = new Object() {
    public String toString() {
      return "UNSET";
    }
  };

  public static class GrinParserException extends Exception {
    private static final long serialVersionUID = 1; 
    GrinParserException(final String message) {
      super(message);
    }
    
    GrinParserException(final String message, final Throwable cause) {
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
    
    public int accept() throws GrinParserException {
      checkBounds(pos);
      return advance();
    }
    
    public int accept(final String expected) throws GrinParserException {
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
    
    public int acceptIn(final String name, final String set) throws GrinParserException {
      checkBounds(pos);
      if(set.indexOf(seq.charAt(pos)) < 0) {
        throw ex("Did not match filter: "+name);
      }
      return advance();
    }
    
    public int acceptNot(final String expected) throws GrinParserException {
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
    
    public int acceptNotIn(final String name, final String set) throws GrinParserException {
      checkBounds(pos);
      if(set.indexOf(seq.charAt(pos)) >= 0) {
        throw ex("Unexpected match: "+name);
      }
      return advance();
    }
    
    public void publish(final Object value) throws GrinParserException {
      try {
        appender.append(value.toString());
      } catch(IOException e) {
        throw ex(e);
      }
    }
    
    public Object isSet(final String name, final Object value) throws GrinParserException {
      if(UNSET.equals(value)) {
        throw ex("variable "+name+" is unset");
      }
      return value;
    }
    
    protected void checkBounds(final int p) throws GrinParserException {
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
    
    public GrinParserException ex(final String message) {
      return new GrinParserException(message + context());
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
    
    public GrinParserException ex(final Throwable cause) {
      return new GrinParserException(context(), cause);
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
    
    public int next() throws GrinParserException {
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

  static Object Catalog(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub1 = input.sub();
    try {
      env[0] /*cat*/= new org.fuwjin.chessur.expression.CatalogImpl((java.lang.String)sub1.isSet("name", env[1]), (org.fuwjin.chessur.CatalogManager)sub1.isSet("manager", env[2]));
    } catch(final Exception e2) {
      throw sub1.ex(e2);
    }
    S(sub1, env);
    try {
      try {
        LoadDeclaration(sub1, env);
      } catch(final GrinParserException e3) {
        try {
          AliasDeclaration(sub1, env);
        } catch(final GrinParserException e4) {
          try {
            ((org.fuwjin.chessur.expression.CatalogImpl)sub1.isSet("cat", env[0])).add((org.fuwjin.chessur.expression.Declaration)ScriptDeclaration(sub1, env));
          } catch(final Exception e5) {
            throw sub1.ex(e5);
          }
        }
      }
      try {
        while(true) {
          try {
            LoadDeclaration(sub1, env);
          } catch(final GrinParserException e6) {
            try {
              AliasDeclaration(sub1, env);
            } catch(final GrinParserException e7) {
              try {
                ((org.fuwjin.chessur.expression.CatalogImpl)sub1.isSet("cat", env[0])).add((org.fuwjin.chessur.expression.Declaration)ScriptDeclaration(sub1, env));
              } catch(final Exception e8) {
                throw sub1.ex(e8);
              }
            }
          }
        }
      } catch(final GrinParserException e9) {
        //continue
      }
    } catch(final GrinParserException e10) {
      //continue
    }
    EndOfFile(sub1, env);
    sub1.commit();
    return sub1.isSet("cat", env[0]);
  }

  static Object LoadDeclaration(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub11 = input.sub();
    sub11.accept("load");
    Sep(sub11, env);
    try {
      env[3] /*path*/= PathName(sub11, env);
    } catch(final GrinParserException e12) {
      sub11.abort(String.valueOf("load keyword requires a file path"), e12);
    }
    try {
      sub11.accept("as");
    } catch(final GrinParserException e13) {
      sub11.abort(String.valueOf("load keyword requires as keyword"), e13);
    }
    Sep(sub11, env);
    try {
      try {
        ((org.fuwjin.chessur.expression.CatalogImpl)sub11.isSet("cat", env[0])).load((java.lang.String)sub11.isSet("path", env[3]), (java.lang.String)Name(sub11, env));
      } catch(final Exception e14) {
        throw sub11.ex(e14);
      }
    } catch(final GrinParserException e15) {
      sub11.abort(String.valueOf("load-as keywords require a name"), e15);
    }
    sub11.commit();
    return null;
  }

  static Object AliasDeclaration(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub16 = input.sub();
    sub16.accept("alias");
    Sep(sub16, env);
    try {
      env[4] /*qname*/= QualifiedName(sub16, env);
    } catch(final GrinParserException e17) {
      sub16.abort(String.valueOf("alias keyword requires a qualified name"), e17);
    }
    try {
      final StringCursor sub18 = sub16.sub();
      sub18.accept("as");
      Sep(sub18, env);
      try {
        try {
          ((org.fuwjin.chessur.expression.CatalogImpl)sub18.isSet("cat", env[0])).alias((java.lang.String)sub18.isSet("qname", env[4]), (java.lang.String)Name(sub18, env));
        } catch(final Exception e19) {
          throw sub18.ex(e19);
        }
      } catch(final GrinParserException e20) {
        sub18.abort(String.valueOf("alias-as keywords require a name"), e20);
      }
      sub18.commit();
    } catch(final GrinParserException e21) {
      try {
        final StringCursor sub22 = sub16.sub();
        sub22.accept("(");
        S(sub22, env);
        try {
          env[5] /*signature*/= new org.fuwjin.dinah.signature.TypedArgsSignature((java.lang.String)sub22.isSet("qname", env[4]));
        } catch(final Exception e23) {
          throw sub22.ex(e23);
        }
        try {
          final StringCursor sub24 = sub22.sub();
          try {
            ((org.fuwjin.dinah.signature.TypedArgsSignature)sub24.isSet("signature", env[5])).addArg((java.lang.String)QualifiedName(sub24, env));
          } catch(final Exception e25) {
            throw sub24.ex(e25);
          }
          try {
            final StringCursor sub26 = sub24.sub();
            sub26.accept(",");
            S(sub26, env);
            try {
              ((org.fuwjin.dinah.signature.TypedArgsSignature)sub26.isSet("signature", env[5])).addArg((java.lang.String)QualifiedName(sub26, env));
            } catch(final Exception e27) {
              throw sub26.ex(e27);
            }
            sub26.commit();
            try {
              while(true) {
                final StringCursor sub28 = sub24.sub();
                sub28.accept(",");
                S(sub28, env);
                try {
                  ((org.fuwjin.dinah.signature.TypedArgsSignature)sub28.isSet("signature", env[5])).addArg((java.lang.String)QualifiedName(sub28, env));
                } catch(final Exception e29) {
                  throw sub28.ex(e29);
                }
                sub28.commit();
              }
            } catch(final GrinParserException e30) {
              //continue
            }
          } catch(final GrinParserException e31) {
            //continue
          }
          sub24.commit();
        } catch(final GrinParserException e32) {
          //continue
        }
        sub22.accept(")");
        S(sub22, env);
        sub22.accept("as");
        Sep(sub22, env);
        try {
          try {
            ((org.fuwjin.chessur.expression.CatalogImpl)sub22.isSet("cat", env[0])).aliasSignature((org.fuwjin.dinah.FunctionSignature)sub22.isSet("signature", env[5]), (java.lang.String)Name(sub22, env));
          } catch(final Exception e33) {
            throw sub22.ex(e33);
          }
        } catch(final GrinParserException e34) {
          sub22.abort(String.valueOf("alias-as keywords require a name"), e34);
        }
        sub22.commit();
      } catch(final GrinParserException e35) {
        sub16.abort(String.valueOf("alias keyword requires as keyword"), e35);
      }
    }
    sub16.commit();
    return null;
  }

  static Object ScriptDeclaration(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub36 = input.sub();
    sub36.accept("<");
    try {
      env[1] /*name*/= Identifier(sub36, env);
    } catch(final GrinParserException e37) {
      sub36.abort(String.valueOf("script identifiers must be enclosed in angle brackets"), e37);
    }
    try {
      sub36.accept(">");
    } catch(final GrinParserException e38) {
      sub36.abort(String.valueOf("script identifiers must end with an angle bracket"), e38);
    }
    S(sub36, env);
    try {
      sub36.accept("{");
    } catch(final GrinParserException e39) {
      sub36.abort(String.valueOf("script declarations must start with a brace"), e39);
    }
    S(sub36, env);
    try {
      env[6] /*script*/= new org.fuwjin.chessur.expression.Declaration((java.lang.String)sub36.isSet("name", env[1]));
    } catch(final Exception e40) {
      throw sub36.ex(e40);
    }
    try {
      try {
        ((org.fuwjin.chessur.expression.Declaration)sub36.isSet("script", env[6])).add((org.fuwjin.chessur.expression.Expression)Statement(sub36, env));
      } catch(final Exception e41) {
        throw sub36.ex(e41);
      }
      try {
        while(true) {
          try {
            ((org.fuwjin.chessur.expression.Declaration)sub36.isSet("script", env[6])).add((org.fuwjin.chessur.expression.Expression)Statement(sub36, env));
          } catch(final Exception e42) {
            throw sub36.ex(e42);
          }
        }
      } catch(final GrinParserException e43) {
        //continue
      }
    } catch(final GrinParserException e44) {
      //continue
    }
    try {
      final StringCursor sub45 = sub36.sub();
      sub45.accept("return");
      Sep(sub45, env);
      try {
        try {
          ((org.fuwjin.chessur.expression.Declaration)sub45.isSet("script", env[6])).returns((org.fuwjin.chessur.expression.Expression)Value(sub45, env));
        } catch(final Exception e46) {
          throw sub45.ex(e46);
        }
      } catch(final GrinParserException e47) {
        sub45.abort(String.valueOf("return keyword requires a value"), e47);
      }
      sub45.commit();
    } catch(final GrinParserException e48) {
      //continue
    }
    try {
      sub36.accept("}");
    } catch(final GrinParserException e49) {
      sub36.abort(String.valueOf("script declaration for ") + String.valueOf(sub36.isSet("name", env[1])) + String.valueOf(" must end with a brace"), e49);
    }
    S(sub36, env);
    sub36.commit();
    return sub36.isSet("script", env[6]);
  }

  static Object Value(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub50 = input.sub();
    try {
      env[7] /*val*/= StaticLiteral(sub50, env);
    } catch(final GrinParserException e51) {
      try {
        env[7] /*val*/= DynamicLiteral(sub50, env);
      } catch(final GrinParserException e52) {
        try {
          env[7] /*val*/= Script(sub50, env);
        } catch(final GrinParserException e53) {
          try {
            env[7] /*val*/= AcceptStatement(sub50, env);
          } catch(final GrinParserException e54) {
            try {
              env[7] /*val*/= Invocation(sub50, env);
            } catch(final GrinParserException e55) {
              try {
                env[7] /*val*/= Number(sub50, env);
              } catch(final GrinParserException e56) {
                try {
                  env[7] /*val*/= Object(sub50, env);
                } catch(final GrinParserException e57) {
                  try {
                    env[7] /*val*/= MatchValue(sub50, env);
                  } catch(final GrinParserException e58) {
                    try {
                      env[7] /*val*/= NextValue(sub50, env);
                    } catch(final GrinParserException e59) {
                      try {
                        env[7] /*val*/= new org.fuwjin.chessur.expression.Variable((java.lang.String)Name(sub50, env));
                      } catch(final Exception e60) {
                        throw sub50.ex(e60);
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
    sub50.commit();
    return sub50.isSet("val", env[7]);
  }

  static Object Statement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub61 = input.sub();
    try {
      env[8] /*stmt*/= AssumeStatement(sub61, env);
    } catch(final GrinParserException e62) {
      try {
        env[8] /*stmt*/= EitherOrStatement(sub61, env);
      } catch(final GrinParserException e63) {
        try {
          env[8] /*stmt*/= CouldStatement(sub61, env);
        } catch(final GrinParserException e64) {
          try {
            env[8] /*stmt*/= RepeatStatement(sub61, env);
          } catch(final GrinParserException e65) {
            try {
              env[8] /*stmt*/= AcceptStatement(sub61, env);
            } catch(final GrinParserException e66) {
              try {
                env[8] /*stmt*/= PublishStatement(sub61, env);
              } catch(final GrinParserException e67) {
                try {
                  env[8] /*stmt*/= AbortStatement(sub61, env);
                } catch(final GrinParserException e68) {
                  try {
                    env[8] /*stmt*/= Script(sub61, env);
                  } catch(final GrinParserException e69) {
                    try {
                      env[8] /*stmt*/= Block(sub61, env);
                    } catch(final GrinParserException e70) {
                      try {
                        env[8] /*stmt*/= Assignment(sub61, env);
                      } catch(final GrinParserException e71) {
                        env[8] /*stmt*/= Invocation(sub61, env);
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
    sub61.commit();
    return sub61.isSet("stmt", env[8]);
  }

  static Object AssumeStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub72 = input.sub();
    sub72.accept("assume");
    Sep(sub72, env);
    try {
      final StringCursor sub73 = sub72.sub();
      sub73.accept("not");
      Sep(sub73, env);
      try {
        env[9] /*notted*/= java.lang.Boolean.TRUE;
      } catch(final Exception e74) {
        throw sub73.ex(e74);
      }
      sub73.commit();
    } catch(final GrinParserException e75) {
      final StringCursor sub76 = sub72.sub();
      try {
        env[9] /*notted*/= java.lang.Boolean.FALSE;
      } catch(final Exception e77) {
        throw sub76.ex(e77);
      }
      sub76.commit();
    }
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.AssumeStatement((java.lang.Boolean)sub72.isSet("notted", env[9]), (org.fuwjin.chessur.expression.Expression)Value(sub72, env));
      } catch(final Exception e78) {
        throw sub72.ex(e78);
      }
    } catch(final GrinParserException e79) {
      try {
        try {
          env[8] /*stmt*/= new org.fuwjin.chessur.expression.AssumeStatement((java.lang.Boolean)sub72.isSet("notted", env[9]), (org.fuwjin.chessur.expression.Expression)Statement(sub72, env));
        } catch(final Exception e80) {
          throw sub72.ex(e80);
        }
      } catch(final GrinParserException e81) {
        sub72.abort(String.valueOf("assume keyword requires value or in keyword"), e81);
      }
    }
    sub72.commit();
    return sub72.isSet("stmt", env[8]);
  }

  static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub82 = input.sub();
    sub82.accept("either");
    Sep(sub82, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.EitherOrStatement((org.fuwjin.chessur.expression.Expression)Statement(sub82, env));
      } catch(final Exception e83) {
        throw sub82.ex(e83);
      }
    } catch(final GrinParserException e84) {
      sub82.abort(String.valueOf("either keyword requires a statement"), e84);
    }
    try {
      final StringCursor sub85 = sub82.sub();
      sub85.accept("or");
      Sep(sub85, env);
      try {
        try {
          ((org.fuwjin.chessur.expression.EitherOrStatement)sub85.isSet("stmt", env[8])).or((org.fuwjin.chessur.expression.Expression)Statement(sub85, env));
        } catch(final Exception e86) {
          throw sub85.ex(e86);
        }
      } catch(final GrinParserException e87) {
        sub85.abort(String.valueOf("or keyword requires a statement"), e87);
      }
      sub85.commit();
      try {
        while(true) {
          final StringCursor sub88 = sub82.sub();
          sub88.accept("or");
          Sep(sub88, env);
          try {
            try {
              ((org.fuwjin.chessur.expression.EitherOrStatement)sub88.isSet("stmt", env[8])).or((org.fuwjin.chessur.expression.Expression)Statement(sub88, env));
            } catch(final Exception e89) {
              throw sub88.ex(e89);
            }
          } catch(final GrinParserException e90) {
            sub88.abort(String.valueOf("or keyword requires a statement"), e90);
          }
          sub88.commit();
        }
      } catch(final GrinParserException e91) {
        //continue
      }
    } catch(final GrinParserException e92) {
      sub82.abort(String.valueOf("either keyword requires at least one or keyword"), e92);
    }
    sub82.commit();
    return sub82.isSet("stmt", env[8]);
  }

  static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub93 = input.sub();
    sub93.accept("could");
    Sep(sub93, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.CouldStatement((org.fuwjin.chessur.expression.Expression)Statement(sub93, env));
      } catch(final Exception e94) {
        throw sub93.ex(e94);
      }
    } catch(final GrinParserException e95) {
      sub93.abort(String.valueOf("could keyword requires a statement"), e95);
    }
    sub93.commit();
    return sub93.isSet("stmt", env[8]);
  }

  static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub96 = input.sub();
    sub96.accept("repeat");
    Sep(sub96, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.RepeatStatement((org.fuwjin.chessur.expression.Expression)Statement(sub96, env));
      } catch(final Exception e97) {
        throw sub96.ex(e97);
      }
    } catch(final GrinParserException e98) {
      sub96.abort(String.valueOf("repeat keyword requires a statement"), e98);
    }
    sub96.commit();
    return sub96.isSet("stmt", env[8]);
  }

  static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub99 = input.sub();
    sub99.accept("accept");
    Sep(sub99, env);
    try {
      final StringCursor sub100 = sub99.sub();
      sub100.accept("not");
      Sep(sub100, env);
      try {
        env[9] /*notted*/= java.lang.Boolean.TRUE;
      } catch(final Exception e101) {
        throw sub100.ex(e101);
      }
      sub100.commit();
    } catch(final GrinParserException e102) {
      final StringCursor sub103 = sub99.sub();
      try {
        env[9] /*notted*/= java.lang.Boolean.FALSE;
      } catch(final Exception e104) {
        throw sub103.ex(e104);
      }
      sub103.commit();
    }
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.FilterAcceptStatement((java.lang.Boolean)sub99.isSet("notted", env[9]), (org.fuwjin.chessur.expression.Filter)InFilter(sub99, env));
      } catch(final Exception e105) {
        throw sub99.ex(e105);
      }
    } catch(final GrinParserException e106) {
      try {
        try {
          env[8] /*stmt*/= new org.fuwjin.chessur.expression.ValueAcceptStatement((java.lang.Boolean)sub99.isSet("notted", env[9]), (org.fuwjin.chessur.expression.Expression)Value(sub99, env));
        } catch(final Exception e107) {
          throw sub99.ex(e107);
        }
      } catch(final GrinParserException e108) {
        sub99.abort(String.valueOf("accept keyword requires a value or in keyword"), e108);
      }
    }
    sub99.commit();
    return sub99.isSet("stmt", env[8]);
  }

  static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub109 = input.sub();
    sub109.accept("publish");
    Sep(sub109, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.PublishStatement((org.fuwjin.chessur.expression.Expression)Value(sub109, env));
      } catch(final Exception e110) {
        throw sub109.ex(e110);
      }
    } catch(final GrinParserException e111) {
      sub109.abort(String.valueOf("publish keyword requires a value"), e111);
    }
    sub109.commit();
    return sub109.isSet("stmt", env[8]);
  }

  static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub112 = input.sub();
    sub112.accept("abort");
    Sep(sub112, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.AbortStatement((org.fuwjin.chessur.expression.Expression)Value(sub112, env));
      } catch(final Exception e113) {
        throw sub112.ex(e113);
      }
    } catch(final GrinParserException e114) {
      sub112.abort(String.valueOf("abort keyword requires a value"), e114);
    }
    sub112.commit();
    return sub112.isSet("stmt", env[8]);
  }

  static Object Block(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub115 = input.sub();
    sub115.accept("{");
    S(sub115, env);
    try {
      env[10] /*block*/= new org.fuwjin.chessur.expression.Block();
    } catch(final Exception e116) {
      throw sub115.ex(e116);
    }
    try {
      try {
        ((org.fuwjin.chessur.expression.Block)sub115.isSet("block", env[10])).add((org.fuwjin.chessur.expression.Expression)Statement(sub115, env));
      } catch(final Exception e117) {
        throw sub115.ex(e117);
      }
      try {
        while(true) {
          try {
            ((org.fuwjin.chessur.expression.Block)sub115.isSet("block", env[10])).add((org.fuwjin.chessur.expression.Expression)Statement(sub115, env));
          } catch(final Exception e118) {
            throw sub115.ex(e118);
          }
        }
      } catch(final GrinParserException e119) {
        //continue
      }
    } catch(final GrinParserException e120) {
      //continue
    }
    try {
      sub115.accept("}");
    } catch(final GrinParserException e121) {
      sub115.abort(String.valueOf("block must end with a brace"), e121);
    }
    S(sub115, env);
    sub115.commit();
    return sub115.isSet("block", env[10]);
  }

  static Object Assignment(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub122 = input.sub();
    env[1] /*name*/= Name(sub122, env);
    sub122.accept("=");
    S(sub122, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.Assignment((java.lang.String)sub122.isSet("name", env[1]), (org.fuwjin.chessur.expression.Expression)Value(sub122, env));
      } catch(final Exception e123) {
        throw sub122.ex(e123);
      }
    } catch(final GrinParserException e124) {
      sub122.abort(String.valueOf("assignment requires a value"), e124);
    }
    sub122.commit();
    return sub122.isSet("stmt", env[8]);
  }

  static Object Invocation(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub125 = input.sub();
    env[1] /*name*/= AliasName(sub125, env);
    sub125.accept("(");
    S(sub125, env);
    try {
      env[11] /*inv*/= new org.fuwjin.chessur.expression.Invocation();
    } catch(final Exception e126) {
      throw sub125.ex(e126);
    }
    try {
      final StringCursor sub127 = sub125.sub();
      try {
        ((org.fuwjin.chessur.expression.Invocation)sub127.isSet("inv", env[11])).addParam((org.fuwjin.chessur.expression.Expression)Value(sub127, env));
      } catch(final Exception e128) {
        throw sub127.ex(e128);
      }
      try {
        final StringCursor sub129 = sub127.sub();
        sub129.accept(",");
        S(sub129, env);
        try {
          try {
            ((org.fuwjin.chessur.expression.Invocation)sub129.isSet("inv", env[11])).addParam((org.fuwjin.chessur.expression.Expression)Value(sub129, env));
          } catch(final Exception e130) {
            throw sub129.ex(e130);
          }
        } catch(final GrinParserException e131) {
          sub129.abort(String.valueOf("invocation parameter must be a value"), e131);
        }
        sub129.commit();
        try {
          while(true) {
            final StringCursor sub132 = sub127.sub();
            sub132.accept(",");
            S(sub132, env);
            try {
              try {
                ((org.fuwjin.chessur.expression.Invocation)sub132.isSet("inv", env[11])).addParam((org.fuwjin.chessur.expression.Expression)Value(sub132, env));
              } catch(final Exception e133) {
                throw sub132.ex(e133);
              }
            } catch(final GrinParserException e134) {
              sub132.abort(String.valueOf("invocation parameter must be a value"), e134);
            }
            sub132.commit();
          }
        } catch(final GrinParserException e135) {
          //continue
        }
      } catch(final GrinParserException e136) {
        //continue
      }
      sub127.commit();
    } catch(final GrinParserException e137) {
      //continue
    }
    try {
      sub125.accept(")");
    } catch(final GrinParserException e138) {
      sub125.abort(String.valueOf("invocation must end with a parenthesis"), e138);
    }
    S(sub125, env);
    try {
      final StringCursor sub139 = sub125.sub();
      try {
        env[5] /*signature*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub139.isSet("cat", env[0])).getSignature((java.lang.String)sub139.isSet("name", env[1]), (java.lang.Integer)((org.fuwjin.chessur.expression.Invocation)sub139.isSet("inv", env[11])).paramCount());
      } catch(final Exception e140) {
        throw sub139.ex(e140);
      }
      try {
        env[12] /*function*/= ((org.fuwjin.dinah.FunctionProvider)sub139.isSet("manager", env[2])).getFunction((org.fuwjin.dinah.FunctionSignature)sub139.isSet("signature", env[5]));
      } catch(final Exception e141) {
        throw sub139.ex(e141);
      }
      try {
        ((org.fuwjin.chessur.expression.Invocation)sub139.isSet("inv", env[11])).setFunction((org.fuwjin.dinah.Function)sub139.isSet("function", env[12]));
      } catch(final Exception e142) {
        throw sub139.ex(e142);
      }
      sub139.commit();
    } catch(final GrinParserException e143) {
      sub125.abort(String.valueOf("Could not get function for ") + String.valueOf(sub125.isSet("name", env[1])), e143);
    }
    sub125.commit();
    return sub125.isSet("inv", env[11]);
  }

  static Object Script(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub144 = input.sub();
    env[6] /*script*/= ScriptIdent(sub144, env);
    try {
      final StringCursor sub145 = sub144.sub();
      sub145.accept("<<");
      S(sub145, env);
      try {
        env[6] /*script*/= new org.fuwjin.chessur.expression.ScriptInput((org.fuwjin.chessur.expression.Expression)sub145.isSet("script", env[6]), (org.fuwjin.chessur.expression.Expression)Value(sub145, env));
      } catch(final Exception e146) {
        throw sub145.ex(e146);
      }
      sub145.commit();
    } catch(final GrinParserException e147) {
      //continue
    }
    try {
      final StringCursor sub148 = sub144.sub();
      sub148.accept(">>");
      S(sub148, env);
      try {
        env[6] /*script*/= new org.fuwjin.chessur.expression.ScriptPipe((org.fuwjin.chessur.expression.Expression)sub148.isSet("script", env[6]), (org.fuwjin.chessur.expression.Expression)ScriptIdent(sub148, env));
      } catch(final Exception e149) {
        throw sub148.ex(e149);
      }
      sub148.commit();
      try {
        while(true) {
          final StringCursor sub150 = sub144.sub();
          sub150.accept(">>");
          S(sub150, env);
          try {
            env[6] /*script*/= new org.fuwjin.chessur.expression.ScriptPipe((org.fuwjin.chessur.expression.Expression)sub150.isSet("script", env[6]), (org.fuwjin.chessur.expression.Expression)ScriptIdent(sub150, env));
          } catch(final Exception e151) {
            throw sub150.ex(e151);
          }
          sub150.commit();
        }
      } catch(final GrinParserException e152) {
        //continue
      }
    } catch(final GrinParserException e153) {
      //continue
    }
    try {
      final StringCursor sub154 = sub144.sub();
      sub154.accept(">>");
      S(sub154, env);
      try {
        env[6] /*script*/= new org.fuwjin.chessur.expression.ScriptOutput((org.fuwjin.chessur.expression.Expression)sub154.isSet("script", env[6]), (java.lang.String)Name(sub154, env));
      } catch(final Exception e155) {
        throw sub154.ex(e155);
      }
      sub154.commit();
    } catch(final GrinParserException e156) {
      //continue
    }
    sub144.commit();
    return sub144.isSet("script", env[6]);
  }

  static Object ScriptIdent(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub157 = input.sub();
    sub157.accept("<");
    try {
      env[13] /*id*/= Identifier(sub157, env);
    } catch(final GrinParserException e158) {
      sub157.abort(String.valueOf("script identifiers must be enclosed in angle brackets"), e158);
    }
    try {
      final StringCursor sub159 = sub157.sub();
      sub159.accept(":");
      try {
        final StringCursor sub160 = sub159.sub();
        env[1] /*name*/= Identifier(sub160, env);
        try {
          env[14] /*module*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub160.isSet("cat", env[0])).getModule((java.lang.String)sub160.isSet("id", env[13]));
        } catch(final Exception e161) {
          throw sub160.ex(e161);
        }
        try {
          env[6] /*script*/= ((org.fuwjin.chessur.Module)sub160.isSet("module", env[14])).get((java.lang.String)sub160.isSet("name", env[1]));
        } catch(final Exception e162) {
          throw sub160.ex(e162);
        }
        sub160.commit();
      } catch(final GrinParserException e163) {
        sub159.abort(String.valueOf("namespaced script ") + String.valueOf(sub159.isSet("id", env[13])) + String.valueOf(": could not be resolved"), e163);
      }
      sub159.commit();
    } catch(final GrinParserException e164) {
      final StringCursor sub165 = sub157.sub();
      try {
        env[6] /*script*/= ((org.fuwjin.chessur.Module)sub165.isSet("cat", env[0])).get((java.lang.String)sub165.isSet("id", env[13]));
      } catch(final Exception e166) {
        throw sub165.ex(e166);
      }
      sub165.commit();
    }
    try {
      sub157.accept(">");
    } catch(final GrinParserException e167) {
      sub157.abort(String.valueOf("script identifiers must be normal identifiers in angle brackets"), e167);
    }
    S(sub157, env);
    sub157.commit();
    return sub157.isSet("script", env[6]);
  }

  static Object Object(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub168 = input.sub();
    sub168.accept("(");
    S(sub168, env);
    env[15] /*type*/= AliasName(sub168, env);
    try {
      env[16] /*constructor*/= ((org.fuwjin.dinah.FunctionProvider)sub168.isSet("manager", env[2])).getFunction((org.fuwjin.dinah.FunctionSignature)new org.fuwjin.dinah.signature.ArgCountSignature((java.lang.String)String.valueOf(sub168.isSet("type", env[15])) + String.valueOf(".new"), (java.lang.Integer)0));
    } catch(final Exception e169) {
      throw sub168.ex(e169);
    }
    try {
      env[17] /*object*/= new org.fuwjin.chessur.expression.ObjectTemplate((java.lang.String)sub168.isSet("type", env[15]), (org.fuwjin.dinah.Function)sub168.isSet("constructor", env[16]));
    } catch(final Exception e170) {
      throw sub168.ex(e170);
    }
    sub168.accept(")");
    S(sub168, env);
    sub168.accept("{");
    S(sub168, env);
    try {
      final StringCursor sub171 = sub168.sub();
      Field(sub171, env);
      try {
        final StringCursor sub172 = sub171.sub();
        sub172.accept(",");
        S(sub172, env);
        Field(sub172, env);
        sub172.commit();
        try {
          while(true) {
            final StringCursor sub173 = sub171.sub();
            sub173.accept(",");
            S(sub173, env);
            Field(sub173, env);
            sub173.commit();
          }
        } catch(final GrinParserException e174) {
          //continue
        }
      } catch(final GrinParserException e175) {
        //continue
      }
      sub171.commit();
    } catch(final GrinParserException e176) {
      //continue
    }
    sub168.accept("}");
    S(sub168, env);
    sub168.commit();
    return sub168.isSet("object", env[17]);
  }

  static Object Field(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub177 = input.sub();
    env[1] /*name*/= Name(sub177, env);
    try {
      env[18] /*setter*/= ((org.fuwjin.dinah.FunctionProvider)sub177.isSet("manager", env[2])).getFunction((org.fuwjin.dinah.FunctionSignature)new org.fuwjin.dinah.signature.ArgCountSignature((java.lang.String)String.valueOf(sub177.isSet("type", env[15])) + String.valueOf(".") + String.valueOf(sub177.isSet("name", env[1])), (java.lang.Integer)2));
    } catch(final Exception e178) {
      throw sub177.ex(e178);
    }
    sub177.accept(":");
    S(sub177, env);
    try {
      ((org.fuwjin.chessur.expression.ObjectTemplate)sub177.isSet("object", env[17])).set((java.lang.String)sub177.isSet("name", env[1]), (org.fuwjin.dinah.Function)sub177.isSet("setter", env[18]), (org.fuwjin.chessur.expression.Expression)Value(sub177, env));
    } catch(final Exception e179) {
      throw sub177.ex(e179);
    }
    sub177.commit();
    return null;
  }

  static Object MatchValue(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub180 = input.sub();
    sub180.accept("match");
    Sep(sub180, env);
    sub180.commit();
    return ((org.fuwjin.chessur.expression.Declaration)sub180.isSet("script", env[6])).match();
  }

  static Object NextValue(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub181 = input.sub();
    sub181.accept("next");
    Sep(sub181, env);
    sub181.commit();
    return org.fuwjin.chessur.expression.Variable.NEXT;
  }

  static Object InFilter(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub182 = input.sub();
    sub182.accept("in");
    Sep(sub182, env);
    try {
      env[19] /*filter*/= new org.fuwjin.chessur.expression.Filter();
    } catch(final Exception e183) {
      throw sub182.ex(e183);
    }
    try {
      FilterRange(sub182, env);
    } catch(final GrinParserException e184) {
      sub182.abort(String.valueOf("in keyword requires at least one filter"), e184);
    }
    try {
      final StringCursor sub185 = sub182.sub();
      sub185.accept(",");
      S(sub185, env);
      try {
        FilterRange(sub185, env);
      } catch(final GrinParserException e186) {
        sub185.abort(String.valueOf("in keyword requires a filter after a comma"), e186);
      }
      sub185.commit();
      try {
        while(true) {
          final StringCursor sub187 = sub182.sub();
          sub187.accept(",");
          S(sub187, env);
          try {
            FilterRange(sub187, env);
          } catch(final GrinParserException e188) {
            sub187.abort(String.valueOf("in keyword requires a filter after a comma"), e188);
          }
          sub187.commit();
        }
      } catch(final GrinParserException e189) {
        //continue
      }
    } catch(final GrinParserException e190) {
      //continue
    }
    sub182.commit();
    return sub182.isSet("filter", env[19]);
  }

  static Object FilterRange(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub191 = input.sub();
    env[20] /*start*/= FilterChar(sub191, env);
    S(sub191, env);
    try {
      final StringCursor sub192 = sub191.sub();
      sub192.accept("-");
      S(sub192, env);
      try {
        ((org.fuwjin.chessur.expression.Filter)sub192.isSet("filter", env[19])).addRange((java.lang.Integer)sub192.isSet("start", env[20]), (java.lang.Integer)FilterChar(sub192, env));
      } catch(final Exception e193) {
        throw sub192.ex(e193);
      }
      S(sub192, env);
      sub192.commit();
    } catch(final GrinParserException e194) {
      final StringCursor sub195 = sub191.sub();
      try {
        ((org.fuwjin.chessur.expression.Filter)sub195.isSet("filter", env[19])).addChar((java.lang.Integer)sub195.isSet("start", env[20]));
      } catch(final Exception e196) {
        throw sub195.ex(e196);
      }
      sub195.commit();
    }
    sub191.commit();
    return null;
  }

  static Object FilterChar(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub197 = input.sub();
    try {
      final StringCursor sub198 = sub197.sub();
      env[21] /*ch*/= Escape(sub198, env);
      sub198.commit();
    } catch(final GrinParserException e199) {
      env[21] /*ch*/= sub197.acceptNot("\\");
    }
    sub197.commit();
    return sub197.isSet("ch", env[21]);
  }

  static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub200 = input.sub();
    sub200.accept("'");
    try {
      env[22] /*lit*/= new org.fuwjin.chessur.expression.Literal();
    } catch(final Exception e201) {
      throw sub200.ex(e201);
    }
    try {
      try {
        final StringCursor sub202 = sub200.sub();
        env[21] /*ch*/= sub202.acceptNotIn("'\\","'\\");
        try {
          ((org.fuwjin.chessur.expression.Literal)sub202.isSet("lit", env[22])).append((java.lang.Integer)sub202.isSet("ch", env[21]));
        } catch(final Exception e203) {
          throw sub202.ex(e203);
        }
        sub202.commit();
      } catch(final GrinParserException e204) {
        final StringCursor sub205 = sub200.sub();
        try {
          ((org.fuwjin.chessur.expression.Literal)sub205.isSet("lit", env[22])).append((java.lang.Integer)Escape(sub205, env));
        } catch(final Exception e206) {
          throw sub205.ex(e206);
        }
        sub205.commit();
      }
      try {
        while(true) {
          try {
            final StringCursor sub207 = sub200.sub();
            env[21] /*ch*/= sub207.acceptNotIn("'\\","'\\");
            try {
              ((org.fuwjin.chessur.expression.Literal)sub207.isSet("lit", env[22])).append((java.lang.Integer)sub207.isSet("ch", env[21]));
            } catch(final Exception e208) {
              throw sub207.ex(e208);
            }
            sub207.commit();
          } catch(final GrinParserException e209) {
            final StringCursor sub210 = sub200.sub();
            try {
              ((org.fuwjin.chessur.expression.Literal)sub210.isSet("lit", env[22])).append((java.lang.Integer)Escape(sub210, env));
            } catch(final Exception e211) {
              throw sub210.ex(e211);
            }
            sub210.commit();
          }
        }
      } catch(final GrinParserException e212) {
        //continue
      }
    } catch(final GrinParserException e213) {
      //continue
    }
    try {
      sub200.accept("'");
    } catch(final GrinParserException e214) {
      sub200.abort(String.valueOf("static literals must end with a quote"), e214);
    }
    S(sub200, env);
    sub200.commit();
    return sub200.isSet("lit", env[22]);
  }

  static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub215 = input.sub();
    sub215.accept("\"");
    try {
      env[22] /*lit*/= new org.fuwjin.chessur.expression.CompositeLiteral();
    } catch(final Exception e216) {
      throw sub215.ex(e216);
    }
    try {
      try {
        final StringCursor sub217 = sub215.sub();
        sub217.accept("'");
        S(sub217, env);
        try {
          ((org.fuwjin.chessur.expression.CompositeLiteral)sub217.isSet("lit", env[22])).append((org.fuwjin.chessur.expression.Expression)Value(sub217, env));
        } catch(final Exception e218) {
          throw sub217.ex(e218);
        }
        sub217.accept("'");
        sub217.commit();
      } catch(final GrinParserException e219) {
        try {
          final StringCursor sub220 = sub215.sub();
          try {
            ((org.fuwjin.chessur.expression.CompositeLiteral)sub220.isSet("lit", env[22])).appendChar((java.lang.Integer)Escape(sub220, env));
          } catch(final Exception e221) {
            throw sub220.ex(e221);
          }
          sub220.commit();
        } catch(final GrinParserException e222) {
          final StringCursor sub223 = sub215.sub();
          env[21] /*ch*/= sub223.acceptNotIn("\"\\","\"\\");
          try {
            ((org.fuwjin.chessur.expression.CompositeLiteral)sub223.isSet("lit", env[22])).appendChar((java.lang.Integer)sub223.isSet("ch", env[21]));
          } catch(final Exception e224) {
            throw sub223.ex(e224);
          }
          sub223.commit();
        }
      }
      try {
        while(true) {
          try {
            final StringCursor sub225 = sub215.sub();
            sub225.accept("'");
            S(sub225, env);
            try {
              ((org.fuwjin.chessur.expression.CompositeLiteral)sub225.isSet("lit", env[22])).append((org.fuwjin.chessur.expression.Expression)Value(sub225, env));
            } catch(final Exception e226) {
              throw sub225.ex(e226);
            }
            sub225.accept("'");
            sub225.commit();
          } catch(final GrinParserException e227) {
            try {
              final StringCursor sub228 = sub215.sub();
              try {
                ((org.fuwjin.chessur.expression.CompositeLiteral)sub228.isSet("lit", env[22])).appendChar((java.lang.Integer)Escape(sub228, env));
              } catch(final Exception e229) {
                throw sub228.ex(e229);
              }
              sub228.commit();
            } catch(final GrinParserException e230) {
              final StringCursor sub231 = sub215.sub();
              env[21] /*ch*/= sub231.acceptNotIn("\"\\","\"\\");
              try {
                ((org.fuwjin.chessur.expression.CompositeLiteral)sub231.isSet("lit", env[22])).appendChar((java.lang.Integer)sub231.isSet("ch", env[21]));
              } catch(final Exception e232) {
                throw sub231.ex(e232);
              }
              sub231.commit();
            }
          }
        }
      } catch(final GrinParserException e233) {
        //continue
      }
    } catch(final GrinParserException e234) {
      //continue
    }
    try {
      sub215.accept("\"");
    } catch(final GrinParserException e235) {
      sub215.abort(String.valueOf("dynamic literals must end with a double quote"), e235);
    }
    S(sub215, env);
    sub215.commit();
    return sub215.isSet("lit", env[22]);
  }

  static Object Escape(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub236 = input.sub();
    sub236.accept("\\");
    try {
      final StringCursor sub237 = sub236.sub();
      sub237.accept("n");
      try {
        env[21] /*ch*/= org.fuwjin.chessur.expression.Literal.NEW_LINE;
      } catch(final Exception e238) {
        throw sub237.ex(e238);
      }
      sub237.commit();
    } catch(final GrinParserException e239) {
      try {
        final StringCursor sub240 = sub236.sub();
        sub240.accept("t");
        try {
          env[21] /*ch*/= org.fuwjin.chessur.expression.Literal.TAB;
        } catch(final Exception e241) {
          throw sub240.ex(e241);
        }
        sub240.commit();
      } catch(final GrinParserException e242) {
        try {
          final StringCursor sub243 = sub236.sub();
          sub243.accept("r");
          try {
            env[21] /*ch*/= org.fuwjin.chessur.expression.Literal.RETURN;
          } catch(final Exception e244) {
            throw sub243.ex(e244);
          }
          sub243.commit();
        } catch(final GrinParserException e245) {
          try {
            final StringCursor sub246 = sub236.sub();
            sub246.accept("x");
            try {
              env[21] /*ch*/= org.fuwjin.chessur.expression.Literal.parseHex((java.lang.String)HexDigits(sub246, env));
            } catch(final Exception e247) {
              throw sub246.ex(e247);
            }
            sub246.commit();
          } catch(final GrinParserException e248) {
            final StringCursor sub249 = sub236.sub();
            env[21] /*ch*/= sub249.accept();
            sub249.commit();
          }
        }
      }
    }
    sub236.commit();
    return sub236.isSet("ch", env[21]);
  }

  static Object HexDigits(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub250 = input.sub();
    HexDigit(sub250, env);
    HexDigit(sub250, env);
    HexDigit(sub250, env);
    HexDigit(sub250, env);
    sub250.commit();
    return sub250.match();
  }

  static Object HexDigit(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub251 = input.sub();
    sub251.acceptIn("0-9A-Fa-f","0123456789ABCDEFabcdef");
    sub251.commit();
    return null;
  }

  static Object Number(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub252 = input.sub();
    try {
      sub252.accept("-");
    } catch(final GrinParserException e253) {
      //continue
    }
    try {
      final StringCursor sub254 = sub252.sub();
      sub254.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub254.acceptIn("0-9","0123456789");
        }
      } catch(final GrinParserException e255) {
        //continue
      }
      try {
        final StringCursor sub256 = sub254.sub();
        sub256.accept(".");
        try {
          sub256.acceptIn("0-9","0123456789");
          try {
            while(true) {
              sub256.acceptIn("0-9","0123456789");
            }
          } catch(final GrinParserException e257) {
            //continue
          }
        } catch(final GrinParserException e258) {
          //continue
        }
        sub256.commit();
      } catch(final GrinParserException e259) {
        //continue
      }
      sub254.commit();
    } catch(final GrinParserException e260) {
      final StringCursor sub261 = sub252.sub();
      sub261.accept(".");
      sub261.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub261.acceptIn("0-9","0123456789");
        }
      } catch(final GrinParserException e262) {
        //continue
      }
      sub261.commit();
    }
    try {
      final StringCursor sub263 = sub252.sub();
      sub263.acceptIn("Ee","Ee");
      try {
        sub263.accept("-");
      } catch(final GrinParserException e264) {
        //continue
      }
      sub263.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub263.acceptIn("0-9","0123456789");
        }
      } catch(final GrinParserException e265) {
        //continue
      }
      sub263.commit();
    } catch(final GrinParserException e266) {
      //continue
    }
    try {
      env[23] /*num*/= new org.fuwjin.chessur.expression.Number((java.lang.String)sub252.match());
    } catch(final Exception e267) {
      throw sub252.ex(e267);
    }
    Sep(sub252, env);
    sub252.commit();
    return sub252.isSet("num", env[23]);
  }

  static Object Path(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub268 = input.sub();
    final StringCursor sub269 = sub268.sub();
    try {
      sub269.accept("/");
    } catch(final GrinParserException e270) {
      //continue
    }
    QualifiedIdentifier(sub269, env);
    sub269.commit();
    try {
      while(true) {
        final StringCursor sub271 = sub268.sub();
        try {
          sub271.accept("/");
        } catch(final GrinParserException e272) {
          //continue
        }
        QualifiedIdentifier(sub271, env);
        sub271.commit();
      }
    } catch(final GrinParserException e273) {
      //continue
    }
    try {
      sub268.accept("/");
    } catch(final GrinParserException e274) {
      //continue
    }
    sub268.commit();
    return sub268.match();
  }

  static Object PathName(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub275 = input.sub();
    env[3] /*path*/= Path(sub275, env);
    S(sub275, env);
    sub275.commit();
    return sub275.isSet("path", env[3]);
  }

  static Object AliasName(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub276 = input.sub();
    env[24] /*prefix*/= Identifier(sub276, env);
    try {
      env[25] /*alias*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub276.isSet("cat", env[0])).alias((java.lang.String)sub276.isSet("prefix", env[24]));
    } catch(final Exception e277) {
      throw sub276.ex(e277);
    }
    try {
      final StringCursor sub278 = sub276.sub();
      sub278.accept(".");
      env[1] /*name*/= String.valueOf(sub278.isSet("alias", env[25])) + String.valueOf(".") + String.valueOf(QualifiedName(sub278, env));
      sub278.commit();
    } catch(final GrinParserException e279) {
      final StringCursor sub280 = sub276.sub();
      env[1] /*name*/= sub280.isSet("alias", env[25]);
      sub280.commit();
    }
    sub276.commit();
    return sub276.isSet("name", env[1]);
  }

  static Object Name(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub281 = input.sub();
    env[13] /*id*/= Identifier(sub281, env);
    S(sub281, env);
    sub281.commit();
    return sub281.isSet("id", env[13]);
  }

  static Object QualifiedName(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub282 = input.sub();
    env[13] /*id*/= QualifiedIdentifier(sub282, env);
    S(sub282, env);
    sub282.commit();
    return sub282.isSet("id", env[13]);
  }

  static Object QualifiedIdentifier(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub283 = input.sub();
    AnnotatedIdentifier(sub283, env);
    try {
      final StringCursor sub284 = sub283.sub();
      sub284.accept(".");
      AnnotatedIdentifier(sub284, env);
      sub284.commit();
      try {
        while(true) {
          final StringCursor sub285 = sub283.sub();
          sub285.accept(".");
          AnnotatedIdentifier(sub285, env);
          sub285.commit();
        }
      } catch(final GrinParserException e286) {
        //continue
      }
    } catch(final GrinParserException e287) {
      //continue
    }
    sub283.commit();
    return sub283.match();
  }

  static Object AnnotatedIdentifier(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub288 = input.sub();
    Identifier(sub288, env);
    try {
      final StringCursor sub289 = sub288.sub();
      sub289.accept("[");
      try {
        Identifier(sub289, env);
      } catch(final GrinParserException e290) {
        //continue
      }
      sub289.accept("]");
      sub289.commit();
      try {
        while(true) {
          final StringCursor sub291 = sub288.sub();
          sub291.accept("[");
          try {
            Identifier(sub291, env);
          } catch(final GrinParserException e292) {
            //continue
          }
          sub291.accept("]");
          sub291.commit();
        }
      } catch(final GrinParserException e293) {
        //continue
      }
    } catch(final GrinParserException e294) {
      //continue
    }
    sub288.commit();
    return null;
  }

  static Object Identifier(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub295 = input.sub();
    IdentifierChar(sub295, env);
    try {
      while(true) {
        IdentifierChar(sub295, env);
      }
    } catch(final GrinParserException e296) {
      //continue
    }
    sub295.commit();
    return sub295.match();
  }

  static Object IdentifierChar(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub297 = input.sub();
    final StringCursor sub298 = sub297.sub();
    if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub298.next()) == Boolean.FALSE) {
      throw sub298.ex("check failed");
    }
    sub297.accept();
    sub297.commit();
    return null;
  }

  static Object Sep(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub299 = input.sub();
    final StringCursor sub300 = sub299.sub();
    boolean b301 = true;
    try {
      if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub300.next()) == Boolean.FALSE) {
        b301 = false;
      }
    } catch(final GrinParserException e302) {
      b301 = false;
    }
    if(b301){
      throw sub299.ex("unexpected value");
    }
    S(sub299, env);
    sub299.commit();
    return null;
  }

  static Object S(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub303 = input.sub();
    try {
      Space(sub303, env);
    } catch(final GrinParserException e304) {
      //continue
    }
    sub303.commit();
    return null;
  }

  static Object Space(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub305 = input.sub();
    try {
      sub305.acceptIn("\t-\n\r ","\t\n\r ");
    } catch(final GrinParserException e306) {
      Comment(sub305, env);
    }
    try {
      while(true) {
        try {
          sub305.acceptIn("\t-\n\r ","\t\n\r ");
        } catch(final GrinParserException e307) {
          Comment(sub305, env);
        }
      }
    } catch(final GrinParserException e308) {
      //continue
    }
    sub305.commit();
    return null;
  }

  static Object Comment(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub309 = input.sub();
    sub309.accept("#");
    try {
      sub309.acceptNotIn("\n\r","\n\r");
      try {
        while(true) {
          sub309.acceptNotIn("\n\r","\n\r");
        }
      } catch(final GrinParserException e310) {
        //continue
      }
    } catch(final GrinParserException e311) {
      //continue
    }
    try {
      sub309.accept("\r");
    } catch(final GrinParserException e312) {
      //continue
    }
    try {
      sub309.accept("\n");
    } catch(final GrinParserException e313) {
      EndOfFile(sub309, env);
    }
    sub309.commit();
    return null;
  }

  static Object EndOfFile(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub314 = input.sub();
    final StringCursor sub315 = sub314.sub();
    boolean b316 = true;
    try {
      if((Object)sub315.next() == Boolean.FALSE) {
        b316 = false;
      }
    } catch(final GrinParserException e317) {
      b316 = false;
    }
    if(b316){
      throw sub314.ex("unexpected value");
    }
    sub314.commit();
    return null;
  }
  
  public static Object interpret(final CharSequence in, final Appendable out, final Map<String, ?> environment) throws GrinParserException {
    final StringCursor input = new StringCursor(in, out);
    final Object[] env = new Object[26];
    env[0] = environment.containsKey("cat") ? environment.get("cat") : UNSET;
    env[1] = environment.containsKey("name") ? environment.get("name") : UNSET;
    env[2] = environment.containsKey("manager") ? environment.get("manager") : UNSET;
    env[3] = environment.containsKey("path") ? environment.get("path") : UNSET;
    env[4] = environment.containsKey("qname") ? environment.get("qname") : UNSET;
    env[5] = environment.containsKey("signature") ? environment.get("signature") : UNSET;
    env[6] = environment.containsKey("script") ? environment.get("script") : UNSET;
    env[7] = environment.containsKey("val") ? environment.get("val") : UNSET;
    env[8] = environment.containsKey("stmt") ? environment.get("stmt") : UNSET;
    env[9] = environment.containsKey("notted") ? environment.get("notted") : UNSET;
    env[10] = environment.containsKey("block") ? environment.get("block") : UNSET;
    env[11] = environment.containsKey("inv") ? environment.get("inv") : UNSET;
    env[12] = environment.containsKey("function") ? environment.get("function") : UNSET;
    env[13] = environment.containsKey("id") ? environment.get("id") : UNSET;
    env[14] = environment.containsKey("module") ? environment.get("module") : UNSET;
    env[15] = environment.containsKey("type") ? environment.get("type") : UNSET;
    env[16] = environment.containsKey("constructor") ? environment.get("constructor") : UNSET;
    env[17] = environment.containsKey("object") ? environment.get("object") : UNSET;
    env[18] = environment.containsKey("setter") ? environment.get("setter") : UNSET;
    env[19] = environment.containsKey("filter") ? environment.get("filter") : UNSET;
    env[20] = environment.containsKey("start") ? environment.get("start") : UNSET;
    env[21] = environment.containsKey("ch") ? environment.get("ch") : UNSET;
    env[22] = environment.containsKey("lit") ? environment.get("lit") : UNSET;
    env[23] = environment.containsKey("num") ? environment.get("num") : UNSET;
    env[24] = environment.containsKey("prefix") ? environment.get("prefix") : UNSET;
    env[25] = environment.containsKey("alias") ? environment.get("alias") : UNSET;
    return Catalog(input, env);
  }
}
