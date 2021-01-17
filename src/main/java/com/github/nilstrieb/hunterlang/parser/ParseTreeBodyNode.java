package com.github.nilstrieb.hunterlang.parser;

import com.github.nilstrieb.hunterlang.lexer.LexToken;
import com.github.nilstrieb.hunterlang.lexer.WordType;

import java.util.ArrayList;

public class ParseTreeBodyNode extends ParseTreeNode{
    private ArrayList<ParseTreeNode> statements;
    private ArrayList<LexToken> tokens;

    public ParseTreeBodyNode(WordType key, String value) {
        super(key, value);
        this.statements = new ArrayList<>();
        this.tokens = new ArrayList<>();
    }

    public ParseTreeBodyNode(ParseTreeNode node) {
        this(node.key, node.value);
    }

    public ArrayList<ParseTreeNode> getStatements() {
        return statements;
    }

    public void addToken(LexToken currentToken) {
        tokens.add(currentToken);
    }

    @Override
    public String toString() {
        return super.toString() + statements;
    }

    @Override
    public String toString(String indent) {
        return super.toString(indent) + statements;
    }

    public void parse() throws ParseException {
        Parser p = new Parser();
        statements = p.parse(tokens);
    }

    public ArrayList<LexToken> getTokens() {
        return tokens;
    }
}
