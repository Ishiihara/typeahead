package com.company;

import java.util.List;

public class QueryProcessor {

    private final Trie trie;

    public QueryProcessor(Trie trie) {
        this.trie = trie;
    }

    public List<String> match(String prefix) {
        return trie.keysWithPrefix(prefix);
    }

    public String get(String key) {
        return trie.get(key);
    }
}
