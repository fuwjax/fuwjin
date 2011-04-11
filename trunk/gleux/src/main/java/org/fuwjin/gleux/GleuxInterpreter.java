package org.fuwjin.gleux;

import java.util.Map;

public class GleuxInterpreter {
   public static class GleuxException extends Exception {
      private GleuxException(final String message) {
         super(message);
      }

      private GleuxException(final String message, final Throwable cause) {
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

      public StringCursor(final CharSequence seq) {
         this(0, 1, 0, seq, null);
      }

      public StringCursor(final int start, final int line, final int column, final CharSequence seq,
            final StringCursor parent) {
         this.start = start;
         pos = start;
         this.seq = seq;
         this.parent = parent;
         this.line = line;
         this.column = column;
      }

      public int accept() throws GleuxException {
         checkBounds(pos);
         return advance();
      }

      public int accept(final String expected) throws GleuxException {
         if(expected == null || expected.length() == 0) {
            throw ex("UNSET");
         }
         checkBounds(pos + expected.length() - 1);
         final CharSequence sub = seq.subSequence(pos, pos + expected.length());
         if(!sub.equals(expected)) {
            throw ex("unexpected character " + sub + "  " + expected);
         }
         final int stop = pos + expected.length() - 1;
         while(pos < stop) {
            advance();
         }
         return advance();
      }

      public int acceptIn(final String set) throws GleuxException {
         checkBounds(pos);
         if(set.indexOf(seq.charAt(pos)) < 0) {
            throw ex("unexpected character");
         }
         return advance();
      }

      public int acceptNot(final String expected) throws GleuxException {
         if(expected == null || expected.length() == 0) {
            throw ex("UNSET");
         }
         checkBounds(pos + expected.length() - 1);
         if(seq.subSequence(pos, pos + expected.length()).equals(expected)) {
            throw ex("unexpected character");
         }
         return advance();
      }

      public int acceptNotIn(final String set) throws GleuxException {
         checkBounds(pos);
         if(set.indexOf(seq.charAt(pos)) >= 0) {
            throw ex("unexpected character");
         }
         return advance();
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

      protected void checkBounds(final int p) throws GleuxException {
         if(p >= seq.length()) {
            throw ex("unexpected EOF");
         }
      }

      public void commit() {
         parent.pos = pos;
         parent.line = line;
         parent.column = column;
      }

      public GleuxException ex(final String message) {
         return new GleuxException("[" + line + "," + column + "] " + message);
      }

      public GleuxException ex(final Throwable cause) {
         return new GleuxException("[" + line + "," + column + "]", cause);
      }

      public String match() {
         return seq.subSequence(start, pos).toString();
      }

      public int next() throws GleuxException {
         checkBounds(pos);
         return seq.charAt(pos);
      }

      public StringCursor sub() {
         return new StringCursor(pos, line, column, seq, this);
      }
   }

   private static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub192 = input.sub();
      sub192.accept("abort");
      Sep(sub192, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.AbortStatement((org.fuwjin.gleux.Expression)Value(sub192, env));
      } catch(final GleuxException e193) {
         throw new RuntimeException("abort keyword requires a value", e193);
      }
      sub192.commit();
      return env[7]/* stmt */;
   }

