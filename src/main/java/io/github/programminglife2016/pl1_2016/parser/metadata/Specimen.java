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
    public final void setNameId(String nameId) {
        this.nameId = nameId;
    }
    public final void setAge(int age) {
        this.age = age;
    }
    public final void setMale(boolean male) {
        isMale = male;
    }
    public final void setHivStatus(int hivStatus) {
        this.hivStatus = hivStatus;
    }
    public final void setCohort(String cohort) {
        this.cohort = cohort;
    }
    public final void setDate(String date) {
        this.date = date;
    }
    public final void setDistrict(String district) {
        this.district = district;
    }
    public final void setType(String type) {
        this.type = type;
    }
    public final void setSmear(int smear) {
        this.smear = smear;
    }
    public final void setSingleColony(boolean singleColony) {
        this.singleColony = singleColony;
    }
    public final void setPdstpattern(String pdstPattern) {
        this.pdstPattern = pdstPattern;
    }
    public final void setCapreomycin(String capreomycin) {
        this.capreomycin = capreomycin;
    }
    public final void setEthambutol(String ethambutol) {
        this.ethambutol = ethambutol;
    }
    public final void setEthionamide(String ethionamide) {
        this.ethionamide = ethionamide;
    }
    public final void setIsoniazid(String isoniazid) {
        this.isoniazid = isoniazid;
    }
    public final void setKanamycin(String kanamycin) {
        this.kanamycin = kanamycin;
    }
    public final void setPyrazinamide(String pyrazinamide) {
        this.pyrazinamide = pyrazinamide;
    }
    public final void setOfloxacin(String ofloxacin) {
        this.ofloxacin = ofloxacin;
    }
    public final void setRifampin(String rifampin) {
        this.rifampin = rifampin;
    }
    public final void setStreptomycin(String streptomycin) {
        this.streptomycin = streptomycin;
    }
    public final void setSpoligotype(String spoligotype) {
        this.spoligotype = spoligotype;
    }
    public final void setLineage(String lineage) {
        this.lineage = lineage;
    }
    public final void setGdstPattern(String gdstPattern) {
        this.gdstPattern = gdstPattern;
    }
    public final void setXdr(String xdr) {
        this.xdr = xdr;
    }
}
