CompilationUnit <- PackageDeclaration? ImportDeclaration* TypeDeclaration*
PackageDeclaration <- Annotation* PACKAGE PackageName SEMI
ImportDeclaration<-IMPORT STATIC? QualifiedIdentifier (DOT STAR)? SEMI
TypeDeclaration <- ClassDeclaration / InterfaceDeclaration / SEMI

ClassDeclaration <- NormalClassDeclaration / EnumDeclaration
NormalClassDeclaration <- ClassModifier* CLASS Identifier TypeParameters? Super? Interfaces? ClassBody
ClassModifier <- Annotation / PUBLIC / PROTECTED / PRIVATE / ABSTRACT / STATIC / FINAL / STRICTFP
Super <- EXTENDS ClassType
Interfaces <- IMPLEMENTS InterfaceTypeList
InterfaceTypeList <- InterfaceType (COMMA InterfaceType)*

ClassBody <- O_BRACE ClassBodyDeclaration* C_BRACE
ClassBodyDeclaration<-ClassMemberDeclaration
    /InstanceInitializer
    /StaticInitializer
    /ConstructorDeclaration
ClassMemberDeclaration<-
    FieldDeclaration
    /MethodDeclaration
    /ClassDeclaration                        
    /InterfaceDeclaration
    /SEMI
FieldDeclaration <- FieldModifiers? Type VariableDeclarators SEMI
VariableDeclarators <- VariableDeclarator (COMMA VariableDeclarator)*
VariableDeclarator <- VariableDeclaratorId EQUALS VariableInitializer / VariableDeclaratorId
    

VariableDeclaratorId:
    Identifier
    VariableDeclaratorId [ ]

VariableInitializer:
    Expression
    ArrayInitializer   

QualifiedIdentifier <- Identifier (DOT Identifier)*
PackageName <- QualifiedIdentifier
TypeName <- QualifiedIdentifier
ExpressionName <- QualifiedIdentifier
MethodName <- QualifiedIdentifier
PackageOrTypeName <- QualifiedIdentifier
AmbiguousName <- QualifiedIdentifier

TypeArguments <- O_ANGLE ActualTypeArgumentList C_ANGLE
ActualTypeArgumentList <- ActualTypeArgument (COMMA ActualTypeArgument)*
ActualTypeArgument <- ReferenceType / Wildcard
Wildcard <- QUESTION WildcardBounds?
WildcardBounds <- EXTENDS ReferenceType / SUPER ReferenceType

TypeParameter <- TypeVariable TypeBound?
TypeVariable <- Identifier
TypeBound <- EXTENDS ClassOrInterfaceType (AND InterfaceType)*

Type <- ReferenceType / PrimitiveType
PrimitiveType <- NumericType / BOOLEAN
NumericType <- IntegralType / FloatingPointType
IntegralType <- BYTE / SHORT / INT / LONG / CHAR
FloatingPointType <- FLOAT / DOUBLE
ReferenceType <- ClassOrInterfaceType ARRAY* / PrimitiveType ARRAY+
ClassOrInterfaceType <- Identifier TypeArguments? (DOT Identifier TypeArguments?)*
TypeName <- Identifier (DOT Identifier)*
InterfaceType <- ClassOrInterfaceType
ClassType <- ClassOrInterfaceType

UnicodeInputCharacter <- UnicodeEscape / .
UnicodeEscape <- '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
HexDigit: [0-9A-Fa-f]

Identifier <- !(Keyword / BooleanLiteral / NullLiteral) IdentifierChars S+
IdentifierChars <- JavaLetter JavaLetterOrDigit*
JavaLetter = java.lang.Character:isJavaIdentifierStart <- .
JavaLetterOrDigit =java.lang.Character:isJavaIdentifierPart <- .      
        
Literal<-IntegerLiteral/FloatingPointLiteral/BooleanLiteral/CharacterLiteral/StringLiteral/NullLiteral

IntegerLiteral<-(HexIntegerLiteral/OctalIntegerLiteral/DecimalIntegerLiteral)S+
DecimalIntegerLiteral<-DecimalNumeral IntegerTypeSuffix?
HexIntegerLiteral<-HexNumeral IntegerTypeSuffix?
OctalIntegerLiteral<-OctalNumeral IntegerTypeSuffix?
IntegerTypeSuffix <- [lL]
DecimalNumeral <- [1-9][0-9]* / '0'
HexNumeral <- '0' [xX] HexDigit+
OctalNumeral <- '0' [0-7]+

