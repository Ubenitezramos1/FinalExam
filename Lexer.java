import java.util.*;
import java.util.regex.*;

public class Lexer{

    static HashMap<String, Token> keywordsAndOps = new HashMap<>();
    static ArrayList<Token> list = new ArrayList<Token>();
    static ArrayList<Character> lexList = new ArrayList<Character>();
    static int token;
    
    Lexer(ArrayList<Token> list){
        Lexer.list = list;
    }

    public static HashMap<String, Token> Lexi(){
        HashMap<String, Token> keywordsAndOps = new HashMap<>();
        keywordsAndOps.put("run", Token.WHILE); //while
        keywordsAndOps.put("given", Token.IF); //if
        keywordsAndOps.put("inst", Token.ELSE); //else
        keywordsAndOps.put("start", Token.START_FILE);
        keywordsAndOps.put("stop", Token.END_FILE);
        keywordsAndOps.put("num", Token.INTEGER_ID);
        keywordsAndOps.put("dec", Token.FLOAT_ID);
        keywordsAndOps.put("largo", Token.LONG_ID);
        keywordsAndOps.put("+", Token.PLUS);
        keywordsAndOps.put("-", Token.MINUS);
        keywordsAndOps.put("*", Token.MULTI);
        keywordsAndOps.put("/", Token.DIVIDE);
        keywordsAndOps.put("%", Token.MODULO);
        keywordsAndOps.put(".", Token.DOT);
        keywordsAndOps.put(",", Token.COMMA);
        keywordsAndOps.put("=", Token.EQUAL);
        keywordsAndOps.put(";", Token.SEMICOLON);
        keywordsAndOps.put("(", Token.LEFT_PARENTHESIS);
        keywordsAndOps.put(")", Token.RIGHT_PARENTHESIS);
        keywordsAndOps.put("{", Token.LEFT_BRACE);
        keywordsAndOps.put("}", Token.RIGHT_BRACE);
        keywordsAndOps.put(">", Token.GREATER_THAN);
        keywordsAndOps.put("<", Token.LESS_THAN);
        keywordsAndOps.put("&", Token.AND);
        keywordsAndOps.put("|", Token.OR);
        keywordsAndOps.put("!", Token.NOT);
        keywordsAndOps.put(">=", Token.GREATER_OR_EQUALS);
        keywordsAndOps.put("<=", Token.LESS_OR_EQUALS);
        keywordsAndOps.put("!=", Token.NOT_EQUALS);
        keywordsAndOps.put("==", Token.EQUAL_EQUAL);
        return keywordsAndOps;
        
    }

