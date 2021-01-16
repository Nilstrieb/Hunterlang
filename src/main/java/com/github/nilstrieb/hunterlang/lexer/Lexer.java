package com.github.nilstrieb.hunterlang.lexer;

import com.github.nilstrieb.hunterlang.lib.ConsoleColors;

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

    private LexedList tokens;

    public LexedList lex(String code) {
        tokens = new LexedList();
        ArrayList<LexToken> tempList = new ArrayList<>();
        String[] lines = code.split("\\r\\n|\\n");

        for (int i = 0; i < lines.length; i++) {
            tokens.newLine();
            String line = lines[i];
            for (int j = 0; j < line.length(); j++) {
                String sub = line.substring(j);

                //COMMENT

                if (sub.startsWith(COMMENT)) {
                    break;
                }

                //FIXED KEYWORDS

                else if (sub.startsWith(MEMCALL)) {
                    tokens.add(i, WordType.MEMCALL);
                    j += MEMCALL.length() - 1;
                } else if (sub.startsWith(ASSIGNMENT)) {
                    tokens.add(i, WordType.ASSIGNMENT);
                    j += ASSIGNMENT.length() - 1;
                } else if (sub.startsWith(IF)) {
                    tokens.add(i, WordType.IF);
                    j += IF.length() - 1;
                } else if (sub.startsWith(WANTS)){
                    tokens.add(i, WordType.WANTS);
                    j += WANTS.length() - 1;
                } else if (sub.startsWith(ELSE)){
                    tokens.add(i, WordType.ELSE);
                    j += ELSE.length() - 1;
                }

                //CONDITIONS
                else if (sub.startsWith(GTHAN)){
                    tokens.add(i, WordType.GTHAN);
                    j += GTHAN.length() - 1;
                } else if (sub.startsWith(LTHAN)){
                    tokens.add(i, WordType.LTHAN);
                    j += LTHAN.length() - 1;
                } else if (sub.startsWith(EQUALS)){
                    tokens.add(i, WordType.EQUALS);
                    j += EQUALS.length() - 1;
                }

                //OPERATORS
                else if (sub.startsWith(PLUS)){
                    tokens.add(i, WordType.PLUS);
                } else if (sub.startsWith(MINUS)){
                    tokens.add(i, WordType.MINUS);
                } else if (sub.startsWith(MULTIPLY)){
                    tokens.add(i, WordType.MULTIPLY);
                }else if (sub.startsWith(DIVIDE)){
                    tokens.add(i, WordType.DIVIDE);
                } else if (sub.startsWith(MOD)){
                    tokens.add(i, WordType.MOD);
                }

                // BRACKETS
                else if (sub.startsWith(BOPEN)){
                    tokens.add(i, WordType.BOPEN);
                } else if (sub.startsWith(BCLOSE)){
                    tokens.add(i, WordType.BCLOSE);
                }

                //VALUES
                else if (sub.matches(STRING_REGEX)) {
                    String string = sub.replaceAll(STRING_REGEX, "$1");
                    tokens.add(i, WordType.STRING, string);
                    j += string.length() + 1;
                } else if (sub.matches(NUMBER_REGEX)) {
                    String number = sub.replaceAll(NUMBER_REGEX, "$1");
                    tokens.add(i, WordType.NUMBER, number);
                    j += number.length() - 1;
                } else if (sub.matches(BOOL_REGEX)){
                    String bool = sub.replaceAll(BOOL_REGEX, "$1");
                    tokens.add(i, WordType.BOOL, bool);
                    j += bool.length() - 1;
                }

                //Calls
                else if(sub.matches(LIBFUNCCALL_REGEX)){
                    String libName = sub.replaceAll(LIBFUNCCALL_REGEX, "$1");
                    String funcName = sub.replaceAll(LIBFUNCCALL_REGEX, "$2");
                    tokens.add(i, WordType.LIB, libName);
                    tokens.add(i, WordType.FUNCCALL, funcName);
                    j += libName.length(); //lib name
                    j += 1 + 4 + 1; // space + does + space
                    j += funcName.length() - 1; //func name
                }
            }
            System.out.println(ConsoleColors.GREEN_BOLD + line);
            System.out.println(ConsoleColors.BLUE_BOLD + tokens.get(i));
        }

        return tokens;
    }

    public static void main(String[] args) {
        Lexer l = new Lexer();

        LexedList tokens = l.lex("""
                killua0 hunts 3
                killua0 hunts -3.4 #hunts nothing
                killua0 hunts 4
                killua1 hunts "hallo"
                #comment
                Gon wants false {
                    Leorio does say "false"
                } wants killua0 > 3 {
                    Leorio does say "big killua"
                } got {
                    Leorio does say "small killua"
                }
                """);
    }
}
