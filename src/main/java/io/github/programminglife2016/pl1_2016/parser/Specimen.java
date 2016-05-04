package io.github.programminglife2016.pl1_2016.parser;

/**
 * Created by ravishivam on 4-5-16.
 */
public class Specimen implements Data {
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

    public Specimen(String nameId, int age, boolean isMale,
                    int HIV, String cohort, String date, String district,
                    String type, int smear, boolean singlecolony, String pDSTPattern,
                    char capreomycin, char ethamButol, char ethionamide,
                    char isoniazid, char kanamycin, char pyrazinamide,
                    char ofloxacin, char rifampin, char streptomycin,
                    char spoligotype, char lineage, String gDSTPattern, String xdr) {
        this.nameId = nameId;
        this.age = age;
        this.isMale = isMale;
        this.HIV = HIV;
        Cohort = cohort;
        Date = date;
        District = district;
        Type = type;
        Smear = smear;
        Singlecolony = singlecolony;
        this.pDSTPattern = pDSTPattern;
        Capreomycin = capreomycin;
        EthamButol = ethamButol;
        Ethionamide = ethionamide;
        Isoniazid = isoniazid;
        Kanamycin = kanamycin;
        Pyrazinamide = pyrazinamide;
        Ofloxacin = ofloxacin;
        Rifampin = rifampin;
        Streptomycin = streptomycin;
        Spoligotype = spoligotype;
        Lineage = lineage;
        this.gDSTPattern = gDSTPattern;
        this.xdr = xdr;
    }

    public String getNameId() {
        return nameId;
    }

    public int getAge() {
        return age;
    }

    public boolean isMale() {
        return isMale;
    }

    public int getHIV() {
        return HIV;
    }

    public String getCohort() {
        return Cohort;
    }

    public String getDate() {
        return Date;
    }

    public String getDistrict() {
        return District;
    }

    public String getType() {
        return Type;
    }

    public int getSmear() {
        return Smear;
    }

    public boolean isSinglecolony() {
        return Singlecolony;
    }

    public String getpDSTPattern() {
        return pDSTPattern;
    }

    public char getCapreomycin() {
        return Capreomycin;
    }

    public char getEthamButol() {
        return EthamButol;
    }

    public char getEthionamide() {
        return Ethionamide;
    }

    public char getIsoniazid() {
        return Isoniazid;
    }

    public char getKanamycin() {
        return Kanamycin;
    }

    public char getPyrazinamide() {
        return Pyrazinamide;
    }

    public char getOfloxacin() {
        return Ofloxacin;
    }

    public char getRifampin() {
        return Rifampin;
    }

    public char getStreptomycin() {
        return Streptomycin;
    }

    public char getSpoligotype() {
        return Spoligotype;
    }

    public char getLineage() {
        return Lineage;
    }

    public String getgDSTPattern() {
        return gDSTPattern;
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

    public void setHIV(int HIV) {
        this.HIV = HIV;
    }

    public void setCohort(String cohort) {
        Cohort = cohort;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setSmear(int smear) {
        Smear = smear;
    }

    public void setSinglecolony(boolean singlecolony) {
        Singlecolony = singlecolony;
    }

    public void setpDSTPattern(String pDSTPattern) {
        this.pDSTPattern = pDSTPattern;
    }

    public void setCapreomycin(char capreomycin) {
        Capreomycin = capreomycin;
    }

    public void setEthamButol(char ethamButol) {
        EthamButol = ethamButol;
    }

    public void setEthionamide(char ethionamide) {
        Ethionamide = ethionamide;
    }

    public void setIsoniazid(char isoniazid) {
        Isoniazid = isoniazid;
    }

    public void setKanamycin(char kanamycin) {
        Kanamycin = kanamycin;
    }

    public void setPyrazinamide(char pyrazinamide) {
        Pyrazinamide = pyrazinamide;
    }

    public void setOfloxacin(char ofloxacin) {
        Ofloxacin = ofloxacin;
    }

    public void setRifampin(char rifampin) {
        Rifampin = rifampin;
    }

    public void setStreptomycin(char streptomycin) {
        Streptomycin = streptomycin;
    }

    public void setSpoligotype(char spoligotype) {
        Spoligotype = spoligotype;
    }

    public void setLineage(char lineage) {
        Lineage = lineage;
    }

    public void setgDSTPattern(String gDSTPattern) {
        this.gDSTPattern = gDSTPattern;
    }

    public void setXdr(String xdr) {
        this.xdr = xdr;
    }
}
