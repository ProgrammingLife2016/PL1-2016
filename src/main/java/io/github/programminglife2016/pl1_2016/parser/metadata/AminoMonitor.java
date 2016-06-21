package io.github.programminglife2016.pl1_2016.parser.metadata;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kamran Tadzjibov on 21.06.2016.
 */
public class AminoMonitor {

    Map<Integer, String> segmentAmino;

    public void getMutatedAminos(List<Node> bubbles) {
        segmentAmino = new HashMap<>();
        Map<String, String> acidsTable = initAcidsTable();
        String startData, endData;
        String aminoName;
        for (Node bubble : bubbles) {
            if (bubble.getContainerSize() == 4
                    || bubble.getContainerSize() ==3) {// && !bubble.getEndNode().isBubble()) {
                startData = getDeepStartData(bubble.getStartNode());
                endData = getDeepEndData(bubble.getEndNode());
                for (Node innerNode : bubble.getContainer()) {
//                    if (innerNode.getLinks().contains(bubble.getEndNode())) {
                        aminoName = getAminoByBase(startData, getDeepStartData(innerNode), endData, acidsTable);
  //                  System.out.println(bubble.getId());
                        if (aminoName != null) {
                            segmentAmino.put(innerNode.getId(), aminoName);
                        }
//                    }
                }
            }
        }
        int xbreak = 0;
    }

    private String getDeepEndData(Node node) {
        if (node.isBubble()) {
            return getDeepEndData(node.getEndNode());
        }
        return node.getData();
    }

    private String getDeepStartData(Node node) {
        if (node.isBubble()) {
            return getDeepEndData(node.getStartNode());
        }
        return node.getData();
    }

    private String getAminoByBase(String bases1,
                                  String bases2,
                                  String bases3,
                                  Map<String, String> acidsTable) {
        String sub1 = bases1.substring(Math.max(bases1.length() - 2, 0))
                      + bases2.substring(0, 1);
        if (acidsTable.containsKey(sub1)) {
            return acidsTable.get(sub1);
        }
        if (bases2.length() == 1) {
            String sub2 = bases1.substring(Math.max(bases1.length() - 1, 0))
                          + bases2 + bases3.substring(0, 1);
            if (acidsTable.containsKey(sub2)) {
                return acidsTable.get(sub2);
            }
        }
        String sub3 = bases2.substring(Math.max(bases2.length() - 1, 0))
                      + bases3.substring(0, 2);
        if (acidsTable.containsKey(sub3)) {
            return acidsTable.get(sub3);
        }
        return null;
    }

    private HashMap<String, String> initAcidsTable() {
        return new HashMap<String, String>() {{
            put("ATT","Isoleucine");
            put("ATC","Isoleucine");
            put("ATA","Isoleucine");
            put("CTT","Leucine");
            put("CTC","Leucine");
            put("CTA","Leucine");
            put("CTG","Leucine");
            put("TTA","Leucine");
            put("TTG","Leucine");
            put("GTT","Valine");
            put("GTC","Valine");
            put("GTA","Valine");
            put("GTG","Valine");
            put("TTT","Phenylalanine");
            put("TTC","Phenylalanine");
            put("ATG","Methionine");
            put("TGT","Cysteine");
            put("TGC","Cysteine");
            put("GCT","Alanine");
            put("GCC","Alanine");
            put("GCA","Alanine");
            put("GCG","Alanine");
            put("GGT","Glycine");
            put("GGC","Glycine");
            put("GGA","Glycine");
            put("GGG","Glycine");
            put("CCT","Proline");
            put("CCC","Proline");
            put("CCA","Proline");
            put("CCG","Proline");
            put("ACT","Threonine");
            put("ACC","Threonine");
            put("ACA","Threonine");
            put("ACG","Threonine");
            put("TCT","Serine");
            put("TCC","Serine");
            put("TCA","Serine");
            put("TCG","Serine");
            put("AGT","Serine");
            put("AGC","Serine");
            put("TAT","Tyrosine");
            put("TAC","Tyrosine");
            put("TGG","Tryptophan");
            put("CAA","Glutamine");
            put("CAG","Glutamine");
            put("AAT","Asparagine");
            put("AAC","Asparagine");
            put("CAT","Histidine");
            put("CAC","Histidine");
            put("GAA","Glutamic acid");
            put("GAG","Glutamic acid");
            put("GAT","Aspartic acid");
            put("GAC","Aspartic acid");
            put("AAA","Lysine");
            put("AAG","Lysine");
            put("CGT","Arginine");
            put("CGC","Arginine");
            put("CGA","Arginine");
            put("CGG","Arginine");
            put("AGA","Arginine");
            put("AGG","Arginine");
            put("TAA","Stop codons");
            put("TAG","Stop codons");
            put("TGA","Stop codons");
        }};
    }

}
