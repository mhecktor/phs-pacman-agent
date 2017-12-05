package de.fh.suche;

import de.fh.pacman.PacmanPercept;
import de.fh.pacman.enums.PacmanAction;
import de.fh.pacman.enums.PacmanTileType;
import de.fh.util.Vector2;

/**
 * Created by daniel on 22.09.16.
 *
 * /**
 * Die Pacmansuche findet einen definierten Zielzustand (z.B. alle Dots gefressen)
 * auf Basis einer Ausgangssituation und einer gewünschten Suche.
 * Ist sie einmal initialisiert, startet man die gewählte Suche mit der start-Methode.
 *
 */
public class PacmanSuche extends Suche {

    private PacmanPercept pacmanPercept;
    private PacmanKnoten zielKnoten;
    private int suchStrategie;



    /**
     *
     * @param pacmanPercept Das Pacman-Percept beim ersten Aufruf -> Ausgangssituation
     * @param zielKnoten    Der gewünschte Zielzustand (z.B. alle Dots gefressen) als Knoten
     * @param suchStrategie hier Tiefensuche, Breitensuche, Bestensuche, A*, usw.
     */
    public PacmanSuche(PacmanPercept pacmanPercept, PacmanKnoten zielKnoten, int suchStrategie) {
        this.zielKnoten = zielKnoten;
        this.pacmanPercept = pacmanPercept;
        this.suchStrategie = suchStrategie;
    }

    /**
     * Ist die Suche fündig geworden, gibt die start-Methode den gefundenen Zielknoten zurück,
     * über den man sich dann wiederum die entsprechenden Pacman-Actions (vom Start zum bis zum Ziel),
     * über eine entsprechende Methode, holen kann
     *
     * @return Ziel(Pacman)knoten
     */
    @Override
    public PacmanKnoten start() {
        //Baue den Baum gemäß gewünschter Suche auf

        if (this.zielKnoten == null || this.pacmanPercept == null) {
            throw new NullPointerException("Ungültiger Zielzustand");
        }


        //Erzeuge Wurzelknoten
        this.KnotenEinfuegen(new PacmanKnoten(pacmanPercept.getView(), pacmanPercept.getPosition()));


        //Solange noch Expansionskandidaten vorhanden (Mindestens die Wurzel)
        while (!openList.isEmpty()) {

            PacmanKnoten expansionsKandidat = (PacmanKnoten) this.popOpenlist();
            this.pushCloselist(expansionsKandidat);

            //Schaue ob Knoten Ziel ist
            if (expansionsKandidat.isZiel(this.zielKnoten)) {
                //Kandidat entspricht dem geünschten Zielzustand
                PacmanKnoten loesungsKnoten = expansionsKandidat;
                return loesungsKnoten;
            } else {
                //Ist nicht gleich dem Zielzustand, also expandiere nächsten Knoten
                expandiereKnoten(expansionsKandidat);

            }
        }

        return null;
    }

    private void expandiereKnoten(PacmanKnoten vorgaenger) {
        /**
         * Die Nachfolgerknoten werden der Reihe nach in die Openlist
         * verschoben. Bei dieser Reihenfolge wird beim nächsten expandieren
         * immer der nördliche, dann der östliche, usw. angeschaut
         */

        // West
        nachfolgerBerechnen(PacmanAction.GO_WEST, vorgaenger);

        // South
        nachfolgerBerechnen(PacmanAction.GO_SOUTH, vorgaenger);

        // East
        nachfolgerBerechnen(PacmanAction.GO_EAST, vorgaenger);

        // Nord
        nachfolgerBerechnen(PacmanAction.GO_NORTH, vorgaenger);

    }

