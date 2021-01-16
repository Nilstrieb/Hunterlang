package com.github.nilstrieb.hunterlang.lexer;

import java.util.ArrayList;

public class LexedList {
    private ArrayList<ArrayList<LexToken>> list;

    public LexedList() {
        list = new ArrayList<>();
    }

    public void newLine(){
        list.add(new ArrayList<>());
    }

    public void add(int line, WordType type, String value) {
        list.get(line).add(new LexToken(type, value));
    }

    public void add(int line, WordType type){
        add(line, type, "");
    }

    public ArrayList<LexToken> get(int i) {
        return list.get(i);
    }
}
