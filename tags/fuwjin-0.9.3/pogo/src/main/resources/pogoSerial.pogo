# POGO serializer grammar
Grammar         =org.fuwjin.pogo.Grammar~iterator
                <- Definition~next+
Definition      =org.fuwjin.pogo.parser.RuleParser
                <- Identifier~name '\t' TypeInfo~this? '<- ' Expression~parser '\n'
TypeInfo        =org.fuwjin.pogo.parser.RuleParser
                <- '=' Category~type ('~' Function~initializer)? ('>' Function~serializer)? (':' Function~finalizer)? '\n\t\t'
Category        =org.fuwjin.pogo.postage.PostageUtils~isCustomCategory
                <- CategoryName~this
CategoryName    =org.fuwjin.postage.Category                
                <- Identifier~name
Function        =org.fuwjin.pogo.postage.PostageUtils~isCustomFunction
                <- FunctionName~this
FunctionName    =org.fuwjin.postage.Function
                <- Identifier~name                
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
                <- Identifier~ruleName ('~' Function~constructor)? ('>' Function~matcher)? (':' Function~converter)?
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
