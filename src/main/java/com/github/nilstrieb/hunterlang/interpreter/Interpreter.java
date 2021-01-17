package com.github.nilstrieb.hunterlang.interpreter;

import com.github.nilstrieb.hunterlang.hllibrary.Library;
import com.github.nilstrieb.hunterlang.hllibrary.Wing;
import com.github.nilstrieb.hunterlang.lexer.WordType;
import com.github.nilstrieb.hunterlang.lib.ConsoleColors;
import com.github.nilstrieb.hunterlang.parser.ParseTreeBodyNode;
import com.github.nilstrieb.hunterlang.parser.ParseTreeNode;

import java.io.PrintStream;
import java.util.ArrayList;

public class Interpreter {

    ArrayList<ParseTreeNode> statements;
    Memory memory;

    public void start(ArrayList<ParseTreeNode> statementNodes) throws RuntimeException {
        System.out.println(ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLACK_BOLD + "------START INTERPRETER------" + ConsoleColors.RESET);
        interpret(statementNodes);
        System.out.println(ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLACK_BOLD + "------STOP INTERPRETER------" + ConsoleColors.RESET);
    }

    public void interpret(ArrayList<ParseTreeNode> statementNodes) throws RuntimeException {

        statements = statementNodes;
        memory = new Memory();
        boolean lastIfTrue = false;

        for (ParseTreeNode statement : statements) {
            switch (statement.getKey()) {
                case ASSIGNMENT -> {
                    ParseTreeNode memoryNode = statement.getChild(0);
                    ParseTreeNode valueNode = statement.getChild(1);

                    int memoryAdress = evaluateInt(memoryNode.getChild(0));
                    Value value = evaluate(valueNode);

                    memory.set(memoryAdress, value);
                    //Wing.dump(memory);
                }
                case IF -> {
                    if(evaluate(statement.getChild(0)).getBoolValue()){
                        lastIfTrue = true;
                        interpret(((ParseTreeBodyNode) statement).getStatements());
                    } else {
                        lastIfTrue = false;
                    }
                }
                case WANTS -> {

                }
                case ELSE -> {

                }
                case LIBFUNCCALL -> {
                    String call = statement.getValue();
                    ArrayList<ParseTreeNode> argsNode = statement.getChildren();
                    ArrayList<Value> args = new ArrayList<>();
                    for (ParseTreeNode parseTreeNode : argsNode) {
                        args.add(evaluate(parseTreeNode));
                    }
                    if (call.equals("Wing.dump")) {
                        Library.call(call, memory);
                    } else {
                        Library.call(call, args.toArray());
                    }
                }
                default -> {
                    throw new RuntimeException("illegal statement type: " + statement.getKey());
                }
            }
        }
    }

    private int evaluateInt(ParseTreeNode parent) {
        return evaluate(parent).getIntValue();
    }

    private Value evaluate(ParseTreeNode parent) {
        switch (parent.getKey()) {
            case MEMCALL -> {
                return memory.get(evaluateInt(parent.getChild(0)));
            }
            case NUMBER -> {
                return new Value(parent.getValue(), WordType.NUMBER);
            }
            case GTHAN -> {
                Value v2 = evaluate(parent.getChild(1));
                int comp = evaluate(parent.getChild(0)).compareTo(v2);
                return comp > 0 ? new Value(true) : new Value(false);
            }
            case LTHAN -> {
                int comp = evaluate(parent.getChild(0)).compareTo(evaluate(parent.getChild(1)));
                return comp < 0 ? new Value(true) : new Value(false);
            }
            case EQUALS -> {
                int comp = evaluate(parent.getChild(0)).compareTo(evaluate(parent.getChild(1)));
                return comp == 0 ? new Value(true) : new Value(false);
            }
            case STRING -> {
                return new Value(parent.getValue(), WordType.STRING);
            }
            case BOOL -> {
                return new Value(parent.getValue(), WordType.BOOL);
            }
            default -> {
                return new Value();
            }
        }
    }
}
