# POGO valid grammar
Grammar         <- Spacing Definition+ EndOfFile
Definition      <- Identifier TypeInfo? LEFTARROW Expression
TypeInfo        <- EQUALS ClassIdentifier (HASH Identifier)? (COLON Identifier)?
Expression      <- Sequence (SLASH Sequence)*
Sequence        <- Prefix+
Prefix          <- (AND / NOT)? Suffix
Suffix          <- Primary (QUESTION / STAR / PLUS)?
Primary         <- Reference / OPEN Expression CLOSE / Literal / CharClass / DOT
Reference       <- Identifier ![<=] (HASH Identifier)? (COLON Identifier)?
ClassIdentifier <- ClassIdent Spacing
Identifier      <- Ident Spacing
Literal         <- '\'' (!'\'' Char)* '\'' Spacing / '"' (!'"' Char)* '"' Spacing
CharClass       <- '[' (!']' (Range / Char))* ']' Spacing
Range           <- Char '-' Char
Char            <- '\\' EscapeChar / !'\\' .
EscapeChar      <- [nrt'"[\]\\] / OctalChar / 'x' UnicodeChar
ClassIdent      <- IdentStart ClassIdentCont*
Ident           <- IdentStart IdentCont*
ClassIdentCont  <- [a-zA-Z0-9_.$:]
IdentCont       <- [a-zA-Z0-9_]
IdentStart      <- [a-zA-Z_]
OctalChar       <- [0-3] [0-7] [0-7] / [0-7] [0-7]?
UnicodeChar     <- [0-9A-Fa-f]+
LEFTARROW       <- '<-' Spacing
COLON           <- ':' Spacing
SLASH           <- '/' Spacing
AND             <- '&' Spacing
NOT             <- '!' Spacing
QUESTION        <- '?' Spacing
STAR            <- '*' Spacing
PLUS            <- '+' Spacing
HASH            <- '~' Spacing
EQUALS          <- '=' Spacing
OPEN            <- '(' Spacing
CLOSE           <- ')' Spacing
DOT             <- '.' Spacing
Spacing         <- (Space / Comment)*
Comment         <- '#' (!EndOfLine .)* EndOfLine
Space           <- ' ' / '\t' / EndOfLine
EndOfLine       <- '\r\n' / [\r\n]
EndOfFile       <- !.
