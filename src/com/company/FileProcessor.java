package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileProcessor implements Runnable {

    private final String filename;
    private final Trie trie;

    public FileProcessor(String filename, Trie trie) {
        this.filename = filename;
        this.trie = trie;
    }

    public void run() {
        Map<String, List<String>> tokens = processFile(filename);
        mergeTrie(trie, tokens);
    }

    private Map<String, List<String>> processFile(String filename) {
        try {
            File file = new File(filename);
            Scanner reader = new Scanner(file);

            Map<String, List<String>> allTokens = new HashMap<>();
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                ArrayList<String> tokens = getTokens(data);
                allTokens.put(data, tokens);
            }
            reader.close();
            return allTokens;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void mergeTrie(Trie trie, Map<String, List<String>> tokens) {
        for (Map.Entry<String, List<String>> entry: tokens.entrySet()) {
            String key = entry.getKey();
            List<String> val = entry.getValue();
            for (String token : val) {
                trie.put(token, key);
            }
        }
    }

    // Each line has the format [Year of Release]\t[Country-code]\t[Movie-title]
    // Since we are looking for prefix matches for any words in a movie title,
    // we need to save the whole title and each word as the keys in the Trie.
    private ArrayList<String> getTokens(String data) {
        String[] parts = data.split("\\t");
        String title = parts[2];
        ArrayList<String> result = new ArrayList<>();
        result.add(title);
        String[] tokens = title.split(" ");
        if (tokens.length != 1) {
            for (String token : tokens) {
                result.add(token);
            }
        }
        return result;
    }
}
