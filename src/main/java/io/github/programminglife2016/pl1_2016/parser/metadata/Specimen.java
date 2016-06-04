// CHECKSTYLE.OFF: JavadocMethod
package io.github.programminglife2016.pl1_2016.parser.metadata;

/**
 * Specimen object containing specifications of a certain genome.
 */
public class Specimen implements Subject {

    private String nameId;
    private int age;
    private boolean _isMale;
    private int _hivStatus;
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
    public final String getNameId() {
        return nameId;
    }
    public final int getAge() {
        return age;
    }
    public final boolean is_isMale() {
        return _isMale;
    }
    public final int get_hivStatus() {
        return _hivStatus;
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
    public final char getCapreomycin() {
        return capreomycin;
    }
    public final char getEthambutol() {
        return ethambutol;
    }
    public final char getEthionamide() {
        return ethionamide;
    }
    public final char getIsoniazid() {
        return isoniazid;
    }
    public final char getKanamycin() {
        return kanamycin;
    }
    public final char getPyrazinamide() {
        return pyrazinamide;
    }
    public final char getOfloxacin() {
        return ofloxacin;
    }
    public final char getRifampin() {
        return rifampin;
    }
    public final char getStreptomycin() {
        return streptomycin;
    }
    public final char getSpoligotype() {
        return spoligotype;
    }
    public final char getLineage() {
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
    public final void set_isMale(boolean isMale) {
        this._isMale = isMale;
    }
    public final void set_hivStatus(int hivStatus) {
        this._hivStatus = hivStatus;
    }
    public final void setCohort(String cohort) {
        this.cohort = cohort;
    }
    public final void setDate(String date) {
        this. date = date;
    }
    public final void setDistrict(String district) {
        this. district = district;
    }
    public final void setType(String type) {
        this. type = type;
    }
    public final void setSmear(int smear) {
        this. smear = smear;
    }
    public final void setSingleColony(boolean singleColony) {
        this. singleColony = singleColony;
    }
    public final void setPdstpattern(String pdstPattern) {
        this. pdstPattern = pdstPattern;
    }
    public final void setCapreomycin(char capreomycin) {
        this. capreomycin = capreomycin;
    }
    public final void setEthambutol(char ethambutol) {
        this. ethambutol = ethambutol;
    }
    public final void setEthionamide(char ethionamide) {
        this. ethionamide = ethionamide;
    }
    public final void setIsoniazid(char isoniazid) {
        this. isoniazid = isoniazid;
    }
    public final void setKanamycin(char kanamycin) {
        this. kanamycin = kanamycin;
    }
    public final void setPyrazinamide(char pyrazinamide) {
        this. pyrazinamide = pyrazinamide;
    }
    public final void setOfloxacin(char ofloxacin) {
        this. ofloxacin = ofloxacin;
    }
    public final void setRifampin(char rifampin) {
        this. rifampin = rifampin;
    }
    public final void setStreptomycin(char streptomycin) {
        this. streptomycin = streptomycin;
    }
    public final void setSpoligotype(char spoligotype) {
        this. spoligotype = spoligotype;
    }
    public final void setLineage(char lineage) {
        this. lineage = lineage;
    }
    public final void setGdstPattern(String gdstPattern) {
        this. gdstPattern = gdstPattern;
    }
    public final void setXdr(String xdr) {
        this. xdr = xdr;
    }
}
