# POGO serializer grammar
Grammar         =org.fuwjin.pogo.Grammar~iterator
                <- Rule~next+
Rule            =org.fuwjin.pogo.parser.RuleParser
                <- Identifier~name '\t' RuleNamespace~this? '<- ' Expression~parser '\n'
RuleNamespace   =org.fuwjin.pogo.parser.RuleParser
                <- '=' Category~namespace RuleAttributes~attributes '\n\t\t'
RuleAttributes  =java.lang.Iterable~iterator
                <- RuleAttribute~next*
RuleAttribute   <- RuleInit~this / RuleMatch~this / RuleResult~this
RuleInit        =org.fuwjin.pogo.parser.RuleInitAttribute
                <- '~' Identifier~name
RuleMatch       =org.fuwjin.pogo.parser.RuleMatchAttribute
                <- '>' Identifier~name
RuleResult      =org.fuwjin.pogo.parser.RuleResultAttribute
                <- ':' Identifier~name

Category        =org.fuwjin.pogo.postage.PostageUtils~isCustomCategory
                <- Identifier~this
Expression      <- LitOpt~this / Option~this / SequenceChain~this
Option          =org.fuwjin.pogo.parser.OptionParser~iterator
                <- SequenceChain~next (' / ' SequenceChain~next)*
Sequence        =org.fuwjin.pogo.parser.SequenceParser~iterator
                <- PrefixChain~next (' ' PrefixChain~next)*
SequenceChain   <- LitSeq~this / Sequence~this / PrefixChain~this
Prefix          =org.fuwjin.pogo.parser.ParserOperator
                <- (AND~this / NOT~this) SuffixChain~parser
PrefixChain     <- Prefix~this / SuffixChain~this
Suffix          =org.fuwjin.pogo.parser.ParserOperator
                <- Primary~parser (QUESTION~this / STAR~this / PLUS~this)
SuffixChain     <- Suffix~this / Primary~this
Primary         <- Reference~this / DOT~this / Literal~this / CharClass~this / SubEx~this
SubEx           <- '(' Expression~this ')'
Reference       =org.fuwjin.pogo.parser.RuleReferenceParser
                <- Identifier~ruleName RefAttributes~attributes
RefAttributes   =java.lang.Iterable~iterator
                <- RefAttribute~next*
RefAttribute    <- RefInit~this / RefMatch~this / RefResult~this
RefInit         =org.fuwjin.pogo.parser.ReferenceInitAttribute
                <- '~' Identifier~name
RefMatch        =org.fuwjin.pogo.parser.ReferenceMatchAttribute
                <- '>' (RETURN~isReturn / Identifier~name)
RefResult       =org.fuwjin.pogo.parser.ReferenceResultAttribute
                <- ':' (RETURN~isReturn / Identifier~name)
Literal         <- LitSeq~this / SingleLit~this
LitSeq          =org.fuwjin.pogo.parser.SequenceParser~isLiteral
                <- '\'' LitChars~this '\''
LitChars        =org.fuwjin.pogo.parser.SequenceParser~iterator
                <- LitChar~next*
SingleLit       <- '\'' LitChar~this '\''
Identifier      <- .
CharClass       <- LitOpt~this / SingleOpt~this
LitOpt          =org.fuwjin.pogo.parser.OptionParser~isLiteral
                <- '[' LitOpts~this ']'
LitOpts         =org.fuwjin.pogo.parser.OptionParser~iterator
                <- CharClassOption~next*
SingleOpt       <- '[' CharClassOption~this ']'
CharClassOption <- Range~this / LitClassChar~this
Range           =org.fuwjin.pogo.parser.CharacterRangeParser
                <- Char~getStart '-' Char~getEnd
LitChar         =org.fuwjin.pogo.parser.CharacterLiteralParser
                <- Char~getLitChar
LitClassChar    =org.fuwjin.pogo.parser.CharacterLiteralParser
                <- Char~getClassChar
Char            <- .
AND             =org.fuwjin.pogo.parser.PositiveLookaheadParser~instanceof
                <- '&'
NOT             =org.fuwjin.pogo.parser.NegativeLookaheadParser~instanceof
                <- '!'
QUESTION        =org.fuwjin.pogo.parser.OptionalParser~instanceof
                <- '?'
STAR            =org.fuwjin.pogo.parser.OptionalSeriesParser~instanceof
                <- '*'
PLUS            =org.fuwjin.pogo.parser.RequiredSeriesParser~instanceof
                <- '+'
DOT             =org.fuwjin.pogo.parser.CharacterParser~instanceof
                <- '.'
RETURN          <- 'return'
