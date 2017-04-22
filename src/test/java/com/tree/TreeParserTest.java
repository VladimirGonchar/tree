package com.tree;

import com.tree.parser.TreeParser;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class TreeParserTest {

    private TreeParser parser = new TreeParser();

    @Test(expected = IllegalArgumentException.class)
    public void parserShouldThrowIllegalArgumentExceptionIfProvidedSteamIsEmpty() throws IOException {
        InputStreamReader is = getResource("com/tree/emptyFile.txt");
        parser.createTree(is);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parserShouldThrowIllegalArgumentExceptionIfAnyLineIsInWrongFormat() throws IOException {
        InputStreamReader is = getResource("com/tree/fileWithWrongLineFormat.txt");
        parser.createTree(is);
    }

    @Test
    public void parserShouldCorrectlyParseFileWithSeveralNodes() throws IOException {
        InputStreamReader is = getResource("com/tree/multiLevelTree.txt");
        Tree actualResult = parser.createTree(is);

        Node root  = new Node("root");
        Node left = new Node("left", root);
        root.setLeft(left);
        root.setRight(new Node("right", root));

        left.setRight(new Node("child right", left));

        Tree expectedResult = new Tree(root);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parserShouldThrowExceptionIfFileHasNodesWithSeveralParents() throws IOException {
        parser.createTree(getResource("com/tree/fileWithChildWhichHasSeveralParents.txt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parserShouldThrowExceptionIfFileHasSeveralHeadNodes() throws IOException {
        parser.createTree(getResource("com/tree/fileWithSeveralHeadNodes.txt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parserShouldThrowExceptionIfFileHasLoopedNodes() throws IOException {
        parser.createTree(getResource("com/tree/fileWithLoopedNodes.txt"));
    }

    private InputStreamReader getResource(String filePath) {
        return new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filePath));
    }
}