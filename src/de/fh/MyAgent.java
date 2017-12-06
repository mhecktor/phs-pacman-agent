package de.fh;

import de.fh.agent.PacmanAgent;
import de.fh.pacman.PacmanPercept;
import de.fh.pacman.enums.PacmanAction;
import de.fh.pacman.enums.PacmanActionEffect;

import de.fh.agent.Agent;
import de.fh.pacman.enums.PacmanTileType;
import de.fh.suche.PacmanKnoten;
import de.fh.suche.PacmanSuche;
import de.fh.suche.Suche;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * DIESE KLASSE VERÄNDERN SIE BITTE NUR AN DEN GEKENNZEICHNETEN STELLEN
 * wenn die Bonusaufgabe bewertet werden soll.
 */
public class MyAgent extends PacmanAgent {

    private PacmanPercept percept;
    private PacmanActionEffect actionEffect;
    private PacmanSuche suche;
    private PacmanKnoten loesungsKonten;
    private LinkedBlockingQueue<PacmanAction> pacmanActionList;
    private PacmanTileType[][] view;
    private boolean worldChanged = true;

    public MyAgent(String name) {
		super(name);
	}

    public static void main(String[] args) {
        MyAgent agent = new MyAgent("");
		Agent.start(agent, "127.0.0.1", 5000);
    }

    /**
     * In dieser Methode kann das Wissen über die Welt (der State,  der Zustand)
     * entsprechend der aktuellen Wahrnehmungen anpasst, und die "interne Welt",
     * die Wissensbasis, des Agenten kontinuierlich ausgebaut werden.
     *
     * Wichtig: Diese Methode wird aufgerufen, bevor der Agent handelt, d.h.
     * bevor die chooseAction()-Methode aufgerufen wird...
     *
     * @param percept Aktuelle Wahrnehmung
     * @param actionEffect Reaktion des Servers auf vorhergewählte Aktion
     */
    @Override
        public void updateState(PacmanPercept percept, PacmanActionEffect actionEffect) {
        /**
         * Je nach Sichtbarkeit & Schwierigkeitsgrad (laut Serverkonfiguration)
         * aktuelle Wahrnehmung des Agenten.
         * Beim Pacman erhalten Sie je nach Level mehr oder weniger Mapinformationen.
         */
        this.percept = (PacmanPercept) percept;


        /*
         Aktuelle Reaktion des Server auf die letzte übermittelte Action.

         // Alle möglichen Serverrückmeldungen:
         if(actionEffect == PacmanActionEffect.GAME_INITIALIZED) {
         //Erster Aufruf
         }

         if(actionEffect == PacmanActionEffect.GAME_OVER) {
         //Das Spiel ist verloren
         }


         if(actionEffect == PacmanActionEffect.BUMPED_INTO_WALL) {
         //Letzte Bewegungsaktion führte in eine Wand
         }

         if(actionEffect == PacmanActionEffect.MOVEMENT_SUCCESSFUL) {
         //Letzte Bewegungsaktion war gültig
         }


         if(actionEffect == PacmanActionEffect.DOT_EATEN) {
         //Ein Dot wurde gefressen
         }
         */

        this.actionEffect = actionEffect;

        /*
        percept.getView() enthält die aktuelle Felderbelegung je nach Level/Sichtweite in einem Array

		Die Interpretationen der Werte können der public PacmanTileType--Klasse
		entnommen werden

        Für den Pacman sind folgende Felderbelegungen möglich

	    PacmanTileType.WALL
        PacmanTileType.DOT
        PacmanTileType.EMPTY
        PacmanTileType.PACMAN
        PacmanTileType.GHOST
        PacmanTileType.GHOST_AND_DOT
        PacmanTileType.GHOST_HUNTER
        PacmanTileType.GHOST_PASSIVE
        PacmanTileType.GHOST_RANDOM
		*/

        //Beispiel:
        view = this.percept.getView();


        //Gebe den aktuellen Zustand der Welt aus
        String view_row = "";
        System.out.println("viewsize: " + view.length + "*" + view[0].length);
        for (int x = 0; x < view[0].length; x++) {
            for (int y = 0; y < view.length; y++) {
                view_row += " " + view[y][x];
            }
            System.out.println(view_row);
            view_row = "";
        }
        System.out.println("-------------------------------");

	}

