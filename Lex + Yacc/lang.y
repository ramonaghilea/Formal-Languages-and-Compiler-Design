%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define YYDEBUG 1

int yylex();
void yyerror(char *s);
%}

%token START
%token ENDPRG
%token BEGIN_STMT
%token END
%token READ
%token WRITE
%token IF
%token THEN
%token ELSE
%token WHILE
%token DO

%token id
%token ct

%token INT
%token BOOLEAN
%token CHAR
%token STRING
%token ARRAY

%token ADD
%token SUBTRACT
%token MULTIPLY
%token DIV
%token MOD
%token SMALLER
%token SMALLER_OR_EQUAL
%token GREATER
%token GREATER_OR_EQUAL
%token EQUAL
%token DIFFERENT
%token ASSIGNED
%token AND
%token OR

%token PARA_OPEN
%token PARA_CLOSED
%token SQUARE_BRACKET_OPEN
%token SQUARE_BRACKET_CLOSED
%token CURLY_BRACKET_OPEN
%token CURLY_BRACKET_CLOSED
%token SEMI_COLON
%token COLON

%%

program:	START decllist compstmt ENDPRG  { printf("program -> START decllist compstmt ENDPRG\n");}
		;
decllist:	  { printf("decllist -> E\n");}
		| declaration SEMI_COLON decllist { printf("decllist -> declaration ; decllist\n");}
		;
declaration: 	id COLON type  { printf("declaration -> id : type\n");}
		;
simple_type:	INT  { printf("simple_type -> INT\n");}
		| BOOLEAN  { printf("simple_type -> BOOLEAN\n");}
		| CHAR  { printf("simple_type -> CHAR\n");}
		| STRING  { printf("simple_type -> STRING\n");}
		;
array_type:	ARRAY SQUARE_BRACKET_OPEN INT SQUARE_BRACKET_CLOSED simple_type { printf("array_type -> ARRAY [ INT ] simple_type\n");}
		;
type:		simple_type  { printf("type -> simple_type\n");}
		| array_type  { printf("type -> array_type\n");}
		;
compstmt:	BEGIN_STMT stmtlist END  { printf("compstmt -> BEGIN stmtlist END\n");}
		;
stmtlist:	  { printf("stmtlist -> E\n");}
		| stmt SEMI_COLON stmtlist  { printf("stmtlist -> stmt ; stmtlist\n");}
		| stmt stmtlist  { printf("stmtlist -> stmt stmtlist\n");}
		;
stmt:		simple_stmt  { printf("stmt -> simple_stmt\n");}
		| struct_stmt  { printf("stmt -> struct_stmt\n");}
		;
simple_stmt:	assign_stmt  { printf("simple_stmt -> assign_stmt\n");}
		| io_stmt  { printf("simple_stmt -> io_stmt\n");}
		;
assign_stmt:	id ASSIGNED expression  { printf("assign_stmt -> id := expression\n");}
		;
expression:	term signed_expression  { printf("expression -> term signed_expression\n");}
		;
signed_expression:	  { printf("signed_expression -> E\n");}
			| operator expression  { printf("signed_expression -> operator expression\n");}
			;
term:		id  { printf("term -> id\n");}
		| ct  { printf("term -> ct\n");}
		;
operator:	ADD  { printf("operator -> +\n");}
		| SUBTRACT  { printf("operator -> -\n");}
		| MULTIPLY  { printf("operator -> *\n");}
		| DIV  { printf("operator -> /\n");}
		| MOD  { printf("operator -> %\n");}
		;
io_stmt:	READ PARA_OPEN id PARA_CLOSED  { printf("io_stmt -> READ ( id  )\n");}
		| WRITE PARA_OPEN id PARA_CLOSED  { printf("io_stmt -> WRITE ( id  )\n");}
		;
struct_stmt:	compstmt  { printf("struct_stmt -> compstmt\n");}
		| ifstmt  { printf("struct_stmt -> ifstmt\n");}
		| whilestmt  { printf("struct_stmt -> whilestmt\n");}
		;
ifstmt:		IF condition THEN compstmt elsestmt  { printf("ifstmt -> IF condition THEN compstmt elsestmt\n");}
		;
elsestmt:	  { printf("elsestmt -> E\n");}
		| ELSE compstmt  { printf("elsestmt -> ELSE compstmt\n");}
		;
whilestmt:	WHILE condition DO compstmt  { printf("whilestmt -> WHILE condition DO compstmt\n");}
		;
condition:	expression RELATION expression  { printf("consition -> expression RELATION expression\n");}
		;
RELATION:	SMALLER  { printf("RELATION -> <\n");}
		| SMALLER_OR_EQUAL  { printf("RELATION -> <=\n");}
		| GREATER  { printf("RELATION -> >\n");}
		| GREATER_OR_EQUAL  { printf("RELATION -> >=\n");}
		| EQUAL  { printf("RELATION -> =\n");}
		| DIFFERENT  { printf("RELATION -> !=\n");}
		| ASSIGNED  { printf("RELATION -> :=\n");}
		| AND  { printf("RELATION -> AND\n");}
		| OR  { printf("RELATION -> OR\n");}
		;
%%

void yyerror(char *s)
{
  printf("%s\n", s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
  if(argc>1) yyin = fopen(argv[1], "r");
  if((argc>2)&&(!strcmp(argv[2],"-d"))) yydebug = 1;
  if(!yyparse()) fprintf(stderr,"syntactically correct\n");
}

