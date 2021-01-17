package com.github.nilstrieb.hunterlang.interpreter;

import com.github.nilstrieb.hunterlang.lexer.WordType;

public class Value {
    private Type type;
    private String stringValue;
    private int intValue;
    private double floatValue;
    private boolean boolValue;

    public Value(String value, WordType tType) {
        if (tType == WordType.NUMBER && value.matches("\\d+")) {
            type = Type.INT;
            fromInt(value);
        } else if (tType == WordType.NUMBER && value.matches("\\d+.\\d")) {
            type = Type.FLOAT;
            fromFloat(value);
        } else if (tType == WordType.BOOL) {
            type = Type.BOOL;
            fromBool(value);
        } else if (tType == WordType.STRING) {
            type = Type.STRING;
            fromString(value);
        }
    }

    public Value() {
        type = Type.INT;
        fromInt("0");
    }

    public Value(boolean b) {
        type = Type.BOOL;
        fromBool(String.valueOf(b));
    }

    private void fromInt(String value){
        intValue = Integer.parseInt(value);
        stringValue = value;
        floatValue = intValue;
        boolValue = intValue != 0;
    }

    private void fromFloat(String value){
        floatValue = Double.parseDouble(value);
        intValue = (int) floatValue;
        stringValue = value;
        boolValue = intValue != 0;
    }

    private void fromBool(String value){
        boolValue = Boolean.parseBoolean(value);
        intValue = boolValue ? 1 : 0;
        floatValue = intValue;
        stringValue = value;
    }

    private void fromString(String value){
        stringValue = value;
        boolValue = false;
        intValue = stringValue.length();
        floatValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public double getFloatValue() {
        return floatValue;
    }

    public boolean isBoolValue() {
        return boolValue;
    }

    @Override
    public String toString() {
        return stringValue + " " + type;
    }

    public void setValue(String value){

    }

    public int compareTo(Value v2) {
        return Double.compare(this.floatValue, v2.floatValue);
    }

    public String toPrintString() {
        return stringValue;
    }

    public boolean getBoolValue() {
        return boolValue;
    }

    enum Type {
        INT, FLOAT, STRING, BOOL
    }
}