    /**
     * Unsere Action-Methode setzt sich (s. Russel / Norwig) hier aus zwei Teilen zusammen:
     * ChooseAction wählt die nächste(n) sinnvolle(n) Aktion(en), gemäß Regeln.
     * DoAct lässt den Agenten nach Lösungsweg laufen bzw. handeln.
     * @param
     * @return Die nächste Pacman-Action die vom Server ausgeführt werden soll
     */
    @Override
    public PacmanAction action() {


        /**
         * ChooseAction:
         * Diesen Part  erweitern Sie so, dass die nächste(n) sinnvolle(n) Aktion(en),
         * auf Basis der vorhandenen Zustandsinformationen und gegebenen Zielen, ausgeführt wird/werden.
         * Der chooseAction-Part soll den Agenten so intelligent wie möglich handeln lassen.
         *
         * Zum Beispiel:
         * Wenn die letzte Wahrnehmung ein Haufen von Dots in einem bestimmten Bereich der
         * Pacman-Welt ist, dann wird der Agent sicher eine intelligente Entscheidung treffen
         * können, welches das/die nächste zu betretende(n) Feld(er) ist/sind.
         *
         *
         * Mögliche PacmanActions sind möglich:
         * PacmanAction.GO_EAST
         * PacmanAction.GO_NORTH
         * PacmanAction.GO_SOUTH
         * PacmanAction.GO_WEST
         * PacmanAction.QUIT_GAME
         * PacmanAction.WAIT
         */

         /*Die Suche muss nicht nach jedem Zug ausgeführt werden, solange sich die Welt nicht ändert,
         * wie z.B. ein Ghost der gefährlich wird -> worldChanged = true!
         *
         */
        if (worldChanged) {

            /*
             * TODO [0] Gewünschte Suchstrategie einsetzen
             * Suche.TIEFENSUCHE
             * Suche.BREITENSUCHE
             * Suche.BESTENSUCHE
             * Suche.ASTERN
             * Suche.DIJKSTRA
             *
             */


            suche = new PacmanSuche(this.percept, defineGoal(view), Suche.ASTERN);

            //Hier startet die gewaehlte Suche.
            loesungsKonten = suche.start();


            if (loesungsKonten != null){



                //Für jeder Knoten wird der Weg, in Form von PacmanActions, von der Wurzel aus mitgeführt.
                this.pacmanActionList = new LinkedBlockingQueue<PacmanAction>();
                this.pacmanActionList.addAll(loesungsKonten.getPacmanActionList());
                //Die Suche soll im nächsten Zug nicht erneut durchgeführt werden,
                //sofern nicht notwendig
                worldChanged = false;
            } else
                System.out.println("Kein Lösungsweg gefunden");


        }



        /**
         * DoAct:
         * Hier lässt man den Pacman nach Lösungsweg laufen bzw. fressen.
         * Man greift (poll) auf die Pacmanactions (als Queue realisiert) zu,
         * die den Weg von der Wurzel zum Zielknoten (wird intern mitgeführt) beschreiben.
         **/

        if (!this.pacmanActionList.isEmpty()) {
            //Gebe die nächste Pacman-Action zurück
            nextAction = this.pacmanActionList.poll();
        } else {
            //Für eine reine Offlinesuche ist das Ziel erreicht, Spiel endet.
            nextAction = PacmanAction.QUIT_GAME;
        }

        /*
        Mögliche PacmanActions sind möglich:
        PacmanAction.GO_EAST
        PacmanAction.GO_NORTH
        PacmanAction.GO_SOUTH
        PacmanAction.GO_WEST
        PacmanAction.QUIT_GAME
        PacmanAction.WAIT
      */


        return nextAction;
	}

	/**
     * Die Ausgangswelt (view) wird variiert zu einem Zielzustand.
     * In unserem Fall ist der Zielzustand eine leer gefressene Welt (Könnte auch anders sein!)
     *
     * @param view Die Ausgangswelt (view)
     * @return Der Zielzustand in Form eines Knotens
     */
    public PacmanKnoten defineGoal(PacmanTileType[][] view){
    	PacmanTileType[][] zielview = new PacmanTileType[view.length][view[0].length];

        // Kopiere die Ausgangswelt und lösche alles ausser die Wände
        for (int spalte = 0; spalte < view.length; spalte++) {
            for (int zeile = 0; zeile < view[0].length; zeile++) {

                if (view[spalte][zeile] != PacmanTileType.WALL)
                    zielview[spalte][zeile] = PacmanTileType.EMPTY;
                else
                    zielview[spalte][zeile] = view[spalte][zeile];
            }
        }

        return new PacmanKnoten(zielview, null);
    }



}
