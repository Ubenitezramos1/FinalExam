import java.util.*;

public class Syntax{
    static ArrayList<Token> list = Lexer.list;
    private static int current = 0;
    private static Token currentToken;

    Syntax(ArrayList<Token> list){  //retrieves token list from lexical analyzer
        Syntax.list = list;
    }
    public static void Syntax_Analyze() throws Exception{
        if(list.get(0) == Token.START_FILE && list.get(list.size()-1) == Token.END_FILE){   //begins code if correct beginning and end statements are present
            getNextToken();
            stmt();
            System.out.print(currentToken + " " + list.indexOf(currentToken));  //will print 'END_FILE' and the index of the token if successfully processed
        }else{
            System.out.println("File should start with 'start' and end with 'end'. Current start: "
            + list.get(0) + ", Current end: " + list.get(list.size()-1));
        }
    }
    public static void getNextToken(){  //retrieves the next token type from the token list
        if (current < list.size()){
            current += 1;
        }
        currentToken = list.get(current);
    }

    public static void stmt() throws Exception{ //stmt method to begin analyzing tokens recursively
        switch (currentToken){
            case IF:
                if_stmt();
                break;
            case WHILE:
                while_stmt();
                break;
            case INTEGER_ID:
                assign();
                break;
            case FLOAT_ID:
                assign();
                break;
            case LONG_ID:
                assign();
                break;
            case VAR:
                assign();
                break;
            case LEFT_BRACE:
                block();
                break;
            case UNKNOWN:
                error();
                break;
            default:
                error();
                break;
        }
    }
    public static void block() throws Exception{    //block stmt method
        if (currentToken == Token.LEFT_BRACE){ //recursively checks for appropriate tokens according to language rules
            getNextToken();
            while (currentToken == Token.IF | currentToken == Token.WHILE | currentToken == Token.INTEGER_ID | currentToken == Token.FLOAT_ID | currentToken == Token.LONG_ID | currentToken == Token.VAR | currentToken == Token.LEFT_BRACE){
                stmt(); 
            }
            if(currentToken == Token.RIGHT_BRACE){
                getNextToken();
            }else{
                error();    //throws error if the code does not follow the rules
            }
        }
    }
    public static void if_stmt() throws Exception{  //if stmt method
        if(currentToken == Token.IF){
            getNextToken();
            if(currentToken == Token.LEFT_PARENTHESIS){ //recursively retrieves boolean expression for the if statement until current token is a right parenthesis
                getNextToken();
                bool_expr();
                if(currentToken == Token.RIGHT_PARENTHESIS){    //checks if stmt should be called if the current token is one of the stmt tokens
                    getNextToken();
                    if(currentToken == Token.IF | currentToken == Token.WHILE | currentToken == Token.INTEGER_ID | currentToken == Token.FLOAT_ID | currentToken == Token.LONG_ID | currentToken == Token.LEFT_BRACE){
                        stmt();
                    }else{
                        error();
                    }
                    if(currentToken == Token.ELSE){ //else stmt also recursively calls stmt with appropriate tokens
                        getNextToken();
                        stmt();
                    }
                }else{
                    error();
                }
            }else{
                error();
            }
        }
    }
    public static void while_stmt() throws Exception{   //while stmt method
        if(currentToken == Token.WHILE){
            getNextToken();
            if(currentToken == Token.LEFT_PARENTHESIS){ //recursively calls boolean expression until a right parenthesis appears
                getNextToken();
                bool_expr();
                if(currentToken == Token.RIGHT_PARENTHESIS){    //once while loop boolean expr ends it will get next stmt
                    getNextToken();
                    stmt();
                }else{
                    error();
                }
            }else{
                error();
            }
        }
    }
    public static void assign() throws Exception{   //assign stmt method
        if(currentToken == Token.INTEGER_ID | currentToken == Token.FLOAT_ID | currentToken == Token.LONG_ID){  //checks if there is a variable afer an identifier; if not, language rule is broken and error will be thrown
            getNextToken();
            if(currentToken == Token.VAR){
                getNextToken();
                if(currentToken == Token.EQUAL){    //checks for correct syntax for assign stmt
                    getNextToken();
                    expr();
                }else if(currentToken != Token.SEMICOLON){
                    error();
                }
                if(currentToken == Token.SEMICOLON){    //semicolon means end of assign stmt
                    getNextToken();
                    if(currentToken != Token.RIGHT_BRACE){  //right brace means end of block stmt so the code will continue until a brace is found
                        stmt();
                    }
                }else{
                    error();
                }
            }
        }else if(currentToken == Token.VAR){    //variables do not need identifiers in language so code will still run if no identifier is in front
            getNextToken();
                if(currentToken == Token.EQUAL){    //checks proper syntax
                    getNextToken();
                    expr();
                }else{
                    error();
                }
                if(currentToken == Token.SEMICOLON){
                    getNextToken();
                    if(currentToken != Token.RIGHT_BRACE){  //right brace means end of block stmt so the code will continue until a brace is found
                        stmt();
                    }
                }else{
                    error();    //throws errors if syntax does not match language rules
                }
        }else{
            error();
        }
    }
    public static void expr() throws Exception{ //expression method that recursively calls term method
        term(); 
        while(currentToken == Token.PLUS | currentToken == Token.MINUS){
            getNextToken();
            term();
        }
    }
    public static void term() throws Exception{ //term method that recursively calls factor method
        factor();
        while(currentToken == Token.MULTI | currentToken == Token.DIVIDE | currentToken == Token.MODULO){
            getNextToken();
            factor();
        }
    }
    public static void factor() throws Exception{   //factor method is end of recursive calls; if syntax does not match up until here, code will throw error
        if(currentToken == Token.INTEGER_ID | currentToken == Token.FLOAT_ID | currentToken == Token.LONG_ID | currentToken == Token.VAR){
            if(currentToken != Token.VAR){
                getNextToken();
                if(currentToken == Token.VAR){
                    getNextToken();
                }else{
                    error();
                }
            }else{
                getNextToken();
            }
        }else if(currentToken == Token.INTEGER_LIT | currentToken == Token.FLOAT_LIT | currentToken == Token.LONG_LIT){
            getNextToken();
        }else if(currentToken == Token.LEFT_PARENTHESIS){
            getNextToken();
            expr();
            if(currentToken == Token.RIGHT_PARENTHESIS){
                getNextToken();
            }else{
                error();
            }
        }else{
            error();
        }
    }
    public static void bool_expr() throws Exception{    //boolean expression method that recursively calls band method
        band();
        while(currentToken == Token.OR){
            getNextToken();
            band();
        }
    }
    public static void band() throws Exception{ //band method that recursively calls beq method
        beq();
        while(currentToken == Token.AND){
            getNextToken();
            beq();
        }
    }
    public static void beq() throws Exception{  //beq method that recursively calls brel method
        brel();
        while(currentToken == Token.NOT_EQUALS | currentToken == Token.EQUAL_EQUAL){
            getNextToken();
            brel();
        }
    }
    public static void brel() throws Exception{ //brel method that recursively calls bexpr method
        bexpr();
        while(currentToken == Token.LESS_OR_EQUALS | currentToken == Token.GREATER_OR_EQUALS | currentToken == Token.LESS_THAN | currentToken == Token.GREATER_THAN){
            getNextToken();
            bexpr();
        }
    }
    public static void bexpr() throws Exception{    //bexpr method that recursively calls bterm method
        bterm();
        while(currentToken == Token.PLUS | currentToken == Token.MINUS){
            getNextToken();
            bterm();
        }
    }
    public static void bterm() throws Exception{    //bterm method that recursively calls bnot method
        bnot();
        while(currentToken == Token.MULTI | currentToken == Token.DIVIDE | currentToken == Token.MODULO){
            getNextToken();
            bnot();
        }
    }
    public static void bnot() throws Exception{ //bnot method that calls factor method
        if(currentToken == Token.NOT){
            getNextToken();
            factor();
        }else{
            factor();
        }
    } 

    static void error() throws Exception{   //method that prints a lexical error and where the lexeme list was stopped
        System.out.println("Syntax error: " + String.valueOf(list.get(current - 1)) + " at " + (current-1) +
        " followed by " + String.valueOf(list.get(current)) + " at " + current);
    }
    
}
