package com.github.nilstrieb.hunterlang.hllibrary;

public class FunctionArgLookup {

    public static int argLookup(String lib, String name){
        return switch (lib){
            case "Leorio" -> switch (name){
                case "say" -> 1;
                case "listen" -> 0;
                default -> 0;
            };
            default -> 0;
        };
    }
}
