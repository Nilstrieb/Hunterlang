package com.github.nilstrieb.hunterlang.parser;

import com.github.nilstrieb.hunterlang.lexer.LexToken;
import com.github.nilstrieb.hunterlang.lexer.WordType;
import com.github.nilstrieb.hunterlang.lib.ConsoleColors;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Parser {

    public ArrayList<ParseTreeNode> parse(ArrayList<LexToken> tokens) throws ParseException {

        System.out.println(ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLACK_BOLD + "------START PARSER------" + ConsoleColors.RESET);

        ArrayList<ArrayList<LexToken>> split = splitStatements(tokens);
        for (ArrayList<LexToken> lexTokens : split) {
            System.out.println(lexTokens);
        }

        System.out.println(ConsoleColors.YELLOW_BACKGROUND_BRIGHT + ConsoleColors.BLACK_BOLD + "PARSER" + ConsoleColors.RESET);

        ArrayList<ParseTreeNode> list = new ArrayList<>();

        ParseTreeNode parent = null;
        for (ArrayList<LexToken> statement : split) {
            if (statement.size() == 0) {
                throw new ParseException("empty body");
            }
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
                        //BOPEN does not need to be a child
                    }

                    //add body tokens
                    int brackets = 0;
                    current = iterator.next();
                    while ((brackets != 0 || current.getKey() != WordType.BCLOSE) && iterator.hasNext()) {
                        parentBN.addToken(current);
                        System.out.println("addt " + current);
                        current = iterator.next();
                        if (current.getKey() == WordType.BOPEN) {
                            brackets++;
                        } else if (current.getKey() == WordType.BCLOSE) {
                            if(brackets == 1){
                                parentBN.addToken(current);
                            }
                            brackets--;
                        }
                    }
                    parentBN.parse();
                }
                case ELSE -> {
                    parent = new ParseTreeBodyNode(current.toNode());
                    ParseTreeBodyNode parentBN = (ParseTreeBodyNode) parent;
                    int brackets = 0;
                    current = iterator.next();
                    if (current.getKey() != WordType.BOPEN) {
                        throw new ParseException("{ Expected: " + current.getKey());
                    }
                    current = iterator.next();
                    while ((brackets != 0 || current.getKey() != WordType.BCLOSE) && iterator.hasNext()) {
                        parentBN.addToken(current);
                        current = iterator.next();
                        if (current.getKey() == WordType.BOPEN) {
                            brackets++;
                        } else if (current.getKey() == WordType.BCLOSE) {
                            brackets--;
                        }
                    }
                    parentBN.parse();
                }
                case LIBFUNCCALL -> {
                    parent = current.toNode();
                    if(current.hasPostfix()) {
                        parent.addChild(evaluate(statement.subList(1, statement.size())));
                    }
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
    private ParseTreeNode evaluate(List<LexToken> tokens) throws ParseException {
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

        if (tokens.size() < 1) {
            return tokensSplit;
        } else if (tokens.size() == 1) {
            tokensSplit.get(0).add(iterator.next());
            return tokensSplit;
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