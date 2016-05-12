package io.github.programminglife2016.pl1_2016;

import io.github.programminglife2016.pl1_2016.parser.*;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;
import io.github.programminglife2016.pl1_2016.server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Reads the input and launches the server.
 */
public final class Launcher {
    private Launcher() {
    }
    /**
     * Read the input data and starts the server on the default port.
     * @param args ignored
     * @throws IOException thrown if the port is in use.
     */
    public static void main(String[] args) throws IOException {
//        TreeNode tkk1 = new BaseTreeNode("TKK1", 0);
//        TreeNode tkk2 = new BaseTreeNode("TKK2", 0);
//        TreeNode p1n2 = new BaseTreeNode("-", 0, Arrays.asList(tkk1, tkk2), null);
//        TreeNode tkk3 = new BaseTreeNode("TKK3", 0);
//        TreeNode rootNode = new BaseTreeNode("-", 0, Arrays.asList(p1n2, tkk3), null);
//
//        Node a = new Segment(1, "A", 0);
//        Node b = new Segment(2, "B", 0);
//        Node c = new Segment(3, "C", 0);
//        Node d = new Segment(4, "D", 0);
//        Node e = new Segment(5, "E", 0);
//        Node f = new Segment(6, "F", 0);
//        Node g = new Segment(7, "G", 0);
//        Node h = new Segment(8, "H", 0);
//        Node i = new Segment(9, "I", 0);
//        Node j = new Segment(10, "J", 0);
//
//        a.setXY(0, 0);
//        b.setXY(100, 100);
//        c.setXY(100, -100);
//        d.setXY(200, 0);
//        e.setXY(300, 100);
//        f.setXY(300, -100);
//        g.setXY(400, 0);
//        h.setXY(500, 100);
//        i.setXY(500, -100);
//        j.setXY(600, 0);
//
//        a.addGenomes(Arrays.asList("TKK1", "TKK2", "TKK3"));
//        b.addGenomes(Arrays.asList("TKK1", "TKK3"));
//        c.addGenomes(Arrays.asList("TKK2"));
//        d.addGenomes(Arrays.asList("TKK1", "TKK2", "TKK3"));
//        e.addGenomes(Arrays.asList("TKK3"));
//        f.addGenomes(Arrays.asList("TKK1", "TKK2"));
//        g.addGenomes(Arrays.asList("TKK1", "TKK2", "TKK3"));
//        h.addGenomes(Arrays.asList("TKK2"));
//        i.addGenomes(Arrays.asList("TKK1", "TKK3"));
//        j.addGenomes(Arrays.asList("TKK1", "TKK2", "TKK3"));
//
//        a.addLink(b);
//        a.addLink(c);
//        b.addLink(d);
//        c.addLink(d);
//        d.addLink(e);
////        d.addLink(g); ///////////////
//        d.addLink(f);
//        e.addLink(g);
//        f.addLink(g);
//        g.addLink(h);
//        g.addLink(i);
//        h.addLink(j);
//        i.addLink(j);
//
//        b.addBackLink(a);
//        c.addBackLink(a);
//        d.addBackLink(b);
//        d.addBackLink(c);
//        e.addBackLink(d);
////        g.addBackLink(d); ///////////////
//        f.addBackLink(d);
//        g.addBackLink(e);
//        g.addBackLink(f);
//        h.addBackLink(g);
//        i.addBackLink(g);
//        j.addBackLink(h);
//        j.addBackLink(i);
//
//        NodeCollection nodeCollection = new NodeList(10);
//        nodeCollection.put(1, a);
//        nodeCollection.put(2, b);
//        nodeCollection.put(3, c);
//        nodeCollection.put(4, d);
//        nodeCollection.put(5, e);
//        nodeCollection.put(6, f);
//        nodeCollection.put(7, g);
//        nodeCollection.put(8, h);
//        nodeCollection.put(9, i);
//        nodeCollection.put(10, j);
        InputStream is = Launcher.class.getResourceAsStream("/genomes/TB10.gfa");
        NodeCollection nodeCollection = new SimpleParser().parse(is);
        InputStream nwk = Launcher.class.getResourceAsStream("/genomes/340tree.rooted.TKK.nwk");
        TreeNodeCollection treeNodeCollection = new PhyloGeneticTreeParser().parse(nwk);
        System.out.println("Done parsing");
        Server server = new RestServer(nodeCollection, treeNodeCollection.getRoot());
        server.startServer();
    }
}

