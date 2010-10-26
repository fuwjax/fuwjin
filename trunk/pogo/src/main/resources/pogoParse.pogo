# POGO parser grammar
Grammar         =org.fuwjin.pogo.Grammar~new:resolve
                <- Spacing Rule:add+ EndOfFile
Rule            =org.fuwjin.pogo.parser.RuleParser
                <- RuleIdent:return RuleAttributes~this? LEFTARROW Expression:parser
RuleIdent       =org.fuwjin.pogo.parser.RuleParser:new
                <- Identifier:return &[=<]
RuleAttributes  =org.fuwjin.pogo.parser.RuleParser
                <- EQUALS Namespace:namespace RuleInit:add? RuleMatch:add? RuleResult:add?
RuleInit        =org.fuwjin.pogo.parser.RuleInitAttribute:new
                <- HASH Identifier:return
RuleMatch       =org.fuwjin.pogo.parser.RuleMatchAttribute:new
                <- OUT Identifier:return
RuleResult      =org.fuwjin.pogo.parser.RuleResultAttribute:new
                <- COLON Identifier:return
Namespace       <- NamespaceIdent>return Spacing
                
Function        =org.fuwjin.postage.CompositeFunction:new
                <- Identifier:return
Expression      =org.fuwjin.pogo.parser.OptionParser~new:reduce
                <- Sequence:add (SLASH Sequence:add)*
Sequence        =org.fuwjin.pogo.parser.SequenceParser~new:reduce
                <- Prefix:add+
Prefix          <- AND:return / NOT:return / Suffix:return
Suffix          <- QUESTION:return / STAR:return / PLUS:return / Primary:return
Primary         <- Reference:return / OPEN Expression:return CLOSE / Literal:return / CharClass:return / DOT:return
Reference       =org.fuwjin.pogo.parser.RuleReferenceParser
                <- RefIdent:return RefInit:add? RefMatch:add? RefResult:add?
RefIdent        =org.fuwjin.pogo.parser.RuleReferenceParser:new
                <- Identifier:return ![<=]
RefInit         =org.fuwjin.pogo.parser.ReferenceInitAttribute:new
                <- HASH Identifier:return
RefMatch        =org.fuwjin.pogo.parser.ReferenceMatchAttribute
                <- OUT (RefMatchReturn:return / RefMatchAttr:return)
RefMatchReturn  =org.fuwjin.pogo.parser.ReferenceMatchAttribute:RETURN
                <- 'return' Spacing
RefMatchAttr    =org.fuwjin.pogo.parser.ReferenceMatchAttribute:new
                <- Identifier:return
RefResult       =org.fuwjin.pogo.parser.ReferenceResultAttribute
                <- COLON (RefResultReturn:return / RefResultAttr:return)
RefResultReturn =org.fuwjin.pogo.parser.ReferenceResultAttribute:RETURN
                <- 'return' Spacing
RefResultAttr   =org.fuwjin.pogo.parser.ReferenceResultAttribute:new
                <- Identifier:return
Literal         =org.fuwjin.pogo.parser.SequenceParser~new:reduce
                <- '\'' (!'\'' LitChar:add)* '\'' Spacing / '"' (!'"' LitChar:add)* '"' Spacing
CharClass       =org.fuwjin.pogo.parser.OptionParser~new:reduce
                <- '[' (!']' (Range:add / LitChar:add))* ']' Spacing
Range           =org.fuwjin.pogo.parser.CharacterRangeParser~new
                <- Char:setStart '-' Char:setEnd
LitChar         =org.fuwjin.pogo.parser.CharacterLiteralParser~new
                <- Char:set
Char            <- '\\' EscapeChar:return / PlainChar>return
PlainChar       <- !'\\' .
EscapeChar      <- Operator>return / ControlChar:return / OctalChar:return / 'x' UnicodeChar:return
Operator        <- [-'"\[\]\\]
Identifier      <- Ident>return Spacing
NamespaceIdent  <- (![~>:< \n\r\t] .)*
Ident           <- IdentStart IdentCont*
IdentCont       <- IdentStart / [0-9]
IdentStart      <- [a-zA-Z_]
ControlChar     =org.fuwjin.pogo.parser.CharacterLiteralParser>slash
                <- [nrt]
OctalChar       =org.fuwjin.pogo.parser.CharacterLiteralParser>octal
                <- [0-3] [0-7] [0-7] / [0-7] [0-7]?
UnicodeChar     =org.fuwjin.pogo.parser.CharacterLiteralParser>unicode
                <- [0-9A-Fa-f]+
LEFTARROW       <- '<-' Spacing
EQUALS          <- '=' Spacing
HASH            <- '~' Spacing
OUT             <- '>' Spacing
COLON           <- ':' Spacing
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
OPEN            <- '(' Spacing
CLOSE           <- ')' Spacing
SLASH           <- '/' Spacing
DOT             =org.fuwjin.pogo.parser.CharacterParser~new
                <- '.' Spacing
Spacing         <- (Space / Comment)*
Comment         <- '#' (!EndOfLine .)* EndOfLine
Space           <- ' ' / '\t' / EndOfLine
EndOfLine       <- '\r\n' / [\r\n]
EndOfFile       <- !.
