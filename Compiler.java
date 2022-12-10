import java.io.*;
import java.util.*;

public class Compiler {
    public static void main(String[] args) throws Exception{
        String data = "";
        try {
            File testFile = new File("test.txt");
            try (Scanner reader = new Scanner(testFile)) {
                while (reader.hasNextLine()) {
                    data += reader.nextLine();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Lexer.lex(data, Lexer.keywordsAndOps);
        System.out.println(Lexer.list.toString());
        Syntax.Syntax_Analyze();
    }

}
