# POGO serializer grammar
CodeGen         =org.fuwjin.pogo.PredefinedGrammar$PogoCodeGenerator
                <- 
'package ' Identifier~packageName ';

import static org.fuwjin.pogo.LiteratePogo.*;
import org.fuwjin.pogo.Grammar;

public final class ' Identifier~name ' extends Grammar{
  {
'    Grammar~grammar
'    resolve();
  }
  
  private static final Grammar grammar = new ' Identifier~name '();
  public static Grammar static' Identifier~name '(){
    return grammar;
  }
}
'
Grammar         =org.fuwjin.pogo.Grammar~iterator
                <- Definition~next+
Definition      =org.fuwjin.pogo.parser.Rule
                <- '    add(rule(' QuotedIdent~name
                   ', ' Category~type
                   ', ' Function~initializer
                   ', ' Function~serializer
                   ', ' Function~finalizer
                   ', ' Expression~parser 
                   '));\n'
Category        =org.fuwjin.postage.Category
                <- QuotedIdent~name
Function        =org.fuwjin.postage.Function
                <- QuotedIdent~name                
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
Suffix          =org.fuwjin.pogo.parser.ParserOperator~instanceof
                <- (QUESTION~this / STAR~this / PLUS~this) '(' Primary~parser ')'
Primary         <- Reference~this / DOT~this / Literal~this / Range~this / Expression~this
Reference       =org.fuwjin.pogo.parser.RuleReferenceParser~instanceof
                <- 'ref(' QuotedIdent~ruleName 
                   ', ' Function~constructor
                   ', ' Function~matcher 
                   ', ' Function~converter ')'
Literal         =org.fuwjin.pogo.parser.CharacterLiteralParser~instanceof
                <- 'lit(\'' Char~getLitChar '\')'
Identifier      <- .
QuotedIdent     <- '"' Identifier~this '"'
Range           =org.fuwjin.pogo.parser.CharacterRangeParser~instanceof
                <- 'range(\'' Char~getStart '\', \'' Char~getEnd '\')'
Char            <- .
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
