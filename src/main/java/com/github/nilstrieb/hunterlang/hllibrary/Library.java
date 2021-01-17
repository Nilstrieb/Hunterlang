package com.github.nilstrieb.hunterlang.hllibrary;

import com.github.nilstrieb.hunterlang.interpreter.RuntimeException;
import com.github.nilstrieb.hunterlang.interpreter.Value;

public class Library {
    public static Object call(String name, Object ... args) throws RuntimeException {
        String[] parts = name.split("\\.");
        return switch (parts[0]){
            case "Leorio" -> Leorio.call(parts[1], args);
            case "Wing" -> Wing.call(parts[1], args);
            default -> throw new RuntimeException("Unexpected lib: " + parts[0]);
        };
    }
}
