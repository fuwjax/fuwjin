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
// @formatter:off
package org.fuwjin.chessur.generated;

import java.io.IOException;
import java.util.Map;

public class ChessurInterpreter {
  public static class ChessurException extends Exception {
    private static final long serialVersionUID = 1; 
    ChessurException(final String message) {
      super(message);
    }
    
    ChessurException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }

  private static class StringCursor {
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
      appender = new StringBuilder();
    }
    
    public int accept() throws ChessurException {
      checkBounds(pos);
      return advance();
    }
    
    public int accept(final String expected) throws ChessurException {
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
    
    public int acceptIn(final String name, final String set) throws ChessurException {
      checkBounds(pos);
      if(set.indexOf(seq.charAt(pos)) < 0) {
        throw ex("Did not match filter: "+name);
      }
      return advance();
    }
    
    public int acceptNot(final String expected) throws ChessurException {
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
    
    public int acceptNotIn(final String name, final String set) throws ChessurException {
      checkBounds(pos);
      if(set.indexOf(seq.charAt(pos)) >= 0) {
        throw ex("Unexpected match: "+name);
      }
      return advance();
    }
    
    public void commit() {
      parent.pos = pos;
      parent.line = line;
      parent.column = column;
      try {
        parent.appender.append(appender.toString());
      } catch(final IOException e) {
        throw new RuntimeException("IOException never thrown by StringBuilder", e);
      }
    }
    
    public ChessurException ex(final String message) {
      if(pos == 0) {
        return new ChessurException(message+ ": [" + line + "," + column + "] SOF -> [1,0] SOF");
      }
      if(pos > seq.length()) {
        return new ChessurException(message+ ": [" + line + "," + column + "] EOF -> [1,0] SOF");
      }
      return new ChessurException(message+ ": [" + line + "," + column + "] '"+ seq.charAt(pos - 1)+"' -> [1,0] SOF");
    }
    
    public ChessurException ex(final Throwable cause) {
      return new ChessurException("[" + line + "," + column + "]", cause);
    }
    
    public Object isSet(final String name, final Object value) throws ChessurException {
      if(UNSET.equals(value)) {
        throw ex("variable "+name+" is unset");
      }
      return value;
    }
    
    public String match() {
      return seq.subSequence(start, pos).toString();
    }
    
    public int next() throws ChessurException {
      checkBounds(pos);
      return seq.charAt(pos);
    }
    
    public StringCursor sub() {
      return new StringCursor(pos, line, column, seq, this);
    }
    
    protected void checkBounds(final int p) throws ChessurException {
      if(p >= seq.length()) {
        throw ex("unexpected EOF");
      }
    }
    
    private int advance() {
      final char ch = seq.charAt(pos++);
       if(ch == '\n') {
         line++;
         column = 1;
       } else {
         column++;
       }
       return ch;
    }
  }
  
  private static final Object UNSET = new Object() {
    @Override
   public String toString() {
      return "UNSET";
    }
  };

  public static Object interpret(final CharSequence in, final Appendable out, final Map<String, ?> environment) throws ChessurException {
    final StringCursor input = new StringCursor(in, out);
    final Object[] env = new Object[27];
    env[0] = environment.containsKey("path") ? environment.get("path") : UNSET;
    env[1] = environment.containsKey("cat") ? environment.get("cat") : UNSET;
    env[2] = environment.containsKey("qname") ? environment.get("qname") : UNSET;
    env[3] = environment.containsKey("signature") ? environment.get("signature") : UNSET;
    env[4] = environment.containsKey("name") ? environment.get("name") : UNSET;
    env[5] = environment.containsKey("script") ? environment.get("script") : UNSET;
    env[6] = environment.containsKey("manager") ? environment.get("manager") : UNSET;
    env[7] = environment.containsKey("id") ? environment.get("id") : UNSET;
    env[8] = environment.containsKey("stmt") ? environment.get("stmt") : UNSET;
    env[9] = environment.containsKey("val") ? environment.get("val") : UNSET;
    env[10] = environment.containsKey("lit") ? environment.get("lit") : UNSET;
    env[11] = environment.containsKey("ch") ? environment.get("ch") : UNSET;
    env[12] = environment.containsKey("notted") ? environment.get("notted") : UNSET;
    env[13] = environment.containsKey("inv") ? environment.get("inv") : UNSET;
    env[14] = environment.containsKey("function") ? environment.get("function") : UNSET;
    env[15] = environment.containsKey("postage") ? environment.get("postage") : UNSET;
    env[16] = environment.containsKey("num") ? environment.get("num") : UNSET;
    env[17] = environment.containsKey("type") ? environment.get("type") : UNSET;
    env[18] = environment.containsKey("constructor") ? environment.get("constructor") : UNSET;
    env[19] = environment.containsKey("object") ? environment.get("object") : UNSET;
    env[20] = environment.containsKey("block") ? environment.get("block") : UNSET;
    env[21] = environment.containsKey("filter") ? environment.get("filter") : UNSET;
    env[22] = environment.containsKey("prefix") ? environment.get("prefix") : UNSET;
    env[23] = environment.containsKey("alias") ? environment.get("alias") : UNSET;
    env[24] = environment.containsKey("module") ? environment.get("module") : UNSET;
    env[25] = environment.containsKey("setter") ? environment.get("setter") : UNSET;
    env[26] = environment.containsKey("start") ? environment.get("start") : UNSET;
    return Catalog(input, env);
  }

  private static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub194 = input.sub();
    sub194.accept("abort");
    Sep(sub194, env);
    try {
      env[8] /*stmt*/= new org.fuwjin.chessur.expression.AbortStatement((org.fuwjin.chessur.expression.Expression)Value(sub194, env));
    } catch(final ChessurException e195) {
      throw new RuntimeException("abort keyword requires a value", e195);
    }
    sub194.commit();
    return sub194.isSet("stmt", env[8]);
  }

