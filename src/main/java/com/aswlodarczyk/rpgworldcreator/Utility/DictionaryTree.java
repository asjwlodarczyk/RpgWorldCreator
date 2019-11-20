package com.aswlodarczyk.rpgworldcreator.Utility;

import io.vavr.collection.List;
import io.vavr.control.Option;

public class DictionaryTree<T> {
    private final Option<Character> keyPart;
    private Option<T> value;
    private List<DictionaryTree<T>> subtrees;

    public DictionaryTree() {
        this.keyPart = Option.none();
        this.value = Option.none();
        this.subtrees = List.empty();
    }

    private DictionaryTree(Option<Character> keyPart,
                           Option<T> value,
                           List<DictionaryTree<T>> subtrees)
    {
        this.keyPart = keyPart;
        this.value = value;
        this.subtrees = subtrees;
    }

    public Option<T> access(String key) {
        if ("".equals(key)) {
            return this.value;
        } else {
            return subtrees.find(tree -> key.charAt(0) == tree.keyPart.get())
                    .flatMap(tree -> tree.access(key.substring(1, key.length())));
        }
    }

    public DictionaryTree<T> extend(String key, T value) {
        if ("".equals(key)) {
            return new DictionaryTree<>(this.keyPart, Option.some(value), this.subtrees);
        } else if (subtrees.find(tree -> tree.keyPart.get() == key.charAt(0)).isDefined()) {
            DictionaryTree<T> subtreeToUpdate = subtrees.find(tree -> tree.keyPart.get() == key.charAt(0)).get();
            List<DictionaryTree<T>> newSubtrees = subtrees.filter(tree -> !tree.equals(subtreeToUpdate))
                    .append(subtreeToUpdate.extend(key.substring(1, key.length()), value));
            return new DictionaryTree<>(this.keyPart, this.value, newSubtrees);
        } else {
            DictionaryTree<T> newDictionaryTree =
                    new DictionaryTree<T>(Option.of(key.charAt(0)), Option.none(), List.empty())
                            .extend(key.substring(1, key.length()), value);
            List<DictionaryTree<T>> newSubtrees = List.ofAll(subtrees).append(newDictionaryTree);
            return new DictionaryTree<>(this.keyPart, this.value, newSubtrees);
        }
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof DictionaryTree &&
                this.keyPart.equals(((DictionaryTree) that).keyPart) &&
                this.value.equals(((DictionaryTree) that).value) &&
                this.subtrees.equals(((DictionaryTree) that).subtrees);
    }
}
