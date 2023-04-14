/**
* Barnehage.java Trine Normann 02.11.2018
* Lar klienten opprette barnehageobjekter med orgnr, navn,
* antall barn og antall voksne.
* Man kan beregne antall barn per voksen.
*/

class Barnehage {
  private String orgnr; // Barnehagens BEDR-organisasjonsnummer
  private String navn; // Barnehagens navn
  private int antSmaabarn; // Barn 0-2 år
  private int antStorebarn; // Barn 3-6 år
  private double antAarsverkVoksne; // Antall årsverk voksne i grunnbemanningen i barnehagen
  private static final double VEKT_SMAA = 2.0; // Småbarn teller dobbelt

  /* Konstruktør */
  public Barnehage(String orgnr, String navn, int antSmaabarn, int antStorebarn, double antAarsverkVoksne) {
    if (orgnr == null || orgnr == "" || navn == null || navn == "" || antSmaabarn < 0 || antStorebarn < 0 || antAarsverkVoksne < 0.0) {
      throw new IllegalArgumentException("Ett eller flere av argumentene mangler. Skal være String orgnr, String navn, int antSmaabarn, int antStorebarn, double antAarsverkVoksne");
    }
    this.orgnr = orgnr;
    this.navn = navn;
    this.antSmaabarn = antSmaabarn;
    this.antStorebarn = antStorebarn;
    this.antAarsverkVoksne = antAarsverkVoksne;
  }

  public String getOrgnr() {
    return orgnr;
  }

  public String getNavn() {
    return navn;
  }

  public int getAntSmaaBarn() {
    return antSmaabarn;
  }

  public int getAntStoreBarn() {
    return antStorebarn;
  }

  public double getAntAaarsverkVoksne() {
    return antAarsverkVoksne;
  }

  public void setAntSmaaBarn(int antall) {
    antSmaabarn = antall;;
  }

  public void setAntStoreBarn(int antall) {
    antStorebarn = antall;
  }

  public void setAntAaarsverkVoksne(double antall) {
    antAarsverkVoksne = antall;
  }

  /*
  * Beregner antall barn per årsverk voksne i barnehagen.
  * Returnerer 0.0 hvis det er registrert 0 barn eller 0 voksne.
  */
  public double beregnBarnPerVoksne() {
    double barnPerVoksne = 0.0;
    if ((antSmaabarn > 0 || antStorebarn > 0) && antAarsverkVoksne > 0.0) {
      barnPerVoksne = (antSmaabarn * VEKT_SMAA + antStorebarn) / antAarsverkVoksne;
    }
    return barnPerVoksne;
  }

  /* To barnehager er like hvis de har samme organisasjonsnummer. */
  public boolean equals(Barnehage denAndre) {
    if (denAndre.getOrgnr().equals(orgnr)) {
      return true;
    }
    return false;
  }

  /*
  * Enkel variant av toString som kun returnerer navn.
  */
  public String toString() {
    return navn;
  }
}
