/**
* LagBhgDatabase.java Trine Normann 04.11.2018
* Programmet leser inn barnehageinfo fra fil og inn i et barnehageregister,
* og oppretter deretter en tabell med denne informasjonen i databasen.
* Programmet trenger ikke å kjøres mer etter at tabellen er opprettet i databasen.
*/

import java.io.*; // Til lesing av data fra fil
import static javax.swing.JOptionPane.*; // Til innlesing fra bruker

class LagBhgDatabase {
  public static void main(String[] args) throws IOException {

    /* Oppretter barnehageregister */
    BarnehageRegister registeret = new BarnehageRegister();

    /* Oppretter leseforbindelse til årsverkfila */
    String filnavn = "barnehager_aarsverk_grunnbemanning_orgnr.txt";
    FileReader leseforbTilFil = new FileReader(filnavn);
    BufferedReader leser = new BufferedReader(leseforbTilFil);

    /* Leser linje for linje fra fila, og legger barnehagene inn i registeret */
    String enLinje = leser.readLine(); // Overskriftsraden, trenger ikke den nå
    enLinje = leser.readLine();
    int antLinjer_aarsverk = 0;
    while (enLinje!=null) { // Så lenge slutten på fila ikke er nådd
      antLinjer_aarsverk++;
      String[] dataTab = enLinje.split("\\t");
      String nyttOrgnr = dataTab[0];
      String nyttNavn = dataTab[1];
      double nyttAarsverk = Double.parseDouble(dataTab[2]);
      registeret.leggTilBarnehage(new Barnehage(nyttOrgnr, nyttNavn, 0, 0, nyttAarsverk)); // Antall barn settes til 0, skal leses inn fra en annen fil
      enLinje = leser.readLine();
    }
    leser.close();

    System.out.println("Antall barnehager lest inn fra årsverkfila: " + antLinjer_aarsverk);

    /* Oppretter leseforbindelse til fila med antall barn */
    filnavn = "barnehager_barn_alder_orgnr_fikset.txt"; // Fil hvor blanke felt er erstattet med 0
    leseforbTilFil = new FileReader(filnavn);
    leser = new BufferedReader(leseforbTilFil);

    /* Leser linje for linje fra fila, og legger barnehagene inn i registeret */
    enLinje = leser.readLine(); // Overskriftsraden, trenger ikke den nå
    enLinje = leser.readLine();
    int antLinjer_alder = 0;
    int antLinjer_oppdatert = 0;
    while (enLinje!=null) { // Så lenge slutten på fila ikke er nådd
      antLinjer_alder++;
      String[] dataTab = enLinje.split("\\t");
      String nyttOrgnr = dataTab[0];
      //String nyttNavn = dataTab[1]; // Trenger ikke denne
      int nullaar = Integer.parseInt(dataTab[2]);
      int ettaar = Integer.parseInt(dataTab[3]);
      int toaar = Integer.parseInt(dataTab[4]);
      int treaar = Integer.parseInt(dataTab[5]);
      int fireaar = Integer.parseInt(dataTab[6]);
      int femaar = Integer.parseInt(dataTab[7]);
      int smaabarn = nullaar + ettaar + toaar;
      int storebarn = treaar + fireaar + femaar;
      Barnehage bhg = registeret.finnBarnehageOrgnr(nyttOrgnr);
      if (bhg != null) {
        bhg.setAntSmaaBarn(smaabarn);
        bhg.setAntStoreBarn(storebarn);
        antLinjer_oppdatert++;
      }
      enLinje = leser.readLine();
    }
    leser.close();

    System.out.println("Antall barnehager lest fra fil med alder: " + antLinjer_alder
                    + "\nAntall barnehager oppdatert i registeret: " + antLinjer_oppdatert);

    /* Ber brukeren om påloggingsinfo slik at dataene kan legges i databasen */
    String databasenavn = showInputDialog("Oppgi navn på databasen som barnehagetabellen skal opprettes i: ");
    String brukernavn = showInputDialog("Oppgi brukernavn til databasen: ");
    String passord = showInputDialog("Oppgi passord til databasen: ");

    /* Oppretter barnehagetabell i databasen */
    try {
      String dbNavn = "jdbc:mysql://localhost:3306/" + databasenavn + "?user=" + brukernavn + "&password=" + passord;
      BhgDatabase database = new BhgDatabase(dbNavn);
      System.out.println("Databaseforbindelsen er opprettet.");
      database.lagBarnehagetabell(registeret);
      database.lukkForbindelse();
      System.out.println("Databaseforbindelsen er lukket.");
    } catch (Exception e) {
      System.out.println("Noe gikk galt. \nFeilmelding: " + e);
    }
  }
}
