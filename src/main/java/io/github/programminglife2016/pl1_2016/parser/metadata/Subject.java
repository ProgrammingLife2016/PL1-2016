// CHECKSTYLE.OFF: JavadocMethod
package io.github.programminglife2016.pl1_2016.parser.metadata;

/**
 * Metadata about a genome.
 */
public interface Subject {
    String getNameId();
    int getAge();
    boolean is_isMale();
    int get_hivStatus();
    String getCohort();
    String getDate();
    String getDistrict();
    String getType();
    int getSmear();
    boolean isSingleColony();
    String getPdstpattern();
    char getCapreomycin();
    char getEthambutol();
    char getEthionamide();
    char getIsoniazid();
    char getKanamycin();
    char getPyrazinamide();
    char getOfloxacin();
    char getRifampin();
    char getStreptomycin();
    char getSpoligotype();
    char getLineage();
    String getGdstPattern();
    String getXdr();
    void setNameId(String nameId);
    void setAge(int age);
    void set_isMale(boolean _isMale);
    void set_hivStatus(int _hivStatus);
    void setCohort(String cohort);
    void setDate(String date);
    void setDistrict(String district);
    void setType(String type);
    void setSmear(int smear);
    void setSingleColony(boolean singleColony);
    void setPdstpattern(String pdstPattern);
    void setCapreomycin(char capreomycin);
    void setEthambutol(char ethambutol);
    void setEthionamide(char ethionamide);
    void setIsoniazid(char isoniazid);
    void setKanamycin(char kanamycin);
    void setPyrazinamide(char pyrazinamide);
    void setOfloxacin(char ofloxacin);
    void setRifampin(char rifampin);
    void setStreptomycin(char streptomycin);
    void setSpoligotype(char spoligotype);
    void setLineage(char lineage);
    void setGdstPattern(String gdstPattern);
    void setXdr(String xdr);
}
