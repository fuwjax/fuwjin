Element         <- Spacing (Null:return / UseReference~this:return / SetReference:return / Dereference:return / Fill~this:return / Value:return) Spacing
Dereference     = context:getReference
                <- Reference:return
UseReference    = context~newReference:addReference
                <- SetRefImpl~this
SetReference    = context~newReference:addReference
                <- SetRefImpl~this
SetRefImpl      = org.fuwjin.jon.Reference
                <- Reference:name EQUALS (Fill~value:value / Value:value)
Reference       <- '&' ReferenceId>return Spacing
ReferenceId     <- [-_.$0-9a-zA-Z]+
Value           <- Explicit:return / Unknown:return
Null            = context:null
                <- 'null'

Fill            =org.fuwjin.jon.builder.Builder~instanceof:toObject
                <- (OPEN_CAST ClassCast:verify CLOSE_CAST)? KnownElement~forType
Explicit        =org.fuwjin.jon.BuilderFactory~new:toObject 
                <- OPEN_CAST ClassCast:type CLOSE_CAST KnownElement~forType
KnownElement    =org.fuwjin.jon.builder.Builder~instanceof
                <- KnownObject~this / KnownMap~this / KnownList~this / KnownString~this / KnownLiteral~this
KnownObject     =org.fuwjin.jon.builder.InstanceBuilder~iterator
                <- OPEN_MAP (KnownInstance~next (CLASS_SEP KnownInstance~next)*)? CLOSE_MAP
KnownInstance   =org.fuwjin.jon.builder.EntriesBuilder~instanceof
                <- (KnownEntry~newEntry:add (ELM_SEP KnownEntry~newEntry:add)*)?
KnownMap        =org.fuwjin.jon.builder.EntriesBuilder~instanceof
                <- OPEN_MAP (KnownEntry~newEntry:add (ELM_SEP KnownEntry~newEntry:add)*)? CLOSE_MAP
KnownList       =org.fuwjin.jon.builder.ElementsBuilder~instanceof
                <- OPEN_LIST (Element~newElement:add (ELM_SEP Element~newElement:add)*)? CLOSE_LIST
KnownEntry      =org.fuwjin.jon.builder.EntriesBuilder$EntryBuilder~instanceof
                <- Element~newKey:key ENTRY_SEP Element~newValue:value
KnownString     =org.fuwjin.jon.builder.LiteralBuilder~instanceof
                <- String:set
KnownLiteral    =org.fuwjin.jon.builder.LiteralBuilder~instanceof
                <- ArbitraryLit>set
ArbitraryLit    <- (![(){}\[\],:&=|\" \r\n\t] .)+ '[]'*

ClassCast       <- SetRefClass:return / Dereference:return / Class:return
SetRefClass     =context~newReference:addReference
                <- SetRefClassImpl~this
SetRefClassImpl =org.fuwjin.jon.Reference
                <- Reference:name EQUALS Class:value

Unknown         <- Map:return / List:return / String:return / Literal:return
Map             =org.fuwjin.jon.builder.MapBuilder~new:toObject
                <- KnownMap~this
List            =java.util.ArrayList~new:toArray 
                <- OPEN_LIST (Element:add (ELM_SEP Element:add)*)? CLOSE_LIST
Literal         <- Boolean:return / Class:return / Float:return / Double:return / Long:return / Integer:return
Boolean         <- True:return / False:return
True            =context:true
                <- 'true'
False           =context:false 
                <- 'false'
Class           =org.fuwjin.jon.JonLiteral>forName 
                <- Identifier ([.$] Identifier)* '[]'*
Identifier      <- [a-zA-Z_] [a-zA-Z0-9_]*
Double          =java.lang.Double>valueOf 
                <- Decimal [dD]? / Digits [dD]
Float           =java.lang.Float>valueOf 
                <- Decimal [fF] / Digits [fF]
Long            <- LongDigits:return [lL]
LongDigits      =java.lang.Long>valueOf 
                <- Digits
Integer         =java.lang.Integer>valueOf 
                <- Digits
Decimal         <- Digits '.' [0-9]* ([eE] Digits)? / Digits [eE] Digits
Digits          <- '-'? [1-9][0-9]* / '0'
String          <- '"' Chars:return '"'
Chars           =java.lang.StringBuilder~new:toString 
                <- Char:append*
Char            <- '\\' EscapeChar:return / !'"' PlainChar>return
EscapeChar      <- NewLine:return / Tab:return / PlainChar>return
PlainChar       <- .
NewLine         =org.fuwjin.jon.JonLiteral:newLine 
                <- 'n'
Tab             =org.fuwjin.jon.JonLiteral:tab 
                <- 't'
                
EQUALS          <- '=' Spacing
OPEN_CAST       <- '(' Spacing
CLOSE_CAST      <- ')' Spacing
OPEN_MAP        <- '{' Spacing
CLOSE_MAP       <- '}' Spacing
OPEN_LIST       <- '[' Spacing
CLOSE_LIST      <- ']' Spacing
CLASS_SEP       <- '|' Spacing
ELM_SEP         <- ',' Spacing
ENTRY_SEP       <- ':' Spacing
Spacing         <- [ \t\r\n]*