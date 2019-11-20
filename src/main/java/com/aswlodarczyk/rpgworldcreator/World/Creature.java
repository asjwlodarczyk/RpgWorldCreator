package com.aswlodarczyk.rpgworldcreator.World;

import io.vavr.collection.List;

public class Creature {
    private String name;
    private String description;
    private List<String> expressions;

    public Creature(String name) {
        this.name = name;
        expressions = List.empty();
    }

    public Creature updateName(String name) {
        this.name = name;
        return this;
    }

    public Creature updateDescription(String description) {
        this.description = description;
        return this;
    }

    public Creature updateSingleExpression(String expression) {
        if (hasExpressions()) {
            expressions = expressions.append(expression);
        } else {
            expressions = List.of(expression);
        }
        return this;
    }

    public List<String> getExpressions() {
        return expressions;
    }

    public boolean hasExpressions() {
        return !expressions.isEmpty();
    }

    public String basicDescription() {
        return name;
    }

    public String fullDescription() {
        return name + "\n" + description;
    }

    public String toString() {
        return name + "\n" + description + "\n" + expressions;
    }
}
