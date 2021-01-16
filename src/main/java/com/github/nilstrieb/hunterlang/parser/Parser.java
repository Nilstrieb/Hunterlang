package com.github.nilstrieb.hunterlang.parser;

import com.github.nilstrieb.hunterlang.hllibrary.FunctionArgLookup;
import com.github.nilstrieb.hunterlang.lexer.LexToken;
import com.github.nilstrieb.hunterlang.lexer.WordType;
import com.github.nilstrieb.hunterlang.lib.ConsoleColors;

import java.util.ArrayList;
import java.util.ListIterator;

public class Parser {

    private ListIterator<LexToken> iterator;

    private LexToken currentToken = null;
    private LexToken prevToken = null;
    private LexToken nextToken = null;

    private ParseTreeNode prevNode;

    public ArrayList<ParseTreeNode> parse(ArrayList<LexToken> tokens) throws ParseException {
        System.out.println(ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLACK_BOLD + "------START PARSER------" + ConsoleColors.RESET);

        ArrayList<ParseTreeNode> statements = new ArrayList<>();

        iterator = tokens.listIterator();

        currentToken = iterator.next();
        if (iterator.hasNext()) {
            nextToken = iterator.next();
        }

        //goes through each statement. On pass -> one statement
        do {
            System.out.println(ConsoleColors.GREEN_BRIGHT + "NEXT STATEMENT");

            prevNode = new ParseTreeNode();

            //start with first token
            ParseTreeNode startNode = currentToken.toNode();

            //if it wants a pre argument theres an error
            if (currentToken.expectsPreArg()) {
                throw new ParseException("previous expression expected, doesn't exist");
            }

            //calculate the arguments of the start node, parsing the whole statement
            doStatement(startNode, true);
            System.out.println(prevNode);
            statements.add(prevNode);
        } while (iterator.hasNext());

        System.out.println(ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLACK_BOLD + "------STOP PARSER------" + ConsoleColors.RESET);
        return statements;
    }

    private void doStatement(ParseTreeNode node, boolean isParent) throws ParseException {
        if (node.getKey() == WordType.MINUS && prevNode.getKey() != WordType.NUMBER) {
            node.setKey(WordType.NEGATIVE);
        }

        if (node.expectsPreArg()) {
            node.addChild(prevNode, true);
        }
        int expectedArg = node.expectsPostArgCount();
        if (expectedArg == -1) {
            if (node.getKey() == WordType.FUNCCALL) {
                //a function. will only support lib functions
                System.out.println(currentToken);
                expectedArg = FunctionArgLookup.argLookup(prevToken.getValue(), currentToken.getValue());
            } else {
                ParseTreeBodyNode bNode = new ParseTreeBodyNode(node);
                node = bNode;
                fillBody(bNode);
            }
        }
        if (isParent) {
            prevNode = node;
        }
        nextToken();
        if (expectedArg != 0) {
            for (int i = 0; i < expectedArg; i++) {
                ParseTreeNode child = currentToken.toNode();
                doStatement(child, false);
                node.addChild(child);
            }
        }

        if (currentToken != null && currentToken.expectsPreArg()) {
            doStatement(currentToken.toNode(), true);
        }
    }

    private void fillBody(ParseTreeBodyNode node) throws ParseException {
        int level = 0;
        nextToken();
        while (currentToken.getKey() != WordType.BCLOSE || level != 0) {
            if (currentToken.getKey() == WordType.BOPEN) {
                level++;
            } else if (currentToken.getKey() == WordType.BCLOSE) {
                level--;
            }
            node.addToken(currentToken);
            nextToken();
        }
        node.parse();
    }

    private void nextToken() {
        prevToken = currentToken;
        currentToken = nextToken;
        if (iterator.hasNext()) {
            nextToken = iterator.next();
        }
    }
}