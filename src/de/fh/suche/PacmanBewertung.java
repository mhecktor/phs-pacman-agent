package de.fh.suche;

/**
 * Created by daniel on 29.09.16.
 * Definiert die Bewertungskriterien für die Pacman-Welt
 */
public class PacmanBewertung extends Bewertung{

    //Die Pfadkosten werden bei dem Schätzwert berücksichtigt
    private float pfadKosten;

    //Die Gesamtanzahl an Dots werden bei dem Schätzwert berücksichtigt
    private int anzahlDots;


    public float getPfadKosten() {
        return pfadKosten;
    }

    public void setPfadKosten(float pfadKosten) {
        this.pfadKosten = pfadKosten;
    }

    public int getAnzahlDots() {
        return anzahlDots;
    }

    public void setAnzahlDots(int anzahlDots) {
        this.anzahlDots = anzahlDots;
    }

    /**
     * Berechnung des Schätzwertes eines Knoten x nach f(x)=g(x)+h(x) mit
     * g(x) die bisherigen Kosten vom Startknoten aus
     * h(x) die geschätzten Kosten von x bis zum Zielknoten
     * @return die heuristische Bewertung des Knoten x
     */
    @Override
    public float getSchaetzwert() {
        return pfadKosten + anzahlDots;
    }
}
