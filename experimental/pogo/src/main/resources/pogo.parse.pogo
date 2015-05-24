# POGO parser grammar
Grammar         <- {grammar=org.fuwjin.pogo.attr.Grammar.new()} 
                   Spacing
                   (
                     Identifier:{name=result} 
                     '<-' Spacing
                     Expression:{org.fuwjin.pogo.attr.Grammar.add(grammar, name, result)}
                   )+
                   EndOfFile
                   {this=grammar}
Expression      <- Sequence:{first=result}
                   ( 
                     !'/'
                     {this=first} 
                   / 
                     {this=org.fuwjin.pogo.attr.OptionExpression.new()}
                     {org.fuwjin.pogo.attr.OptionExpression.add(this, first)}
                     ( 
                       '/' Spacing
                       Sequence:{org.fuwjin.pogo.attr.OptionExpression.add(this, result)}
                     )+
                   )
Sequence        <- Statement:{first=result}
                   (
                     Statement:{second=result}
                     {this=org.fuwjin.pogo.attr.SequenceExpression.new()}
                     {org.fuwjin.pogo.attr.SequenceExpression.add(this, first)}
                     {org.fuwjin.pogo.attr.SequenceExpression.add(this, second)}
                     Statement:{org.fuwjin.pogo.attr.SequenceExpression.add(this, result)}*
                   /
                     {this=first}
                   )
Statement       <- Attribute:{this=result} / Prefix:{this=result}
Prefix          <- '&' Spacing 
                   Assertion:{this=org.fuwjin.pogo.attr.PositiveLookaheadExpression.new(result)}
                 / '!' Spacing 
                   Assertion:{this=org.fuwjin.pogo.attr.NegativeLookaheadExpression.new(result)}
                 / Suffix:{this=result}
Assertion       <- '{' Spacing
                   Function:{this=result}
                   '}' Spacing
                 / Suffix:{this=result}
Suffix          <- Primary:{primary=result}
                   (
                     '?' Spacing
                     {this=org.fuwjin.pogo.attr.OptionalExpression.new(primary)}
                   / 
                     '*' Spacing
                     {this=org.fuwjin.pogo.attr.OptionalSeriesExpression.new(primary)}
                   / 
                     '+' Spacing
                     {this=org.fuwjin.pogo.attr.RequiredSeriesExpression.new(primary)}
                   / 
                     {this=primary}
                   )
Primary         <- '(' Spacing Expression:{this=result} ')' Spacing
                 / Reference:{this=result}
                 / Literal:{this=result}
                 / CharClass:{this=result}
                 / '.' Spacing {this=org.fuwjin.pogo.attr.CharSetExpression.ANY_CHAR()}
Reference       <- Identifier:{name=result}
                   !'<'
                   {rule=org.fuwjin.pogo.attr.Grammar.get(grammar, name)}
                   {this=org.fuwjin.pogo.attr.ReferenceExpression.new(rule)}
                   (
                     ':' Spacing 
                     Attribute:{org.fuwjin.pogo.attr.ReferenceExpression.attribute(this, result)}
                   )?

Attribute       <- '{' Spacing
                   (
                     Assignment:{this=result}
                   /
                     Function:{this=result}
                   ) 
                   '}' Spacing
Assignment      <- Identifier:{name=result}
                   '=' Spacing
                   (
                     Function:{this=org.fuwjin.pogo.attr.AssignmentAttribute.new(name,result)}
                   /
                     Variable:{this=org.fuwjin.pogo.attr.AssignmentAttribute.new(name,result)}
                   )
Function        <- FuncIdentifier:{name=result}
                   '(' Spacing
                   {this=org.fuwjin.pogo.attr.FunctionAttribute.new(name)}
                   (
                     Variable:{org.fuwjin.pogo.attr.FunctionAttribute.addParameter(this,result)}
                     (
                       ',' Spacing
                       Variable:{org.fuwjin.pogo.attr.FunctionAttribute.addParameter(this,result)}
                     )*
                   )?
                   ')' Spacing
                   {org.fuwjin.pogo.attr.FunctionAttribute.resolve(this, postage)}
Variable        <- 'match'
                   {this=org.fuwjin.pogo.attr.MatchAttribute.new()}
                 / Identifier:{this=org.fuwjin.pogo.attr.VariableAttribute.new(result)}
Literal         <- "'"
                   {this=org.fuwjin.pogo.attr.LiteralExpression.new()} 
                   (
                     !"'" 
                     Char:{org.fuwjin.pogo.attr.LiteralExpression.append(this,result)}
                   )*
                   "'" Spacing
                 /
                   '"' 
                   {this=org.fuwjin.pogo.attr.LiteralExpression.new()} 
                   (
                     !'"' 
                     Char:{org.fuwjin.pogo.attr.LiteralExpression.append(this,result)}
                   )*
                   '"' Spacing
CharClass       <- '['
                   {this=org.fuwjin.pogo.attr.CharSetParser.new()}
                   (
                     !']'
                     Char:{first=result}
                     (
                       '-'
                       Char:{org.fuwjin.pogo.attr.CharSetExpression.addRange(this, first, result)}
                     /
                       {org.fuwjin.pogo.attr.CharSetExpression.addChar(this, first)}
                     )
                   )*
                   ']' Spacing

Identifier      <- Ident:{this=match} Spacing
QIdentifier     <- QIdent:{this=match} Spacing
QIdent          <- Ident ('[]'+ [.$] SimpleQIdent / [.$] QIdent)?
SimpleQIdent    <- Ident ([.$] Ident)*
Ident           <- IdentChar:&{org.fuwjin.pogo.attr.LiteralExpression.isIdentifierStart(match)}
                   IdentChar:&{org.fuwjin.pogo.attr.LiteralExpression.isIdentifierPart(match)}*
IdentChar       <- . 

Char            <- '\\' EscapeChar:{this=result} 
                 / PlainChar:{this=org.fuwjin.pogo.attr.LiteralExpression.codepoint(match)}
PlainChar       <- !'\\' .
EscapeChar      <- Operator:{this=org.fuwjin.pogo.attr.LiteralExpression.codepoint(match)}
                 / ControlChar:{this=result} 
                 / OctalChar:{this=org.fuwjin.pogo.attr.LiteralExpression.parseOctal(match)} 
                 / 'x' UnicodeChar:{this=org.fuwjin.pogo.attr.LiteralExpression.parseHex(match)}
Operator        <- [-'"[\]\\]
ControlChar     <- 'n' {this=org.fuwjin.pogo.attr.LiteralExpression.NEWLINE()}
                 / 't' {this=org.fuwjin.pogo.attr.LiteralExpression.TAB()}
                 / 'r' {this=org.fuwjin.pogo.attr.LiteralExpression.RETURN()}
OctalChar       <- [0-3] [0-7] [0-7] / [0-7] [0-7]?
UnicodeChar     <- [0-9A-Fa-f]+
                
Spacing         <- (Space / Comment)*
Comment         <- '#' (![\r\n] .)* EndOfLine
Space           <- [ \n\t\r]
EndOfLine       <- '\n' / '\r' '\n'?
EndOfFile       <- !.
