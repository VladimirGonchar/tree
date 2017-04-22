package com.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * All incoming node names are divided into two types:
 * - head - node which can have child nodes (leafs) and is a root candidate for a tree;
 * - leaf - child node of a head node. Leaf can have another child nodes, but can't be a root candidate;
 * <p>
 * Rules:
 * 1) Any new created head node becomes candidate for a root of a tree.
 * 2) If leaf is created from parsed head (already presents in {@link TreeBuilder#parsedHeads}),
 * then this node is removed from root candidates.
 * 3) All members of {@link TreeBuilder#parsedHeads} and {@link TreeBuilder#parsedLeafs} should be unique,
 * class will throw exception in case adding already existent head node of leaf node accordingly
 */
public class TreeBuilder {

    private Map<String, Node> parsedHeads = new HashMap<>();
    private Map<String, Node> parsedLeafs = new HashMap<>();
    private Map<String, Node> rootCandidates = new HashMap<>();

    /**
     * Find root of parsed nodes and create tree from it.
     *
     * @return - tree represented by single root candidate
     * @throws IllegalArgumentException - if there is zero or more then one root candidate.
     *                                  What means that tree has loops or several root candidates.
     */
    public Tree build() throws IllegalArgumentException {
        if (rootCandidates.size() == 1) {
            return new Tree(rootCandidates.entrySet().iterator().next().getValue());
        } else if (rootCandidates.size() == 0) {
            throw new IllegalArgumentException("Doesn't have roots (looping)");
        } else {
            throw new IllegalArgumentException("Has several roots");
        }
    }

    /**
     * Add new node with left and right leafs.
     * Or add leafs to already passed leaf node ({@param headName} was passed as leaf previously).
     *
     * @param headName      - name of head node
     * @param leftLeafName  - optional name of left leaf
     * @param rightLeafName - optional name of right leaf
     * @throws IllegalArgumentException - if such head or leaf already mentioned in builder
     */
    public TreeBuilder addNode(String headName, String leftLeafName, String rightLeafName) throws IllegalArgumentException {
        Node headNode = parseHead(headName);

        if (leftLeafName != null) {
            Node leftLeaf = parseLeaf(leftLeafName, headNode);
            headNode.setLeft(leftLeaf);
        }

        if (rightLeafName != null) {
            Node rightLeaf = parseLeaf(rightLeafName, headNode);
            headNode.setRight(rightLeaf);
        }
        return this;
    }

    private Node parseHead(String headNodeName) {
        if (parsedHeads.containsKey(headNodeName)) {
            throw new IllegalArgumentException("Node " + headNodeName + " is duplicated");
        }

        return parsedLeafs.containsKey(headNodeName)
                ? this.adaptLeafToHeadNode(headNodeName)
                : this.createNewHeadNode(headNodeName);
    }

    private Node parseLeaf(String leafName, Node headNode) {
        if (parsedLeafs.containsKey(leafName)) {
            throw new IllegalArgumentException("Node " + leafName + " has several parents");
        }

        return parsedHeads.containsKey(leafName)
                ? adaptHeadNodeToLeaf(leafName, headNode)
                : createNewLeaf(leafName, headNode);

    }

    private Node createNewHeadNode(String headNodeName) {
        Node newNode = new Node(headNodeName);
        parsedHeads.put(newNode.getName(), newNode);
        rootCandidates.put(newNode.getName(), newNode);
        return newNode;
    }

    private Node getExistingHeadNode(String nodeName) {
        return parsedHeads.get(nodeName);
    }

    private Node createNewLeaf(String name, Node headNode) {
        Node newNode = new Node(name, headNode);
        parsedLeafs.put(newNode.getName(), newNode);
        return newNode;
    }

    private Node adaptLeafToHeadNode(String nodeName) {
        Node parsedLeaf = parsedLeafs.get(nodeName);
        rootCandidates.remove(nodeName);
        parsedHeads.put(nodeName, parsedLeaf);
        return parsedLeafs.get(nodeName);
    }

    private Node adaptHeadNodeToLeaf(String leafName, Node headNode) {
        Node node = getExistingHeadNode(leafName);
        node.setParent(headNode);
        parsedLeafs.put(node.getName(), node);
        rootCandidates.remove(node.getName(), node);
        return node;
    }
}
