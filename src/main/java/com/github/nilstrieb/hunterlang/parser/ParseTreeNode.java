package com.github.nilstrieb.hunterlang.parser;

import com.github.nilstrieb.hunterlang.lexer.WordType;
import com.github.nilstrieb.hunterlang.lib.ConsoleColors;

import java.util.ArrayList;

public class ParseTreeNode {
    WordType key;
    String value;
    ArrayList<ParseTreeNode> children;

    int remainingPreArgs;
    int remainingPostArgs;

    public ParseTreeNode(WordType key, String value) {
        this.key = key;
        this.value = value;
        children = new ArrayList<>();
        remainingPostArgs = key.expectsPostArg();
        remainingPreArgs = key.expectsPreArg();
    }

    public ParseTreeNode() {
        this(WordType.EMPTY, "");
    }

    public ArrayList<ParseTreeNode> getChildren() {
        return children;
    }

    public void addChild(ParseTreeNode n, boolean pre) {
        children.add(n);
        if (pre) {
            remainingPreArgs--;
        }
    }

    public void addChild(ParseTreeNode n) {
        addChild(n, false);
    }

    public int expectsPostArgCount() {
        return remainingPostArgs;
    }

    public boolean expectsPreArg() {
        return remainingPreArgs > 0;
    }

    public void setKey(WordType key) {
        this.key = key;
        remainingPostArgs = key.expectsPostArg();
        remainingPreArgs = key.expectsPreArg();
    }

    @Override
    public String toString() {
        return ConsoleColors.PURPLE_BOLD + "N" + ConsoleColors.BLUE_BOLD + "{name=" +
                ConsoleColors.RED_BOLD_BRIGHT + key +
                ConsoleColors.BLUE_BOLD + ", value=" +
                ConsoleColors.YELLOW + value +
                ConsoleColors.BLUE_BOLD + ", children=" + printList(children, "") +
                ConsoleColors.BLUE_BOLD + "}" + ConsoleColors.RESET;
    }

    public String toString(String indent) {
        return ConsoleColors.PURPLE_BOLD + indent + "N" + ConsoleColors.BLUE_BOLD + "{name=" +
                ConsoleColors.RED_BOLD_BRIGHT + key +
                ConsoleColors.BLUE_BOLD + ", value=" +
                ConsoleColors.YELLOW + value +
                ConsoleColors.BLUE_BOLD + ", children=" + printList(children, indent + "   ") +
                ConsoleColors.BLUE_BOLD + "}" + ConsoleColors.RESET;
    }

    public String printList(ArrayList<ParseTreeNode> list, String indent) {
        if(list.size() == 0){
            return "[]";
        }

        String indentHigher = indent + "   ";

        StringBuilder sb = new StringBuilder();
        for (ParseTreeNode node : list) {
            sb.append("[\n");
            sb.append(node.toString(indentHigher));
        }
        sb.append("]");
        return sb.toString();
    }

    public WordType getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
