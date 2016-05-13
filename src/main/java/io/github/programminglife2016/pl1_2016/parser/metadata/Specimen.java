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
    private char capreomycin;
    private char ethambutol;
    private char ethionamide;
    private char isoniazid;
    private char kanamycin;
    private char pyrazinamide;
    private char ofloxacin;
    private char rifampin;
    private char streptomycin;
    private char spoligotype;
    private char lineage;
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
    public char getCapreomycin() {
        return capreomycin;
    }
    public char getEthambutol() {
        return ethambutol;
    }
    public char getEthionamide() {
        return ethionamide;
    }
    public char getIsoniazid() {
        return isoniazid;
    }
    public char getKanamycin() {
        return kanamycin;
    }
    public char getPyrazinamide() {
        return pyrazinamide;
    }
    public char getOfloxacin() {
        return ofloxacin;
    }
    public char getRifampin() {
        return rifampin;
    }
    public char getStreptomycin() {
        return streptomycin;
    }
    public char getSpoligotype() {
        return spoligotype;
    }
    public char getLineage() {
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
    public void setCapreomycin(char capreomycin) {
        this.capreomycin = capreomycin;
    }
    public void setEthambutol(char ethambutol) {
        this.ethambutol = ethambutol;
    }
    public void setEthionamide(char ethionamide) {
        this.ethionamide = ethionamide;
    }
    public void setIsoniazid(char isoniazid) {
        this.isoniazid = isoniazid;
    }
    public void setKanamycin(char kanamycin) {
        this.kanamycin = kanamycin;
    }
    public void setPyrazinamide(char pyrazinamide) {
        this.pyrazinamide = pyrazinamide;
    }
    public void setOfloxacin(char ofloxacin) {
        this.ofloxacin = ofloxacin;
    }
    public void setRifampin(char rifampin) {
        this.rifampin = rifampin;
    }
    public void setStreptomycin(char streptomycin) {
        this.streptomycin = streptomycin;
    }
    public void setSpoligotype(char spoligotype) {
        this.spoligotype = spoligotype;
    }
    public void setLineage(char lineage) {
        this.lineage = lineage;
    }
    public void setGdstPattern(String gdstPattern) {
        this.gdstPattern = gdstPattern;
    }
    public void setXdr(String xdr) {
        this.xdr = xdr;
    }
}
