package com.github.nilstrieb.hunterlang.run;

import com.github.nilstrieb.hunterlang.interpreter.Interpreter;
import com.github.nilstrieb.hunterlang.interpreter.RuntimeException;
import com.github.nilstrieb.hunterlang.lexer.LexToken;
import com.github.nilstrieb.hunterlang.lexer.Lexer;
import com.github.nilstrieb.hunterlang.parser.ParseException;
import com.github.nilstrieb.hunterlang.parser.ParseTreeNode;
import com.github.nilstrieb.hunterlang.parser.Parser;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Lexer l = new Lexer();

        String assign = """
                killua70 hunts 3 > 0
                killua0 hunts 3
                killua0 hunts -3.4 #hunts nothing
                killua1 hunts "hallo"
                #comment""";
        String ifs = """
                killua0 hunts 3 > 0
                Gon wants false {
                    Leorio does say "false"
                }
                wants killua0 > 3 {
                    Leorio does say "big killua"
                } got {
                    Leorio does say "small killua"
                }
                Gon wants true {
                    Gon wants true {
                        Leorio does say "that is actually very true"
                    }
                }
                """;
        String ifif = """
                Gon wants true {
                    Gon wants true {
                        Leorio does say "hallo"
                    }
                }
                """;
        String sif = """
                Gon wants true {
                    Leorio does say "hallo"
                }
                """;
        String hierarchy = """
                killua0 hunts 3 > 3
                """;

        String weird = """
                killua1 hunts 2
                Wing does dump
                Gon wants 5 < killua1 {
                    Leorio does say "its actually smaller gon!!!"
                } wants 1 < killua1 {
                    Leorio does say "at least that"
                }
                """;
        String hw = """
                Leorio does say "Hello World!"
                """;

        ArrayList<LexToken> tokens = l.lex(weird);

        Parser p = new Parser();
        try {
            ArrayList<ParseTreeNode> nodes = p.parse(tokens);

            for (ParseTreeNode parseTreeNode : nodes) {
                System.out.println(parseTreeNode);
            }

            Interpreter i = new Interpreter();
            i.start(nodes);
        } catch (ParseException | RuntimeException e) {
            e.printStackTrace();
        }
    }
}
