import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads the file and uses the lexer class to tokenize the input.
 */
public class Shank
{
    /**
     * This method reads the file and uses the lexer class to tokenize the input.
     * @param args
     */
    public static void main(String[] args)
    {
        // Check to make sure args contains 1 argument. If not, then print an error message.
        if (args.length != 1)
        {
            System.out.println("Error: " + args.length + " arguments.");
            return;
        }

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Interpreter interpreter = new Interpreter();

        // Read all lines from that file.
        String fileName = args[0];

        try
        {
            Path myPath = Paths.get(fileName);
            List<String> lines = Files.readAllLines(myPath, StandardCharsets.UTF_8);
            List<Token> tokens = new ArrayList<>();

            for (String line : lines)
            {
                lexer.lex(line);

                System.out.println("Input: " + line);
                System.out.println("Output: " + lexer);

                tokens.addAll(lexer.getTokens());
                lexer.clearTokens();
            }

            ProgramNode programNode = parser.parse(tokens);

            SemanticAnalysis semanticAnalysis = new SemanticAnalysis();
            semanticAnalysis.checkAssignments(programNode);

            interpreter.interpret(programNode);
        }
        catch (IOException e)
        {
            System.out.println("Error reading file: " + e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
