# POGO parser grammar
Grammar         =org.fuwjin.pogo.Grammar~{this=new(postage)}:{resolve(this)}
                <- Spacing Rule:{add(this,result)}+ EndOfFile
Rule            =org.fuwjin.pogo.parser.RuleParser
                <- RuleIdent:{this=new(result)} RuleAttributes~{this}? LEFTARROW Expression:{parser(this,result)}
RuleIdent       <- Identifier:{this=result} &[=<]
RuleAttributes  =org.fuwjin.pogo.parser.RuleParser
                <- EQUALS Namespace:{namespace(this,result)} RuleInit:{add(this,result)}? RuleMatch:{add(this,result)}? RuleResult:{add(this,result)}?
RuleInit        =org.fuwjin.pogo.parser.RuleInitAttribute
                <- HASH Identifier:{this=new(result)}
RuleMatch       =org.fuwjin.pogo.parser.RuleMatchAttribute
                <- OUT Identifier:{this=new(result)}
RuleResult      =org.fuwjin.pogo.parser.RuleResultAttribute
                <- COLON Identifier:{this=new(result)}
Namespace       <- NamespaceIdent:{this=match} Spacing
                
Expression      =org.fuwjin.pogo.parser.OptionParser~{this=new()}:{reduce(this)}
                <- Sequence:{add(this,result)} (SLASH Sequence:{add(this,result)})*
Sequence        =org.fuwjin.pogo.parser.SequenceParser~{this=new()}:{reduce(this)}
                <- Prefix:{add(this,result)}+
Prefix          <- AND:{this=result} / NOT:{this=result} / Suffix:{this=result}
Suffix          <- QUESTION:{this=result} / STAR:{this=result} / PLUS:{this=result} / Primary:{this=result}
Primary         <- Reference:{this=result} / OPEN Expression:{this=result} CLOSE / Literal:{this=result} / CharClass:{this=result} / DOT:{this=result}
Reference       =org.fuwjin.pogo.parser.RuleReferenceParser
                <- RefIdent:{this=new(result)} RefInit:{add(this,result)}? RefMatch:{add(this,result)}? RefResult:{add(this,result)}?
RefIdent        =org.fuwjin.pogo.parser.RuleReferenceParser
                <- Identifier:{this=result} ![<=]
RefInit         =org.fuwjin.pogo.parser.ReferenceInitAttribute
                <- HASH Identifier:{this=new(result)}
RefMatch        =org.fuwjin.pogo.parser.ReferenceMatchAttribute
                <- OUT Identifier:{this=new(result)}
RefResult       =org.fuwjin.pogo.parser.ReferenceResultAttribute
                <- COLON Identifier:{this=new(result)}
Literal         =org.fuwjin.pogo.parser.SequenceParser~{this=new()}:{reduce(this)}
                <- '\'' (!'\'' LitChar:{add(this,result)})* '\'' Spacing / '"' (!'"' LitChar:{add(this,result)})* '"' Spacing
CharClass       =org.fuwjin.pogo.parser.OptionParser~{this=new()}:{reduce(this)}
                <- '[' (!']' (Range:{add(this,result)} / LitChar:{add(this,result)}))* ']' Spacing
Range           =org.fuwjin.pogo.parser.CharacterRangeParser~{this=new()}
                <- Char:{setStart(this,result) '-' Char:{setEnd(this,result)}
LitChar         =org.fuwjin.pogo.parser.CharacterLiteralParser~{this=new()}
                <- Char:{set(this,result)}
Char            <- '\\' EscapeChar:{this=result} / PlainChar:{this=match}
PlainChar       <- !'\\' .
EscapeChar      <- Operator:{this=match} / ControlChar:{this=result} / OctalChar:{this=result} / 'x' UnicodeChar:{this=result}
Operator        <- [-'"\[\]\\]
Identifier      <- Ident:{this=match} Spacing
NamespaceIdent  <- (![~>:< \n\r\t] .)*
Ident           <- IdentStart IdentCont*
IdentCont       <- IdentStart / [0-9]
IdentStart      <- [a-zA-Z_]
ControlChar     =org.fuwjin.pogo.parser.CharacterLiteralParser:{this=slash(match)}
                <- [nrt]
OctalChar       =org.fuwjin.pogo.parser.CharacterLiteralParser:{this=octal(match)}
                <- [0-3] [0-7] [0-7] / [0-7] [0-7]?
UnicodeChar     =org.fuwjin.pogo.parser.CharacterLiteralParser:{this=unicode(match)}
                <- [0-9A-Fa-f]+
LEFTARROW       <- '<-' Spacing
EQUALS          <- '=' Spacing
HASH            <- '~' Spacing
OUT             <- '>' Spacing
COLON           <- ':' Spacing
AND             =org.fuwjin.pogo.parser.PositiveLookaheadParser
                <- '&' Spacing Suffix:{this=new(result)}
NOT             =org.fuwjin.pogo.parser.NegativeLookaheadParser
                <- '!' Spacing Suffix:{this=new(result)}
QUESTION        =org.fuwjin.pogo.parser.OptionalParser:{this=new(parser)}
                <- Primary:{parser=result} '?' Spacing
STAR            =org.fuwjin.pogo.parser.OptionalSeriesParser:{this=new(parser)}
                <- Primary:{parser=result} '*' Spacing
PLUS            =org.fuwjin.pogo.parser.RequiredSeriesParser:{this=new(parser)}
                <- Primary:{parser=result} '+' Spacing
OPEN            <- '(' Spacing
CLOSE           <- ')' Spacing
SLASH           <- '/' Spacing
DOT             =org.fuwjin.pogo.parser.CharacterParser:{this=DOT()}
                <- '.' Spacing
Spacing         <- (Space / Comment)*
Comment         <- '#' (!EndOfLine .)* EndOfLine
Space           <- ' ' / '\t' / EndOfLine
EndOfLine       <- '\r\n' / [\r\n]
EndOfFile       <- !.
