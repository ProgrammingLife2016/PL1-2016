package io.github.programminglife2016.pl1_2016.parser.metadata;

/**
 * Data structure for the annotation data.
 */
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

    /**
     * Create an annotations data structure.
     */
    public Annotation() { }

    /**
     * Set seq id of annotation.
     * @param seqId seqid of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setSeqId(String seqId) {
        this.seqId = seqId;
        return this;
    }

    /**
     * Set source of annotation.
     * @param source source of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setSource(String source) {
        this.source = source;
        return this;
    }

    /**
     * Set type of annotation.
     * @param type type of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Set start position of annotation.
     * @param start of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setStart(int start) {
        this.start = start;
        return this;
    }

    /**
     * Set end position of annotation.
     * @param end of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setEnd(int end) {
        this.end = end;
        return this;
    }

    /**
     * Set score of annotation.
     * @param score of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setScore(float score) {
        this.score = score;
        return this;
    }

    /**
     * Set score of annotation.
     * @param strand of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setStrand(String strand) {
        this.strand = strand;
        return this;
    }

    /**
     * Set phase of annotation.
     * @param phase of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setPhase(String phase) {
        this.phase = phase;
        return this;
    }

    /**
     * Set calhounclass of annotation.
     * @param calhounClass of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setCalhounClass(String calhounClass) {
        this.calhounClass = calhounClass;
        return this;
    }

    /**
     * Set name of annotation.
     * @param name of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set id of annotation.
     * @param id of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Set displayname of annotation.
     * @param displayName of the annotation.
     * @return this reference to current data structure.
     */
    public Annotation setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * @return seq id.
     */
    public String getSeqId() {
        return seqId;
    }

    /**
     * @return source of annotation.
     */
    public String getSource() {
        return source;
    }

    /**
     * @return type of annotation.
     */
    public String getType() {
        return type;
    }

    /**
     * @return get start of annotation.
     */
    public int getStart() {
        return start;
    }

    /**
     * @return Get end of annotation.
     */
    public int getEnd() {
        return end;
    }

    /**
     * @return get score of annotation.
     */
    public float getScore() {
        return score;
    }

    /**
     * @return get strand of annotation.
     */
    public String getStrand() {
        return strand;
    }

    /**
     * @return get phase of annotation.
     */
    public String getPhase() {
        return phase;
    }

    /**
     * @return get calhounclass of annotation.
     */
    public String getCalhounClass() {
        return calhounClass;
    }

    /**
     * @return get name of annotation.
     */
    public String getName() {
        return name;
    }

    /**
     * @return get id of annotation.
     */
    public String getId() {
        return id;
    }

    /**
     * @return get displayname of annotation.
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return "Annotation{"
                + "seqId='" + seqId + '\'' + ", source='" + source + '\''
                + ", type='" + type + '\'' + ", start=" + start
                + ", end=" + end + ", score=" + score
                + ", strand='" + strand + '\'' + ", phase='" + phase + '\''
                + ", calhounClass='" + calhounClass + '\'' + ", name='" + name + '\''
                + ", id='" + id + '\'' + ", displayName='" + displayName + '\'' + '}';
    }
}