    private void nachfolgerBerechnen(PacmanAction bewegungsRichtung, PacmanKnoten vorgaenger) {
    	//Ist die neue Postion eine Wandposition kann man sich das Erzeugen
    	//des neuen Knoten und das Prüfen ob er sich schon in der closedList enthalten ist sparen
    	Vector2 pos = vorgaenger.berechneNeuePosition(vorgaenger.getPos(), bewegungsRichtung);
    	if (vorgaenger.getView()[pos.getX()][pos.getY()] == PacmanTileType.WALL)
    		return;

        //Erzeuge Nachfolgerknoten nach gewünschter Bewegungsrichtung
        PacmanKnoten nachfolger = new PacmanKnoten(vorgaenger, bewegungsRichtung);


        boolean isInClosedlist = false;

        //Durchsuche Closelist ob es diesen Zustand (Zustand der Welt und Pacman-Position) schon mal gab
        for (Knoten knoten : closedList) {
            PacmanKnoten pacmanKnoten = (PacmanKnoten) knoten;
            if (nachfolger.isGleich(pacmanKnoten)) {
                //Zustand ist gleich, also nicht erneut in die Openlist aufnehmen (sonst Loop!)
                isInClosedlist = true;
            }
        }

        //Ist die neue Postion des neuen Knoten eine Wandposition oder ist er schon Closelist...
        if (vorgaenger.getView()[nachfolger.getPos().getX()][nachfolger.getPos().getY()] == PacmanTileType.WALL ||
                isInClosedlist) {
            //... dann ist es kein gültiger Nachfolgezustand
            nachfolger = null;
        }

        if (nachfolger != null) {
            //Knoten wird gemaess der Suchstrategie bewertet
            KnotenBewerten(nachfolger);
            //Es ist ein gültiger Nachfolgezustand, also in die Openlist
            KnotenEinfuegen(nachfolger);
        }
    }




    /**
     * Hautfunktion zum Einfügen eines Knoten in die Openlist
     * Diese unterscheidet dann zwischen den Suchalgorithmen
     *
     * @param expansionsKandidat
     */
    @Override
    protected void KnotenEinfuegen(Knoten expansionsKandidat) {
        if(expansionsKandidat == null)
            throw new NullPointerException("Ungültiger Expansionskandidat");

        switch (suchStrategie) {
            case TIEFENSUCHE:
                KnotenEinfuegen_TS((PacmanKnoten) expansionsKandidat);
                break;
            case BREITENSUCHE:
                KnotenEinfuegen_BS((PacmanKnoten) expansionsKandidat);
                break;
            case DIJKSTRA:
                KnotenEinfuegen_Dijkstra((PacmanKnoten) expansionsKandidat);
                break;
            case BESTENSUCHE:
                KnotenEinfuegen_BFS((PacmanKnoten) expansionsKandidat);
                break;
            case ASTERN:
                KnotenEinfuegen_AS((PacmanKnoten) expansionsKandidat);
                break;
        }
    }


    /**
     * Konkrete Implentierung des Einfügens eines Knoten in
     * die Openlist bei der Tiefensuche
     *
     * @param expansionsKandidat
     */
    private void KnotenEinfuegen_TS(PacmanKnoten expansionsKandidat) {

        //Beispiel Tiefensuche
        //Implementiert openList.add(Index,exp), mit dem richtigen Index gemäß Suchstrategie
        openList.add(0,expansionsKandidat);
    }

    /**
     * Konkrete Implentierung des Einfügens eines Knoten in
     * die Openlist bei der Breitensuche
     *
     * @param expansionsKandidat
     */
	private void KnotenEinfuegen_BS(PacmanKnoten expansionsKandidat) {

		//DONE Breitensuche
        openList.add(expansionsKandidat);
        //Implementiert openList.add(Index,exp) mit dem richtigen Index gemäß Suchstrategie
	}

    /**
     * Konkrete Implentierung des Einfügens eines Knoten in
     * die Openlist beim Dijkstra-Algorithmus
     *
     * @param expansionsKandidat
     */
	private void KnotenEinfuegen_Dijkstra(PacmanKnoten expansionsKandidat) {

        //DONE Dijkstra
        openList.add(expansionsKandidat);
        openList.sort((x,y) -> Float.compare(x.bewertung.getSchaetzwert(), y.bewertung.getSchaetzwert()));
        //Implementiert openList.add(Index,exp) mit dem richtigen Index gemäß Suchstrategie
	}

