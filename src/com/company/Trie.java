package com.company;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Trie {

    private static final int R = 256;

    ReadWriteLock rwLock = new ReentrantReadWriteLock();

    static class TrieNode{
        TrieNode[] children = new TrieNode[R];
        String val = null;
    }

    TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void put(String key, String val) {
        try {
            rwLock.writeLock().lock();
            root = put(root, key, val, 0);
        } finally {
            rwLock.writeLock().unlock();
        }

    }

    private TrieNode put(TrieNode node, String key, String val, int i) {
        if (node == null) {
            node = new TrieNode();
        }
        if (i == key.length()) {
            node.val = val;
            return node;
        }
        char c = key.charAt(i);
        node.children[c] = put(node.children[c], key, val, i + 1);
        return node;
    }

    public List<String> keysWithPrefix(String prefix) {
        try {
            rwLock.readLock().lock();
            List<String> res = new LinkedList<>();
            TrieNode x = getNode(root, prefix);
            if (x == null) {
                return res;
            }
            traverse(x, new StringBuilder(prefix), res);
            return res;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public String get(String key) {
        try {
            rwLock.readLock().lock();
            TrieNode x = getNode(root, key);
            if (x == null || x.val == null) {
                return null;
            }
            return x.val;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    private TrieNode getNode(TrieNode node, String key) {
        TrieNode p = node;
        for (int i = 0; i < key.length(); i++) {
            if (p == null) {
                return null;
            }
            char c = key.charAt(i);
            p = p.children[c];
        }
        return p;
    }

    private void traverse(TrieNode node, StringBuilder path, List<String> res) {
        if (node == null) {
            return;
        }

        if (node.val != null) {
            res.add(path.toString());
        }

        for (char c = 0; c < R; c++) {
            path.append(c);
            traverse(node.children[c], path, res);
            path.deleteCharAt(path.length() - 1);
        }
    }
}
