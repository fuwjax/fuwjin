Element         <- Null~this / ReferenceOnly~this / Reference~this? Cast~this? Value~this
Null            =null~instanceof
                <- 'null'
ReferenceOnly   =org.fuwjin.jon.ref.BaseReference~isJustReference
                <- '&' Identifier~name
Reference       =org.fuwjin.jon.ref.BaseReference~isReference
                <- '&' Identifier~name '='
Cast            =org.fuwjin.jon.ref.BaseReference~isCast
                <- '(' Element~type ')'                
Value           <- Literal~this / String~this / List~this / Map~this / Instance~this / Base~this
Literal         =org.fuwjin.jon.ref.LiteralReference
                <- Identifier~get
String          =org.fuwjin.jon.ref.StringReference~instanceof
                <- '"' Content~get '"'
List            =org.fuwjin.jon.ref.BaseReference$ListReference~iterator
                <- '[' (Element~next (',' Element~next)*)? ']'
Map             =org.fuwjin.jon.ref.BaseReference$MapReference~iterator
                <- '{' (Entry~next (',' Entry~next)*)? '}'
Entry           =org.fuwjin.jon.ref.EntryReference
                <- Element~key ':' Element~value
Instance        =org.fuwjin.jon.ref.InstanceReference~iterator
                <- '{' (Object~next ('|' Object~next)*)? '}'
Object          =org.fuwjin.jon.ref.BaseReference$MapReference~iterator
                <- (Entry~next (',' Entry~next)*)?

Base            <- Field~this / Boolean~this / Float~this / Long~this / Number~this / EverythingElse~this
Field           =java.lang.reflect.Field~getName
                <- .
Float           =java.lang.Float~instanceof
                <- . 'F'
Long            =java.lang.Long~instanceof
                <- . 'L'
Number          =java.lang.Number~instanceof
                <- .
Boolean         =java.lang.Boolean~instanceof
                <- .
EverythingElse  <- '"' Content~this '"'

Identifier      <- .
Content         =org.fuwjin.jon.JonLiteral~escape
                <- .