    /**
     * Konkrete Implentierung des Einfügens eines Knoten in
     * die Openlist bei der Bestensuche
     *
     * @param expansionsKandidat
     */
	private void KnotenEinfuegen_BFS(PacmanKnoten expansionsKandidat) {

        //DONE Bestensuche
        //Implementiert openList.add(Index,exp) mit dem richtigen Index gemäß Suchstrategie
        openList.add(expansionsKandidat);
        openList.sort((x,y) -> Float.compare(x.bewertung.getSchaetzwert(), y.bewertung.getSchaetzwert()));
	}

    /**
     * Konkrete Implentierung des Einfügens eines Knoten in
     * die Openlist bei der A*-Suche
     *
     * @param expansionsKandidat
     */
	private void KnotenEinfuegen_AS(PacmanKnoten expansionsKandidat) {

		//TODO AStern
        //Implementiert openList.add(Index,exp) mit dem richtigen Index gemäß Suchstrategie
	}


    /**
     * Hauptfunktion zum Bewerten eines Knoten.
     * Diese unterscheidet dann zwischen den Suchalgorithmen
     *
     * @param expansionsKandidat
     */
    @Override
    protected void KnotenBewerten(Knoten expansionsKandidat) {

        if(expansionsKandidat == null)
            throw new NullPointerException("Ungültiger Expansionskandidat");

        switch (suchStrategie) {
            case BESTENSUCHE:
                KnotenBewerten_BFS((PacmanKnoten) expansionsKandidat);
                break;
            case ASTERN:
                KnotenBewerten_AS((PacmanKnoten) expansionsKandidat);
                break;
            case DIJKSTRA:
                KnotenBewerten_Dijkstra((PacmanKnoten) expansionsKandidat);
                break;
            default:
        }
    }

    /**
     * Konkrete Implentierung des Bewertens eines Knoten in
     * beim Dijkstra-Algortihmus
     *
     * @param expansionsKandidat
     */
    private void KnotenBewerten_Dijkstra(PacmanKnoten expansionsKandidat) {

        PacmanBewertung bewertung = expansionsKandidat.getBewertung();
        //TODO Dijkstra
        //Setzt die richtigen Attributwerte im Objekt 'bewertung'

        bewertung.setPfadKosten(expansionsKandidat.getBewertung().getPfadKosten() + 1);
        expansionsKandidat.setBewertung(bewertung);
    }

    /**
     * Konkrete Implentierung des Bewertens eines Knoten in
     * der Bestensuche-Suche
     *
     * @param expansionsKandidat
     */
	private void KnotenBewerten_BFS(PacmanKnoten expansionsKandidat) {

        PacmanBewertung bewertung = expansionsKandidat.getBewertung();
		//TODO Bestensuche
        //Setzt die richtigen Attributwerte im Objekt 'bewertung'

        if(expansionsKandidat.getVorgaenger().getVorgaenger() == null) {
            int dots = 0;
            for (int i = 0; i < expansionsKandidat.getView().length; i++) {
                for (int j = 0; j < expansionsKandidat.getView()[i].length; j++) {
                    if (expansionsKandidat.getView()[i][j] == PacmanTileType.DOT) {
                        dots++;
                    }
                }
            }
            bewertung.setAnzahlDots(dots);
        } else {
            int dots = expansionsKandidat.getVorgaenger().getBewertung().getAnzahlDots();
            if(expansionsKandidat.getVorgaenger().getView()[expansionsKandidat.getPos().getX()][expansionsKandidat.getPos().getY()] == PacmanTileType.DOT) {
                dots--;
            }
            bewertung.setAnzahlDots(dots);
        }
        expansionsKandidat.setBewertung(bewertung);
	}

    /**
     * Konkrete Implentierung des Bewertens eines Knoten inbranch
     * der A*-Suche
     *
     * @param expansionsKandidat
     */
	private void KnotenBewerten_AS(PacmanKnoten expansionsKandidat) {

		PacmanBewertung bewertung = expansionsKandidat.getBewertung();
		//TODO AStern
        //Setzt die richtigen Attributwerte im Objekt 'bewertung'


        expansionsKandidat.setBewertung(bewertung);
	}
}
