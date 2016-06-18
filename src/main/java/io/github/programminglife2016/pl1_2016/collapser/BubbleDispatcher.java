package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.ObjectSerializer;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Takes apart collapsed layered bubbles and
 * uncollapses most important parts depending on threshold value.
 *
 * @author Kamran Tadzjibov
 */
public class BubbleDispatcher {

    private static String BUBBLES_SERIAL =
            "src/main/resources/objects/%s/bubbles-organized.ser";
    private static String LOWEST_LEVEL_SERIAL =
            "src/main/resources/objects/%s/lowestLevel-orgi.ser";
    public static final int ALIGNER_THRESHOLD = 128;
    private List<Node> bubbleCollection;
    private int lastId;
    private int lowestLevel;
    private static final double TIME = 1000000000d;
    private static final int THREADS = 8;
    private ForkJoinPool forkJoinPool = new ForkJoinPool(THREADS);
    private NodeCollection originalCollection;

    private Map<String, Node> quickReference;

    /**
     * Initialize bubbles by running bubble collapser.
     *
     * @param collection of nodes
     * @param dataset segment.
     */
    public BubbleDispatcher(NodeCollection collection, String dataset) {
        BUBBLES_SERIAL = String.format(BUBBLES_SERIAL, dataset);
        LOWEST_LEVEL_SERIAL = String.format(LOWEST_LEVEL_SERIAL, dataset);
        if (checkIfSerialExists()) {
            initBubblesWithSerial();
        } else {
            BubbleCollapser collapser = new BubbleCollapser(collection);
            collapser.collapseBubbles();
            this.bubbleCollection = collapser.getBubbles();
            this.lowestLevel = collapser.getLowestLevel();
            initDispatcher();
            lastId = bubbleCollection.stream().max((b1, b2) ->
                Integer.compare(b1.getId(), b2.getId())).get().getId();
            serializeBubbleCollection();
        }
        originalCollection = collection;
    }

    /**
     * Initialize a bubble dispatcher instance.
     * @param collection collection of bubbles.
     */
    public BubbleDispatcher(NodeCollection collection) {
        BubbleCollapser collapser = new BubbleCollapser(collection);
        collapser.collapseBubbles();
        this.bubbleCollection = collapser.getBubbles();
        originalCollection = collection;
        lowestLevel = collapser.getLowestLevel();
        initDispatcher();

        lastId = bubbleCollection.stream().max((b1, b2) ->
                Integer.compare(b1.getId(), b2.getId())).get().getId();
    }

    private boolean checkIfSerialExists() {
        File file = new File(BUBBLES_SERIAL);
        return file.exists();
    }

