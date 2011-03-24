package org.fuwjin.gleux;

import org.fuwjin.postage.Postage;
import org.fuwjin.util.StringUtils;

/**
 * A static Gleux interpreter.
 */
public class GleuxInterpreter {
   /**
    * Thrown when a statement fails.
    */
   public static class GleuxException extends Exception {
      private static final long serialVersionUID = 1L;

      /**
       * Thrown when a statement fails.
       * @param message the message
       */
      public GleuxException(final String message) {
         super(message);
      }
   }

   private static class StringCursor {
      private int pos;
      private final CharSequence seq;
      private final int start;
      private final StringCursor parent;

      public StringCursor(final CharSequence seq) {
         this(0, seq, null);
      }

      private StringCursor(final int start, final CharSequence seq, final StringCursor parent) {
         this.start = start;
         pos = start;
         this.seq = seq;
         this.parent = parent;
      }

      public int accept() throws GleuxException {
         checkBounds(pos);
         return seq.charAt(pos++);
      }

      public int accept(final String expected) throws GleuxException {
         if(expected == null || expected.length() == 0) {
            throw new GleuxException("UNSET");
         }
         checkBounds(pos + expected.length() - 1);
         if(!seq.subSequence(pos, pos + expected.length()).equals(expected)) {
            throw new GleuxException("unexpected character");
         }
         pos = pos + expected.length();
         return seq.charAt(pos - 1);
      }

      public int acceptIn(final String set) throws GleuxException {
         checkBounds(pos);
         if(set.indexOf(seq.charAt(pos)) < 0) {
            throw new GleuxException("unexpected character");
         }
         return seq.charAt(pos++);
      }

      public int acceptNot(final String expected) throws GleuxException {
         if(expected == null || expected.length() == 0) {
            throw new GleuxException("UNSET");
         }
         checkBounds(pos + expected.length() - 1);
         if(seq.subSequence(pos, pos + expected.length()).equals(expected)) {
            throw new GleuxException("unexpected character");
         }
         return seq.charAt(pos++);
      }

      public int acceptNotIn(final String set) throws GleuxException {
         checkBounds(pos);
         if(set.indexOf(seq.charAt(pos)) >= 0) {
            throw new GleuxException("unexpected character");
         }
         return seq.charAt(pos++);
      }

      protected void checkBounds(final int p) throws GleuxException {
         if(p >= seq.length()) {
            throw new GleuxException("unexpected EOF");
         }
      }

      public void commit() {
         parent.pos = pos;
      }

      public int next() throws GleuxException {
         checkBounds(pos);
         return seq.charAt(pos);
      }

      public StringCursor sub() {
         return new StringCursor(pos, seq, this);
      }

      @Override
      public String toString() {
         return seq.subSequence(start, pos).toString();
      }
   }

