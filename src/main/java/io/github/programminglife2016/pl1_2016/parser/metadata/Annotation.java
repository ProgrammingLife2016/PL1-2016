package io.github.programminglife2016.pl1_2016.parser.metadata;

public class Annotation {

    private String seqId;
    private String source;
    private String type;
    private int start;
    private int end;
    private float score;
    private String strand;
    private String phase;
    private String calhounClass;
    private String name;
    private String id;
    private String displayName;

    public Annotation() {}

    public Annotation setSeqId(String seqId) {
        this.seqId = seqId;
        return this;
    }

    public Annotation setSource(String source) {
        this.source = source;
        return this;
    }

    public Annotation setType(String type) {
        this.type = type;
        return this;
    }

    public Annotation setStart(int start) {
        this.start = start;
        return this;
    }

    public Annotation setEnd(int end) {
        this.end = end;
        return this;
    }

    public Annotation setScore(float score) {
        this.score = score;
        return this;
    }

    public Annotation setStrand(String strand) {
        this.strand = strand;
        return this;
    }

    public Annotation setPhase(String phase) {
        this.phase = phase;
        return this;
    }

    public Annotation setCalhounClass(String calhounClass) {
        this.calhounClass = calhounClass;
        return this;
    }

    public Annotation setName(String name) {
        this.name = name;
        return this;
    }

    public Annotation setId(String id) {
        this.id = id;
        return this;
    }

    public Annotation setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getSeqId() { return seqId; }
    public String getSource() { return source; }
    public String getType() { return type; }
    public int getStart() { return start; }
    public int getEnd() { return end; }
    public float getScore() { return score; }
    public String getStrand() { return strand; }
    public String getPhase() { return phase; }
    public String getCalhounClass() { return calhounClass; }
    public String getName() { return name; }
    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
}
