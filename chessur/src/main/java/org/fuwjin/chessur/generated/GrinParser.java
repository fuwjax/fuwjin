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
    
    public String nextStr() throws GrinParserException {
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
          env[5] /*signature*/= ((org.fuwjin.dinah.FunctionProvider)sub22.isSet("manager", env[2])).forName((java.lang.String)sub22.isSet("qname", env[4]));
        } catch(final Exception e23) {
          throw sub22.ex(e23);
        }
        try {
          final StringCursor sub24 = sub22.sub();
          try {
            ((org.fuwjin.dinah.ConstraintBuilder)sub24.isSet("signature", env[5])).withNextArg((java.lang.String)QualifiedName(sub24, env));
          } catch(final Exception e25) {
            throw sub24.ex(e25);
          }
          try {
            final StringCursor sub26 = sub24.sub();
            sub26.accept(",");
            S(sub26, env);
            try {
              ((org.fuwjin.dinah.ConstraintBuilder)sub26.isSet("signature", env[5])).withNextArg((java.lang.String)QualifiedName(sub26, env));
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
                  ((org.fuwjin.dinah.ConstraintBuilder)sub28.isSet("signature", env[5])).withNextArg((java.lang.String)QualifiedName(sub28, env));
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
            ((org.fuwjin.chessur.expression.CatalogImpl)sub22.isSet("cat", env[0])).aliasSignature((org.fuwjin.dinah.SignatureConstraint)((org.fuwjin.dinah.ConstraintBuilder)sub22.isSet("signature", env[5])).constraint(), (java.lang.String)Name(sub22, env));
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
                    env[8] /*stmt*/= LogStatement(sub61, env);
                  } catch(final GrinParserException e69) {
                    try {
                      env[8] /*stmt*/= Script(sub61, env);
                    } catch(final GrinParserException e70) {
                      try {
                        env[8] /*stmt*/= Block(sub61, env);
                      } catch(final GrinParserException e71) {
                        try {
                          env[8] /*stmt*/= Assignment(sub61, env);
                        } catch(final GrinParserException e72) {
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
    }
    sub61.commit();
    return sub61.isSet("stmt", env[8]);
  }

  static Object AssumeStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub73 = input.sub();
    sub73.accept("assume");
    Sep(sub73, env);
    try {
      final StringCursor sub74 = sub73.sub();
      sub74.accept("not");
      Sep(sub74, env);
      try {
        env[9] /*notted*/= java.lang.Boolean.TRUE;
      } catch(final Exception e75) {
        throw sub74.ex(e75);
      }
      sub74.commit();
    } catch(final GrinParserException e76) {
      final StringCursor sub77 = sub73.sub();
      try {
        env[9] /*notted*/= java.lang.Boolean.FALSE;
      } catch(final Exception e78) {
        throw sub77.ex(e78);
      }
      sub77.commit();
    }
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.AssumeStatement((java.lang.Boolean)sub73.isSet("notted", env[9]), (org.fuwjin.chessur.expression.Expression)Value(sub73, env));
      } catch(final Exception e79) {
        throw sub73.ex(e79);
      }
    } catch(final GrinParserException e80) {
      try {
        try {
          env[8] /*stmt*/= new org.fuwjin.chessur.expression.AssumeStatement((java.lang.Boolean)sub73.isSet("notted", env[9]), (org.fuwjin.chessur.expression.Expression)Statement(sub73, env));
        } catch(final Exception e81) {
          throw sub73.ex(e81);
        }
      } catch(final GrinParserException e82) {
        sub73.abort(String.valueOf("assume keyword requires value or in keyword"), e82);
      }
    }
    sub73.commit();
    return sub73.isSet("stmt", env[8]);
  }

  static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub83 = input.sub();
    sub83.accept("either");
    Sep(sub83, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.EitherOrStatement((org.fuwjin.chessur.expression.Expression)Statement(sub83, env));
      } catch(final Exception e84) {
        throw sub83.ex(e84);
      }
    } catch(final GrinParserException e85) {
      sub83.abort(String.valueOf("either keyword requires a statement"), e85);
    }
    try {
      final StringCursor sub86 = sub83.sub();
      sub86.accept("or");
      Sep(sub86, env);
      try {
        try {
          ((org.fuwjin.chessur.expression.EitherOrStatement)sub86.isSet("stmt", env[8])).or((org.fuwjin.chessur.expression.Expression)Statement(sub86, env));
        } catch(final Exception e87) {
          throw sub86.ex(e87);
        }
      } catch(final GrinParserException e88) {
        sub86.abort(String.valueOf("or keyword requires a statement"), e88);
      }
      sub86.commit();
      try {
        while(true) {
          final StringCursor sub89 = sub83.sub();
          sub89.accept("or");
          Sep(sub89, env);
          try {
            try {
              ((org.fuwjin.chessur.expression.EitherOrStatement)sub89.isSet("stmt", env[8])).or((org.fuwjin.chessur.expression.Expression)Statement(sub89, env));
            } catch(final Exception e90) {
              throw sub89.ex(e90);
            }
          } catch(final GrinParserException e91) {
            sub89.abort(String.valueOf("or keyword requires a statement"), e91);
          }
          sub89.commit();
        }
      } catch(final GrinParserException e92) {
        //continue
      }
    } catch(final GrinParserException e93) {
      sub83.abort(String.valueOf("either keyword requires at least one or keyword"), e93);
    }
    sub83.commit();
    return sub83.isSet("stmt", env[8]);
  }

  static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub94 = input.sub();
    sub94.accept("could");
    Sep(sub94, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.CouldStatement((org.fuwjin.chessur.expression.Expression)Statement(sub94, env));
      } catch(final Exception e95) {
        throw sub94.ex(e95);
      }
    } catch(final GrinParserException e96) {
      sub94.abort(String.valueOf("could keyword requires a statement"), e96);
    }
    sub94.commit();
    return sub94.isSet("stmt", env[8]);
  }

  static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub97 = input.sub();
    sub97.accept("repeat");
    Sep(sub97, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.RepeatStatement((org.fuwjin.chessur.expression.Expression)Statement(sub97, env));
      } catch(final Exception e98) {
        throw sub97.ex(e98);
      }
    } catch(final GrinParserException e99) {
      sub97.abort(String.valueOf("repeat keyword requires a statement"), e99);
    }
    sub97.commit();
    return sub97.isSet("stmt", env[8]);
  }

  static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub100 = input.sub();
    sub100.accept("accept");
    Sep(sub100, env);
    try {
      final StringCursor sub101 = sub100.sub();
      sub101.accept("not");
      Sep(sub101, env);
      try {
        env[9] /*notted*/= java.lang.Boolean.TRUE;
      } catch(final Exception e102) {
        throw sub101.ex(e102);
      }
      sub101.commit();
    } catch(final GrinParserException e103) {
      final StringCursor sub104 = sub100.sub();
      try {
        env[9] /*notted*/= java.lang.Boolean.FALSE;
      } catch(final Exception e105) {
        throw sub104.ex(e105);
      }
      sub104.commit();
    }
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.FilterAcceptStatement((java.lang.Boolean)sub100.isSet("notted", env[9]), (org.fuwjin.chessur.expression.Filter)InFilter(sub100, env));
      } catch(final Exception e106) {
        throw sub100.ex(e106);
      }
    } catch(final GrinParserException e107) {
      try {
        try {
          env[8] /*stmt*/= new org.fuwjin.chessur.expression.ValueAcceptStatement((java.lang.Boolean)sub100.isSet("notted", env[9]), (org.fuwjin.chessur.expression.Expression)Value(sub100, env));
        } catch(final Exception e108) {
          throw sub100.ex(e108);
        }
      } catch(final GrinParserException e109) {
        sub100.abort(String.valueOf("accept keyword requires a value or in keyword"), e109);
      }
    }
    sub100.commit();
    return sub100.isSet("stmt", env[8]);
  }

  static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub110 = input.sub();
    sub110.accept("publish");
    Sep(sub110, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.PublishStatement((org.fuwjin.chessur.expression.Expression)Value(sub110, env));
      } catch(final Exception e111) {
        throw sub110.ex(e111);
      }
    } catch(final GrinParserException e112) {
      sub110.abort(String.valueOf("publish keyword requires a value"), e112);
    }
    sub110.commit();
    return sub110.isSet("stmt", env[8]);
  }

  static Object LogStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub113 = input.sub();
    sub113.accept("log");
    Sep(sub113, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.LogStatement((org.fuwjin.chessur.expression.Expression)Value(sub113, env));
      } catch(final Exception e114) {
        throw sub113.ex(e114);
      }
    } catch(final GrinParserException e115) {
      sub113.abort(String.valueOf("log keyword requires a value"), e115);
    }
    sub113.commit();
    return sub113.isSet("stmt", env[8]);
  }

  static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub116 = input.sub();
    sub116.accept("abort");
    Sep(sub116, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.AbortStatement((org.fuwjin.chessur.expression.Expression)Value(sub116, env));
      } catch(final Exception e117) {
        throw sub116.ex(e117);
      }
    } catch(final GrinParserException e118) {
      sub116.abort(String.valueOf("abort keyword requires a value"), e118);
    }
    sub116.commit();
    return sub116.isSet("stmt", env[8]);
  }

  static Object Block(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub119 = input.sub();
    sub119.accept("{");
    S(sub119, env);
    try {
      env[10] /*block*/= new org.fuwjin.chessur.expression.Block();
    } catch(final Exception e120) {
      throw sub119.ex(e120);
    }
    try {
      try {
        ((org.fuwjin.chessur.expression.Block)sub119.isSet("block", env[10])).add((org.fuwjin.chessur.expression.Expression)Statement(sub119, env));
      } catch(final Exception e121) {
        throw sub119.ex(e121);
      }
      try {
        while(true) {
          try {
            ((org.fuwjin.chessur.expression.Block)sub119.isSet("block", env[10])).add((org.fuwjin.chessur.expression.Expression)Statement(sub119, env));
          } catch(final Exception e122) {
            throw sub119.ex(e122);
          }
        }
      } catch(final GrinParserException e123) {
        //continue
      }
    } catch(final GrinParserException e124) {
      //continue
    }
    try {
      sub119.accept("}");
    } catch(final GrinParserException e125) {
      sub119.abort(String.valueOf("block must end with a brace"), e125);
    }
    S(sub119, env);
    sub119.commit();
    return sub119.isSet("block", env[10]);
  }

  static Object Assignment(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub126 = input.sub();
    env[1] /*name*/= Name(sub126, env);
    sub126.accept("=");
    S(sub126, env);
    try {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.Assignment((java.lang.String)sub126.isSet("name", env[1]), (org.fuwjin.chessur.expression.Expression)Value(sub126, env));
      } catch(final Exception e127) {
        throw sub126.ex(e127);
      }
    } catch(final GrinParserException e128) {
      sub126.abort(String.valueOf("assignment requires a value"), e128);
    }
    sub126.commit();
    return sub126.isSet("stmt", env[8]);
  }

  static Object Invocation(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub129 = input.sub();
    env[1] /*name*/= AliasName(sub129, env);
    sub129.accept("(");
    S(sub129, env);
    try {
      env[11] /*inv*/= new org.fuwjin.chessur.expression.Invocation();
    } catch(final Exception e130) {
      throw sub129.ex(e130);
    }
    try {
      final StringCursor sub131 = sub129.sub();
      try {
        ((org.fuwjin.chessur.expression.Invocation)sub131.isSet("inv", env[11])).addParam((org.fuwjin.chessur.expression.Expression)Value(sub131, env));
      } catch(final Exception e132) {
        throw sub131.ex(e132);
      }
      try {
        final StringCursor sub133 = sub131.sub();
        sub133.accept(",");
        S(sub133, env);
        try {
          try {
            ((org.fuwjin.chessur.expression.Invocation)sub133.isSet("inv", env[11])).addParam((org.fuwjin.chessur.expression.Expression)Value(sub133, env));
          } catch(final Exception e134) {
            throw sub133.ex(e134);
          }
        } catch(final GrinParserException e135) {
          sub133.abort(String.valueOf("invocation parameter must be a value"), e135);
        }
        sub133.commit();
        try {
          while(true) {
            final StringCursor sub136 = sub131.sub();
            sub136.accept(",");
            S(sub136, env);
            try {
              try {
                ((org.fuwjin.chessur.expression.Invocation)sub136.isSet("inv", env[11])).addParam((org.fuwjin.chessur.expression.Expression)Value(sub136, env));
              } catch(final Exception e137) {
                throw sub136.ex(e137);
              }
            } catch(final GrinParserException e138) {
              sub136.abort(String.valueOf("invocation parameter must be a value"), e138);
            }
            sub136.commit();
          }
        } catch(final GrinParserException e139) {
          //continue
        }
      } catch(final GrinParserException e140) {
        //continue
      }
      sub131.commit();
    } catch(final GrinParserException e141) {
      //continue
    }
    try {
      sub129.accept(")");
    } catch(final GrinParserException e142) {
      sub129.abort(String.valueOf("invocation must end with a parenthesis"), e142);
    }
    S(sub129, env);
    try {
      env[5] /*signature*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub129.isSet("cat", env[0])).getSignature((java.lang.String)sub129.isSet("name", env[1]), (java.lang.Integer)((org.fuwjin.chessur.expression.Invocation)sub129.isSet("inv", env[11])).paramCount());
    } catch(final Exception e143) {
      throw sub129.ex(e143);
    }
    try {
      final StringCursor sub144 = sub129.sub();
      try {
        env[12] /*function*/= ((org.fuwjin.dinah.FunctionProvider)sub144.isSet("manager", env[2])).getFunction((org.fuwjin.dinah.SignatureConstraint)sub144.isSet("signature", env[5]));
      } catch(final Exception e145) {
        throw sub144.ex(e145);
      }
      try {
        ((org.fuwjin.chessur.expression.Invocation)sub144.isSet("inv", env[11])).setFunction((org.fuwjin.dinah.Function)sub144.isSet("function", env[12]));
      } catch(final Exception e146) {
        throw sub144.ex(e146);
      }
      sub144.commit();
    } catch(final GrinParserException e147) {
      sub129.abort(String.valueOf("Could not get function for ") + String.valueOf(sub129.isSet("signature", env[5])), e147);
    }
    sub129.commit();
    return sub129.isSet("inv", env[11]);
  }

  static Object Script(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub148 = input.sub();
    env[6] /*script*/= ScriptIdent(sub148, env);
    try {
      final StringCursor sub149 = sub148.sub();
      sub149.accept("<<");
      S(sub149, env);
      try {
        env[6] /*script*/= new org.fuwjin.chessur.expression.ScriptInput((org.fuwjin.chessur.expression.Expression)sub149.isSet("script", env[6]), (org.fuwjin.chessur.expression.Expression)Value(sub149, env));
      } catch(final Exception e150) {
        throw sub149.ex(e150);
      }
      sub149.commit();
    } catch(final GrinParserException e151) {
      //continue
    }
    try {
      final StringCursor sub152 = sub148.sub();
      sub152.accept(">>");
      S(sub152, env);
      try {
        env[6] /*script*/= new org.fuwjin.chessur.expression.ScriptPipe((org.fuwjin.chessur.expression.Expression)sub152.isSet("script", env[6]), (org.fuwjin.chessur.expression.Expression)ScriptIdent(sub152, env));
      } catch(final Exception e153) {
        throw sub152.ex(e153);
      }
      sub152.commit();
      try {
        while(true) {
          final StringCursor sub154 = sub148.sub();
          sub154.accept(">>");
          S(sub154, env);
          try {
            env[6] /*script*/= new org.fuwjin.chessur.expression.ScriptPipe((org.fuwjin.chessur.expression.Expression)sub154.isSet("script", env[6]), (org.fuwjin.chessur.expression.Expression)ScriptIdent(sub154, env));
          } catch(final Exception e155) {
            throw sub154.ex(e155);
          }
          sub154.commit();
        }
      } catch(final GrinParserException e156) {
        //continue
      }
    } catch(final GrinParserException e157) {
      //continue
    }
    try {
      final StringCursor sub158 = sub148.sub();
      sub158.accept(">>");
      S(sub158, env);
      try {
        env[6] /*script*/= new org.fuwjin.chessur.expression.ScriptOutput((org.fuwjin.chessur.expression.Expression)sub158.isSet("script", env[6]), (java.lang.String)Name(sub158, env));
      } catch(final Exception e159) {
        throw sub158.ex(e159);
      }
      sub158.commit();
    } catch(final GrinParserException e160) {
      //continue
    }
    sub148.commit();
    return sub148.isSet("script", env[6]);
  }

  static Object ScriptIdent(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub161 = input.sub();
    sub161.accept("<");
    try {
      env[13] /*id*/= Identifier(sub161, env);
    } catch(final GrinParserException e162) {
      sub161.abort(String.valueOf("script identifiers must be enclosed in angle brackets"), e162);
    }
    try {
      final StringCursor sub163 = sub161.sub();
      sub163.accept(":");
      try {
        final StringCursor sub164 = sub163.sub();
        env[1] /*name*/= Identifier(sub164, env);
        try {
          env[14] /*module*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub164.isSet("cat", env[0])).getModule((java.lang.String)sub164.isSet("id", env[13]));
        } catch(final Exception e165) {
          throw sub164.ex(e165);
        }
        try {
          env[6] /*script*/= ((org.fuwjin.chessur.Module)sub164.isSet("module", env[14])).get((java.lang.String)sub164.isSet("name", env[1]));
        } catch(final Exception e166) {
          throw sub164.ex(e166);
        }
        sub164.commit();
      } catch(final GrinParserException e167) {
        sub163.abort(String.valueOf("namespaced script ") + String.valueOf(sub163.isSet("id", env[13])) + String.valueOf(": could not be resolved"), e167);
      }
      sub163.commit();
    } catch(final GrinParserException e168) {
      final StringCursor sub169 = sub161.sub();
      try {
        env[6] /*script*/= ((org.fuwjin.chessur.Module)sub169.isSet("cat", env[0])).get((java.lang.String)sub169.isSet("id", env[13]));
      } catch(final Exception e170) {
        throw sub169.ex(e170);
      }
      sub169.commit();
    }
    try {
      sub161.accept(">");
    } catch(final GrinParserException e171) {
      sub161.abort(String.valueOf("script identifiers must be normal identifiers in angle brackets"), e171);
    }
    S(sub161, env);
    sub161.commit();
    return sub161.isSet("script", env[6]);
  }

  static Object Object(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub172 = input.sub();
    sub172.accept("(");
    S(sub172, env);
    env[15] /*type*/= AliasName(sub172, env);
    try {
      env[5] /*signature*/= ((org.fuwjin.dinah.FunctionProvider)sub172.isSet("manager", env[2])).forName((java.lang.String)String.valueOf(sub172.isSet("type", env[15])) + String.valueOf(".new"));
    } catch(final Exception e173) {
      throw sub172.ex(e173);
    }
    try {
      ((org.fuwjin.dinah.ConstraintBuilder)sub172.isSet("signature", env[5])).withArgCount((java.lang.Integer)0);
    } catch(final Exception e174) {
      throw sub172.ex(e174);
    }
    try {
      env[16] /*constructor*/= ((org.fuwjin.dinah.ConstraintBuilder)sub172.isSet("signature", env[5])).function();
    } catch(final Exception e175) {
      throw sub172.ex(e175);
    }
    try {
      env[17] /*object*/= new org.fuwjin.chessur.expression.ObjectTemplate((java.lang.String)sub172.isSet("type", env[15]), (org.fuwjin.dinah.Function)sub172.isSet("constructor", env[16]));
    } catch(final Exception e176) {
      throw sub172.ex(e176);
    }
    sub172.accept(")");
    S(sub172, env);
    sub172.accept("{");
    S(sub172, env);
    try {
      final StringCursor sub177 = sub172.sub();
      Field(sub177, env);
      try {
        final StringCursor sub178 = sub177.sub();
        sub178.accept(",");
        S(sub178, env);
        Field(sub178, env);
        sub178.commit();
        try {
          while(true) {
            final StringCursor sub179 = sub177.sub();
            sub179.accept(",");
            S(sub179, env);
            Field(sub179, env);
            sub179.commit();
          }
        } catch(final GrinParserException e180) {
          //continue
        }
      } catch(final GrinParserException e181) {
        //continue
      }
      sub177.commit();
    } catch(final GrinParserException e182) {
      //continue
    }
    sub172.accept("}");
    S(sub172, env);
    sub172.commit();
    return sub172.isSet("object", env[17]);
  }

  static Object Field(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub183 = input.sub();
    env[1] /*name*/= Name(sub183, env);
    try {
      env[5] /*signature*/= ((org.fuwjin.dinah.FunctionProvider)sub183.isSet("manager", env[2])).forName((java.lang.String)String.valueOf(sub183.isSet("type", env[15])) + String.valueOf(".") + String.valueOf(sub183.isSet("name", env[1])));
    } catch(final Exception e184) {
      throw sub183.ex(e184);
    }
    try {
      ((org.fuwjin.dinah.ConstraintBuilder)sub183.isSet("signature", env[5])).withArgCount((java.lang.Integer)2);
    } catch(final Exception e185) {
      throw sub183.ex(e185);
    }
    try {
      env[18] /*setter*/= ((org.fuwjin.dinah.ConstraintBuilder)sub183.isSet("signature", env[5])).function();
    } catch(final Exception e186) {
      throw sub183.ex(e186);
    }
    sub183.accept(":");
    S(sub183, env);
    try {
      ((org.fuwjin.chessur.expression.ObjectTemplate)sub183.isSet("object", env[17])).set((java.lang.String)sub183.isSet("name", env[1]), (org.fuwjin.dinah.Function)sub183.isSet("setter", env[18]), (org.fuwjin.chessur.expression.Expression)Value(sub183, env));
    } catch(final Exception e187) {
      throw sub183.ex(e187);
    }
    sub183.commit();
    return null;
  }

  static Object MatchValue(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub188 = input.sub();
    sub188.accept("match");
    Sep(sub188, env);
    sub188.commit();
    return ((org.fuwjin.chessur.expression.Declaration)sub188.isSet("script", env[6])).match();
  }

  static Object NextValue(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub189 = input.sub();
    sub189.accept("next");
    Sep(sub189, env);
    sub189.commit();
    return org.fuwjin.chessur.expression.Variable.NEXT;
  }

  static Object InFilter(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub190 = input.sub();
    sub190.accept("in");
    Sep(sub190, env);
    try {
      env[19] /*filter*/= new org.fuwjin.chessur.expression.Filter();
    } catch(final Exception e191) {
      throw sub190.ex(e191);
    }
    try {
      FilterRange(sub190, env);
    } catch(final GrinParserException e192) {
      sub190.abort(String.valueOf("in keyword requires at least one filter"), e192);
    }
    try {
      final StringCursor sub193 = sub190.sub();
      sub193.accept(",");
      S(sub193, env);
      try {
        FilterRange(sub193, env);
      } catch(final GrinParserException e194) {
        sub193.abort(String.valueOf("in keyword requires a filter after a comma"), e194);
      }
      sub193.commit();
      try {
        while(true) {
          final StringCursor sub195 = sub190.sub();
          sub195.accept(",");
          S(sub195, env);
          try {
            FilterRange(sub195, env);
          } catch(final GrinParserException e196) {
            sub195.abort(String.valueOf("in keyword requires a filter after a comma"), e196);
          }
          sub195.commit();
        }
      } catch(final GrinParserException e197) {
        //continue
      }
    } catch(final GrinParserException e198) {
      //continue
    }
    sub190.commit();
    return sub190.isSet("filter", env[19]);
  }

  static Object FilterRange(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub199 = input.sub();
    env[20] /*start*/= FilterChar(sub199, env);
    S(sub199, env);
    try {
      final StringCursor sub200 = sub199.sub();
      sub200.accept("-");
      S(sub200, env);
      try {
        ((org.fuwjin.chessur.expression.Filter)sub200.isSet("filter", env[19])).addRange((java.lang.Integer)sub200.isSet("start", env[20]), (java.lang.Integer)FilterChar(sub200, env));
      } catch(final Exception e201) {
        throw sub200.ex(e201);
      }
      S(sub200, env);
      sub200.commit();
    } catch(final GrinParserException e202) {
      final StringCursor sub203 = sub199.sub();
      try {
        ((org.fuwjin.chessur.expression.Filter)sub203.isSet("filter", env[19])).addChar((java.lang.Integer)sub203.isSet("start", env[20]));
      } catch(final Exception e204) {
        throw sub203.ex(e204);
      }
      sub203.commit();
    }
    sub199.commit();
    return null;
  }

  static Object FilterChar(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub205 = input.sub();
    try {
      final StringCursor sub206 = sub205.sub();
      env[21] /*ch*/= Escape(sub206, env);
      sub206.commit();
    } catch(final GrinParserException e207) {
      final StringCursor sub208 = sub205.sub();
      env[21] /*ch*/= sub208.next();
      sub208.acceptNot("\\");
      sub208.commit();
    }
    sub205.commit();
    return sub205.isSet("ch", env[21]);
  }

  static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub209 = input.sub();
    sub209.accept("'");
    try {
      env[22] /*lit*/= new org.fuwjin.chessur.expression.Literal();
    } catch(final Exception e210) {
      throw sub209.ex(e210);
    }
    try {
      try {
        final StringCursor sub211 = sub209.sub();
        env[21] /*ch*/= sub211.next();
        sub211.acceptNotIn("'\\","'\\");
        try {
          ((org.fuwjin.chessur.expression.Literal)sub211.isSet("lit", env[22])).append((java.lang.Integer)sub211.isSet("ch", env[21]));
        } catch(final Exception e212) {
          throw sub211.ex(e212);
        }
        sub211.commit();
      } catch(final GrinParserException e213) {
        final StringCursor sub214 = sub209.sub();
        try {
          ((org.fuwjin.chessur.expression.Literal)sub214.isSet("lit", env[22])).append((java.lang.Integer)Escape(sub214, env));
        } catch(final Exception e215) {
          throw sub214.ex(e215);
        }
        sub214.commit();
      }
      try {
        while(true) {
          try {
            final StringCursor sub216 = sub209.sub();
            env[21] /*ch*/= sub216.next();
            sub216.acceptNotIn("'\\","'\\");
            try {
              ((org.fuwjin.chessur.expression.Literal)sub216.isSet("lit", env[22])).append((java.lang.Integer)sub216.isSet("ch", env[21]));
            } catch(final Exception e217) {
              throw sub216.ex(e217);
            }
            sub216.commit();
          } catch(final GrinParserException e218) {
            final StringCursor sub219 = sub209.sub();
            try {
              ((org.fuwjin.chessur.expression.Literal)sub219.isSet("lit", env[22])).append((java.lang.Integer)Escape(sub219, env));
            } catch(final Exception e220) {
              throw sub219.ex(e220);
            }
            sub219.commit();
          }
        }
      } catch(final GrinParserException e221) {
        //continue
      }
    } catch(final GrinParserException e222) {
      //continue
    }
    try {
      sub209.accept("'");
    } catch(final GrinParserException e223) {
      sub209.abort(String.valueOf("static literals must end with a quote"), e223);
    }
    S(sub209, env);
    sub209.commit();
    return sub209.isSet("lit", env[22]);
  }

  static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub224 = input.sub();
    sub224.accept("\"");
    try {
      env[22] /*lit*/= new org.fuwjin.chessur.expression.CompositeLiteral();
    } catch(final Exception e225) {
      throw sub224.ex(e225);
    }
    try {
      try {
        final StringCursor sub226 = sub224.sub();
        sub226.accept("'");
        S(sub226, env);
        try {
          ((org.fuwjin.chessur.expression.CompositeLiteral)sub226.isSet("lit", env[22])).append((org.fuwjin.chessur.expression.Expression)Value(sub226, env));
        } catch(final Exception e227) {
          throw sub226.ex(e227);
        }
        sub226.accept("'");
        sub226.commit();
      } catch(final GrinParserException e228) {
        try {
          final StringCursor sub229 = sub224.sub();
          try {
            ((org.fuwjin.chessur.expression.CompositeLiteral)sub229.isSet("lit", env[22])).appendChar((java.lang.Integer)Escape(sub229, env));
          } catch(final Exception e230) {
            throw sub229.ex(e230);
          }
          sub229.commit();
        } catch(final GrinParserException e231) {
          final StringCursor sub232 = sub224.sub();
          env[21] /*ch*/= sub232.next();
          sub232.acceptNotIn("\"\\","\"\\");
          try {
            ((org.fuwjin.chessur.expression.CompositeLiteral)sub232.isSet("lit", env[22])).appendChar((java.lang.Integer)sub232.isSet("ch", env[21]));
          } catch(final Exception e233) {
            throw sub232.ex(e233);
          }
          sub232.commit();
        }
      }
      try {
        while(true) {
          try {
            final StringCursor sub234 = sub224.sub();
            sub234.accept("'");
            S(sub234, env);
            try {
              ((org.fuwjin.chessur.expression.CompositeLiteral)sub234.isSet("lit", env[22])).append((org.fuwjin.chessur.expression.Expression)Value(sub234, env));
            } catch(final Exception e235) {
              throw sub234.ex(e235);
            }
            sub234.accept("'");
            sub234.commit();
          } catch(final GrinParserException e236) {
            try {
              final StringCursor sub237 = sub224.sub();
              try {
                ((org.fuwjin.chessur.expression.CompositeLiteral)sub237.isSet("lit", env[22])).appendChar((java.lang.Integer)Escape(sub237, env));
              } catch(final Exception e238) {
                throw sub237.ex(e238);
              }
              sub237.commit();
            } catch(final GrinParserException e239) {
              final StringCursor sub240 = sub224.sub();
              env[21] /*ch*/= sub240.next();
              sub240.acceptNotIn("\"\\","\"\\");
              try {
                ((org.fuwjin.chessur.expression.CompositeLiteral)sub240.isSet("lit", env[22])).appendChar((java.lang.Integer)sub240.isSet("ch", env[21]));
              } catch(final Exception e241) {
                throw sub240.ex(e241);
              }
              sub240.commit();
            }
          }
        }
      } catch(final GrinParserException e242) {
        //continue
      }
    } catch(final GrinParserException e243) {
      //continue
    }
    try {
      sub224.accept("\"");
    } catch(final GrinParserException e244) {
      sub224.abort(String.valueOf("dynamic literals must end with a double quote"), e244);
    }
    S(sub224, env);
    sub224.commit();
    return sub224.isSet("lit", env[22]);
  }

  static Object Escape(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub245 = input.sub();
    sub245.accept("\\");
    try {
      final StringCursor sub246 = sub245.sub();
      sub246.accept("n");
      try {
        env[21] /*ch*/= org.fuwjin.chessur.expression.Literal.NEW_LINE;
      } catch(final Exception e247) {
        throw sub246.ex(e247);
      }
      sub246.commit();
    } catch(final GrinParserException e248) {
      try {
        final StringCursor sub249 = sub245.sub();
        sub249.accept("t");
        try {
          env[21] /*ch*/= org.fuwjin.chessur.expression.Literal.TAB;
        } catch(final Exception e250) {
          throw sub249.ex(e250);
        }
        sub249.commit();
      } catch(final GrinParserException e251) {
        try {
          final StringCursor sub252 = sub245.sub();
          sub252.accept("r");
          try {
            env[21] /*ch*/= org.fuwjin.chessur.expression.Literal.RETURN;
          } catch(final Exception e253) {
            throw sub252.ex(e253);
          }
          sub252.commit();
        } catch(final GrinParserException e254) {
          try {
            final StringCursor sub255 = sub245.sub();
            sub255.accept("x");
            try {
              env[21] /*ch*/= org.fuwjin.chessur.expression.Literal.parseHex((java.lang.String)HexDigits(sub255, env));
            } catch(final Exception e256) {
              throw sub255.ex(e256);
            }
            sub255.commit();
          } catch(final GrinParserException e257) {
            final StringCursor sub258 = sub245.sub();
            env[21] /*ch*/= sub258.next();
            sub258.accept();
            sub258.commit();
          }
        }
      }
    }
    sub245.commit();
    return sub245.isSet("ch", env[21]);
  }

  static Object HexDigits(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub259 = input.sub();
    HexDigit(sub259, env);
    HexDigit(sub259, env);
    HexDigit(sub259, env);
    HexDigit(sub259, env);
    sub259.commit();
    return sub259.match();
  }

  static Object HexDigit(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub260 = input.sub();
    sub260.acceptIn("0-9A-Fa-f","0123456789ABCDEFabcdef");
    sub260.commit();
    return null;
  }

  static Object Number(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub261 = input.sub();
    try {
      sub261.accept("-");
    } catch(final GrinParserException e262) {
      //continue
    }
    try {
      final StringCursor sub263 = sub261.sub();
      sub263.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub263.acceptIn("0-9","0123456789");
        }
      } catch(final GrinParserException e264) {
        //continue
      }
      try {
        final StringCursor sub265 = sub263.sub();
        sub265.accept(".");
        try {
          sub265.acceptIn("0-9","0123456789");
          try {
            while(true) {
              sub265.acceptIn("0-9","0123456789");
            }
          } catch(final GrinParserException e266) {
            //continue
          }
        } catch(final GrinParserException e267) {
          //continue
        }
        sub265.commit();
      } catch(final GrinParserException e268) {
        //continue
      }
      sub263.commit();
    } catch(final GrinParserException e269) {
      final StringCursor sub270 = sub261.sub();
      sub270.accept(".");
      sub270.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub270.acceptIn("0-9","0123456789");
        }
      } catch(final GrinParserException e271) {
        //continue
      }
      sub270.commit();
    }
    try {
      final StringCursor sub272 = sub261.sub();
      sub272.acceptIn("Ee","Ee");
      try {
        sub272.accept("-");
      } catch(final GrinParserException e273) {
        //continue
      }
      sub272.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub272.acceptIn("0-9","0123456789");
        }
      } catch(final GrinParserException e274) {
        //continue
      }
      sub272.commit();
    } catch(final GrinParserException e275) {
      //continue
    }
    try {
      env[23] /*num*/= new org.fuwjin.chessur.expression.Number((java.lang.String)sub261.match());
    } catch(final Exception e276) {
      throw sub261.ex(e276);
    }
    Sep(sub261, env);
    sub261.commit();
    return sub261.isSet("num", env[23]);
  }

  static Object Path(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub277 = input.sub();
    final StringCursor sub278 = sub277.sub();
    try {
      sub278.accept("/");
    } catch(final GrinParserException e279) {
      //continue
    }
    QualifiedIdentifier(sub278, env);
    sub278.commit();
    try {
      while(true) {
        final StringCursor sub280 = sub277.sub();
        try {
          sub280.accept("/");
        } catch(final GrinParserException e281) {
          //continue
        }
        QualifiedIdentifier(sub280, env);
        sub280.commit();
      }
    } catch(final GrinParserException e282) {
      //continue
    }
    try {
      sub277.accept("/");
    } catch(final GrinParserException e283) {
      //continue
    }
    sub277.commit();
    return sub277.match();
  }

  static Object PathName(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub284 = input.sub();
    env[3] /*path*/= Path(sub284, env);
    S(sub284, env);
    sub284.commit();
    return sub284.isSet("path", env[3]);
  }

  static Object AliasName(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub285 = input.sub();
    env[24] /*prefix*/= AnnotatedIdentifier(sub285, env);
    try {
      env[25] /*alias*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub285.isSet("cat", env[0])).alias((java.lang.String)sub285.isSet("prefix", env[24]));
    } catch(final Exception e286) {
      throw sub285.ex(e286);
    }
    try {
      final StringCursor sub287 = sub285.sub();
      sub287.accept(".");
      env[1] /*name*/= String.valueOf(sub287.isSet("alias", env[25])) + String.valueOf(".") + String.valueOf(QualifiedName(sub287, env));
      sub287.commit();
    } catch(final GrinParserException e288) {
      final StringCursor sub289 = sub285.sub();
      env[1] /*name*/= sub289.isSet("alias", env[25]);
      sub289.commit();
    }
    sub285.commit();
    return sub285.isSet("name", env[1]);
  }

  static Object Name(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub290 = input.sub();
    env[13] /*id*/= Identifier(sub290, env);
    S(sub290, env);
    sub290.commit();
    return sub290.isSet("id", env[13]);
  }

  static Object QualifiedName(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub291 = input.sub();
    env[13] /*id*/= QualifiedIdentifier(sub291, env);
    S(sub291, env);
    sub291.commit();
    return sub291.isSet("id", env[13]);
  }

  static Object QualifiedIdentifier(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub292 = input.sub();
    AnnotatedIdentifier(sub292, env);
    try {
      final StringCursor sub293 = sub292.sub();
      sub293.accept(".");
      AnnotatedIdentifier(sub293, env);
      sub293.commit();
      try {
        while(true) {
          final StringCursor sub294 = sub292.sub();
          sub294.accept(".");
          AnnotatedIdentifier(sub294, env);
          sub294.commit();
        }
      } catch(final GrinParserException e295) {
        //continue
      }
    } catch(final GrinParserException e296) {
      //continue
    }
    sub292.commit();
    return sub292.match();
  }

  static Object AnnotatedIdentifier(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub297 = input.sub();
    Identifier(sub297, env);
    try {
      final StringCursor sub298 = sub297.sub();
      sub298.accept("[");
      try {
        Identifier(sub298, env);
      } catch(final GrinParserException e299) {
        //continue
      }
      sub298.accept("]");
      sub298.commit();
      try {
        while(true) {
          final StringCursor sub300 = sub297.sub();
          sub300.accept("[");
          try {
            Identifier(sub300, env);
          } catch(final GrinParserException e301) {
            //continue
          }
          sub300.accept("]");
          sub300.commit();
        }
      } catch(final GrinParserException e302) {
        //continue
      }
    } catch(final GrinParserException e303) {
      //continue
    }
    sub297.commit();
    return sub297.match();
  }

  static Object Identifier(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub304 = input.sub();
    IdentifierChar(sub304, env);
    try {
      while(true) {
        IdentifierChar(sub304, env);
      }
    } catch(final GrinParserException e305) {
      //continue
    }
    sub304.commit();
    return sub304.match();
  }

  static Object IdentifierChar(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub306 = input.sub();
    final StringCursor sub307 = sub306.sub();
    if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub307.next()) == Boolean.FALSE) {
      throw sub307.ex("check failed");
    }
    sub306.accept();
    sub306.commit();
    return null;
  }

  static Object Sep(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub308 = input.sub();
    final StringCursor sub309 = sub308.sub();
    boolean b310 = true;
    try {
      if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub309.next()) == Boolean.FALSE) {
        b310 = false;
      }
    } catch(final GrinParserException e311) {
      b310 = false;
    }
    if(b310){
      throw sub308.ex("unexpected value");
    }
    S(sub308, env);
    sub308.commit();
    return null;
  }

  static Object S(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub312 = input.sub();
    try {
      Space(sub312, env);
    } catch(final GrinParserException e313) {
      //continue
    }
    sub312.commit();
    return null;
  }

  static Object Space(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub314 = input.sub();
    try {
      sub314.acceptIn("\t-\n\r ","\t\n\r ");
    } catch(final GrinParserException e315) {
      Comment(sub314, env);
    }
    try {
      while(true) {
        try {
          sub314.acceptIn("\t-\n\r ","\t\n\r ");
        } catch(final GrinParserException e316) {
          Comment(sub314, env);
        }
      }
    } catch(final GrinParserException e317) {
      //continue
    }
    sub314.commit();
    return null;
  }

  static Object Comment(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub318 = input.sub();
    sub318.accept("#");
    try {
      sub318.acceptNotIn("\n\r","\n\r");
      try {
        while(true) {
          sub318.acceptNotIn("\n\r","\n\r");
        }
      } catch(final GrinParserException e319) {
        //continue
      }
    } catch(final GrinParserException e320) {
      //continue
    }
    try {
      sub318.accept("\r");
    } catch(final GrinParserException e321) {
      //continue
    }
    try {
      sub318.accept("\n");
    } catch(final GrinParserException e322) {
      EndOfFile(sub318, env);
    }
    sub318.commit();
    return null;
  }

  static Object EndOfFile(final StringCursor input, final Object... parentEnv) throws GrinParserException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub323 = input.sub();
    final StringCursor sub324 = sub323.sub();
    boolean b325 = true;
    try {
      if((Object)sub324.next() == Boolean.FALSE) {
        b325 = false;
      }
    } catch(final GrinParserException e326) {
      b325 = false;
    }
    if(b325){
      throw sub323.ex("unexpected value");
    }
    sub323.commit();
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