   private static AbortStatement AbortStatement(final StringCursor input, final Gleux gleux, final Postage postage)
         throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("abort");
      Sep(sub);
      final AbortStatement ret = new AbortStatement(Value(sub, gleux, postage));
      sub.commit();
      return ret;
   }

   private static Expression AcceptStatement(final StringCursor input, final Gleux gleux, final Postage postage)
         throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("accept");
      Sep(sub);
      Boolean notted;
      try {
         final StringCursor sub2 = sub.sub();
         sub2.accept("not");
         Sep(sub2);
         notted = Boolean.TRUE;
         sub2.commit();
      } catch(final GleuxException e) {
         notted = Boolean.FALSE;
      }
      Expression stmt;
      try {
         stmt = new FilterAcceptStatement(notted, InFilter(sub));
      } catch(final GleuxException e) {
         stmt = new ValueAcceptStatement(notted, Value(sub, gleux, postage));
      }
      sub.commit();
      return stmt;
   }

   private static void AliasDeclaration(final StringCursor input, final Gleux gleux) throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("alias");
      Sep(sub);
      final String qname = QualifiedName(sub);
      sub.accept("as");
      Sep(sub);
      gleux.alias(qname, Name(sub));
      sub.commit();
   }

   private static void AnnotatedIdentifier(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      Identifier(sub);
      try {
         while(true) {
            final StringCursor sub1 = sub.sub();
            sub1.accept("[");
            try {
               Identifier(sub1);
            } catch(final GleuxException e) {
               // continue
            }
            sub1.accept("]");
            sub1.commit();
         }
      } catch(final GleuxException e) {
         // continue
      }
      sub.commit();
   }

   private static Assignment Assignment(final StringCursor input, final Gleux gleux, final Postage postage)
         throws GleuxException {
      final StringCursor sub = input.sub();
      final String name = Name(sub);
      sub.accept("=");
      S(sub);
      final Assignment ret = new Assignment(name, Value(sub, gleux, postage));
      sub.commit();
      return ret;
   }

   private static Block Block(final StringCursor input, final Gleux gleux, final Postage postage) throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("{");
      S(sub);
      final Block block = new Block();
      try {
         while(true) {
            block.add(Statement(sub, gleux, postage));
         }
      } catch(final GleuxException e) {
         // continue
      }
      sub.accept("}");
      S(sub);
      sub.commit();
      return block;
   }

   private static void Comment(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("#");
      try {
         while(true) {
            sub.acceptNotIn("\r\n");
         }
      } catch(final GleuxException e) {
         // continue
      }
      try {
         sub.accept("\r");
      } catch(final GleuxException e) {
         // continue
      }
      sub.accept("\n");
      sub.commit();
   }

   private static CouldStatement CouldStatement(final StringCursor input, final Gleux gleux, final Postage postage)
         throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("could");
      Sep(sub);
      final CouldStatement ret = new CouldStatement(Statement(sub, gleux, postage));
      sub.commit();
      return ret;
   }

   private static CompositeLiteral DynamicLiteral(final StringCursor input, final Gleux gleux) throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("\"");
      final CompositeLiteral lit = new CompositeLiteral();
      try {
         while(true) {
            try {
               sub.accept("'");
               lit.append(new Variable(Identifier(sub)));
               sub.accept("'");
            } catch(final GleuxException e) {
               try {
                  lit.append(gleux.get(Specification(sub)));
               } catch(final GleuxException e1) {
                  try {
                     lit.appendChar(Escape(sub));
                  } catch(final GleuxException e2) {
                     final int ch = sub.acceptNotIn("\\\"");
                     lit.appendChar(ch);
                  }
               }
            }
         }
      } catch(final GleuxException e) {
         // continue
      }
      sub.accept("\"");
      S(sub);
      sub.commit();
      return lit;
   }

   private static EitherOrStatement EitherOrStatement(final StringCursor input, final Gleux gleux, final Postage postage)
         throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("either");
      Sep(sub);
      final EitherOrStatement stmt = new EitherOrStatement(Statement(sub, gleux, postage));
      sub.accept("or");
      Sep(sub);
      stmt.or(Statement(sub, gleux, postage));
      try {
         while(true) {
            final StringCursor sub2 = sub.sub();
            sub2.accept("or");
            Sep(sub2);
            stmt.or(Statement(sub2, gleux, postage));
            sub2.commit();
         }
      } catch(final GleuxException e) {
         // continue
      }
      sub.commit();
      return stmt;
   }

   private static void EndOfFile(final StringCursor input) throws GleuxException {
      int ch = 0;
      try {
         ch = input.next();
      } catch(final GleuxException e) {
         // continue
      }
      if(ch != 0) {
         throw new GleuxException("Unexpected value");
      }
   }

   private static int Escape(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("\\");
      int ch;
      try {
         sub.accept("n");
         ch = '\n';
      } catch(final GleuxException e) {
         try {
            sub.accept("t");
            ch = '\t';
         } catch(final GleuxException e2) {
            try {
               sub.accept("r");
               ch = '\r';
            } catch(final GleuxException e3) {
               try {
                  sub.accept("x");
                  ch = Literal.parseHex(HexDigits(sub));
               } catch(final GleuxException e4) {
                  ch = sub.accept();
               }
            }
         }
      }
      sub.commit();
      return ch;
   }

   private static int FilterChar(final StringCursor input) throws GleuxException {
      int ch;
      try {
         ch = Escape(input);
      } catch(final GleuxException e) {
         ch = input.acceptNot("\\");
      }
      return ch;
   }

   private static void FilterRange(final StringCursor input, final Filter filter) throws GleuxException {
      final StringCursor sub = input.sub();
      final int start = FilterChar(sub);
      S(sub);
      try {
         final StringCursor sub2 = sub.sub();
         sub2.accept("-");
         S(sub2);
         filter.addRange(start, FilterChar(sub2));
         S(sub2);
         sub2.commit();
      } catch(final GleuxException e) {
         filter.addChar(start);
      }
      sub.commit();
   }

   private static void HexDigit(final StringCursor input) throws GleuxException {
      input.acceptIn("0123456789abcdefABCDEF");
   }

   private static String HexDigits(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      HexDigit(sub);
      HexDigit(sub);
      HexDigit(sub);
      HexDigit(sub);
      sub.commit();
      return sub.toString();
   }

   private static String Identifier(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      IdentifierChar(sub);
      try {
         while(true) {
            IdentifierChar(sub);
         }
      } catch(final GleuxException e) {
         // continue
      }
      sub.commit();
      return sub.toString();
   }

   private static void IdentifierChar(final StringCursor input) throws GleuxException {
      if(!Character.isJavaIdentifierPart(input.next())) {
         throw new GleuxException("not a valid identifier character");
      }
      input.accept();
   }

   private static Filter InFilter(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("in");
      Sep(sub);
      final Filter filter = new Filter();
      FilterRange(sub, filter);
      try {
         while(true) {
            final StringCursor sub2 = sub.sub();
            sub2.accept(",");
            S(sub2);
            FilterRange(sub2, filter);
            sub2.commit();
         }
      } catch(final GleuxException e) {
         // continue
      }
      sub.commit();
      return filter;
   }

   /**
    * Parses a Gleux structure for the input stream.
    * @param seq the input stream
    * @param postage the postage instance for resolving invocations
    * @return the Gleux structure
    * @throws GleuxException if it fails
    */
   public static Gleux interpret(final CharSequence seq, final Postage postage) throws GleuxException {
      final StringCursor input = new StringCursor(seq);
      final Gleux gleux = new Gleux();
      try {
         while(true) {
            AliasDeclaration(input, gleux);
         }
      } catch(final GleuxException e) {
         // continue
      }
      gleux.add(SpecificationDeclaration(input, gleux, postage));
      try {
         while(true) {
            gleux.add(SpecificationDeclaration(input, gleux, postage));
         }
      } catch(final GleuxException e) {
         // continue
      }
      EndOfFile(input);
      return gleux;
   }

   private static Invocation Invocation(final StringCursor input, final Gleux gleux, final Postage postage)
         throws GleuxException {
      final StringCursor sub = input.sub();
      final String prefix = Identifier(sub);
      final String alias = gleux.alias(prefix);
      String name;
      try {
         sub.accept(".");
         name = StringUtils.concatenate(alias, ".", QualifiedName(sub));
      } catch(final GleuxException e) {
         name = alias;
      }
      sub.accept("(");
      S(sub);
      final Invocation inv = new Invocation(name);
      try {
         inv.addParam(Value(sub, gleux, postage));
         try {
            while(true) {
               final StringCursor sub2 = sub.sub();
               sub2.accept(",");
               S(sub2);
               inv.addParam(Value(sub2, gleux, postage));
               sub2.commit();
            }
         } catch(final GleuxException e) {
            // continue
         }
      } catch(final GleuxException e) {
         // continue
      }
      sub.accept(")");
      S(sub);
      inv.resolve(postage);
      sub.commit();
      return inv;
   }

   private static IsStatement IsStatement(final StringCursor input, final Gleux gleux, final Postage postage)
         throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("is");
      Sep(sub);
      Boolean notted;
      try {
         final StringCursor sub2 = sub.sub();
         sub2.accept("not");
         Sep(sub2);
         notted = Boolean.TRUE;
         sub2.commit();
      } catch(final GleuxException e) {
         notted = Boolean.FALSE;
      }
      IsStatement stmt;
      try {
         stmt = new IsStatement(notted, new FilterAcceptStatement(Boolean.FALSE, InFilter(sub)));
      } catch(final GleuxException e) {
         try {
            stmt = new IsStatement(notted, new ValueAcceptStatement(Boolean.FALSE, StaticLiteral(sub)));
         } catch(final GleuxException e1) {
            stmt = new IsStatement(notted, Value(sub, gleux, postage));
         }
      }
      sub.commit();
      return stmt;
   }

   private static Expression MatchValue(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("match");
      Sep(sub);
      sub.commit();
      return Variable.MATCH;
   }

   private static String Name(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      final String id = Identifier(sub);
      S(sub);
      sub.commit();
      return id;
   }

   private static Expression NextValue(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("next");
      Sep(sub);
      sub.commit();
      return Variable.NEXT;
   }

   private static PublishStatement PublishStatement(final StringCursor input, final Gleux gleux, final Postage postage)
         throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("publish");
      Sep(sub);
      final PublishStatement ret = new PublishStatement(Value(sub, gleux, postage));
      sub.commit();
      return ret;
   }

   private static String QualifiedIdentifier(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      AnnotatedIdentifier(sub);
      try {
         while(true) {
            final StringCursor sub2 = sub.sub();
            sub2.accept(".");
            AnnotatedIdentifier(sub2);
            sub2.commit();
         }
      } catch(final GleuxException e) {
         // continue
      }
      sub.commit();
      return sub.toString();
   }

   private static String QualifiedName(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      final String id = QualifiedIdentifier(sub);
      S(sub);
      sub.commit();
      return id;
   }

   private static RepeatStatement RepeatStatement(final StringCursor input, final Gleux gleux, final Postage postage)
         throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("repeat");
      Sep(sub);
      final RepeatStatement ret = new RepeatStatement(Statement(sub, gleux, postage));
      sub.commit();
      return ret;
   }

   private static void S(final StringCursor input) {
      try {
         Space(input);
      } catch(final GleuxException e) {
         // continue
      }
   }

   private static void Sep(final StringCursor input) {
      try {
         final StringCursor sub = input.sub();
         IdentifierChar(sub);
         throw new GleuxException("Unexpected identifier char");
      } catch(final GleuxException e) {
         S(input);
      }
   }

   private static void Space(final StringCursor input) throws GleuxException {
      try {
         input.acceptIn(" \n\r\t");
      } catch(final GleuxException e) {
         Comment(input);
      }
      try {
         while(true) {
            try {
               input.acceptIn(" \n\r\t");
            } catch(final GleuxException e) {
               Comment(input);
            }
         }
      } catch(final GleuxException e) {
         // continue
      }
   }

   private static String Specification(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("<");
      final String id = Identifier(sub);
      sub.accept(">");
      S(sub);
      sub.commit();
      return id;
   }

   private static Declaration SpecificationDeclaration(final StringCursor input, final Gleux gleux,
         final Postage postage) throws GleuxException {
      final StringCursor sub = input.sub();
      final String name = Specification(sub);
      Declaration spec;
      try {
         sub.accept("{");
         S(sub);
         spec = new Declaration(name);
         try {
            while(true) {
               spec.add(Statement(sub, gleux, postage));
            }
         } catch(final GleuxException e) {
            // continue
         }
         try {
            final StringCursor sub2 = sub.sub();
            sub2.accept("return");
            Sep(sub2);
            spec.returns(Value(sub2, gleux, postage));
            sub2.commit();
         } catch(final GleuxException e) {
            // continue
         }
         sub.accept("}");
         S(sub);
         sub.commit();
      } catch(final GleuxException e) {
         throw new RuntimeException("Failure while constructing " + name);
      }
      return spec;
   }

   private static Expression Statement(final StringCursor input, final Gleux gleux, final Postage postage)
         throws GleuxException {
      Expression stmt;
      try {
         stmt = IsStatement(input, gleux, postage);
      } catch(final GleuxException e) {
         try {
            stmt = EitherOrStatement(input, gleux, postage);
         } catch(final GleuxException e1) {
            try {
               stmt = CouldStatement(input, gleux, postage);
            } catch(final GleuxException e2) {
               try {
                  stmt = RepeatStatement(input, gleux, postage);
               } catch(final GleuxException e3) {
                  try {
                     stmt = AcceptStatement(input, gleux, postage);
                  } catch(final GleuxException e4) {
                     try {
                        stmt = PublishStatement(input, gleux, postage);
                     } catch(final GleuxException e5) {
                        try {
                           stmt = AbortStatement(input, gleux, postage);
                        } catch(final GleuxException e6) {
                           try {
                              stmt = gleux.get(Specification(input));
                           } catch(final GleuxException e7) {
                              try {
                                 stmt = Block(input, gleux, postage);
                              } catch(final GleuxException e8) {
                                 try {
                                    stmt = Assignment(input, gleux, postage);
                                 } catch(final GleuxException e9) {
                                    stmt = Invocation(input, gleux, postage);
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
      return stmt;
   }

   private static Literal StaticLiteral(final StringCursor input) throws GleuxException {
      final StringCursor sub = input.sub();
      sub.accept("'");
      final Literal lit = new Literal();
      try {
         while(true) {
            try {
               final int ch = sub.acceptNotIn("\\'");
               lit.append(ch);
            } catch(final GleuxException e) {
               lit.append(Escape(sub));
            }
         }
      } catch(final GleuxException e) {
         // continue
      }
      sub.accept("'");
      S(sub);
      sub.commit();
      return lit;
   }

   private static Expression Value(final StringCursor input, final Gleux gleux, final Postage postage)
         throws GleuxException {
      Expression val;
      try {
         val = gleux.get(Specification(input));
      } catch(final GleuxException e) {
         try {
            val = StaticLiteral(input);
         } catch(final GleuxException e6) {
            try {
               val = DynamicLiteral(input, gleux);
            } catch(final GleuxException e1) {
               try {
                  val = AcceptStatement(input, gleux, postage);
               } catch(final GleuxException e2) {
                  try {
                     val = MatchValue(input);
                  } catch(final GleuxException e3) {
                     try {
                        val = NextValue(input);
                     } catch(final GleuxException e4) {
                        try {
                           val = Invocation(input, gleux, postage);
                        } catch(final GleuxException e5) {
                           val = new Variable(Name(input));
                        }
                     }
                  }
               }
            }
         }
      }
      return val;
   }
}
