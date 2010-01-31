# POGO serializer grammar
CodeGen         =org.fuwjin.pogo.PogoCodeGenerator
                <- 
'package ' Identifier~qualifiedPackage ';

import static org.fuwjin.pogo.PogoUtils.*;
import org.fuwjin.pogo.Grammar;

public final class ' Identifier~name ' extends Grammar{
  {
'    Grammar~grammar
'    resolve();
  }
  
  private static final Grammar grammar = new ' Identifier~name '();
  public static Grammar get' Identifier~name '(){
    return grammar;
  }
}
'
Grammar         =org.fuwjin.pogo.Grammar~iterator
                <- Definition~next+
Definition      =org.fuwjin.pogo.parser.Rule
                <- '    add(rule('
                   '"' Identifier~name '"' 
                   ', ' Type~type
                   ', ' Initializer~initializer
                   ', ' Finalizer~finalizer
                   ', ' Expression~parser 
                   '));\n'
Type            <- TRUE~this / NULL~this / ClassType~this / 'type()'
ClassType       =org.fuwjin.pogo.reflect.ClassType
                <- 'type(' ClassName~type '.class)'
ClassName       =java.lang.Class
                <- Identifier~getName
Initializer     <- NEW~this / INSTANCEOF~this / ContextInit~this / InitMethod~this / 'init()'
ContextInit     =org.fuwjin.pogo.reflect.ContextInitializerTask
                <- 'contextInit("' Identifier~name '")'
InitMethod      =org.fuwjin.pogo.reflect.StaticInitializerTask
                <- 'init("' Identifier~name '")'
Finalizer       <- ContextResult~this / ResultMethod~this / 'result()'
ContextResult   =org.fuwjin.pogo.reflect.ContextFinalizerTask
                <- 'contextResult("' Identifier~name '")'
ResultMethod    =org.fuwjin.pogo.reflect.ResultTask
                <- 'result("' Identifier~name '")'
Expression      <- Option~this / SequenceChain~this
Option          =org.fuwjin.pogo.parser.OptionParser~iterator
                <- 'option(' SequenceChain~next (', ' SequenceChain~next)* ')'
SequenceChain   <- Sequence~this / PrefixChain~this
Sequence        =org.fuwjin.pogo.parser.SequenceParser~iterator
                <- 'seq(' PrefixChain~next (', ' PrefixChain~next)* ')'
PrefixChain     <- Prefix~this / SuffixChain~this
Prefix          =org.fuwjin.pogo.parser.ParserOperator
                <- (AND~this / NOT~this) '(' SuffixChain~parser ')'
SuffixChain     <- Suffix~this / Primary~this
Suffix          =org.fuwjin.pogo.parser.ParserOperator
                <- (QUESTION~this / STAR~this / PLUS~this) '(' Primary~parser ')'
Primary         <- Reference~this / DOT~this / Literal~this / Range~this / Expression~this
Reference       =org.fuwjin.pogo.parser.RuleReferenceParser
                <- 'ref("' Identifier~ruleName '", ' Constructor~constructor ', ' Converter~converter ')'
Constructor     <- THIS~this / NEXT~this / ConstMethod~this / 'ignore()'
ConstMethod     =org.fuwjin.pogo.reflect.FactoryTask
                <- 'build("' Identifier~name '")'
Converter       <- RETURN~this / ConvMethod~this / 'ignore()'
ConvMethod      =org.fuwjin.pogo.reflect.AppendTask
                <- 'append("' Identifier~name '")'
Literal         =org.fuwjin.pogo.parser.CharacterLiteralParser
                <- 'lit(\'' Char~getLitChar '\')'
Identifier      <- .
Range           =org.fuwjin.pogo.parser.CharacterRangeParser
                <- 'range(\'' Char~getStart '\', \'' Char~getEnd '\')'
Char            <- .
NULL            =org.fuwjin.pogo.reflect.NullType~instanceof
                <- '_null()'
TRUE            =org.fuwjin.pogo.reflect.AllType~instanceof
                <- '_true()'
NEW             =org.fuwjin.pogo.reflect.ConstructorTask~instanceof
                <- '_new()'
NEXT            =org.fuwjin.pogo.reflect.NextTask~instanceof
                <- 'next()'
THIS            =org.fuwjin.pogo.reflect.PassThruTask~instanceof
                <- '_this()'
RETURN          =org.fuwjin.pogo.reflect.ReturnTask~instanceof
                <- '_return()'
INSTANCEOF      =org.fuwjin.pogo.reflect.InstanceOfTask~instanceof
                <- '_instanceof()'
AND             =org.fuwjin.pogo.parser.PositiveLookaheadParser~instanceof
                <- 'and'
NOT             =org.fuwjin.pogo.parser.NegativeLookaheadParser~instanceof
                <- 'not'
QUESTION        =org.fuwjin.pogo.parser.OptionalParser~instanceof
                <- 'optional'
STAR            =org.fuwjin.pogo.parser.OptionalSeriesParser~instanceof
                <- 'star'
PLUS            =org.fuwjin.pogo.parser.RequiredSeriesParser~instanceof
                <- 'plus'
DOT             =org.fuwjin.pogo.parser.CharacterParser~instanceof
                <- 'dot()'
