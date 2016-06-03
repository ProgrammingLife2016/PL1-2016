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
    public String getNameId() {
        return nameId;
    }
    public int getAge() {
        return age;
    }
    public boolean isMale() {
        return isMale;
    }
    public int getHivStatus() {
        return hivStatus;
    }
    public String getCohort() {
        return cohort;
    }
    public String getDate() {
        return date;
    }
    public String getDistrict() {
        return district;
    }
    public String getType() {
        return type;
    }
    public int getSmear() {
        return smear;
    }
    public boolean isSingleColony() {
        return singleColony;
    }
    public String getPdstpattern() {
        return pdstPattern;
    }
    public String getCapreomycin() {
        return capreomycin;
    }
    public String getEthambutol() {
        return ethambutol;
    }
    public String getEthionamide() {
        return ethionamide;
    }
    public String getIsoniazid() {
        return isoniazid;
    }
    public String getKanamycin() {
        return kanamycin;
    }
    public String getPyrazinamide() {
        return pyrazinamide;
    }
    public String getOfloxacin() {
        return ofloxacin;
    }
    public String getRifampin() {
        return rifampin;
    }
    public String getStreptomycin() {
        return streptomycin;
    }
    public String getSpoligotype() {
        return spoligotype;
    }
    public String getLineage() {
        return lineage;
    }
    public String getGdstPattern() {
        return gdstPattern;
    }
    public String getXdr() {
        return xdr;
    }
    public void setNameId(String nameId) {
        this.nameId = nameId;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setMale(boolean male) {
        isMale = male;
    }
    public void setHivStatus(int hivStatus) {
        this.hivStatus = hivStatus;
    }
    public void setCohort(String cohort) {
        this.cohort = cohort;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setSmear(int smear) {
        this.smear = smear;
    }
    public void setSingleColony(boolean singleColony) {
        this.singleColony = singleColony;
    }
    public void setPdstpattern(String pdstPattern) {
        this.pdstPattern = pdstPattern;
    }
    public void setCapreomycin(String capreomycin) {
        this.capreomycin = capreomycin;
    }
    public void setEthambutol(String ethambutol) {
        this.ethambutol = ethambutol;
    }
    public void setEthionamide(String ethionamide) {
        this.ethionamide = ethionamide;
    }
    public void setIsoniazid(String isoniazid) {
        this.isoniazid = isoniazid;
    }
    public void setKanamycin(String kanamycin) {
        this.kanamycin = kanamycin;
    }
    public void setPyrazinamide(String pyrazinamide) {
        this.pyrazinamide = pyrazinamide;
    }
    public void setOfloxacin(String ofloxacin) {
        this.ofloxacin = ofloxacin;
    }
    public void setRifampin(String rifampin) {
        this.rifampin = rifampin;
    }
    public void setStreptomycin(String streptomycin) {
        this.streptomycin = streptomycin;
    }
    public void setSpoligotype(String spoligotype) {
        this.spoligotype = spoligotype;
    }
    public void setLineage(String lineage) {
        this.lineage = lineage;
    }
    public void setGdstPattern(String gdstPattern) {
        this.gdstPattern = gdstPattern;
    }
    public void setXdr(String xdr) {
        this.xdr = xdr;
    }
}
