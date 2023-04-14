/**
* BemanningsKalkulator.java Trine Normann 04.11.2018
* Programmet viser en bemanningskalkulator for barnehager,
* hvor brukeren kan manipulere tall og finne ut om en
* barnehage oppfyller bemanningsnormen.
* Data for alle barnehager hentes fra tabellen 'barnehage' i databasen.
* Fila inneholder GUI og klientprogram (main).
*/

import static javax.swing.JOptionPane.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

@SuppressWarnings("serial")

/* GUI for bemanningskalkulatoren */
class KalkulatorGUI extends JFrame {
  private BhgDatabase databasen; // settes i klientprogrammet
  private DefaultListModel<Barnehage> listeinnhold = new DefaultListModel<Barnehage>();
  private JList<Barnehage> bhgListe = new JList<Barnehage>(listeinnhold);
  private static final double VEKT_SMAA = 2.0; // Småbarn teller dobbelt
  private static final double GRENSE = 6.0; // Grense antall barn per voksne
  private JButton beregnKnapp = new JButton("Beregn");
  private JLabel oppfyllerNorm = new JLabel("Velg en barnehage fra listen!");
  private JTextField antSmaabarn = new JTextField(5);
  private JTextField antStorebarn = new JTextField(5);
  private JTextField antVoksne = new JTextField(5);
  private JTextField gjsnittBarnPerVoksen = new JTextField(5);
  private Barnehage valgtBhg = null;

  public KalkulatorGUI(String tittel, BhgDatabase databasen) {
    this.databasen = databasen;
    setTitle(tittel);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    addWindowListener(new VindusLytter());

    ListePanel barnehagene = new ListePanel();
    add(barnehagene, BorderLayout.NORTH);
    GrunnlagsPanel grunnlaget = new GrunnlagsPanel();
    add(grunnlaget, BorderLayout.CENTER);
    ResultatPanel resultatet = new ResultatPanel();
    add(resultatet, BorderLayout.SOUTH);
    fyllListeMedData();
    setLocation(300,300);
    pack();
  }

  /* Fyller en liste med navn på alle barnehagene i databasen */
  private void fyllListeMedData() {
    listeinnhold.clear();
    ArrayList<Barnehage> alle = databasen.finnAlle();
    databasen.lukkForbindelse(); // Trenger ikke databasen etter at barnehagene er hentet ut
    if (alle != null) {
      for (Barnehage enBhg : alle) {
        listeinnhold.addElement(enBhg);
      }
      if (listeinnhold.size() > 0) {
        bhgListe.setSelectedIndex(0); // Default-valg
      } else {
        beregnKnapp.setEnabled(false);
      }
    } else {
      showMessageDialog(KalkulatorGUI.this, "Kunne ikke hente data fra databasen.");
    }
  }

  /* Visning av barnehagelisten */
  class ListePanel extends JPanel {
    public ListePanel() {
      //bhgListe.setPreferredSize(new Dimension(300,300)); // FlowLayout tar hensyn til dette. OBS dette begrenser antall verdier som vises i listen!
      JLabel ledetekst = new JLabel("Velg en barnehagefra listen: ");
      //add(ledetekst, BorderLayout.NORTH);
      bhgListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      JScrollPane rullefelt = new JScrollPane(bhgListe);
      add(rullefelt, BorderLayout.SOUTH);

      JViewport jvp = new JViewport();
      jvp.setView(ledetekst);
      rullefelt.setColumnHeader(jvp);
      add(rullefelt);

      ListeboksLytter lytter = new ListeboksLytter();
      bhgListe.addListSelectionListener(lytter);
      //pack();
    }
  }

  /* Visning av grunnlaget for beregningen */
  class GrunnlagsPanel extends JPanel {
    public GrunnlagsPanel() {
      JPanel nord = new JPanel();
      add(nord, BorderLayout.NORTH);
      nord.add(new JLabel("Antall barn 0-2 år"));
      antSmaabarn.setText("0");
      nord.add(antSmaabarn);
      antSmaabarn.setEditable(false);

      JPanel midten = new JPanel();
      add(midten, BorderLayout.CENTER);
      midten.add(new JLabel("Antall barn 3-6 år"));
      antStorebarn.setText("0");
      midten.add(antStorebarn);
      antStorebarn.setEditable(false);

      JPanel soer = new JPanel();
      add(soer, BorderLayout.SOUTH);
      soer.add(new JLabel("Antall årsverk voksne"));
      antVoksne.setText("0.0");
      soer.add(antVoksne);
      antVoksne.setEditable(false);

      pack();
    }
  }

