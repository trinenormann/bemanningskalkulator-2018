/**
* BhgDatabase.java Trine Normann 31.10.2018
* Programmet lar brukeren opprette tabellen 'barnehage' i en database,
* og fylle den med informasjon fra et barnehageregister.
* Det er mulig å hente ut informasjon om alle barnehagene.
*/

import java.sql.*;
import java.util.ArrayList;

class BhgDatabase {
  private Connection forbindelse;

  /* Laster JDBC-klassene og åpner databaseforbindelsen */
  public BhgDatabase(String dbNavn) throws Exception {
    try {
      forbindelse = DriverManager.getConnection(dbNavn);
    } catch (Exception e) {
      System.err.println("*** Feil oppstått: Konstruktør. ***");
      e.printStackTrace(System.err);
      throw e;
    }
  }

  /* Metode for å lukke resultatsettet */
  public static void lukkResSet(ResultSet res) {
    try {
      if (res != null) {
        res.close();
      }
    } catch (SQLException e) {
      System.err.println("*** Feil oppstått: lukkResSet(). ***");
      e.printStackTrace(System.err);
    }
  }

  /* Metode for å lukke sql-setningen */
  public static void lukkSetning(Statement stm) {
    try {
      if (stm != null) {
        stm.close();
      }
    } catch (SQLException e) {
      System.err.println("*** Feil oppstått: lukkSetning(). ***");
      e.printStackTrace(System.err);
    }
  }

  /* Metode for å lukke forbindelsen. Må kalles av klienten før programmet avsluttes. */
  public void lukkForbindelse() {
    try {
      if (forbindelse != null) {
        forbindelse.close();
      }
    } catch (SQLException e) {
      System.err.println("*** Feil oppstått: lukkForbindelse(). ***");
      e.printStackTrace(System.err);
    }
  }

  /* Metode som returnerer alle barnehagene i databasen */
  public ArrayList<Barnehage> finnAlle() {
    ArrayList<Barnehage> alle = new ArrayList<Barnehage>();
    String sqlsetning = "select * from barnehage order by navn";
    ResultSet res = null;
    Statement setning = null;
    try {
      setning = forbindelse.createStatement();
      res = setning.executeQuery(sqlsetning);
      while (res.next()) {
        String orgnr = res.getString("orgnr");
        String navn = res.getString("navn");
        int antSmaabarn = res.getInt("antSmaabarn");
        int antStorebarn = res.getInt("antStorebarn");
        double antAarsverkVoksne = res.getDouble("antAarsverkVoksne");
        Barnehage denne = new Barnehage(orgnr, navn, antSmaabarn, antStorebarn, antAarsverkVoksne);
        alle.add(denne);
      }
    } catch (SQLException e) {
      System.out.println("Kunne ikke hente data om alle barnehagene. Feilmelding: \n" + e);
    } finally {
      lukkResSet(res);
      lukkSetning(setning);
    }
    return alle;
  }

  /* Oppretter tabellen barnehage og legger inn data */
  public void lagBarnehagetabell(BarnehageRegister registeret) {
    try {
      Statement setning = forbindelse.createStatement();
      setning.executeUpdate("drop table if exists barnehage");
      System.out.println("Har slettet tabellen barnehage, eller den fantes ikke fra før");
      setning.executeUpdate("create table barnehage(orgnr varchar(9) primary key, navn varchar(100) not null, " +
        "antSmaabarn integer, antStorebarn integer, antAarsverkVoksne double)");
      System.out.println("Har laget tabellen barnehage");
      setning.close();

      int antRader = 0;
      PreparedStatement kompilertSetning = forbindelse.prepareStatement("insert into barnehage values(?,?,?,?,?)");
      for (int i = 0; i < registeret.finnAntallBarnehager(); i++) {
        kompilertSetning.setString(1, registeret.finnBarnehageIndeks(i).getOrgnr());
        kompilertSetning.setString(2, registeret.finnBarnehageIndeks(i).getNavn());
        kompilertSetning.setInt(3, registeret.finnBarnehageIndeks(i).getAntSmaaBarn());
        kompilertSetning.setInt(4, registeret.finnBarnehageIndeks(i).getAntStoreBarn());
        kompilertSetning.setDouble(5, registeret.finnBarnehageIndeks(i).getAntAaarsverkVoksne());
        antRader += kompilertSetning.executeUpdate();
      }
      System.out.println("Har lagt inn " + antRader + " rader i tabellen. ");
      kompilertSetning.close();
      System.out.println("Har lukket setningen.");
    } catch (SQLException e) {
      System.out.println("Noe gikk galt i arbeidet med tabellen. Feilmelding: \n" + e);
    }
  }
}
