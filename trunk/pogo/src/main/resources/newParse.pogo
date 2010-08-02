# POGO parser grammar
Grammar         =org.fuwjin.pogo.Grammar~new:resolve
                <- Spacing Definition:add+ EndOfFile
Definition      =org.fuwjin.pogo.parser.Rule~new
                <- Identifier:name TypeInfo~this? LEFTARROW Expression:parser
TypeInfo        =org.fuwjin.pogo.parser.Rule
                <- EQUALS InvokerLit:type (HASH InvokerLit:initializer)? (COLON InvokerLit:finalizer)?
Expression      =org.fuwjin.pogo.parser.OptionParser~new:reduce
                <- Sequence:add (SLASH Sequence:add)*
Sequence        =org.fuwjin.pogo.parser.SequenceParser~new:reduce
                <- Prefix:add+
Prefix          <- AND:return / NOT:return / Suffix:return
Suffix          <- QUESTION:return / STAR:return / PLUS:return / Primary:return
Primary         <- Reference:return / OPEN Expression:return CLOSE / Literal:return / CharClass:return / DOT:return
Reference       =org.fuwjin.pogo.parser.RuleReferenceParser~new
                <- Identifier:ruleName ![<=] (HASH InvokerLit:constructor)? (COLON InvokerLit:converter)?
InvokerLit      <- DottedIdent:return Spacing
DottedIdent     <- [a-zA-Z0-9_.$]*
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
EscapeChar      <- ['"\[\]\\] / ControlChar:return / OctalChar:return / 'x' UnicodeChar:return
Ident           <- IdentStart IdentCont*
IdentCont       <- IdentStart / [0-9]
IdentStart      <- [a-zA-Z_]
ControlChar     =org.fuwjin.pogo.parser.CharacterLiteralParser:slash
                <- [nrt]
OctalChar       =org.fuwjin.pogo.parser.CharacterLiteralParser:octal
                <- [0-3] [0-7] [0-7] / [0-7] [0-7]?
UnicodeChar     =org.fuwjin.pogo.parser.CharacterLiteralParser:unicode
                <- [0-9A-Fa-f]+
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
