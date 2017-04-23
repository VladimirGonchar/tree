package com.tree.parser;

import com.tree.model.Node;
import com.tree.model.Tree;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class TreeParserTest {

    /**
     * Test cases with valid data.
     */
    public static class ValidInputTests {
        static final String RESOURCES_ROOT = "com/tree/parser/TreeParserTest/validFiles";

        private Tree expectedTree;

        {
            Node root = new Node("root");
            Node left = new Node("left", root);
            root.setLeft(left);
            root.setRight(new Node("right", root));

            left.setRight(new Node("child right", left));
            left.setLeft(new Node("child left", left));

            expectedTree = new Tree(root);
        }

        @Test
        public void shouldParseFileWithMultipleLines() throws IOException {
            InputStream is = getResource(RESOURCES_ROOT + "/multiLevelTree.txt");
            assertEquals(expectedTree, TreeParser.createTree(is));
        }

        @Test
        public void shouldParseIfRootIsInLastLine() throws IOException {
            InputStream is = getResource(RESOURCES_ROOT + "/fileWithRootInTheLastLine.txt");
            assertEquals(expectedTree, TreeParser.createTree(is));
        }

    }

    /**
     * Parser should throw exception in cases which are described in files below
     */
    @RunWith(Parameterized.class)
    public static class InvalidInputTests {

        static final String RESOURCES_ROOT = "com/tree/parser/TreeParserTest/invalidFiles";

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> getParameters() {
            return Arrays.asList(new Object[][]{
                    {RESOURCES_ROOT + "/emptyFile.txt"},
                    {RESOURCES_ROOT + "/fileWithWrongLineFormat.txt"},
                    {RESOURCES_ROOT + "/fileWithLeafWhichHasSeveralParents.txt"},
                    {RESOURCES_ROOT + "/fileWithLoopedNodes.txt"},
                    {RESOURCES_ROOT + "/fileWithSeveralHeadNodes.txt"}
            });
        }

        @Parameterized.Parameter(0)
        public String filePath;


        @Test(expected = IllegalArgumentException.class)
        public void parserShouldThrowExceptionIfDataInFileAreWrong() throws IOException {
            TreeParser.createTree(getResource(filePath));
        }

    }

    private static InputStream getResource(String filePath) {
        return TreeParserTest.class.getClassLoader().getResourceAsStream(filePath);
    }
}