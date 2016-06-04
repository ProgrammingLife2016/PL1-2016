// CHECKSTYLE.OFF: JavadocMethod
package io.github.programminglife2016.pl1_2016.parser.metadata;

/**
 * Metadata about a genome.
 */
public interface Subject {
    String getNameId();
    int getAge();
    boolean isMale();
    int getHivStatus();
    String getCohort();
    String getDate();
    String getDistrict();
    String getType();
    int getSmear();
    boolean isSingleColony();
    String getPdstpattern();
    String getCapreomycin();
    String getEthambutol();
    String getEthionamide();
    String getIsoniazid();
    String getKanamycin();
    String getPyrazinamide();
    String getOfloxacin();
    String getRifampin();
    String getStreptomycin();
    String getSpoligotype();
    String getLineage();
    String getGdstPattern();
    String getXdr();
    void setNameId(String nameId);
    void setAge(int age);
    void setMale(boolean male);
    void setHivStatus(int hivStatus);
    void setCohort(String cohort);
    void setDate(String date);
    void setDistrict(String district);
    void setType(String type);
    void setSmear(int smear);
    void setSingleColony(boolean singleColony);
    void setPdstpattern(String pdstPattern);
    void setCapreomycin(String capreomycin);
    void setEthambutol(String ethambutol);
    void setEthionamide(String ethionamide);
    void setIsoniazid(String isoniazid);
    void setKanamycin(String kanamycin);
    void setPyrazinamide(String pyrazinamide);
    void setOfloxacin(String ofloxacin);
    void setRifampin(String rifampin);
    void setStreptomycin(String streptomycin);
    void setSpoligotype(String spoligotype);
    void setLineage(String lineage);
    void setGdstPattern(String gdstPattern);
    void setXdr(String xdr);
}
