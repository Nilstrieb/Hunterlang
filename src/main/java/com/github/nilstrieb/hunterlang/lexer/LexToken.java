package com.github.nilstrieb.hunterlang.lexer;

import com.github.nilstrieb.hunterlang.lib.ConsoleColors;
import com.github.nilstrieb.hunterlang.parser.ParseTreeNode;

public class LexToken {
    WordType key;
    String value;

    public LexToken(WordType key, String value) {
        this.key = key;
        this.value = value;
    }

    public LexToken(WordType key) {
        this(key, "");
    }

    public int expectsPostArgCount() {
        return key.expectsPostArg();
    }
    public boolean expectsPreArg() {
        return key.expectsPreArg() > 0;
    }

    public ParseTreeNode toNode(){
        return new ParseTreeNode(key, value);
    }

    @Override
    public String toString() {
        return  ConsoleColors.PURPLE_BOLD + "T" + ConsoleColors.BLUE_BOLD + "{Key=" +
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
}
