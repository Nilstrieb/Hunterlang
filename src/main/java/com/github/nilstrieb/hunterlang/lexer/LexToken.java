package com.github.nilstrieb.hunterlang.lexer;

import com.github.nilstrieb.hunterlang.lib.ConsoleColors;

public class LexToken {
    WordType key;
    String value;

    public LexToken(WordType key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return ConsoleColors.BLUE_BOLD + "{Key=" +
                ConsoleColors.RED_BOLD_BRIGHT + key +
                ConsoleColors.BLUE_BOLD + ", value=" +
                ConsoleColors.YELLOW + value + ConsoleColors.BLUE_BOLD + "}";
    }
}
