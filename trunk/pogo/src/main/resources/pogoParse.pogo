# POGO parser grammar
Grammar         =org.fuwjin.pogo.Grammar~new:resolve
                <- Spacing Definition:add+ EndOfFile
Definition      =org.fuwjin.pogo.parser.Rule~new
                <- Identifier:name TypeInfo~this? LEFTARROW Expression:parser
TypeInfo        =org.fuwjin.pogo.parser.Rule
                <- EQUALS TypeName:type (HASH Initializer:initializer)? (COLON Finalizer:finalizer)?
TypeName        <- TRUE:return / NULL:return / ClassIdentifier:return
Initializer     <- NEW:return / INSTANCEOF:return / ContextInit:return / InitMethod:return
ContextInit     =org.fuwjin.pogo.reflect.ContextInitializerTask~new
                <- 'context.' Identifier:name
InitMethod      =org.fuwjin.pogo.reflect.StaticInitializerTask~new
                <- Identifier:name
Finalizer       <- ContextResult:return / ResultMethod:return
ContextResult   =org.fuwjin.pogo.reflect.ContextFinalizerTask~new
                <- 'context.' Identifier:name
ResultMethod    =org.fuwjin.pogo.reflect.ResultTask~new
                <- Identifier:name
Expression      =org.fuwjin.pogo.parser.OptionParser~new:reduce
                <- Sequence:add (SLASH Sequence:add)*
Sequence        =org.fuwjin.pogo.parser.SequenceParser~new:reduce
                <- Prefix:add+
Prefix          <- AND:return / NOT:return / Suffix:return
Suffix          <- QUESTION:return / STAR:return / PLUS:return / Primary:return
Primary         <- Reference:return / OPEN Expression:return CLOSE / Literal:return / CharClass:return / DOT:return
Reference       =org.fuwjin.pogo.parser.RuleReferenceParser~new
                <- Identifier:ruleName ![<=] (HASH Constructor:constructor)? (COLON Converter:converter)?
Constructor     <- THIS:return / NEXT:return / ConstMethod:return
ConstMethod     =org.fuwjin.pogo.reflect.FactoryTask~new
                <- Identifier:name
Converter       <- RETURN:return / ConvMethod:return
ConvMethod      =org.fuwjin.pogo.reflect.AppendTask~new
                <- Identifier:name
ClassIdentifier =org.fuwjin.pogo.reflect.ClassType~new
                <- ClassIdent:type Spacing
Identifier      <- Ident:return Spacing
Literal         =org.fuwjin.pogo.parser.SequenceParser~new:reduce
                <- '\'' (!'\'' LitChar:add)* '\'' Spacing / '"' (!'"' LitChar:add)* '"' Spacing
CharClass       =org.fuwjin.pogo.parser.OptionParser~new:reduce
                <- '[' (!']' (Range:add / LitChar:add))* ']' Spacing
Range           =org.fuwjin.pogo.parser.CharacterRangeParser~new
                <- Char:setStart '-' Char:setEnd
LitChar         =org.fuwjin.pogo.parser.CharacterLiteralParser~new
                <- Char:set
Char            <- '\\' EscapeChar:return / !'\\' .
EscapeChar      <- [-'"\[\]\\] / ControlChar:return / OctalChar:return / 'x' UnicodeChar:return
ClassIdent      =java.lang.Class:forName
                <- Ident ([.$] Ident)*
Ident           <- IdentStart IdentCont*
IdentCont       <- IdentStart / [0-9]
IdentStart      <- [a-zA-Z_]
ControlChar     =org.fuwjin.pogo.parser.CharacterLiteralParser:slash
                <- [nrt]
OctalChar       =org.fuwjin.pogo.parser.CharacterLiteralParser:octal
                <- [0-3] [0-7] [0-7] / [0-7] [0-7]?
UnicodeChar     =org.fuwjin.pogo.parser.CharacterLiteralParser:unicode
                <- [0-9A-Fa-f]+
NULL            =org.fuwjin.pogo.reflect.NullType~new
                <- 'null' Spacing
TRUE            =org.fuwjin.pogo.reflect.AllType~new
                <- 'true' Spacing
NEW             =org.fuwjin.pogo.reflect.ConstructorTask~new
                <- 'new' Spacing
NEXT            =org.fuwjin.pogo.reflect.NextTask~new
                <- 'next' Spacing
THIS            =org.fuwjin.pogo.reflect.PassThruTask~new
                <- 'this' Spacing
RETURN          =org.fuwjin.pogo.reflect.ReturnTask~new
                <- 'return' Spacing
INSTANCEOF      =org.fuwjin.pogo.reflect.InstanceOfTask~new
                <- 'instanceof' Spacing
LEFTARROW       <- '<-' Spacing
COLON           <- ':' Spacing
SLASH           <- '/' Spacing
AND             =org.fuwjin.pogo.parser.PositiveLookaheadParser~new
                <- '&' Spacing Suffix:parser
NOT             =org.fuwjin.pogo.parser.NegativeLookaheadParser~new
                <- '!' Spacing Suffix:parser
QUESTION        =org.fuwjin.pogo.parser.OptionalParser~new
                <- Primary:parser '?' Spacing
STAR            =org.fuwjin.pogo.parser.OptionalSeriesParser~new
                <- Primary:parser '*' Spacing
PLUS            =org.fuwjin.pogo.parser.RequiredSeriesParser~new
                <- Primary:parser '+' Spacing
HASH            <- '~' Spacing
EQUALS          <- '=' Spacing
OPEN            <- '(' Spacing
CLOSE           <- ')' Spacing
DOT             =org.fuwjin.pogo.parser.CharacterParser~new
                <- '.' Spacing
Spacing         <- (Space / Comment)*
Comment         <- '#' (!EndOfLine .)* EndOfLine
Space           <- ' ' / '\t' / EndOfLine
EndOfLine       <- '\r\n' / [\r\n]
EndOfFile       <- !.
