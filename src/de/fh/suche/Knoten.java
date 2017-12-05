package de.fh.suche;

/**
 * Created by daniel on 22.09.16.
 * Generische Klasse fur unsere Suchalgorithmen
 */
public abstract class Knoten {

    //Knoten haben bis auf die Wurzel Vorgänger
    protected Knoten vorgaenger;

    //Knoten sind bei heuristischen Suchen bewertet
    protected Bewertung bewertung;

    public Knoten(Bewertung bewertung){
        this.bewertung = bewertung;
    }

    public Knoten(Knoten vorgaenger, Bewertung bewertung){
        this.vorgaenger = vorgaenger;
        this.bewertung = bewertung;
    };

    public Knoten getVorgaenger() {
        return vorgaenger;
    }

    public Bewertung getBewertung() {
        return bewertung;
    }

    public void setBewertung(Bewertung bewertung) {
        this.bewertung = bewertung;
    }

    /**
     * Wann sind zwei Konten gleich
     *
     * @param knoten
     * @return
     */
    abstract public boolean isGleich(Knoten knoten);

    /**
     * Ist der Konten der Zielzustand
     *
     * @param knoten
     * @return
     */
    abstract public boolean isZiel(Knoten knoten);

}