FloatingPointLiteral<-(DecimalFloatingPointLiteral/HexadecimalFloatingPointLiteral)S+
DecimalFloatingPointLiteral<- [0-9]+ '.' [0-9]* ExponentPart? FloatTypeSuffix?
    / '.' [0-9]+ ExponentPart? FloatTypeSuffix?
      /[0-9]+ ExponentPart FloatTypeSuffix?
        /[0-9]+ FloatTypeSuffix
ExponentPart<- [eE] [+-]? [0-9]+
FloatTypeSuffix<- [fFdD]
HexadecimalFloatingPointLiteral<-HexSignificand BinaryExponent FloatTypeSuffix?
HexSignificand<-HexNumeral '.'? / '0' [xX] HexDigit* '.' HexDigit+
BinaryExponent<-[pP] [+-]? [0-9]+

BooleanLiteral <- TRUE / FALSE
NullLiteral <- NULL

CharacterLiteral <- "'" (EscapeSequence / !['\\] .) "'" S+
StringLiteral <- '"' StringCharacter* '"' S+
StringCharacter <- EscapeSequence / !["\\] .
EscapeSequence<- '\\' [btnfr"'\\] / OctalEscape
OctalEscape <- '\\' ([0-3][0-7][0-7] / [0-7][0-7]?)

LineTerminator <- '\r\n' / [\r\n]
InputCharacter <- ![\r\n] .

Input <- InputElement* EOF
InputElement <- WhiteSpace / Comment / Token
Token <- Identifier / Keyword / Literal / Separator / Operator

Comment <- TraditionalComment / EndOfLineComment
TraditionalComment <- '/*' (!'*/' .)* '*/'
EndOfLineComment <- '//' InputCharacter* 
WhiteSpace <- [ \t\014] / LineTerminator
EOF <- '\032'? !.

ARRAY <- O_SQR C_SQR
O_SQR <- '[' S*
C_SQR <- ']' S*
O_ANGLE <- '<' S*
C_ANGLE <- '>' S*
O_BRACE <- '{' S*
C_BRACE <- '}' S*
DOT <- '.' S*
AND <- '&' S*
COMMA <- ',' S*
QUESTION <- '?' S*
SEMI <- ';' S*
STAR <- '*' S*
EQUALS <- '=' S*

PUBLIC <- 'public' S+
PROTECTED <- 'protected' S+
PRIVATE <- 'private' S+
ABSTRACT <- 'abstract' S+
STATIC <- 'static' S+
FINAL <- 'final' S+
STRICTFP <- 'strictfp' S+
CLASS <- 'class' S+
IMPORT <- 'import' S+
PACKAGE <- 'package' S+
SUPER <- 'super' S+
EXTENDS <- 'extends' S+
IMPLEMENTS <- 'implements' S+
BOOLEAN <- 'boolean' S+
BYTE <- 'byte' S+
SHORT <- 'short' S+
INT <- 'int' S+
LONG <- 'long' S+
CHAR <- 'char' S+
FLOAT <- 'float' S+
DOUBLE <- 'double' S+
TRUE <- 'true' S+
FALSE <- 'false' S+
NULL <- 'null' S+

Keyword<- PrimitiveType
Keyword: one of
        abstract    continue    for           new          switch
        assert      default     if            package      synchronized
             do          goto          private      this
        break             implements    protected    throw
                else        import        public       throws
        case        enum        instanceof    return       transient
        catch       extends                        try
                final       interface     static       void 
        class       finally               strictfp     volatile
        const              native        super        while    
Separator: one of
        (       )       {       }       [       ]       ;       ,       .
Operator: one of
        =       >    <    !       ~       ?       :
        ==      <=   >=   !=      &&      ||      ++      --
        +       -       *       /       &   |       ^       %       <<        >>        >>>
        +=      -=      *=      /=      &=  |=      ^=      %=      <<=       >>=       >>>=


InputElements <- InputElement+