   private static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub124 = input.sub();
      sub124.accept("accept");
      Sep(sub124, env);
      try {
         final StringCursor sub125 = sub124.sub();
         sub125.accept("not");
         Sep(sub125, env);
         env[11] /* notted */= java.lang.Boolean.TRUE;
         sub125.commit();
      } catch(final GleuxException e126) {
         final StringCursor sub127 = sub124.sub();
         env[11] /* notted */= java.lang.Boolean.FALSE;
         sub127.commit();
      }
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.FilterAcceptStatement((java.lang.Boolean)env[11]/* notted */,
               (org.fuwjin.gleux.Filter)InFilter(sub124, env));
      } catch(final GleuxException e128) {
         try {
            env[7] /* stmt */= new org.fuwjin.gleux.ValueAcceptStatement((java.lang.Boolean)env[11]/* notted */,
                  (org.fuwjin.gleux.Expression)Value(sub124, env));
         } catch(final GleuxException e129) {
            throw new RuntimeException("accept keyword requires a value or in keyword", e129);
         }
      }
      sub124.commit();
      return env[7]/* stmt */;
   }

   private static Object AliasDeclaration(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub8 = input.sub();
      sub8.accept("alias");
      Sep(sub8, env);
      try {
         env[2] /* qname */= QualifiedName(sub8, env);
      } catch(final GleuxException e9) {
         throw new RuntimeException("alias keyword requires a qualified name", e9);
      }
      try {
         final StringCursor sub10 = sub8.sub();
         sub10.accept("as");
         Sep(sub10, env);
         try {
            try {
               ((org.fuwjin.gleux.Gleux)env[1]/* gleux */).alias((java.lang.String)env[2]/* qname */,
                     (java.lang.String)Name(sub10, env));
            } catch(final Exception e11) {
               throw sub10.ex(e11);
            }
         } catch(final GleuxException e12) {
            throw new RuntimeException("alias-as keywords require a name", e12);
         }
         sub10.commit();
      } catch(final GleuxException e13) {
         try {
            final StringCursor sub14 = sub8.sub();
            sub14.accept("(");
            S(sub14, env);
            env[3] /* signature */= new org.fuwjin.dinah.FunctionSignature((java.lang.String)env[2]/* qname */);
            try {
               final StringCursor sub15 = sub14.sub();
               try {
                  ((org.fuwjin.dinah.FunctionSignature)env[3]/* signature */).addArg((java.lang.String)QualifiedName(
                        sub15, env));
               } catch(final Exception e16) {
                  throw sub15.ex(e16);
               }
               try {
                  final StringCursor sub17 = sub15.sub();
                  sub17.accept(",");
                  S(sub17, env);
                  try {
                     ((org.fuwjin.dinah.FunctionSignature)env[3]/* signature */)
                           .addArg((java.lang.String)QualifiedName(sub17, env));
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
                           ((org.fuwjin.dinah.FunctionSignature)env[3]/* signature */)
                                 .addArg((java.lang.String)QualifiedName(sub19, env));
                        } catch(final Exception e20) {
                           throw sub19.ex(e20);
                        }
                        sub19.commit();
                     }
                  } catch(final GleuxException e21) {
                     // continue
                  }
               } catch(final GleuxException e22) {
                  // continue
               }
               sub15.commit();
            } catch(final GleuxException e23) {
               // continue
            }
            sub14.accept(")");
            S(sub14, env);
            sub14.accept("as");
            Sep(sub14, env);
            try {
               try {
                  ((org.fuwjin.gleux.Gleux)env[1]/* gleux */).aliasSignature(
                        (org.fuwjin.dinah.FunctionSignature)env[3]/* signature */, (java.lang.String)Name(sub14, env));
               } catch(final Exception e24) {
                  throw sub14.ex(e24);
               }
            } catch(final GleuxException e25) {
               throw new RuntimeException("alias-as keywords require a name", e25);
            }
            sub14.commit();
         } catch(final GleuxException e26) {
            throw new RuntimeException("alias keyword requires as keyword", e26);
         }
      }
      sub8.commit();
      return null;
   }

   private static Object AliasName(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub210 = input.sub();
      env[21] /* prefix */= Identifier(sub210, env);
      env[22] /* alias */= ((org.fuwjin.gleux.Gleux)env[1]/* gleux */).alias((java.lang.String)env[21]/* prefix */);
      try {
         final StringCursor sub211 = sub210.sub();
         sub211.accept(".");
         env[4] /* name */= env[22]/* alias */+ "." + QualifiedName(sub211, env);
         sub211.commit();
      } catch(final GleuxException e212) {
         final StringCursor sub213 = sub210.sub();
         env[4] /* name */= env[22]/* alias */;
         sub213.commit();
      }
      sub210.commit();
      return env[4]/* name */;
   }

   private static Object AnnotatedIdentifier(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub255 = input.sub();
      Identifier(sub255, env);
      try {
         final StringCursor sub256 = sub255.sub();
         sub256.accept("[");
         try {
            Identifier(sub256, env);
         } catch(final GleuxException e257) {
            // continue
         }
         sub256.accept("]");
         sub256.commit();
         try {
            while(true) {
               final StringCursor sub258 = sub255.sub();
               sub258.accept("[");
               try {
                  Identifier(sub258, env);
               } catch(final GleuxException e259) {
                  // continue
               }
               sub258.accept("]");
               sub258.commit();
            }
         } catch(final GleuxException e260) {
            // continue
         }
      } catch(final GleuxException e261) {
         // continue
      }
      sub255.commit();
      return null;
   }

   private static Object Assignment(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub200 = input.sub();
      env[4] /* name */= Name(sub200, env);
      sub200.accept("=");
      S(sub200, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.Assignment((java.lang.String)env[4]/* name */,
               (org.fuwjin.gleux.Expression)Value(sub200, env));
      } catch(final GleuxException e201) {
         throw new RuntimeException("assignment requires a value", e201);
      }
      sub200.commit();
      return env[7]/* stmt */;
   }

   private static Object Block(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub194 = input.sub();
      sub194.accept("{");
      S(sub194, env);
      env[19] /* block */= new org.fuwjin.gleux.Block();
      try {
         try {
            ((org.fuwjin.gleux.Block)env[19]/* block */).add((org.fuwjin.gleux.Expression)Statement(sub194, env));
         } catch(final Exception e195) {
            throw sub194.ex(e195);
         }
         try {
            while(true) {
               try {
                  ((org.fuwjin.gleux.Block)env[19]/* block */).add((org.fuwjin.gleux.Expression)Statement(sub194, env));
               } catch(final Exception e196) {
                  throw sub194.ex(e196);
               }
            }
         } catch(final GleuxException e197) {
            // continue
         }
      } catch(final GleuxException e198) {
         // continue
      }
      try {
         sub194.accept("}");
      } catch(final GleuxException e199) {
         throw new RuntimeException("block must end with a brace", e199);
      }
      S(sub194, env);
      sub194.commit();
      return env[19]/* block */;
   }

   private static Object Comment(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub268 = input.sub();
      sub268.accept("#");
      try {
         sub268.acceptNotIn("\n\r");
         try {
            while(true) {
               sub268.acceptNotIn("\n\r");
            }
         } catch(final GleuxException e269) {
            // continue
         }
      } catch(final GleuxException e270) {
         // continue
      }
      try {
         sub268.accept("\r");
      } catch(final GleuxException e271) {
         // continue
      }
      try {
         sub268.accept("\n");
      } catch(final GleuxException e272) {
         EndOfFile(sub268, env);
      }
      sub268.commit();
      return null;
   }

   private static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub186 = input.sub();
      sub186.accept("could");
      Sep(sub186, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.CouldStatement((org.fuwjin.gleux.Expression)Statement(sub186, env));
      } catch(final GleuxException e187) {
         throw new RuntimeException("could keyword requires a statement", e187);
      }
      sub186.commit();
      return env[7]/* stmt */;
   }

   private static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub95 = input.sub();
      sub95.accept("\"");
      env[9] /* lit */= new org.fuwjin.gleux.CompositeLiteral();
      try {
         try {
            final StringCursor sub96 = sub95.sub();
            sub96.accept("'");
            S(sub96, env);
            try {
               ((org.fuwjin.gleux.CompositeLiteral)env[9]/* lit */).append((org.fuwjin.gleux.Expression)Value(sub96,
                     env));
            } catch(final Exception e97) {
               throw sub96.ex(e97);
            }
            sub96.accept("'");
            sub96.commit();
         } catch(final GleuxException e98) {
            try {
               final StringCursor sub99 = sub95.sub();
               try {
                  ((org.fuwjin.gleux.CompositeLiteral)env[9]/* lit */)
                        .appendChar((java.lang.Integer)Escape(sub99, env));
               } catch(final Exception e100) {
                  throw sub99.ex(e100);
               }
               sub99.commit();
            } catch(final GleuxException e101) {
               final StringCursor sub102 = sub95.sub();
               env[10] /* ch */= sub102.acceptNotIn("\"\\");
               try {
                  ((org.fuwjin.gleux.CompositeLiteral)env[9]/* lit */).appendChar((java.lang.Integer)env[10]/* ch */);
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
                     ((org.fuwjin.gleux.CompositeLiteral)env[9]/* lit */).append((org.fuwjin.gleux.Expression)Value(
                           sub104, env));
                  } catch(final Exception e105) {
                     throw sub104.ex(e105);
                  }
                  sub104.accept("'");
                  sub104.commit();
               } catch(final GleuxException e106) {
                  try {
                     final StringCursor sub107 = sub95.sub();
                     try {
                        ((org.fuwjin.gleux.CompositeLiteral)env[9]/* lit */).appendChar((java.lang.Integer)Escape(
                              sub107, env));
                     } catch(final Exception e108) {
                        throw sub107.ex(e108);
                     }
                     sub107.commit();
                  } catch(final GleuxException e109) {
                     final StringCursor sub110 = sub95.sub();
                     env[10] /* ch */= sub110.acceptNotIn("\"\\");
                     try {
                        ((org.fuwjin.gleux.CompositeLiteral)env[9]/* lit */)
                              .appendChar((java.lang.Integer)env[10]/* ch */);
                     } catch(final Exception e111) {
                        throw sub110.ex(e111);
                     }
                     sub110.commit();
                  }
               }
            }
         } catch(final GleuxException e112) {
            // continue
         }
      } catch(final GleuxException e113) {
         // continue
      }
      try {
         sub95.accept("\"");
      } catch(final GleuxException e114) {
         throw new RuntimeException("dynamic literals must end with a double quote", e114);
      }
      S(sub95, env);
      sub95.commit();
      return env[9]/* lit */;
   }

   private static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub176 = input.sub();
      sub176.accept("either");
      Sep(sub176, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.EitherOrStatement((org.fuwjin.gleux.Expression)Statement(sub176, env));
      } catch(final GleuxException e177) {
         throw new RuntimeException("either keyword requires a statement", e177);
      }
      try {
         final StringCursor sub178 = sub176.sub();
         sub178.accept("or");
         Sep(sub178, env);
         try {
            try {
               ((org.fuwjin.gleux.EitherOrStatement)env[7]/* stmt */).or((org.fuwjin.gleux.Expression)Statement(sub178,
                     env));
            } catch(final Exception e179) {
               throw sub178.ex(e179);
            }
         } catch(final GleuxException e180) {
            throw new RuntimeException("or keyword requires a statement", e180);
         }
         sub178.commit();
         try {
            while(true) {
               final StringCursor sub181 = sub176.sub();
               sub181.accept("or");
               Sep(sub181, env);
               try {
                  try {
                     ((org.fuwjin.gleux.EitherOrStatement)env[7]/* stmt */).or((org.fuwjin.gleux.Expression)Statement(
                           sub181, env));
                  } catch(final Exception e182) {
                     throw sub181.ex(e182);
                  }
               } catch(final GleuxException e183) {
                  throw new RuntimeException("or keyword requires a statement", e183);
               }
               sub181.commit();
            }
         } catch(final GleuxException e184) {
            // continue
         }
      } catch(final GleuxException e185) {
         throw new RuntimeException("either keyword requires at least one or keyword", e185);
      }
      sub176.commit();
      return env[7]/* stmt */;
   }

   private static Object EndOfFile(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub40 = input.sub();
      final StringCursor sub41 = sub40.sub();
      boolean b = true;
      try {
         if((Object)sub41.next() == Boolean.FALSE) {
            b = false;
         }
      } catch(final GleuxException e42) {
         b = false;
      }
      if(b) {
         throw sub41.ex("unexpected value");
      }
      sub40.commit();
      return null;
   }

   private static Object Escape(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub231 = input.sub();
      sub231.accept("\\");
      try {
         final StringCursor sub232 = sub231.sub();
         sub232.accept("n");
         env[10] /* ch */= org.fuwjin.gleux.Literal.NEW_LINE;
         sub232.commit();
      } catch(final GleuxException e233) {
         try {
            final StringCursor sub234 = sub231.sub();
            sub234.accept("t");
            env[10] /* ch */= org.fuwjin.gleux.Literal.TAB;
            sub234.commit();
         } catch(final GleuxException e235) {
            try {
               final StringCursor sub236 = sub231.sub();
               sub236.accept("r");
               env[10] /* ch */= org.fuwjin.gleux.Literal.RETURN;
               sub236.commit();
            } catch(final GleuxException e237) {
               try {
                  final StringCursor sub238 = sub231.sub();
                  sub238.accept("x");
                  env[10] /* ch */= org.fuwjin.gleux.Literal.parseHex((java.lang.String)HexDigits(sub238, env));
                  sub238.commit();
               } catch(final GleuxException e239) {
                  final StringCursor sub240 = sub231.sub();
                  env[10] /* ch */= sub240.accept();
                  sub240.commit();
               }
            }
         }
      }
      sub231.commit();
      return env[10]/* ch */;
   }

   private static Object Field(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub220 = input.sub();
      env[4] /* name */= Name(sub220, env);
      env[24] /* setter */= ((org.fuwjin.dinah.FunctionProvider)env[14]/* postage */)
            .getFunction((org.fuwjin.dinah.FunctionSignature)new org.fuwjin.dinah.FunctionSignature(
                  (java.lang.String)env[16]/* type */+ "." + env[4]/* name */));
      sub220.accept(":");
      S(sub220, env);
      try {
         ((org.fuwjin.gleux.ObjectTemplate)env[18]/* object */).set((org.fuwjin.dinah.Function)env[24]/* setter */,
               (org.fuwjin.gleux.Expression)Value(sub220, env));
      } catch(final Exception e221) {
         throw sub220.ex(e221);
      }
      sub220.commit();
      return null;
   }

   private static Object FilterChar(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub228 = input.sub();
      try {
         final StringCursor sub229 = sub228.sub();
         env[10] /* ch */= Escape(sub229, env);
         sub229.commit();
      } catch(final GleuxException e230) {
         env[10] /* ch */= sub228.acceptNot("\\");
      }
      sub228.commit();
      return env[10]/* ch */;
   }

   private static Object FilterRange(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub222 = input.sub();
      env[25] /* start */= FilterChar(sub222, env);
      S(sub222, env);
      try {
         final StringCursor sub223 = sub222.sub();
         sub223.accept("-");
         S(sub223, env);
         try {
            ((org.fuwjin.gleux.Filter)env[20]/* filter */).addRange((java.lang.Integer)env[25]/* start */,
                  (java.lang.Integer)FilterChar(sub223, env));
         } catch(final Exception e224) {
            throw sub223.ex(e224);
         }
         S(sub223, env);
         sub223.commit();
      } catch(final GleuxException e225) {
         final StringCursor sub226 = sub222.sub();
         try {
            ((org.fuwjin.gleux.Filter)env[20]/* filter */).addChar((java.lang.Integer)env[25]/* start */);
         } catch(final Exception e227) {
            throw sub226.ex(e227);
         }
         sub226.commit();
      }
      sub222.commit();
      return null;
   }

   private static Object Gleux(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub43 = input.sub();
      env[1] /* gleux */= new org.fuwjin.gleux.Gleux();
      S(sub43, env);
      try {
         try {
            LoadDeclaration(sub43, env);
         } catch(final GleuxException e44) {
            try {
               AliasDeclaration(sub43, env);
            } catch(final GleuxException e45) {
               try {
                  ((org.fuwjin.gleux.Gleux)env[1]/* gleux */).add((org.fuwjin.gleux.Declaration)ScriptDeclaration(
                        sub43, env));
               } catch(final Exception e46) {
                  throw sub43.ex(e46);
               }
            }
         }
         try {
            while(true) {
               try {
                  LoadDeclaration(sub43, env);
               } catch(final GleuxException e47) {
                  try {
                     AliasDeclaration(sub43, env);
                  } catch(final GleuxException e48) {
                     try {
                        ((org.fuwjin.gleux.Gleux)env[1]/* gleux */)
                              .add((org.fuwjin.gleux.Declaration)ScriptDeclaration(sub43, env));
                     } catch(final Exception e49) {
                        throw sub43.ex(e49);
                     }
                  }
               }
            }
         } catch(final GleuxException e50) {
            // continue
         }
      } catch(final GleuxException e51) {
         // continue
      }
      EndOfFile(sub43, env);
      sub43.commit();
      return env[1]/* gleux */;
   }

   private static Object HexDigit(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub242 = input.sub();
      sub242.acceptIn("0123456789ABCDEFabcdef");
      sub242.commit();
      return null;
   }

   private static Object HexDigits(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub241 = input.sub();
      HexDigit(sub241, env);
      HexDigit(sub241, env);
      HexDigit(sub241, env);
      HexDigit(sub241, env);
      sub241.commit();
      return sub241.match();
   }

   private static Object Identifier(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub58 = input.sub();
      IdentifierChar(sub58, env);
      try {
         while(true) {
            IdentifierChar(sub58, env);
         }
      } catch(final GleuxException e59) {
         // continue
      }
      sub58.commit();
      return sub58.match();
   }

   private static Object IdentifierChar(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub262 = input.sub();
      final StringCursor sub263 = sub262.sub();
      if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub263.next()) == Boolean.FALSE) {
         throw sub263.ex("check failed");
      }
      sub262.accept();
      sub262.commit();
      return null;
   }

   private static Object InFilter(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub202 = input.sub();
      sub202.accept("in");
      Sep(sub202, env);
      env[20] /* filter */= new org.fuwjin.gleux.Filter();
      try {
         FilterRange(sub202, env);
      } catch(final GleuxException e203) {
         throw new RuntimeException("in keyword requires at least one filter", e203);
      }
      try {
         final StringCursor sub204 = sub202.sub();
         sub204.accept(",");
         S(sub204, env);
         try {
            FilterRange(sub204, env);
         } catch(final GleuxException e205) {
            throw new RuntimeException("in keyword requires a filter after a comma", e205);
         }
         sub204.commit();
         try {
            while(true) {
               final StringCursor sub206 = sub202.sub();
               sub206.accept(",");
               S(sub206, env);
               try {
                  FilterRange(sub206, env);
               } catch(final GleuxException e207) {
                  throw new RuntimeException("in keyword requires a filter after a comma", e207);
               }
               sub206.commit();
            }
         } catch(final GleuxException e208) {
            // continue
         }
      } catch(final GleuxException e209) {
         // continue
      }
      sub202.commit();
      return env[20]/* filter */;
   }

   public static Object interpret(final CharSequence in, final Map<String, ?> environment) throws GleuxException {
      final StringCursor input = new StringCursor(in);
      final Object[] env = new Object[26];
      env[0] = environment.get("path");
      env[1] = environment.get("gleux");
      env[2] = environment.get("qname");
      env[3] = environment.get("signature");
      env[4] = environment.get("name");
      env[5] = environment.get("script");
      env[6] = environment.get("id");
      env[7] = environment.get("stmt");
      env[8] = environment.get("val");
      env[9] = environment.get("lit");
      env[10] = environment.get("ch");
      env[11] = environment.get("notted");
      env[12] = environment.get("inv");
      env[13] = environment.get("function");
      env[14] = environment.get("postage");
      env[15] = environment.get("num");
      env[16] = environment.get("type");
      env[17] = environment.get("constructor");
      env[18] = environment.get("object");
      env[19] = environment.get("block");
      env[20] = environment.get("filter");
      env[21] = environment.get("prefix");
      env[22] = environment.get("alias");
      env[23] = environment.get("module");
      env[24] = environment.get("setter");
      env[25] = environment.get("start");
      return Gleux(input, env);
   }

   private static Object Invocation(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub130 = input.sub();
      env[4] /* name */= AliasName(sub130, env);
      env[3] /* signature */= ((org.fuwjin.gleux.Gleux)env[1]/* gleux */)
            .getSignature((java.lang.String)env[4]/* name */);
      sub130.accept("(");
      S(sub130, env);
      env[12] /* inv */= new org.fuwjin.gleux.Invocation();
      try {
         final StringCursor sub131 = sub130.sub();
         try {
            ((org.fuwjin.gleux.Invocation)env[12]/* inv */).addParam((org.fuwjin.gleux.Expression)Value(sub131, env));
         } catch(final Exception e132) {
            throw sub131.ex(e132);
         }
         try {
            final StringCursor sub133 = sub131.sub();
            sub133.accept(",");
            S(sub133, env);
            try {
               try {
                  ((org.fuwjin.gleux.Invocation)env[12]/* inv */).addParam((org.fuwjin.gleux.Expression)Value(sub133,
                        env));
               } catch(final Exception e134) {
                  throw sub133.ex(e134);
               }
            } catch(final GleuxException e135) {
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
                        ((org.fuwjin.gleux.Invocation)env[12]/* inv */).addParam((org.fuwjin.gleux.Expression)Value(
                              sub136, env));
                     } catch(final Exception e137) {
                        throw sub136.ex(e137);
                     }
                  } catch(final GleuxException e138) {
                     throw new RuntimeException("invocation parameter must be a value", e138);
                  }
                  sub136.commit();
               }
            } catch(final GleuxException e139) {
               // continue
            }
         } catch(final GleuxException e140) {
            // continue
         }
         sub131.commit();
      } catch(final GleuxException e141) {
         // continue
      }
      try {
         sub130.accept(")");
      } catch(final GleuxException e142) {
         throw new RuntimeException("invocation must end with a parenthesis", e142);
      }
      S(sub130, env);
      try {
         ((org.fuwjin.dinah.FunctionSignature)env[3]/* signature */)
               .setArgCount(((org.fuwjin.gleux.Invocation)env[12]/* inv */).paramCount());
      } catch(final Exception e143) {
         throw sub130.ex(e143);
      }
      env[13] /* function */= ((org.fuwjin.dinah.FunctionProvider)env[14]/* postage */)
            .getFunction((org.fuwjin.dinah.FunctionSignature)env[3]/* signature */);
      try {
         ((org.fuwjin.gleux.Invocation)env[12]/* inv */).setFunction((org.fuwjin.dinah.Function)env[13]/* function */);
      } catch(final Exception e144) {
         throw sub130.ex(e144);
      }
      sub130.commit();
      return env[12]/* inv */;
   }

   private static Object IsStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub169 = input.sub();
      sub169.accept("is");
      Sep(sub169, env);
      try {
         final StringCursor sub170 = sub169.sub();
         sub170.accept("not");
         Sep(sub170, env);
         env[11] /* notted */= java.lang.Boolean.TRUE;
         sub170.commit();
      } catch(final GleuxException e171) {
         final StringCursor sub172 = sub169.sub();
         env[11] /* notted */= java.lang.Boolean.FALSE;
         sub172.commit();
      }
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.IsStatement((java.lang.Boolean)env[11]/* notted */,
               (org.fuwjin.gleux.Expression)new org.fuwjin.gleux.FilterAcceptStatement(java.lang.Boolean.FALSE,
                     (org.fuwjin.gleux.Filter)InFilter(sub169, env)));
      } catch(final GleuxException e173) {
         try {
            env[7] /* stmt */= new org.fuwjin.gleux.IsStatement((java.lang.Boolean)env[11]/* notted */,
                  (org.fuwjin.gleux.Expression)new org.fuwjin.gleux.ValueAcceptStatement(
                        (java.lang.Boolean)java.lang.Boolean.FALSE, (org.fuwjin.gleux.Expression)StaticLiteral(sub169,
                              env)));
         } catch(final GleuxException e174) {
            try {
               env[7] /* stmt */= new org.fuwjin.gleux.IsStatement((java.lang.Boolean)env[11]/* notted */,
                     (org.fuwjin.gleux.Expression)Value(sub169, env));
            } catch(final GleuxException e175) {
               throw new RuntimeException("is keyword requires value or in keyword", e175);
            }
         }
      }
      sub169.commit();
      return env[7]/* stmt */;
   }

   private static Object LoadDeclaration(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub3 = input.sub();
      sub3.accept("load");
      Sep(sub3, env);
      try {
         env[0] /* path */= PathName(sub3, env);
      } catch(final GleuxException e4) {
         throw new RuntimeException("load keyword requires a file path", e4);
      }
      try {
         sub3.accept("as");
      } catch(final GleuxException e5) {
         throw new RuntimeException("load keyword requires as keyword", e5);
      }
      Sep(sub3, env);
      try {
         try {
            ((org.fuwjin.gleux.Gleux)env[1]/* gleux */).load((java.lang.String)env[0]/* path */,
                  (java.lang.String)Name(sub3, env));
         } catch(final Exception e6) {
            throw sub3.ex(e6);
         }
      } catch(final GleuxException e7) {
         throw new RuntimeException("load-as keywords require a name", e7);
      }
      sub3.commit();
      return null;
   }

   private static Object MatchValue(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub167 = input.sub();
      sub167.accept("match");
      Sep(sub167, env);
      sub167.commit();
      return org.fuwjin.gleux.Variable.MATCH;
   }

   private static Object Name(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub56 = input.sub();
      env[6] /* id */= Identifier(sub56, env);
      S(sub56, env);
      sub56.commit();
      return env[6]/* id */;
   }

   private static Object NextValue(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub168 = input.sub();
      sub168.accept("next");
      Sep(sub168, env);
      sub168.commit();
      return org.fuwjin.gleux.Variable.NEXT;
   }

   private static Object Number(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub145 = input.sub();
      try {
         sub145.accept("-");
      } catch(final GleuxException e146) {
         // continue
      }
      try {
         final StringCursor sub147 = sub145.sub();
         sub147.acceptIn("0123456789");
         try {
            while(true) {
               sub147.acceptIn("0123456789");
            }
         } catch(final GleuxException e148) {
            // continue
         }
         try {
            final StringCursor sub149 = sub147.sub();
            sub149.accept(".");
            try {
               sub149.acceptIn("0123456789");
               try {
                  while(true) {
                     sub149.acceptIn("0123456789");
                  }
               } catch(final GleuxException e150) {
                  // continue
               }
            } catch(final GleuxException e151) {
               // continue
            }
            sub149.commit();
         } catch(final GleuxException e152) {
            // continue
         }
         sub147.commit();
      } catch(final GleuxException e153) {
         final StringCursor sub154 = sub145.sub();
         sub154.accept(".");
         sub154.acceptIn("0123456789");
         try {
            while(true) {
               sub154.acceptIn("0123456789");
            }
         } catch(final GleuxException e155) {
            // continue
         }
         sub154.commit();
      }
      try {
         final StringCursor sub156 = sub145.sub();
         sub156.acceptIn("Ee");
         try {
            sub156.accept("-");
         } catch(final GleuxException e157) {
            // continue
         }
         sub156.acceptIn("0123456789");
         try {
            while(true) {
               sub156.acceptIn("0123456789");
            }
         } catch(final GleuxException e158) {
            // continue
         }
         sub156.commit();
      } catch(final GleuxException e159) {
         // continue
      }
      env[15] /* num */= new org.fuwjin.gleux.Number(sub145.match());
      Sep(sub145, env);
      sub145.commit();
      return env[15]/* num */;
   }

   private static Object Object(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub160 = input.sub();
      sub160.accept("(");
      S(sub160, env);
      env[16] /* type */= AliasName(sub160, env);
      env[17] /* constructor */= ((org.fuwjin.dinah.FunctionProvider)env[14]/* postage */)
            .getFunction(new org.fuwjin.dinah.FunctionSignature((java.lang.String)env[16]/* type */+ ".new"));
      env[18] /* object */= new org.fuwjin.gleux.ObjectTemplate((org.fuwjin.dinah.Function)env[17]/* constructor */);
      sub160.accept(")");
      S(sub160, env);
      sub160.accept("{");
      S(sub160, env);
      try {
         final StringCursor sub161 = sub160.sub();
         Field(sub161, env);
         try {
            final StringCursor sub162 = sub161.sub();
            sub162.accept(",");
            S(sub162, env);
            Field(sub162, env);
            sub162.commit();
            try {
               while(true) {
                  final StringCursor sub163 = sub161.sub();
                  sub163.accept(",");
                  S(sub163, env);
                  Field(sub163, env);
                  sub163.commit();
               }
            } catch(final GleuxException e164) {
               // continue
            }
         } catch(final GleuxException e165) {
            // continue
         }
         sub161.commit();
      } catch(final GleuxException e166) {
         // continue
      }
      sub160.accept("}");
      S(sub160, env);
      sub160.commit();
      return env[18]/* object */;
   }

   private static Object Path(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub248 = input.sub();
      final StringCursor sub249 = sub248.sub();
      try {
         sub249.accept("/");
      } catch(final GleuxException e250) {
         // continue
      }
      QualifiedIdentifier(sub249, env);
      sub249.commit();
      try {
         while(true) {
            final StringCursor sub251 = sub248.sub();
            try {
               sub251.accept("/");
            } catch(final GleuxException e252) {
               // continue
            }
            QualifiedIdentifier(sub251, env);
            sub251.commit();
         }
      } catch(final GleuxException e253) {
         // continue
      }
      try {
         sub248.accept("/");
      } catch(final GleuxException e254) {
         // continue
      }
      sub248.commit();
      return sub248.match();
   }

   private static Object PathName(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub55 = input.sub();
      env[0] /* path */= Path(sub55, env);
      S(sub55, env);
      sub55.commit();
      return env[0]/* path */;
   }

   private static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub190 = input.sub();
      sub190.accept("publish");
      Sep(sub190, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.PublishStatement((org.fuwjin.gleux.Expression)Value(sub190, env));
      } catch(final GleuxException e191) {
         throw new RuntimeException("publish keyword requires a value", e191);
      }
      sub190.commit();
      return env[7]/* stmt */;
   }

   private static Object QualifiedIdentifier(final StringCursor input, final Object... parentEnv) throws GleuxException {
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
         } catch(final GleuxException e246) {
            // continue
         }
      } catch(final GleuxException e247) {
         // continue
      }
      sub243.commit();
      return sub243.match();
   }

   private static Object QualifiedName(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub57 = input.sub();
      env[6] /* id */= QualifiedIdentifier(sub57, env);
      S(sub57, env);
      sub57.commit();
      return env[6]/* id */;
   }

   private static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub188 = input.sub();
      sub188.accept("repeat");
      Sep(sub188, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.RepeatStatement((org.fuwjin.gleux.Expression)Statement(sub188, env));
      } catch(final GleuxException e189) {
         throw new RuntimeException("repeat keyword requires a statement", e189);
      }
      sub188.commit();
      return env[7]/* stmt */;
   }

   private static Object S(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub1 = input.sub();
      try {
         Space(sub1, env);
      } catch(final GleuxException e2) {
         // continue
      }
      sub1.commit();
      return null;
   }

   private static Object Script(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub115 = input.sub();
      env[5] /* script */= ScriptIdent(sub115, env);
      try {
         final StringCursor sub116 = sub115.sub();
         sub116.accept("<<");
         S(sub116, env);
         env[5] /* script */= new org.fuwjin.gleux.ScriptInput((org.fuwjin.gleux.Transformer)env[5]/* script */,
               (org.fuwjin.gleux.Expression)Value(sub116, env));
         sub116.commit();
      } catch(final GleuxException e117) {
         // continue
      }
      try {
         final StringCursor sub118 = sub115.sub();
         sub118.accept(">>");
         S(sub118, env);
         env[5] /* script */= new org.fuwjin.gleux.ScriptPipe((org.fuwjin.gleux.Transformer)env[5]/* script */,
               (org.fuwjin.gleux.Transformer)ScriptIdent(sub118, env));
         sub118.commit();
         try {
            while(true) {
               final StringCursor sub119 = sub115.sub();
               sub119.accept(">>");
               S(sub119, env);
               env[5] /* script */= new org.fuwjin.gleux.ScriptPipe((org.fuwjin.gleux.Transformer)env[5]/* script */,
                     (org.fuwjin.gleux.Transformer)ScriptIdent(sub119, env));
               sub119.commit();
            }
         } catch(final GleuxException e120) {
            // continue
         }
      } catch(final GleuxException e121) {
         // continue
      }
      try {
         final StringCursor sub122 = sub115.sub();
         sub122.accept(">>");
         S(sub122, env);
         env[5] /* script */= new org.fuwjin.gleux.ScriptOutput((org.fuwjin.gleux.Transformer)env[5]/* script */,
               (java.lang.String)Name(sub122, env));
         sub122.commit();
      } catch(final GleuxException e123) {
         // continue
      }
      sub115.commit();
      return env[5]/* script */;
   }

   private static Object ScriptDeclaration(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub27 = input.sub();
      sub27.accept("<");
      try {
         env[4] /* name */= Identifier(sub27, env);
      } catch(final GleuxException e28) {
         throw new RuntimeException("script identifiers must be enclosed in angle brackets", e28);
      }
      try {
         sub27.accept(">");
      } catch(final GleuxException e29) {
         throw new RuntimeException("script identifiers must end with an angle bracket", e29);
      }
      S(sub27, env);
      try {
         sub27.accept("{");
      } catch(final GleuxException e30) {
         throw new RuntimeException("script declarations must start with a brace", e30);
      }
      S(sub27, env);
      env[5] /* script */= new org.fuwjin.gleux.Declaration((java.lang.String)env[4]/* name */);
      try {
         try {
            ((org.fuwjin.gleux.Declaration)env[5]/* script */).add((org.fuwjin.gleux.Expression)Statement(sub27, env));
         } catch(final Exception e31) {
            throw sub27.ex(e31);
         }
         try {
            while(true) {
               try {
                  ((org.fuwjin.gleux.Declaration)env[5]/* script */).add((org.fuwjin.gleux.Expression)Statement(sub27,
                        env));
               } catch(final Exception e32) {
                  throw sub27.ex(e32);
               }
            }
         } catch(final GleuxException e33) {
            // continue
         }
      } catch(final GleuxException e34) {
         // continue
      }
      try {
         final StringCursor sub35 = sub27.sub();
         sub35.accept("return");
         Sep(sub35, env);
         try {
            try {
               ((org.fuwjin.gleux.Declaration)env[5]/* script */)
                     .returns((org.fuwjin.gleux.Expression)Value(sub35, env));
            } catch(final Exception e36) {
               throw sub35.ex(e36);
            }
         } catch(final GleuxException e37) {
            throw new RuntimeException("return keyword requires a value", e37);
         }
         sub35.commit();
      } catch(final GleuxException e38) {
         // continue
      }
      try {
         sub27.accept("}");
      } catch(final GleuxException e39) {
         throw new RuntimeException("script declaration for " + env[4]/* name */+ " must end with a brace", e39);
      }
      S(sub27, env);
      sub27.commit();
      return env[5]/* script */;
   }

   private static Object ScriptIdent(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub214 = input.sub();
      sub214.accept("<");
      try {
         env[6] /* id */= Identifier(sub214, env);
      } catch(final GleuxException e215) {
         throw new RuntimeException("script identifiers must be enclosed in angle brackets", e215);
      }
      try {
         final StringCursor sub216 = sub214.sub();
         sub216.accept(":");
         env[4] /* name */= Identifier(sub216, env);
         env[23] /* module */= ((org.fuwjin.gleux.Gleux)env[1]/* gleux */).getModule((java.lang.String)env[6]/* id */);
         env[5] /* script */= ((org.fuwjin.gleux.Gleux)env[23]/* module */).get((java.lang.String)env[4]/* name */);
         sub216.commit();
      } catch(final GleuxException e217) {
         final StringCursor sub218 = sub214.sub();
         env[5] /* script */= ((org.fuwjin.gleux.Gleux)env[1]/* gleux */).get((java.lang.String)env[6]/* id */);
         sub218.commit();
      }
      try {
         sub214.accept(">");
      } catch(final GleuxException e219) {
         throw new RuntimeException("script identifiers must be normal identifiers in angle brackets", e219);
      }
      S(sub214, env);
      sub214.commit();
      return env[5]/* script */;
   }

   private static Object Sep(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub52 = input.sub();
      final StringCursor sub53 = sub52.sub();
      boolean b = true;
      try {
         if(IdentifierChar(sub53, env) == Boolean.FALSE) {
            b = false;
         }
      } catch(final GleuxException e54) {
         b = false;
      }
      if(b) {
         throw sub53.ex("unexpected value");
      }
      S(sub52, env);
      sub52.commit();
      return null;
   }

   private static Object Space(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub264 = input.sub();
      try {
         sub264.acceptIn("\t\n\r ");
      } catch(final GleuxException e265) {
         Comment(sub264, env);
      }
      try {
         while(true) {
            try {
               sub264.acceptIn("\t\n\r ");
            } catch(final GleuxException e266) {
               Comment(sub264, env);
            }
         }
      } catch(final GleuxException e267) {
         // continue
      }
      sub264.commit();
      return null;
   }

   private static Object Statement(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub60 = input.sub();
      try {
         env[7] /* stmt */= IsStatement(sub60, env);
      } catch(final GleuxException e61) {
         try {
            env[7] /* stmt */= EitherOrStatement(sub60, env);
         } catch(final GleuxException e62) {
            try {
               env[7] /* stmt */= CouldStatement(sub60, env);
            } catch(final GleuxException e63) {
               try {
                  env[7] /* stmt */= RepeatStatement(sub60, env);
               } catch(final GleuxException e64) {
                  try {
                     env[7] /* stmt */= AcceptStatement(sub60, env);
                  } catch(final GleuxException e65) {
                     try {
                        env[7] /* stmt */= PublishStatement(sub60, env);
                     } catch(final GleuxException e66) {
                        try {
                           env[7] /* stmt */= AbortStatement(sub60, env);
                        } catch(final GleuxException e67) {
                           try {
                              env[7] /* stmt */= Script(sub60, env);
                           } catch(final GleuxException e68) {
                              try {
                                 env[7] /* stmt */= Block(sub60, env);
                              } catch(final GleuxException e69) {
                                 try {
                                    env[7] /* stmt */= Assignment(sub60, env);
                                 } catch(final GleuxException e70) {
                                    env[7] /* stmt */= Invocation(sub60, env);
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
      return env[7]/* stmt */;
   }

   private static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub81 = input.sub();
      sub81.accept("'");
      env[9] /* lit */= new org.fuwjin.gleux.Literal();
      try {
         try {
            final StringCursor sub82 = sub81.sub();
            env[10] /* ch */= sub82.acceptNotIn("'\\");
            try {
               ((org.fuwjin.gleux.Literal)env[9]/* lit */).append((java.lang.Integer)env[10]/* ch */);
            } catch(final Exception e83) {
               throw sub82.ex(e83);
            }
            sub82.commit();
         } catch(final GleuxException e84) {
            final StringCursor sub85 = sub81.sub();
            try {
               ((org.fuwjin.gleux.Literal)env[9]/* lit */).append((java.lang.Integer)Escape(sub85, env));
            } catch(final Exception e86) {
               throw sub85.ex(e86);
            }
            sub85.commit();
         }
         try {
            while(true) {
               try {
                  final StringCursor sub87 = sub81.sub();
                  env[10] /* ch */= sub87.acceptNotIn("'\\");
                  try {
                     ((org.fuwjin.gleux.Literal)env[9]/* lit */).append((java.lang.Integer)env[10]/* ch */);
                  } catch(final Exception e88) {
                     throw sub87.ex(e88);
                  }
                  sub87.commit();
               } catch(final GleuxException e89) {
                  final StringCursor sub90 = sub81.sub();
                  try {
                     ((org.fuwjin.gleux.Literal)env[9]/* lit */).append((java.lang.Integer)Escape(sub90, env));
                  } catch(final Exception e91) {
                     throw sub90.ex(e91);
                  }
                  sub90.commit();
               }
            }
         } catch(final GleuxException e92) {
            // continue
         }
      } catch(final GleuxException e93) {
         // continue
      }
      try {
         sub81.accept("'");
      } catch(final GleuxException e94) {
         throw new RuntimeException("static literals must end with a quote", e94);
      }
      S(sub81, env);
      sub81.commit();
      return env[9]/* lit */;
   }

   private static Object Value(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub71 = input.sub();
      try {
         env[8] /* val */= StaticLiteral(sub71, env);
      } catch(final GleuxException e72) {
         try {
            env[8] /* val */= DynamicLiteral(sub71, env);
         } catch(final GleuxException e73) {
            try {
               env[8] /* val */= Script(sub71, env);
            } catch(final GleuxException e74) {
               try {
                  env[8] /* val */= AcceptStatement(sub71, env);
               } catch(final GleuxException e75) {
                  try {
                     env[8] /* val */= Invocation(sub71, env);
                  } catch(final GleuxException e76) {
                     try {
                        env[8] /* val */= Number(sub71, env);
                     } catch(final GleuxException e77) {
                        try {
                           env[8] /* val */= Object(sub71, env);
                        } catch(final GleuxException e78) {
                           try {
                              env[8] /* val */= MatchValue(sub71, env);
                           } catch(final GleuxException e79) {
                              try {
                                 env[8] /* val */= NextValue(sub71, env);
                              } catch(final GleuxException e80) {
                                 env[8] /* val */= new org.fuwjin.gleux.Variable((java.lang.String)Name(sub71, env));
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
      return env[8]/* val */;
   }
}
