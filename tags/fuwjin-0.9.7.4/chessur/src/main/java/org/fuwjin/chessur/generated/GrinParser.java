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
import java.util.concurrent.ExecutionException;

public class GrinParser {
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

      public void abort(final Object message) {
         throw new RuntimeException(message + context());
      }

      public void abort(final Object message, final Throwable cause) {
         throw new RuntimeException(message + context(), cause);
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
            throw ex("failed while matching " + expected);
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
            throw ex("Did not match filter: " + name);
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
            throw ex("failed while matching " + expected);
         }
         return advance();
      }

      public int acceptNotIn(final String name, final String set) throws GrinParserException {
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

      public GrinParserException ex(final String message) {
         return new GrinParserException(message + context());
      }

      public GrinParserException ex(final Throwable cause) {
         return new GrinParserException(context(), cause);
      }

      public Object isSet(final String name, final Object value) throws GrinParserException {
         if(UNSET.equals(value)) {
            throw ex("variable " + name + " is unset");
         }
         return value;
      }

      public String match() {
         return seq.subSequence(start, pos).toString();
      }

      public int next() throws GrinParserException {
         checkBounds(pos);
         return seq.charAt(pos);
      }

      public String nextStr() throws GrinParserException {
         checkBounds(pos);
         return seq.subSequence(pos, pos + 1).toString();
      }

      public void publish(final Object value) throws GrinParserException {
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

      protected void checkBounds(final int p) throws GrinParserException {
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
         throws ExecutionException {
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
      try {
         return Catalog(input, env);
      } catch(final GrinParserException e) {
         throw new ExecutionException(e);
      }
   }

   static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub112 = input.sub();
      sub112.accept("abort");
      Sep(sub112, env);
      try {
         try {
            env[8] /* stmt */= new org.fuwjin.chessur.expression.AbortStatement(
                  (org.fuwjin.chessur.expression.Expression)Value(sub112, env));
         } catch(final Exception e113) {
            throw sub112.ex(e113);
         }
      } catch(final GrinParserException e114) {
         sub112.abort(String.valueOf("abort keyword requires a value"), e114);
      }
      sub112.commit();
      return sub112.isSet("stmt", env[8]);
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
            env[9] /* notted */= java.lang.Boolean.TRUE;
         } catch(final Exception e101) {
            throw sub100.ex(e101);
         }
         sub100.commit();
      } catch(final GrinParserException e102) {
         final StringCursor sub103 = sub99.sub();
         try {
            env[9] /* notted */= java.lang.Boolean.FALSE;
         } catch(final Exception e104) {
            throw sub103.ex(e104);
         }
         sub103.commit();
      }
      try {
         try {
            env[8] /* stmt */= new org.fuwjin.chessur.expression.FilterAcceptStatement((java.lang.Boolean)sub99.isSet(
                  "notted", env[9]), (org.fuwjin.chessur.expression.Filter)InFilter(sub99, env));
         } catch(final Exception e105) {
            throw sub99.ex(e105);
         }
      } catch(final GrinParserException e106) {
         try {
            try {
               env[8] /* stmt */= new org.fuwjin.chessur.expression.ValueAcceptStatement(
                     (java.lang.Boolean)sub99.isSet("notted", env[9]), (org.fuwjin.chessur.expression.Expression)Value(
                           sub99, env));
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

   static Object AliasDeclaration(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub16 = input.sub();
      sub16.accept("alias");
      Sep(sub16, env);
      try {
         env[4] /* qname */= QualifiedName(sub16, env);
      } catch(final GrinParserException e17) {
         sub16.abort(String.valueOf("alias keyword requires a qualified name"), e17);
      }
      try {
         final StringCursor sub18 = sub16.sub();
         sub18.accept("as");
         Sep(sub18, env);
         try {
            try {
               ((org.fuwjin.chessur.expression.CatalogImpl)sub18.isSet("cat", env[0])).alias(
                     (java.lang.String)sub18.isSet("qname", env[4]), (java.lang.String)Name(sub18, env));
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
               env[5] /* signature */= ((org.fuwjin.dinah.FunctionProvider)sub22.isSet("manager", env[2]))
                     .forName((java.lang.String)sub22.isSet("qname", env[4]));
            } catch(final Exception e23) {
               throw sub22.ex(e23);
            }
            try {
               final StringCursor sub24 = sub22.sub();
               try {
                  ((org.fuwjin.dinah.ConstraintBuilder)sub24.isSet("signature", env[5]))
                        .withNextArg((java.lang.String)QualifiedName(sub24, env));
               } catch(final Exception e25) {
                  throw sub24.ex(e25);
               }
               try {
                  final StringCursor sub26 = sub24.sub();
                  sub26.accept(",");
                  S(sub26, env);
                  try {
                     ((org.fuwjin.dinah.ConstraintBuilder)sub26.isSet("signature", env[5]))
                           .withNextArg((java.lang.String)QualifiedName(sub26, env));
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
                           ((org.fuwjin.dinah.ConstraintBuilder)sub28.isSet("signature", env[5]))
                                 .withNextArg((java.lang.String)QualifiedName(sub28, env));
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
                  ((org.fuwjin.chessur.expression.CatalogImpl)sub22.isSet("cat", env[0])).aliasSignature(
                        ((org.fuwjin.dinah.ConstraintBuilder)sub22.isSet("signature", env[5])).constraint(),
                        (java.lang.String)Name(sub22, env));
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

   static Object AliasName(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub277 = input.sub();
      env[24] /* prefix */= AnnotatedIdentifier(sub277, env);
      try {
         env[25] /* alias */= ((org.fuwjin.chessur.expression.CatalogImpl)sub277.isSet("cat", env[0]))
               .alias((java.lang.String)sub277.isSet("prefix", env[24]));
      } catch(final Exception e278) {
         throw sub277.ex(e278);
      }
      try {
         final StringCursor sub279 = sub277.sub();
         sub279.accept(".");
         env[1] /* name */= String.valueOf(sub279.isSet("alias", env[25])) + String.valueOf(".")
               + String.valueOf(QualifiedName(sub279, env));
         sub279.commit();
      } catch(final GrinParserException e280) {
         final StringCursor sub281 = sub277.sub();
         env[1] /* name */= sub281.isSet("alias", env[25]);
         sub281.commit();
      }
      sub277.commit();
      return sub277.isSet("name", env[1]);
   }

   static Object AnnotatedIdentifier(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub289 = input.sub();
      Identifier(sub289, env);
      try {
         final StringCursor sub290 = sub289.sub();
         sub290.accept("[");
         try {
            Identifier(sub290, env);
         } catch(final GrinParserException e291) {
            //continue
         }
         sub290.accept("]");
         sub290.commit();
         try {
            while(true) {
               final StringCursor sub292 = sub289.sub();
               sub292.accept("[");
               try {
                  Identifier(sub292, env);
               } catch(final GrinParserException e293) {
                  //continue
               }
               sub292.accept("]");
               sub292.commit();
            }
         } catch(final GrinParserException e294) {
            //continue
         }
      } catch(final GrinParserException e295) {
         //continue
      }
      sub289.commit();
      return sub289.match();
   }

   static Object Assignment(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub122 = input.sub();
      env[1] /* name */= Name(sub122, env);
      sub122.accept("=");
      S(sub122, env);
      try {
         try {
            env[8] /* stmt */= new org.fuwjin.chessur.expression.Assignment((java.lang.String)sub122.isSet("name",
                  env[1]), (org.fuwjin.chessur.expression.Expression)Value(sub122, env));
         } catch(final Exception e123) {
            throw sub122.ex(e123);
         }
      } catch(final GrinParserException e124) {
         sub122.abort(String.valueOf("assignment requires a value"), e124);
      }
      sub122.commit();
      return sub122.isSet("stmt", env[8]);
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
            env[9] /* notted */= java.lang.Boolean.TRUE;
         } catch(final Exception e74) {
            throw sub73.ex(e74);
         }
         sub73.commit();
      } catch(final GrinParserException e75) {
         final StringCursor sub76 = sub72.sub();
         try {
            env[9] /* notted */= java.lang.Boolean.FALSE;
         } catch(final Exception e77) {
            throw sub76.ex(e77);
         }
         sub76.commit();
      }
      try {
         try {
            env[8] /* stmt */= new org.fuwjin.chessur.expression.AssumeStatement((java.lang.Boolean)sub72.isSet(
                  "notted", env[9]), (org.fuwjin.chessur.expression.Expression)Value(sub72, env));
         } catch(final Exception e78) {
            throw sub72.ex(e78);
         }
      } catch(final GrinParserException e79) {
         try {
            try {
               env[8] /* stmt */= new org.fuwjin.chessur.expression.AssumeStatement((java.lang.Boolean)sub72.isSet(
                     "notted", env[9]), (org.fuwjin.chessur.expression.Expression)Statement(sub72, env));
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

   static Object Block(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub115 = input.sub();
      sub115.accept("{");
      S(sub115, env);
      try {
         env[10] /* block */= new org.fuwjin.chessur.expression.Block();
      } catch(final Exception e116) {
         throw sub115.ex(e116);
      }
      try {
         try {
            ((org.fuwjin.chessur.expression.Block)sub115.isSet("block", env[10]))
                  .add((org.fuwjin.chessur.expression.Expression)Statement(sub115, env));
         } catch(final Exception e117) {
            throw sub115.ex(e117);
         }
         try {
            while(true) {
               try {
                  ((org.fuwjin.chessur.expression.Block)sub115.isSet("block", env[10]))
                        .add((org.fuwjin.chessur.expression.Expression)Statement(sub115, env));
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

   static Object Catalog(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub1 = input.sub();
      try {
         env[0] /* cat */= new org.fuwjin.chessur.expression.CatalogImpl((java.lang.String)sub1.isSet("name", env[1]),
               (org.fuwjin.chessur.CatalogManager)sub1.isSet("manager", env[2]));
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
                  ((org.fuwjin.chessur.expression.CatalogImpl)sub1.isSet("cat", env[0]))
                        .add((org.fuwjin.chessur.expression.Declaration)ScriptDeclaration(sub1, env));
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
                        ((org.fuwjin.chessur.expression.CatalogImpl)sub1.isSet("cat", env[0]))
                              .add((org.fuwjin.chessur.expression.Declaration)ScriptDeclaration(sub1, env));
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

   static Object Comment(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub310 = input.sub();
      sub310.accept("#");
      try {
         sub310.acceptNotIn("\n\r", "\n\r");
         try {
            while(true) {
               sub310.acceptNotIn("\n\r", "\n\r");
            }
         } catch(final GrinParserException e311) {
            //continue
         }
      } catch(final GrinParserException e312) {
         //continue
      }
      try {
         sub310.accept("\r");
      } catch(final GrinParserException e313) {
         //continue
      }
      try {
         sub310.accept("\n");
      } catch(final GrinParserException e314) {
         EndOfFile(sub310, env);
      }
      sub310.commit();
      return null;
   }

   static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub93 = input.sub();
      sub93.accept("could");
      Sep(sub93, env);
      try {
         try {
            env[8] /* stmt */= new org.fuwjin.chessur.expression.CouldStatement(
                  (org.fuwjin.chessur.expression.Expression)Statement(sub93, env));
         } catch(final Exception e94) {
            throw sub93.ex(e94);
         }
      } catch(final GrinParserException e95) {
         sub93.abort(String.valueOf("could keyword requires a statement"), e95);
      }
      sub93.commit();
      return sub93.isSet("stmt", env[8]);
   }

   static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub216 = input.sub();
      sub216.accept("\"");
      try {
         env[22] /* lit */= new org.fuwjin.chessur.expression.CompositeLiteral();
      } catch(final Exception e217) {
         throw sub216.ex(e217);
      }
      try {
         try {
            final StringCursor sub218 = sub216.sub();
            sub218.accept("'");
            S(sub218, env);
            try {
               ((org.fuwjin.chessur.expression.CompositeLiteral)sub218.isSet("lit", env[22]))
                     .append((org.fuwjin.chessur.expression.Expression)Value(sub218, env));
            } catch(final Exception e219) {
               throw sub218.ex(e219);
            }
            sub218.accept("'");
            sub218.commit();
         } catch(final GrinParserException e220) {
            try {
               final StringCursor sub221 = sub216.sub();
               try {
                  ((org.fuwjin.chessur.expression.CompositeLiteral)sub221.isSet("lit", env[22]))
                        .appendChar((java.lang.Integer)Escape(sub221, env));
               } catch(final Exception e222) {
                  throw sub221.ex(e222);
               }
               sub221.commit();
            } catch(final GrinParserException e223) {
               final StringCursor sub224 = sub216.sub();
               env[21] /* ch */= sub224.next();
               sub224.acceptNotIn("\"\\", "\"\\");
               try {
                  ((org.fuwjin.chessur.expression.CompositeLiteral)sub224.isSet("lit", env[22]))
                        .appendChar((java.lang.Integer)sub224.isSet("ch", env[21]));
               } catch(final Exception e225) {
                  throw sub224.ex(e225);
               }
               sub224.commit();
            }
         }
         try {
            while(true) {
               try {
                  final StringCursor sub226 = sub216.sub();
                  sub226.accept("'");
                  S(sub226, env);
                  try {
                     ((org.fuwjin.chessur.expression.CompositeLiteral)sub226.isSet("lit", env[22]))
                           .append((org.fuwjin.chessur.expression.Expression)Value(sub226, env));
                  } catch(final Exception e227) {
                     throw sub226.ex(e227);
                  }
                  sub226.accept("'");
                  sub226.commit();
               } catch(final GrinParserException e228) {
                  try {
                     final StringCursor sub229 = sub216.sub();
                     try {
                        ((org.fuwjin.chessur.expression.CompositeLiteral)sub229.isSet("lit", env[22]))
                              .appendChar((java.lang.Integer)Escape(sub229, env));
                     } catch(final Exception e230) {
                        throw sub229.ex(e230);
                     }
                     sub229.commit();
                  } catch(final GrinParserException e231) {
                     final StringCursor sub232 = sub216.sub();
                     env[21] /* ch */= sub232.next();
                     sub232.acceptNotIn("\"\\", "\"\\");
                     try {
                        ((org.fuwjin.chessur.expression.CompositeLiteral)sub232.isSet("lit", env[22]))
                              .appendChar((java.lang.Integer)sub232.isSet("ch", env[21]));
                     } catch(final Exception e233) {
                        throw sub232.ex(e233);
                     }
                     sub232.commit();
                  }
               }
            }
         } catch(final GrinParserException e234) {
            //continue
         }
      } catch(final GrinParserException e235) {
         //continue
      }
      try {
         sub216.accept("\"");
      } catch(final GrinParserException e236) {
         sub216.abort(String.valueOf("dynamic literals must end with a double quote"), e236);
      }
      S(sub216, env);
      sub216.commit();
      return sub216.isSet("lit", env[22]);
   }

   static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub82 = input.sub();
      sub82.accept("either");
      Sep(sub82, env);
      try {
         try {
            env[8] /* stmt */= new org.fuwjin.chessur.expression.EitherOrStatement(
                  (org.fuwjin.chessur.expression.Expression)Statement(sub82, env));
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
               ((org.fuwjin.chessur.expression.EitherOrStatement)sub85.isSet("stmt", env[8]))
                     .or((org.fuwjin.chessur.expression.Expression)Statement(sub85, env));
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
                     ((org.fuwjin.chessur.expression.EitherOrStatement)sub88.isSet("stmt", env[8]))
                           .or((org.fuwjin.chessur.expression.Expression)Statement(sub88, env));
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

   static Object EndOfFile(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub315 = input.sub();
      final StringCursor sub316 = sub315.sub();
      boolean b317 = true;
      try {
         if((Object)sub316.next() == Boolean.FALSE) {
            b317 = false;
         }
      } catch(final GrinParserException e318) {
         b317 = false;
      }
      if(b317) {
         throw sub315.ex("unexpected value");
      }
      sub315.commit();
      return null;
   }

   static Object Escape(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub237 = input.sub();
      sub237.accept("\\");
      try {
         final StringCursor sub238 = sub237.sub();
         sub238.accept("n");
         try {
            env[21] /* ch */= org.fuwjin.chessur.expression.Literal.NEW_LINE;
         } catch(final Exception e239) {
            throw sub238.ex(e239);
         }
         sub238.commit();
      } catch(final GrinParserException e240) {
         try {
            final StringCursor sub241 = sub237.sub();
            sub241.accept("t");
            try {
               env[21] /* ch */= org.fuwjin.chessur.expression.Literal.TAB;
            } catch(final Exception e242) {
               throw sub241.ex(e242);
            }
            sub241.commit();
         } catch(final GrinParserException e243) {
            try {
               final StringCursor sub244 = sub237.sub();
               sub244.accept("r");
               try {
                  env[21] /* ch */= org.fuwjin.chessur.expression.Literal.RETURN;
               } catch(final Exception e245) {
                  throw sub244.ex(e245);
               }
               sub244.commit();
            } catch(final GrinParserException e246) {
               try {
                  final StringCursor sub247 = sub237.sub();
                  sub247.accept("x");
                  try {
                     env[21] /* ch */= org.fuwjin.chessur.expression.Literal.parseHex((java.lang.String)HexDigits(
                           sub247, env));
                  } catch(final Exception e248) {
                     throw sub247.ex(e248);
                  }
                  sub247.commit();
               } catch(final GrinParserException e249) {
                  final StringCursor sub250 = sub237.sub();
                  env[21] /* ch */= sub250.next();
                  sub250.accept();
                  sub250.commit();
               }
            }
         }
      }
      sub237.commit();
      return sub237.isSet("ch", env[21]);
   }

   static Object Field(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub177 = input.sub();
      env[1] /* name */= Name(sub177, env);
      try {
         env[18] /* setter */= ((org.fuwjin.dinah.FunctionProvider)sub177.isSet("manager", env[2]))
               .forName(
                     (java.lang.String)String.valueOf(sub177.isSet("type", env[15])) + String.valueOf(".")
                           + String.valueOf(sub177.isSet("name", env[1]))).withArgCount((java.lang.Integer)2)
               .function();
      } catch(final Exception e178) {
         throw sub177.ex(e178);
      }
      sub177.accept(":");
      S(sub177, env);
      try {
         ((org.fuwjin.chessur.expression.ObjectTemplate)sub177.isSet("object", env[17])).set(
               (java.lang.String)sub177.isSet("name", env[1]),
               (org.fuwjin.dinah.Function)sub177.isSet("setter", env[18]),
               (org.fuwjin.chessur.expression.Expression)Value(sub177, env));
      } catch(final Exception e179) {
         throw sub177.ex(e179);
      }
      sub177.commit();
      return null;
   }

   static Object FilterChar(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub197 = input.sub();
      try {
         final StringCursor sub198 = sub197.sub();
         env[21] /* ch */= Escape(sub198, env);
         sub198.commit();
      } catch(final GrinParserException e199) {
         final StringCursor sub200 = sub197.sub();
         env[21] /* ch */= sub200.next();
         sub200.acceptNot("\\");
         sub200.commit();
      }
      sub197.commit();
      return sub197.isSet("ch", env[21]);
   }

   static Object FilterRange(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub191 = input.sub();
      env[20] /* start */= FilterChar(sub191, env);
      S(sub191, env);
      try {
         final StringCursor sub192 = sub191.sub();
         sub192.accept("-");
         S(sub192, env);
         try {
            ((org.fuwjin.chessur.expression.Filter)sub192.isSet("filter", env[19])).addRange(
                  (java.lang.Integer)sub192.isSet("start", env[20]), (java.lang.Integer)FilterChar(sub192, env));
         } catch(final Exception e193) {
            throw sub192.ex(e193);
         }
         S(sub192, env);
         sub192.commit();
      } catch(final GrinParserException e194) {
         final StringCursor sub195 = sub191.sub();
         try {
            ((org.fuwjin.chessur.expression.Filter)sub195.isSet("filter", env[19])).addChar((java.lang.Integer)sub195
                  .isSet("start", env[20]));
         } catch(final Exception e196) {
            throw sub195.ex(e196);
         }
         sub195.commit();
      }
      sub191.commit();
      return null;
   }

   static Object HexDigit(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub252 = input.sub();
      sub252.acceptIn("0-9A-Fa-f", "0123456789ABCDEFabcdef");
      sub252.commit();
      return null;
   }

   static Object HexDigits(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub251 = input.sub();
      HexDigit(sub251, env);
      HexDigit(sub251, env);
      HexDigit(sub251, env);
      HexDigit(sub251, env);
      sub251.commit();
      return sub251.match();
   }

   static Object Identifier(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub296 = input.sub();
      IdentifierChar(sub296, env);
      try {
         while(true) {
            IdentifierChar(sub296, env);
         }
      } catch(final GrinParserException e297) {
         //continue
      }
      sub296.commit();
      return sub296.match();
   }

   static Object IdentifierChar(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub298 = input.sub();
      final StringCursor sub299 = sub298.sub();
      if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub299.next()) == Boolean.FALSE) {
         throw sub299.ex("check failed");
      }
      sub298.accept();
      sub298.commit();
      return null;
   }

   static Object InFilter(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub182 = input.sub();
      sub182.accept("in");
      Sep(sub182, env);
      try {
         env[19] /* filter */= new org.fuwjin.chessur.expression.Filter();
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

   static Object Invocation(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub125 = input.sub();
      env[1] /* name */= AliasName(sub125, env);
      sub125.accept("(");
      S(sub125, env);
      try {
         env[11] /* inv */= new org.fuwjin.chessur.expression.Invocation();
      } catch(final Exception e126) {
         throw sub125.ex(e126);
      }
      try {
         final StringCursor sub127 = sub125.sub();
         try {
            ((org.fuwjin.chessur.expression.Invocation)sub127.isSet("inv", env[11]))
                  .addParam((org.fuwjin.chessur.expression.Expression)Value(sub127, env));
         } catch(final Exception e128) {
            throw sub127.ex(e128);
         }
         try {
            final StringCursor sub129 = sub127.sub();
            sub129.accept(",");
            S(sub129, env);
            try {
               try {
                  ((org.fuwjin.chessur.expression.Invocation)sub129.isSet("inv", env[11]))
                        .addParam((org.fuwjin.chessur.expression.Expression)Value(sub129, env));
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
                        ((org.fuwjin.chessur.expression.Invocation)sub132.isSet("inv", env[11]))
                              .addParam((org.fuwjin.chessur.expression.Expression)Value(sub132, env));
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
            env[5] /* signature */= ((org.fuwjin.chessur.expression.CatalogImpl)sub139.isSet("cat", env[0]))
                  .getSignature((java.lang.String)sub139.isSet("name", env[1]),
                        (java.lang.Integer)((org.fuwjin.chessur.expression.Invocation)sub139.isSet("inv", env[11]))
                              .paramCount());
         } catch(final Exception e140) {
            throw sub139.ex(e140);
         }
         try {
            env[12] /* function */= ((org.fuwjin.dinah.FunctionProvider)sub139.isSet("manager", env[2]))
                  .getFunction((org.fuwjin.dinah.SignatureConstraint)sub139.isSet("signature", env[5]));
         } catch(final Exception e141) {
            throw sub139.ex(e141);
         }
         try {
            ((org.fuwjin.chessur.expression.Invocation)sub139.isSet("inv", env[11]))
                  .setFunction((org.fuwjin.dinah.Function)sub139.isSet("function", env[12]));
         } catch(final Exception e142) {
            throw sub139.ex(e142);
         }
         sub139.commit();
      } catch(final GrinParserException e143) {
         sub125.abort(String.valueOf("Could not get function for ") + String.valueOf(sub125.isSet("name", env[1])),
               e143);
      }
      sub125.commit();
      return sub125.isSet("inv", env[11]);
   }

   static Object LoadDeclaration(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub11 = input.sub();
      sub11.accept("load");
      Sep(sub11, env);
      try {
         env[3] /* path */= PathName(sub11, env);
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
            ((org.fuwjin.chessur.expression.CatalogImpl)sub11.isSet("cat", env[0])).load(
                  (java.lang.String)sub11.isSet("path", env[3]), (java.lang.String)Name(sub11, env));
         } catch(final Exception e14) {
            throw sub11.ex(e14);
         }
      } catch(final GrinParserException e15) {
         sub11.abort(String.valueOf("load-as keywords require a name"), e15);
      }
      sub11.commit();
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

   static Object Name(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub282 = input.sub();
      env[13] /* id */= Identifier(sub282, env);
      S(sub282, env);
      sub282.commit();
      return sub282.isSet("id", env[13]);
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

   static Object Number(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub253 = input.sub();
      try {
         sub253.accept("-");
      } catch(final GrinParserException e254) {
         //continue
      }
      try {
         final StringCursor sub255 = sub253.sub();
         sub255.acceptIn("0-9", "0123456789");
         try {
            while(true) {
               sub255.acceptIn("0-9", "0123456789");
            }
         } catch(final GrinParserException e256) {
            //continue
         }
         try {
            final StringCursor sub257 = sub255.sub();
            sub257.accept(".");
            try {
               sub257.acceptIn("0-9", "0123456789");
               try {
                  while(true) {
                     sub257.acceptIn("0-9", "0123456789");
                  }
               } catch(final GrinParserException e258) {
                  //continue
               }
            } catch(final GrinParserException e259) {
               //continue
            }
            sub257.commit();
         } catch(final GrinParserException e260) {
            //continue
         }
         sub255.commit();
      } catch(final GrinParserException e261) {
         final StringCursor sub262 = sub253.sub();
         sub262.accept(".");
         sub262.acceptIn("0-9", "0123456789");
         try {
            while(true) {
               sub262.acceptIn("0-9", "0123456789");
            }
         } catch(final GrinParserException e263) {
            //continue
         }
         sub262.commit();
      }
      try {
         final StringCursor sub264 = sub253.sub();
         sub264.acceptIn("Ee", "Ee");
         try {
            sub264.accept("-");
         } catch(final GrinParserException e265) {
            //continue
         }
         sub264.acceptIn("0-9", "0123456789");
         try {
            while(true) {
               sub264.acceptIn("0-9", "0123456789");
            }
         } catch(final GrinParserException e266) {
            //continue
         }
         sub264.commit();
      } catch(final GrinParserException e267) {
         //continue
      }
      try {
         env[23] /* num */= new org.fuwjin.chessur.expression.Number((java.lang.String)sub253.match());
      } catch(final Exception e268) {
         throw sub253.ex(e268);
      }
      Sep(sub253, env);
      sub253.commit();
      return sub253.isSet("num", env[23]);
   }

   static Object Object(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub168 = input.sub();
      sub168.accept("(");
      S(sub168, env);
      env[15] /* type */= AliasName(sub168, env);
      try {
         env[16] /* constructor */= ((org.fuwjin.dinah.FunctionProvider)sub168.isSet("manager", env[2]))
               .forName((java.lang.String)String.valueOf(sub168.isSet("type", env[15])) + String.valueOf(".new"))
               .withArgCount((java.lang.Integer)0).function();
      } catch(final Exception e169) {
         throw sub168.ex(e169);
      }
      try {
         env[17] /* object */= new org.fuwjin.chessur.expression.ObjectTemplate((java.lang.String)sub168.isSet("type",
               env[15]), (org.fuwjin.dinah.Function)sub168.isSet("constructor", env[16]));
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

   static Object Path(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub269 = input.sub();
      final StringCursor sub270 = sub269.sub();
      try {
         sub270.accept("/");
      } catch(final GrinParserException e271) {
         //continue
      }
      QualifiedIdentifier(sub270, env);
      sub270.commit();
      try {
         while(true) {
            final StringCursor sub272 = sub269.sub();
            try {
               sub272.accept("/");
            } catch(final GrinParserException e273) {
               //continue
            }
            QualifiedIdentifier(sub272, env);
            sub272.commit();
         }
      } catch(final GrinParserException e274) {
         //continue
      }
      try {
         sub269.accept("/");
      } catch(final GrinParserException e275) {
         //continue
      }
      sub269.commit();
      return sub269.match();
   }

   static Object PathName(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub276 = input.sub();
      env[3] /* path */= Path(sub276, env);
      S(sub276, env);
      sub276.commit();
      return sub276.isSet("path", env[3]);
   }

   static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub109 = input.sub();
      sub109.accept("publish");
      Sep(sub109, env);
      try {
         try {
            env[8] /* stmt */= new org.fuwjin.chessur.expression.PublishStatement(
                  (org.fuwjin.chessur.expression.Expression)Value(sub109, env));
         } catch(final Exception e110) {
            throw sub109.ex(e110);
         }
      } catch(final GrinParserException e111) {
         sub109.abort(String.valueOf("publish keyword requires a value"), e111);
      }
      sub109.commit();
      return sub109.isSet("stmt", env[8]);
   }

   static Object QualifiedIdentifier(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub284 = input.sub();
      AnnotatedIdentifier(sub284, env);
      try {
         final StringCursor sub285 = sub284.sub();
         sub285.accept(".");
         AnnotatedIdentifier(sub285, env);
         sub285.commit();
         try {
            while(true) {
               final StringCursor sub286 = sub284.sub();
               sub286.accept(".");
               AnnotatedIdentifier(sub286, env);
               sub286.commit();
            }
         } catch(final GrinParserException e287) {
            //continue
         }
      } catch(final GrinParserException e288) {
         //continue
      }
      sub284.commit();
      return sub284.match();
   }

   static Object QualifiedName(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub283 = input.sub();
      env[13] /* id */= QualifiedIdentifier(sub283, env);
      S(sub283, env);
      sub283.commit();
      return sub283.isSet("id", env[13]);
   }

   static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub96 = input.sub();
      sub96.accept("repeat");
      Sep(sub96, env);
      try {
         try {
            env[8] /* stmt */= new org.fuwjin.chessur.expression.RepeatStatement(
                  (org.fuwjin.chessur.expression.Expression)Statement(sub96, env));
         } catch(final Exception e97) {
            throw sub96.ex(e97);
         }
      } catch(final GrinParserException e98) {
         sub96.abort(String.valueOf("repeat keyword requires a statement"), e98);
      }
      sub96.commit();
      return sub96.isSet("stmt", env[8]);
   }

   static Object S(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub304 = input.sub();
      try {
         Space(sub304, env);
      } catch(final GrinParserException e305) {
         //continue
      }
      sub304.commit();
      return null;
   }

   static Object Script(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub144 = input.sub();
      env[6] /* script */= ScriptIdent(sub144, env);
      try {
         final StringCursor sub145 = sub144.sub();
         sub145.accept("<<");
         S(sub145, env);
         try {
            env[6] /* script */= new org.fuwjin.chessur.expression.ScriptInput(
                  (org.fuwjin.chessur.expression.Expression)sub145.isSet("script", env[6]),
                  (org.fuwjin.chessur.expression.Expression)Value(sub145, env));
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
            env[6] /* script */= new org.fuwjin.chessur.expression.ScriptPipe(
                  (org.fuwjin.chessur.expression.Expression)sub148.isSet("script", env[6]),
                  (org.fuwjin.chessur.expression.Expression)ScriptIdent(sub148, env));
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
                  env[6] /* script */= new org.fuwjin.chessur.expression.ScriptPipe(
                        (org.fuwjin.chessur.expression.Expression)sub150.isSet("script", env[6]),
                        (org.fuwjin.chessur.expression.Expression)ScriptIdent(sub150, env));
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
            env[6] /* script */= new org.fuwjin.chessur.expression.ScriptOutput(
                  (org.fuwjin.chessur.expression.Expression)sub154.isSet("script", env[6]), (java.lang.String)Name(
                        sub154, env));
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

   static Object ScriptDeclaration(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub36 = input.sub();
      sub36.accept("<");
      try {
         env[1] /* name */= Identifier(sub36, env);
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
         env[6] /* script */= new org.fuwjin.chessur.expression.Declaration((java.lang.String)sub36.isSet("name",
               env[1]));
      } catch(final Exception e40) {
         throw sub36.ex(e40);
      }
      try {
         try {
            ((org.fuwjin.chessur.expression.Declaration)sub36.isSet("script", env[6]))
                  .add((org.fuwjin.chessur.expression.Expression)Statement(sub36, env));
         } catch(final Exception e41) {
            throw sub36.ex(e41);
         }
         try {
            while(true) {
               try {
                  ((org.fuwjin.chessur.expression.Declaration)sub36.isSet("script", env[6]))
                        .add((org.fuwjin.chessur.expression.Expression)Statement(sub36, env));
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
               ((org.fuwjin.chessur.expression.Declaration)sub45.isSet("script", env[6]))
                     .returns((org.fuwjin.chessur.expression.Expression)Value(sub45, env));
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
         sub36.abort(
               String.valueOf("script declaration for ") + String.valueOf(sub36.isSet("name", env[1]))
                     + String.valueOf(" must end with a brace"), e49);
      }
      S(sub36, env);
      sub36.commit();
      return sub36.isSet("script", env[6]);
   }

   static Object ScriptIdent(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub157 = input.sub();
      sub157.accept("<");
      try {
         env[13] /* id */= Identifier(sub157, env);
      } catch(final GrinParserException e158) {
         sub157.abort(String.valueOf("script identifiers must be enclosed in angle brackets"), e158);
      }
      try {
         final StringCursor sub159 = sub157.sub();
         sub159.accept(":");
         try {
            final StringCursor sub160 = sub159.sub();
            env[1] /* name */= Identifier(sub160, env);
            try {
               env[14] /* module */= ((org.fuwjin.chessur.expression.CatalogImpl)sub160.isSet("cat", env[0]))
                     .getModule((java.lang.String)sub160.isSet("id", env[13]));
            } catch(final Exception e161) {
               throw sub160.ex(e161);
            }
            try {
               env[6] /* script */= ((org.fuwjin.chessur.Module)sub160.isSet("module", env[14]))
                     .get((java.lang.String)sub160.isSet("name", env[1]));
            } catch(final Exception e162) {
               throw sub160.ex(e162);
            }
            sub160.commit();
         } catch(final GrinParserException e163) {
            sub159.abort(
                  String.valueOf("namespaced script ") + String.valueOf(sub159.isSet("id", env[13]))
                        + String.valueOf(": could not be resolved"), e163);
         }
         sub159.commit();
      } catch(final GrinParserException e164) {
         final StringCursor sub165 = sub157.sub();
         try {
            env[6] /* script */= ((org.fuwjin.chessur.Module)sub165.isSet("cat", env[0])).get((java.lang.String)sub165
                  .isSet("id", env[13]));
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

   static Object Sep(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub300 = input.sub();
      final StringCursor sub301 = sub300.sub();
      boolean b302 = true;
      try {
         if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub301.next()) == Boolean.FALSE) {
            b302 = false;
         }
      } catch(final GrinParserException e303) {
         b302 = false;
      }
      if(b302) {
         throw sub300.ex("unexpected value");
      }
      S(sub300, env);
      sub300.commit();
      return null;
   }

   static Object Space(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub306 = input.sub();
      try {
         sub306.acceptIn("\t-\n\r ", "\t\n\r ");
      } catch(final GrinParserException e307) {
         Comment(sub306, env);
      }
      try {
         while(true) {
            try {
               sub306.acceptIn("\t-\n\r ", "\t\n\r ");
            } catch(final GrinParserException e308) {
               Comment(sub306, env);
            }
         }
      } catch(final GrinParserException e309) {
         //continue
      }
      sub306.commit();
      return null;
   }

   static Object Statement(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub61 = input.sub();
      try {
         env[8] /* stmt */= AssumeStatement(sub61, env);
      } catch(final GrinParserException e62) {
         try {
            env[8] /* stmt */= EitherOrStatement(sub61, env);
         } catch(final GrinParserException e63) {
            try {
               env[8] /* stmt */= CouldStatement(sub61, env);
            } catch(final GrinParserException e64) {
               try {
                  env[8] /* stmt */= RepeatStatement(sub61, env);
               } catch(final GrinParserException e65) {
                  try {
                     env[8] /* stmt */= AcceptStatement(sub61, env);
                  } catch(final GrinParserException e66) {
                     try {
                        env[8] /* stmt */= PublishStatement(sub61, env);
                     } catch(final GrinParserException e67) {
                        try {
                           env[8] /* stmt */= AbortStatement(sub61, env);
                        } catch(final GrinParserException e68) {
                           try {
                              env[8] /* stmt */= Script(sub61, env);
                           } catch(final GrinParserException e69) {
                              try {
                                 env[8] /* stmt */= Block(sub61, env);
                              } catch(final GrinParserException e70) {
                                 try {
                                    env[8] /* stmt */= Assignment(sub61, env);
                                 } catch(final GrinParserException e71) {
                                    env[8] /* stmt */= Invocation(sub61, env);
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

   static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub201 = input.sub();
      sub201.accept("'");
      try {
         env[22] /* lit */= new org.fuwjin.chessur.expression.Literal();
      } catch(final Exception e202) {
         throw sub201.ex(e202);
      }
      try {
         try {
            final StringCursor sub203 = sub201.sub();
            env[21] /* ch */= sub203.next();
            sub203.acceptNotIn("'\\", "'\\");
            try {
               ((org.fuwjin.chessur.expression.Literal)sub203.isSet("lit", env[22])).append((java.lang.Integer)sub203
                     .isSet("ch", env[21]));
            } catch(final Exception e204) {
               throw sub203.ex(e204);
            }
            sub203.commit();
         } catch(final GrinParserException e205) {
            final StringCursor sub206 = sub201.sub();
            try {
               ((org.fuwjin.chessur.expression.Literal)sub206.isSet("lit", env[22])).append((java.lang.Integer)Escape(
                     sub206, env));
            } catch(final Exception e207) {
               throw sub206.ex(e207);
            }
            sub206.commit();
         }
         try {
            while(true) {
               try {
                  final StringCursor sub208 = sub201.sub();
                  env[21] /* ch */= sub208.next();
                  sub208.acceptNotIn("'\\", "'\\");
                  try {
                     ((org.fuwjin.chessur.expression.Literal)sub208.isSet("lit", env[22]))
                           .append((java.lang.Integer)sub208.isSet("ch", env[21]));
                  } catch(final Exception e209) {
                     throw sub208.ex(e209);
                  }
                  sub208.commit();
               } catch(final GrinParserException e210) {
                  final StringCursor sub211 = sub201.sub();
                  try {
                     ((org.fuwjin.chessur.expression.Literal)sub211.isSet("lit", env[22]))
                           .append((java.lang.Integer)Escape(sub211, env));
                  } catch(final Exception e212) {
                     throw sub211.ex(e212);
                  }
                  sub211.commit();
               }
            }
         } catch(final GrinParserException e213) {
            //continue
         }
      } catch(final GrinParserException e214) {
         //continue
      }
      try {
         sub201.accept("'");
      } catch(final GrinParserException e215) {
         sub201.abort(String.valueOf("static literals must end with a quote"), e215);
      }
      S(sub201, env);
      sub201.commit();
      return sub201.isSet("lit", env[22]);
   }

   static Object Value(final StringCursor input, final Object... parentEnv) throws GrinParserException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub50 = input.sub();
      try {
         env[7] /* val */= StaticLiteral(sub50, env);
      } catch(final GrinParserException e51) {
         try {
            env[7] /* val */= DynamicLiteral(sub50, env);
         } catch(final GrinParserException e52) {
            try {
               env[7] /* val */= Script(sub50, env);
            } catch(final GrinParserException e53) {
               try {
                  env[7] /* val */= AcceptStatement(sub50, env);
               } catch(final GrinParserException e54) {
                  try {
                     env[7] /* val */= Invocation(sub50, env);
                  } catch(final GrinParserException e55) {
                     try {
                        env[7] /* val */= Number(sub50, env);
                     } catch(final GrinParserException e56) {
                        try {
                           env[7] /* val */= Object(sub50, env);
                        } catch(final GrinParserException e57) {
                           try {
                              env[7] /* val */= MatchValue(sub50, env);
                           } catch(final GrinParserException e58) {
                              try {
                                 env[7] /* val */= NextValue(sub50, env);
                              } catch(final GrinParserException e59) {
                                 try {
                                    env[7] /* val */= new org.fuwjin.chessur.expression.Variable(
                                          (java.lang.String)Name(sub50, env));
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
}
