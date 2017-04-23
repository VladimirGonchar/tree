package com.tree;

import com.tree.model.Node;
import com.tree.model.Tree;
import com.tree.parser.TreeParser;

import java.io.IOException;

public class TestRun {

    public static void main(String[] args) throws IOException {
        Tree tree = TreeParser.createTree(TestRun.class.getClassLoader()
                .getResourceAsStream("exampleFromTaskDescription.txt"));

        Node node = tree.getRoot();
        int level = 0;
        printNode(node, level);
    }

    private static void printNode(Node node, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("-");
        }
        System.out.println(node.getName());
        level++;
        if (node.getLeft() != null) {
            printNode(node.getLeft(), level);
        }
        if (node.getRight() != null) {
            printNode(node.getRight(), level);
        }
    }
}
