package com.github.nilstrieb.hunterlang.lexer;

import com.github.nilstrieb.hunterlang.lib.ConsoleColors;
import com.github.nilstrieb.hunterlang.parser.ParseException;
import com.github.nilstrieb.hunterlang.parser.ParseTreeNode;
import com.github.nilstrieb.hunterlang.parser.Parser;

import java.util.ArrayList;

public class Lexer {

    public static final String COMMENT = "#";

    private static final String MEMCALL = "killua";
    public static final String ASSIGNMENT = "hunts";
    public static final String IF = "Gon";
    public static final String WANTS = "wants";
    public static final String ELSE = "got";
    public static final String BOPEN = "{";
    public static final String BCLOSE = "}";

    public static final String GTHAN = ">";
    public static final String LTHAN = "<";
    public static final String EQUALS = "=";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";
    public static final String MOD = "%";

    public static final String LIBFUNCCALL_REGEX = "(\\w+) does (\\w+).*";

    public static final String BOOL_REGEX = "(true|false).*";
    public static final String STRING_REGEX = "^\"(.*)\".*";
    public static final String NUMBER_REGEX = "^(-?\\d+.?\\d*).*";

    private ArrayList<LexToken> tokens;
    private ArrayList<LexToken> temp;

    public ArrayList<LexToken> lex(String code) {
        tokens = new ArrayList<>();
        temp = new ArrayList<>();

        String[] lines = code.split("\\r\\n|\\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            for (int j = 0; j < line.length(); j++) {
                String sub = line.substring(j);

                //COMMENT

                if (sub.startsWith(COMMENT)) {
                    break;
                }

                //FIXED KEYWORDS

                else if (sub.startsWith(MEMCALL)) {
                    addToken(WordType.MEMCALL);
                    j += MEMCALL.length() - 1;
                } else if (sub.startsWith(ASSIGNMENT)) {
                    addToken(WordType.ASSIGNMENT);
                    j += ASSIGNMENT.length() - 1;
                } else if (sub.startsWith(IF)) {
                    addToken(WordType.IF);
                    j += IF.length() - 1;
                } else if (sub.startsWith(WANTS)) {
                    addToken(WordType.WANTS);
                    j += WANTS.length() - 1;
                } else if (sub.startsWith(ELSE)) {
                    addToken(WordType.ELSE);
                    j += ELSE.length() - 1;
                }

                //CONDITIONS
                else if (sub.startsWith(GTHAN)) {
                    addToken(WordType.GTHAN);
                    j += GTHAN.length() - 1;
                } else if (sub.startsWith(LTHAN)) {
                    addToken(WordType.LTHAN);
                    j += LTHAN.length() - 1;
                } else if (sub.startsWith(EQUALS)) {
                    addToken(WordType.EQUALS);
                    j += EQUALS.length() - 1;
                }

                //OPERATORS
                else if (sub.startsWith(PLUS)) {
                    addToken(WordType.PLUS);
                } else if (sub.startsWith(MINUS)) {
                    addToken(WordType.MINUS);
                } else if (sub.startsWith(MULTIPLY)) {
                    addToken(WordType.MULTIPLY);
                } else if (sub.startsWith(DIVIDE)) {
                    addToken(WordType.DIVIDE);
                } else if (sub.startsWith(MOD)) {
                    addToken(WordType.MOD);
                }

                // BRACKETS
                else if (sub.startsWith(BOPEN)) {
                    addToken(WordType.BOPEN);
                } else if (sub.startsWith(BCLOSE)) {
                    addToken(WordType.BCLOSE);
                }

                //VALUES
                else if (sub.matches(STRING_REGEX)) {
                    String string = sub.replaceAll(STRING_REGEX, "$1");
                    addToken(WordType.STRING, string);
                    j += string.length() + 1;
                } else if (sub.matches(NUMBER_REGEX)) {
                    String number = sub.replaceAll(NUMBER_REGEX, "$1");
                    addToken(WordType.NUMBER, number);
                    j += number.length() - 1;
                } else if (sub.matches(BOOL_REGEX)) {
                    String bool = sub.replaceAll(BOOL_REGEX, "$1");
                    addToken(WordType.BOOL, bool);
                    j += bool.length() - 1;
                }

                //Calls
                else if (sub.matches(LIBFUNCCALL_REGEX)) {
                    String libName = sub.replaceAll(LIBFUNCCALL_REGEX, "$1");
                    String funcName = sub.replaceAll(LIBFUNCCALL_REGEX, "$2");
                    addToken(WordType.LIB, libName);
                    addToken(WordType.FUNCCALL, funcName);
                    j += libName.length(); //lib name
                    j += 1 + 4 + 1; // space + does + space
                    j += funcName.length() - 1; //func name
                }
            }
            System.out.println(ConsoleColors.GREEN_BOLD + line);
            System.out.println(ConsoleColors.BLUE_BOLD + temp);
            tokens.addAll(temp);
            temp.clear();
        }

        return tokens;
    }

    private void addToken(WordType type, String value) {
        temp.add(new LexToken(type, value));
    }

    private void addToken(WordType type) {
        temp.add(new LexToken(type));
    }

    public static void main(String[] args) {
        Lexer l = new Lexer();

        String assign = """
                killua0 hunts 3
                killua0 hunts -3.4 #hunts nothing
                killua1 hunts "hallo"
                #comment""";
        String ifs = """
                Gon wants false {
                    Leorio does say "false"
                }
                wants killua0 > 3 {
                    Leorio does say "big killua"
                } got {
                    Leorio does say "small killua"
                }
                """;
        String hierarchy = """
                killua0 hunts 3 > 3
                """;

        ArrayList<LexToken> tokens = l.lex(hierarchy);

        Parser p = new Parser();
        try {
            ArrayList<ParseTreeNode> nodes = p.parse(tokens);

            for (ParseTreeNode node : nodes) {
                System.out.println(node);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
