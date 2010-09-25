# An Element is a Value which can be assigned to and referred to by a Reference
Element         <- Spacing ReferenceElm Spacing
ReferenceElm    <- Reference (EQUALS Value)? / Value
# A Reference is an id beginning with an '&'
Reference       <- '&' ReferenceId Spacing
ReferenceId     <- [-_.$0-9a-zA-Z]+

# A Value is a rich value optionally Cast
Value           <- (OPEN_CAST ClassCast CLOSE_CAST)? CastValue
# A Cast works much as a normal Element, except that the value must be a Class identifier
ClassCast       <- Reference (EQUALS Class)? / Class
CastValue       <- Object / Map / List / String / Literal
# Objects look like Maps, except Entries may be separated by '|' to indicate superclass boundaries
# For an Object to deserialize successfully the first Element in an Entry must map to a field name
# So in general these are Strings or ArbitraryLits
Object          <- OPEN_MAP (Entries? (CLASS_SEP Entries?)*)? CLOSE_MAP
Entries         <- Entry (ELM_SEP Entry)*
# Maps look like JSON maps
Map             <- OPEN_MAP Entries? CLOSE_MAP
Entry           <- Element ENTRY_SEP Element
# Lists look like JSON arrays
List            <- OPEN_LIST (Element (ELM_SEP Element)*)? CLOSE_LIST
# Strings look like JSON strings
String          <- '"' Char* '"'
# Literals may be any stream of characters that wouldn't have another JON-interpreted meaning
# However some literals have a predefined meaning, 
Literal         <- Null / Boolean / Class / Float / Double / Long / Integer / ArbitraryLit

# Characters in a String may be any character prefixed by a '\' or any non-double quote character
# \n and \t have special meaning as new line and tab
# However a new line and tab may be used unescaped as well
Char            <- '\\n' / '\\t' / '\\' . / !'"' .
Null            <- 'null'
Boolean         <- 'true' / 'false'
Class           <- Identifier ([.$] Identifier)* '[]'*
Identifier      <- [a-zA-Z_] [a-zA-Z0-9_]*
Double          <- Decimal [dD]? / Digits [dD]
Float           <- Decimal [fF] / Digits [fF]
Long            <- Digits [lL]
Integer         <- Digits
Decimal         <- Digits '.' [0-9]* ([eE] Digits)? / Digits [eE] Digits
Digits          <- '-'? [1-9][0-9]* / '0'
# Arbitrary Literals can use any sequence of characters that don't have special meaning in JON
# Note that things like URLs cannot be Arbitrary Literals, and must be expressed as Strings instead
# So this production is generally only used for field names in Object Entries
ArbitraryLit    <- (![(){}\[\],:&=|\" \r\n\t] .)+ '[]'*
                
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
