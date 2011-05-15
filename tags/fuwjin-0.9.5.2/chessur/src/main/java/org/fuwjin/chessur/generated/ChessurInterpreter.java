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

      public StringCursor(final int start, final int line, final int column, final CharSequence seq,
            final StringCursor parent) {
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
            throw ex("failed while matching " + expected);
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
            throw ex("Did not match filter: " + name);
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
            throw ex("failed while matching " + expected);
         }
         return advance();
      }

      public int acceptNotIn(final String name, final String set) throws ChessurException {
         checkBounds(pos);
         if(set.indexOf(seq.charAt(pos)) >= 0) {
            throw ex("Unexpected match: " + name);
         }
         return advance();
      }

      public void commit() {
         commitInput();
         commitOutput();
      }

      public String context() {
         if(pos == 0) {
            return ": [" + line + "," + column + "] SOF -> [1,0] SOF";
         }
         if(pos > seq.length()) {
            return ": [" + line + "," + column + "] EOF -> [1,0] SOF";
         }
         return ": [" + line + "," + column + "] '" + seq.charAt(pos - 1) + "' -> [1,0] SOF";
      }

      public ChessurException ex(final String message) {
         return new ChessurException(message + context());
      }

      public ChessurException ex(final Throwable cause) {
         return new ChessurException("[" + line + "," + column + "]", cause);
      }

      public Object isSet(final String name, final Object value) throws ChessurException {
         if(UNSET.equals(value)) {
            throw ex("variable " + name + " is unset");
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

      public void publish(final Object value) throws ChessurException {
         try {
            appender.append(value.toString());
         } catch(final IOException e) {
            throw ex(e);
         }
      }

      public StringCursor sub() {
         return new StringCursor(pos, line, column, seq, this);
      }

      public StringCursor subInput(final CharSequence newInput) {
         return new StringCursor(0, 1, 0, newInput, this) {
            @Override
            public void commit() {
               commitOutput();
            }
         };
      }

      public StringCursor subOutput(final StringBuilder newOutput) {
         return new StringCursor(pos, line, column, seq, this) {
            @Override
            public void commit() {
               commitInput();
               appendTo(newOutput);
            }
         };
      }

      protected void checkBounds(final int p) throws ChessurException {
         if(p >= seq.length()) {
            throw ex("unexpected EOF");
         }
      }

      void appendTo(final Appendable dest) {
         try {
            dest.append(appender.toString());
         } catch(final IOException e) {
            throw new RuntimeException("IOException never thrown by StringBuilder", e);
         }
      }

      void commitInput() {
         parent.pos = pos;
         parent.line = line;
         parent.column = column;
      }

      void commitOutput() {
         appendTo(parent.appender);
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
   }

   static final Object UNSET = new Object() {
      @Override
      public String toString() {
         return "UNSET";
      }
   };

   public static Object interpret(final CharSequence in, final Appendable out, final Map<String, ?> environment)
         throws ChessurException {
      final StringCursor input = new StringCursor(in, out);
      final Object[] env = new Object[27];
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
      env[13] = environment.containsKey("postage") ? environment.get("postage") : UNSET;
      env[14] = environment.containsKey("id") ? environment.get("id") : UNSET;
      env[15] = environment.containsKey("module") ? environment.get("module") : UNSET;
      env[16] = environment.containsKey("type") ? environment.get("type") : UNSET;
      env[17] = environment.containsKey("constructor") ? environment.get("constructor") : UNSET;
      env[18] = environment.containsKey("object") ? environment.get("object") : UNSET;
      env[19] = environment.containsKey("setter") ? environment.get("setter") : UNSET;
      env[20] = environment.containsKey("filter") ? environment.get("filter") : UNSET;
      env[21] = environment.containsKey("start") ? environment.get("start") : UNSET;
      env[22] = environment.containsKey("ch") ? environment.get("ch") : UNSET;
      env[23] = environment.containsKey("lit") ? environment.get("lit") : UNSET;
      env[24] = environment.containsKey("num") ? environment.get("num") : UNSET;
      env[25] = environment.containsKey("prefix") ? environment.get("prefix") : UNSET;
      env[26] = environment.containsKey("alias") ? environment.get("alias") : UNSET;
      return Catalog(input, env);
   }

   static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub97 = input.sub();
      sub97.accept("abort");
      Sep(sub97, env);
      try {
         env[8] /* stmt */= new org.fuwjin.chessur.expression.AbortStatement(
               (org.fuwjin.chessur.expression.Expression)Value(sub97, env));
      } catch(final ChessurException e98) {
         throw new RuntimeException("abort keyword requires a value" + sub97.context(), e98);
      }
      sub97.commit();
      return sub97.isSet("stmt", env[8]);
   }

   static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub89 = input.sub();
      sub89.accept("accept");
      Sep(sub89, env);
      try {
         final StringCursor sub90 = sub89.sub();
         sub90.accept("not");
         Sep(sub90, env);
         env[9] /* notted */= java.lang.Boolean.TRUE;
         sub90.commit();
      } catch(final ChessurException e91) {
         final StringCursor sub92 = sub89.sub();
         env[9] /* notted */= java.lang.Boolean.FALSE;
         sub92.commit();
      }
      try {
         env[8] /* stmt */= new org.fuwjin.chessur.expression.FilterAcceptStatement((java.lang.Boolean)sub89.isSet(
               "notted", env[9]), (org.fuwjin.chessur.expression.Filter)InFilter(sub89, env));
      } catch(final ChessurException e93) {
         try {
            env[8] /* stmt */= new org.fuwjin.chessur.expression.ValueAcceptStatement((java.lang.Boolean)sub89.isSet(
                  "notted", env[9]), (org.fuwjin.chessur.expression.Expression)Value(sub89, env));
         } catch(final ChessurException e94) {
            throw new RuntimeException("accept keyword requires a value or in keyword" + sub89.context(), e94);
         }
      }
      sub89.commit();
      return sub89.isSet("stmt", env[8]);
   }

   static Object AliasDeclaration(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub15 = input.sub();
      sub15.accept("alias");
      Sep(sub15, env);
      try {
         env[4] /* qname */= QualifiedName(sub15, env);
      } catch(final ChessurException e16) {
         throw new RuntimeException("alias keyword requires a qualified name" + sub15.context(), e16);
      }
      try {
         final StringCursor sub17 = sub15.sub();
         sub17.accept("as");
         Sep(sub17, env);
         try {
            try {
               ((org.fuwjin.chessur.expression.CatalogImpl)sub17.isSet("cat", env[0])).alias(
                     (java.lang.String)sub17.isSet("qname", env[4]), (java.lang.String)Name(sub17, env));
            } catch(final Exception e18) {
               throw sub17.ex(e18);
            }
         } catch(final ChessurException e19) {
            throw new RuntimeException("alias-as keywords require a name" + sub17.context(), e19);
         }
         sub17.commit();
      } catch(final ChessurException e20) {
         try {
            final StringCursor sub21 = sub15.sub();
            sub21.accept("(");
            S(sub21, env);
            env[5] /* signature */= new org.fuwjin.dinah.TypedArgsSignature((java.lang.String)sub21.isSet("qname",
                  env[4]));
            try {
               final StringCursor sub22 = sub21.sub();
               try {
                  ((org.fuwjin.dinah.TypedArgsSignature)sub22.isSet("signature", env[5]))
                        .addArg((java.lang.String)QualifiedName(sub22, env));
               } catch(final Exception e23) {
                  throw sub22.ex(e23);
               }
               try {
                  final StringCursor sub24 = sub22.sub();
                  sub24.accept(",");
                  S(sub24, env);
                  try {
                     ((org.fuwjin.dinah.TypedArgsSignature)sub24.isSet("signature", env[5]))
                           .addArg((java.lang.String)QualifiedName(sub24, env));
                  } catch(final Exception e25) {
                     throw sub24.ex(e25);
                  }
                  sub24.commit();
                  try {
                     while(true) {
                        final StringCursor sub26 = sub22.sub();
                        sub26.accept(",");
                        S(sub26, env);
                        try {
                           ((org.fuwjin.dinah.TypedArgsSignature)sub26.isSet("signature", env[5]))
                                 .addArg((java.lang.String)QualifiedName(sub26, env));
                        } catch(final Exception e27) {
                           throw sub26.ex(e27);
                        }
                        sub26.commit();
                     }
                  } catch(final ChessurException e28) {
                     //continue
                  }
               } catch(final ChessurException e29) {
                  //continue
               }
               sub22.commit();
            } catch(final ChessurException e30) {
               //continue
            }
            sub21.accept(")");
            S(sub21, env);
            sub21.accept("as");
            Sep(sub21, env);
            try {
               try {
                  ((org.fuwjin.chessur.expression.CatalogImpl)sub21.isSet("cat", env[0])).aliasSignature(
                        (org.fuwjin.dinah.FunctionSignature)sub21.isSet("signature", env[5]),
                        (java.lang.String)Name(sub21, env));
               } catch(final Exception e31) {
                  throw sub21.ex(e31);
               }
            } catch(final ChessurException e32) {
               throw new RuntimeException("alias-as keywords require a name" + sub21.context(), e32);
            }
            sub21.commit();
         } catch(final ChessurException e33) {
            throw new RuntimeException("alias keyword requires as keyword" + sub15.context(), e33);
         }
      }
      sub15.commit();
      return null;
   }

   static Object AliasName(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub237 = input.sub();
      env[25] /* prefix */= Identifier(sub237, env);
      env[26] /* alias */= ((org.fuwjin.chessur.expression.CatalogImpl)sub237.isSet("cat", env[0]))
            .alias((java.lang.String)sub237.isSet("prefix", env[25]));
      try {
         final StringCursor sub238 = sub237.sub();
         sub238.accept(".");
         env[1] /* name */= sub238.isSet("alias", env[26]) + "." + QualifiedName(sub238, env);
         sub238.commit();
      } catch(final ChessurException e239) {
         final StringCursor sub240 = sub237.sub();
         env[1] /* name */= sub240.isSet("alias", env[26]);
         sub240.commit();
      }
      sub237.commit();
      return sub237.isSet("name", env[1]);
   }

   static Object AnnotatedIdentifier(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub248 = input.sub();
      Identifier(sub248, env);
      try {
         final StringCursor sub249 = sub248.sub();
         sub249.accept("[");
         try {
            Identifier(sub249, env);
         } catch(final ChessurException e250) {
            //continue
         }
         sub249.accept("]");
         sub249.commit();
         try {
            while(true) {
               final StringCursor sub251 = sub248.sub();
               sub251.accept("[");
               try {
                  Identifier(sub251, env);
               } catch(final ChessurException e252) {
                  //continue
               }
               sub251.accept("]");
               sub251.commit();
            }
         } catch(final ChessurException e253) {
            //continue
         }
      } catch(final ChessurException e254) {
         //continue
      }
      sub248.commit();
      return null;
   }

   static Object Assignment(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub105 = input.sub();
      env[1] /* name */= Name(sub105, env);
      sub105.accept("=");
      S(sub105, env);
      try {
         env[8] /* stmt */= new org.fuwjin.chessur.expression.Assignment(
               (java.lang.String)sub105.isSet("name", env[1]), (org.fuwjin.chessur.expression.Expression)Value(sub105,
                     env));
      } catch(final ChessurException e106) {
         throw new RuntimeException("assignment requires a value" + sub105.context(), e106);
      }
      sub105.commit();
      return sub105.isSet("stmt", env[8]);
   }

   static Object Block(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub99 = input.sub();
      sub99.accept("{");
      S(sub99, env);
      env[10] /* block */= new org.fuwjin.chessur.expression.Block();
      try {
         try {
            ((org.fuwjin.chessur.expression.Block)sub99.isSet("block", env[10]))
                  .add((org.fuwjin.chessur.expression.Expression)Statement(sub99, env));
         } catch(final Exception e100) {
            throw sub99.ex(e100);
         }
         try {
            while(true) {
               try {
                  ((org.fuwjin.chessur.expression.Block)sub99.isSet("block", env[10]))
                        .add((org.fuwjin.chessur.expression.Expression)Statement(sub99, env));
               } catch(final Exception e101) {
                  throw sub99.ex(e101);
               }
            }
         } catch(final ChessurException e102) {
            //continue
         }
      } catch(final ChessurException e103) {
         //continue
      }
      try {
         sub99.accept("}");
      } catch(final ChessurException e104) {
         throw new RuntimeException("block must end with a brace" + sub99.context(), e104);
      }
      S(sub99, env);
      sub99.commit();
      return sub99.isSet("block", env[10]);
   }

   static Object Catalog(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub1 = input.sub();
      env[0] /* cat */= new org.fuwjin.chessur.expression.CatalogImpl((java.lang.String)sub1.isSet("name", env[1]),
            (org.fuwjin.chessur.CatalogManager)sub1.isSet("manager", env[2]));
      S(sub1, env);
      try {
         try {
            LoadDeclaration(sub1, env);
         } catch(final ChessurException e2) {
            try {
               AliasDeclaration(sub1, env);
            } catch(final ChessurException e3) {
               try {
                  ((org.fuwjin.chessur.expression.CatalogImpl)sub1.isSet("cat", env[0]))
                        .add((org.fuwjin.chessur.expression.Declaration)ScriptDeclaration(sub1, env));
               } catch(final Exception e4) {
                  throw sub1.ex(e4);
               }
            }
         }
         try {
            while(true) {
               try {
                  LoadDeclaration(sub1, env);
               } catch(final ChessurException e5) {
                  try {
                     AliasDeclaration(sub1, env);
                  } catch(final ChessurException e6) {
                     try {
                        ((org.fuwjin.chessur.expression.CatalogImpl)sub1.isSet("cat", env[0]))
                              .add((org.fuwjin.chessur.expression.Declaration)ScriptDeclaration(sub1, env));
                     } catch(final Exception e7) {
                        throw sub1.ex(e7);
                     }
                  }
               }
            }
         } catch(final ChessurException e8) {
            //continue
         }
      } catch(final ChessurException e9) {
         //continue
      }
      EndOfFile(sub1, env);
      sub1.commit();
      return sub1.isSet("cat", env[0]);
   }

   static Object Comment(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub268 = input.sub();
      sub268.accept("#");
      try {
         sub268.acceptNotIn("\n\r", "\n\r");
         try {
            while(true) {
               sub268.acceptNotIn("\n\r", "\n\r");
            }
         } catch(final ChessurException e269) {
            //continue
         }
      } catch(final ChessurException e270) {
         //continue
      }
      try {
         sub268.accept("\r");
      } catch(final ChessurException e271) {
         //continue
      }
      try {
         sub268.accept("\n");
      } catch(final ChessurException e272) {
         EndOfFile(sub268, env);
      }
      sub268.commit();
      return null;
   }

   static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub85 = input.sub();
      sub85.accept("could");
      Sep(sub85, env);
      try {
         env[8] /* stmt */= new org.fuwjin.chessur.expression.CouldStatement(
               (org.fuwjin.chessur.expression.Expression)Statement(sub85, env));
      } catch(final ChessurException e86) {
         throw new RuntimeException("could keyword requires a statement" + sub85.context(), e86);
      }
      sub85.commit();
      return sub85.isSet("stmt", env[8]);
   }

   static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub182 = input.sub();
      sub182.accept("\"");
      env[23] /* lit */= new org.fuwjin.chessur.expression.CompositeLiteral();
      try {
         try {
            final StringCursor sub183 = sub182.sub();
            sub183.accept("'");
            S(sub183, env);
            try {
               ((org.fuwjin.chessur.expression.CompositeLiteral)sub183.isSet("lit", env[23]))
                     .append((org.fuwjin.chessur.expression.Expression)Value(sub183, env));
            } catch(final Exception e184) {
               throw sub183.ex(e184);
            }
            sub183.accept("'");
            sub183.commit();
         } catch(final ChessurException e185) {
            try {
               final StringCursor sub186 = sub182.sub();
               try {
                  ((org.fuwjin.chessur.expression.CompositeLiteral)sub186.isSet("lit", env[23]))
                        .appendChar((java.lang.Integer)Escape(sub186, env));
               } catch(final Exception e187) {
                  throw sub186.ex(e187);
               }
               sub186.commit();
            } catch(final ChessurException e188) {
               final StringCursor sub189 = sub182.sub();
               env[22] /* ch */= sub189.acceptNotIn("\"\\", "\"\\");
               try {
                  ((org.fuwjin.chessur.expression.CompositeLiteral)sub189.isSet("lit", env[23]))
                        .appendChar((java.lang.Integer)sub189.isSet("ch", env[22]));
               } catch(final Exception e190) {
                  throw sub189.ex(e190);
               }
               sub189.commit();
            }
         }
         try {
            while(true) {
               try {
                  final StringCursor sub191 = sub182.sub();
                  sub191.accept("'");
                  S(sub191, env);
                  try {
                     ((org.fuwjin.chessur.expression.CompositeLiteral)sub191.isSet("lit", env[23]))
                           .append((org.fuwjin.chessur.expression.Expression)Value(sub191, env));
                  } catch(final Exception e192) {
                     throw sub191.ex(e192);
                  }
                  sub191.accept("'");
                  sub191.commit();
               } catch(final ChessurException e193) {
                  try {
                     final StringCursor sub194 = sub182.sub();
                     try {
                        ((org.fuwjin.chessur.expression.CompositeLiteral)sub194.isSet("lit", env[23]))
                              .appendChar((java.lang.Integer)Escape(sub194, env));
                     } catch(final Exception e195) {
                        throw sub194.ex(e195);
                     }
                     sub194.commit();
                  } catch(final ChessurException e196) {
                     final StringCursor sub197 = sub182.sub();
                     env[22] /* ch */= sub197.acceptNotIn("\"\\", "\"\\");
                     try {
                        ((org.fuwjin.chessur.expression.CompositeLiteral)sub197.isSet("lit", env[23]))
                              .appendChar((java.lang.Integer)sub197.isSet("ch", env[22]));
                     } catch(final Exception e198) {
                        throw sub197.ex(e198);
                     }
                     sub197.commit();
                  }
               }
            }
         } catch(final ChessurException e199) {
            //continue
         }
      } catch(final ChessurException e200) {
         //continue
      }
      try {
         sub182.accept("\"");
      } catch(final ChessurException e201) {
         throw new RuntimeException("dynamic literals must end with a double quote" + sub182.context(), e201);
      }
      S(sub182, env);
      sub182.commit();
      return sub182.isSet("lit", env[23]);
   }

   static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub75 = input.sub();
      sub75.accept("either");
      Sep(sub75, env);
      try {
         env[8] /* stmt */= new org.fuwjin.chessur.expression.EitherOrStatement(
               (org.fuwjin.chessur.expression.Expression)Statement(sub75, env));
      } catch(final ChessurException e76) {
         throw new RuntimeException("either keyword requires a statement" + sub75.context(), e76);
      }
      try {
         final StringCursor sub77 = sub75.sub();
         sub77.accept("or");
         Sep(sub77, env);
         try {
            try {
               ((org.fuwjin.chessur.expression.EitherOrStatement)sub77.isSet("stmt", env[8]))
                     .or((org.fuwjin.chessur.expression.Expression)Statement(sub77, env));
            } catch(final Exception e78) {
               throw sub77.ex(e78);
            }
         } catch(final ChessurException e79) {
            throw new RuntimeException("or keyword requires a statement" + sub77.context(), e79);
         }
         sub77.commit();
         try {
            while(true) {
               final StringCursor sub80 = sub75.sub();
               sub80.accept("or");
               Sep(sub80, env);
               try {
                  try {
                     ((org.fuwjin.chessur.expression.EitherOrStatement)sub80.isSet("stmt", env[8]))
                           .or((org.fuwjin.chessur.expression.Expression)Statement(sub80, env));
                  } catch(final Exception e81) {
                     throw sub80.ex(e81);
                  }
               } catch(final ChessurException e82) {
                  throw new RuntimeException("or keyword requires a statement" + sub80.context(), e82);
               }
               sub80.commit();
            }
         } catch(final ChessurException e83) {
            //continue
         }
      } catch(final ChessurException e84) {
         throw new RuntimeException("either keyword requires at least one or keyword" + sub75.context(), e84);
      }
      sub75.commit();
      return sub75.isSet("stmt", env[8]);
   }

   static Object EndOfFile(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub273 = input.sub();
      final StringCursor sub274 = sub273.sub();
      boolean b = true;
      try {
         if((Object)sub274.next() == Boolean.FALSE) {
            b = false;
         }
      } catch(final ChessurException e275) {
         b = false;
      }
      if(b) {
         throw sub273.ex("unexpected value");
      }
      sub273.commit();
      return null;
   }

   static Object Escape(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub202 = input.sub();
      sub202.accept("\\");
      try {
         final StringCursor sub203 = sub202.sub();
         sub203.accept("n");
         env[22] /* ch */= org.fuwjin.chessur.expression.Literal.NEW_LINE;
         sub203.commit();
      } catch(final ChessurException e204) {
         try {
            final StringCursor sub205 = sub202.sub();
            sub205.accept("t");
            env[22] /* ch */= org.fuwjin.chessur.expression.Literal.TAB;
            sub205.commit();
         } catch(final ChessurException e206) {
            try {
               final StringCursor sub207 = sub202.sub();
               sub207.accept("r");
               env[22] /* ch */= org.fuwjin.chessur.expression.Literal.RETURN;
               sub207.commit();
            } catch(final ChessurException e208) {
               try {
                  final StringCursor sub209 = sub202.sub();
                  sub209.accept("x");
                  env[22] /* ch */= org.fuwjin.chessur.expression.Literal.parseHex((java.lang.String)HexDigits(sub209,
                        env));
                  sub209.commit();
               } catch(final ChessurException e210) {
                  final StringCursor sub211 = sub202.sub();
                  env[22] /* ch */= sub211.accept();
                  sub211.commit();
               }
            }
         }
      }
      sub202.commit();
      return sub202.isSet("ch", env[22]);
   }

   static Object Field(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub147 = input.sub();
      env[1] /* name */= Name(sub147, env);
      env[19] /* setter */= ((org.fuwjin.dinah.FunctionProvider)sub147.isSet("postage", env[13]))
            .getFunction((org.fuwjin.dinah.FunctionSignature)new org.fuwjin.dinah.ArgCountSignature(
                  (java.lang.String)sub147.isSet("type", env[16]) + "." + sub147.isSet("name", env[1]),
                  (java.lang.Integer)2));
      sub147.accept(":");
      S(sub147, env);
      try {
         ((org.fuwjin.chessur.expression.ObjectTemplate)sub147.isSet("object", env[18])).set(
               (java.lang.String)sub147.isSet("name", env[1]),
               (org.fuwjin.dinah.Function)sub147.isSet("setter", env[19]),
               (org.fuwjin.chessur.expression.Expression)Value(sub147, env));
      } catch(final Exception e148) {
         throw sub147.ex(e148);
      }
      sub147.commit();
      return null;
   }

   static Object FilterChar(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub165 = input.sub();
      try {
         final StringCursor sub166 = sub165.sub();
         env[22] /* ch */= Escape(sub166, env);
         sub166.commit();
      } catch(final ChessurException e167) {
         env[22] /* ch */= sub165.acceptNot("\\");
      }
      sub165.commit();
      return sub165.isSet("ch", env[22]);
   }

   static Object FilterRange(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub159 = input.sub();
      env[21] /* start */= FilterChar(sub159, env);
      S(sub159, env);
      try {
         final StringCursor sub160 = sub159.sub();
         sub160.accept("-");
         S(sub160, env);
         try {
            ((org.fuwjin.chessur.expression.Filter)sub160.isSet("filter", env[20])).addRange(
                  (java.lang.Integer)sub160.isSet("start", env[21]), (java.lang.Integer)FilterChar(sub160, env));
         } catch(final Exception e161) {
            throw sub160.ex(e161);
         }
         S(sub160, env);
         sub160.commit();
      } catch(final ChessurException e162) {
         final StringCursor sub163 = sub159.sub();
         try {
            ((org.fuwjin.chessur.expression.Filter)sub163.isSet("filter", env[20])).addChar((java.lang.Integer)sub163
                  .isSet("start", env[21]));
         } catch(final Exception e164) {
            throw sub163.ex(e164);
         }
         sub163.commit();
      }
      sub159.commit();
      return null;
   }

   static Object HexDigit(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub213 = input.sub();
      sub213.acceptIn("0-9A-Fa-f", "0123456789ABCDEFabcdef");
      sub213.commit();
      return null;
   }

   static Object HexDigits(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub212 = input.sub();
      HexDigit(sub212, env);
      HexDigit(sub212, env);
      HexDigit(sub212, env);
      HexDigit(sub212, env);
      sub212.commit();
      return sub212.match();
   }

   static Object Identifier(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub255 = input.sub();
      IdentifierChar(sub255, env);
      try {
         while(true) {
            IdentifierChar(sub255, env);
         }
      } catch(final ChessurException e256) {
         //continue
      }
      sub255.commit();
      return sub255.match();
   }

   static Object IdentifierChar(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub257 = input.sub();
      final StringCursor sub258 = sub257.sub();
      if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub258.next()) == Boolean.FALSE) {
         throw sub258.ex("check failed");
      }
      sub257.accept();
      sub257.commit();
      return null;
   }

   static Object InFilter(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub151 = input.sub();
      sub151.accept("in");
      Sep(sub151, env);
      env[20] /* filter */= new org.fuwjin.chessur.expression.Filter();
      try {
         FilterRange(sub151, env);
      } catch(final ChessurException e152) {
         throw new RuntimeException("in keyword requires at least one filter" + sub151.context(), e152);
      }
      try {
         final StringCursor sub153 = sub151.sub();
         sub153.accept(",");
         S(sub153, env);
         try {
            FilterRange(sub153, env);
         } catch(final ChessurException e154) {
            throw new RuntimeException("in keyword requires a filter after a comma" + sub153.context(), e154);
         }
         sub153.commit();
         try {
            while(true) {
               final StringCursor sub155 = sub151.sub();
               sub155.accept(",");
               S(sub155, env);
               try {
                  FilterRange(sub155, env);
               } catch(final ChessurException e156) {
                  throw new RuntimeException("in keyword requires a filter after a comma" + sub155.context(), e156);
               }
               sub155.commit();
            }
         } catch(final ChessurException e157) {
            //continue
         }
      } catch(final ChessurException e158) {
         //continue
      }
      sub151.commit();
      return sub151.isSet("filter", env[20]);
   }

   static Object Invocation(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub107 = input.sub();
      env[1] /* name */= AliasName(sub107, env);
      sub107.accept("(");
      S(sub107, env);
      env[11] /* inv */= new org.fuwjin.chessur.expression.Invocation();
      try {
         final StringCursor sub108 = sub107.sub();
         try {
            ((org.fuwjin.chessur.expression.Invocation)sub108.isSet("inv", env[11]))
                  .addParam((org.fuwjin.chessur.expression.Expression)Value(sub108, env));
         } catch(final Exception e109) {
            throw sub108.ex(e109);
         }
         try {
            final StringCursor sub110 = sub108.sub();
            sub110.accept(",");
            S(sub110, env);
            try {
               try {
                  ((org.fuwjin.chessur.expression.Invocation)sub110.isSet("inv", env[11]))
                        .addParam((org.fuwjin.chessur.expression.Expression)Value(sub110, env));
               } catch(final Exception e111) {
                  throw sub110.ex(e111);
               }
            } catch(final ChessurException e112) {
               throw new RuntimeException("invocation parameter must be a value" + sub110.context(), e112);
            }
            sub110.commit();
            try {
               while(true) {
                  final StringCursor sub113 = sub108.sub();
                  sub113.accept(",");
                  S(sub113, env);
                  try {
                     try {
                        ((org.fuwjin.chessur.expression.Invocation)sub113.isSet("inv", env[11]))
                              .addParam((org.fuwjin.chessur.expression.Expression)Value(sub113, env));
                     } catch(final Exception e114) {
                        throw sub113.ex(e114);
                     }
                  } catch(final ChessurException e115) {
                     throw new RuntimeException("invocation parameter must be a value" + sub113.context(), e115);
                  }
                  sub113.commit();
               }
            } catch(final ChessurException e116) {
               //continue
            }
         } catch(final ChessurException e117) {
            //continue
         }
         sub108.commit();
      } catch(final ChessurException e118) {
         //continue
      }
      try {
         sub107.accept(")");
      } catch(final ChessurException e119) {
         throw new RuntimeException("invocation must end with a parenthesis" + sub107.context(), e119);
      }
      S(sub107, env);
      try {
         final StringCursor sub120 = sub107.sub();
         env[5] /* signature */= ((org.fuwjin.chessur.expression.CatalogImpl)sub120.isSet("cat", env[0]))
               .getSignature((java.lang.String)sub120.isSet("name", env[1]),
                     (java.lang.Integer)((org.fuwjin.chessur.expression.Invocation)sub120.isSet("inv", env[11]))
                           .paramCount());
         env[12] /* function */= ((org.fuwjin.dinah.FunctionProvider)sub120.isSet("postage", env[13]))
               .getFunction((org.fuwjin.dinah.FunctionSignature)sub120.isSet("signature", env[5]));
         try {
            ((org.fuwjin.chessur.expression.Invocation)sub120.isSet("inv", env[11]))
                  .setFunction((org.fuwjin.dinah.Function)sub120.isSet("function", env[12]));
         } catch(final Exception e121) {
            throw sub120.ex(e121);
         }
         sub120.commit();
      } catch(final ChessurException e122) {
         throw new RuntimeException("Could not get function for " + sub107.isSet("name", env[1]) + sub107.context(),
               e122);
      }
      sub107.commit();
      return sub107.isSet("inv", env[11]);
   }

   static Object IsStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub68 = input.sub();
      sub68.accept("is");
      Sep(sub68, env);
      try {
         final StringCursor sub69 = sub68.sub();
         sub69.accept("not");
         Sep(sub69, env);
         env[9] /* notted */= java.lang.Boolean.TRUE;
         sub69.commit();
      } catch(final ChessurException e70) {
         final StringCursor sub71 = sub68.sub();
         env[9] /* notted */= java.lang.Boolean.FALSE;
         sub71.commit();
      }
      try {
         env[8] /* stmt */= new org.fuwjin.chessur.expression.IsStatement((java.lang.Boolean)sub68.isSet("notted",
               env[9]),
               (org.fuwjin.chessur.expression.Expression)new org.fuwjin.chessur.expression.FilterAcceptStatement(
                     java.lang.Boolean.FALSE, (org.fuwjin.chessur.expression.Filter)InFilter(sub68, env)));
      } catch(final ChessurException e72) {
         try {
            env[8] /* stmt */= new org.fuwjin.chessur.expression.IsStatement((java.lang.Boolean)sub68.isSet("notted",
                  env[9]),
                  (org.fuwjin.chessur.expression.Expression)new org.fuwjin.chessur.expression.ValueAcceptStatement(
                        java.lang.Boolean.FALSE, (org.fuwjin.chessur.expression.Expression)StaticLiteral(sub68, env)));
         } catch(final ChessurException e73) {
            try {
               env[8] /* stmt */= new org.fuwjin.chessur.expression.IsStatement((java.lang.Boolean)sub68.isSet(
                     "notted", env[9]), (org.fuwjin.chessur.expression.Expression)Value(sub68, env));
            } catch(final ChessurException e74) {
               throw new RuntimeException("is keyword requires value or in keyword" + sub68.context(), e74);
            }
         }
      }
      sub68.commit();
      return sub68.isSet("stmt", env[8]);
   }

   static Object LoadDeclaration(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub10 = input.sub();
      sub10.accept("load");
      Sep(sub10, env);
      try {
         env[3] /* path */= PathName(sub10, env);
      } catch(final ChessurException e11) {
         throw new RuntimeException("load keyword requires a file path" + sub10.context(), e11);
      }
      try {
         sub10.accept("as");
      } catch(final ChessurException e12) {
         throw new RuntimeException("load keyword requires as keyword" + sub10.context(), e12);
      }
      Sep(sub10, env);
      try {
         try {
            ((org.fuwjin.chessur.expression.CatalogImpl)sub10.isSet("cat", env[0])).load(
                  (java.lang.String)sub10.isSet("path", env[3]), (java.lang.String)Name(sub10, env));
         } catch(final Exception e13) {
            throw sub10.ex(e13);
         }
      } catch(final ChessurException e14) {
         throw new RuntimeException("load-as keywords require a name" + sub10.context(), e14);
      }
      sub10.commit();
      return null;
   }

   static Object MatchValue(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub149 = input.sub();
      sub149.accept("match");
      Sep(sub149, env);
      sub149.commit();
      return org.fuwjin.chessur.expression.Variable.MATCH;
   }

   static Object Name(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub241 = input.sub();
      env[14] /* id */= Identifier(sub241, env);
      S(sub241, env);
      sub241.commit();
      return sub241.isSet("id", env[14]);
   }

   static Object NextValue(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub150 = input.sub();
      sub150.accept("next");
      Sep(sub150, env);
      sub150.commit();
      return org.fuwjin.chessur.expression.Variable.NEXT;
   }

   static Object Number(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub214 = input.sub();
      try {
         sub214.accept("-");
      } catch(final ChessurException e215) {
         //continue
      }
      try {
         final StringCursor sub216 = sub214.sub();
         sub216.acceptIn("0-9", "0123456789");
         try {
            while(true) {
               sub216.acceptIn("0-9", "0123456789");
            }
         } catch(final ChessurException e217) {
            //continue
         }
         try {
            final StringCursor sub218 = sub216.sub();
            sub218.accept(".");
            try {
               sub218.acceptIn("0-9", "0123456789");
               try {
                  while(true) {
                     sub218.acceptIn("0-9", "0123456789");
                  }
               } catch(final ChessurException e219) {
                  //continue
               }
            } catch(final ChessurException e220) {
               //continue
            }
            sub218.commit();
         } catch(final ChessurException e221) {
            //continue
         }
         sub216.commit();
      } catch(final ChessurException e222) {
         final StringCursor sub223 = sub214.sub();
         sub223.accept(".");
         sub223.acceptIn("0-9", "0123456789");
         try {
            while(true) {
               sub223.acceptIn("0-9", "0123456789");
            }
         } catch(final ChessurException e224) {
            //continue
         }
         sub223.commit();
      }
      try {
         final StringCursor sub225 = sub214.sub();
         sub225.acceptIn("Ee", "Ee");
         try {
            sub225.accept("-");
         } catch(final ChessurException e226) {
            //continue
         }
         sub225.acceptIn("0-9", "0123456789");
         try {
            while(true) {
               sub225.acceptIn("0-9", "0123456789");
            }
         } catch(final ChessurException e227) {
            //continue
         }
         sub225.commit();
      } catch(final ChessurException e228) {
         //continue
      }
      env[24] /* num */= new org.fuwjin.chessur.expression.Number((java.lang.String)sub214.match());
      Sep(sub214, env);
      sub214.commit();
      return sub214.isSet("num", env[24]);
   }

   static Object Object(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub140 = input.sub();
      sub140.accept("(");
      S(sub140, env);
      env[16] /* type */= AliasName(sub140, env);
      env[17] /* constructor */= ((org.fuwjin.dinah.FunctionProvider)sub140.isSet("postage", env[13]))
            .getFunction((org.fuwjin.dinah.FunctionSignature)new org.fuwjin.dinah.ArgCountSignature(
                  (java.lang.String)sub140.isSet("type", env[16]) + ".new", (java.lang.Integer)0));
      env[18] /* object */= new org.fuwjin.chessur.expression.ObjectTemplate((java.lang.String)sub140.isSet("type",
            env[16]), (org.fuwjin.dinah.Function)sub140.isSet("constructor", env[17]));
      sub140.accept(")");
      S(sub140, env);
      sub140.accept("{");
      S(sub140, env);
      try {
         final StringCursor sub141 = sub140.sub();
         Field(sub141, env);
         try {
            final StringCursor sub142 = sub141.sub();
            sub142.accept(",");
            S(sub142, env);
            Field(sub142, env);
            sub142.commit();
            try {
               while(true) {
                  final StringCursor sub143 = sub141.sub();
                  sub143.accept(",");
                  S(sub143, env);
                  Field(sub143, env);
                  sub143.commit();
               }
            } catch(final ChessurException e144) {
               //continue
            }
         } catch(final ChessurException e145) {
            //continue
         }
         sub141.commit();
      } catch(final ChessurException e146) {
         //continue
      }
      sub140.accept("}");
      S(sub140, env);
      sub140.commit();
      return sub140.isSet("object", env[18]);
   }

   static Object Path(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub229 = input.sub();
      final StringCursor sub230 = sub229.sub();
      try {
         sub230.accept("/");
      } catch(final ChessurException e231) {
         //continue
      }
      QualifiedIdentifier(sub230, env);
      sub230.commit();
      try {
         while(true) {
            final StringCursor sub232 = sub229.sub();
            try {
               sub232.accept("/");
            } catch(final ChessurException e233) {
               //continue
            }
            QualifiedIdentifier(sub232, env);
            sub232.commit();
         }
      } catch(final ChessurException e234) {
         //continue
      }
      try {
         sub229.accept("/");
      } catch(final ChessurException e235) {
         //continue
      }
      sub229.commit();
      return sub229.match();
   }

   static Object PathName(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub236 = input.sub();
      env[3] /* path */= Path(sub236, env);
      S(sub236, env);
      sub236.commit();
      return sub236.isSet("path", env[3]);
   }

   static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub95 = input.sub();
      sub95.accept("publish");
      Sep(sub95, env);
      try {
         env[8] /* stmt */= new org.fuwjin.chessur.expression.PublishStatement(
               (org.fuwjin.chessur.expression.Expression)Value(sub95, env));
      } catch(final ChessurException e96) {
         throw new RuntimeException("publish keyword requires a value" + sub95.context(), e96);
      }
      sub95.commit();
      return sub95.isSet("stmt", env[8]);
   }

   static Object QualifiedIdentifier(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub243 = input.sub();
      AnnotatedIdentifier(sub243, env);
      try {
         final StringCursor sub244 = sub243.sub();
         sub244.accept(".");
         AnnotatedIdentifier(sub244, env);
         sub244.commit();
         try {
            while(true) {
               final StringCursor sub245 = sub243.sub();
               sub245.accept(".");
               AnnotatedIdentifier(sub245, env);
               sub245.commit();
            }
         } catch(final ChessurException e246) {
            //continue
         }
      } catch(final ChessurException e247) {
         //continue
      }
      sub243.commit();
      return sub243.match();
   }

   static Object QualifiedName(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub242 = input.sub();
      env[14] /* id */= QualifiedIdentifier(sub242, env);
      S(sub242, env);
      sub242.commit();
      return sub242.isSet("id", env[14]);
   }

   static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub87 = input.sub();
      sub87.accept("repeat");
      Sep(sub87, env);
      try {
         env[8] /* stmt */= new org.fuwjin.chessur.expression.RepeatStatement(
               (org.fuwjin.chessur.expression.Expression)Statement(sub87, env));
      } catch(final ChessurException e88) {
         throw new RuntimeException("repeat keyword requires a statement" + sub87.context(), e88);
      }
      sub87.commit();
      return sub87.isSet("stmt", env[8]);
   }

   static Object S(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub262 = input.sub();
      try {
         Space(sub262, env);
      } catch(final ChessurException e263) {
         //continue
      }
      sub262.commit();
      return null;
   }

   static Object Script(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub123 = input.sub();
      env[6] /* script */= ScriptIdent(sub123, env);
      try {
         final StringCursor sub124 = sub123.sub();
         sub124.accept("<<");
         S(sub124, env);
         env[6] /* script */= new org.fuwjin.chessur.expression.ScriptInput(
               (org.fuwjin.chessur.expression.Expression)sub124.isSet("script", env[6]),
               (org.fuwjin.chessur.expression.Expression)Value(sub124, env));
         sub124.commit();
      } catch(final ChessurException e125) {
         //continue
      }
      try {
         final StringCursor sub126 = sub123.sub();
         sub126.accept(">>");
         S(sub126, env);
         env[6] /* script */= new org.fuwjin.chessur.expression.ScriptPipe(
               (org.fuwjin.chessur.expression.Expression)sub126.isSet("script", env[6]),
               (org.fuwjin.chessur.expression.Expression)ScriptIdent(sub126, env));
         sub126.commit();
         try {
            while(true) {
               final StringCursor sub127 = sub123.sub();
               sub127.accept(">>");
               S(sub127, env);
               env[6] /* script */= new org.fuwjin.chessur.expression.ScriptPipe(
                     (org.fuwjin.chessur.expression.Expression)sub127.isSet("script", env[6]),
                     (org.fuwjin.chessur.expression.Expression)ScriptIdent(sub127, env));
               sub127.commit();
            }
         } catch(final ChessurException e128) {
            //continue
         }
      } catch(final ChessurException e129) {
         //continue
      }
      try {
         final StringCursor sub130 = sub123.sub();
         sub130.accept(">>");
         S(sub130, env);
         env[6] /* script */= new org.fuwjin.chessur.expression.ScriptOutput(
               (org.fuwjin.chessur.expression.Expression)sub130.isSet("script", env[6]), (java.lang.String)Name(sub130,
                     env));
         sub130.commit();
      } catch(final ChessurException e131) {
         //continue
      }
      sub123.commit();
      return sub123.isSet("script", env[6]);
   }

   static Object ScriptDeclaration(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub34 = input.sub();
      sub34.accept("<");
      try {
         env[1] /* name */= Identifier(sub34, env);
      } catch(final ChessurException e35) {
         throw new RuntimeException("script identifiers must be enclosed in angle brackets" + sub34.context(), e35);
      }
      try {
         sub34.accept(">");
      } catch(final ChessurException e36) {
         throw new RuntimeException("script identifiers must end with an angle bracket" + sub34.context(), e36);
      }
      S(sub34, env);
      try {
         sub34.accept("{");
      } catch(final ChessurException e37) {
         throw new RuntimeException("script declarations must start with a brace" + sub34.context(), e37);
      }
      S(sub34, env);
      env[6] /* script */= new org.fuwjin.chessur.expression.Declaration((java.lang.String)sub34.isSet("name", env[1]));
      try {
         try {
            ((org.fuwjin.chessur.expression.Declaration)sub34.isSet("script", env[6]))
                  .add((org.fuwjin.chessur.expression.Expression)Statement(sub34, env));
         } catch(final Exception e38) {
            throw sub34.ex(e38);
         }
         try {
            while(true) {
               try {
                  ((org.fuwjin.chessur.expression.Declaration)sub34.isSet("script", env[6]))
                        .add((org.fuwjin.chessur.expression.Expression)Statement(sub34, env));
               } catch(final Exception e39) {
                  throw sub34.ex(e39);
               }
            }
         } catch(final ChessurException e40) {
            //continue
         }
      } catch(final ChessurException e41) {
         //continue
      }
      try {
         final StringCursor sub42 = sub34.sub();
         sub42.accept("return");
         Sep(sub42, env);
         try {
            try {
               ((org.fuwjin.chessur.expression.Declaration)sub42.isSet("script", env[6]))
                     .returns((org.fuwjin.chessur.expression.Expression)Value(sub42, env));
            } catch(final Exception e43) {
               throw sub42.ex(e43);
            }
         } catch(final ChessurException e44) {
            throw new RuntimeException("return keyword requires a value" + sub42.context(), e44);
         }
         sub42.commit();
      } catch(final ChessurException e45) {
         //continue
      }
      try {
         sub34.accept("}");
      } catch(final ChessurException e46) {
         throw new RuntimeException("script declaration for " + sub34.isSet("name", env[1]) + " must end with a brace"
               + sub34.context(), e46);
      }
      S(sub34, env);
      sub34.commit();
      return sub34.isSet("script", env[6]);
   }

   static Object ScriptIdent(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub132 = input.sub();
      sub132.accept("<");
      try {
         env[14] /* id */= Identifier(sub132, env);
      } catch(final ChessurException e133) {
         throw new RuntimeException("script identifiers must be enclosed in angle brackets" + sub132.context(), e133);
      }
      try {
         final StringCursor sub134 = sub132.sub();
         sub134.accept(":");
         try {
            final StringCursor sub135 = sub134.sub();
            env[1] /* name */= Identifier(sub135, env);
            env[15] /* module */= ((org.fuwjin.chessur.expression.CatalogImpl)sub135.isSet("cat", env[0]))
                  .getModule((java.lang.String)sub135.isSet("id", env[14]));
            env[6] /* script */= ((org.fuwjin.chessur.Module)sub135.isSet("module", env[15]))
                  .get((java.lang.String)sub135.isSet("name", env[1]));
            sub135.commit();
         } catch(final ChessurException e136) {
            throw new RuntimeException("namespaced script " + sub134.isSet("id", env[14]) + ": could not be resolved"
                  + sub134.context(), e136);
         }
         sub134.commit();
      } catch(final ChessurException e137) {
         final StringCursor sub138 = sub132.sub();
         env[6] /* script */= ((org.fuwjin.chessur.Module)sub138.isSet("cat", env[0])).get((java.lang.String)sub138
               .isSet("id", env[14]));
         sub138.commit();
      }
      try {
         sub132.accept(">");
      } catch(final ChessurException e139) {
         throw new RuntimeException("script identifiers must be normal identifiers in angle brackets"
               + sub132.context(), e139);
      }
      S(sub132, env);
      sub132.commit();
      return sub132.isSet("script", env[6]);
   }

   static Object Sep(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub259 = input.sub();
      final StringCursor sub260 = sub259.sub();
      boolean b = true;
      try {
         if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub260.next()) == Boolean.FALSE) {
            b = false;
         }
      } catch(final ChessurException e261) {
         b = false;
      }
      if(b) {
         throw sub259.ex("unexpected value");
      }
      S(sub259, env);
      sub259.commit();
      return null;
   }

   static Object Space(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub264 = input.sub();
      try {
         sub264.acceptIn("\t-\n\r ", "\t\n\r ");
      } catch(final ChessurException e265) {
         Comment(sub264, env);
      }
      try {
         while(true) {
            try {
               sub264.acceptIn("\t-\n\r ", "\t\n\r ");
            } catch(final ChessurException e266) {
               Comment(sub264, env);
            }
         }
      } catch(final ChessurException e267) {
         //continue
      }
      sub264.commit();
      return null;
   }

   static Object Statement(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub57 = input.sub();
      try {
         env[8] /* stmt */= IsStatement(sub57, env);
      } catch(final ChessurException e58) {
         try {
            env[8] /* stmt */= EitherOrStatement(sub57, env);
         } catch(final ChessurException e59) {
            try {
               env[8] /* stmt */= CouldStatement(sub57, env);
            } catch(final ChessurException e60) {
               try {
                  env[8] /* stmt */= RepeatStatement(sub57, env);
               } catch(final ChessurException e61) {
                  try {
                     env[8] /* stmt */= AcceptStatement(sub57, env);
                  } catch(final ChessurException e62) {
                     try {
                        env[8] /* stmt */= PublishStatement(sub57, env);
                     } catch(final ChessurException e63) {
                        try {
                           env[8] /* stmt */= AbortStatement(sub57, env);
                        } catch(final ChessurException e64) {
                           try {
                              env[8] /* stmt */= Script(sub57, env);
                           } catch(final ChessurException e65) {
                              try {
                                 env[8] /* stmt */= Block(sub57, env);
                              } catch(final ChessurException e66) {
                                 try {
                                    env[8] /* stmt */= Assignment(sub57, env);
                                 } catch(final ChessurException e67) {
                                    env[8] /* stmt */= Invocation(sub57, env);
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
      sub57.commit();
      return sub57.isSet("stmt", env[8]);
   }

   static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub168 = input.sub();
      sub168.accept("'");
      env[23] /* lit */= new org.fuwjin.chessur.expression.Literal();
      try {
         try {
            final StringCursor sub169 = sub168.sub();
            env[22] /* ch */= sub169.acceptNotIn("'\\", "'\\");
            try {
               ((org.fuwjin.chessur.expression.Literal)sub169.isSet("lit", env[23])).append((java.lang.Integer)sub169
                     .isSet("ch", env[22]));
            } catch(final Exception e170) {
               throw sub169.ex(e170);
            }
            sub169.commit();
         } catch(final ChessurException e171) {
            final StringCursor sub172 = sub168.sub();
            try {
               ((org.fuwjin.chessur.expression.Literal)sub172.isSet("lit", env[23])).append((java.lang.Integer)Escape(
                     sub172, env));
            } catch(final Exception e173) {
               throw sub172.ex(e173);
            }
            sub172.commit();
         }
         try {
            while(true) {
               try {
                  final StringCursor sub174 = sub168.sub();
                  env[22] /* ch */= sub174.acceptNotIn("'\\", "'\\");
                  try {
                     ((org.fuwjin.chessur.expression.Literal)sub174.isSet("lit", env[23]))
                           .append((java.lang.Integer)sub174.isSet("ch", env[22]));
                  } catch(final Exception e175) {
                     throw sub174.ex(e175);
                  }
                  sub174.commit();
               } catch(final ChessurException e176) {
                  final StringCursor sub177 = sub168.sub();
                  try {
                     ((org.fuwjin.chessur.expression.Literal)sub177.isSet("lit", env[23]))
                           .append((java.lang.Integer)Escape(sub177, env));
                  } catch(final Exception e178) {
                     throw sub177.ex(e178);
                  }
                  sub177.commit();
               }
            }
         } catch(final ChessurException e179) {
            //continue
         }
      } catch(final ChessurException e180) {
         //continue
      }
      try {
         sub168.accept("'");
      } catch(final ChessurException e181) {
         throw new RuntimeException("static literals must end with a quote" + sub168.context(), e181);
      }
      S(sub168, env);
      sub168.commit();
      return sub168.isSet("lit", env[23]);
   }

   static Object Value(final StringCursor input, final Object... parentEnv) throws ChessurException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub47 = input.sub();
      try {
         env[7] /* val */= StaticLiteral(sub47, env);
      } catch(final ChessurException e48) {
         try {
            env[7] /* val */= DynamicLiteral(sub47, env);
         } catch(final ChessurException e49) {
            try {
               env[7] /* val */= Script(sub47, env);
            } catch(final ChessurException e50) {
               try {
                  env[7] /* val */= AcceptStatement(sub47, env);
               } catch(final ChessurException e51) {
                  try {
                     env[7] /* val */= Invocation(sub47, env);
                  } catch(final ChessurException e52) {
                     try {
                        env[7] /* val */= Number(sub47, env);
                     } catch(final ChessurException e53) {
                        try {
                           env[7] /* val */= Object(sub47, env);
                        } catch(final ChessurException e54) {
                           try {
                              env[7] /* val */= MatchValue(sub47, env);
                           } catch(final ChessurException e55) {
                              try {
                                 env[7] /* val */= NextValue(sub47, env);
                              } catch(final ChessurException e56) {
                                 env[7] /* val */= new org.fuwjin.chessur.expression.Variable((java.lang.String)Name(
                                       sub47, env));
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
      sub47.commit();
      return sub47.isSet("val", env[7]);
   }
}
