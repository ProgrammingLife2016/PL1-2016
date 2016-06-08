// CHECKSTYLE.OFF: JavadocMethod
package io.github.programminglife2016.pl1_2016.parser.metadata;

/**
 * Specimen object containing specifications of a certain genome.
 */
public class Specimen implements Subject {

    private String nameId;
    private int age;
    private boolean isMale;
    private int hivStatus;
    private String cohort;
    private String date;
    private String district;
    private String type;
    private int smear;
    private boolean singleColony;
    private String pdstPattern;
    private String capreomycin;
    private String ethambutol;
    private String ethionamide;
    private String isoniazid;
    private String kanamycin;
    private String pyrazinamide;
    private String ofloxacin;
    private String rifampin;
    private String streptomycin;
    private String spoligotype;
    private String lineage;
    private String gdstPattern;
    private String xdr;

    public Specimen() { }
    public final String getNameId() {
        return nameId;
    }
    public final int getAge() {
        return age;
    }
    public final boolean isMale() {
        return isMale;
    }
    public final int getHivStatus() {
        return hivStatus;
    }
    public final String getCohort() {
        return cohort;
    }
    public final String getDate() {
        return date;
    }
    public final String getDistrict() {
        return district;
    }
    public final String getType() {
        return type;
    }
    public final int getSmear() {
        return smear;
    }
    public final boolean isSingleColony() {
        return singleColony;
    }
    public final String getPdstpattern() {
        return pdstPattern;
    }
    public final String getCapreomycin() {
        return capreomycin;
    }
    public final String getEthambutol() {
        return ethambutol;
    }
    public final String getEthionamide() {
        return ethionamide;
    }
    public final String getIsoniazid() {
        return isoniazid;
    }
    public final String getKanamycin() {
        return kanamycin;
    }
    public final String getPyrazinamide() {
        return pyrazinamide;
    }
    public final String getOfloxacin() {
        return ofloxacin;
    }
    public final String getRifampin() {
        return rifampin;
    }
    public final String getStreptomycin() {
        return streptomycin;
    }
    public final String getSpoligotype() {
        return spoligotype;
    }
    public final String getLineage() {
        return lineage;
    }
    public final String getGdstPattern() {
        return gdstPattern;
    }
    public final String getXdr() {
        return xdr;
    }
    public final Specimen setNameId(String nameId) {
        this.nameId = nameId;
        return this;
    }
    public final Specimen setAge(int age) {
        this.age = age;
        return this;
    }
    public final Specimen setMale(boolean male) {
        isMale = male;
        return this;
    }
    public final Specimen setHivStatus(int hivStatus) {
        this.hivStatus = hivStatus;
        return this;
    }
    public final Specimen setCohort(String cohort) {
        this.cohort = cohort;
        return this;
    }
    public final Specimen setDate(String date) {
        this.date = date;
        return this;
    }
    public final Specimen setDistrict(String district) {
        this.district = district;
        return this;
    }
    public final Specimen setType(String type) {
        this.type = type;
        return this;
    }
    public final Specimen setSmear(int smear) {
        this.smear = smear;
        return this;
    }
    public final Specimen setSingleColony(boolean singleColony) {
        this.singleColony = singleColony;
        return this;
    }
    public final Specimen setPdstpattern(String pdstPattern) {
        this.pdstPattern = pdstPattern;
        return this;
    }
    public final Specimen setCapreomycin(String capreomycin) {
        this.capreomycin = capreomycin;
        return this;
    }
    public final Specimen setEthambutol(String ethambutol) {
        this.ethambutol = ethambutol;
        return this;
    }
    public final Specimen setEthionamide(String ethionamide) {
        this.ethionamide = ethionamide;
        return this;
    }
    public final Specimen setIsoniazid(String isoniazid) {
        this.isoniazid = isoniazid;
        return this;
    }
    public final Specimen setKanamycin(String kanamycin) {
        this.kanamycin = kanamycin;
        return this;
    }
    public final Specimen setPyrazinamide(String pyrazinamide) {
        this.pyrazinamide = pyrazinamide;
        return this;
    }
    public final Specimen setOfloxacin(String ofloxacin) {
        this.ofloxacin = ofloxacin;
        return this;
    }
    public final Specimen setRifampin(String rifampin) {
        this.rifampin = rifampin;
        return this;
    }
    public final Specimen setStreptomycin(String streptomycin) {
        this.streptomycin = streptomycin;
        return this;
    }
    public final Specimen setSpoligotype(String spoligotype) {
        this.spoligotype = spoligotype;
        return this;
    }
    public final Specimen setLineage(String lineage) {
        this.lineage = lineage;
        return this;
    }
    public final Specimen setGdstPattern(String gdstPattern) {
        this.gdstPattern = gdstPattern;
        return this;
    }
    public final Specimen setXdr(String xdr) {
        this.xdr = xdr;
        return this;
    }
}
