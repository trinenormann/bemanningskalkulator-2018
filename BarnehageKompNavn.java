/**
* BarnehageKompNavn.java Trine Normann 21.05.2018
* En Comparator-klasse for å sortere barnehager etter navn
*/
class BarnehageKompNavn implements java.util.Comparator<Barnehage> {
  public int compare(Barnehage b1, Barnehage b2) {
    return (b1.getNavn().compareTo(b2.getNavn()));
  }
}
