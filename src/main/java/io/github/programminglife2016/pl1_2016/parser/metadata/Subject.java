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
    Subject setNameId(String nameId);
    Subject setAge(int age);
    Subject setMale(boolean male);
    Subject setHivStatus(int hivStatus);
    Subject setCohort(String cohort);
    Subject setDate(String date);
    Subject setDistrict(String district);
    Subject setType(String type);
    Subject setSmear(int smear);
    Subject setSingleColony(boolean singleColony);
    Subject setPdstpattern(String pdstPattern);
    Subject setCapreomycin(String capreomycin);
    Subject setEthambutol(String ethambutol);
    Subject setEthionamide(String ethionamide);
    Subject setIsoniazid(String isoniazid);
    Subject setKanamycin(String kanamycin);
    Subject setPyrazinamide(String pyrazinamide);
    Subject setOfloxacin(String ofloxacin);
    Subject setRifampin(String rifampin);
    Subject setStreptomycin(String streptomycin);
    Subject setSpoligotype(String spoligotype);
    Subject setLineage(String lineage);
    Subject setGdstPattern(String gdstPattern);
    Subject setXdr(String xdr);
}
