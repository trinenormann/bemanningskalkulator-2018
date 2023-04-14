/**
* TestBarnehageRegister.java Trine Normann 02.11.2018
* Enkle tester av klassene Barnehage og BarnehageRegister.
*
*/

class TestBarnehageRegister {
  public static void main(String[] args) {

    System.out.println("Totalt antall tester: 6");

    /* Oppretter et tomt barnehageregister */
    BarnehageRegister registeret = new BarnehageRegister();

    /* Legger til noen barnehager */
    boolean bhg1 = registeret.leggTilBarnehage(new Barnehage("123456789", "Bjerke barnehage", 24, 48, 16.0));
    boolean bhg2 = registeret.leggTilBarnehage(new Barnehage("234567891", "Grorud barnehage", 24, 48, 0.0));
    boolean bhg3 = registeret.leggTilBarnehage(new Barnehage("345678912", "Alna barnehage", 24, 48, 16.0));
    boolean bhg4 = registeret.leggTilBarnehage(new Barnehage("123456789", "Stovner barnehage", 24, 48, 16.0)); // False fordi orgnr finnes fra før
    boolean bhg5 = registeret.leggTilBarnehage(new Barnehage("987654321", "Bjerke barnehage", 24, 48, 12.0)); // True fordi det er OK å ha samme navn

    if (bhg1 == true && bhg2 == true && bhg3 == true && bhg4 == false && bhg5 == true) {
      System.out.println("Legg til barnehage: \tTest 1 OK");
    } else {
      System.out.println("Legg til barnehage: Minst en av barnehagene feilet");
    }

    if (registeret.finnAntallBarnehager() == 4) {
      System.out.println("Finn antall barnehager: Test 2 OK");
    } else {
      System.out.println("Finn antall barnehager: Returnerte feil antall");
    }

    String utskrift = registeret.toString();
    if (utskrift.equals("Bjerke barnehage\nGrorud barnehage\nAlna barnehage\nBjerke barnehage\n")) {
      System.out.println("Utskrift: \t\tTest 3 OK");
    } else {
      System.out.println("Utskrift: Utskriften er feil");
    }

    registeret.sorterBarnehager();
    String utskriftSortert = registeret.toString();
    if (utskriftSortert.equals("Alna barnehage\nBjerke barnehage\nBjerke barnehage\nGrorud barnehage\n")) {
      System.out.println("Utskrift sortert: \tTest 4 OK");
    } else {
      System.out.println("Utskrift sortert: Utskriften er feil");
    }

    Barnehage funnet1 = registeret.finnBarnehageNavn("Bjerke barnehage"); // Bjerke
    Barnehage funnet2 = registeret.finnBarnehageOrgnr("234567891"); // Grorud
    Barnehage funnet3 = registeret.finnBarnehageIndeks(0); // Alna
    Barnehage funnet4 = registeret.finnBarnehageIndeks(2); // Bjerke
    String alleFunnet = funnet1.toString() + funnet2.toString() + funnet3.toString() + funnet4.toString();
    if (alleFunnet.equals("Bjerke barnehageGrorud barnehageAlna barnehageBjerke barnehage")) {
      System.out.println("Finn barnehage: \tTest 5 OK");
    } else {
      System.out.println("Finn barnehage: Fant ikke alle barnehagene");
    }

    double gjsnitt1 = registeret.beregnBarnPerVoksne(funnet1);
    double gjsnitt2 = registeret.beregnBarnPerVoksne(funnet2);
    double gjsnitt3 = registeret.beregnBarnPerVoksne(funnet3);
    double gjsnitt4 = registeret.beregnBarnPerVoksne(funnet4);
    if (gjsnitt1 == 6.0 && gjsnitt2 == 0.0 && gjsnitt3 == 6.0 && gjsnitt4 == 8.0) {
      System.out.println("Beregn barn per voksne: Test 6 OK");
    } else {
      System.out.println("Beregn barn per voksne: Gjennomsnittet er feil");
    }
  }
}
