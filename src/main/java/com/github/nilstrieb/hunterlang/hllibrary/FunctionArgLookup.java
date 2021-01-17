package com.github.nilstrieb.hunterlang.hllibrary;

public class FunctionArgLookup {

    public static int argLookup(String name) {
        return switch (name) {
            case "Leorio.say" -> 1;
            case "Leorio.listen" -> 0;
            case "Wing.dump" -> 0;
            default -> 0;
        };
    }
}
