package com.github.nilstrieb.hunterlang.interpreter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Memory {
    private ArrayList<Value> mem;

    public Memory() {
        mem = new ArrayList<>();
    }

    public Value get(int i){
        if(i >= mem.size()){
            return new Value();
        } else {
            return mem.get(i);
        }
    }

    public void set(int i, Value v){
        if(i >= mem.size()){
            int dif = i - mem.size() + 1;
            for (int j = 0; j < dif; j++) {
                mem.add(new Value());
            }
        }

        mem.set(i, v);
    }

    public ArrayList<Value> getList() {
        return mem;
    }
}
