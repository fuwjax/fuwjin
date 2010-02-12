#Blocks
Grammar 	= org.fuwjin.gravitas.parser.Parser~new
	    	<- Space* EOL? Uses:resolver Context:addContext+ EOF
Uses    	= org.fuwjin.gravitas.parser.ClassResolver~new
	    	<- Use:addPackage*
Context 	= org.fuwjin.gravitas.parser.Context~new
	    	<- From:type Command:addCommand+
Command 	= org.fuwjin.gravitas.parser.Command~new
	    	<- Comment:addHelp+ Instruction:addInstruction+

#Lines
Use     	<- USE Ident:return EOL
From    	<- FROM Ident:return EOL	
Comment 	<- HASH CommentLit:return EOL
Instruction = org.fuwjin.gravitas.parser.Instruction~new
	        <- Atom:addAtom+ ARROW Ident:type EOL

#Semantic Tokens
Atom 		<- Literal:return / Var:return
Literal 	= org.fuwjin.gravitas.parser.Literal~new
			<- Ident:value
Var 		= org.fuwjin.gravitas.parser.Var~new
			<- DOLLAR Ident:name
Ident 		<- IdentLit:return Space*

#Literals
IdentLit	<- (![ \t\r\n=$] .)+
CommentLit	<- (![\r\n] .)*

#Symbols
USE 		<- 'use' Space+
FROM 		<- 'from' Space+
ARROW 		<- ('=>' / '->') Space*
DOLLAR 		<- '$'
HASH 		<- '#'

#Whitespace
EOL 		<- (EndOfLine Space*)+ / EOF
Space 		<- [ \t]
EndOfLine 	<- '\r\n' / [\r\n]
EOF 		<- !.
