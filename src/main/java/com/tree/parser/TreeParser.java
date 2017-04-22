package com.tree.parser;

import com.tree.Tree;
import com.tree.TreeBuilder;

import java.io.*;

/**
 * Creates {@link Tree} from InputStream.
 */
public class TreeParser {

    private static final String EMPTY_CHILD = "#";
    private static final String NODE_NAMES_DELIMITER = ",";
    private static final int NODES_PER_LINE = 3;

    private static final int HEAD_INDEX = 0;
    private static final int LEFT_LEAF_INDEX = 1;
    private static final int RIGHT_LEAF_INDEX = 2;

    public static Tree createTree(InputStream is) throws IOException {
        return TreeParser.createTree(new InputStreamReader(is));
    }

    public static Tree createTree(Reader is) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(is);
        TreeBuilder builder = new TreeBuilder();

        bufferedReader
                .lines()
                .forEach(line -> parseSingleLine(line, builder));

        return builder.build();
    }

    private static void parseSingleLine(String textLine, TreeBuilder builder) {

        String[] nodeNames = textLine.split(NODE_NAMES_DELIMITER);
        if (nodeNames.length != NODES_PER_LINE) {
            throw new IllegalArgumentException("Line (" + textLine + ") is in wrong format");
        }
        String headName = nodeNames[HEAD_INDEX];
        String leftLeafName = nodeNames[LEFT_LEAF_INDEX];
        String rightLeafName = nodeNames[RIGHT_LEAF_INDEX];

        builder.addNode(headName,
                isValidLeafName(leftLeafName) ? leftLeafName : null,
                isValidLeafName(rightLeafName) ? rightLeafName : null);
    }

    private static boolean isValidLeafName(String leftNodeName) {
        return !EMPTY_CHILD.equals(leftNodeName);
    }

}
