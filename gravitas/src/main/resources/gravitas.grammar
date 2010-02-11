Grammar = org.fuwjin.gravitas.parser.Parser~new
	<- Space* EOL? Uses:resolver From:addContext+ EOF
Uses = org.fuwjin.gravitas.parser.ClassResolver~new
	<- Use:addPackage*
Use <- USE Ident:return EOL
From = org.fuwjin.gravitas.parser.Context~new
	<- FROM Ident:type EOL Command:addCommand+
Command = org.fuwjin.gravitas.parser.Command~new
	<- Atom:addAtom+ ARROW Ident:type EOL
Atom <- Literal:return / Var:return
Literal = org.fuwjin.gravitas.parser.Literal~new
	<- Ident:value
Var = org.fuwjin.gravitas.parser.Var~new
	<- DOLLAR Ident:name

USE <- 'use' Space+
FROM <- 'from' Space+
ARROW <- ('=>' / '->') Space*
DOLLAR <- '$'

Ident <- Identifier:return Space*
Identifier <- (![- \t\r\n=$] .)+

EOL <- (EndOfLine Space*)+ / EOF
Space <- [ \t]
EndOfLine <- '\r\n' / [\r\n]
EOF <- !.
