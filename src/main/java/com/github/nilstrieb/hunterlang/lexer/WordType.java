package com.github.nilstrieb.hunterlang.lexer;

import com.github.nilstrieb.hunterlang.hllibrary.FunctionArgLookup;

public enum WordType {
    ASSIGNMENT(1, 1), //memory, value
    MEMCALL(1), //adress
    STRING,
    NUMBER,
    BOOL,
    IF(1), //wants
    WANTS(2), //condition, body
    ELSE(1), //body
    BOPEN(-1), //body, close
    BCLOSE,
    GTHAN(1, 1),
    LTHAN(1, 1),
    EQUALS(1, 1),
    MINUS(1, 1),
    PLUS(1, 1),
    MULTIPLY(1, 1),
    DIVIDE(1, 1),
    MOD(1, 1),
    NEGATIVE(1),
    EMPTY, LIBFUNCCALL(-1);

    private int postArgAmount;
    private int preArgAmount;

    WordType(int preArgAmount, int postArgAmount) {
        this.postArgAmount = postArgAmount;
        this.preArgAmount = preArgAmount;
    }

    WordType(int postArgAmount) {
        this(0, postArgAmount);
    }

    WordType() {
        this(0, 0);
    }

    public int expectsPostArg() {
        return postArgAmount;
    }
    public int expectsPreArg() {
        return preArgAmount;
    }
}
