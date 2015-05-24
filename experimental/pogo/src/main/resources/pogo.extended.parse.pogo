# POGO parser grammar
Grammar         =org.fuwjin.pogo.Grammar~new:resolve
                <- Spacing Rule:add+ EndOfFile
Rule            =org.fuwjin.pogo.parser.AttributedRuleParser
                <- RuleIdent:new RuleAttributes~this? LEFTARROW Expression:parser
RuleIdent       <- Identifier:return &[=<]
RuleAttributes  =org.fuwjin.pogo.parser.AttributedRuleParser
                <- EQUALS Namespace:namespace InitAttribute:init? ResultAttribute:result?
Namespace       <- QualifiedIdent>return Spacing
                
InitAttribute   <- HASH (Attribute:return / SimpleInit:return)
SimpleInit      =org.fuwjin.pogo.parser.InitAttribute:new
                <- Identifier:return
ResultAttribute <- HASH SimpleMatch:return / COLON (Attribute:return / SimpleResult:return)
SimpleMatch     =org.fuwjin.pogo.parser.MatchAttribute
                <- RETURN:match / Identifier:new
SimpleResult    =org.fuwjin.pogo.parser.ResultAttribute
                <- RETURN:result / Identifier:new
                
Expression      =org.fuwjin.pogo.parser.OptionParser~new:reduce
                <- Sequence:add (SLASH Sequence:add)*
Sequence        =org.fuwjin.pogo.parser.SequenceParser~new:reduce
                <- Prefix:add+
Prefix          <- AND:return / NOT:return / Suffix:return
Suffix          <- QUESTION:return / STAR:return / PLUS:return / Primary:return
Primary         <- Reference:return / OPEN Expression:return CLOSE / Literal:return / CharClass:return / DOT:return

Reference       =org.fuwjin.pogo.parser.AttributedRuleReference
                <- RefIdent:new InitAttribute:init? ResultAttribute:result?
RefIdent        <- Identifier:return ![<=]
                
Attribute       <- ATT_OPEN (THIS EQUALS)? (AttributeFunc:return / AttributeVar:return) ATT_CLOSE
AttributeFunc   =org.fuwjin.pogo.parser.AttributeFunction
                <- QualifiedIdent>new OPEN (AttributeVar:add (COMMA AttributeVar:add)*)? CLOSE
AttributeVar    =org.fuwjin.pogo.parser.AttributeVariable
                <- MATCH:match / RESULT:result

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
QualifiedIdent  <- (![~>:<( \n\r\t] .)*
Ident           <- IdentStart IdentCont*
IdentCont       <- IdentStart / [0-9]
IdentStart      <- [a-zA-Z_]
ControlChar     =org.fuwjin.pogo.parser.CharacterLiteralParser>slash
                <- [nrt]
OctalChar       =org.fuwjin.pogo.parser.CharacterLiteralParser>octal
                <- [0-3] [0-7] [0-7] / [0-7] [0-7]?
UnicodeChar     =org.fuwjin.pogo.parser.CharacterLiteralParser>unicode
                <- [0-9A-Fa-f]+
THIS            <- 'this' Spacing
RETURN          <- 'return' Spacing
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
ATT_OPEN        <- '{' Spacing
ATT_CLOSE       <- '}' Spacing
MATCH           <- 'match' Spacing
RESULT          <- 'result' Spacing
SLASH           <- '/' Spacing
DOT             =org.fuwjin.pogo.parser.CharacterParser~new
                <- '.' Spacing
Spacing         <- (Space / Comment)*
Comment         <- '#' (!EndOfLine .)* EndOfLine
Space           <- ' ' / '\t' / EndOfLine
EndOfLine       <- '\r\n' / [\r\n]
EndOfFile       <- !.
