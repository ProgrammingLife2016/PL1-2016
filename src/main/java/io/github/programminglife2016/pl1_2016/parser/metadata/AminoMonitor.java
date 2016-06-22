package io.github.programminglife2016.pl1_2016.parser.metadata;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class to find mutated amino acids in reference genome MT_H37RV_BRD_V5
 */
public class AminoMonitor {


    public Map<Integer, String> getMutatedAminos(List<Node> bubbles) {
        String startData, endData;
        String aminoName;
        HashMap<String, String> acidsTable = initAcidsTable();
        HashMap<Integer, String> startIdToResult = new HashMap<>();
        int endMT;
        for (Node bubble : bubbles) {
            if (bubble.getStartNode().getRangePerGenome().containsKey("MT_H37RV_BRD_V5.ref")
                && (bubble.getContainerSize() == 4
                    || bubble.getContainerSize() == 3)) {
                endMT = bubble.getStartNode().getRangePerGenome().get("MT_H37RV_BRD_V5.ref").getEnd();
                startData = getDeepStartData(bubble.getStartNode());
                endData = getDeepEndData(bubble.getEndNode());
                if (bubble.getContainerSize() == 3) {
                    addMutationToMap(getAminoByBaseInDel(startData, endData, acidsTable, endMT),
                            startIdToResult, bubble);
                }
                for (Node innerNode : bubble.getContainer()) {
                    aminoName = getAminoByBase(startData, getDeepStartData(innerNode), endData, acidsTable, endMT);
                    if (aminoName != null) {
                        addMutationToMap(aminoName, startIdToResult, bubble);
                    }
                }
            }
        }
        return startIdToResult;
    }

    private void addMutationToMap(String aminoName, HashMap<Integer, String> startIdToResult, Node bubble) {
        if(!startIdToResult.containsKey(bubble.getStartNode().getId())) {
            startIdToResult.put(bubble.getStartNode().getId(), aminoName);
        } else {
            startIdToResult.put(bubble.getStartNode().getId(), startIdToResult.get(bubble.getStartNode().getId()) + " -> " + aminoName.split(": ")[1]);
        }
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
                                  Map<String, String> acidsTable, int endMT) {
        String sub1 = bases1.substring(Math.max(bases1.length() - 2, 0))
                      + bases2.substring(0, 1);
        if (acidsTable.containsKey(sub1)) {
            return (endMT - 2) + ": " + acidsTable.get(sub1);
        }
        if (bases2.length() == 1) {
            String sub2 = bases1.substring(Math.max(bases1.length() - 1, 0))
                          + bases2 + bases3.substring(0, 1);
            if (acidsTable.containsKey(sub2)) {
                return (endMT - 1) + ": " + acidsTable.get(sub2);
            }
        }
        String sub3 = bases2.substring(Math.max(bases2.length() - 1, 0))
                      + bases3.substring(0, 2);
        if (acidsTable.containsKey(sub3)) {
            return endMT + ": " + acidsTable.get(sub3);
        }
        return null;
    }

    private String getAminoByBaseInDel(String bases1,
                                  String bases3,
                                  Map<String, String> acidsTable, int endMT) {
        String sub1 = bases1.substring(Math.max(bases1.length() - 2, 0))
                      + bases3.substring(0, 1);
        if (acidsTable.containsKey(sub1)) {
            return (endMT - 2) + ": " + acidsTable.get(sub1);
        }
        String sub3 = bases1.substring(Math.max(bases1.length() - 1, 0))
                      + bases3.substring(0, 2);
        if (acidsTable.containsKey(sub3)) {
            return (endMT - 1) + ": " + acidsTable.get(sub3);
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
