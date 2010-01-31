# POGO serializer grammar
Grammar         =org.fuwjin.pogo.Grammar~iterator
                <- Definition~next+
Definition      =org.fuwjin.pogo.parser.Rule
                <- Identifier~name '\t' TypeInfo~this? '<- ' Expression~parser '\n'
TypeInfo        =org.fuwjin.pogo.parser.Rule
                <- '=' Type~type ('~' Initializer~initializer)? (':' Finalizer~finalizer)? '\n\t\t'
Type            <- TRUE~this / NULL~this / ClassType~this
ClassType       =org.fuwjin.pogo.reflect.ClassType
                <- ClassObj~type
ClassObj        =java.lang.Class
                <- Identifier~getName
Initializer     <- NEW~this / INSTANCEOF~this / ContextInit~this / InitMethod~this
ContextInit     =org.fuwjin.pogo.reflect.ContextInitializerTask
                <- Identifier~name
InitMethod      =org.fuwjin.pogo.reflect.StaticInitializerTask
                <- Identifier~name
Finalizer       <- ContextResult~this / ResultMethod~this
ContextResult   =org.fuwjin.pogo.reflect.ContextFinalizerTask
                <- Identifier~name
ResultMethod    =org.fuwjin.pogo.reflect.ResultTask
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
                <- Identifier~ruleName ('~' Constructor~constructor)? (':' Converter~converter)?
Constructor     <- THIS~this / NEXT~this / ConstMethod~this
ConstMethod     =org.fuwjin.pogo.reflect.FactoryTask
                <- Identifier~name
Converter       <- RETURN~this / ConvMethod~this
ConvMethod      =org.fuwjin.pogo.reflect.AppendTask
                <- Identifier~name
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
NULL            =org.fuwjin.pogo.reflect.NullType~instanceof
                <- 'null'
TRUE            =org.fuwjin.pogo.reflect.AllType~instanceof
                <- 'true'
NEW             =org.fuwjin.pogo.reflect.ConstructorTask~instanceof
                <- 'new'
NEXT            =org.fuwjin.pogo.reflect.NextTask~instanceof
                <- 'next'
THIS            =org.fuwjin.pogo.reflect.PassThruTask~instanceof
                <- 'this'
RETURN          =org.fuwjin.pogo.reflect.ReturnTask~instanceof
                <- 'return'
INSTANCEOF      =org.fuwjin.pogo.reflect.InstanceOfTask~instanceof
                <- 'instanceof'
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
