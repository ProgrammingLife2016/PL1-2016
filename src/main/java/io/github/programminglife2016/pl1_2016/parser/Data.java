package io.github.programminglife2016.pl1_2016.parser;

/**
 * Created by ravishivam on 4-5-16.
 */
public interface Data {
    /**
     * Get the name of the subject which is also the id.
     * @return Name of the subject.
     */
    String getNameId();

    /**
     * Get the age of the subject.
     * @return age of the subject
     */
    int getAge();

    /**
     * Get the gender of the subject.
     * @return True if subject is male, else false.
     */
    boolean isMale();

    /**
     * Get HIV status of the subject.
     * @return 1 if positive, -1 if negative and 0 if it's unknown.
     */
    int getHIV();


    /**
     * Getter for Cohort.
     *
     * @return java.lang.String value of Cohort
     */
    String getCohort();

    /**
     * Getter for Date.
     *
     * @return java.lang.String value of Date
     */
    String getDate();

    /**
     * Getter for District.
     *
     * @return java.lang.String value of District
     */
    String getDistrict();

    /**
     * Getter for Type.
     *
     * @return java.lang.String value of Type
     */
    String getType();

    /**
     * Getter for Smear.
     *
     * @return int value of Smear
     */
    int getSmear();

    /**
     * Getter for Singlecolony.
     *
     * @return boolean value of Singlecolony
     */
    boolean isSinglecolony();

    /**
     * Getter for pDSTPattern.
     *
     * @return java.lang.String value of pDSTPattern
     */
    String getpDSTPattern();

    /**
     * Getter for Capreomycin.
     *
     * @return char value of Capreomycin
     */
    char getCapreomycin();

    /**
     * Getter for EthamButol.
     *
     * @return char value of EthamButol
     */
    char getEthamButol();

    /**
     * Getter for Ethionamide.
     *
     * @return char value of Ethionamide
     */
    char getEthionamide();

    /**
     * Getter for Isoniazid.
     *
     * @return char value of Isoniazid
     */
    char getIsoniazid();

    /**
     * Getter for Kanamycin.
     *
     * @return char value of Kanamycin
     */
    char getKanamycin();

    /**
     * Getter for Pyrazinamide.
     *
     * @return char value of Pyrazinamide
     */
    char getPyrazinamide();

    /**
     * Getter for Ofloxacin.
     *
     * @return char value of Ofloxacin
     */
    char getOfloxacin();

    /**
     * Getter for Rifampin.
     *
     * @return char value of Rifampin
     */
    char getRifampin();

    /**
     * Getter for Streptomycin.
     *
     * @return char value of Streptomycin
     */
    char getStreptomycin();

    /**
     * Getter for Spoligotype.
     *
     * @return char value of Spoligotype
     */
    char getSpoligotype();

    /**
     * Getter for Lineage.
     *
     * @return char value of Lineage
     */
    char getLineage();

    /**
     * Getter for gDSTPattern.
     *
     * @return java.lang.String value of gDSTPattern
     */
    String getgDSTPattern();

    /**
     * Getter for xdr.
     *
     * @return java.lang.String value of xdr
     */
    String getXdr();

    /**
     * Setter for nameId.
     *
     * @nameId field that should be assigned to nameId.
     */
    void setNameId(String nameId);

    /**
     * Setter for age.
     *
     * @age field that should be assigned to age.
     */
    void setAge(int age);

    /**
     * Setter for isMale.
     *
     * @male field that should be assigned to isMale.
     */
    void setMale(boolean male);

    /**
     * Setter for HIV.
     *
     * @HIV field that should be assigned to HIV.
     */
    void setHIV(int HIV);

    /**
     * Setter for Cohort.
     *
     * @cohort field that should be assigned to Cohort.
     */
    void setCohort(String cohort);

    /**
     * Setter for Date.
     *
     * @date field that should be assigned to Date.
     */
    void setDate(String date);

    /**
     * Setter for District.
     *
     * @district field that should be assigned to District.
     */
    void setDistrict(String district);

    /**
     * Setter for Type.
     *
     * @type field that should be assigned to Type.
     */
    void setType(String type);

    /**
     * Setter for Smear.
     *
     * @smear field that should be assigned to Smear.
     */
    void setSmear(int smear);

    /**
     * Setter for Singlecolony.
     *
     * @singlecolony field that should be assigned to Singlecolony.
     */
    void setSinglecolony(boolean singlecolony);

    /**
     * Setter for pDSTPattern.
     *
     * @pDSTPattern field that should be assigned to pDSTPattern.
     */
    void setpDSTPattern(String pDSTPattern);

    /**
     * Setter for Capreomycin.
     *
     * @capreomycin field that should be assigned to Capreomycin.
     */
    void setCapreomycin(char capreomycin);

    /**
     * Setter for EthamButol.
     *
     * @ethamButol field that should be assigned to EthamButol.
     */
    void setEthamButol(char ethamButol);

    /**
     * Setter for Ethionamide.
     *
     * @ethionamide field that should be assigned to Ethionamide.
     */
    void setEthionamide(char ethionamide);

    /**
     * Setter for Isoniazid.
     *
     * @isoniazid field that should be assigned to Isoniazid.
     */
    void setIsoniazid(char isoniazid);

    /**
     * Setter for Kanamycin.
     *
     * @kanamycin field that should be assigned to Kanamycin.
     */
    void setKanamycin(char kanamycin);

    /**
     * Setter for Pyrazinamide.
     *
     * @pyrazinamide field that should be assigned to Pyrazinamide.
     */
    void setPyrazinamide(char pyrazinamide);

    /**
     * Setter for Ofloxacin.
     *
     * @ofloxacin field that should be assigned to Ofloxacin.
     */
    void setOfloxacin(char ofloxacin);

    /**
     * Setter for Rifampin.
     *
     * @rifampin field that should be assigned to Rifampin.
     */
    void setRifampin(char rifampin);

    /**
     * Setter for Streptomycin.
     *
     * @streptomycin field that should be assigned to Streptomycin.
     */
    void setStreptomycin(char streptomycin);

    /**
     * Setter for Spoligotype.
     *
     * @spoligotype field that should be assigned to Spoligotype.
     */
    void setSpoligotype(char spoligotype);

    /**
     * Setter for Lineage.
     *
     * @lineage field that should be assigned to Lineage.
     */
    void setLineage(char lineage);

    /**
     * Setter for gDSTPattern.
     *
     * @gDSTPattern field that should be assigned to gDSTPattern.
     */
    void setgDSTPattern(String gDSTPattern);

    /**
     * Setter for xdr.
     *
     * @xdr field that should be assigned to xdr.
     */
    void setXdr(String xdr);
}