    private void initBubblesWithSerial() {
        ObjectSerializer ser = new ObjectSerializer();
        try {
            this.bubbleCollection = (List<Node>) ser.getSerializedItem(BUBBLES_SERIAL);
            this.lowestLevel = (int) ser.getSerializedItem(LOWEST_LEVEL_SERIAL);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void serializeBubbleCollection() {
        createNewFiles();
        ObjectSerializer ser = new ObjectSerializer();
        try {
            ser.serializeItem(this.bubbleCollection, BUBBLES_SERIAL);
            System.out.println("REACHED");
            ser.serializeItem(this.lowestLevel, LOWEST_LEVEL_SERIAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewFiles() {
        File bubblefile = new File(BUBBLES_SERIAL);
        File keyfile = new File(LOWEST_LEVEL_SERIAL);
        try {
            if (!(bubblefile.createNewFile() && keyfile.createNewFile())) {
                System.out.println("Serialize files already exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize dispatcher by calculating real container size.
     */
    private void initDispatcher() {
        for (Node bubble : bubbleCollection) {
            bubble.setContainerSize(getBubbleSize(bubble));
        }
    }

    /**
     * Calculates real bubble size.
     * Only segments are counted to calculate size.
     *
     * @param bubble to get amount of segments that it contains
     * @return size of bubble
     */
    private int getBubbleSize(Node bubble) {
        if (bubble.getContainer().size() == 0) {
            return 1;
        } else {
            int size = 2;
            for (int i = 0; i < bubble.getContainer().size(); i++) {
                size += getBubbleSize(bubble.getContainer().get(i));
            }
            return size;
        }
    }

    /**
     * Get graph where each bubble contains not more segments than given threshold.
     *
     * @param threshold value to filter the graphs bubbles
     * @param onlyGivenThreshold whether this method should return bubbles of a specific threshold.
     * @return graph with thresholded bubbles
     */
    public NodeCollection getThresholdedBubbles(int threshold, boolean onlyGivenThreshold) {
        System.gc();
        System.out.println("Started filtering....");
        long startTime = System.nanoTime();
        Set<Node> filtered = filterMultithreaded(threshold, onlyGivenThreshold);
        long endTime = System.nanoTime();
        System.out.println("Done filtering. time: " + ((endTime - startTime) / TIME) + " s.");
        startTime = System.nanoTime();
        findNewLinks(filtered);
        endTime = System.nanoTime();
        System.out.println("Done relinking. time: " + ((endTime - startTime) / TIME) + " s.");

        addBacklinks(filtered);
//        getAllParents();
//        if (threshold >= ALIGNER_THRESHOLD) {
            BubbleAligner aligner = new BubbleAligner(filtered);
            Collection<Node> temp = aligner.align();
            return listAsNodeCollection(temp); //aggregateLines();
//        }
//        return listAsNodeCollection(filtered); //aggregateLines();
//        return aggregateLines(listAsNodeCollection(filtered));
    }

    private void addBacklinks(Collection<Node> nodeCollection) {
        nodeCollection.forEach(x -> x.getBackLinks().clear());
        for (Node node : nodeCollection) {
            for (Node link : node.getLinks()) {
                link.getBackLinks().add(node);
            }
        }
    }

    private NodeCollection aggregateLines(NodeCollection nodeCollection) {
        List<Node> kowed = new ArrayList<>();
        nodeCollection.values().stream()
                      .filter(node -> node.getLinks().size() == 1).forEach(node -> {
            boolean repeat = true;
            while (repeat) {
                repeat = false;
                Node link = node.getLinks().iterator().next();
                if (link.getBackLinks().size() == 1 && link.getLinks().size() == 1) {
                    repeat = true;
                    kowed.add(link);
                    node.getLinks().clear();
                    node.getLinks().add(link.getLinks().iterator().next());
                }
            }
        });
        kowed.stream().map(Node::getId).forEach(nodeCollection::remove);
        return nodeCollection;
    }

    /**
     * Run multithreaded bubble filtering based on threshold value.
     *
     * @param threshold amount of segments per bubble which is used to filter bubbles
     * @param onlyGivenThreshold whether this method should return bubbles of a specific threshold.
     * @return filtered set of bubbles
     */
    public Set<Node> filterMultithreaded(int threshold, boolean onlyGivenThreshold) {
        Set<Node> filtered = Collections.synchronizedSet(new HashSet<>());
        try {
            forkJoinPool.submit(() -> {
                filtered.addAll(filterBubbles(threshold, onlyGivenThreshold));
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filtered;
    }

    /**
     * Get unlinked bubbles by given threshold.
     *
     * @param threshold value to filter the graphs bubbles
     * @return set of wrong linked in current context bubbles which are filtered on threshold value
     */
    private Set<Node> filterBubbles(int threshold, boolean onlyGivenThreshold) {
        createQuickRefForFiltering();
        Set<Integer> containers = Collections.synchronizedSet(new HashSet<>()); //new ArrayList<>();
        Set<Node> filtered = Collections.synchronizedSet(new HashSet<>());
        for (int i = 1; i < lowestLevel; i++) {
            Set<Node> tempFiltered = getFilteredNodes(containers,
                    i,
                    filtered,
                    threshold,
                    onlyGivenThreshold);
            containers.addAll(
                    tempFiltered.parallelStream().map(Node::getId).collect(Collectors.toList())
            );
            filtered.addAll(tempFiltered);
        }
        return filtered;
    }

    /**
     * Get all needed bubbles from current level by given threshold
     *
     * @param containers         list of bubble ids which are directly or
     *                           indirectly in the set of filtered bubbles
     * @param currentLevel       current bubbles level
     * @param filtered           set of already filtered nodes
     * @param threshold          value to filter the graphs bubbles
     * @param onlyGivenThreshold if true collapse only bubbles with threshold amount of segments
     * @return filtered bubbles by threshold for given level
     */
    private Set<Node> getFilteredNodes(Set<Integer> containers,
                                       int currentLevel,
                                       Set<Node> filtered,
                                       int threshold,
                                       boolean onlyGivenThreshold) {
        Set<Node> tempFiltered = Collections.synchronizedSet(new HashSet<>());
        List<Node> currLevelBubbles = bubbleCollection.parallelStream()
                .filter(x -> x.getZoomLevel() == currentLevel)
                .collect(Collectors.toList());
        currLevelBubbles.forEach(x -> {
            if (filtered.parallelStream().filter(b -> b.getEndNode().equals(x)).count() == 0
                    && x.getZoomLevel() == currentLevel
                    && !containers.contains(x.getContainerId())) {
                boolean compareMethod;
                if (onlyGivenThreshold) {
                    compareMethod = (x.getContainerSize() == threshold
                            || x.getContainerSize() <= 1);
                } else {
                    compareMethod = (x.getContainerSize() <= threshold);
                }
                if (compareMethod) {
                    tempFiltered.removeAll(filtered.stream()
                            .filter(b -> b.getZoomLevel() == -1 && (b.getStartNode()
                            .getId() == x.getStartNode().getId()
                                    || b.getEndNode().getId() == x.getEndNode().getId())
                    ).collect(Collectors.toList()));
                    tempFiltered.removeIf(b -> b.getZoomLevel() == -1
                            && (b.getStartNode().getId() == x.getStartNode().getId()
                            || b.getEndNode().getId() == x.getEndNode().getId()));
                    tempFiltered.add(x);
                } else if (!x.getStartNode().isBubble()) {
                    tempFiltered.addAll(replaceInside(x));
                }
            }
            if (containers.contains(x.getContainerId())) {
                containers.add(x.getId());
            }
        });
        return tempFiltered;
    }

    /**
     * Replace old links by the found links for each bubble in given collection.
     *
     * @param filteredBubbles collection of by threshold filtered bubbles
     */
    private void findNewLinks(Collection<Node> filteredBubbles) {
        createQuickRefForRelinking();
        filteredBubbles.parallelStream().forEach(bubble -> {
            Node primitiveEnd = getPrimitiveEnd(bubble);
            Node prospectiveLink = getExistingAncestor(primitiveEnd,
                    filteredBubbles,
                    (n1, n2) -> n1.getStartNode()
                    .equals(n2));
            bubble.getLinks().clear();
            if (prospectiveLink != null && !prospectiveLink.equals(bubble)) {
                bubble.getLinks().add(prospectiveLink);
            } else {
                for (Node primLink : primitiveEnd.getLinks()) {
                    Node found = getExistingAncestor(primLink,
                            filteredBubbles,
                            (n1, n2) -> n1.getId() == n2
                            .getContainerId());
                    if (found != null) {
                        bubble.getLinks().add(found);
                    } else {
                        System.err.println("Verkeerd connected node: " + bubble);
                    }
                }
            }
        });
    }

    /**
     * Get simple leaf segment of the given end node.
     *
     * @param node Bubble to get end segment
     * @return end segment of given bubble
     */
    private Node getPrimitiveEnd(Node node) {
        if (node.getEndNode().isBubble()) {
            return getPrimitiveEnd(node.getEndNode());
        }
        return node.getEndNode();
    }

    /**
     * Get highest existing ancestor which exist in filtered bubbles.
     *
     * @param node            to find its ancestor
     * @param filteredBubbles collection of by threshold filtered bubbles
     * @param equals          equals function to filter the ancestors
     * @return highest existing ancestor which exist in filtered bubbles
     */
    private Node getExistingAncestor(Node node,
                                     Collection<Node> filteredBubbles,
                                     BiFunction<Node, Node, Boolean>
            equals) {
        if (filteredBubbles.contains(node)) {
            Optional<Node> parent = filteredBubbles.parallelStream()
                    .filter(x -> equals.apply(x, node))
                    .findAny();
            if (parent.isPresent()) {
                return parent.get();
            }
            return node;
        }
        if (quickReference.containsKey(String.valueOf(node.getContainerId()))) {
            return getExistingAncestor(quickReference.get(String.valueOf(node.getContainerId())),
                    filteredBubbles,
                    equals);
        }
        return null;
    }

    /**
     * Convert collection of nodes to NodeCollection
     *
     * @param res collection of nodes
     * @return NodeCollection of given nodes
     */
    private NodeCollection listAsNodeCollection(Collection<Node> res) {
        NodeCollection collection = new NodeMap();
        for (Node node : res) {
            collection.put(node.getId(), node);
        }
        return collection;
    }

    private Set<Node> replaceInside(Node bubble) {
        Set<Node> newBubbles = new HashSet<>();
        newBubbles.add(initNewBubble(bubble.getStartNode(), bubble.getId()));
        newBubbles.add(initNewBubble(bubble.getEndNode(), bubble.getId()));
        for (Node n : bubble.getContainer()) {
            if (!n.isBubble()) {
                newBubbles.add(initNewBubble(n, bubble.getId()));
            }
            else {
                newBubbles.add(n);
            }
        }
        return newBubbles;
    }

    private Bubble initNewBubble(Node node, int contId) {
        String key = getNodeKeyForFiltering(node);
        if (quickReference.containsKey(key)) {
            return (Bubble) quickReference.get(key);
        }
        lastId++;
        Bubble newBubble = new Bubble(lastId, contId, (Segment) node);
        bubbleCollection.add(newBubble);
        quickReference.put(getNodeKeyForFiltering(newBubble), newBubble);
        return newBubble;
    }


    private void createQuickRefForFiltering() {
        quickReference = Collections.synchronizedMap(new HashMap<>(bubbleCollection.size()));
        for (Node n : bubbleCollection) {
            quickReference.put(getNodeKeyForFiltering(n), n);
        }
    }

    private void createQuickRefForRelinking() {
        quickReference = Collections.synchronizedMap(new HashMap<>(bubbleCollection.size()));
        for (Node n : bubbleCollection) {
            quickReference.put(String.valueOf(n.getId()), n);
        }
    }

    private String getNodeKeyForFiltering(Node node) {
        return node.getStartNode().getId() + "_" + node.getEndNode().getId();
    }

    /**
     * Get all parents
     */
    public void getAllParents() {
        originalCollection.values().forEach(this::getAllParentsOfSegment); //parallelStream().
    }

    /**
     * Get parents of a specific segment.
     * @param node node of which the parents are needed.
     * @return return string array of parents.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public String[] getAllParentsOfSegment(Node node) {
        Node currNode = node;
        Node parent;
        StringBuilder startNodeIn = new StringBuilder();
        StringBuilder endNodeIn = new StringBuilder();
        StringBuilder containsIn = new StringBuilder();
        String formattedId;
        while (currNode.getContainerId() > 0) {
            parent = quickReference.get(String.valueOf(currNode.getContainerId()));
            formattedId = "/" + parent.getId() + "/";
            if (parent.getStartNode().getId() == currNode.getId()) {
                startNodeIn.append(formattedId);
            } else if (parent.getEndNode().getId() == currNode.getId()) {
                endNodeIn.append(formattedId);
            } else {
                containsIn.append(formattedId);
            }
            currNode = parent;
        }
        System.out.println(startNodeIn);
        String[] ret = buildStringArray(node, startNodeIn.toString(), endNodeIn.toString(), containsIn.toString());
        return ret;
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private String[] buildStringArray(Node node,
                                      String startNodeIn,
                                      String endNodeIn,
                                      String containsIn) {
        String[] ret = new String[6];
        ret[0] = Integer.toString(node.getId());
        ret[1] = startNodeIn.length() != 0 ? startNodeIn.toString() : "NULL";
        ret[2] = endNodeIn.length() != 0 ? endNodeIn.toString() : "NULL";
        ret[3] = containsIn.length() != 0 ? containsIn.toString() : "NULL";
        ret[4] = node.getData();
        ret[5] = node.getGenomes().toString();
        return ret;
    }

    /**
     * Return original collection.
     * @return original collection.
     */
    public NodeCollection getOriginalCollection() {
        return originalCollection;
    }
}