package de.fh.suche;

/**
 * Created by daniel on 29.09.16.
 * Generische Klasse für Bewertungen
 * Knoten haben bei heuristischen Suchen Bewertungen
 */
public abstract class Bewertung {

    /**
     * Berechnung des Schätzwertes eines Knoten x nach f(x)=g(x)+h(x) mit
     * g(x) die bisherigen Kosten vom Startknoten aus
     * h(x) die geschätzten Kosten von x bis zum Zielknoten
     * @return die heuristische Bewertung des Knoten x
     */
    abstract public float getSchaetzwert();

}
