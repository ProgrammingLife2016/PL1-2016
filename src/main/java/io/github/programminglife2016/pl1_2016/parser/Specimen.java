package io.github.programminglife2016.pl1_2016.parser;

/** Specimen object containing specifications of a certain genome.
 * Created by ravishivam on 4-5-16.
 */
public class Specimen implements Subject {

    private String nameId;
    private int age;
    private boolean isMale;
    private int HIV;
    private String Cohort;
    private String Date;
    private String District;
    private String Type;
    private int Smear;
    private boolean Singlecolony;
    private String pDSTPattern;
    private char Capreomycin;
    private char EthamButol;
    private char Ethionamide;
    private char Isoniazid;
    private char Kanamycin;
    private char Pyrazinamide;
    private char Ofloxacin;
    private char Rifampin;
    private char Streptomycin;
    private char Spoligotype;
    private char Lineage;
    private String gDSTPattern;
    private String xdr;

    /**
     * Default constructor for a specimen.
     */
    public Specimen() { }

    /**
     * Getter for nameId.
     *
     * @return java.lang.String value of nameId
     */
    public String getNameId() {
        return nameId;
    }

    /**
     * Getter for age.
     *
     * @return int value of age
     */
    public int getAge() {
        return age;
    }

    /**
     * Getter for isMale.
     *
     * @return boolean value of isMale
     */
    public boolean isMale() {
        return isMale;
    }

    /**
     * Getter for HIV.
     *
     * @return int value of HIV
     */
    public int getHIV() {
        return HIV;
    }

    /**
     * Getter for Cohort.
     *
     * @return java.lang.String value of Cohort
     */
    public String getCohort() {
        return Cohort;
    }

    /**
     * Getter for Date.
     *
     * @return java.lang.String value of Date
     */
    public String getDate() {
        return Date;
    }

    /**
     * Getter for District.
     *
     * @return java.lang.String value of District
     */
    public String getDistrict() {
        return District;
    }

    /**
     * Getter for Type.
     *
     * @return java.lang.String value of Type
     */
    public String getType() {
        return Type;
    }

    /**
     * Getter for Smear.
     *
     * @return int value of Smear
     */
    public int getSmear() {
        return Smear;
    }

    /**
     * Getter for Singlecolony.
     *
     * @return boolean value of Singlecolony
     */
    public boolean isSinglecolony() {
        return Singlecolony;
    }

    /**
     * Getter for pDSTPattern.
     *
     * @return java.lang.String value of pDSTPattern
     */
    public String getpDSTPattern() {
        return pDSTPattern;
    }

    /**
     * Getter for Capreomycin.
     *
     * @return char value of Capreomycin
     */
    public char getCapreomycin() {
        return Capreomycin;
    }

    /**
     * Getter for EthamButol.
     *
     * @return char value of EthamButol
     */
    public char getEthamButol() {
        return EthamButol;
    }

    /**
     * Getter for Ethionamide.
     *
     * @return char value of Ethionamide
     */
    public char getEthionamide() {
        return Ethionamide;
    }

    /**
     * Getter for Isoniazid.
     *
     * @return char value of Isoniazid
     */
    public char getIsoniazid() {
        return Isoniazid;
    }

    /**
     * Getter for Kanamycin.
     *
     * @return char value of Kanamycin
     */
    public char getKanamycin() {
        return Kanamycin;
    }

    /**
     * Getter for Pyrazinamide.
     *
     * @return char value of Pyrazinamide
     */
    public char getPyrazinamide() {
        return Pyrazinamide;
    }

    /**
     * Getter for Ofloxacin.
     *
     * @return char value of Ofloxacin
     */
    public char getOfloxacin() {
        return Ofloxacin;
    }

    /**
     * Getter for Rifampin.
     *
     * @return char value of Rifampin
     */
    public char getRifampin() {
        return Rifampin;
    }

    /**
     * Getter for Streptomycin.
     *
     * @return char value of Streptomycin
     */
    public char getStreptomycin() {
        return Streptomycin;
    }

    /**
     * Getter for Spoligotype.
     *
     * @return char value of Spoligotype
     */
    public char getSpoligotype() {
        return Spoligotype;
    }

    /**
     * Getter for Lineage.
     *
     * @return char value of Lineage
     */
    public char getLineage() {
        return Lineage;
    }

    /**
     * Getter for gDSTPattern.
     *
     * @return java.lang.String value of gDSTPattern
     */
    public String getgDSTPattern() {
        return gDSTPattern;
    }

