/**
* BarnehageRegister.java Trine Normann 21.05.2018
* Klassen oppretter en ArrayList med barnehager.
* Det er mulig å sortere barnehagene alfabetisk etter navn.
* Man kan finne en barnehage i registeret via navn, organisasjonsnummer eller indeks.
* Det finnes en metode for å finne antall barn per årsverk voksne i en gitt barnehage.
*/

import java.util.ArrayList;
import java.util.Collections; // Trengs til sorteringsmetodene

class BarnehageRegister {
  private ArrayList<Barnehage> barnehager = new ArrayList<Barnehage>();

  /* Legger til en barnehage i slutten av listen, hvis det ikke finnes en barnehage med samme organisasjonsnummer fra før. */
  public boolean leggTilBarnehage(Barnehage nyBhg) {
    if (nyBhg != null && finnBarnehageOrgnr(nyBhg.getOrgnr()) == null) {
      barnehager.add(nyBhg);
      return true;
    }
    return false;
  }

  /* Sorterer barnehagene etter navn. Forutsetter at vi har laget en Comparator-klasse for dette */
  public void sorterBarnehager() {
      Collections.sort(barnehager, new BarnehageKompNavn());
  }

  /* Finner den første barnehagen med oppgitt navn. Returnerer null hvis navnet ikke finnes. */
  public Barnehage finnBarnehageNavn(String bhgNavn) {
    if (bhgNavn != null && !bhgNavn.equals("")) {
      for (Barnehage enBarnehage : barnehager) {
        if (enBarnehage.getNavn().equals(bhgNavn)) {
          return enBarnehage;
        }
      }
    }
    return null;
  }

  /* Finner barnehagen med oppgitt orgnr. Returnerer null hvis den ikke finnes. */
  public Barnehage finnBarnehageOrgnr(String bhgOrgnr) {
    if (bhgOrgnr != null && !bhgOrgnr.equals("")) {
      for (Barnehage enBarnehage : barnehager) {
        if (enBarnehage.getOrgnr().equals(bhgOrgnr)) {
          return enBarnehage;
        }
      }
    }
    return null;
  }

  /* Finner barnehagen på oppgitt indeks. Returnerer null hvis den ikke finnes. */
  public Barnehage finnBarnehageIndeks(int indeks) {
    if (indeks >= 0 && indeks < barnehager.size()) {
      return barnehager.get(indeks);
    }
    return null;
  }

  /* Returnerer antall barnehager i registeret. */
  public int finnAntallBarnehager() {
    return barnehager.size();
  }

  /* Returnerer antall barn per årsverk voksne i barnehagen med oppgitt navn. */
  public double beregnBarnPerVoksne(Barnehage soekeBhg) {
    // Barnehage soekeBhg = finnBarnehageNavn(bhgNavn);
    double gjsnitt = 0.0;
    if (soekeBhg != null) {
      gjsnitt = soekeBhg.beregnBarnPerVoksne();
    }
    return gjsnitt;
  }

  public String toString() {
    String tekst = "";
    for (Barnehage enBarnehage : barnehager) {
      tekst += enBarnehage.toString() + "\n";
    }
    return tekst;
  }
}
