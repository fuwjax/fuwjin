#Blocks
Grammar 	= org.fuwjin.gravitas.config.GravitasConfig~new
	    	<- Space* EOL? Uses:resolver Context:addContext+ EOF
Uses    	= org.fuwjin.gravitas.config.ClassResolver~new
	    	<- Use:addPackage*
Context 	= org.fuwjin.gravitas.config.ContextConfig~new
	    	<- From:type Command:addCommand+
Command 	= org.fuwjin.gravitas.config.CommandConfig~new
	    	<- Comment:addHelp+ Instruction:addInstruction+

#Lines
Use     	<- USE Ident:return EOL
From    	<- FROM Ident:return EOL	
Comment 	<- HASH CommentLit:return EOL
Instruction = org.fuwjin.gravitas.config.InstructionConfig~new
	        <- Atom:addToken+ Strategy:strategy EOL

#Semantic Tokens
Strategy	<- TypeStrategy:return / AliasStrategy:return
TypeStrategy = org.fuwjin.gravitas.config.TypeTargetStrategy~new
			<- ARROW Ident:type
AliasStrategy = org.fuwjin.gravitas.config.TokenTargetStrategy~new
	        <- COLON Atom:addToken+
Atom 		<- Array:return / Var:return / Literal:return
Literal 	= org.fuwjin.gravitas.config.LiteralToken~new
			<- Ident:value
Var 		= org.fuwjin.gravitas.config.VariableToken~new
			<- DOLLAR Ident:name
Array 		= org.fuwjin.gravitas.config.ArrayToken~new
			<- DOLLAR SQUARE_LEFT IdentLit:name SQUARE_RIGHT
Ident 		<- IdentLit:return Space*

#Literals
IdentLit	<- (![ \t\r\n=$\]:] .)+
CommentLit	<- (![\r\n] .)*

#Symbols
USE 		<- 'use' Space+
FROM 		<- 'from' Space+
ARROW 		<- '=>' Space*
COLON		<- ':' Space*
DOLLAR 		<- '$'
HASH 		<- '#'
SQUARE_LEFT <- '['
SQUARE_RIGHT<- ']' Space*

#Whitespace
EOL 		<- (EndOfLine Space*)+ / EOF
Space 		<- [ \t]
EndOfLine 	<- '\r\n' / [\r\n]
EOF 		<- !.
