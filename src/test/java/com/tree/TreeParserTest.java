package com.tree;

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

        left.setLeft(new Node("child left", left));
        left.setRight(new Node("child right", left));


        Tree expectedResult = new Tree(root);
        assertEquals(expectedResult, actualResult);
    }

    private InputStreamReader getResource(String filePath) {
        return new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filePath));
    }
}