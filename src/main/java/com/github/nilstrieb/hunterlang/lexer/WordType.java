package com.github.nilstrieb.hunterlang.lexer;

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
    FUNCCALL(-1), //args
    LIB(1), //function
    MINUS(1, 1),
    PLUS(1, 1),
    MULTIPLY(1, 1),
    DIVIDE(1, 1),
    MOD(1, 1),
    NEGATIVE(1),
    EMPTY;

    private int postArgAmount;
    private int preArgAmount;

    WordType(int postArgAmount, int preArgAmount) {
        this.postArgAmount = postArgAmount;
        this.preArgAmount = preArgAmount;
    }

    WordType(int postArgAmount) {
        this(postArgAmount, 0);
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