    /**
     * Getter for xdr.
     *
     * @return java.lang.String value of xdr
     */
    public String getXdr() {
        return xdr;
    }

    /**
     * Setter for nameId.
     *
     * @nameId field that should be assigned to nameId.
     */
    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    /**
     * Setter for age.
     *
     * @age field that should be assigned to age.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Setter for isMale.
     *
     * @male field that should be assigned to isMale.
     */
    public void setMale(boolean male) {
        isMale = male;
    }

    /**
     * Setter for HIV.
     *
     * @HIV field that should be assigned to HIV.
     */
    public void setHIV(int HIV) {
        this.HIV = HIV;
    }

    /**
     * Setter for Cohort.
     *
     * @cohort field that should be assigned to Cohort.
     */
    public void setCohort(String cohort) {
        Cohort = cohort;
    }

    /**
     * Setter for Date.
     *
     * @date field that should be assigned to Date.
     */
    public void setDate(String date) {
        Date = date;
    }

    /**
     * Setter for District.
     *
     * @district field that should be assigned to District.
     */
    public void setDistrict(String district) {
        District = district;
    }

    /**
     * Setter for Type.
     *
     * @type field that should be assigned to Type.
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     * Setter for Smear.
     *
     * @smear field that should be assigned to Smear.
     */
    public void setSmear(int smear) {
        Smear = smear;
    }

    /**
     * Setter for Singlecolony.
     *
     * @singlecolony field that should be assigned to Singlecolony.
     */
    public void setSinglecolony(boolean singlecolony) {
        Singlecolony = singlecolony;
    }

    /**
     * Setter for pDSTPattern.
     *
     * @pDSTPattern field that should be assigned to pDSTPattern.
     */
    public void setpDSTPattern(String pDSTPattern) {
        this.pDSTPattern = pDSTPattern;
    }

    /**
     * Setter for Capreomycin.
     *
     * @capreomycin field that should be assigned to Capreomycin.
     */
    public void setCapreomycin(char capreomycin) {
        Capreomycin = capreomycin;
    }

    /**
     * Setter for EthamButol.
     *
     * @ethamButol field that should be assigned to EthamButol.
     */
    public void setEthamButol(char ethamButol) {
        EthamButol = ethamButol;
    }

    /**
     * Setter for Ethionamide.
     *
     * @ethionamide field that should be assigned to Ethionamide.
     */
    public void setEthionamide(char ethionamide) {
        Ethionamide = ethionamide;
    }

    /**
     * Setter for Isoniazid.
     *
     * @isoniazid field that should be assigned to Isoniazid.
     */
    public void setIsoniazid(char isoniazid) {
        Isoniazid = isoniazid;
    }

    /**
     * Setter for Kanamycin.
     *
     * @kanamycin field that should be assigned to Kanamycin.
     */
    public void setKanamycin(char kanamycin) {
        Kanamycin = kanamycin;
    }

    /**
     * Setter for Pyrazinamide.
     *
     * @pyrazinamide field that should be assigned to Pyrazinamide.
     */
    public void setPyrazinamide(char pyrazinamide) {
        Pyrazinamide = pyrazinamide;
    }

    /**
     * Setter for Ofloxacin.
     *
     * @ofloxacin field that should be assigned to Ofloxacin.
     */
    public void setOfloxacin(char ofloxacin) {
        Ofloxacin = ofloxacin;
    }

    /**
     * Setter for Rifampin.
     *
     * @rifampin field that should be assigned to Rifampin.
     */
    public void setRifampin(char rifampin) {
        Rifampin = rifampin;
    }

    /**
     * Setter for Streptomycin.
     *
     * @streptomycin field that should be assigned to Streptomycin.
     */
    public void setStreptomycin(char streptomycin) {
        Streptomycin = streptomycin;
    }

    /**
     * Setter for Spoligotype.
     *
     * @spoligotype field that should be assigned to Spoligotype.
     */
    public void setSpoligotype(char spoligotype) {
        Spoligotype = spoligotype;
    }

    /**
     * Setter for Lineage.
     *
     * @lineage field that should be assigned to Lineage.
     */
    public void setLineage(char lineage) {
        Lineage = lineage;
    }

    /**
     * Setter for gDSTPattern.
     *
     * @gDSTPattern field that should be assigned to gDSTPattern.
     */
    public void setgDSTPattern(String gDSTPattern) {
        this.gDSTPattern = gDSTPattern;
    }

    /**
     * Setter for xdr.
     *
     * @xdr field that should be assigned to xdr.
     */
    public void setXdr(String xdr) {
        this.xdr = xdr;
    }
}