  /* Visning av resultatet av beregningen */
  class ResultatPanel extends JPanel {
    public ResultatPanel() {
      JLabel barnPerVoksenTekst = new JLabel("Antall barn per voksen");
      add(barnPerVoksenTekst);
      gjsnittBarnPerVoksen.setText("  0");
      add(gjsnittBarnPerVoksen);
      gjsnittBarnPerVoksen.setEditable(false);
      add(oppfyllerNorm);
      beregnKnapp.setEnabled(false);
      add(beregnKnapp);
      KnappeLytter lytter = new KnappeLytter();
      beregnKnapp.addActionListener(lytter);
      pack();
    }
  }

  /* Hva som skjer hvis brukeren lukker vinduet */
  private class VindusLytter extends WindowAdapter {
    public void windowClosing(WindowEvent hendelse) {
      dispose();
      System.exit(0);
    }
  }

  /* Hva som skjer når brukeren velger en barnehage i listen */
  private class ListeboksLytter implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent hendelse) {
      valgtBhg = bhgListe.getSelectedValue();
      oppfyllerNorm.setText("Du har valgt: " + valgtBhg.getNavn());
      antSmaabarn.setText("" + valgtBhg.getAntSmaaBarn());
      antStorebarn.setText("" + valgtBhg.getAntStoreBarn());
      antVoksne.setText("" + valgtBhg.getAntAaarsverkVoksne());
      double gjsnitt = valgtBhg.beregnBarnPerVoksne();
      java.util.Formatter f = new java.util.Formatter();
      f.format("%.1f", gjsnitt);
      gjsnittBarnPerVoksen.setText(f.toString());
      f.close();
      if (gjsnitt > 0.0 && gjsnitt <= GRENSE) {
        oppfyllerNorm.setText(valgtBhg.getNavn() + " oppfyller bemanningsnormen.");
      } else if (gjsnitt > 0.0 && gjsnitt > GRENSE) {
        oppfyllerNorm.setText(valgtBhg.getNavn() + " oppfyller ikke bemanningsnormen.");
      } else {
        oppfyllerNorm.setText(valgtBhg.getNavn() + " mangler noen data.");
      }
      antSmaabarn.setEditable(true);
      antStorebarn.setEditable(true);
      antVoksne.setEditable(true);
      beregnKnapp.setEnabled(true);
    }
  }

  /* Hva som skjer hvis brukeren har skrevet inn egne tall */
  private class KnappeLytter implements ActionListener {
    public void actionPerformed(ActionEvent hendelse) {
      int antallSmaa = Integer.parseInt(antSmaabarn.getText());
      int antallStore = Integer.parseInt(antStorebarn.getText());
      double antallVoksne = Double.parseDouble(antVoksne.getText());
      if (antallSmaa > 0 || antallStore > 0 && antallVoksne > 0) {
        double gjsnitt = (antallSmaa * VEKT_SMAA + antallStore) / antallVoksne;
        java.util.Formatter f = new java.util.Formatter();
        f.format("%.1f", gjsnitt);
        gjsnittBarnPerVoksen.setText(f.toString());
        f.close();
        if (gjsnitt <= GRENSE) {
          oppfyllerNorm.setText(valgtBhg + " oppfyller bemanningsnormen.");
        } else {
          oppfyllerNorm.setText(valgtBhg + " oppfyller ikke bemanningsnormen.");
        }
      }
    }
  }
}

/* Klientprogram */
class BemanningsKalkulator {
  public static void main(String[] args) {
    /* Ber brukeren om påloggingsinfo slik at dataene kan hentes fra databasen */
    String databasenavn = showInputDialog("Oppgi navn på databasen som barnehagetabellen finnes i: ");
    String brukernavn = showInputDialog("Oppgi brukernavn til databasen: ");
    String passord = showInputDialog("Oppgi passord til databasen: ");

    /* Henter data fra databasen og viser dem frem i brukergrensesnittet */
    try {
      String dbNavn = "jdbc:mysql://localhost:3306/" + databasenavn + "?user=" + brukernavn + "&password=" + passord;
      BhgDatabase databasekontakt = new BhgDatabase(dbNavn);
      KalkulatorGUI mittVindu = new KalkulatorGUI("Bemanningskalkulator", databasekontakt);
      mittVindu.setVisible(true);
    } catch (Exception e) {
      showMessageDialog(null,
                  "Får ikke koblet opp mot databasen. \nFeilmelding: " + e);
    }
  }
}
