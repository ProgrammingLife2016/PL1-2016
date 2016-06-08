package io.github.programminglife2016.pl1_2016.parser.nodes;

import java.util.Collection;

import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;

import java.util.List;
import java.util.Set;

/**
 * An object that represents a displayable bubble/segment.
 */
public interface Node extends Cloneable {
    /**
     * Set the x and y coordinates of the node.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    void setXY(int x, int y);

    /**
     * Get the x coordinate.
     *
     * @return the x coordinate
     */
    int getX();

    /**
     * Get the y coordinate.
     *
     * @return the y coordinate
     */
    int getY();

    /**
     * Set the data of the node.
     *
     * @param data data of the node
     */
    void setData(String data);

    /**
     * Set the file-specified column of the node.
     *
     * @param column column of the node
     */
    void setColumn(int column);

    /**
     * Add a link to another node, which results in a directed edge when displayed.
     *
     * @param node connected node
     */
    void addLink(Node node);

    /**
     * Add a predecessor node.
     *
     * @param node connected node
     */
    void addBackLink(Node node);

    /**
     * Get the id of the node.
     *
     * @return id of the node
     */
    int getId();

    /**
     * Get the data of the node.
     *
     * @return data of the node
     */
    String getData();

    /**
     * Get the connected nodes of this node.
     *
     * @return links of this node
     */
    Collection<Node> getLinks();

    /**
     * Get the predecessors of this node.
     *
     * @return links of this node
     */
    Collection<Node> getBackLinks();

    /**
     * Get the column of this node.
     *
     * @return the column of this node
     */
    int getColumn();

    /**
     * Add the genomes this segment belongs to.
     *
     * @param genomes the genomes this segment belongs to
     */
    void addGenomes(Collection<Subject> genomes);

    /**
     * Get the genomes this segment belongs to.
     *
     * @return the genomes this segment belongs to
     */
    Set<String> getGenomes();

    /**
     * Get the subjects this segment belongs to.
     *
     * @return the subjects this segment belongs to
     */
    Set<Subject> getSubjects();

    /**
     * Return the id of the container the node resides in.
     *
     * @return id of the container.
     */
    int getContainerId();

    /**
     * Set the id of the container the node resides in.
     *
     * @param containerId id of the bubble in which current node is located
     */
    void setContainerId(int containerId);


    /**
     * Return the zoomlevel the node resides in.
     *
     * @return the depth of the level.
     */
    int getZoomLevel();

    /**
     * Return whether the node is a bubble.
     *
     * @return true if it is a bubble.
     */
    Boolean isBubble();

    /**
     * Get the nodes if the nodes has a container.
     *
     * @return List of the nodes the node contains.
     */
    List<Node> getContainer();

    /**
     * Returns the size of the container.
     *
     * @return size of the container.
     */
    int getContainerSize();

    /**
     * Sets the set size of the bubble.
     *
     * @param size size of the bubble.
     */
    void setContainerSize(int size);

    /**
     * Set the zoom level of the node
     * @param level zoomlevel.
     */
    void setZoomLevel(int level);

    /**
     * Make a shallow clone of this node. Only the links are cloned one level more.
     *
     * @return the cloned node.
     */
    Node clone();

    /**
     * Get the startnode of the the bubble (if any).
     * @return startnode of the bubble
     */
    Node getStartNode();

    /**
     * Get the endnode of the bubble (if any).
     * @return endnode of the bubble.
     */
    Node getEndNode();

    /**
     * Sets the end node of the bubble.
     * @param node new end node of the bubble.
     */
    void setEndNode(Node node);

    /**
     * Sets the start node of the bubble.
     * @param node new start node of the bubble.
     */
    void setStartNode(Node node);
}
