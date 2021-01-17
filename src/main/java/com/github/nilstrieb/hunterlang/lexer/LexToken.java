package com.github.nilstrieb.hunterlang.lexer;

import com.github.nilstrieb.hunterlang.hllibrary.FunctionArgLookup;
import com.github.nilstrieb.hunterlang.lib.ConsoleColors;
import com.github.nilstrieb.hunterlang.parser.ParseTreeNode;

public class LexToken {
    WordType key;
    String value;



    private int argOverrideValue = -1;

    public LexToken(WordType key, String value) {
        this.key = key;
        this.value = value;

        if (key.expectsPostArg() == -1) {
            if (key == WordType.LIBFUNCCALL) {
                //a function. will only support lib functions
                argOverrideValue = FunctionArgLookup.argLookup(value);
            }
        }
    }

    public LexToken(WordType key) {
        this(key, "");
    }

    public boolean hasPostfix() {
        if(argOverrideValue == -1){
            return key.expectsPostArg() > 0;
        } else {
            return argOverrideValue > 0;
        }

    }

    public boolean hasPrefix() {
        return key.expectsPreArg() > 0;
    }

    public ParseTreeNode toNode() {
        return new ParseTreeNode(key, value);
    }

    @Override
    public String toString() {
        return ConsoleColors.PURPLE_BOLD + "T" + ConsoleColors.BLUE_BOLD + "{Key=" +
                ConsoleColors.RED_BOLD_BRIGHT + key +
                ConsoleColors.BLUE_BOLD + ", value=" +
                ConsoleColors.YELLOW + value + ConsoleColors.BLUE_BOLD + "}" + ConsoleColors.RESET;
    }

    public WordType getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public int postfixCount() {
        if(argOverrideValue == -1){
            return key.expectsPostArg();
        } else {
            return argOverrideValue;
        }
    }
}
