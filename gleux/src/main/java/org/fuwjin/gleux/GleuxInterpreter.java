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
      final StringCursor sub191 = input.sub();
      sub191.accept("abort");
      Sep(sub191, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.AbortStatement((org.fuwjin.gleux.Expression)Value(sub191, env));
      } catch(final GleuxException e192) {
         throw new RuntimeException("abort keyword requires a value", e192);
      }
      sub191.commit();
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
      final StringCursor sub209 = input.sub();
      env[21] /* prefix */= Identifier(sub209, env);
      env[22] /* alias */= ((org.fuwjin.gleux.Gleux)env[1]/* gleux */).alias((java.lang.String)env[21]/* prefix */);
      try {
         final StringCursor sub210 = sub209.sub();
         sub210.accept(".");
         env[4] /* name */= env[22]/* alias */+ "." + QualifiedName(sub210, env);
         sub210.commit();
      } catch(final GleuxException e211) {
         final StringCursor sub212 = sub209.sub();
         env[4] /* name */= env[22]/* alias */;
         sub212.commit();
      }
      sub209.commit();
      return env[4]/* name */;
   }

   private static Object AnnotatedIdentifier(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub254 = input.sub();
      Identifier(sub254, env);
      try {
         final StringCursor sub255 = sub254.sub();
         sub255.accept("[");
         try {
            Identifier(sub255, env);
         } catch(final GleuxException e256) {
            // continue
         }
         sub255.accept("]");
         sub255.commit();
         try {
            while(true) {
               final StringCursor sub257 = sub254.sub();
               sub257.accept("[");
               try {
                  Identifier(sub257, env);
               } catch(final GleuxException e258) {
                  // continue
               }
               sub257.accept("]");
               sub257.commit();
            }
         } catch(final GleuxException e259) {
            // continue
         }
      } catch(final GleuxException e260) {
         // continue
      }
      sub254.commit();
      return null;
   }

   private static Object Assignment(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub199 = input.sub();
      env[4] /* name */= Name(sub199, env);
      sub199.accept("=");
      S(sub199, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.Assignment((java.lang.String)env[4]/* name */,
               (org.fuwjin.gleux.Expression)Value(sub199, env));
      } catch(final GleuxException e200) {
         throw new RuntimeException("assignment requires a value", e200);
      }
      sub199.commit();
      return env[7]/* stmt */;
   }

   private static Object Block(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub193 = input.sub();
      sub193.accept("{");
      S(sub193, env);
      env[19] /* block */= new org.fuwjin.gleux.Block();
      try {
         try {
            ((org.fuwjin.gleux.Block)env[19]/* block */).add((org.fuwjin.gleux.Expression)Statement(sub193, env));
         } catch(final Exception e194) {
            throw sub193.ex(e194);
         }
         try {
            while(true) {
               try {
                  ((org.fuwjin.gleux.Block)env[19]/* block */).add((org.fuwjin.gleux.Expression)Statement(sub193, env));
               } catch(final Exception e195) {
                  throw sub193.ex(e195);
               }
            }
         } catch(final GleuxException e196) {
            // continue
         }
      } catch(final GleuxException e197) {
         // continue
      }
      try {
         sub193.accept("}");
      } catch(final GleuxException e198) {
         throw new RuntimeException("block must end with a brace", e198);
      }
      S(sub193, env);
      sub193.commit();
      return env[19]/* block */;
   }

   private static Object Comment(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub267 = input.sub();
      sub267.accept("#");
      try {
         sub267.acceptNotIn("\n\r");
         try {
            while(true) {
               sub267.acceptNotIn("\n\r");
            }
         } catch(final GleuxException e268) {
            // continue
         }
      } catch(final GleuxException e269) {
         // continue
      }
      try {
         sub267.accept("\r");
      } catch(final GleuxException e270) {
         // continue
      }
      try {
         sub267.accept("\n");
      } catch(final GleuxException e271) {
         EndOfFile(sub267, env);
      }
      sub267.commit();
      return null;
   }

   private static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub185 = input.sub();
      sub185.accept("could");
      Sep(sub185, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.CouldStatement((org.fuwjin.gleux.Expression)Statement(sub185, env));
      } catch(final GleuxException e186) {
         throw new RuntimeException("could keyword requires a statement", e186);
      }
      sub185.commit();
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
      final StringCursor sub175 = input.sub();
      sub175.accept("either");
      Sep(sub175, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.EitherOrStatement((org.fuwjin.gleux.Expression)Statement(sub175, env));
      } catch(final GleuxException e176) {
         throw new RuntimeException("either keyword requires a statement", e176);
      }
      try {
         final StringCursor sub177 = sub175.sub();
         sub177.accept("or");
         Sep(sub177, env);
         try {
            try {
               ((org.fuwjin.gleux.EitherOrStatement)env[7]/* stmt */).or((org.fuwjin.gleux.Expression)Statement(sub177,
                     env));
            } catch(final Exception e178) {
               throw sub177.ex(e178);
            }
         } catch(final GleuxException e179) {
            throw new RuntimeException("or keyword requires a statement", e179);
         }
         sub177.commit();
         try {
            while(true) {
               final StringCursor sub180 = sub175.sub();
               sub180.accept("or");
               Sep(sub180, env);
               try {
                  try {
                     ((org.fuwjin.gleux.EitherOrStatement)env[7]/* stmt */).or((org.fuwjin.gleux.Expression)Statement(
                           sub180, env));
                  } catch(final Exception e181) {
                     throw sub180.ex(e181);
                  }
               } catch(final GleuxException e182) {
                  throw new RuntimeException("or keyword requires a statement", e182);
               }
               sub180.commit();
            }
         } catch(final GleuxException e183) {
            // continue
         }
      } catch(final GleuxException e184) {
         throw new RuntimeException("either keyword requires at least one or keyword", e184);
      }
      sub175.commit();
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
      final StringCursor sub230 = input.sub();
      sub230.accept("\\");
      try {
         final StringCursor sub231 = sub230.sub();
         sub231.accept("n");
         env[10] /* ch */= org.fuwjin.gleux.Literal.NEW_LINE;
         sub231.commit();
      } catch(final GleuxException e232) {
         try {
            final StringCursor sub233 = sub230.sub();
            sub233.accept("t");
            env[10] /* ch */= org.fuwjin.gleux.Literal.TAB;
            sub233.commit();
         } catch(final GleuxException e234) {
            try {
               final StringCursor sub235 = sub230.sub();
               sub235.accept("r");
               env[10] /* ch */= org.fuwjin.gleux.Literal.RETURN;
               sub235.commit();
            } catch(final GleuxException e236) {
               try {
                  final StringCursor sub237 = sub230.sub();
                  sub237.accept("x");
                  env[10] /* ch */= org.fuwjin.gleux.Literal.parseHex((java.lang.String)HexDigits(sub237, env));
                  sub237.commit();
               } catch(final GleuxException e238) {
                  final StringCursor sub239 = sub230.sub();
                  env[10] /* ch */= sub239.accept();
                  sub239.commit();
               }
            }
         }
      }
      sub230.commit();
      return env[10]/* ch */;
   }

   private static Object Field(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub219 = input.sub();
      env[4] /* name */= Name(sub219, env);
      env[24] /* setter */= ((org.fuwjin.dinah.FunctionProvider)env[13]/* postage */)
            .getFunction((org.fuwjin.dinah.FunctionSignature)new org.fuwjin.dinah.FunctionSignature(
                  (java.lang.String)env[16]/* type */+ "." + env[4]/* name */));
      sub219.accept(":");
      S(sub219, env);
      try {
         ((org.fuwjin.gleux.ObjectTemplate)env[18]/* object */).set((org.fuwjin.dinah.Function)env[24]/* setter */,
               (org.fuwjin.gleux.Expression)Value(sub219, env));
      } catch(final Exception e220) {
         throw sub219.ex(e220);
      }
      sub219.commit();
      return null;
   }

   private static Object FilterChar(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub227 = input.sub();
      try {
         final StringCursor sub228 = sub227.sub();
         env[10] /* ch */= Escape(sub228, env);
         sub228.commit();
      } catch(final GleuxException e229) {
         env[10] /* ch */= sub227.acceptNot("\\");
      }
      sub227.commit();
      return env[10]/* ch */;
   }

   private static Object FilterRange(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub221 = input.sub();
      env[25] /* start */= FilterChar(sub221, env);
      S(sub221, env);
      try {
         final StringCursor sub222 = sub221.sub();
         sub222.accept("-");
         S(sub222, env);
         try {
            ((org.fuwjin.gleux.Filter)env[20]/* filter */).addRange((java.lang.Integer)env[25]/* start */,
                  (java.lang.Integer)FilterChar(sub222, env));
         } catch(final Exception e223) {
            throw sub222.ex(e223);
         }
         S(sub222, env);
         sub222.commit();
      } catch(final GleuxException e224) {
         final StringCursor sub225 = sub221.sub();
         try {
            ((org.fuwjin.gleux.Filter)env[20]/* filter */).addChar((java.lang.Integer)env[25]/* start */);
         } catch(final Exception e226) {
            throw sub225.ex(e226);
         }
         sub225.commit();
      }
      sub221.commit();
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
      final StringCursor sub241 = input.sub();
      sub241.acceptIn("0123456789ABCDEFabcdef");
      sub241.commit();
      return null;
   }

   private static Object HexDigits(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub240 = input.sub();
      HexDigit(sub240, env);
      HexDigit(sub240, env);
      HexDigit(sub240, env);
      HexDigit(sub240, env);
      sub240.commit();
      return sub240.match();
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
      final StringCursor sub261 = input.sub();
      final StringCursor sub262 = sub261.sub();
      if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub262.next()) == Boolean.FALSE) {
         throw sub262.ex("check failed");
      }
      sub261.accept();
      sub261.commit();
      return null;
   }

   private static Object InFilter(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub201 = input.sub();
      sub201.accept("in");
      Sep(sub201, env);
      env[20] /* filter */= new org.fuwjin.gleux.Filter();
      try {
         FilterRange(sub201, env);
      } catch(final GleuxException e202) {
         throw new RuntimeException("in keyword requires at least one filter", e202);
      }
      try {
         final StringCursor sub203 = sub201.sub();
         sub203.accept(",");
         S(sub203, env);
         try {
            FilterRange(sub203, env);
         } catch(final GleuxException e204) {
            throw new RuntimeException("in keyword requires a filter after a comma", e204);
         }
         sub203.commit();
         try {
            while(true) {
               final StringCursor sub205 = sub201.sub();
               sub205.accept(",");
               S(sub205, env);
               try {
                  FilterRange(sub205, env);
               } catch(final GleuxException e206) {
                  throw new RuntimeException("in keyword requires a filter after a comma", e206);
               }
               sub205.commit();
            }
         } catch(final GleuxException e207) {
            // continue
         }
      } catch(final GleuxException e208) {
         // continue
      }
      sub201.commit();
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
      env[12] = environment.get("function");
      env[13] = environment.get("postage");
      env[14] = environment.get("inv");
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
      env[12] /* function */= ((org.fuwjin.dinah.FunctionProvider)env[13]/* postage */)
            .getFunction((org.fuwjin.dinah.FunctionSignature)env[3]/* signature */);
      env[14] /* inv */= new org.fuwjin.gleux.Invocation((org.fuwjin.dinah.Function)env[12]/* function */);
      try {
         final StringCursor sub131 = sub130.sub();
         try {
            ((org.fuwjin.gleux.Invocation)env[14]/* inv */).addParam((org.fuwjin.gleux.Expression)Value(sub131, env));
         } catch(final Exception e132) {
            throw sub131.ex(e132);
         }
         try {
            final StringCursor sub133 = sub131.sub();
            sub133.accept(",");
            S(sub133, env);
            try {
               try {
                  ((org.fuwjin.gleux.Invocation)env[14]/* inv */).addParam((org.fuwjin.gleux.Expression)Value(sub133,
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
                        ((org.fuwjin.gleux.Invocation)env[14]/* inv */).addParam((org.fuwjin.gleux.Expression)Value(
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
         ((org.fuwjin.gleux.Invocation)env[14]/* inv */).resolve();
      } catch(final Exception e143) {
         throw sub130.ex(e143);
      }
      sub130.commit();
      return env[14]/* inv */;
   }

   private static Object IsStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub168 = input.sub();
      sub168.accept("is");
      Sep(sub168, env);
      try {
         final StringCursor sub169 = sub168.sub();
         sub169.accept("not");
         Sep(sub169, env);
         env[11] /* notted */= java.lang.Boolean.TRUE;
         sub169.commit();
      } catch(final GleuxException e170) {
         final StringCursor sub171 = sub168.sub();
         env[11] /* notted */= java.lang.Boolean.FALSE;
         sub171.commit();
      }
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.IsStatement((java.lang.Boolean)env[11]/* notted */,
               (org.fuwjin.gleux.Expression)new org.fuwjin.gleux.FilterAcceptStatement(java.lang.Boolean.FALSE,
                     (org.fuwjin.gleux.Filter)InFilter(sub168, env)));
      } catch(final GleuxException e172) {
         try {
            env[7] /* stmt */= new org.fuwjin.gleux.IsStatement((java.lang.Boolean)env[11]/* notted */,
                  (org.fuwjin.gleux.Expression)new org.fuwjin.gleux.ValueAcceptStatement(
                        (java.lang.Boolean)java.lang.Boolean.FALSE, (org.fuwjin.gleux.Expression)StaticLiteral(sub168,
                              env)));
         } catch(final GleuxException e173) {
            try {
               env[7] /* stmt */= new org.fuwjin.gleux.IsStatement((java.lang.Boolean)env[11]/* notted */,
                     (org.fuwjin.gleux.Expression)Value(sub168, env));
            } catch(final GleuxException e174) {
               throw new RuntimeException("is keyword requires value or in keyword", e174);
            }
         }
      }
      sub168.commit();
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
      final StringCursor sub166 = input.sub();
      sub166.accept("match");
      Sep(sub166, env);
      sub166.commit();
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
      final StringCursor sub167 = input.sub();
      sub167.accept("next");
      Sep(sub167, env);
      sub167.commit();
      return org.fuwjin.gleux.Variable.NEXT;
   }

   private static Object Number(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub144 = input.sub();
      try {
         sub144.accept("-");
      } catch(final GleuxException e145) {
         // continue
      }
      try {
         final StringCursor sub146 = sub144.sub();
         sub146.acceptIn("0123456789");
         try {
            while(true) {
               sub146.acceptIn("0123456789");
            }
         } catch(final GleuxException e147) {
            // continue
         }
         try {
            final StringCursor sub148 = sub146.sub();
            sub148.accept(".");
            try {
               sub148.acceptIn("0123456789");
               try {
                  while(true) {
                     sub148.acceptIn("0123456789");
                  }
               } catch(final GleuxException e149) {
                  // continue
               }
            } catch(final GleuxException e150) {
               // continue
            }
            sub148.commit();
         } catch(final GleuxException e151) {
            // continue
         }
         sub146.commit();
      } catch(final GleuxException e152) {
         final StringCursor sub153 = sub144.sub();
         sub153.accept(".");
         sub153.acceptIn("0123456789");
         try {
            while(true) {
               sub153.acceptIn("0123456789");
            }
         } catch(final GleuxException e154) {
            // continue
         }
         sub153.commit();
      }
      try {
         final StringCursor sub155 = sub144.sub();
         sub155.acceptIn("Ee");
         try {
            sub155.accept("-");
         } catch(final GleuxException e156) {
            // continue
         }
         sub155.acceptIn("0123456789");
         try {
            while(true) {
               sub155.acceptIn("0123456789");
            }
         } catch(final GleuxException e157) {
            // continue
         }
         sub155.commit();
      } catch(final GleuxException e158) {
         // continue
      }
      env[15] /* num */= new org.fuwjin.gleux.Number(sub144.match());
      Sep(sub144, env);
      sub144.commit();
      return env[15]/* num */;
   }

   private static Object Object(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub159 = input.sub();
      sub159.accept("(");
      S(sub159, env);
      env[16] /* type */= AliasName(sub159, env);
      env[17] /* constructor */= ((org.fuwjin.dinah.FunctionProvider)env[13]/* postage */)
            .getFunction(new org.fuwjin.dinah.FunctionSignature((java.lang.String)env[16]/* type */+ ".new"));
      env[18] /* object */= new org.fuwjin.gleux.ObjectTemplate((org.fuwjin.dinah.Function)env[17]/* constructor */);
      sub159.accept(")");
      S(sub159, env);
      sub159.accept("{");
      S(sub159, env);
      try {
         final StringCursor sub160 = sub159.sub();
         Field(sub160, env);
         try {
            final StringCursor sub161 = sub160.sub();
            sub161.accept(",");
            S(sub161, env);
            Field(sub161, env);
            sub161.commit();
            try {
               while(true) {
                  final StringCursor sub162 = sub160.sub();
                  sub162.accept(",");
                  S(sub162, env);
                  Field(sub162, env);
                  sub162.commit();
               }
            } catch(final GleuxException e163) {
               // continue
            }
         } catch(final GleuxException e164) {
            // continue
         }
         sub160.commit();
      } catch(final GleuxException e165) {
         // continue
      }
      sub159.accept("}");
      S(sub159, env);
      sub159.commit();
      return env[18]/* object */;
   }

   private static Object Path(final StringCursor input, final Object... parentEnv) throws GleuxException {
      final Object[] env = new Object[parentEnv.length];
      System.arraycopy(parentEnv, 0, env, 0, env.length);
      final StringCursor sub247 = input.sub();
      final StringCursor sub248 = sub247.sub();
      try {
         sub248.accept("/");
      } catch(final GleuxException e249) {
         // continue
      }
      QualifiedIdentifier(sub248, env);
      sub248.commit();
      try {
         while(true) {
            final StringCursor sub250 = sub247.sub();
            try {
               sub250.accept("/");
            } catch(final GleuxException e251) {
               // continue
            }
            QualifiedIdentifier(sub250, env);
            sub250.commit();
         }
      } catch(final GleuxException e252) {
         // continue
      }
      try {
         sub247.accept("/");
      } catch(final GleuxException e253) {
         // continue
      }
      sub247.commit();
      return sub247.match();
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
      final StringCursor sub189 = input.sub();
      sub189.accept("publish");
      Sep(sub189, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.PublishStatement((org.fuwjin.gleux.Expression)Value(sub189, env));
      } catch(final GleuxException e190) {
         throw new RuntimeException("publish keyword requires a value", e190);
      }
      sub189.commit();
      return env[7]/* stmt */;
   }

   private static Object QualifiedIdentifier(final StringCursor input, final Object... parentEnv) throws GleuxException {
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
         } catch(final GleuxException e245) {
            // continue
         }
      } catch(final GleuxException e246) {
         // continue
      }
      sub242.commit();
      return sub242.match();
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
      final StringCursor sub187 = input.sub();
      sub187.accept("repeat");
      Sep(sub187, env);
      try {
         env[7] /* stmt */= new org.fuwjin.gleux.RepeatStatement((org.fuwjin.gleux.Expression)Statement(sub187, env));
      } catch(final GleuxException e188) {
         throw new RuntimeException("repeat keyword requires a statement", e188);
      }
      sub187.commit();
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
      final StringCursor sub213 = input.sub();
      sub213.accept("<");
      try {
         env[6] /* id */= Identifier(sub213, env);
      } catch(final GleuxException e214) {
         throw new RuntimeException("script identifiers must be enclosed in angle brackets", e214);
      }
      try {
         final StringCursor sub215 = sub213.sub();
         sub215.accept(":");
         env[4] /* name */= Identifier(sub215, env);
         env[23] /* module */= ((org.fuwjin.gleux.Gleux)env[1]/* gleux */).getModule((java.lang.String)env[6]/* id */);
         env[5] /* script */= ((org.fuwjin.gleux.Gleux)env[23]/* module */).get((java.lang.String)env[4]/* name */);
         sub215.commit();
      } catch(final GleuxException e216) {
         final StringCursor sub217 = sub213.sub();
         env[5] /* script */= ((org.fuwjin.gleux.Gleux)env[1]/* gleux */).get((java.lang.String)env[6]/* id */);
         sub217.commit();
      }
      try {
         sub213.accept(">");
      } catch(final GleuxException e218) {
         throw new RuntimeException("script identifiers must be normal identifiers in angle brackets", e218);
      }
      S(sub213, env);
      sub213.commit();
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
      final StringCursor sub263 = input.sub();
      try {
         sub263.acceptIn("\t\n\r ");
      } catch(final GleuxException e264) {
         Comment(sub263, env);
      }
      try {
         while(true) {
            try {
               sub263.acceptIn("\t\n\r ");
            } catch(final GleuxException e265) {
               Comment(sub263, env);
            }
         }
      } catch(final GleuxException e266) {
         // continue
      }
      sub263.commit();
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
