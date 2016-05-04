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
}
