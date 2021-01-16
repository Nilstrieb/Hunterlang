package com.github.nilstrieb.hunterlang.parser;

import com.github.nilstrieb.hunterlang.lexer.LexToken;
import com.github.nilstrieb.hunterlang.lexer.WordType;
import com.github.nilstrieb.hunterlang.lib.ConsoleColors;
import com.sun.source.tree.LambdaExpressionTree;

import java.util.ArrayList;
import java.util.ListIterator;

public class Parser {

    public ArrayList<ParseTreeNode> parse(ArrayList<LexToken> tokens) throws ParseException {

        System.out.println(ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLACK_BOLD + "------START PARSER------" + ConsoleColors.RESET);

        ArrayList<ArrayList<LexToken>> split = splitStatements(tokens);
        for (ArrayList<LexToken> lexTokens : split) {
            System.out.println(lexTokens);
        }

        ArrayList<ParseTreeNode> list = new ArrayList<>();

        ParseTreeNode parent = null;
        for (ArrayList<LexToken> statement : split) {
            ListIterator<LexToken> iterator = statement.listIterator();

            LexToken current = iterator.next();

            switch (current.getKey()) {
                case MEMCALL -> {
                    //assignment
                    int assignmentIndex = 0;
                    while (iterator.hasNext()) {
                        assignmentIndex++;
                        current = iterator.next();
                        if (current.getKey() == WordType.ASSIGNMENT) {
                            break;
                        }
                    }

                    parent = current.toNode();
                    parent.addChild(evaluate(new ArrayList<>(statement.subList(0, assignmentIndex))));
                    parent.addChild(evaluate(new ArrayList<>(statement.subList(assignmentIndex + 1, statement.size()))));
                }
                case IF, WANTS -> {
                    parent = new ParseTreeBodyNode(current.toNode());
                    ParseTreeBodyNode parentBN = (ParseTreeBodyNode) parent;
                    while (current.getKey() != WordType.BOPEN) {
                        current = iterator.next();
                        parent.addChild(current.toNode());
                    }

                    int brackets = 0;
                    while ((brackets != 0 || current.getKey() != WordType.BCLOSE) && iterator.hasNext()) {
                        current = iterator.next();
                        parentBN.addToken(current);
                        if (current.getKey() == WordType.BOPEN) {
                            brackets++;
                        } else if (current.getKey() == WordType.BCLOSE) {
                            brackets--;
                        }
                    }
                    parentBN.parse();
                }
                case ELSE -> {

                }
                case LIBFUNCCALL -> {

                }
                case BCLOSE -> {
                }

                default -> throw new ParseException("Unexpected value at start of statement: " + current.getKey());
            }

            list.add(parent);
        }

        System.out.println(ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLACK_BOLD + "------STOP PARSER------" + ConsoleColors.RESET);
        return list;
    }

    /**
     * Evalues an expression. For example something like killuakillua3+4 > 6
     *
     * @param tokens The tokens
     * @return The parent node
     */
    private ParseTreeNode evaluate(ArrayList<LexToken> tokens) throws ParseException {
        ListIterator<LexToken> iterator = tokens.listIterator();
        LexToken prev;
        LexToken curr;
        LexToken next;
        ParseTreeNode currentParent;

        prev = iterator.next();
        if (iterator.hasNext()) {
            curr = iterator.next();
        } else {
            //1 token
            return prev.toNode();
        }

        if (iterator.hasNext()) {
            next = iterator.next();
        } else {
            //2 tokens
            currentParent = prev.toNode();
            currentParent.addChild(curr.toNode());
            return currentParent;
        }

        if (curr.hasPrefix()) {
            currentParent = curr.toNode();
            currentParent.addChild(prev.toNode(), true);
            currentParent.addChild(evaluate(new ArrayList<>(tokens.subList(2, tokens.size()))));
        } else if (prev.postfixCount() == 1) {
            currentParent = prev.toNode();
            currentParent.addChild(evaluate(new ArrayList<>(tokens.subList(1, tokens.size()))));
        } else {
            throw new ParseException("more than one arg: " + prev.postfixCount() + " thrown on: " + prev + " all: " + tokens);
        }
        return currentParent;
    }

    private ArrayList<ArrayList<LexToken>> splitStatements(ArrayList<LexToken> tokens) throws ParseException {
        System.out.println(ConsoleColors.YELLOW_BACKGROUND_BRIGHT + ConsoleColors.BLACK_BOLD + "SPLITTER" + ConsoleColors.RESET);
        ArrayList<ArrayList<LexToken>> tokensSplit = new ArrayList<>();
        tokensSplit.add(new ArrayList<>());

        ListIterator<LexToken> iterator = tokens.listIterator();

        if (tokens.size() < 2) {
            throw new ParseException("code has to consist of at least 2 tokens");
        }

        LexToken prev;
        LexToken curr = iterator.next();
        tokensSplit.get(0).add(curr);

        int i = 0;
        int brackets = 0;
        while (iterator.hasNext()) {
            prev = curr;
            curr = iterator.next();

            if (curr.getKey() == WordType.BOPEN) {
                brackets++;
            }
            if (brackets == 0 && !prev.hasPostfix() && !curr.hasPrefix()) {
                //when there is no connection between two tokens, they are part of seperate statements
                tokensSplit.add(new ArrayList<>());
                i++;
            }

            tokensSplit.get(i).add(curr);

            if (curr.getKey() == WordType.BCLOSE) {
                brackets--;
            }
        }
        return tokensSplit;
    }
}