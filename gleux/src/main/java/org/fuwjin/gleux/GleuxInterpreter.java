package org.fuwjin.gleux;

import java.util.Map;

public class GleuxInterpreter {
  public static class GleuxException extends Exception {
    private GleuxException(final String message) {
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
    
    public StringCursor(final int start, final CharSequence seq, final StringCursor parent) {
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
      final CharSequence sub = seq.subSequence(pos, pos + expected.length());
      if(!sub.equals(expected)) {
        throw new GleuxException("unexpected character "+sub+"  "+expected);
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
    
    public String toString() {
      return seq.subSequence(start, pos).toString();
    }
  }

  private static Object AliasDeclaration(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub1 = input.sub();
    sub1.accept("alias");
    Sep(sub1, env);
    try {
      env[0] /*qname*/= QualifiedName(sub1, env);
    } catch(final GleuxException e2) {
      throw new RuntimeException("alias keyword requires a qualified name", e2);
    }
    try {
      sub1.accept("as");
    } catch(final GleuxException e3) {
      throw new RuntimeException("alias keyword requires as keyword", e3);
    }
    Sep(sub1, env);
    try {
      ((org.fuwjin.gleux.Gleux)env[1]/*gleux*/).alias((java.lang.String)env[0]/*qname*/, (java.lang.String)Name(sub1, env));
    } catch(final GleuxException e4) {
      throw new RuntimeException("alias-as keywords require a name", e4);
    }
    sub1.commit();
    return null;
  }

  private static Object SpecificationDeclaration(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub5 = input.sub();
    env[2] /*name*/= Specification(sub5, env);
    try {
      sub5.accept("{");
    } catch(final GleuxException e6) {
      throw new RuntimeException("specification declarations must start with a brace", e6);
    }
    S(sub5, env);
    env[3] /*spec*/= new org.fuwjin.gleux.Declaration((java.lang.String)env[2]/*name*/);
    try {
      ((org.fuwjin.gleux.Declaration)env[3]/*spec*/).add((org.fuwjin.gleux.Expression)Statement(sub5, env));
      try {
        while(true) {
          ((org.fuwjin.gleux.Declaration)env[3]/*spec*/).add((org.fuwjin.gleux.Expression)Statement(sub5, env));
        }
      } catch(final GleuxException e7) {
        //continue
      }
    } catch(final GleuxException e8) {
      //continue
    }
    try {
      final StringCursor sub9 = sub5.sub();
      sub9.accept("return");
      Sep(sub9, env);
      try {
        ((org.fuwjin.gleux.Declaration)env[3]/*spec*/).returns((org.fuwjin.gleux.Expression)Value(sub9, env));
      } catch(final GleuxException e10) {
        throw new RuntimeException("return keyword requires a value", e10);
      }
      sub9.commit();
    } catch(final GleuxException e11) {
      //continue
    }
    try {
      sub5.accept("}");
    } catch(final GleuxException e12) {
      throw new RuntimeException("specification declaration for " + env[2]/*name*/ + " must end with a brace", e12);
    }
    S(sub5, env);
    sub5.commit();
    return env[3]/*spec*/;
  }

  private static Object EndOfFile(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub13 = input.sub();
    final StringCursor sub14 = sub13.sub();
    boolean b = true;
    try {
      if((Object)sub14.next() == Boolean.FALSE) {
        b = false;
      }
    } catch(final GleuxException e15) {
      b = false;
    }
    if(b){
      throw new GleuxException("unexpected value");
    }
    sub13.commit();
    return null;
  }

  private static Object Gleux(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub16 = input.sub();
    env[1] /*gleux*/= new org.fuwjin.gleux.Gleux();
    try {
      AliasDeclaration(sub16, env);
      try {
        while(true) {
          AliasDeclaration(sub16, env);
        }
      } catch(final GleuxException e17) {
        //continue
      }
    } catch(final GleuxException e18) {
      //continue
    }
    ((org.fuwjin.gleux.Gleux)env[1]/*gleux*/).add((org.fuwjin.gleux.Declaration)SpecificationDeclaration(sub16, env));
    try {
      while(true) {
        ((org.fuwjin.gleux.Gleux)env[1]/*gleux*/).add((org.fuwjin.gleux.Declaration)SpecificationDeclaration(sub16, env));
      }
    } catch(final GleuxException e19) {
      //continue
    }
    EndOfFile(sub16, env);
    sub16.commit();
    return env[1]/*gleux*/;
  }

  private static Object Sep(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub20 = input.sub();
    final StringCursor sub21 = sub20.sub();
    boolean b = true;
    try {
      if((Object)IdentifierChar(sub21, env) == Boolean.FALSE) {
        b = false;
      }
    } catch(final GleuxException e22) {
      b = false;
    }
    if(b){
      throw new GleuxException("unexpected value");
    }
    S(sub20, env);
    sub20.commit();
    return null;
  }

  private static Object QualifiedName(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub23 = input.sub();
    env[4] /*id*/= QualifiedIdentifier(sub23, env);
    S(sub23, env);
    sub23.commit();
    return env[4]/*id*/;
  }

  private static Object Name(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub24 = input.sub();
    env[4] /*id*/= Identifier(sub24, env);
    S(sub24, env);
    sub24.commit();
    return env[4]/*id*/;
  }

  private static Object Specification(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub25 = input.sub();
    sub25.accept("<");
    try {
      env[4] /*id*/= Identifier(sub25, env);
    } catch(final GleuxException e26) {
      throw new RuntimeException("specifications must be an identifier enclosed in angle brackets", e26);
    }
    try {
      sub25.accept(">");
    } catch(final GleuxException e27) {
      throw new RuntimeException("specifications must end with an angle bracket", e27);
    }
    S(sub25, env);
    sub25.commit();
    return env[4]/*id*/;
  }

  private static Object S(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub28 = input.sub();
    try {
      Space(sub28, env);
    } catch(final GleuxException e29) {
      //continue
    }
    sub28.commit();
    return null;
  }

  private static Object Statement(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub30 = input.sub();
    try {
      env[5] /*stmt*/= IsStatement(sub30, env);
    } catch(final GleuxException e31) {
      try {
        env[5] /*stmt*/= EitherOrStatement(sub30, env);
      } catch(final GleuxException e32) {
        try {
          env[5] /*stmt*/= CouldStatement(sub30, env);
        } catch(final GleuxException e33) {
          try {
            env[5] /*stmt*/= RepeatStatement(sub30, env);
          } catch(final GleuxException e34) {
            try {
              env[5] /*stmt*/= AcceptStatement(sub30, env);
            } catch(final GleuxException e35) {
              try {
                env[5] /*stmt*/= PublishStatement(sub30, env);
              } catch(final GleuxException e36) {
                try {
                  env[5] /*stmt*/= AbortStatement(sub30, env);
                } catch(final GleuxException e37) {
                  try {
                    env[5] /*stmt*/= ((org.fuwjin.gleux.Gleux)env[1]/*gleux*/).get((java.lang.String)Specification(sub30, env));
                  } catch(final GleuxException e38) {
                    try {
                      env[5] /*stmt*/= Block(sub30, env);
                    } catch(final GleuxException e39) {
                      try {
                        env[5] /*stmt*/= Assignment(sub30, env);
                      } catch(final GleuxException e40) {
                        env[5] /*stmt*/= Invocation(sub30, env);
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
    sub30.commit();
    return env[5]/*stmt*/;
  }

  private static Object Value(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub41 = input.sub();
    try {
      env[6] /*val*/= ((org.fuwjin.gleux.Gleux)env[1]/*gleux*/).get((java.lang.String)Specification(sub41, env));
    } catch(final GleuxException e42) {
      try {
        env[6] /*val*/= StaticLiteral(sub41, env);
      } catch(final GleuxException e43) {
        try {
          env[6] /*val*/= DynamicLiteral(sub41, env);
        } catch(final GleuxException e44) {
          try {
            env[6] /*val*/= AcceptStatement(sub41, env);
          } catch(final GleuxException e45) {
            try {
              env[6] /*val*/= MatchValue(sub41, env);
            } catch(final GleuxException e46) {
              try {
                env[6] /*val*/= NextValue(sub41, env);
              } catch(final GleuxException e47) {
                try {
                  env[6] /*val*/= Invocation(sub41, env);
                } catch(final GleuxException e48) {
                  env[6] /*val*/= new org.fuwjin.gleux.Variable((java.lang.String)Name(sub41, env));
                }
              }
            }
          }
        }
      }
    }
    sub41.commit();
    return env[6]/*val*/;
  }

  private static Object StaticLiteral(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub49 = input.sub();
    sub49.accept("'");
    env[7] /*lit*/= new org.fuwjin.gleux.Literal();
    try {
      try {
        final StringCursor sub50 = sub49.sub();
        env[8] /*ch*/= sub50.acceptNotIn("'\\");
        ((org.fuwjin.gleux.Literal)env[7]/*lit*/).append((java.lang.Integer)env[8]/*ch*/);
        sub50.commit();
      } catch(final GleuxException e51) {
        final StringCursor sub52 = sub49.sub();
        ((org.fuwjin.gleux.Literal)env[7]/*lit*/).append((java.lang.Integer)Escape(sub52, env));
        sub52.commit();
      }
      try {
        while(true) {
          try {
            final StringCursor sub53 = sub49.sub();
            env[8] /*ch*/= sub53.acceptNotIn("'\\");
            ((org.fuwjin.gleux.Literal)env[7]/*lit*/).append((java.lang.Integer)env[8]/*ch*/);
            sub53.commit();
          } catch(final GleuxException e54) {
            final StringCursor sub55 = sub49.sub();
            ((org.fuwjin.gleux.Literal)env[7]/*lit*/).append((java.lang.Integer)Escape(sub55, env));
            sub55.commit();
          }
        }
      } catch(final GleuxException e56) {
        //continue
      }
    } catch(final GleuxException e57) {
      //continue
    }
    try {
      sub49.accept("'");
    } catch(final GleuxException e58) {
      throw new RuntimeException("static literals must end with a quote", e58);
    }
    S(sub49, env);
    sub49.commit();
    return env[7]/*lit*/;
  }

  private static Object DynamicLiteral(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub59 = input.sub();
    sub59.accept("\"");
    env[7] /*lit*/= new org.fuwjin.gleux.CompositeLiteral();
    try {
      try {
        final StringCursor sub60 = sub59.sub();
        sub60.accept("'");
        ((org.fuwjin.gleux.CompositeLiteral)env[7]/*lit*/).append((org.fuwjin.gleux.Expression)new org.fuwjin.gleux.Variable((java.lang.String)Identifier(sub60, env)));
        sub60.accept("'");
        sub60.commit();
      } catch(final GleuxException e61) {
        try {
          final StringCursor sub62 = sub59.sub();
          sub62.accept("<");
          ((org.fuwjin.gleux.CompositeLiteral)env[7]/*lit*/).append((org.fuwjin.gleux.Expression)((org.fuwjin.gleux.Gleux)env[1]/*gleux*/).get((java.lang.String)Identifier(sub62, env)));
          sub62.accept(">");
          sub62.commit();
        } catch(final GleuxException e63) {
          try {
            final StringCursor sub64 = sub59.sub();
            ((org.fuwjin.gleux.CompositeLiteral)env[7]/*lit*/).appendChar((java.lang.Integer)Escape(sub64, env));
            sub64.commit();
          } catch(final GleuxException e65) {
            final StringCursor sub66 = sub59.sub();
            env[8] /*ch*/= sub66.acceptNotIn("\"\\");
            ((org.fuwjin.gleux.CompositeLiteral)env[7]/*lit*/).appendChar((java.lang.Integer)env[8]/*ch*/);
            sub66.commit();
          }
        }
      }
      try {
        while(true) {
          try {
            final StringCursor sub67 = sub59.sub();
            sub67.accept("'");
            ((org.fuwjin.gleux.CompositeLiteral)env[7]/*lit*/).append((org.fuwjin.gleux.Expression)new org.fuwjin.gleux.Variable((java.lang.String)Identifier(sub67, env)));
            sub67.accept("'");
            sub67.commit();
          } catch(final GleuxException e68) {
            try {
              final StringCursor sub69 = sub59.sub();
              sub69.accept("<");
              ((org.fuwjin.gleux.CompositeLiteral)env[7]/*lit*/).append((org.fuwjin.gleux.Expression)((org.fuwjin.gleux.Gleux)env[1]/*gleux*/).get((java.lang.String)Identifier(sub69, env)));
              sub69.accept(">");
              sub69.commit();
            } catch(final GleuxException e70) {
              try {
                final StringCursor sub71 = sub59.sub();
                ((org.fuwjin.gleux.CompositeLiteral)env[7]/*lit*/).appendChar((java.lang.Integer)Escape(sub71, env));
                sub71.commit();
              } catch(final GleuxException e72) {
                final StringCursor sub73 = sub59.sub();
                env[8] /*ch*/= sub73.acceptNotIn("\"\\");
                ((org.fuwjin.gleux.CompositeLiteral)env[7]/*lit*/).appendChar((java.lang.Integer)env[8]/*ch*/);
                sub73.commit();
              }
            }
          }
        }
      } catch(final GleuxException e74) {
        //continue
      }
    } catch(final GleuxException e75) {
      //continue
    }
    try {
      sub59.accept("\"");
    } catch(final GleuxException e76) {
      throw new RuntimeException("dynamic literals must end with a double quote", e76);
    }
    S(sub59, env);
    sub59.commit();
    return env[7]/*lit*/;
  }

  private static Object AcceptStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub77 = input.sub();
    sub77.accept("accept");
    Sep(sub77, env);
    try {
      final StringCursor sub78 = sub77.sub();
      sub78.accept("not");
      Sep(sub78, env);
      env[9] /*notted*/= java.lang.Boolean.TRUE;
      sub78.commit();
    } catch(final GleuxException e79) {
      final StringCursor sub80 = sub77.sub();
      env[9] /*notted*/= java.lang.Boolean.FALSE;
      sub80.commit();
    }
    try {
      env[5] /*stmt*/= new org.fuwjin.gleux.FilterAcceptStatement((java.lang.Boolean)env[9]/*notted*/, (org.fuwjin.gleux.Filter)InFilter(sub77, env));
    } catch(final GleuxException e81) {
      try {
        env[5] /*stmt*/= new org.fuwjin.gleux.ValueAcceptStatement((java.lang.Boolean)env[9]/*notted*/, (org.fuwjin.gleux.Expression)Value(sub77, env));
      } catch(final GleuxException e82) {
        throw new RuntimeException("accept keyword requires a value or in keyword", e82);
      }
    }
    sub77.commit();
    return env[5]/*stmt*/;
  }

  private static Object MatchValue(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub83 = input.sub();
    sub83.accept("match");
    Sep(sub83, env);
    sub83.commit();
    return org.fuwjin.gleux.Variable.MATCH;
  }

  private static Object NextValue(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub84 = input.sub();
    sub84.accept("next");
    Sep(sub84, env);
    sub84.commit();
    return org.fuwjin.gleux.Variable.NEXT;
  }

  private static Object Invocation(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub85 = input.sub();
    env[10] /*prefix*/= Identifier(sub85, env);
    env[11] /*alias*/= ((org.fuwjin.gleux.Gleux)env[1]/*gleux*/).alias((java.lang.String)env[10]/*prefix*/);
    try {
      final StringCursor sub86 = sub85.sub();
      sub86.accept(".");
      env[2] /*name*/= env[11]/*alias*/ + "." + QualifiedName(sub86, env);
      sub86.commit();
    } catch(final GleuxException e87) {
      final StringCursor sub88 = sub85.sub();
      env[2] /*name*/= env[11]/*alias*/;
      sub88.commit();
    }
    sub85.accept("(");
    S(sub85, env);
    env[12] /*inv*/= new org.fuwjin.gleux.Invocation((java.lang.String)env[2]/*name*/);
    try {
      final StringCursor sub89 = sub85.sub();
      ((org.fuwjin.gleux.Invocation)env[12]/*inv*/).addParam((org.fuwjin.gleux.Expression)Value(sub89, env));
      try {
        final StringCursor sub90 = sub89.sub();
        sub90.accept(",");
        S(sub90, env);
        try {
          ((org.fuwjin.gleux.Invocation)env[12]/*inv*/).addParam((org.fuwjin.gleux.Expression)Value(sub90, env));
        } catch(final GleuxException e91) {
          throw new RuntimeException("invocation parameter must be a value", e91);
        }
        sub90.commit();
        try {
          while(true) {
            final StringCursor sub92 = sub89.sub();
            sub92.accept(",");
            S(sub92, env);
            try {
              ((org.fuwjin.gleux.Invocation)env[12]/*inv*/).addParam((org.fuwjin.gleux.Expression)Value(sub92, env));
            } catch(final GleuxException e93) {
              throw new RuntimeException("invocation parameter must be a value", e93);
            }
            sub92.commit();
          }
        } catch(final GleuxException e94) {
          //continue
        }
      } catch(final GleuxException e95) {
        //continue
      }
      sub89.commit();
    } catch(final GleuxException e96) {
      //continue
    }
    try {
      sub85.accept(")");
    } catch(final GleuxException e97) {
      throw new RuntimeException("invocation must end with a parenthesis", e97);
    }
    S(sub85, env);
    ((org.fuwjin.gleux.Invocation)env[12]/*inv*/).resolve((org.fuwjin.postage.Postage)env[13]/*postage*/);
    sub85.commit();
    return env[12]/*inv*/;
  }

  private static Object IsStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub98 = input.sub();
    sub98.accept("is");
    Sep(sub98, env);
    try {
      final StringCursor sub99 = sub98.sub();
      sub99.accept("not");
      Sep(sub99, env);
      env[9] /*notted*/= java.lang.Boolean.TRUE;
      sub99.commit();
    } catch(final GleuxException e100) {
      final StringCursor sub101 = sub98.sub();
      env[9] /*notted*/= java.lang.Boolean.FALSE;
      sub101.commit();
    }
    try {
      env[5] /*stmt*/= new org.fuwjin.gleux.IsStatement((java.lang.Boolean)env[9]/*notted*/, (org.fuwjin.gleux.Expression)new org.fuwjin.gleux.FilterAcceptStatement((java.lang.Boolean)java.lang.Boolean.FALSE, (org.fuwjin.gleux.Filter)InFilter(sub98, env)));
    } catch(final GleuxException e102) {
      try {
        env[5] /*stmt*/= new org.fuwjin.gleux.IsStatement((java.lang.Boolean)env[9]/*notted*/, (org.fuwjin.gleux.Expression)new org.fuwjin.gleux.ValueAcceptStatement((java.lang.Boolean)java.lang.Boolean.FALSE, (org.fuwjin.gleux.Expression)StaticLiteral(sub98, env)));
      } catch(final GleuxException e103) {
        try {
          env[5] /*stmt*/= new org.fuwjin.gleux.IsStatement((java.lang.Boolean)env[9]/*notted*/, (org.fuwjin.gleux.Expression)Value(sub98, env));
        } catch(final GleuxException e104) {
          throw new RuntimeException("is keyword requires value or in keyword", e104);
        }
      }
    }
    sub98.commit();
    return env[5]/*stmt*/;
  }

  private static Object EitherOrStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub105 = input.sub();
    sub105.accept("either");
    Sep(sub105, env);
    try {
      env[5] /*stmt*/= new org.fuwjin.gleux.EitherOrStatement((org.fuwjin.gleux.Expression)Statement(sub105, env));
    } catch(final GleuxException e106) {
      throw new RuntimeException("either keyword requires a statement", e106);
    }
    try {
      final StringCursor sub107 = sub105.sub();
      sub107.accept("or");
      Sep(sub107, env);
      try {
        ((org.fuwjin.gleux.EitherOrStatement)env[5]/*stmt*/).or((org.fuwjin.gleux.Expression)Statement(sub107, env));
      } catch(final GleuxException e108) {
        throw new RuntimeException("or keyword requires a statement", e108);
      }
      sub107.commit();
      try {
        while(true) {
          final StringCursor sub109 = sub105.sub();
          sub109.accept("or");
          Sep(sub109, env);
          try {
            ((org.fuwjin.gleux.EitherOrStatement)env[5]/*stmt*/).or((org.fuwjin.gleux.Expression)Statement(sub109, env));
          } catch(final GleuxException e110) {
            throw new RuntimeException("or keyword requires a statement", e110);
          }
          sub109.commit();
        }
      } catch(final GleuxException e111) {
        //continue
      }
    } catch(final GleuxException e112) {
      throw new RuntimeException("either keyword requires at least one or keyword", e112);
    }
    sub105.commit();
    return env[5]/*stmt*/;
  }

  private static Object CouldStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub113 = input.sub();
    sub113.accept("could");
    Sep(sub113, env);
    try {
      env[5] /*stmt*/= new org.fuwjin.gleux.CouldStatement((org.fuwjin.gleux.Expression)Statement(sub113, env));
    } catch(final GleuxException e114) {
      throw new RuntimeException("could keyword requires a statement", e114);
    }
    sub113.commit();
    return env[5]/*stmt*/;
  }

  private static Object RepeatStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub115 = input.sub();
    sub115.accept("repeat");
    Sep(sub115, env);
    try {
      env[5] /*stmt*/= new org.fuwjin.gleux.RepeatStatement((org.fuwjin.gleux.Expression)Statement(sub115, env));
    } catch(final GleuxException e116) {
      throw new RuntimeException("repeat keyword requires a statement", e116);
    }
    sub115.commit();
    return env[5]/*stmt*/;
  }

  private static Object PublishStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub117 = input.sub();
    sub117.accept("publish");
    Sep(sub117, env);
    try {
      env[5] /*stmt*/= new org.fuwjin.gleux.PublishStatement((org.fuwjin.gleux.Expression)Value(sub117, env));
    } catch(final GleuxException e118) {
      throw new RuntimeException("publish keyword requires a value", e118);
    }
    sub117.commit();
    return env[5]/*stmt*/;
  }

  private static Object AbortStatement(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub119 = input.sub();
    sub119.accept("abort");
    Sep(sub119, env);
    try {
      env[5] /*stmt*/= new org.fuwjin.gleux.AbortStatement((org.fuwjin.gleux.Expression)Value(sub119, env));
    } catch(final GleuxException e120) {
      throw new RuntimeException("abort keyword requires a value", e120);
    }
    sub119.commit();
    return env[5]/*stmt*/;
  }

  private static Object Block(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub121 = input.sub();
    sub121.accept("{");
    S(sub121, env);
    env[14] /*block*/= new org.fuwjin.gleux.Block();
    try {
      ((org.fuwjin.gleux.Block)env[14]/*block*/).add((org.fuwjin.gleux.Expression)Statement(sub121, env));
      try {
        while(true) {
          ((org.fuwjin.gleux.Block)env[14]/*block*/).add((org.fuwjin.gleux.Expression)Statement(sub121, env));
        }
      } catch(final GleuxException e122) {
        //continue
      }
    } catch(final GleuxException e123) {
      //continue
    }
    try {
      sub121.accept("}");
    } catch(final GleuxException e124) {
      throw new RuntimeException("block must end with a brace", e124);
    }
    S(sub121, env);
    sub121.commit();
    return env[14]/*block*/;
  }

  private static Object Assignment(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub125 = input.sub();
    env[2] /*name*/= Name(sub125, env);
    sub125.accept("=");
    S(sub125, env);
    try {
      env[5] /*stmt*/= new org.fuwjin.gleux.Assignment((java.lang.String)env[2]/*name*/, (org.fuwjin.gleux.Expression)Value(sub125, env));
    } catch(final GleuxException e126) {
      throw new RuntimeException("assignment requires a value", e126);
    }
    sub125.commit();
    return env[5]/*stmt*/;
  }

  private static Object InFilter(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub127 = input.sub();
    sub127.accept("in");
    Sep(sub127, env);
    env[15] /*filter*/= new org.fuwjin.gleux.Filter();
    try {
      FilterRange(sub127, env);
    } catch(final GleuxException e128) {
      throw new RuntimeException("in keyword requires at least one filter", e128);
    }
    try {
      final StringCursor sub129 = sub127.sub();
      sub129.accept(",");
      S(sub129, env);
      try {
        FilterRange(sub129, env);
      } catch(final GleuxException e130) {
        throw new RuntimeException("in keyword requires a filter after a comma", e130);
      }
      sub129.commit();
      try {
        while(true) {
          final StringCursor sub131 = sub127.sub();
          sub131.accept(",");
          S(sub131, env);
          try {
            FilterRange(sub131, env);
          } catch(final GleuxException e132) {
            throw new RuntimeException("in keyword requires a filter after a comma", e132);
          }
          sub131.commit();
        }
      } catch(final GleuxException e133) {
        //continue
      }
    } catch(final GleuxException e134) {
      //continue
    }
    sub127.commit();
    return env[15]/*filter*/;
  }

  private static Object Identifier(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub135 = input.sub();
    IdentifierChar(sub135, env);
    try {
      while(true) {
        IdentifierChar(sub135, env);
      }
    } catch(final GleuxException e136) {
      //continue
    }
    sub135.commit();
    return sub135.toString();
  }

  private static Object FilterRange(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub137 = input.sub();
    env[16] /*start*/= FilterChar(sub137, env);
    S(sub137, env);
    try {
      final StringCursor sub138 = sub137.sub();
      sub138.accept("-");
      S(sub138, env);
      ((org.fuwjin.gleux.Filter)env[15]/*filter*/).addRange((java.lang.Integer)env[16]/*start*/, (java.lang.Integer)FilterChar(sub138, env));
      S(sub138, env);
      sub138.commit();
    } catch(final GleuxException e139) {
      final StringCursor sub140 = sub137.sub();
      ((org.fuwjin.gleux.Filter)env[15]/*filter*/).addChar((java.lang.Integer)env[16]/*start*/);
      sub140.commit();
    }
    sub137.commit();
    return null;
  }

  private static Object FilterChar(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub141 = input.sub();
    try {
      final StringCursor sub142 = sub141.sub();
      env[8] /*ch*/= Escape(sub142, env);
      sub142.commit();
    } catch(final GleuxException e143) {
      env[8] /*ch*/= sub141.acceptNot("\\");
    }
    sub141.commit();
    return env[8]/*ch*/;
  }

  private static Object Escape(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub144 = input.sub();
    sub144.accept("\\");
    try {
      final StringCursor sub145 = sub144.sub();
      sub145.accept("n");
      env[8] /*ch*/= org.fuwjin.gleux.Literal.NEW_LINE;
      sub145.commit();
    } catch(final GleuxException e146) {
      try {
        final StringCursor sub147 = sub144.sub();
        sub147.accept("t");
        env[8] /*ch*/= org.fuwjin.gleux.Literal.TAB;
        sub147.commit();
      } catch(final GleuxException e148) {
        try {
          final StringCursor sub149 = sub144.sub();
          sub149.accept("r");
          env[8] /*ch*/= org.fuwjin.gleux.Literal.RETURN;
          sub149.commit();
        } catch(final GleuxException e150) {
          try {
            final StringCursor sub151 = sub144.sub();
            sub151.accept("x");
            env[8] /*ch*/= org.fuwjin.gleux.Literal.parseHex((java.lang.String)HexDigits(sub151, env));
            sub151.commit();
          } catch(final GleuxException e152) {
            final StringCursor sub153 = sub144.sub();
            env[8] /*ch*/= sub153.accept();
            sub153.commit();
          }
        }
      }
    }
    sub144.commit();
    return env[8]/*ch*/;
  }

  private static Object HexDigits(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub154 = input.sub();
    HexDigit(sub154, env);
    HexDigit(sub154, env);
    HexDigit(sub154, env);
    HexDigit(sub154, env);
    sub154.commit();
    return sub154.toString();
  }

  private static Object HexDigit(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub155 = input.sub();
    sub155.acceptIn("0123456789ABCDEFabcdef");
    sub155.commit();
    return null;
  }

  private static Object QualifiedIdentifier(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub156 = input.sub();
    AnnotatedIdentifier(sub156, env);
    try {
      final StringCursor sub157 = sub156.sub();
      sub157.accept(".");
      AnnotatedIdentifier(sub157, env);
      sub157.commit();
      try {
        while(true) {
          final StringCursor sub158 = sub156.sub();
          sub158.accept(".");
          AnnotatedIdentifier(sub158, env);
          sub158.commit();
        }
      } catch(final GleuxException e159) {
        //continue
      }
    } catch(final GleuxException e160) {
      //continue
    }
    sub156.commit();
    return sub156.toString();
  }

  private static Object AnnotatedIdentifier(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub161 = input.sub();
    Identifier(sub161, env);
    try {
      final StringCursor sub162 = sub161.sub();
      sub162.accept("[");
      try {
        Identifier(sub162, env);
      } catch(final GleuxException e163) {
        //continue
      }
      sub162.accept("]");
      sub162.commit();
      try {
        while(true) {
          final StringCursor sub164 = sub161.sub();
          sub164.accept("[");
          try {
            Identifier(sub164, env);
          } catch(final GleuxException e165) {
            //continue
          }
          sub164.accept("]");
          sub164.commit();
        }
      } catch(final GleuxException e166) {
        //continue
      }
    } catch(final GleuxException e167) {
      //continue
    }
    sub161.commit();
    return null;
  }

  private static Object IdentifierChar(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub168 = input.sub();
    final StringCursor sub169 = sub168.sub();
    if((Object)java.lang.Character.isJavaIdentifierPart((java.lang.Integer)sub169.next()) == Boolean.FALSE) {
      throw new GleuxException("check failed");
    }
    sub168.accept();
    sub168.commit();
    return null;
  }

  private static Object Space(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub170 = input.sub();
    try {
      sub170.acceptIn("\t\n\r ");
    } catch(final GleuxException e171) {
      Comment(sub170, env);
    }
    try {
      while(true) {
        try {
          sub170.acceptIn("\t\n\r ");
        } catch(final GleuxException e172) {
          Comment(sub170, env);
        }
      }
    } catch(final GleuxException e173) {
      //continue
    }
    sub170.commit();
    return null;
  }

  private static Object Comment(final StringCursor input, final Object... parentEnv) throws GleuxException {
    final Object[] env = new Object[parentEnv.length];
    System.arraycopy(parentEnv, 0, env, 0, env.length);
    final StringCursor sub174 = input.sub();
    sub174.accept("#");
    try {
      sub174.acceptNotIn("\n\r");
      try {
        while(true) {
          sub174.acceptNotIn("\n\r");
        }
      } catch(final GleuxException e175) {
        //continue
      }
    } catch(final GleuxException e176) {
      //continue
    }
    try {
      sub174.accept("\r");
    } catch(final GleuxException e177) {
      //continue
    }
    try {
      sub174.accept("\n");
    } catch(final GleuxException e178) {
      EndOfFile(sub174, env);
    }
    sub174.commit();
    return null;
  }
  
  public static Object interpret(final CharSequence in, final Map<String, ?> environment) throws GleuxException {
    final StringCursor input = new StringCursor(in);
    final Object[] env = new Object[17];
    env[0] = environment.get("qname");
    env[1] = environment.get("gleux");
    env[2] = environment.get("name");
    env[3] = environment.get("spec");
    env[4] = environment.get("id");
    env[5] = environment.get("stmt");
    env[6] = environment.get("val");
    env[7] = environment.get("lit");
    env[8] = environment.get("ch");
    env[9] = environment.get("notted");
    env[10] = environment.get("prefix");
    env[11] = environment.get("alias");
    env[12] = environment.get("inv");
    env[13] = environment.get("postage");
    env[14] = environment.get("block");
    env[15] = environment.get("filter");
    env[16] = environment.get("start");
    return Gleux(input, env);
  }
}
