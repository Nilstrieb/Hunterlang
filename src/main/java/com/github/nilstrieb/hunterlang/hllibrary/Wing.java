package com.github.nilstrieb.hunterlang.hllibrary;

import com.github.nilstrieb.hunterlang.interpreter.Memory;
import com.github.nilstrieb.hunterlang.interpreter.RuntimeException;
import com.github.nilstrieb.hunterlang.interpreter.Value;
import com.github.nilstrieb.hunterlang.lib.ConsoleColors;

import java.util.ArrayList;

public class Wing {
    public static void dump(Memory m) {
        ArrayList<Value> list = m.getList();
        for (int i = 0; i < list.size(); i++) {
            Value value = list.get(i);
            System.out.println(ConsoleColors.CYAN + i + ConsoleColors.YELLOW + " " + value + ConsoleColors.RESET);
        }
    }

    public static Value call(String name, Object ... args) throws RuntimeException {
        switch (name) {
            case "dump" -> {
                dump((Memory) args[0]);
                return new Value();
            }
            default -> {
                throw new RuntimeException("function not found");
            }
        }
    }
}