    public static void lex(String data, Map<String, Token> keyMap){
        HashMap<String, Token> keywordsAndOps = Lexi();
        for(int i = 0;i < data.length();i++){
            if(data.charAt(i) == ' ' && i < data.length()-1){
                continue;
            }else{
                lexList.add(data.charAt(i));
            if(lexList.get(lexList.size()-1) == '>'|lexList.get(lexList.size()-1) == '<'| lexList.get(lexList.size()-1) == '!'| lexList.get(lexList.size()-1) == '='){    //checks if lexeme is singular or is paired with '='
                if(i != data.length()-1 && data.charAt(i+1) == '='){
                    lexList.add(data.charAt(i+1));
                    list.add(keywordsAndOps.get(getValueString())); 
                    lexList.clear();
                    i++;
                }else{
                    list.add(keywordsAndOps.get(getValueString()));
                    lexList.clear();
                }
            }else if(lexList.size() == 1 && keywordsAndOps.containsKey(getValueString())){  //if lexeme is singular, it adds the singular token to list of tokens
                list.add(keywordsAndOps.get(getValueString()));
                lexList.clear();
            }else if(lexList.get(lexList.size()-1) >= '0' && lexList.get(lexList.size()-1) <='9'){  //checks if lexeme is the start of int, float, or long values
                while(i < data.length() && ((data.charAt(i) >= '0' && data.charAt(i) <='9') | data.charAt(i) == '.' | data.charAt(i) == 'l')){
                    lexList.add(data.charAt(i));
                    i++;
                }
                i--;    //reset counter by 1 to not skip characters thru iteration
                list.add(numCheck(data, i));
                lexList.clear();
                if(list.contains(Token.UNKNOWN)){   //throws error if num check comes back as unknown
                    try {
                        error();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                }
            }else if(lexList.get(lexList.size()-1) >= 'a' && lexList.get(lexList.size()-1) <= 'z'){ //checks letters for variables, keywords, or identifiers
                if(checkVar(data, i)){  //first check is for variables using checkVar() method, skips all the characters included in variable name
                        while(i < data.length() && ((!Character.isWhitespace(data.charAt(i))) && data.charAt(i) != '=' && data.charAt(i) != '(' && data.charAt(i) != ')'&& data.charAt(i) != '{' && data.charAt(i) != '}' && data.charAt(i) != ';')){
                        i++;
                    }
                    i--;    //reset counter by 1 to not skip characters thru iteration
                    lexList.clear();
                    list.add(Token.VAR);    //adds variable token to list of tokens
                }else{
                    i++;
                    while(i < data.length() && ((!Character.isWhitespace(data.charAt(i))) && data.charAt(i) != '=' && data.charAt(i) != '(' && data.charAt(i) != ')'&& data.charAt(i) != '{' && data.charAt(i) != '}' && data.charAt(i) != ';')){
                        lexList.add(data.charAt(i));
                        i++;
                    }
                    i--;    //reset counter by 1 to not skip characters thru iteration
                    if(keywordsAndOps.containsKey(getValueString())){
                        list.add(keywordsAndOps.get(getValueString()));
                        lexList.clear();
                    }else{
                        list.add(Token.UNKNOWN);
                        try {
                            error();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                    
            }
            }
            
        }
    }
    
    static String getValueString(){ //returns the string value of the lexeme array list
        String lexString = "";
        for(int i = 0; i < lexList.size(); i++){
            lexString = lexString + lexList.get(i);
        }
        return lexString;
    }

    public static Token numCheck(String data, int count){   //method to check what type of number literal the sequence of characters is
        String str = "" + lexList.get(0);
        count+=1;
        for(int i = 1; i < lexList.size(); i++){
            str += lexList.get(i);
        }
        Pattern int_lit = Pattern.compile("[0-9]+"); //regex expr for int literals
        Matcher m = int_lit.matcher(str);
        boolean isInt = m.matches();    //boolean that matches the regex to string of characters

        Pattern float_lit = Pattern.compile("[0-9]*[.][0-9]+"); //regex expr for float literals
        Matcher m1 = float_lit.matcher(str);
        boolean isFloat = m1.matches(); //boolean that matches the regex to string of characters

        Pattern long_lit = Pattern.compile("[0-9]+[l]"); //regex expr for long literals
        Matcher m2 = long_lit.matcher(str);
        boolean isLong = m2.matches();  //boolean that matches the regex to string of characters
        
        if(isInt){  //returns the appropriate token, if none match then unknown token is returned and error is thrown after method call
            return Token.INTEGER_LIT;
        }else if(isFloat){
            return Token.FLOAT_LIT;
        }else if(isLong){
            return Token.LONG_LIT;
        }else{
            return Token.UNKNOWN;
        }

    }

    public static boolean checkVar(String data, int count){ //checks if the sequence of characters follows the rule of variables
        String str = "" + lexList.get(lexList.size()-1);
        count +=1;
        Pattern var = Pattern.compile("[a-z][A-Za-z0-9_]{5,7}");    //regex expression for variables
        while(count < data.length() && ((!Character.isWhitespace(data.charAt(count))) && data.charAt(count) != '=' && data.charAt(count) != '(' && data.charAt(count) != ')'&& data.charAt(count) != '{' && data.charAt(count) != '}' && data.charAt(count) != ';')){  //as long as there are letters or digits it will check the string
            str = str + data.charAt(count);
            count++;
        }
        Matcher m3 = var.matcher(str);
        boolean isVar = m3.matches();   //checks for a variable match
        return isVar;
    }
    static void error() throws Exception{   //method that prints a lexical error and where the lexeme list was stopped
        System.out.println("Lexical error due to unknown lexeme: " + String.valueOf(lexList));
    }

    
}
