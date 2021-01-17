package com.github.nilstrieb.hunterlang.hllibrary;

import com.github.nilstrieb.hunterlang.interpreter.RuntimeException;
import com.github.nilstrieb.hunterlang.interpreter.Value;
import com.github.nilstrieb.hunterlang.lexer.WordType;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Leorio {

    public static void say(Value s) {
        System.out.println(s.toPrintString());
    }

    public static String listen() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static Value call(String name, Object[] args) throws RuntimeException {
        switch (name) {
            case "say" -> {
                say((Value) args[0]);
                return new Value();
            }
            case "listen" -> {
                return new Value(listen(), WordType.STRING);
            }
            default -> {
                throw new RuntimeException("function not found");
            }
        }
    }
}