  private static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub124 = input.sub();
    sub124.accept("accept");
    Sep(sub124, env);
    try {
      final StringCursor sub125 = sub124.sub();
      sub125.accept("not");
      Sep(sub125, env);
      env[12] /*notted*/= java.lang.Boolean.TRUE;
      sub125.commit();
    } catch(final ChessurException e126) {
      final StringCursor sub127 = sub124.sub();
      env[12] /*notted*/= java.lang.Boolean.FALSE;
      sub127.commit();
    }
    try {
      env[8] /*stmt*/= new org.fuwjin.chessur.expression.FilterAcceptStatement((java.lang.Boolean)sub124.isSet("notted", env[12]), (org.fuwjin.chessur.expression.Filter)InFilter(sub124, env));
    } catch(final ChessurException e128) {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.ValueAcceptStatement((java.lang.Boolean)sub124.isSet("notted", env[12]), (org.fuwjin.chessur.expression.Expression)Value(sub124, env));
      } catch(final ChessurException e129) {
        throw new RuntimeException("accept keyword requires a value or in keyword", e129);
      }
    }
    sub124.commit();
    return sub124.isSet("stmt", env[8]);
  }

  private static Object AliasDeclaration(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub8 = input.sub();
    sub8.accept("alias");
    Sep(sub8, env);
    try {
      env[2] /*qname*/= QualifiedName(sub8, env);
    } catch(final ChessurException e9) {
      throw new RuntimeException("alias keyword requires a qualified name", e9);
    }
    try {
      final StringCursor sub10 = sub8.sub();
      sub10.accept("as");
      Sep(sub10, env);
      try {
        try {
          ((org.fuwjin.chessur.expression.CatalogImpl)sub10.isSet("cat", env[1])).alias((java.lang.String)sub10.isSet("qname", env[2]), (java.lang.String)Name(sub10, env));
        } catch(final Exception e11) {
          throw sub10.ex(e11);
        }
      } catch(final ChessurException e12) {
        throw new RuntimeException("alias-as keywords require a name", e12);
      }
      sub10.commit();
    } catch(final ChessurException e13) {
      try {
        final StringCursor sub14 = sub8.sub();
        sub14.accept("(");
        S(sub14, env);
        env[3] /*signature*/= new org.fuwjin.dinah.FunctionSignature((java.lang.String)sub14.isSet("qname", env[2]));
        try {
          final StringCursor sub15 = sub14.sub();
          try {
            ((org.fuwjin.dinah.FunctionSignature)sub15.isSet("signature", env[3])).addArg((java.lang.String)QualifiedName(sub15, env));
          } catch(final Exception e16) {
            throw sub15.ex(e16);
          }
          try {
            final StringCursor sub17 = sub15.sub();
            sub17.accept(",");
            S(sub17, env);
            try {
              ((org.fuwjin.dinah.FunctionSignature)sub17.isSet("signature", env[3])).addArg((java.lang.String)QualifiedName(sub17, env));
            } catch(final Exception e18) {
              throw sub17.ex(e18);
            }
            sub17.commit();
            try {
              while(true) {
                final StringCursor sub19 = sub15.sub();
                sub19.accept(",");
                S(sub19, env);
                try {
                  ((org.fuwjin.dinah.FunctionSignature)sub19.isSet("signature", env[3])).addArg((java.lang.String)QualifiedName(sub19, env));
                } catch(final Exception e20) {
                  throw sub19.ex(e20);
                }
                sub19.commit();
              }
            } catch(final ChessurException e21) {
              //continue
            }
          } catch(final ChessurException e22) {
            //continue
          }
          sub15.commit();
        } catch(final ChessurException e23) {
          //continue
        }
        sub14.accept(")");
        S(sub14, env);
        sub14.accept("as");
        Sep(sub14, env);
        try {
          try {
            ((org.fuwjin.chessur.expression.CatalogImpl)sub14.isSet("cat", env[1])).aliasSignature((org.fuwjin.dinah.FunctionSignature)sub14.isSet("signature", env[3]), (java.lang.String)Name(sub14, env));
          } catch(final Exception e24) {
            throw sub14.ex(e24);
          }
        } catch(final ChessurException e25) {
          throw new RuntimeException("alias-as keywords require a name", e25);
        }
        sub14.commit();
      } catch(final ChessurException e26) {
        throw new RuntimeException("alias keyword requires as keyword", e26);
      }
    }
    sub8.commit();
    return null;
  }

  private static Object AliasName(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub212 = input.sub();
    env[22] /*prefix*/= Identifier(sub212, env);
    env[23] /*alias*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub212.isSet("cat", env[1])).alias((java.lang.String)sub212.isSet("prefix", env[22]));
    try {
      final StringCursor sub213 = sub212.sub();
      sub213.accept(".");
      env[4] /*name*/= sub213.isSet("alias", env[23]) + "." + QualifiedName(sub213, env);
      sub213.commit();
    } catch(final ChessurException e214) {
      final StringCursor sub215 = sub212.sub();
      env[4] /*name*/= sub215.isSet("alias", env[23]);
      sub215.commit();
    }
    sub212.commit();
    return sub212.isSet("name", env[4]);
  }

  private static Object AnnotatedIdentifier(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub257 = input.sub();
    Identifier(sub257, env);
    try {
      final StringCursor sub258 = sub257.sub();
      sub258.accept("[");
      try {
        Identifier(sub258, env);
      } catch(final ChessurException e259) {
        //continue
      }
      sub258.accept("]");
      sub258.commit();
      try {
        while(true) {
          final StringCursor sub260 = sub257.sub();
          sub260.accept("[");
          try {
            Identifier(sub260, env);
          } catch(final ChessurException e261) {
            //continue
          }
          sub260.accept("]");
          sub260.commit();
        }
      } catch(final ChessurException e262) {
        //continue
      }
    } catch(final ChessurException e263) {
      //continue
    }
    sub257.commit();
    return null;
  }

  private static Object Assignment(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub202 = input.sub();
    env[4] /*name*/= Name(sub202, env);
    sub202.accept("=");
    S(sub202, env);
    try {
      env[8] /*stmt*/= new org.fuwjin.chessur.expression.Assignment((java.lang.String)sub202.isSet("name", env[4]), (org.fuwjin.chessur.expression.Expression)Value(sub202, env));
    } catch(final ChessurException e203) {
      throw new RuntimeException("assignment requires a value", e203);
    }
    sub202.commit();
    return sub202.isSet("stmt", env[8]);
  }

  private static Object Block(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub196 = input.sub();
    sub196.accept("{");
    S(sub196, env);
    env[20] /*block*/= new org.fuwjin.chessur.expression.Block();
    try {
      try {
        ((org.fuwjin.chessur.expression.Block)sub196.isSet("block", env[20])).add((org.fuwjin.chessur.expression.Expression)Statement(sub196, env));
      } catch(final Exception e197) {
        throw sub196.ex(e197);
      }
      try {
        while(true) {
          try {
            ((org.fuwjin.chessur.expression.Block)sub196.isSet("block", env[20])).add((org.fuwjin.chessur.expression.Expression)Statement(sub196, env));
          } catch(final Exception e198) {
            throw sub196.ex(e198);
          }
        }
      } catch(final ChessurException e199) {
        //continue
      }
    } catch(final ChessurException e200) {
      //continue
    }
    try {
      sub196.accept("}");
    } catch(final ChessurException e201) {
      throw new RuntimeException("block must end with a brace", e201);
    }
    S(sub196, env);
    sub196.commit();
    return sub196.isSet("block", env[20]);
  }

  private static Object Catalog(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub43 = input.sub();
    env[1] /*cat*/= new org.fuwjin.chessur.expression.CatalogImpl((java.lang.String)sub43.isSet("name", env[4]), (org.fuwjin.chessur.CatalogManager)sub43.isSet("manager", env[6]));
    S(sub43, env);
    try {
      try {
        LoadDeclaration(sub43, env);
      } catch(final ChessurException e44) {
        try {
          AliasDeclaration(sub43, env);
        } catch(final ChessurException e45) {
          try {
            ((org.fuwjin.chessur.expression.CatalogImpl)sub43.isSet("cat", env[1])).add((org.fuwjin.chessur.expression.Declaration)ScriptDeclaration(sub43, env));
          } catch(final Exception e46) {
            throw sub43.ex(e46);
          }
        }
      }
      try {
        while(true) {
          try {
            LoadDeclaration(sub43, env);
          } catch(final ChessurException e47) {
            try {
              AliasDeclaration(sub43, env);
            } catch(final ChessurException e48) {
              try {
                ((org.fuwjin.chessur.expression.CatalogImpl)sub43.isSet("cat", env[1])).add((org.fuwjin.chessur.expression.Declaration)ScriptDeclaration(sub43, env));
              } catch(final Exception e49) {
                throw sub43.ex(e49);
              }
            }
          }
        }
      } catch(final ChessurException e50) {
        //continue
      }
    } catch(final ChessurException e51) {
      //continue
    }
    EndOfFile(sub43, env);
    sub43.commit();
    return sub43.isSet("cat", env[1]);
  }

  private static Object Comment(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub270 = input.sub();
    sub270.accept("#");
    try {
      sub270.acceptNotIn("\n\r","\n\r");
      try {
        while(true) {
          sub270.acceptNotIn("\n\r","\n\r");
        }
      } catch(final ChessurException e271) {
        //continue
      }
    } catch(final ChessurException e272) {
      //continue
    }
    try {
      sub270.accept("\r");
    } catch(final ChessurException e273) {
      //continue
    }
    try {
      sub270.accept("\n");
    } catch(final ChessurException e274) {
      EndOfFile(sub270, env);
    }
    sub270.commit();
    return null;
  }

  private static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub188 = input.sub();
    sub188.accept("could");
    Sep(sub188, env);
    try {
      env[8] /*stmt*/= new org.fuwjin.chessur.expression.CouldStatement((org.fuwjin.chessur.expression.Expression)Statement(sub188, env));
    } catch(final ChessurException e189) {
      throw new RuntimeException("could keyword requires a statement", e189);
    }
    sub188.commit();
    return sub188.isSet("stmt", env[8]);
  }

  private static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub95 = input.sub();
    sub95.accept("\"");
    env[10] /*lit*/= new org.fuwjin.chessur.expression.CompositeLiteral();
    try {
      try {
        final StringCursor sub96 = sub95.sub();
        sub96.accept("'");
        S(sub96, env);
        try {
          ((org.fuwjin.chessur.expression.CompositeLiteral)sub96.isSet("lit", env[10])).append((org.fuwjin.chessur.expression.Expression)Value(sub96, env));
        } catch(final Exception e97) {
          throw sub96.ex(e97);
        }
        sub96.accept("'");
        sub96.commit();
      } catch(final ChessurException e98) {
        try {
          final StringCursor sub99 = sub95.sub();
          try {
            ((org.fuwjin.chessur.expression.CompositeLiteral)sub99.isSet("lit", env[10])).appendChar((java.lang.Integer)Escape(sub99, env));
          } catch(final Exception e100) {
            throw sub99.ex(e100);
          }
          sub99.commit();
        } catch(final ChessurException e101) {
          final StringCursor sub102 = sub95.sub();
          env[11] /*ch*/= sub102.acceptNotIn("\"\\","\"\\");
          try {
            ((org.fuwjin.chessur.expression.CompositeLiteral)sub102.isSet("lit", env[10])).appendChar((java.lang.Integer)sub102.isSet("ch", env[11]));
          } catch(final Exception e103) {
            throw sub102.ex(e103);
          }
          sub102.commit();
        }
      }
      try {
        while(true) {
          try {
            final StringCursor sub104 = sub95.sub();
            sub104.accept("'");
            S(sub104, env);
            try {
              ((org.fuwjin.chessur.expression.CompositeLiteral)sub104.isSet("lit", env[10])).append((org.fuwjin.chessur.expression.Expression)Value(sub104, env));
            } catch(final Exception e105) {
              throw sub104.ex(e105);
            }
            sub104.accept("'");
            sub104.commit();
          } catch(final ChessurException e106) {
            try {
              final StringCursor sub107 = sub95.sub();
              try {
                ((org.fuwjin.chessur.expression.CompositeLiteral)sub107.isSet("lit", env[10])).appendChar((java.lang.Integer)Escape(sub107, env));
              } catch(final Exception e108) {
                throw sub107.ex(e108);
              }
              sub107.commit();
            } catch(final ChessurException e109) {
              final StringCursor sub110 = sub95.sub();
              env[11] /*ch*/= sub110.acceptNotIn("\"\\","\"\\");
              try {
                ((org.fuwjin.chessur.expression.CompositeLiteral)sub110.isSet("lit", env[10])).appendChar((java.lang.Integer)sub110.isSet("ch", env[11]));
              } catch(final Exception e111) {
                throw sub110.ex(e111);
              }
              sub110.commit();
            }
          }
        }
      } catch(final ChessurException e112) {
        //continue
      }
    } catch(final ChessurException e113) {
      //continue
    }
    try {
      sub95.accept("\"");
    } catch(final ChessurException e114) {
      throw new RuntimeException("dynamic literals must end with a double quote", e114);
    }
    S(sub95, env);
    sub95.commit();
    return sub95.isSet("lit", env[10]);
  }

  private static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub178 = input.sub();
    sub178.accept("either");
    Sep(sub178, env);
    try {
      env[8] /*stmt*/= new org.fuwjin.chessur.expression.EitherOrStatement((org.fuwjin.chessur.expression.Expression)Statement(sub178, env));
    } catch(final ChessurException e179) {
      throw new RuntimeException("either keyword requires a statement", e179);
    }
    try {
      final StringCursor sub180 = sub178.sub();
      sub180.accept("or");
      Sep(sub180, env);
      try {
        try {
          ((org.fuwjin.chessur.expression.EitherOrStatement)sub180.isSet("stmt", env[8])).or((org.fuwjin.chessur.expression.Expression)Statement(sub180, env));
        } catch(final Exception e181) {
          throw sub180.ex(e181);
        }
      } catch(final ChessurException e182) {
        throw new RuntimeException("or keyword requires a statement", e182);
      }
      sub180.commit();
      try {
        while(true) {
          final StringCursor sub183 = sub178.sub();
          sub183.accept("or");
          Sep(sub183, env);
          try {
            try {
              ((org.fuwjin.chessur.expression.EitherOrStatement)sub183.isSet("stmt", env[8])).or((org.fuwjin.chessur.expression.Expression)Statement(sub183, env));
            } catch(final Exception e184) {
              throw sub183.ex(e184);
            }
          } catch(final ChessurException e185) {
            throw new RuntimeException("or keyword requires a statement", e185);
          }
          sub183.commit();
        }
      } catch(final ChessurException e186) {
        //continue
      }
    } catch(final ChessurException e187) {
      throw new RuntimeException("either keyword requires at least one or keyword", e187);
    }
    sub178.commit();
    return sub178.isSet("stmt", env[8]);
  }

  private static Object EndOfFile(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub40 = input.sub();
    final StringCursor sub41 = sub40.sub();
    boolean b = true;
    try {
      if((Object)sub41.next() == Boolean.FALSE) {
        b = false;
      }
    } catch(final ChessurException e42) {
      b = false;
    }
    if(b){
      throw sub40.ex("unexpected value");
    }
    sub40.commit();
    return null;
  }

  private static Object Escape(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub233 = input.sub();
    sub233.accept("\\");
    try {
      final StringCursor sub234 = sub233.sub();
      sub234.accept("n");
      env[11] /*ch*/= org.fuwjin.chessur.expression.Literal.NEW_LINE;
      sub234.commit();
    } catch(final ChessurException e235) {
      try {
        final StringCursor sub236 = sub233.sub();
        sub236.accept("t");
        env[11] /*ch*/= org.fuwjin.chessur.expression.Literal.TAB;
        sub236.commit();
      } catch(final ChessurException e237) {
        try {
          final StringCursor sub238 = sub233.sub();
          sub238.accept("r");
          env[11] /*ch*/= org.fuwjin.chessur.expression.Literal.RETURN;
          sub238.commit();
        } catch(final ChessurException e239) {
          try {
            final StringCursor sub240 = sub233.sub();
            sub240.accept("x");
            env[11] /*ch*/= org.fuwjin.chessur.expression.Literal.parseHex((java.lang.String)HexDigits(sub240, env));
            sub240.commit();
          } catch(final ChessurException e241) {
            final StringCursor sub242 = sub233.sub();
            env[11] /*ch*/= sub242.accept();
            sub242.commit();
          }
        }
      }
    }
    sub233.commit();
    return sub233.isSet("ch", env[11]);
  }

  private static Object Field(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub222 = input.sub();
    env[4] /*name*/= Name(sub222, env);
    env[25] /*setter*/= ((org.fuwjin.dinah.FunctionProvider)sub222.isSet("postage", env[15])).getFunction((org.fuwjin.dinah.FunctionSignature)new org.fuwjin.dinah.FunctionSignature((java.lang.String)sub222.isSet("type", env[17]) + "." + sub222.isSet("name", env[4])));
    sub222.accept(":");
    S(sub222, env);
    try {
      ((org.fuwjin.chessur.expression.ObjectTemplate)sub222.isSet("object", env[19])).set((org.fuwjin.dinah.Function)sub222.isSet("setter", env[25]), (org.fuwjin.chessur.expression.Expression)Value(sub222, env));
    } catch(final Exception e223) {
      throw sub222.ex(e223);
    }
    sub222.commit();
    return null;
  }

  private static Object FilterChar(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub230 = input.sub();
    try {
      final StringCursor sub231 = sub230.sub();
      env[11] /*ch*/= Escape(sub231, env);
      sub231.commit();
    } catch(final ChessurException e232) {
      env[11] /*ch*/= sub230.acceptNot("\\");
    }
    sub230.commit();
    return sub230.isSet("ch", env[11]);
  }

  private static Object FilterRange(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub224 = input.sub();
    env[26] /*start*/= FilterChar(sub224, env);
    S(sub224, env);
    try {
      final StringCursor sub225 = sub224.sub();
      sub225.accept("-");
      S(sub225, env);
      try {
        ((org.fuwjin.chessur.expression.Filter)sub225.isSet("filter", env[21])).addRange((java.lang.Integer)sub225.isSet("start", env[26]), (java.lang.Integer)FilterChar(sub225, env));
      } catch(final Exception e226) {
        throw sub225.ex(e226);
      }
      S(sub225, env);
      sub225.commit();
    } catch(final ChessurException e227) {
      final StringCursor sub228 = sub224.sub();
      try {
        ((org.fuwjin.chessur.expression.Filter)sub228.isSet("filter", env[21])).addChar((java.lang.Integer)sub228.isSet("start", env[26]));
      } catch(final Exception e229) {
        throw sub228.ex(e229);
      }
      sub228.commit();
    }
    sub224.commit();
    return null;
  }

  private static Object HexDigit(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub244 = input.sub();
    sub244.acceptIn("0-9A-Fa-f","0123456789ABCDEFabcdef");
    sub244.commit();
    return null;
  }

  private static Object HexDigits(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub243 = input.sub();
    HexDigit(sub243, env);
    HexDigit(sub243, env);
    HexDigit(sub243, env);
    HexDigit(sub243, env);
    sub243.commit();
    return sub243.match();
  }

  private static Object Identifier(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub58 = input.sub();
    IdentifierChar(sub58, env);
    try {
      while(true) {
        IdentifierChar(sub58, env);
      }
    } catch(final ChessurException e59) {
      //continue
    }
    sub58.commit();
    return sub58.match();
  }

  private static Object IdentifierChar(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub264 = input.sub();
    final StringCursor sub265 = sub264.sub();
    if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub265.next()) == Boolean.FALSE) {
      throw sub265.ex("check failed");
    }
    sub264.accept();
    sub264.commit();
    return null;
  }

  private static Object InFilter(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub204 = input.sub();
    sub204.accept("in");
    Sep(sub204, env);
    env[21] /*filter*/= new org.fuwjin.chessur.expression.Filter();
    try {
      FilterRange(sub204, env);
    } catch(final ChessurException e205) {
      throw new RuntimeException("in keyword requires at least one filter", e205);
    }
    try {
      final StringCursor sub206 = sub204.sub();
      sub206.accept(",");
      S(sub206, env);
      try {
        FilterRange(sub206, env);
      } catch(final ChessurException e207) {
        throw new RuntimeException("in keyword requires a filter after a comma", e207);
      }
      sub206.commit();
      try {
        while(true) {
          final StringCursor sub208 = sub204.sub();
          sub208.accept(",");
          S(sub208, env);
          try {
            FilterRange(sub208, env);
          } catch(final ChessurException e209) {
            throw new RuntimeException("in keyword requires a filter after a comma", e209);
          }
          sub208.commit();
        }
      } catch(final ChessurException e210) {
        //continue
      }
    } catch(final ChessurException e211) {
      //continue
    }
    sub204.commit();
    return sub204.isSet("filter", env[21]);
  }

  private static Object Invocation(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub130 = input.sub();
    env[4] /*name*/= AliasName(sub130, env);
    env[3] /*signature*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub130.isSet("cat", env[1])).getSignature((java.lang.String)sub130.isSet("name", env[4]));
    sub130.accept("(");
    S(sub130, env);
    env[13] /*inv*/= new org.fuwjin.chessur.expression.Invocation();
    try {
      final StringCursor sub131 = sub130.sub();
      try {
        ((org.fuwjin.chessur.expression.Invocation)sub131.isSet("inv", env[13])).addParam((org.fuwjin.chessur.expression.Expression)Value(sub131, env));
      } catch(final Exception e132) {
        throw sub131.ex(e132);
      }
      try {
        final StringCursor sub133 = sub131.sub();
        sub133.accept(",");
        S(sub133, env);
        try {
          try {
            ((org.fuwjin.chessur.expression.Invocation)sub133.isSet("inv", env[13])).addParam((org.fuwjin.chessur.expression.Expression)Value(sub133, env));
          } catch(final Exception e134) {
            throw sub133.ex(e134);
          }
        } catch(final ChessurException e135) {
          throw new RuntimeException("invocation parameter must be a value", e135);
        }
        sub133.commit();
        try {
          while(true) {
            final StringCursor sub136 = sub131.sub();
            sub136.accept(",");
            S(sub136, env);
            try {
              try {
                ((org.fuwjin.chessur.expression.Invocation)sub136.isSet("inv", env[13])).addParam((org.fuwjin.chessur.expression.Expression)Value(sub136, env));
              } catch(final Exception e137) {
                throw sub136.ex(e137);
              }
            } catch(final ChessurException e138) {
              throw new RuntimeException("invocation parameter must be a value", e138);
            }
            sub136.commit();
          }
        } catch(final ChessurException e139) {
          //continue
        }
      } catch(final ChessurException e140) {
        //continue
      }
      sub131.commit();
    } catch(final ChessurException e141) {
      //continue
    }
    try {
      sub130.accept(")");
    } catch(final ChessurException e142) {
      throw new RuntimeException("invocation must end with a parenthesis", e142);
    }
    S(sub130, env);
    try {
      final StringCursor sub143 = sub130.sub();
      try {
        ((org.fuwjin.dinah.FunctionSignature)sub143.isSet("signature", env[3])).setArgCount((java.lang.Integer)((org.fuwjin.chessur.expression.Invocation)sub143.isSet("inv", env[13])).paramCount());
      } catch(final Exception e144) {
        throw sub143.ex(e144);
      }
      env[14] /*function*/= ((org.fuwjin.dinah.FunctionProvider)sub143.isSet("postage", env[15])).getFunction((org.fuwjin.dinah.FunctionSignature)sub143.isSet("signature", env[3]));
      try {
        ((org.fuwjin.chessur.expression.Invocation)sub143.isSet("inv", env[13])).setFunction((org.fuwjin.dinah.Function)sub143.isSet("function", env[14]));
      } catch(final Exception e145) {
        throw sub143.ex(e145);
      }
      sub143.commit();
    } catch(final ChessurException e146) {
      throw new RuntimeException("Could not get function for " + sub130.isSet("signature", env[3]), e146);
    }
    sub130.commit();
    return sub130.isSet("inv", env[13]);
  }

  private static Object IsStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub171 = input.sub();
    sub171.accept("is");
    Sep(sub171, env);
    try {
      final StringCursor sub172 = sub171.sub();
      sub172.accept("not");
      Sep(sub172, env);
      env[12] /*notted*/= java.lang.Boolean.TRUE;
      sub172.commit();
    } catch(final ChessurException e173) {
      final StringCursor sub174 = sub171.sub();
      env[12] /*notted*/= java.lang.Boolean.FALSE;
      sub174.commit();
    }
    try {
      env[8] /*stmt*/= new org.fuwjin.chessur.expression.IsStatement((java.lang.Boolean)sub171.isSet("notted", env[12]), (org.fuwjin.chessur.expression.Expression)new org.fuwjin.chessur.expression.FilterAcceptStatement((java.lang.Boolean)java.lang.Boolean.FALSE, (org.fuwjin.chessur.expression.Filter)InFilter(sub171, env)));
    } catch(final ChessurException e175) {
      try {
        env[8] /*stmt*/= new org.fuwjin.chessur.expression.IsStatement((java.lang.Boolean)sub171.isSet("notted", env[12]), (org.fuwjin.chessur.expression.Expression)new org.fuwjin.chessur.expression.ValueAcceptStatement((java.lang.Boolean)java.lang.Boolean.FALSE, (org.fuwjin.chessur.expression.Expression)StaticLiteral(sub171, env)));
      } catch(final ChessurException e176) {
        try {
          env[8] /*stmt*/= new org.fuwjin.chessur.expression.IsStatement((java.lang.Boolean)sub171.isSet("notted", env[12]), (org.fuwjin.chessur.expression.Expression)Value(sub171, env));
        } catch(final ChessurException e177) {
          throw new RuntimeException("is keyword requires value or in keyword", e177);
        }
      }
    }
    sub171.commit();
    return sub171.isSet("stmt", env[8]);
  }

  private static Object LoadDeclaration(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub3 = input.sub();
    sub3.accept("load");
    Sep(sub3, env);
    try {
      env[0] /*path*/= PathName(sub3, env);
    } catch(final ChessurException e4) {
      throw new RuntimeException("load keyword requires a file path", e4);
    }
    try {
      sub3.accept("as");
    } catch(final ChessurException e5) {
      throw new RuntimeException("load keyword requires as keyword", e5);
    }
    Sep(sub3, env);
    try {
      try {
        ((org.fuwjin.chessur.expression.CatalogImpl)sub3.isSet("cat", env[1])).load((java.lang.String)sub3.isSet("path", env[0]), (java.lang.String)Name(sub3, env));
      } catch(final Exception e6) {
        throw sub3.ex(e6);
      }
    } catch(final ChessurException e7) {
      throw new RuntimeException("load-as keywords require a name", e7);
    }
    sub3.commit();
    return null;
  }

  private static Object MatchValue(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub169 = input.sub();
    sub169.accept("match");
    Sep(sub169, env);
    sub169.commit();
    return org.fuwjin.chessur.expression.Variable.MATCH;
  }

  private static Object Name(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub56 = input.sub();
    env[7] /*id*/= Identifier(sub56, env);
    S(sub56, env);
    sub56.commit();
    return sub56.isSet("id", env[7]);
  }

  private static Object NextValue(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub170 = input.sub();
    sub170.accept("next");
    Sep(sub170, env);
    sub170.commit();
    return org.fuwjin.chessur.expression.Variable.NEXT;
  }

  private static Object Number(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub147 = input.sub();
    try {
      sub147.accept("-");
    } catch(final ChessurException e148) {
      //continue
    }
    try {
      final StringCursor sub149 = sub147.sub();
      sub149.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub149.acceptIn("0-9","0123456789");
        }
      } catch(final ChessurException e150) {
        //continue
      }
      try {
        final StringCursor sub151 = sub149.sub();
        sub151.accept(".");
        try {
          sub151.acceptIn("0-9","0123456789");
          try {
            while(true) {
              sub151.acceptIn("0-9","0123456789");
            }
          } catch(final ChessurException e152) {
            //continue
          }
        } catch(final ChessurException e153) {
          //continue
        }
        sub151.commit();
      } catch(final ChessurException e154) {
        //continue
      }
      sub149.commit();
    } catch(final ChessurException e155) {
      final StringCursor sub156 = sub147.sub();
      sub156.accept(".");
      sub156.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub156.acceptIn("0-9","0123456789");
        }
      } catch(final ChessurException e157) {
        //continue
      }
      sub156.commit();
    }
    try {
      final StringCursor sub158 = sub147.sub();
      sub158.acceptIn("Ee","Ee");
      try {
        sub158.accept("-");
      } catch(final ChessurException e159) {
        //continue
      }
      sub158.acceptIn("0-9","0123456789");
      try {
        while(true) {
          sub158.acceptIn("0-9","0123456789");
        }
      } catch(final ChessurException e160) {
        //continue
      }
      sub158.commit();
    } catch(final ChessurException e161) {
      //continue
    }
    env[16] /*num*/= new org.fuwjin.chessur.expression.Number((java.lang.String)sub147.match());
    Sep(sub147, env);
    sub147.commit();
    return sub147.isSet("num", env[16]);
  }

  private static Object Object(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub162 = input.sub();
    sub162.accept("(");
    S(sub162, env);
    env[17] /*type*/= AliasName(sub162, env);
    env[18] /*constructor*/= ((org.fuwjin.dinah.FunctionProvider)sub162.isSet("postage", env[15])).getFunction((org.fuwjin.dinah.FunctionSignature)new org.fuwjin.dinah.FunctionSignature((java.lang.String)sub162.isSet("type", env[17]) + ".new"));
    env[19] /*object*/= new org.fuwjin.chessur.expression.ObjectTemplate((org.fuwjin.dinah.Function)sub162.isSet("constructor", env[18]));
    sub162.accept(")");
    S(sub162, env);
    sub162.accept("{");
    S(sub162, env);
    try {
      final StringCursor sub163 = sub162.sub();
      Field(sub163, env);
      try {
        final StringCursor sub164 = sub163.sub();
        sub164.accept(",");
        S(sub164, env);
        Field(sub164, env);
        sub164.commit();
        try {
          while(true) {
            final StringCursor sub165 = sub163.sub();
            sub165.accept(",");
            S(sub165, env);
            Field(sub165, env);
            sub165.commit();
          }
        } catch(final ChessurException e166) {
          //continue
        }
      } catch(final ChessurException e167) {
        //continue
      }
      sub163.commit();
    } catch(final ChessurException e168) {
      //continue
    }
    sub162.accept("}");
    S(sub162, env);
    sub162.commit();
    return sub162.isSet("object", env[19]);
  }

  private static Object Path(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub250 = input.sub();
    final StringCursor sub251 = sub250.sub();
    try {
      sub251.accept("/");
    } catch(final ChessurException e252) {
      //continue
    }
    QualifiedIdentifier(sub251, env);
    sub251.commit();
    try {
      while(true) {
        final StringCursor sub253 = sub250.sub();
        try {
          sub253.accept("/");
        } catch(final ChessurException e254) {
          //continue
        }
        QualifiedIdentifier(sub253, env);
        sub253.commit();
      }
    } catch(final ChessurException e255) {
      //continue
    }
    try {
      sub250.accept("/");
    } catch(final ChessurException e256) {
      //continue
    }
    sub250.commit();
    return sub250.match();
  }

  private static Object PathName(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub55 = input.sub();
    env[0] /*path*/= Path(sub55, env);
    S(sub55, env);
    sub55.commit();
    return sub55.isSet("path", env[0]);
  }

  private static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub192 = input.sub();
    sub192.accept("publish");
    Sep(sub192, env);
    try {
      env[8] /*stmt*/= new org.fuwjin.chessur.expression.PublishStatement((org.fuwjin.chessur.expression.Expression)Value(sub192, env));
    } catch(final ChessurException e193) {
      throw new RuntimeException("publish keyword requires a value", e193);
    }
    sub192.commit();
    return sub192.isSet("stmt", env[8]);
  }

  private static Object QualifiedIdentifier(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub245 = input.sub();
    AnnotatedIdentifier(sub245, env);
    try {
      final StringCursor sub246 = sub245.sub();
      sub246.accept(".");
      AnnotatedIdentifier(sub246, env);
      sub246.commit();
      try {
        while(true) {
          final StringCursor sub247 = sub245.sub();
          sub247.accept(".");
          AnnotatedIdentifier(sub247, env);
          sub247.commit();
        }
      } catch(final ChessurException e248) {
        //continue
      }
    } catch(final ChessurException e249) {
      //continue
    }
    sub245.commit();
    return sub245.match();
  }

  private static Object QualifiedName(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub57 = input.sub();
    env[7] /*id*/= QualifiedIdentifier(sub57, env);
    S(sub57, env);
    sub57.commit();
    return sub57.isSet("id", env[7]);
  }

  private static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub190 = input.sub();
    sub190.accept("repeat");
    Sep(sub190, env);
    try {
      env[8] /*stmt*/= new org.fuwjin.chessur.expression.RepeatStatement((org.fuwjin.chessur.expression.Expression)Statement(sub190, env));
    } catch(final ChessurException e191) {
      throw new RuntimeException("repeat keyword requires a statement", e191);
    }
    sub190.commit();
    return sub190.isSet("stmt", env[8]);
  }

  private static Object S(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub1 = input.sub();
    try {
      Space(sub1, env);
    } catch(final ChessurException e2) {
      //continue
    }
    sub1.commit();
    return null;
  }

  private static Object Script(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub115 = input.sub();
    env[5] /*script*/= ScriptIdent(sub115, env);
    try {
      final StringCursor sub116 = sub115.sub();
      sub116.accept("<<");
      S(sub116, env);
      env[5] /*script*/= new org.fuwjin.chessur.expression.ScriptInput((org.fuwjin.chessur.expression.Expression)sub116.isSet("script", env[5]), (org.fuwjin.chessur.expression.Expression)Value(sub116, env));
      sub116.commit();
    } catch(final ChessurException e117) {
      //continue
    }
    try {
      final StringCursor sub118 = sub115.sub();
      sub118.accept(">>");
      S(sub118, env);
      env[5] /*script*/= new org.fuwjin.chessur.expression.ScriptPipe((org.fuwjin.chessur.expression.Expression)sub118.isSet("script", env[5]), (org.fuwjin.chessur.expression.Expression)ScriptIdent(sub118, env));
      sub118.commit();
      try {
        while(true) {
          final StringCursor sub119 = sub115.sub();
          sub119.accept(">>");
          S(sub119, env);
          env[5] /*script*/= new org.fuwjin.chessur.expression.ScriptPipe((org.fuwjin.chessur.expression.Expression)sub119.isSet("script", env[5]), (org.fuwjin.chessur.expression.Expression)ScriptIdent(sub119, env));
          sub119.commit();
        }
      } catch(final ChessurException e120) {
        //continue
      }
    } catch(final ChessurException e121) {
      //continue
    }
    try {
      final StringCursor sub122 = sub115.sub();
      sub122.accept(">>");
      S(sub122, env);
      env[5] /*script*/= new org.fuwjin.chessur.expression.ScriptOutput((org.fuwjin.chessur.expression.Expression)sub122.isSet("script", env[5]), (java.lang.String)Name(sub122, env));
      sub122.commit();
    } catch(final ChessurException e123) {
      //continue
    }
    sub115.commit();
    return sub115.isSet("script", env[5]);
  }

  private static Object ScriptDeclaration(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub27 = input.sub();
    sub27.accept("<");
    try {
      env[4] /*name*/= Identifier(sub27, env);
    } catch(final ChessurException e28) {
      throw new RuntimeException("script identifiers must be enclosed in angle brackets", e28);
    }
    try {
      sub27.accept(">");
    } catch(final ChessurException e29) {
      throw new RuntimeException("script identifiers must end with an angle bracket", e29);
    }
    S(sub27, env);
    try {
      sub27.accept("{");
    } catch(final ChessurException e30) {
      throw new RuntimeException("script declarations must start with a brace", e30);
    }
    S(sub27, env);
    env[5] /*script*/= new org.fuwjin.chessur.expression.Declaration((java.lang.String)sub27.isSet("name", env[4]));
    try {
      try {
        ((org.fuwjin.chessur.expression.Declaration)sub27.isSet("script", env[5])).add((org.fuwjin.chessur.expression.Expression)Statement(sub27, env));
      } catch(final Exception e31) {
        throw sub27.ex(e31);
      }
      try {
        while(true) {
          try {
            ((org.fuwjin.chessur.expression.Declaration)sub27.isSet("script", env[5])).add((org.fuwjin.chessur.expression.Expression)Statement(sub27, env));
          } catch(final Exception e32) {
            throw sub27.ex(e32);
          }
        }
      } catch(final ChessurException e33) {
        //continue
      }
    } catch(final ChessurException e34) {
      //continue
    }
    try {
      final StringCursor sub35 = sub27.sub();
      sub35.accept("return");
      Sep(sub35, env);
      try {
        try {
          ((org.fuwjin.chessur.expression.Declaration)sub35.isSet("script", env[5])).returns((org.fuwjin.chessur.expression.Expression)Value(sub35, env));
        } catch(final Exception e36) {
          throw sub35.ex(e36);
        }
      } catch(final ChessurException e37) {
        throw new RuntimeException("return keyword requires a value", e37);
      }
      sub35.commit();
    } catch(final ChessurException e38) {
      //continue
    }
    try {
      sub27.accept("}");
    } catch(final ChessurException e39) {
      throw new RuntimeException("script declaration for " + sub27.isSet("name", env[4]) + " must end with a brace", e39);
    }
    S(sub27, env);
    sub27.commit();
    return sub27.isSet("script", env[5]);
  }

  private static Object ScriptIdent(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub216 = input.sub();
    sub216.accept("<");
    try {
      env[7] /*id*/= Identifier(sub216, env);
    } catch(final ChessurException e217) {
      throw new RuntimeException("script identifiers must be enclosed in angle brackets", e217);
    }
    try {
      final StringCursor sub218 = sub216.sub();
      sub218.accept(":");
      env[4] /*name*/= Identifier(sub218, env);
      env[24] /*module*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub218.isSet("cat", env[1])).getModule((java.lang.String)sub218.isSet("id", env[7]));
      env[5] /*script*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub218.isSet("module", env[24])).get((java.lang.String)sub218.isSet("name", env[4]));
      sub218.commit();
    } catch(final ChessurException e219) {
      final StringCursor sub220 = sub216.sub();
      env[5] /*script*/= ((org.fuwjin.chessur.expression.CatalogImpl)sub220.isSet("cat", env[1])).get((java.lang.String)sub220.isSet("id", env[7]));
      sub220.commit();
    }
    try {
      sub216.accept(">");
    } catch(final ChessurException e221) {
      throw new RuntimeException("script identifiers must be normal identifiers in angle brackets", e221);
    }
    S(sub216, env);
    sub216.commit();
    return sub216.isSet("script", env[5]);
  }

  private static Object Sep(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub52 = input.sub();
    final StringCursor sub53 = sub52.sub();
    boolean b = true;
    try {
      if(java.lang.Character.isJavaIdentifierPart(sub53.next()) == Boolean.FALSE) {
        b = false;
      }
    } catch(final ChessurException e54) {
      b = false;
    }
    if(b){
      throw sub52.ex("unexpected value");
    }
    S(sub52, env);
    sub52.commit();
    return null;
  }

  private static Object Space(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub266 = input.sub();
    try {
      sub266.acceptIn("\t-\n\r ","\t\n\r ");
    } catch(final ChessurException e267) {
      Comment(sub266, env);
    }
    try {
      while(true) {
        try {
          sub266.acceptIn("\t-\n\r ","\t\n\r ");
        } catch(final ChessurException e268) {
          Comment(sub266, env);
        }
      }
    } catch(final ChessurException e269) {
      //continue
    }
    sub266.commit();
    return null;
  }

  private static Object Statement(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub60 = input.sub();
    try {
      env[8] /*stmt*/= IsStatement(sub60, env);
    } catch(final ChessurException e61) {
      try {
        env[8] /*stmt*/= EitherOrStatement(sub60, env);
      } catch(final ChessurException e62) {
        try {
          env[8] /*stmt*/= CouldStatement(sub60, env);
        } catch(final ChessurException e63) {
          try {
            env[8] /*stmt*/= RepeatStatement(sub60, env);
          } catch(final ChessurException e64) {
            try {
              env[8] /*stmt*/= AcceptStatement(sub60, env);
            } catch(final ChessurException e65) {
              try {
                env[8] /*stmt*/= PublishStatement(sub60, env);
              } catch(final ChessurException e66) {
                try {
                  env[8] /*stmt*/= AbortStatement(sub60, env);
                } catch(final ChessurException e67) {
                  try {
                    env[8] /*stmt*/= Script(sub60, env);
                  } catch(final ChessurException e68) {
                    try {
                      env[8] /*stmt*/= Block(sub60, env);
                    } catch(final ChessurException e69) {
                      try {
                        env[8] /*stmt*/= Assignment(sub60, env);
                      } catch(final ChessurException e70) {
                        env[8] /*stmt*/= Invocation(sub60, env);
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
    sub60.commit();
    return sub60.isSet("stmt", env[8]);
  }

  private static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub81 = input.sub();
    sub81.accept("'");
    env[10] /*lit*/= new org.fuwjin.chessur.expression.Literal();
    try {
      try {
        final StringCursor sub82 = sub81.sub();
        env[11] /*ch*/= sub82.acceptNotIn("'\\","'\\");
        try {
          ((org.fuwjin.chessur.expression.Literal)sub82.isSet("lit", env[10])).append((java.lang.Integer)sub82.isSet("ch", env[11]));
        } catch(final Exception e83) {
          throw sub82.ex(e83);
        }
        sub82.commit();
      } catch(final ChessurException e84) {
        final StringCursor sub85 = sub81.sub();
        try {
          ((org.fuwjin.chessur.expression.Literal)sub85.isSet("lit", env[10])).append((java.lang.Integer)Escape(sub85, env));
        } catch(final Exception e86) {
          throw sub85.ex(e86);
        }
        sub85.commit();
      }
      try {
        while(true) {
          try {
            final StringCursor sub87 = sub81.sub();
            env[11] /*ch*/= sub87.acceptNotIn("'\\","'\\");
            try {
              ((org.fuwjin.chessur.expression.Literal)sub87.isSet("lit", env[10])).append((java.lang.Integer)sub87.isSet("ch", env[11]));
            } catch(final Exception e88) {
              throw sub87.ex(e88);
            }
            sub87.commit();
          } catch(final ChessurException e89) {
            final StringCursor sub90 = sub81.sub();
            try {
              ((org.fuwjin.chessur.expression.Literal)sub90.isSet("lit", env[10])).append((java.lang.Integer)Escape(sub90, env));
            } catch(final Exception e91) {
              throw sub90.ex(e91);
            }
            sub90.commit();
          }
        }
      } catch(final ChessurException e92) {
        //continue
      }
    } catch(final ChessurException e93) {
      //continue
    }
    try {
      sub81.accept("'");
    } catch(final ChessurException e94) {
      throw new RuntimeException("static literals must end with a quote", e94);
    }
    S(sub81, env);
    sub81.commit();
    return sub81.isSet("lit", env[10]);
  }
  
  private static Object Value(final StringCursor input, final Object... parentEnv) throws ChessurException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub71 = input.sub();
    try {
      env[9] /*val*/= StaticLiteral(sub71, env);
    } catch(final ChessurException e72) {
      try {
        env[9] /*val*/= DynamicLiteral(sub71, env);
      } catch(final ChessurException e73) {
        try {
          env[9] /*val*/= Script(sub71, env);
        } catch(final ChessurException e74) {
          try {
            env[9] /*val*/= AcceptStatement(sub71, env);
          } catch(final ChessurException e75) {
            try {
              env[9] /*val*/= Invocation(sub71, env);
            } catch(final ChessurException e76) {
              try {
                env[9] /*val*/= Number(sub71, env);
              } catch(final ChessurException e77) {
                try {
                  env[9] /*val*/= Object(sub71, env);
                } catch(final ChessurException e78) {
                  try {
                    env[9] /*val*/= MatchValue(sub71, env);
                  } catch(final ChessurException e79) {
                    try {
                      env[9] /*val*/= NextValue(sub71, env);
                    } catch(final ChessurException e80) {
                      env[9] /*val*/= new org.fuwjin.chessur.expression.Variable((java.lang.String)Name(sub71, env));
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    sub71.commit();
    return sub71.isSet("val", env[9]);
  }
}
