package com.company;

import java.io.*;
import java.util.List;

public class Main {

    private static final String PROCESS_FILE = "process-file";
    private static final String QUERY = "query";
    private static final String QUIT = "quit";

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.println("Please enter your command:");
                String line = br.readLine();
                String[] parts = line.split(" ");
                // assume the parts is formatted
                String cmd = parts[0];
                String param = parts[1];
                Trie trie = new Trie();
                QueryProcessor queryProcessor = new QueryProcessor(trie);
                switch (cmd) {
                    case PROCESS_FILE:
                        new Thread(new FileProcessor(param, trie)).start();
                        break;
                    case QUERY:
                        List<String> result = queryProcessor.match(param);
                        for (String s : result) {
                            System.out.println(s);
                        }
                        break;
                    case QUIT:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid command.");

                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
